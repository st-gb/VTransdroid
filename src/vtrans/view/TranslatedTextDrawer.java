package vtrans.view;

import java.util.Iterator;

import vtrans.dynlib.attributes.TranslatedText;
import vtrans.dynlib.attributes.TranslationPossibility;
import vtrans.dynlib.attributes.WordAndGrammarPartName;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

public class TranslatedTextDrawer
  extends ITranslatedTextProcessor
{

  private Canvas _canvas;
  private Paint _roundRectPaint;
  private boolean _wroteAtLeast1WordInLineYet = false;

  public TranslatedTextDrawer(
    final TranslatedText translatedText,
    final Paint textPaint,
    Canvas canvas,
    final int viewWidthInPixels
    )
  {
    super(translatedText, textPaint, viewWidthInPixels);
    _roundRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    _roundRectPaint.setStyle(Paint.Style.FILL);
    _canvas = canvas;
  }
  
  protected String addTranslations(final TranslationPossibility tp) {
    Iterator<WordAndGrammarPartName> iterWords = tp.getIterator();
    String s = "";
    while( iterWords.hasNext() )
    {
      WordAndGrammarPartName wagpn = iterWords.next();
      if( wagpn._strGrammarPartName.equals("UnknownWord") )
      {
        _roundRectPaint.setColor(Color./*rgb(0, 0, 128)*/
            parseColor("#a0a0ff") );
      }
      else if( wagpn._strGrammarPartName.equals("singular_noun") )
      {
        _roundRectPaint.setColor(Color.parseColor("#aaaaaa") );
      }
      else
        _roundRectPaint.setColor(/*Color.LTGRAY*/ 
          //_canvas.get
            //TODO use View's background color
            Color.WHITE
          );
      drawString(wagpn._strTranslation + " ", _canvas);
    }
    return s;
  }
    
  private void drawString(final String word, Canvas canvas)
  {
    _lastWordTextBounds = new Rect();
    _textPaint.getTextBounds(word, 0, word.length(), _lastWordTextBounds);
    
    if( /** Prevent drawing a word 1 line lower if it does not fit into line*/
        _wroteAtLeast1WordInLineYet && 
        _currentX + _lastWordTextBounds.right > _currentViewWidth )
    {
      _currentY += _lastWordTextBounds.bottom +
        _fullFontHeight;
      Log.v(this.getClass().getName() + " drawString", "new Y:" + _currentY);
      _currentX = 0.0f;
      _wroteAtLeast1WordInLineYet = false;
    }

    final int rightEnd = (int) _currentX + _lastWordTextBounds.right;
    final int bottom = (int) (_currentY + /*_fontHeightFromBaseLine*/
      _fullFontHeight) + 
      _lastWordTextBounds.bottom;
    RectF rect = new RectF(_currentX , _currentY 
      //TODO possibly start a little bit below (->add pixels)
      //  + 
      , rightEnd, 
//      _fontHeightFromBaseLine + _lastWordTextBounds.bottom
      bottom
      );
    
    canvas.drawRoundRect(rect, 4, 3, _roundRectPaint);
    
    canvas.drawText(word, _currentX, //_fontHeightFromBaseLine + _currentY
      _currentY + _fullFontHeight , _textPaint);
    _wroteAtLeast1WordInLineYet = true;
    
    _currentX += _lastWordTextBounds.right + _spaceCharTextBounds.right;
//    canvas.s
  }

  @Override
  protected void processWord(String word) {
    _roundRectPaint.setColor(/*Color.LTGRAY*/ 
        //_canvas.get
        //TODO use View's background color
        Color.WHITE
      );
    drawString(word, _canvas);
  }
}
