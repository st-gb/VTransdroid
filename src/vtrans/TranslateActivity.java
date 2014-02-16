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
    _scrollView = (ScrollView) findViewById(R.id.colouredTextViewScrollView);
    
//    Log.i("onCreate", "germanText:" + _germanText);
    
    _translateButton = /*new Button(this);*/ (Button) findViewById(R.id.translate);
    _stopButton = /*new Button(this);*/ (Button) findViewById(R.id.stop);
    _settingsButton = (Button) findViewById(R.id.settings);
    _englishText = /*new EditText(this);*/ (EditText) findViewById(R.id.englishText);
    //TODO set input language to English
//    _englishText.setI
    _duration = (TextView) findViewById(R.id.duration);
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
//        new OnTranslateButtonClickListener(
//            _vtransApp._vtransDynLibJNI, this)
    );
  }
  
	/** @brief Called when:
	 *  -the activity is first created. 
	 *  -changed from portrait to landscape mode and vice versa */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		Log.i("VTransDynLibJNI", "onCreate beg");
	  super.onCreate(savedInstanceState);

	  assignControlsToMemberVariables();
	  setControlStatesAndListeners();
	  
	//    	Log.i("VTransDynLibJNI", "onCreate " + Context.getFilesDir().getPath() );
  	_vtransApp = (VTransApp) getApplication();
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

		public void vtransIsReady(boolean b)
		{
			Log.d("GUIcallbacks", "vtransIsReady ready?:" + b);
			runOnUiThread(new Runnable()
			{
				@Override
				public void run()
				{
				  Log.v("vtransIsReady", "runOnUiThread run");
				  /** Must be enabled before assigning a click listener? */
          _translateButton.setEnabled(true);
			    _translateButton.setOnClickListener(new OnTranslateButtonClickListener(
		    		_vtransApp._vtransDynLibJNI, _translateActivity) );
			    _stopButton.setOnClickListener(new OnStopButtonClickListener(
			    		_vtransApp, TranslateActivity.this) );
		      _stopButton.setEnabled(false);
          Log.v("vtransIsReady", "runOnUiThread end");
				}
			});
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
		_englishText.setEnabled(enabled);
    _translateButton.setEnabled(enabled);
  }
}
