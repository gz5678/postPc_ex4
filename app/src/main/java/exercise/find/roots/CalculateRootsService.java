package exercise.find.roots;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class CalculateRootsService extends IntentService {


  public CalculateRootsService() {
    super("CalculateRootsService");
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    if (intent == null) return;
    long timeStartMs = System.currentTimeMillis();
    long numberToCalculateRootsFor = intent.getLongExtra("number_for_service", 0);
    if (numberToCalculateRootsFor <= 0) {
      Log.e("CalculateRootsService", "can't calculate roots for non-positive input" + numberToCalculateRootsFor);
      return;
    }

    long root1 = numberToCalculateRootsFor;
    long root2 = 1;
    for(int i = 2; i < Math.sqrt(numberToCalculateRootsFor); i++) {
      if (System.currentTimeMillis() - timeStartMs > 20000) {
        Intent failedIntent = new Intent();
        failedIntent.setAction("stopped_calculations");
        failedIntent.putExtra("original_number", numberToCalculateRootsFor);
        failedIntent.putExtra("time_until_give_up_seconds", 20);
        this.sendBroadcast(failedIntent);
      }
      if (numberToCalculateRootsFor % i == 0) {
        root1 = i;
        root2 = numberToCalculateRootsFor / i;
        break;
      }
    }
    Intent successIntent = new Intent();
    successIntent.setAction("found_roots");
    successIntent.putExtra("original_number", numberToCalculateRootsFor);
    successIntent.putExtra("time_until_roots_found", (System.currentTimeMillis() - timeStartMs) / 1000 );
    successIntent.putExtra("root1", root1);
    successIntent.putExtra("root2", root2);
    this.sendBroadcast(successIntent);

  }
}