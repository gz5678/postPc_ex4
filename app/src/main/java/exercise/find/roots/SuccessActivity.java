package exercise.find.roots;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SuccessActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_success);

        Intent startedIntent = getIntent();
        long originalNumber = startedIntent.getLongExtra("original_number", -1);
        long root1 = startedIntent.getLongExtra("root1", -1);
        long root2 = startedIntent.getLongExtra("root2", -1);
        long calculationTimeInMs = startedIntent.getLongExtra("time_of_calculation", -1);

        // Transform milliseconds to seconds
        double calculationTimeInSeconds = calculationTimeInMs / 1000.0;

        TextView textView = findViewById(R.id.successTextView);
        String firstSentence = "The original number was: " + String.valueOf(originalNumber);
        String secondSentence = "The roots: " + String.valueOf(root1) + "x" +
                String.valueOf(root2) + "=" + String.valueOf(originalNumber);
        String thirdSentence = "Time of calculation: " + String.valueOf(calculationTimeInSeconds) + " seconds";
        String completeSentence = firstSentence + "\n" + secondSentence + "\n" + thirdSentence;
        textView.setText(completeSentence);
    }
}
