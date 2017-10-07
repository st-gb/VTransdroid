package vtrans.view;

import vtrans.VTransApp;
import vtrans.dynlib.attributes.TranslatedText;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.widget.LinearLayout;
import android.widget.Spinner;

public class ColouredTextView extends View 
  implements View.OnTouchListener
{
	private Paint _textPaint;
	private String m_xmlData;
  private TranslatedText _translatedText;
  private int _currentHeight;
  private int _currentWidth;
  float _fontHeightFromBaseLine;
  private ScaleGestureDetector _scaleDetector;
  private VTransApp _vtransApp = null;
  private int _widthMeasureSpec;
  private int _heightMeasureSpec;
  private int _minHeightInPixels = 0;
  /** To determine the height of the parent->determine max. height of _this_
   *  View */
  private View _parentView;
  private TranslatedTextDrawer _translatedTextDrawer = new 
    TranslatedTextDrawer();
  private TranslatedTextHeightCalculator _translatedTextHeightCalculator = new 
    TranslatedTextHeightCalculator();
  
	public ColouredTextView(Context context) {
	  super(context);
	  init();
  }

	public ColouredTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public ColouredTextView(Context context, 
		AttributeSet ats, 
		int defaultStyle)
	{
		super(context, ats, defaultStyle);
		init();
	}
	
	public void setApp(VTransApp app)
	{
	  _vtransApp = app;
	}
	
	@TargetApi(Build.VERSION_CODES.FROYO)
  private void addScaleGestureDetector()
	{
    //http://stackoverflow.com/questions/5375817/android-pinch-zoom
    _scaleDetector = new ScaleGestureDetector(getContext(), 
      new OnScaleGestureListener()
      {
        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
        }
        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            return true;
        }
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
          final float scaleFactor = detector.getScaleFactor();
          
          float currentTextSize = _textPaint.getTextSize();        
          final float newTextSize = currentTextSize * scaleFactor;
          
          if( _vtransApp != null && 
              _vtransApp.textHeightIsInRange(newTextSize)
              )
          {
            currentTextSize = newTextSize;          
          }
          
          Log.d("ColouredTextView.ScaleGestureDetector", "zoom ongoing, scale: " 
            + scaleFactor + " text size:" + currentTextSize);
          
          if( scaleFactor != 1.0f )
          {
            setTextSize(currentTextSize);
  //          _app._textSize = currentTextSize;
            determineHeight();
  //          measure(_widthMeasureSpec, _heightMeasureSpec);
            //http://stackoverflow.com/questions/6946478/what-triggers-a-views-measure-to-be-called
            requestLayout();
            invalidate(); //force redraw
          }
          return false;
        }
      });
	}
	
	protected void init()
	{
//	  _app = (App) getContext().getApplicationContext().getApplication();
	  /** If not set then the background color e.g. may be black. */
	  setBackgroundColor(Color.WHITE);
	  
	  addScaleGestureDetector();
	  
		_textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//		setTextSize(_app._textSize);
//		_textPaint.setTextAlign(Align.);
//		textPaint.setColor(r.getColor(R.color.text_color));
	}
	
	@Override
	public void setMinimumHeight(int minHeight) {
	  super.setMinimumHeight(minHeight);
	  _minHeightInPixels  = minHeight;
	}
	
  public void setTextSize(final float textSize) {
    _textPaint.setTextSize(textSize);
    if(_vtransApp._maximumTextHeightInPixelsEditText < textSize)
    {
      //else zooming is impossible due to range checks.
      _vtransApp._maximumTextHeightInPixelsEditText = textSize;
    }
    _fontHeightFromBaseLine = ITranslatedTextProcessor.getFontHeightFromBaseLine(
        _textPaint.getFontMetrics());
  }
	
	@Override 
	protected void onDraw(Canvas canvas)
	{
	  //get the size of the outer scroll view
//	  getParent().
//		String word = "�g";
		
		Rect textBounds = new Rect();
//		_textPaint.getTextBounds(word, 0, word.length(), textBounds);
		
//		_roundRectPaint.setColor(Color.RED);
//		canvas.get
		Log.v(this.getClass().getName() + " onDraw", "textBounds.right:" + 
	    textBounds.right +  " textBounds.bottom" + textBounds.bottom );
				
//		rect = new RectF(15,15, 70, 100);
//		canvas.drawRoundRect(rect, 4, 3, _roundRectPaint);
//		
//		canvas.drawText(word, 0, fontHeightFromBaseLine, _textPaint);
//		TranslatedTextDrawer translatedTextDrawer = new TranslatedTextDrawer(
//	    _translatedText, _textPaint, canvas, _currentWidth);
		_translatedTextDrawer.set(_textPaint);
		_translatedTextDrawer.set(canvas);
		_translatedTextDrawer.setWidthInPixels(_currentWidth);
		_translatedTextDrawer.process();
		
//	  _translateButton.setText("translate");
		//http://stackoverflow.com/questions/6535648/how-can-i-dynamically-set-the-position-of-view-in-android
//		Spinner spinner = new Spinner( getContext() );
//		spinner.setAdapter(adapter)
//		getContext().getContentView().
//		setLeft
//		spinner.set
//		spinner.set
	}

    protected void onLayout(boolean changed,
             int left,
             int top,
             int right,
             int bottom)
    {
        Log.v(this.getClass().getName(), " onLayout new:" + left + "left" + top +
                " top:" + right + "right" + bottom);
    }
    /** from http://stackoverflow.com/questions/6652400/how-can-i-get-the-canvas-size-of-a-custom-view-outside-of-the-ondraw-method
    /** Is called when the size of the View is changed while the _same_ activity
        is executed , e.g. due to a soft keyboard (dis)appearance.
        It is NOT called when e.g. returning from another activity and the software keyboard
        disappeared. */
	protected void onSizeChanged(int newWidthInPixels, int newHeightInPixels,
        int oldWidthInPixels, int oldHeightInPixels)
    {
        _currentWidth = newWidthInPixels;
        _currentHeight = newHeightInPixels;
        super.onSizeChanged(newWidthInPixels, newHeightInPixels, oldWidthInPixels, oldHeightInPixels);
        Log.v(this.getClass().getName(), " onSizeChanged new:" + newWidthInPixels + "x" + newHeightInPixels +
          " old:" + oldWidthInPixels + "x" + oldHeightInPixels);
	}
	
  //http://stackoverflow.com/questions/5375817/android-pinch-zoom
  @Override
  public boolean onTouchEvent(MotionEvent event) {
      _scaleDetector.onTouchEvent(event);
      return true;
  }
  
	protected int getSuggestedMinimumHeight() 
	{
	  return (int) _fontHeightFromBaseLine;
	}
	
	public void setParentView(final View view)
	{
	  _parentView = view;
	}
