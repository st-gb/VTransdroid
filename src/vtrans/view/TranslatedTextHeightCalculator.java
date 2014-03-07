package vtrans.view;

import java.util.Iterator;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import vtrans.dynlib.attributes.TranslatedText;
import vtrans.dynlib.attributes.TranslationPossibility;
import vtrans.dynlib.attributes.WordAndGrammarPartName;

public class TranslatedTextHeightCalculator
  extends ITranslatedTextProcessor
{

  TranslatedTextHeightCalculator(
    final TranslatedText translatedText, 
    final Paint textPaint,
    final int viewWidthInPixels)
  {
    super(translatedText, textPaint, viewWidthInPixels);
  }
  
  @Override
  protected void processWord(String word)
  {
    _lastWordTextBounds = new Rect();
    _textPaint.getTextBounds(word, 0, word.length(), _lastWordTextBounds);
    
    if( _currentX + _lastWordTextBounds.right > _currentViewWidth )
    {
      _currentY += //_lastWordTextBounds.bottom;
        _fullFontHeight;
      Log.v("TranslatedTextHeightCalculator", "processWord new Y:" + _currentY);
      _currentX = 0.0f;
    }
    
//    RectF rect = new RectF(_currentX , _currentY, _lastWordTextBounds.right, 
//      _fontHeightFromBaseLine + _lastWordTextBounds.bottom);
        
    _currentX += _lastWordTextBounds.right + _spaceCharTextBounds.right;
  }

  @Override
  protected String addTranslations(TranslationPossibility tp) {
    Iterator<WordAndGrammarPartName> iterWords = tp.getIterator();
    String s = "";
    while( iterWords.hasNext() )
    {
      WordAndGrammarPartName wagpn = iterWords.next();
      processWord(wagpn._strTranslation + " ");
    }
    return null;
  }

  public int getCalculatedHeight() {
    final float calculatedHeight = _currentY + _fullFontHeight;
    return (int) (calculatedHeight);
  }

}
