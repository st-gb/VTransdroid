package vtrans.dynlib;

import android.util.Log;
import vtrans.dynlib.TranslateActivity.GuiCallBacks;

public class ShowTranslationStatus implements Runnable {

	private GuiCallBacks _guiCallBacks;
	private volatile boolean _run = true;
	private VTransDynLibJNI _vtransDynLibJNI;
	private Translater _translater;
	
	public ShowTranslationStatus(
			GuiCallBacks guiCallBacks, 
			VTransDynLibJNI	vtransDynLibJNI,
			Translater translater)
	{
		_guiCallBacks = guiCallBacks;
		_vtransDynLibJNI = vtransDynLibJNI;
		_translater = translater;
	}
	
	@Override
	public void run()
	{
		Log.d("ShowTranslationStatus", "run begin");
		StringBuffer item = new StringBuffer();
		String time = "";
		byte statusCode;
		while(_run)
		{
			Log.v("ShowTranslationStatus", "run before GetStatus");
			statusCode = _vtransDynLibJNI.GetStatus(item, time);
			
			final String statusMessage = VTransDynLibJNI.getStatusMessage(statusCode);
			String statusText = /*statusCode + */ statusMessage;
			if( item.length() > 0 )
				statusText += ":" + item + time;
			Log.v("ShowTranslationStatus", "setting status to " + statusText);
			_guiCallBacks.setGermanText(statusText);
			
			final java.util.Date currentTime = new java.util.Date();
			final long timeDiffInMs = currentTime.getTime() - _translater.
		    _timeBeforeTranslation.getTime();
			_guiCallBacks.setDuration( (timeDiffInMs / 1000) + "s," + (timeDiffInMs % 1000) + "ms" );
			/** Set StringBuffer to "" (empty string). */
			item.setLength(0);
			try {
				Log.v("ShowTranslationStatus", "run sleeping");
	      Thread.sleep(1000);
      } catch (InterruptedException e) {
//	      e.printStackTrace();
      	_guiCallBacks.setGermanText(e.toString() );
      }
		}
	}

	public void stop() {
		Log.d("ShowTranslationStatus", "stop");
		_run = false;
  }
}
