package vtrans;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import vtrans.ApkUtil;
import vtrans.dynlib.VTransDynLibJNI;
import android.app.Application;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

/** @see http://developer.android.com/reference/android/app/Application.html 
 * */
public class VTransApp
	extends android.app.Application
{
	ApkUtil _apkUtil = null;
	VTransDynLibJNI _vtransDynLibJNI = new VTransDynLibJNI();
	boolean _LogInTranslationEngine = false;
	String _rootDirectoryPath;
	private Thread thread;
	//TODO
	boolean testMode = true;
	boolean _runningOnEmulator = true;
	
	/** (non-Javadoc)
	 * @see android.app.Application#onCreate()
   * Seide: diese Methode wird einmal zu Beginn beim Start der Anwendung aufgerufen - hier kannst
   * du eine notwendige Initialisierung der Configdateien machen mit ApkUtils
	 */
	@Override
	public void onCreate()
	{
		super.onCreate();
		
		//http://stackoverflow.com/questions/2799097/how-can-i-detect-when-an-android-application-is-running-in-the-emulator
		_runningOnEmulator = "goldfish".equals(Build.HARDWARE);
//		if( isEmulator)
//			testMode = false;
		//TODO check if currently debugging: if yes: set _rootDirectoryPath to 
		// external storage (for extracting files)
		if( /*testMode*/ ! _runningOnEmulator )
		{
			//from http://stackoverflow.com/questions/1490869/how-to-get-vm-arguments-from-inside-of-java-application
			String testModeValue = System.getProperty("testMode");
			/** from
			 * http://stackoverflow.com/questions/3551821/android-write-to-sd-card-folder
			 */
			_rootDirectoryPath = Environment.getExternalStorageDirectory() + File.separator + 
				//TODO use "app_name" string 
				"VTrans";
		/** needs 
		 * uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" /
		 */			
		}
		else
			_rootDirectoryPath = getCacheDir().toString();
		Log.i("VTransApp onCreate", "using root dir: " + _rootDirectoryPath);
		_vtransDynLibJNI.setPathes(_rootDirectoryPath);
		
  }
	
	public void onTerminate()
	{
		Log.i("VTransDynLibJNI", "onTerminate beg");
		try
		{
	    //TODO change to "callFreeMemory()"
		  _vtransDynLibJNI.callFreeMemory();
		}
		catch(Exception e)
		{
		  //TODO show in GUI
		  Log.i("VTransApp::onTerminate", e.toString() );
		}
		Log.i("VTransDynLibJNI", "onTerminate end");
	}
	
	void initializeVTransInBackGround(
	//	final TextView tv,
	//	final String mainConfigFilePath,
	//	final String configurationDirFullPath,
	//	Button translateButton
			TranslateActivity.GuiCallBacks callbacks
			//, Handler handler
		)
	{
		Log.i("initializeVTransInBackGround", "before calling Init in bg thread");
    //from http://stackoverflow.com/questions/833768/java-code-for-getting-current-time
    final Date currentTime = new Date();
//    final String strCurrentTime = currentTime.getDay() + "-" + currentTime.getMonth() + " " + 
//  		currentTime.getHours() + ":" + currentTime.getMinutes() + ":" + currentTime.getSeconds();

		/** From S.Seide */
	  callbacks.setStatusText("Initializing Trans (begin:" + currentTime + ")"
			//+ java.lang.System.
			);
	  
		thread = new Thread(
			new VTransBackground(/*handler, */
				callbacks, 
				_vtransDynLibJNI,
				this),
			"VTransInitializer");
		thread.start();
		
	//	/** from "Professional Android™	Application Development", 
	//	* "Chapter 8: Working in the Background", "Creating New Threads" */
	//	Thread thread = new Thread(
	//		null, 
	//		new Runnable()
	//		{
	//			public void run() {
	//				initializeVTrans(
	//					);
	//			}
	//		}
	//		, "VTransInitializer");
	//	thread.start();
	}	
	
	// Method which does some processing in the background.
	void initializeVTrans(
	//	TextView tv,
	//	String mainConfigFilePath,
	//	String configurationDirFullPath,
	//	Button translateButton
		)
	{
		Log.i("initializeVTrans", "before calling Init");
		
//	  _initReturnCode = Init(
//			_mainConfigFilePath,
//			_configurationDirFullPath,
//			);
//		Log.i("initializeVTrans", "after calling Init");
//	  //"Synchronizing Threads for GUI Operations"
//	  handler.post(doUpdateGUI);
		Log.i("VTransDynLibJNI", "onCreate ret val of Init:" + 
				_vtransDynLibJNI.get_initReturnCode() );
	}
	
	public void setStatusText(String string) {
	  
  }

	public void possiblyCopyAssetFilesIntoCacheDirInBG(TranslateActivity.GuiCallBacks callbacks)
	{
		thread = new Thread(	new AssetFilesExtracter(callbacks, _apkUtil, this), "AssetFilesExtracter");
		thread.start();
  }
}
