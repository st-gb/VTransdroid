package vtrans;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;

public class OnEnglishTextChangedListener 
  implements OnKeyListener
{
  private TranslateActivity _translateActivity;

  public OnEnglishTextChangedListener(final TranslateActivity ta) {
    _translateActivity = ta;
  }

  @Override
  public boolean onKey(View v, int keyCode, KeyEvent event) {
//    _translateActivity.translate();
    return false;
  }

}
