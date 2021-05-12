package exercise.find.roots;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
public class MainActivityTest extends TestCase {

  @Test
  public void when_activityIsLaunching_then_theButtonShouldStartDisabled(){
    // create a MainActivity and let it think it's currently displayed on the screen
    MainActivity mainActivity = Robolectric.buildActivity(MainActivity.class).create().visible().get();

    // test: make sure that the "calculate" button is disabled
    Button button = mainActivity.findViewById(R.id.buttonCalculateRoots);
    assertFalse(button.isEnabled());
  }

  @Test
  public void when_activityIsLaunching_then_theEditTextShouldStartEmpty(){
    // create a MainActivity and let it think it's currently displayed on the screen
    MainActivity mainActivity = Robolectric.buildActivity(MainActivity.class).create().visible().get();

    // test: make sure that the "input" edit-text has no text
    EditText inputEditText = mainActivity.findViewById(R.id.editTextInputNumber);
    String input = inputEditText.getText().toString();
    assertTrue(input == null || input.isEmpty());
  }

  @Test
  public void when_userIsEnteringNumberInput_and_noCalculationAlreadyHappned_then_theButtonShouldBeEnabled(){
    // create a MainActivity and let it think it's currently displayed on the screen
    MainActivity mainActivity = Robolectric.buildActivity(MainActivity.class).create().visible().get();

    // find the edit-text and the button
    EditText inputEditText = mainActivity.findViewById(R.id.editTextInputNumber);
    Button button = mainActivity.findViewById(R.id.buttonCalculateRoots);

    // enter good number and check that button is enabled
    inputEditText.setText("1234567");
    assertTrue(button.isEnabled());
  }

  @Test
  public void when_mainActivityLaunches_then_progressBarIsHidden() {
    // create a MainActivity and let it think it's currently displayed on the screen
    MainActivity mainActivity = Robolectric.buildActivity(MainActivity.class).create().visible().get();

    // find the progress bar and check that it's hidden
    ProgressBar progressBar = mainActivity.findViewById(R.id.progressBar);
    assertEquals(progressBar.getVisibility(), View.GONE);
  }

  @Test
  public void when_insertGoodNumber_and_clickButton_then_progressBarIsDisplayed() {
    // create a MainActivity and let it think it's currently displayed on the screen
    MainActivity mainActivity = Robolectric.buildActivity(MainActivity.class).create().visible().get();

    // find the edit-text, button and progress bar
    EditText inputEditText = mainActivity.findViewById(R.id.editTextInputNumber);
    Button button = mainActivity.findViewById(R.id.buttonCalculateRoots);
    ProgressBar progressBar = mainActivity.findViewById(R.id.progressBar);

    // enter good number and click button
    inputEditText.setText("1234567");
    button.performClick();

    // assert that progress bar is visible
    assertEquals(progressBar.getVisibility(), View.VISIBLE);
  }

  @Test
  public void when_enterBadInput_then_buttonShouldBeDisabled() {
    // create a MainActivity and let it think it's currently displayed on the screen
    MainActivity mainActivity = Robolectric.buildActivity(MainActivity.class).create().visible().get();

    // find the edit-text and button
    EditText inputEditText = mainActivity.findViewById(R.id.editTextInputNumber);
    Button button = mainActivity.findViewById(R.id.buttonCalculateRoots);

    // enter bad input and check that button is disabled
    inputEditText.setText("17.3");
    assertFalse(button.isEnabled());
  }

  @Test
  public void when_enterGoodInput_and_deletingIt_then_buttonShouldBeEnabledAndThenDisabled() {
    // create a MainActivity and let it think it's currently displayed on the screen
    MainActivity mainActivity = Robolectric.buildActivity(MainActivity.class).create().visible().get();

    // find the edit-text and button
    EditText inputEditText = mainActivity.findViewById(R.id.editTextInputNumber);
    Button button = mainActivity.findViewById(R.id.buttonCalculateRoots);

    // enter good input and check that button is enabled
    inputEditText.setText("173");
    assertTrue(button.isEnabled());

    // enter bad input and check that button is disabled
    inputEditText.setText("17.3");
    assertFalse(button.isEnabled());
  }

  @Test
  public void when_enterInput_and_flipScreen_then_inputShouldRemain() {
    // create a MainActivity and let it think it's currently displayed on the screen
    MainActivity mainActivity = Robolectric.buildActivity(MainActivity.class).create().visible().get();

    String inputNum = "173";

    // find the edit-text and enter input
    EditText inputEditText = mainActivity.findViewById(R.id.editTextInputNumber);
    inputEditText.setText(inputNum);

    // flip screen
    mainActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    // check input is the same
    assertEquals(inputEditText.getText().toString(), inputNum);
  }

  @Test
  public void when_startingCalculation_then_buttonShouldBeDisabled() {
    // create a MainActivity and let it think it's currently displayed on the screen
    MainActivity mainActivity = Robolectric.buildActivity(MainActivity.class).create().visible().get();

    // find the edit-text and button
    EditText inputEditText = mainActivity.findViewById(R.id.editTextInputNumber);
    Button button = mainActivity.findViewById(R.id.buttonCalculateRoots);

    // enter good input and press button
    inputEditText.setText("2305843009213693951");
    button.performClick();

    // Check that button is disabled
    assertFalse(button.isEnabled());
  }

  @Test
  public void when_startingCalculation_and_receivingStoppedCalculation_then_buttonShouldBeEnabled() {
    // create a MainActivity and let it think it's currently displayed on the screen
    MainActivity mainActivity = Robolectric.buildActivity(MainActivity.class).create().visible().get();

    // find the edit-text and button
    EditText inputEditText = mainActivity.findViewById(R.id.editTextInputNumber);
    Button button = mainActivity.findViewById(R.id.buttonCalculateRoots);

    // enter good input and press button
    inputEditText.setText("2305843009213693951");
    button.performClick();

    // Send stopped_calculation broadcast
    Intent failedIntent = new Intent();
    failedIntent.setAction("stopped_calculations");
    failedIntent.putExtra("original_number", "2305843009213693951");
    failedIntent.putExtra("time_until_give_up_seconds", 20);
    RuntimeEnvironment.application.sendBroadcast(failedIntent);
    Shadows.shadowOf(Looper.getMainLooper()).idle();

    // Check that button is enabled
    assertTrue(button.isEnabled());
  }

  @Test
  public void when_startingCalculation_and_receivingStoppedCalculation_then_progressBarShouldDisappear() {
    // create a MainActivity and let it think it's currently displayed on the screen
    MainActivity mainActivity = Robolectric.buildActivity(MainActivity.class).create().visible().get();

    // find the edit-text, button and progress bar
    EditText inputEditText = mainActivity.findViewById(R.id.editTextInputNumber);
    Button button = mainActivity.findViewById(R.id.buttonCalculateRoots);
    ProgressBar progressBar = mainActivity.findViewById(R.id.progressBar);

    // enter good input and press button
    inputEditText.setText("2305843009213693951");
    button.performClick();

    // Send stopped_calculation broadcast
    Intent failedIntent = new Intent();
    failedIntent.setAction("stopped_calculations");
    failedIntent.putExtra("original_number", "2305843009213693951");
    failedIntent.putExtra("time_until_give_up_seconds", 20);
    RuntimeEnvironment.application.sendBroadcast(failedIntent);
    Shadows.shadowOf(Looper.getMainLooper()).idle();

    // Check that progress bar is not visible
    assertEquals(progressBar.getVisibility(), View.GONE);
  }
}