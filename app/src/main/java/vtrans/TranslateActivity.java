package vtrans;

//import vtrans.dynlib.VTransDynLibJNI.GuiCallBacks;
import vtrans.OnTranslateButtonClickListener;
import vtrans.dynlib.InitFunction;
import vtrans.dynlib.R;
import vtrans.dynlib.R.id;
import vtrans.dynlib.R.layout;
import vtrans.dynlib.attributes.TranslatedText;
import vtrans.view.ColouredTextView;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class TranslateActivity
	extends android.app.Activity
{
	VTransApp _vtransApp;
//	VTransDynLibJNI _vtransDynLibJNI = new VTransDynLibJNI();
	//private Vector<Vector> _translations;
	  
	//TextView _textView;
	EditText _englishText;
	TextView _duration;
	Button _translateButton;
	ToggleButton _softKeyBoardToggleButton;
	Button _infoButton;
	Button _stopButton;
	Button _settingsButton;
	/** from S.Seide */
	GuiCallBacks callbacks = new GuiCallBacks(this);
	
	EditText _germanText;
  private ColouredTextView _colouredTextView;
  private ScrollView _scrollView;
	
  void assignControlsToMemberVariables()
  {
    /** from "Professional Android ...", "Chapter 4: Creating User Interfaces" 
    *  "Creating Activity User Interfaces with Views" */
    setContentView(R.layout.activity_main);
    
  //  _textView = /*new TextView(this);*/
    _germanText = /*new TextView(this);*/ (EditText) findViewById(R.id.germanText);
    
    _colouredTextView = (ColouredTextView) findViewById(R.id.colouredTextView);
    _colouredTextView.setApp(_vtransApp);
    
    _scrollView = (ScrollView) findViewById(R.id.colouredTextViewScrollView);
    
//    Log.i("onCreate", "germanText:" + _germanText);
    
    _translateButton = /*new Button(this);*/ (Button) findViewById(R.id.translate);
	_infoButton = (Button) findViewById(R.id.info);
	_softKeyBoardToggleButton = (ToggleButton) findViewById(id.softKeyBoardToggleButton);
    _stopButton = /*new Button(this);*/ (Button) findViewById(R.id.stop);
    _settingsButton = (Button) findViewById(R.id.settings);
    _englishText = /*new EditText(this);*/ (EditText) findViewById(R.id.englishText);
	  //https://stackoverflow.com/questions/4165414/how-to-hide-soft-keyboard-on-android-after-clicking-outside-edittext
	_englishText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
		@Override
		public void onFocusChange(View view, boolean hasFocus) {
			if (!hasFocus) {
/*				android.view.inputmethod.InputMethodManager inputMethodManager =
					(android.view.inputmethod.InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
				inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);*/
			}
		}
	});
    //TODO set input language to English
