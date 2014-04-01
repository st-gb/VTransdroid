/**
 * 
 */
package vtrans.dynlib;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Iterator;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

/** zuerst wird aufgerufen: onCreate(), dann onResume(),
 *  wenn Anwendung in den Hintergrund: onPause()
 *  wenn Anwendung im Vordergrund: onResume()
 *  beim Beenden "onStop()" 
 *  
 *  Ordner "res/drawable-[]dpi: für verschiedene Auflösungen
 *     wenn nur 1 Bild, dann mögliche eine hohe Auflösung ("xhdpi") empfohlen, 
 *     die dann runterskaliert wird
 *     anstatt eine niedrige, die hochskaliert wird 
 *     
 *   wenn man ein UI control im Code nutzen will: "android:id="@id/blah" "
 *   
 *   Haftungsausschluss bei Nutzung des Übersetzers angeben in Nutzungsbedingungen
 *   in Deutsch und Englisch.
 *   
 *  */

/**
 * @author mr.sys
 *
 */
public class VTransDynLibJNI
{
  String _configurationDirFullPath;
  String _mainConfigFilePath;
  
  static String [] s_statusMessages = new String [] { "not set", 
  	"looking up word in dictionary", "checking if translation rule applies",
  	"possibly transformig the parse tree", "generating XML tree from parse tree"};
  
  public static String getStatusMessage(byte statusCode)
  {
  	if( statusCode < s_statusMessages.length )
  		return s_statusMessages[statusCode];
  	return "";
  }

	private byte _initReturnCode = -1;
	
	public byte get_initReturnCode() {
		return _initReturnCode;
	}
	
	boolean _initFunctionCalledYet = false;
	private String _currentWorkingDirToUse;
	public volatile boolean _translationStopped = false;
	
	/** Native methods that is implemented by the native library which is 
   * packaged with this application. 
   * Make them private to prevent calling them directly from outside of this 
   * class. Instead, call the "callXXX" methods) 
   * This enables catching an exception with a detailed error message. */
  public native byte GetStatus(/*String*/
    //from http://www.javaworld.com/article/2077554/learn-java/java-tip-54--returning-data-in-reference-arguments-via-jni.html:
		//must be class "StringBuffer" for call-by-reference
		StringBuffer strItem, String time);
  public native byte  Init(
		String jstrMainCfgFile,
    String jstrCfgFilesRootPath,
    String currentWorkingDir);
  private native byte Settings(String name, String value);
  private native String Translate(String englishText);
  private native void FreeMemory();
  public native void Stop();
  
  public void callFreeMemory()
      throws java.lang.Exception
  {
    Log.d("JNI", "callFreeMemory begin");
    try{
      FreeMemory();
    }catch(Throwable t)
    {
      t.printStackTrace();
      throw new java.lang.Exception("calling native function \"FreeMemory\" failed:" 
        + t.toString() );
    }
    Log.d("JNI", "callFreeMemory end");
  }
  
  public void callSettings(String name, String value)
		throws java.lang.Exception
  {
		Log.d("JNI", "callSettings begin name:" + name + " value:" + value );
  	try{
  		byte by = Settings(name, value);
  		if( by > 0 )
    		throw new java.lang.Exception("calling native function \"Translate\" failed:" 
    				+ "allocating char string array failed" );  			
  	}catch(Throwable t)
  	{
  		t.printStackTrace();
  		throw new java.lang.Exception("calling native function \"Translate\" failed:" 
				+ t.toString() );
  	}
		Log.d("JNI", "callSettings end");
  }
  
  public String callTranslate(String englishText) throws java.lang.Exception
  {
		Log.d("JNI", "callTranslate begin");
		String xml;
  	try{
      _translationStopped = false;
  		xml = Translate(englishText);
  		Log.v("translations as XML:", xml);
  	}catch(Throwable t)
  	{
  		t.printStackTrace();
  		throw new java.lang.Exception("calling native function \"Translate\" failed:" 
				+ t.toString() );
  	}
		Log.d("JNI", "callTranslate end");
		return xml;
  }
  
  public void callStop() throws java.lang.Exception
  {
		Log.d("JNI", "callStop begin");
  	try{
  	Stop();
    _translationStopped = true;
  	}catch(Throwable t)
  	{
  		t.printStackTrace();
  		throw new java.lang.Exception("calling native function \"Stop\" failed:" 
  				+ t.toString() );
  	}
		Log.d("JNI", "callStop end");
  }
  
  /* This is used to load the translation core dynamic library on application
   * startup. The library has already been unpacked into
   * /data/data/<package dir path>/lib/<dyn_lib>.so at
   * installation time by the package manager. */
  public static void loadDynLib(final String dynLibPathWoutExtension)
  {
  	Log.i("VTransDynLibJNI", "static beg");
      System.loadLibrary(dynLibPathWoutExtension);
    Log.i("VTransDynLibJNI", "static end");
  }
  
  public boolean initFunctionCalledYet() {
	  return _initFunctionCalledYet;
  }
	
	public void setPathes(final /*File*/ String rootDirectory)
	{
		Log.i("VTransApp", "cacheDir:" + rootDirectory.toString() );
    _configurationDirFullPath = rootDirectory + File.separator + 
  		"configuration";
		_mainConfigFilePath = //_cacheDir + File.separator + 
		//"configuration/VTrans_main_config.xml";
		_configurationDirFullPath + File.separator + "VTrans_main_config.xml";
		_currentWorkingDirToUse = rootDirectory.toString();
	}
	
	public void callInit(/*final String currentWorkingDirToUse*/ )
	{
		if( _currentWorkingDirToUse == null )
			_currentWorkingDirToUse = _configurationDirFullPath;
		Log.d("callInit", "before calling native Init method");
		_initReturnCode = Init(//Context.getFilesDir().getPath() +"assets/configuration/VTrans_main_config.xml"
	//  		"/cache/VTrans_main_config.xml", 
	//  		"/data/data/vtrans.dynlib/assets/configuration/VTrans_main_config.xml",
			_mainConfigFilePath,
	//  		"assets/configuration"
			_configurationDirFullPath,
			_currentWorkingDirToUse
			);
		Log.d("callInit", "after calling native Init method");
  }
}
