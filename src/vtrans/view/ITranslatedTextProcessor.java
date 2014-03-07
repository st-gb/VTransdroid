package vtrans.view;

import java.util.Iterator;
import java.util.Vector;

import vtrans.dynlib.attributes.TranslatedText;
import vtrans.dynlib.attributes.TranslationPossibilities;
import vtrans.dynlib.attributes.TranslationPossibility;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.FontMetrics;
import android.util.Log;

/** Base class of e.g. classes for drawing and measuring the text height. */
public abstract class ITranslatedTextProcessor {

  protected int _currentViewWidth;
  protected Paint _textPaint;
  protected TranslatedText _translatedText;
  protected float _fontHeightFromBaseLine;
  protected float _currentY = 0.0f;
  protected float _currentX = 0.0f;
  protected Rect _spaceCharTextBounds = new Rect();
  protected float _fullFontHeight;
  Rect _lastWordTextBounds;
//  protected VTransApp _vtransApp;

  ITranslatedTextProcessor(
    final TranslatedText translatedText, 
    Paint textPaint, 
    int viewWidthInPixels//,
//    VTransApp vtransApp
    )
  {
//    _vtransApp = vtransApp;
    _translatedText = translatedText;
    _currentViewWidth = viewWidthInPixels;
    _textPaint = textPaint;
    
    final String spaceChar = "_";
    _textPaint.getTextBounds(spaceChar, 0, spaceChar.length(), 
      _spaceCharTextBounds);
    Log.v("ITranslatedTextProcessor", "space char text bounds:" + 
      _spaceCharTextBounds.toString() );
    
    final FontMetrics fontMetrics = _textPaint.getFontMetrics();
    _fontHeightFromBaseLine = getFontHeightFromBaseLine(fontMetrics);
    _fullFontHeight = getFullFontHeight(fontMetrics);
    Log.v("ITranslatedTextProcessor", "full font height:" + _fullFontHeight );
    
//    final FontMetrics fontMetrics = _textPaint.getFontMetrics();
//    _fontHeightFromBaseLine = getFontHeightFromBaseLine(fontMetrics);
  }
  
  protected abstract void processWord(final String word/*, final Canvas canvas*/);
  protected abstract String addTranslations(final TranslationPossibility tp);
  
  public static float getFontHeightFromBaseLine(final FontMetrics fontMetrics)
  {
    Log.v(ITranslatedTextProcessor.class.getName() + " getFontHeightFromBaseLine", 
      "font ascent:" + fontMetrics.ascent);
    final float fontHeightFromBaseLine = fontMetrics.ascent * -1.0f;
    return fontHeightFromBaseLine;
  }
  
  public static float getFullFontHeight(final FontMetrics fontMetrics)
  {
    Log.v( ITranslatedTextProcessor.class.getName(), " getFullFontHeight " +
  		"font ascent:" + fontMetrics.ascent);
    /** see http://porcupineprogrammer.blogspot.de/2012/10/android-font-metrics-for-dummies.html
     *  gGÖ    ascent: from baseLine (0.0, below "Ö" upwards to the dots)
     *         leading: from the upper circle of "g" downwards
               descent: from baseLine (.0.0, below "G") upwards */
    final float fullFontHeight = fontMetrics.ascent * -1.0f + fontMetrics.leading;
    return fullFontHeight;
  }
  
  public void process()
  {
    if( _translatedText != null )
    {
      Iterator<TranslationPossibilities> iterSerialTranslPoss = 
        _translatedText.getVector().iterator();
      while( iterSerialTranslPoss.hasNext() )
      {
        Log.v(this.getClass().getName(), "iterSerialTranslPoss.hasNext()" );
        TranslationPossibilities tps = iterSerialTranslPoss.next();
        Vector<TranslationPossibility> tpVec = tps.getVector();
        Log.v(this.getClass().getName(), "tpVec.size():" + tpVec.size() );
        /** More than 1 translation possibility */
        if( tpVec.size() > 1 )
        {
          processWord("{");
          Iterator<TranslationPossibility> translPossIter = tps.getVector().
            iterator();
          TranslationPossibility tp = translPossIter.next();
          addTranslations(tp);
          while(translPossIter.hasNext() )
          {
            processWord(";");
            tp = translPossIter.next();
            addTranslations(tp);
          }
          processWord("}");
        }
        else if(tpVec.size() > 0 )
        {
    //      Vector<WordAndGrammarPartName> translation = tpVec.elementAt(0);
          TranslationPossibility tp = tpVec.elementAt(0);
          addTranslations(tp);
        }
      }
    }
  }
}
