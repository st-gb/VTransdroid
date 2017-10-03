package vtrans;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;

public class OnEnglishTextChangedListener
//http://developer.android.com/reference/android/text/TextWatcher.html
  implements TextWatcher
{
  private TranslateActivity _translateActivity;

  public OnEnglishTextChangedListener(final TranslateActivity ta) {
    _translateActivity = ta;
  }

//  @Override
//  public boolean onKey(View v, int keyCode, KeyEvent event) {
//    return false;
//  }

  @Override
  public void beforeTextChanged(CharSequence s, int start, int count, int after) {
  }

  @Override
  public void onTextChanged(CharSequence s, int start, int before, int count) {
    _translateActivity.translate();
  }

  @Override
  public void afterTextChanged(Editable s) {
    // TODO Auto-generated method stub
    
  }

}
