package vtrans.dynlib.attributes;

import java.util.Iterator;
import java.util.Vector;

/** = the WHOLE translation. parse tree in a row? "e.g. for text "the cow the cats":
 *    "the cow" and  "the cats" */
public class TranslatedText
{
  /** e.g. for whole text "the cow the cats" 2 elements 
   *  "the cow" and "the cats"*/
  Vector<TranslationPossibilities> _serialTranslationPossibilities = new
      Vector<TranslationPossibilities>();
  
  public void add(TranslationPossibilities translationPossibilities) {
    _serialTranslationPossibilities.addElement(translationPossibilities);
  }
  
  public int size() {
    return _serialTranslationPossibilities.size();
  }
  
  public Vector<TranslationPossibilities> getVector() {
    return _serialTranslationPossibilities;
  }
}
