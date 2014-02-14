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
        _roundRectPaint.setColor(Color.LTGRAY);
      drawString(wagpn._strTranslation + " ", _canvas);
    }
    return s;
  }
    
  private void drawString(final String word, Canvas canvas)
  {
    _lastWordTextBounds = new Rect();
    _textPaint.getTextBounds(word, 0, word.length(), _lastWordTextBounds);
    
    if( _currentX + _lastWordTextBounds.right > _currentViewWidth )
    {
      _currentY += //_lastWordTextBounds.bottom;
        _fullFontHeight;
      Log.v("drawString", "new Y:" + _currentY);
      _currentX = 0.0f;
    }
    
    RectF rect = new RectF(_currentX , _currentY, _lastWordTextBounds.right, 
      _fontHeightFromBaseLine + _lastWordTextBounds.bottom);
    
    canvas.drawRoundRect(rect, 4, 3, _roundRectPaint);
    
    canvas.drawText(word, _currentX, _fontHeightFromBaseLine + _currentY, _textPaint);
    
    _currentX += _lastWordTextBounds.right + _spaceCharTextBounds.right;
//    canvas.s
  }

  @Override
  protected void processWord(String word) {
    drawString(word, _canvas);
  }
}
