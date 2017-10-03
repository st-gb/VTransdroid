package vtrans;

import android.util.Log;
import android.view.View;

public class OnStopButtonClickListener
	implements View.OnClickListener
{
	private VTransApp _vtransApp;
	private TranslateActivity _translateActivity;
	
	OnStopButtonClickListener(
		VTransApp vtransApp, 
		TranslateActivity translateActivity
		)
	{
		_translateActivity = translateActivity;
		_vtransApp = vtransApp;
	}
	
	@Override
	public void onClick(View arg0) {
		try {
			Log.d("OnStopButtonClickListener", "onClick begin");
      _vtransApp._vtransDynLibJNI.callStop();
//        _vtransApp._translationStopped = true;
    } catch (Exception e) {
      e.printStackTrace();
      _translateActivity._germanText.setText(e.toString() );
    }
		}
}
