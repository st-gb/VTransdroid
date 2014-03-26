package vtrans.dynlib;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

import vtrans.dynlib.attributes.TranslationPossibility;
import vtrans.dynlib.attributes.WordAndGrammarPartName;

class MinimalTranslPossDiff
{
  /** a set ensure no equal WordAndGrammarPartName are possible. */
  Map<Integer, Set<WordAndGrammarPartName> > _map = new TreeMap<Integer, 
    Set<WordAndGrammarPartName>>();
  
  Vector<TranslationPossibility> _translationPossibilityVector;
  
  public MinimalTranslPossDiff(
    final Vector<TranslationPossibility> translationPossibilityVector ) {
    _translationPossibilityVector = translationPossibilityVector;
  }

  public void create()
  {
//    if( _translationPossibilityVector.size() > 1 )
    {
      /** e.g. "das Auto" */
//      final TranslationPossibility firstTranslPoss = 
//        _translationPossibilityVector.elementAt(0);
      Iterator<TranslationPossibility> translPossIter = 
        _translationPossibilityVector.iterator();
//      for( int tpIndex = 0; tpIndex < tpVec.size(); ++ tpIndex )
      while( translPossIter.hasNext() )
      {
        /** e.g. "der Fahrkorb" */
        TranslationPossibility tp = translPossIter.next();
//        if( ! tp.equals(firstTranslPoss) )
        {
//          Iterator<WordAndGrammarPartName> firstTranslPossIter = firstTranslPoss.getIterator();
          Iterator<WordAndGrammarPartName> currentTranslPossIter = tp.getIterator();
          int currentTranslPossIndex = 0;
          while( /*firstTranslPossIter.hasNext() &&*/ currentTranslPossIter.hasNext() )
          {
//            WordAndGrammarPartName firstTranslPossWordAndGrammarPartName = firstTranslPossIter.next();
            final WordAndGrammarPartName currentTranslPossWordAndGrammarPartName
              = currentTranslPossIter.next();
//            if( firstTranslPossWordAndGrammarPartName != currentTranslPossWordAndGrammarPartName )
            {
              add( currentTranslPossWordAndGrammarPartName, currentTranslPossIndex);
            }
            ++ currentTranslPossIndex;
          }
        }
      }
    }

  }
  
  public void add(final WordAndGrammarPartName translPossWordAndGrammarPartName, 
      final int index)
  {
    Set<WordAndGrammarPartName> set = _map.get(index);
    if( set == null )
    {
      set = new TreeSet<WordAndGrammarPartName>();
      _map.put(index, set);
    }
    set.add(translPossWordAndGrammarPartName);
  }

  public Vector<WordAndGrammarPartName> serialalizeGrammartPart() {
    Vector<WordAndGrammarPartName> vec = new Vector<WordAndGrammarPartName>();
    
    Collection<Set<WordAndGrammarPartName>> coll = _map.values();
    Iterator<Set<WordAndGrammarPartName>> iter = coll.iterator();
    while(iter.hasNext() )
    {
      Set<WordAndGrammarPartName> set = iter.next();
      if( set.size() > 1 )
      {
        vec.addElement(new WordAndGrammarPartName("{", "" ) );
        Iterator<WordAndGrammarPartName> wordAndGrammarPartNameIter = 
          set.iterator();
        WordAndGrammarPartName wordAndGrammarPartName = 
          wordAndGrammarPartNameIter.next();
        vec.addElement( wordAndGrammarPartName);
        while( wordAndGrammarPartNameIter.hasNext() )
        {
          vec.addElement(new WordAndGrammarPartName(";", "") );
          wordAndGrammarPartName = wordAndGrammarPartNameIter.next();
          vec.addElement(wordAndGrammarPartName );
        }
        vec.addElement(new WordAndGrammarPartName("}", "") );
      }
      else if( set.size() == 1 )
      {
        Iterator<WordAndGrammarPartName> wordAndGrammarPartNameIter = 
            set.iterator();
        final WordAndGrammarPartName wordAndGrammarPartName = 
          wordAndGrammarPartNameIter.next();
        vec.addElement(wordAndGrammarPartName);
      }
    }
    return vec;
  }
  
}