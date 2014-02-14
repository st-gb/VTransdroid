package vtrans;

import java.io.IOException;

import vtrans.TranslateActivity.GuiCallBacks;
import vtrans.dynlib.InitFunction;
import vtrans.dynlib.VTransDynLibJNI;
import vtrans.dynlib.InitFunction.Init_return_codes;

public class AssetFilesExtracter implements Runnable
{
	private ApkUtil _apkUtil;
	private GuiCallBacks _callbacks;
	private VTransApp _vtransApp;
	private VTransDynLibJNI _vtransDynLibJNI;

	public AssetFilesExtracter(GuiCallBacks callbacks, ApkUtil apkUtil, 
			VTransApp vTransApp)
	{
		this._callbacks = callbacks;
		_apkUtil = apkUtil;
		_vtransApp = vTransApp;
		_vtransDynLibJNI = vTransApp._vtransDynLibJNI;
	}
	
	@Override
	public void run() {
  	try
  	{
	    _apkUtil.possiblyCopyAssetFilesIntoFileSystemDir("configuration");
      _apkUtil.possiblyCopyAssetFilesIntoFileSystemDir("dictionaries");
	    
	  	if( _vtransDynLibJNI./*initFunctionCalledYet()*/get_initReturnCode() == -1 )
	  	{
	  	  //TODO does not need to be executed in another thread because this
	  	  // is already intended to be executed in a non-GUI thread.
	  		_vtransApp.initializeVTransInBackGround(_callbacks/*, handler*/);
//	  		_vtransApp.initializeVTrans();
//	  		initializeVTransInBackGround();
	  	}
	  	else
	  	{
	  		if ( _vtransDynLibJNI.get_initReturnCode() == 
  		    InitFunction.Init_return_codes.success.ordinal() ) {
	  			_callbacks.vtransIsReady(true);
	  		}
	  	}
    } catch (IOException e) {
	    _callbacks.setGermanText(e.toString() );
	    e.printStackTrace();
    }
//	  _callbacks.setTranslateControlsState(true); //_translateButton.setEnabled(false);
	}
}
