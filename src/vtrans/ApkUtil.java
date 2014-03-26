package vtrans;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import vtrans.TranslateActivity.GuiCallBacks;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.util.Log;

public class ApkUtil
{
  final static int CHARACTER_NOT_FOUND = -1;
  final static int END_OF_STREAM = -1;
  
	private VTransApp _vtransApp;
	protected AssetManager _assetManager;
	TranslateActivity.GuiCallBacks _guiCB;
	/** Must be volatile because it is read from another thread. */
	private volatile int _bytesCopied;
	volatile long _fileSizeInBytes;

	public ApkUtil(VTransApp vtransApp, GuiCallBacks guiCB)
	{
		_vtransApp = vtransApp;
		_guiCB = guiCB;
    //http://stackoverflow.com/questions/17383552/how-to-package-native-commandline-application-in-apk
    //AssetManager.AssetInputStream = getAssets();
    _assetManager = _vtransApp.getAssets();
	}
	
	/** Recursively traverses a directory tree inside the assets folder of an
	 *  apk archive.*/
  void extractAssetFiles(final String assetRootPath)
    throws IOException
  {
    String assetRelativeFilePathes [];
//  try {
		
//    assetRelativeFilePathes = am.list("assets/");
//  	Log.i("VTransDynLibJNI", "onCreate # assets:" + assetRelativeFilePathes.length);
		
//		String assetRootPath = "configuration";
    assetRelativeFilePathes = _assetManager.list(//"configuration"
    		assetRootPath);
  	Log.i("VTransDynLibJNI", "onCreate # assets:" + assetRelativeFilePathes.length);
  	for(int i=0; i < assetRelativeFilePathes.length; ++ i)
  	{
    	final String filePath = //"configuration/"
    			assetRootPath + File.separator + assetRelativeFilePathes[i];
  		final File fileOrDir = new File(filePath);
    	
    	Log.i("VTransDynLibJNI", "onCreate asset:" + assetRelativeFilePathes[i] 
    			+ " exists? " +  fileOrDir.exists() + " ");
    	
    	final boolean isFile = isFile(filePath);
    	if( ! isFile )
    	{
    		createDirectory(filePath);
    	}
    	
  		if( fileOrDir.isFile() )
  		{
	    	Log.i("VTransDynLibJNI", "onCreate is file:" + assetRelativeFilePathes[i]);
  		}
  		if( fileOrDir.isDirectory() )
  		{
	    	Log.i("VTransDynLibJNI", "onCreate is dir:" + assetRelativeFilePathes[i]);
  		}
  		extractAssetFiles(filePath);
  		if( //f.length() > 0
  				isFile)
  			extractFile(filePath);
  	}
//		FileOutputStream fos = new FileOutputStream(new File("/cache/VTrans_main_config.xml"));
  	//laut Seide: getExternalFilesDir("");
//		File externalFilesDir = getExternalFilesDir(null);
//		Log.i("VTransDynLibJNI", "onCreate externalFilesDir:" + externalFilesDir.toString() );		
  }

	/**
	 * @param filePath
	 * @throws IOException
	 */
  private boolean isFile(final String filePath) throws IOException {
  	boolean isFile = true;
	  try
	  {
	  	InputStream is = _assetManager.open(filePath);
    	Log.i("VTransDynLibJNI", "is:" + is); 
    	is.close();
	  }
	  catch(FileNotFoundException fne)
	  {
	  	isFile = false;
	  }
	  return isFile;
  }
  
	boolean configDataCopied(final String directoryRelativeToRootDir)
	{
  	Log.i("VTransDynLibJNI", "configDataCopied direcory:\"" + 
			directoryRelativeToRootDir + "\"");
  	
	  final String fullConfigDirPath = getFullDirPath(directoryRelativeToRootDir);
	  
	  final boolean dirExists = dirExists(fullConfigDirPath);
	  
	  Log.i("VTransDynLibJNI", "configDataCopied directory \"" + fullConfigDirPath +
	  	"\"exists?" + dirExists );
		return dirExists;
  }

	private String getFullDirPath(final String directoryRelativeToCacheDir) {
		return _vtransApp._rootDirectoryPath + File.separator + 
			directoryRelativeToCacheDir;
  }

	private boolean dirExists(final String fullDirPath) {
	  final File dir = new File(fullDirPath);
	  final boolean dirExists = dir.exists();
	  return dirExists;
  }

