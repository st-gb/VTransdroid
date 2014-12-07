package vtrans;

//import vtrans.dynlib.VTransDynLibJNI.GuiCallBacks;

import vtrans.dynlib.InitFunction;
import vtrans.dynlib.VTransDynLibJNI;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

/** @author S.Seide */
public class VTransBackground
	implements Runnable
{

	private vtrans.TranslateActivity.GuiCallBacks callback;
	private Handler guiHandler;
	private VTransDynLibJNI _vtransDynLibJNI;
	private VTransApp _vtransApp;
	
	public VTransBackground(
		/*Handler handler,*/ 
		TranslateActivity.GuiCallBacks cb, 
		VTransDynLibJNI vtransDynLibJNI, VTransApp vTransApp)
	{
		this.callback = cb;
//		this.guiHandler = handler;
		_vtransDynLibJNI = vtransDynLibJNI;
		_vtransApp = vTransApp;
	}

	public void run()
	{
		initializeVTrans();
	}

	/** This method is time-consuming and so should be called in a non-GUI 
	 *   thread. */
	private void initializeVTrans()
	{
	  //from http://stackoverflow.com/questions/2848575/how-to-detect-ui-thread-on-android
    // On UI thread.
	  if (Looper.getMainLooper().getThread() == Thread.currentThread())
	  {
	    Log.w("VTransBackground initializeVTrans", 
        "time-consuming init in UI thread");
	  }
		Log.i("initializeVTrans", "before loading dyn lib");				
    try {
  		_vtransDynLibJNI.loadDynLib("VTrans");
  		
//  		boolean testMode = true;
//  		if( /*! testMode*/ ! _vtransApp._runningOnEmulator )
  			_vtransDynLibJNI.callSettings("logging", "disable");
      
  		Log.i("initializeVTrans", "before calling Init");
  		_vtransDynLibJNI.callInit( /*_cacheDir.toString()*/);
  		Log.i("initializeVTrans", "after calling Init");
  		
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    //"Synchronizing Threads for GUI Operations"
    doUpdateGUI();
  	Log.i("VTransDynLibJNI", "onCreate ret val of Init:" + 
  			_vtransDynLibJNI.get_initReturnCode() );
	}

	/** Seide: Variante A) direkt
	* Runnable that executes the update GUI method. */
	public void doUpdateGUI()
	{
		Log.i("doUpdateGUI", "run");
		if ( _vtransDynLibJNI.get_initReturnCode() == 0) {
			callback.vtransIsReady(true);
		}
		callback.setStatusText("after Initializing Trans:"
			+ InitFunction.getInitMessage(_vtransDynLibJNI.get_initReturnCode() ));
	}
	
	/** Seide: Variante B) indirekt ueber Handler und Post
	* Runnable that executes the update GUI method. */
	public void doUpdateGUI2()
	{
		guiHandler.post( new Runnable() {
			@Override
			public void run() {
//				Log.i("doUpdateGUI", "run");
//				if (_initReturnCode == 0) {
//					callback.vtransIsReady(true);
//				}
//				callback.setStatusText("after Initializing Trans:"
//						+ getInitMessage(_initReturnCode));
			}
		});
	};
}