//	and getSuggestedMinimumWidth()). 
	
	/** https://groups.google.com/forum/#!topic/android-developers/W9_7WjqlKXc
	* "CONTRACT: When overriding this method, you must call 
	* setMeasuredDimension(int, int) to store the measured width and height of 
	* this view. Failure to do so will trigger an IllegalStateException, thrown 
	* by measure(int, int). Calling the superclass' onMeasure(int, int) is a 
	* valid use. */
	@Override
  protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec) 
	{
	  _widthMeasureSpec = widthMeasureSpec;
    this._heightMeasureSpec = heightMeasureSpec;
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	  
	  //from http://stackoverflow.com/questions/12266899/onmeasure-custom-view-explanation
	  final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
	  final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
	  final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
	  final int heightSize = MeasureSpec.getSize(heightMeasureSpec);
//  final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
//  final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
    
	  int width = 100;
    int height = (int) _fontHeightFromBaseLine;

	  switch( widthMode )
	  {
	  case MeasureSpec.EXACTLY:
	    width = widthSize;
	    break;
	  case MeasureSpec.AT_MOST:
	    //Can't be bigger than...
      width = Math.min(width, widthSize);
      break;
	  }
    _currentWidth = width;
    
	  switch(heightMode)
	  {
	  case MeasureSpec.EXACTLY:
	    height = heightSize;
	    break;
	  case MeasureSpec.AT_MOST:
	    //Can't be bigger than...
      height = Math.min(height, heightSize);
      break;
	  case MeasureSpec.UNSPECIFIED : /** If e.g. inside a ScrollView. */
	    height = determineHeight();
//	    if(_parentView != null )
//	    {
//  	    final int parentViewHeigthInPixels = _parentView.getHeight();
//  //	    getParent().get
//  	    height = Math.max(/*_minHeightInPixels*/ parentViewHeigthInPixels, height);
//	    }
	    break;
	  }
	  
    setMeasuredDimension(/*_currentWidth*/ width, height);   
    Log.v(this.getClass().getName(), " onMeasure setMeasuredDimension" + width + " " + height);
  }
	
	public int determineHeight()
	{
	  if( _translatedText != null )
    {
//  	  TranslatedTextHeightCalculator translatedTextHeightCalculator = new 
//        TranslatedTextHeightCalculator(_translatedText, _textPaint, 
//        _currentWidth);
	    _translatedTextHeightCalculator.set(_textPaint);
	    _translatedTextHeightCalculator.setWidthInPixels(_currentWidth);
	    
//	    _translatedTextHeightCalculator.set(_translatedText);
  	  _translatedTextHeightCalculator.process();
  	  int heightInPixels = _translatedTextHeightCalculator.getCalculatedHeight();
      if(_parentView != null )
      {
        final int parentViewHeigthInPixels = _parentView.getHeight();
        heightInPixels = Math.max(
          /*_minHeightInPixels*/ parentViewHeigthInPixels, heightInPixels);
      }  	  
      return heightInPixels;
    }
	  return 0;
	}
	
  public void setXMLdata(String string) {
	  m_xmlData = string;	  
  }

  public void setTranslatedText(
    vtrans.dynlib.attributes.TranslatedText translatedText) {
    _translatedText = translatedText;
    
    _translatedTextHeightCalculator.set(translatedText);
    _translatedTextDrawer._wordAndGrammarPartNameVector = 
      _translatedTextHeightCalculator._wordAndGrammarPartNameVector;
//    set(translatedText);
  }
  
  void buildGUI()
  {
//  LinearLayout ll = new LinearLayout(this);
//  ll.setOrientation(LinearLayout.VERTICAL);
//  
//  final int minSize = LinearLayout.LayoutParams.WRAP_CONTENT;
//  int lHeight = LinearLayout.LayoutParams.WRAP_CONTENT;
//  int lWidth = LinearLayout.LayoutParams.MATCH_PARENT;      
//  LinearLayout.LayoutParams textLayoutParams = new 
//    LinearLayout.LayoutParams(lWidth, lHeight);
//  
//  LinearLayout.LayoutParams buttonLayoutParams = new 
//    LinearLayout.LayoutParams(minSize, minSize);
//  
//  ll.addView(_englishText, textLayoutParams);
//  ll.addView(_translateButton, buttonLayoutParams);
//  //ll.addView(_textView, textLayoutParams);
//  ll.addView(_germanText, textLayoutParams);
  }

  @Override
  public boolean onTouch(View v, MotionEvent event) {
    // TODO Auto-generated method stub
    float x = event.getX();
    return false;
  }
}

