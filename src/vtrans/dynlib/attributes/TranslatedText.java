package vtrans.dynlib.attributes;

import java.util.Iterator;
import java.util.Vector;

public class TranslatedText
  {
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
