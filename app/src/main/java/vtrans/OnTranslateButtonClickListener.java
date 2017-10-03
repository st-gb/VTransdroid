package vtrans;

import java.util.Date;

import vtrans.TranslateActivity.GuiCallBacks;
import vtrans.dynlib.VTransDynLibJNI;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class OnTranslateButtonClickListener
  implements OnClickListener
{

//	TextView _textView;
	private VTransDynLibJNI _vTransDynLibJNI;
  private GuiCallBacks _guiCallBacks;
  private TranslateActivity _translateActivity;
	
	public OnTranslateButtonClickListener(//TextView textView
			VTransDynLibJNI vTransDynLibJNI,
			TranslateActivity translateActivity)
	{
		this._vTransDynLibJNI = vTransDynLibJNI;
		_translateActivity = translateActivity;
		_guiCallBacks = translateActivity.callbacks;
    Log.v(this.getClass().getName(), "OnTranslateButtonClickListener end");
//		_textView = textView;
	}
	
	@Override
  public void onClick(View view)
  {
	  _translateActivity.translate();
  }
}
