package vtrans;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Locale;

import vtrans.ApkUtil;
import vtrans.dynlib.VTransDynLibJNI;
import vtrans.dynlib.attributes.TranslatedText;
import vtrans.view.ColouredTextView;
import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

/** @see http://developer.android.com/reference/android/app/Application.html 
 * */
public class VTransApp
	extends android.app.Application
{
	private static final float _MAXIMUM_TEXT_HEIGHT_IN_PIXELS = 36.0f;
  private static final float _MINIMUM_TEXT_HEIGHT_IN_PIXELS = 15.0f;
  ApkUtil _apkUtil = null;
	VTransDynLibJNI _vtransDynLibJNI = new VTransDynLibJNI();
	boolean _LogInTranslationEngine = false;
	String _rootDirectoryPath;
	private Thread thread;
	//TODO
	boolean testMode = true;
	boolean _runningOnEmulator = true;
  protected android.view.View _latestGermanTranslationView = null;
  protected TranslatedText _translatedText;
  public float _minimumTextHeightInPixelsEditText;
  public float _maximumTextHeightInPixelsEditText;
	
  /** from "Professional Android Application Development (Wrox Programmer to Programmer)"
   *  "Chapter 6: Data Storage, Retrieval, and Sharing" 
   *  Step 6: "Add public static String values to use to identify the named 
   *  Shared Preference you’re going to create, and the keys it will use to 
   *  store each preference value." */
  public static final String USER_PREFERENCE = "USER_PREFERENCES";
  public static final String PREF_MIN_TEXT_HEIGHT_IN_PIXELS = "PREF_MIN_TEXT_HEIGHT_IN_PIXELS";
  public static final String PREF_MAX_TEXT_HEIGHT_IN_PIXELS = "PREF_MAX_TEXT_HEIGHT_IN_PIXELS";
  SharedPreferences _sharedPrefs;
  private float _initialMinimumTextHeightInPixels;
  private float _initialMaximumTextHeightInPixels;

  /** from http://stackoverflow.com/questions/5755460/how-to-change-the-default-language-of-android-emulator 
   *  @see WARNING: also changes the dictionary/ word suggestions while text 
   *  input? so that if user */
  void setLocale(final String localeName)
  {
    Locale locale = null;
    Configuration config=null;
     config = getBaseContext().getResources().getConfiguration();
    locale = new Locale(localeName);
    Locale.setDefault(locale);
    config.locale = locale;
  }
  
  void setGermanLocale()
  {
    setLocale("de");
  }
  
  private void setRootDirPath()
  {
    ////http://stackoverflow.com/questions/2799097/how-can-i-detect-when-an-android-application-is-running-in-the-emulator
    //_runningOnEmulator = "goldfish".equals(Build.HARDWARE);
    ////if( isEmulator)
    ////  testMode = false;
    //
    ////TODO check if currently debugging: if yes: set _rootDirectoryPath to 
    //// external storage (for extracting files)
    //if( /*testMode*/ ! _runningOnEmulator )
    //{
    //  //from http://stackoverflow.com/questions/1490869/how-to-get-vm-arguments-from-inside-of-java-application
    //  String testModeValue = System.getProperty("testMode");
    //  /** from
    //   * http://stackoverflow.com/questions/3551821/android-write-to-sd-card-folder
    //   */
    //  _rootDirectoryPath = Environment.getExternalStorageDirectory() + File.separator + 
    //    //TODO use "app_name" string 
    //    "VTrans";
    ///** needs 
    // * uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" /
    // */     
    //}
    //else
      _rootDirectoryPath = getCacheDir().toString();
  }
  
	/** (non-Javadoc)
	 * @see android.app.Application#onCreate()
   * Seide: diese Methode wird einmal zu Beginn beim Start der Anwendung aufgerufen - hier kannst
   * du eine notwendige Initialisierung der Configdateien machen mit ApkUtils
	 */
	@Override
	public void onCreate()
	{
		super.onCreate();
		
    /** Step 6*/
    _sharedPrefs = getSharedPreferences(USER_PREFERENCE, Activity.MODE_PRIVATE);
    storePreferencesIntoMemberVars();
//		setGermanLocale();
		
    setRootDirPath();
		Log.i("VTransApp onCreate", "using root dir: " + _rootDirectoryPath);
		_vtransDynLibJNI.setPathes(_rootDirectoryPath);
  }
	
  /** "Professional Android Application Development (Wrox Programmer to Programmer)"
   *  "Chapter 6: Data Storage, Retrieval, and Sharing"
   *  "8. Fill in the savePreferencesmethod to record the current preferences, 
   *  based on the UI selections, to the Shared Preference object." *
   */
  private void savePreferences() {
//    storeUIcontrolValuesInMemberVars();
    //TODO
    Editor editor = _sharedPrefs.edit();
    if( _minimumTextHeightInPixelsEditText != _initialMinimumTextHeightInPixels )
      editor.putFloat(PREF_MIN_TEXT_HEIGHT_IN_PIXELS, 
        _minimumTextHeightInPixelsEditText);
    if( _maximumTextHeightInPixelsEditText != _initialMaximumTextHeightInPixels )
      editor.putFloat(PREF_MAX_TEXT_HEIGHT_IN_PIXELS, 
        _maximumTextHeightInPixelsEditText);
    editor.commit();
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

  public boolean textHeightIsInRange(final float newTextSize) {
    final boolean textHeightIsInRange = 
      newTextSize > _minimumTextHeightInPixelsEditText &&
      newTextSize <= _maximumTextHeightInPixelsEditText;
    return textHeightIsInRange;
  }

  public void storePreferencesIntoMemberVars() {
    _initialMinimumTextHeightInPixels = _sharedPrefs.getFloat(
        PREF_MIN_TEXT_HEIGHT_IN_PIXELS, _MINIMUM_TEXT_HEIGHT_IN_PIXELS);
    _minimumTextHeightInPixelsEditText = _initialMinimumTextHeightInPixels;
    
    _initialMaximumTextHeightInPixels = _sharedPrefs.getFloat(
        PREF_MAX_TEXT_HEIGHT_IN_PIXELS, _MAXIMUM_TEXT_HEIGHT_IN_PIXELS);
    _maximumTextHeightInPixelsEditText = _initialMaximumTextHeightInPixels;
  }
}