//    _englishText.setI
    _duration = (TextView) findViewById(R.id.duration);
  }

  void enableHideAndShowSoftKeyboard() {
	  _softKeyBoardToggleButton.setOnCheckedChangeListener(
			  new android.widget.CompoundButton.OnCheckedChangeListener() {
				  public void onCheckedChanged(android.widget.CompoundButton buttonView, boolean isChecked) {
					  if (isChecked) {
						  android.view.inputmethod.InputMethodManager inputMethodManager =
								  (android.view.inputmethod.InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
						  inputMethodManager.showSoftInput(_englishText, 0);
					  } else {
						  android.view.inputmethod.InputMethodManager inputMethodManager =
								  (android.view.inputmethod.InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
						  inputMethodManager.hideSoftInputFromWindow(_englishText.getWindowToken(), 0);
					  }
				  }
			  });
/*	  _softKeyBoardToggleButton.setOnClickListener(
		  new View.OnClickListener() {
			  @Override
			  public void onClick(View v) {
			*//*android.view.inputmethod.InputMethodManager inputMethodManager =
					(android.view.inputmethod.InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(_englishText.getWindowToken(), 0);*//*
			  }
		  }
	  );*/
  }

	/** Should be called after assigning controls from class "R" to member
   *  variables or after building GUI by hand. */
  void setControlStatesAndListeners()
  {
    _stopButton.setEnabled(false);    
    _translateButton.setEnabled(false);
    _colouredTextView.setParentView(_scrollView);
    _settingsButton.setOnClickListener(
      new View.OnClickListener() {
        @Override
        public void onClick(View v) {
  //        setContentView(R.layout.settings);
          /** from http://stackoverflow.com/questions/7991393/how-to-switch-between-screens */
          Intent intent = new Intent(TranslateActivity.this, SettingsActivity.class);
          startActivity(intent);
        }
      }
	);
//        new OnTranslateButtonClickListener(
//            _vtransApp._vtransDynLibJNI, this)
	enableHideAndShowSoftKeyboard();
  }
  
	/** @brief Called when:
	 *  -the activity is first created. 
	 *  -changed from portrait to landscape mode and vice versa */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		Log.i("VTransDynLibJNI", "onCreate beg");
	  super.onCreate(savedInstanceState);

	  /** Must be assigned at first because reference is needed in following calls.*/
    _vtransApp = (VTransApp) getApplication();
    _vtransApp.setTranslateActivity(this);
	  assignControlsToMemberVariables();
	  setControlStatesAndListeners();
	  
	//    	Log.i("VTransDynLibJNI", "onCreate " + Context.getFilesDir().getPath() );
  	_vtransApp._apkUtil = new ApkUtil(_vtransApp, this.callbacks);
  	
  	_vtransApp.possiblyCopyAssetFilesIntoCacheDirInBG(callbacks);
    
    /** If text has been translated yet: show translation */
    if( _vtransApp._translatedText != null)
    {
//      _germanText.setVisibility(View.INVISIBLE);
//      _colouredTextView.setVisibility(View.VISIBLE);
      setViewToColouredTextView(_vtransApp._translatedText);
      //TODO
      //BackgroundColorSpan android.text.style
      //Oder Text.FromHTML()
      //android.text.html
    }
  	Log.i("VTransDynLibJNI", "onCreate end");
	}
	
//  protected void onResume()
//  {
//  }
	/**
	 * 
	 */
	//TODO extract method to a new class
	private void createGUI()
	{
	  //      ViewGroup vg = new ViewGroup(this);
	  LinearLayout ll = new LinearLayout(this);
	  ll.setOrientation(LinearLayout.VERTICAL);
	  
	  final int minSize = LinearLayout.LayoutParams.WRAP_CONTENT;
	  int lHeight = LinearLayout.LayoutParams.WRAP_CONTENT;
	  int lWidth = LinearLayout.LayoutParams.MATCH_PARENT;      
	  LinearLayout.LayoutParams textLayoutParams = new 
			LinearLayout.LayoutParams(lWidth, lHeight);
	  
	  LinearLayout.LayoutParams buttonLayoutParams = new 
			LinearLayout.LayoutParams(minSize, minSize);
	  
	  ll.addView(_englishText, textLayoutParams);
	  ll.addView(_translateButton, buttonLayoutParams);
	  //ll.addView(_textView, textLayoutParams);
//	  ll.addView(_germanText, textLayoutParams);
	  
	  _translateButton.setText("translate");
	}
	
		
	//Initialize a handler on the main thread.
	private Handler handler = new Handler();
	
	//Runnable that executes the update GUI method.
	private Runnable doUpdateGUI = new Runnable() {
		public void run() {
	  Log.i("doUpdateGUI", "run");			
		updateGUIAfterInit();
		}
	};
  private Thread _translationThread;
  private WaitForTranslationEndedThread _waitForTranslationEndedThread;

  private OnEnglishTextChangedListener _onEnglishTextChangedListener = 
    new OnEnglishTextChangedListener(this);
	
	/** @author Stefan Seide */
	public void vtransIsReady(final boolean isReady) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if ( isReady ) {
					findViewById(R.id.translate).setEnabled(true);
				}
				else {
					Toast.makeText(getApplicationContext(), "Init VTrans failed", Toast.LENGTH_LONG)
						.show();
				}
			}
		});
	}

  void setViewToColouredTextView(final TranslatedText translatedText)
  {
    _germanText.setVisibility(View.INVISIBLE);
    _colouredTextView.setVisibility(View.VISIBLE);
    
    final float textSize = _germanText.getTextSize();
    
    _colouredTextView.setMinimumHeight(_scrollView.getHeight() );
    
    /** Else the text may be too small to be readable. */
    _colouredTextView.setTextSize(textSize);
    _colouredTextView.setTranslatedText(translatedText);
//    _colouredTextView.determineHeight();
    _colouredTextView.requestLayout();
    _colouredTextView.invalidate(); //force redraw
  }
	
	/** @author Stefan Seide */
	// calls to update gui elements from other threads
	// this methods can be called directly from every thread without Looper etc.
	public class GuiCallBacks {
		
		private TranslateActivity _translateActivity;

		GuiCallBacks(TranslateActivity translateActivity)
		{
			this._translateActivity = translateActivity;
		}
		
		public void setStatusText(final String text)
		{
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					_germanText.setText( text );
				}
			});
		}
		
		public void setGermanText(final String s) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
          _germanText.setVisibility(View.VISIBLE);
          _colouredTextView.setVisibility(View.INVISIBLE);
					_germanText.setText( s );
