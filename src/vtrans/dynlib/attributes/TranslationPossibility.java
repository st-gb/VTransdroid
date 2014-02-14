package vtrans.dynlib.attributes;

import java.util.Iterator;
import java.util.Vector;

public class TranslationPossibility
{
  Vector<WordAndGrammarPartName> _words = new Vector<WordAndGrammarPartName>();
  public void add(WordAndGrammarPartName wordAndGrammarPartName)
  {
    _words.addElement(wordAndGrammarPartName);
  }
  public Iterator<WordAndGrammarPartName> getIterator() {
    return _words.iterator();
  }
}
