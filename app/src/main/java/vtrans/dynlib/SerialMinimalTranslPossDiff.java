package vtrans.dynlib;

import java.util.Iterator;
import java.util.Vector;

import vtrans.dynlib.attributes.WordAndGrammarPartName;

public class SerialMinimalTranslPossDiff {
  Vector<MinimalTranslPossDiff> _vec = new Vector<MinimalTranslPossDiff>();

  public void addElement(final MinimalTranslPossDiff minDiff) {
    _vec.addElement(minDiff);
  }

  public Vector<WordAndGrammarPartName> serialalizeGrammartPart()
  {
    Vector<WordAndGrammarPartName> retVec = new Vector<WordAndGrammarPartName>();
    Iterator<MinimalTranslPossDiff> iter = _vec.iterator();
    
    while(iter.hasNext() )
    {
      final MinimalTranslPossDiff minimalTranslPossDiff = iter.next();
      retVec.addAll(minimalTranslPossDiff.serialalizeGrammartPart() );
    }
    return retVec;
  }
}
