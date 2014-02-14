package vtrans;

import vtrans.TranslateActivity.GuiCallBacks;
import android.util.Log;

public class ShowFileExtractionStatus
	implements Runnable
{
	GuiCallBacks _guiCallBacks;
	/** Must be volatile because it is intended to be read from another thread.*/
	private volatile boolean _run = true;
	ApkUtil _apkUtil;
	
	public ShowFileExtractionStatus(GuiCallBacks guiCallBacks,
			ApkUtil apkUtil)
	{
		_guiCallBacks = guiCallBacks;
		_apkUtil = apkUtil;
	}
	
	@Override
	public void run()
	{
		Log.d("ShowFileExtractionStatus", "run begin");
		do
		{
			_guiCallBacks.setDuration(_apkUtil.getNumBytesCopied() 
				//+ "/" + _apkUtil._fileSizeInBytes 
				+ " bytes copied");
			try {
	      Thread.sleep(1000);
      } catch (InterruptedException e) {
	      _guiCallBacks.setGermanText(e.toString() );
	      e.printStackTrace();
      }
		}while( _run );
		Log.d("ShowFileExtractionStatus", "run end");
	}
	
	public void stop() {
		Log.d("ShowFileExtractionStatus", "stop begin--setting run to false");
		_run = false;
		Log.d("ShowFileExtractionStatus", "stop end");
  }
}
