package vtrans.dynlib;

import java.util.Date;

import android.util.Log;
import vtrans.dynlib.TranslateActivity.GuiCallBacks;

/** Class that does the actual translation and GUI updates regarding 
 * translation. */
public class Translater
  implements Runnable
{

	private GuiCallBacks _guiCallBacks;
	private String _englishText;
  private VTransDynLibJNI _vTransDynLibJNI;
  java.util.Date _timeBeforeTranslation;

	public Translater(
    GuiCallBacks guiCallBacks, 
    String englishText, 
    VTransDynLibJNI vTransDynLibJNI
    )
	{
	  _guiCallBacks = guiCallBacks;
	  _englishText = englishText;
	  _vTransDynLibJNI = vTransDynLibJNI;
  }
	
	@Override
  public void run()
	{
		Log.d("Translater", "run begin");
//		_guiCallBacks.setTranslateControlsState(false);
		
		/** Get the current time. */
		_timeBeforeTranslation = new Date();
		
		ShowTranslationStatus sts = new ShowTranslationStatus(_guiCallBacks, _vTransDynLibJNI, this);
		Thread showTranslationStatusThread = new Thread(sts, 
				"ShowTranslationStatus");
		showTranslationStatusThread.start();
		
		//TODO update GUI synchronously but in GUI thread
		_guiCallBacks.setStopButtonEnabled(true);
		String xml = "";
		try
		{
//			_guiCallBacks.setEnglishTextEnabled(false);
			//TODO endless loop when "the car walks the cat"
			xml = _vTransDynLibJNI.callTranslate(_englishText);
		}
		catch(Throwable t)
		{
			_guiCallBacks.setGermanText(t.toString() );
			t.printStackTrace();
		}
    /** Get the current time*/
		java.util.Date timeAfterTranslation = new Date();
    //TODO update GUI synchronously but in GUI thread?
		_guiCallBacks.setStopButtonEnabled(false);

		sts.stop();
		try {
			Log.d("Translater", "run before join");
			/** Wait for thread to end. */
	    showTranslationStatusThread.join();
	    
			Log.v("Translater", "run after join");
  		String resultText = "";
  		if( _vTransDynLibJNI._translationStopped )
  		{
  			resultText = "translation has been stopped";
  		}
  		else
  		{
  			//serialize XML data to vector of <word, grammar part>
  			resultText = SAX2serializer.serializeXML(xml);
  		}
  		/** http://stackoverflow.com/questions/4927856/how-to-calculate-time-difference-in-java */
  		long timeDiffInMs = timeAfterTranslation.getTime() - 
		    _timeBeforeTranslation.getTime();
  		
      //TODO update GUI synchronously but in GUI thread?
  		_guiCallBacks.setDuration( (timeDiffInMs / 1000) + "s," + 
		    (timeDiffInMs % 1000) + "ms" );
  		_guiCallBacks.setGermanText(resultText);
  		_guiCallBacks.setTranslateControlsState(true);
  		
    } catch (InterruptedException e) {
	    //e.printStackTrace();
    	_guiCallBacks.setGermanText(e.toString() );
    }
    Log.d("Translater", "run end");
  }
}
