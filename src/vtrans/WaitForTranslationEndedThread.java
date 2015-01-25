package vtrans;

public class WaitForTranslationEndedThread
  extends Thread
{

  private TranslateActivity _translateActivity;

  public WaitForTranslationEndedThread(TranslateActivity translateActivity) {
    _translateActivity = translateActivity;
  }

  public void run() {
    Thread th = _translateActivity.getTranslationThread();
    if( th != null )
    {
      try {
        th.join();
        _translateActivity.translate();
      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }
}