//					_et.setSelection(0, 1);
//					_germanText.
				}
			});
		}
		
		public void setGermanTranslation(final TranslatedText translatedText)
		{
      runOnUiThread(new Runnable() {
        @Override
        public void run() {
          
          setViewToColouredTextView(translatedText);
          
          _vtransApp._latestGermanTranslationView = _colouredTextView;
          _vtransApp._translatedText = translatedText;
//          _et.setSelection(0, 1);
//          _germanText.
        }
      });
		}
		
		public void setEnglishText(final String edit) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					_englishText.setText( edit );
				}
			});
		}

		public void vtransIsReady(boolean isReady)
		{
			Log.d("GUIcallbacks", "vtransIsReady ready?:" + isReady);
			runOnUiThread(new Runnable()
  			{
  				@Override
  				public void run()
  				{
  				  Log.v("TranslateActivity.vtransIsReady", "runOnUiThread run");
  				  /** Must be enabled before assigning a click listener? */
            _translateButton.setEnabled(true);
  			    _translateButton.setOnClickListener(new OnTranslateButtonClickListener(
  		    		_vtransApp._vtransDynLibJNI, _translateActivity) );
  			    if( _vtransApp._translateOnChangedText )
              _englishText.addTextChangedListener(_onEnglishTextChangedListener);
  			    _stopButton.setOnClickListener(new OnStopButtonClickListener(
  			    		_vtransApp, TranslateActivity.this) );
  		      _stopButton.setEnabled(false);
  		      //if( // isReady == true )
  		      {
              Log.v("TranslateActivity.vtransIsReady", "calling translate");
  		        translate();
  		      }
            Log.v("TranslateActivity.vtransIsReady", "runOnUiThread end");
  				}
  			}
			);
      Log.d("GUIcallbacks", "vtransIsReady end");
    }

		public void setTranslateControlsState(final boolean enabled) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					TranslateActivity.this.setTranslateControlsState(enabled);
				}
			});	    
    }

		public void setStopButtonEnabled(final boolean enabled) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					_stopButton.setEnabled(enabled);
				}
			});	    
    }

		public void setDuration(final String string) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					_duration.setText(string);
				}
			});	    
    }
	
	}
	
	private void updateGUIAfterInit() {
		Log.i("updateGUIAfterInit", "updateGUIAfterInit");
		final byte initReturnCode = _vtransApp._vtransDynLibJNI.get_initReturnCode();
	  if( initReturnCode == 0)
	  {
//	  	_translateButton.setOnClickListener(
//				new OnTranslateButtonClickListener(this) );
	  }
	//  _textView.setText("after Initializing Trans:" + getInitMessage(_initReturnCode) );		
	  _germanText.setText("after Initializing Trans:" + InitFunction.getInitMessage(initReturnCode) );		
	}
	
	public void onStop()
	{
		Log.i("TranslateActivity", "onStop beg");  	
		super.onStop();
//    int h = _scrollView.getHeight();
//    h = _englishText.getHeight();    
		Log.i("TranslateActivity", "onStop end");
	}

	public void setTranslateControlsState(boolean enabled) {
//		_englishText.setEnabled(enabled);
    _translateButton.setEnabled(enabled);
  }

	/** May be called from different thread: 
	 *  -UI thread
	 *  -VTrans native engine init thread
	 *  -Wait for translation ended thread 
	 *  so _synchronize_ access */
  public synchronized void translate()
  {
    Log.v("TA.translate", "begin" );
    try
    {
      if( _waitForTranslationEndedThread != null && ! _waitForTranslationEndedThread.isAlive() )
        _waitForTranslationEndedThread = null;
    if( _translationThread != null && _translationThread.isAlive() )
    {
      Log.v("TA.translate", "already translating" );
      if( _waitForTranslationEndedThread == null //&& ! _waitForTranslationEndedThread.isAlive() 
          )
      {
        _waitForTranslationEndedThread = new WaitForTranslationEndedThread(
          this);
        _waitForTranslationEndedThread.start();
      }
    }
    else
    {
      Log.v("TA.translate", "not already translating" );
      final String englishText = _englishText.getText().toString();
      Log.v("TranslateActivity translate", englishText);
      if( ! englishText.isEmpty() )
      {
  //    translateActivity._vtransApp._translationStopped = false;
      
      //from http://stackoverflow.com/questions/833768/java-code-for-getting-current-time
  //    Date currentTime = new Date();
  //    final String strCurrentTime = currentTime.getDay() + "-" + currentTime.getMonth() + " " + 
  //      currentTime.getHours() + ":" + currentTime.getMinutes() + ":" + currentTime.getSeconds();
  //    _guiCallBacks.setGermanText("translating (started:" + strCurrentTime + ")");
      
        setTranslateControlsState(false);
        _stopButton.setEnabled(true);
        
        Translater translater = new Translater(callbacks, englishText, 
          _vtransApp.get_vtransDynLibJNI() );
        _translationThread = new Thread( translater, "Translater");
        _translationThread.start();
      }
    }
    }
    catch(Throwable t)
    {
      Log.e("TA.translate", t.toString() );
    }
  }

  public Thread getTranslationThread() {
    return _translationThread;
  }

  public void addOnKeyListenerForEnglishText() {
    _englishText.addTextChangedListener(_onEnglishTextChangedListener);
  }

  public void deleteOnKeyListenerForEnglishText() {
//    _englishText.setOnKeyListener(null);
    _englishText.removeTextChangedListener(_onEnglishTextChangedListener);
  }
}
