package vtrans.dynlib;

import java.util.Date;

import vtrans.dynlib.TranslateActivity.GuiCallBacks;
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
//		_textView = textView;
	}
	
	@Override
  public void onClick(View view)
  {
  	final String englishText = _translateActivity._englishText.getText().toString();
    Log.v("OnTranslateButtonClickListener onClick", englishText);
//    translateActivity._vtransApp._translationStopped = false;
    
    //from http://stackoverflow.com/questions/833768/java-code-for-getting-current-time
//    Date currentTime = new Date();
//    final String strCurrentTime = currentTime.getDay() + "-" + currentTime.getMonth() + " " + 
//  		currentTime.getHours() + ":" + currentTime.getMinutes() + ":" + currentTime.getSeconds();
//    _guiCallBacks.setGermanText("translating (started:" + strCurrentTime + ")");
    
    _translateActivity.setTranslateControlsState(false);
    _translateActivity._stopButton.setEnabled(true);
    
    Translater translater = new Translater(_guiCallBacks, englishText, _vTransDynLibJNI);
    Thread thread = new Thread( translater, "Translater");
    thread.start();
  }
}
