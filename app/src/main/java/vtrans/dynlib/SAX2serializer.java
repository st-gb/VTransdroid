package vtrans.dynlib;

import java.io.IOException;
import java.io.StringReader;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import vtrans.dynlib.attributes.TranslatedText;
import vtrans.dynlib.attributes.TranslationPossibilities;
import vtrans.dynlib.attributes.TranslationPossibility;
import vtrans.dynlib.attributes.WordAndGrammarPartName;

/**
*
* @author Stefan
*/
public class SAX2serializer
{
  private static String addTranslations(TranslationPossibility tp) {
    Iterator<WordAndGrammarPartName> iterWords = tp.getIterator();
    String s = "";
    while( iterWords.hasNext() )
    {
      WordAndGrammarPartName wagpn = iterWords.next();
      s += wagpn._strTranslation + " ";
    }
    return s;
  }

  public static SerialMinimalTranslPossDiff serializeTranslationPossibilities(
    final TranslatedText translatedText)
  {
    SerialMinimalTranslPossDiff serialMinimalTranslPossDiff = new //Vector<MinimalTranslPossDiff>();
      SerialMinimalTranslPossDiff();
    Iterator<TranslationPossibilities> iterSerialTranslPoss = 
        translatedText.getVector().iterator();
      while( iterSerialTranslPoss.hasNext() )
      {
        /** "e.g. for text "the car the cats":
         *   "the car" */
        TranslationPossibilities tps = iterSerialTranslPoss.next();
        /** e.g. German translation poss. for "the car": "das Auto", "der Fahrkorb" */
//        Vector<TranslationPossibility> tpVec = tps.getVector();
//        if( tpVec.size() > 1 )
        {
          final Vector<TranslationPossibility> translationPossibilityVector = 
            tps.get_translationPossibilities();
          MinimalTranslPossDiff minDiff = new MinimalTranslPossDiff(
            translationPossibilityVector);
          minDiff.create();
          serialMinimalTranslPossDiff.addElement(minDiff);
        }
      }
    return serialMinimalTranslPossDiff;
  }
  
  public static String serializeXML(final String xmlData)
  {
    //XMLReader 
    String s = "";
    
    vtrans.dynlib.HierarchicObjectsFromXMLgenerator hierarchicObjectsFromXMLgenerator = new 
      HierarchicObjectsFromXMLgenerator(xmlData);
    final TranslatedText translatedText = hierarchicObjectsFromXMLgenerator.
      generateTranslatedText();

  //  final int numTranslations = _translations.size();
  //  Iterator<WordAndGrammarPartName> iters [] = new Iterator<WordAndGrammarPartName>
  //    [numTranslations];
    
    Iterator<TranslationPossibilities> iterSerialTranslPoss = 
      translatedText.getVector().iterator();
    while( iterSerialTranslPoss.hasNext() )
    {
      /** "e.g. for text "the car the cats":
       *   "the car" */
      TranslationPossibilities tps = iterSerialTranslPoss.next();
      /** e.g. German translation poss. for "the car": "das Auto", "der Fahrkorb" */
      Vector<TranslationPossibility> tpVec = tps.getVector();
      if( tpVec.size() > 1 )
      {
//        s += "{";
//        Iterator<TranslationPossibility> translPossIter = tps.getVector().iterator();
//        TranslationPossibility tp = translPossIter.next();
//        s += addTranslations(tp);
//        while(translPossIter.hasNext() )
//        {
//          s += ";";
//          tp = translPossIter.next();
//          s += addTranslations(tp);
//        }
//        s += "}";
        
        MinimalTranslPossDiff minDiff = minimizeToDifferences(tpVec);
        
      }
      else if(tpVec.size() > 0 )
      {
  //      Vector<WordAndGrammarPartName> translation = tpVec.elementAt(0);
        TranslationPossibility tp = tpVec.elementAt(0);
        s += addTranslations(tp);
      }
    }
  return s;
  }

  /** @param tpVec: e.g. German translation poss. for "the car": "das Auto", 
   *   "der Fahrkorb" */
  private static MinimalTranslPossDiff minimizeToDifferences(
    final Vector<TranslationPossibility> tpVec)
  {
//    Vector<>
    MinimalTranslPossDiff mindiff = new MinimalTranslPossDiff(tpVec);
    mindiff.create();
    return mindiff;
  }
}
