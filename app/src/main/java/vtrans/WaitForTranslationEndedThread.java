package vtrans;

import android.util.Log;

public class WaitForTranslationEndedThread
  extends Thread
{

  private TranslateActivity _translateActivity;

  public WaitForTranslationEndedThread(TranslateActivity translateActivity) {
    super("WaitForTranslationEndedThread");
    _translateActivity = translateActivity;
  }

  public void run() {
    Log.d("WaitForTranslationEndedThread", "run begin");
    Thread th = _translateActivity.getTranslationThread();
    if( th != null )
    {
      try {
        /** Wait for the translation thread to finish. */
        Log.d("WaitForTranslationEndedThread", "Wait for the translation thread to finish");
        th.join();
        Log.d("WaitForTranslationEndedThread", "translation thread finished->calling translate");
        _translateActivity.runOnUiThread(new Runnable()
          {
            @Override
            public void run()
            {
              _translateActivity.translate();
            }
          });
      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }
}
