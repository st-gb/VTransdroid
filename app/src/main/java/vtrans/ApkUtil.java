package vtrans;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import vtrans.TranslateActivity.GuiCallBacks;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.AssetManager.AssetInputStream;
import android.util.Log;

public class ApkUtil
{
  final static int CHARACTER_NOT_FOUND = -1;
  final static int END_OF_STREAM = -1;
  private static final int FILE_BUFFER_SIZE = 1024;
  
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
    final int numAssetsInDirLevel = assetRelativeFilePathes.length;
  	Log.i("VTransDynLibJNI", "onCreate # assets:" +
      numAssetsInDirLevel);
  	for(int assetFileOrDirIdx = 0;assetFileOrDirIdx < numAssetsInDirLevel;
		++ assetFileOrDirIdx)
  	{
      final String filePath = //"configuration/"
        assetRootPath + File.separator + assetRelativeFilePathes[
          assetFileOrDirIdx];
      final File fileOrDir = new File(filePath);
    	
      Log.i("VTransDynLibJNI", "onCreate asset:" + assetRelativeFilePathes[
      	assetFileOrDirIdx] + " exists? " +  fileOrDir.exists() + " ");
    	
    	final boolean isFile = isFile(filePath);
    	if( ! isFile )
    	{
    		createDirectory(filePath);
    	}

  	  if( fileOrDir.isFile() )
      {
        Log.i("VTransDynLibJNI", "onCreate is file:" + assetRelativeFilePathes[
          assetFileOrDirIdx]);
      }
      if( fileOrDir.isDirectory() )
      {
        Log.i("VTransDynLibJNI", "onCreate is dir:" +
          assetRelativeFilePathes[assetFileOrDirIdx]);
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

  /** See http://www.javaworld.com/article/2076241/build-ci-sdlc/tweak-your-io-performance-for-faster-runtime.html:
  *  fastest is "Read data 1 K at a time, using FileInputStream.read(byte[]), 
  *  and access the data from the buffer" */
  private void copyFileViaSelfBufferedInputStream(final InputStream is,
    final File file) throws IOException
  {
    final byte [] buffer = new byte[FILE_BUFFER_SIZE];
//    final FileInputStream fis = new FileInputStream(is);
    FileOutputStream fos = new FileOutputStream(file);
    int numBytesRead;
    do
    {
      numBytesRead = is.
        /** "read" Returns the number of bytes actually read or -1 if the end 
          * of the stream has been reached. */
        read(buffer);
      if( numBytesRead == 0)
        Log.w("copyFileViaSelfBufferedInputStream", "0 bytes read");
      _bytesCopied += numBytesRead;
      if( numBytesRead > 0)
        /** If not specifying the number of bytes to write then the file size 
         * gets a multiple of the (default) buffer size (e.g. 1024 bytes) */
        fos.write(buffer, 0, numBytesRead);
    }while( numBytesRead == FILE_BUFFER_SIZE );
    fos.close();
    is.close();
  }
  
  private void copyFileViaBufferedInputStream(final InputStream is, 
      final File file) throws IOException
  {
    BufferedInputStream bis = new BufferedInputStream(is);
    FileOutputStream fos = new FileOutputStream(file);
    BufferedOutputStream bos = new BufferedOutputStream(fos);
//    BufferedReader r;
    int i;
    //TODO test if file size equals return value of "skip"
//    final long numBytesSkipped = bis.skip(Long.MAX_VALUE);
//    Log.v("copy file", "numBytesSkipped: " + numBytesSkipped);
    do
    {
      i = bis.read();
      //TODO use interlockedincrement here?
      ++ _bytesCopied;
      bos.write(i);
    }while( i != END_OF_STREAM );
    bis.close();
    bos.close();
  }
  
  /** Copies a single file from the archive's asset folder to a file system 
   *  path. */
  private void extractFile(final String filePath )
		throws IOException
  {
  	Log.i("VTransDynLibJNI", "extractFile: \"" + filePath + "\" from assets" );
  	_guiCB.setStatusText("extracting " + filePath);
  	  	
//		AssetInputStream assetInputStream = new AssetInputStream();
		_fileSizeInBytes = getFileSize(filePath);
		/** Must be created after calling "getFileSize", else the file size of 
		*  created files was zero. */
    InputStream is = _assetManager.open(//"configuration/VTrans_main_config.xml"
        filePath);
		
  	//from http://stackoverflow.com/questions/6992002/size-of-file-which-we-get-through-assetmanager-function-getassets-in-android
		/** openFd() error: can not be opened ... compressed */
//  	_assetManager.openFd(filePath);
//  	AssetFileDescriptor fd = _assetManager.openNonAssetFd(filePath);
//  	_fileSizeInBytes = fd.getLength();
  	
		//TODO get file size in order to show a progress bar
		final String outputFilePath = _vtransApp._rootDirectoryPath + File.separator + filePath;
  	File file = new File(//mainConfigFilePath
			outputFilePath);
  	Log.i("VTransDynLibJNI", "outputFilePath:" + outputFilePath);
    _bytesCopied = 0;
    /** See http://nadeausoftware.com/articles/2008/02/java_tip_how_read_files_quickly#Benchmarks 
     *  See http://stackoverflow.com/questions/5854859/faster-way-to-read-file
     *  : "Mapped byte buffers is the fastest way"
     * See http://www.javaworld.com/article/2076241/build-ci-sdlc/tweak-your-io-performance-for-faster-runtime.html:
     *  fastest is "Read data 1 K at a time, using FileInputStream.read(byte[]), 
     *  and access the data from the buffer" */
    copyFileViaSelfBufferedInputStream(is, file);
//    copyFileViaBufferedInputStream(is, file);
    
		file.setExecutable(true, false);
		file.setReadable(true, false);
		Log.v("VTransDynLibJNI", "file size: " + file.length() );	  
  }

  /** The only fast way to determine a file size of an _assert_ (i.e. a file 
   * within an archive file) file is to seek the file pointer */
	private long getFileSize(final String filePath) throws IOException
	{
	  /** Opens a new InputStream because "skip()" is used, so it can not be  
	  * seeked to the file begin. */
	  InputStream inputStream = _assetManager.open(//"configuration/VTrans_main_config.xml"
      filePath);
	  long numBytesSkipped = 0;//the number of bytes actually skipped.
	  final long NUM_BYTES_TO_SKIP = 100000;
	  int iterations = 0;
	  while( (numBytesSkipped = inputStream.skip(NUM_BYTES_TO_SKIP) ) == NUM_BYTES_TO_SKIP )
	  {
	    ++ iterations;
	  }
	  inputStream.close();
	  final long fileSizeInBytes = iterations * NUM_BYTES_TO_SKIP + numBytesSkipped;
	  return fileSizeInBytes;
  }

  public void possiblyCopyAssetFilesIntoFileSystemDir(final String assetDirPath)
    throws IOException
	{//TODO handle dir create/copy errors and show errors in GUI
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
