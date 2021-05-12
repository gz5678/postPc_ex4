package exercise.find.roots;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

  private BroadcastReceiver broadcastReceiverForSuccess = null;
  private BroadcastReceiver broadcastReceiverForFailure = null;
  private long startCalculationTime = System.currentTimeMillis();


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    ProgressBar progressBar = findViewById(R.id.progressBar);
    EditText editTextUserInput = findViewById(R.id.editTextInputNumber);
    Button buttonCalculateRoots = findViewById(R.id.buttonCalculateRoots);

    // set initial UI:
    progressBar.setVisibility(View.GONE); // hide progress
    editTextUserInput.setText(""); // cleanup text in edit-text
    editTextUserInput.setEnabled(true); // set edit-text as enabled (user can input text)
    buttonCalculateRoots.setEnabled(false); // set button as disabled (user can't click)

    // set listener on the input written by the keyboard to the edit-text
    editTextUserInput.addTextChangedListener(new TextWatcher() {
      public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
      public void onTextChanged(CharSequence s, int start, int before, int count) { }
      public void afterTextChanged(Editable s) {
        // text did change
        String newText = editTextUserInput.getText().toString();
        try {
          long num = Long.parseLong(newText);
          if (num <= 0) throw new NumberFormatException();
          buttonCalculateRoots.setEnabled(true);
        }
        catch (NumberFormatException e) {
          buttonCalculateRoots.setEnabled(false);
        }
      }
    });

    // set click-listener to the button
    buttonCalculateRoots.setOnClickListener(v -> {
      Intent intentToOpenService = new Intent(MainActivity.this, CalculateRootsService.class);
      String userInputString = editTextUserInput.getText().toString();
      // No bad input can reach here since user is constrained to long positive integers.
      long userInputLong = Long.parseLong(userInputString);
      intentToOpenService.putExtra("number_for_service", userInputLong);
      startService(intentToOpenService);
      startCalculationTime = System.currentTimeMillis();
      buttonCalculateRoots.setEnabled(false);
      editTextUserInput.setEnabled(false);
      progressBar.setVisibility(View.VISIBLE);
    });

    // register a broadcast-receiver to handle action "found_roots"
    broadcastReceiverForSuccess = new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent incomingIntent) {
        if (incomingIntent == null || !incomingIntent.getAction().equals("found_roots")) return;
        // success finding roots!
        progressBar.setVisibility(View.GONE);
        editTextUserInput.setEnabled(true);
        buttonCalculateRoots.setEnabled(true);
        Intent intentForSuccess = new Intent(context, SuccessActivity.class);
        intentForSuccess.putExtra("original_number", incomingIntent.getLongExtra("original_number", -1));
        intentForSuccess.putExtra("root1", incomingIntent.getLongExtra("root1", -1));
        intentForSuccess.putExtra("root2", incomingIntent.getLongExtra("root2", -1));
        intentForSuccess.putExtra("time_of_calculation", System.currentTimeMillis() - startCalculationTime);
        context.startActivity(intentForSuccess);
      }
    };
    registerReceiver(broadcastReceiverForSuccess, new IntentFilter("found_roots"));

    broadcastReceiverForFailure = new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent incomingIntent) {
        if (incomingIntent == null || !incomingIntent.getAction().equals("stopped_calculations")) return;
        // got timeout on finding roots
        progressBar.setVisibility(View.GONE);
        editTextUserInput.setEnabled(true);
        buttonCalculateRoots.setEnabled(true);
        int timeUntilTimeout = (int) incomingIntent.getIntExtra("time_until_give_up_seconds", -1);
        String msg = "Calculation aborted after " + timeUntilTimeout + " seconds";
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
      }
    };
    registerReceiver(broadcastReceiverForFailure, new IntentFilter("stopped_calculations"));
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    this.unregisterReceiver(broadcastReceiverForSuccess);
    this.unregisterReceiver(broadcastReceiverForFailure);
  }

  @Override
  protected void onSaveInstanceState(@NonNull Bundle outState) {
    super.onSaveInstanceState(outState);
    EditText editTextUserInput = findViewById(R.id.editTextInputNumber);
    Button buttonCalculateRoots = findViewById(R.id.buttonCalculateRoots);
    ProgressBar progressBar = findViewById(R.id.progressBar);
    outState.putString("number_in_text_field", editTextUserInput.getText().toString());
    outState.putBoolean("is_button_on", buttonCalculateRoots.isEnabled());

    boolean isCalculation = progressBar.getVisibility() != View.GONE;
    outState.putBoolean("is_calculation", isCalculation);
  }

  @Override
  protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
    EditText editTextUserInput = findViewById(R.id.editTextInputNumber);
    Button buttonCalculateRoots = findViewById(R.id.buttonCalculateRoots);
    ProgressBar progressBar = findViewById(R.id.progressBar);

    editTextUserInput.setText(savedInstanceState.getString("number_in_text_field"));
    if(savedInstanceState.getBoolean("is_calculation")) {
      progressBar.setVisibility(View.VISIBLE);
      buttonCalculateRoots.setEnabled(false);
      editTextUserInput.setEnabled(false);
    }
    else {
      progressBar.setVisibility(View.GONE);
      buttonCalculateRoots.setEnabled(savedInstanceState.getBoolean("is_button_on"));
      editTextUserInput.setEnabled(true);
    }
  }
}