	/** 
	 * 
	 */
  void createDirectory(final String dir) {
  	Log.i("VTransDynLibJNI", "createDirectory " + dir);
  	
	  final String fullConfigDirPath = //_vtransApp._rootDirectoryPath + File.separator + dir;
	    getFullDirPath(dir);
	  final File configDir = new File(fullConfigDirPath);
	  configDir.mkdir();
	  Log.i("VTransDynLibJNI", "createDirectory dir exists?" + configDir.exists() );
  }

  /** Copies a single file from the archive's asset folder to a file system 
   *  path. */
  private void extractFile(final String filePath )
		throws IOException
  {
  	Log.i("VTransDynLibJNI", "extractFile: \"" + filePath + "\" from assets" );
  	_guiCB.setStatusText("extracting " + filePath);
  	
		InputStream is = _assetManager.open(//"configuration/VTrans_main_config.xml"
			filePath);
		
  	//from http://stackoverflow.com/questions/6992002/size-of-file-which-we-get-through-assetmanager-function-getassets-in-android
		/** openFd() error: can not be opened ... compressed */
//  	_assetManager.openFd(filePath);
//  	AssetFileDescriptor fd = _assetManager.openNonAssetFd(filePath);
//  	_fileSizeInBytes = fd.getLength();
  	
  	BufferedInputStream bis = new BufferedInputStream(is);
		//TODO get file size in order to show a progress bar
		final String outputFilePath = _vtransApp._rootDirectoryPath + File.separator + filePath;
  	File file = new File(//mainConfigFilePath
			outputFilePath);
  	Log.i("VTransDynLibJNI", "outputFilePath:" + outputFilePath);
  	FileOutputStream fos = new FileOutputStream(file);
  	BufferedOutputStream bos = new BufferedOutputStream(fos);
//  	BufferedReader r;
		int i;
		_bytesCopied = 0;
		//TODO test if file size equals return value of "skip"
//		final long numBytesSkipped = bis.skip(Long.MAX_VALUE);
//		Log.v("copy file", "numBytesSkipped: " + numBytesSkipped);
		do
		{
			i = bis.read();
			++ _bytesCopied;
			bos.write(i);
		}while( i != END_OF_STREAM );
		bis.close();
		bos.close();
		file.setExecutable(true, false);
		file.setReadable(true, false);
		Log.v("VTransDynLibJNI", "file size: " + file.length() );	  
  }

	public void possiblyCopyAssetFilesIntoFileSystemDir(final String assetDirPath)
    throws IOException
	{		
		if( ! configDataCopied(assetDirPath) )
		{
			createAllDirLevels(assetDirPath);
			
  		_guiCB.setStatusText("before copying");
      
  		ShowFileExtractionStatus showFileExtractionStatus = new 
      		ShowFileExtractionStatus(_guiCB, this);
      Thread thread = new Thread(showFileExtractionStatus, 
    		"show file extraction status");
      thread.start();
      
  		extractAssetFiles(assetDirPath);
    	showFileExtractionStatus.stop();
		}	  
  }

	/** Creates levels of {@link path} directory path if not already existant. */
	private void createAllDirLevels(final String relativeFileSystemPath)
	{
		final String absoluteFileSystemPath = getFullDirPath(relativeFileSystemPath);
		int indexOfSeperatorChar = 
			//-> start with index 1 for first call 
			0;
//		int indexOfRightSeperatorChar;
		int indexOfStringEnd;
//		String dirLevel = "";
		do
		{
			indexOfSeperatorChar = absoluteFileSystemPath.indexOf(File.separatorChar, 
		    indexOfSeperatorChar + 1);
			if( indexOfSeperatorChar == CHARACTER_NOT_FOUND )
				indexOfStringEnd = absoluteFileSystemPath.length();
			else
				indexOfStringEnd = indexOfSeperatorChar;
//			indexOfRightSeperatorChar = path.indexOf(File.separatorChar, 
//				indexOfSeperatorChar + 1);
//			if( indexOfRightSeperatorChar == -1 )
//				indexOfStringEnd = path.length();
//			else
//				indexOfStringEnd = indexOfRightSeperatorChar;
//			dirPath = path.substring(indexOfSeperatorChar + 1, indexOfStringEnd);
			final String fullDirPath = absoluteFileSystemPath.substring(0, 
		    indexOfStringEnd);
			
			if( ! dirExists(fullDirPath) )
			{
//				createDirectory();
			  File dir = new File(fullDirPath);
			  dir.mkdir();
			}
//		createDirectory("configuration");
		}while(indexOfSeperatorChar != CHARACTER_NOT_FOUND);
  }

	public int getNumBytesCopied() {
	  return _bytesCopied;
  }
}
