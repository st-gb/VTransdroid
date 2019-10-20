package vtrans.dynlib;

//import HierarchicObjectsFromXMLgenerator;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import vtrans.dynlib.attributes.TranslationPossibilities;
import vtrans.dynlib.attributes.WordAndGrammarPartName;
import vtrans.dynlib.attributes.TranslatedText;
import vtrans.dynlib.attributes.TranslationPossibility;

public class HierarchicObjectsFromXMLgenerator
  extends
  /** Use the full name because "DefaultHandler" does not tell very much about it.*/
  org.xml.sax.helpers.DefaultHandler
{
  boolean _bTranslationXMLelement = false;
  String _xmlData;
  java.util.TreeMap<String,String> _grammarpartname2CSSname;
  private String _strCurrentXMLelement;
  private String _strGrammarPartName;
  
  //private Vector<WordAndGrammarPartName> _vector = new Vector<WordAndGrammarPartName>();
  //public Vector<Vector> _translationsVector = new Vector<Vector>();
  private TranslationPossibility _translationPossibility = null;
  private TranslationPossibilities _translationPossibilities = null;
  public TranslatedText _translatedText = new TranslatedText();
  
  public HierarchicObjectsFromXMLgenerator(final String xmlData)
  {
    _xmlData = xmlData;
  //  Logger.getLogger( this.getClass().getName() ).log(Level.INFO, null );
  }
  
  public TranslatedText generateTranslatedText()
  {
    InputSource is = new InputSource(new StringReader(_xmlData) );
    //from http://www.mkyong.com/java/how-to-read-xml-file-in-java-sax-parser/
    SAXParserFactory factory = SAXParserFactory.newInstance();
    SAXParser saxParser;
    try {
      saxParser = factory.newSAXParser();
//      HierarchicObjectsFromXMLgenerator sax2s = new HierarchicObjectsFromXMLgenerator(xml);
      saxParser.parse(is, this);
    //  _translations = sax2s._translationsVector;
//      TranslatedText tt = _translatedText;
    } catch (ParserConfigurationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (SAXException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return _translatedText;
  }
  
  ///** This callback method is called when characters between an XML start and 
  //* XML end tag appear. */
  //public void characters( char[] ar_ch, int start, int length)
  //  throws org.xml.sax.SAXException 
  //{
  //  if( _bTranslationXMLelement)
  //  {
  ////      String strGermanTranslation = new String (ar_ch, start, length);
  ////      _vector.addElement(new WordAndGrammarPartName(strGermanTranslation, ) );
  //  }
  //}
  
  public void endElement(String uri, String localName, String qName)
    //throws SAXException
  {
    if( _bTranslationXMLelement )
    {
      _bTranslationXMLelement = false;
    }
    if( //_strCurrentXMLelement
        localName.equals(//"sentence"
          "translation")
      )
    {
  //    _translationsVector.addElement(_vector);
      _translationPossibilities.add(_translationPossibility);
  //    _vector = new Vector<WordAndGrammarPartName>();
  //    _translationPossibility = new TranslationPossibility();
    }
    if( //_strCurrentXMLelement
        localName.equals(//"sentence"
          "translations")
      )
    {
      _translatedText.add(_translationPossibilities);
  //    _translationPossibilities = new TranslationPossibilities();
    }
  //    }
  //    catch(SAXException sax_e)
  //    {
  //      
  //    }
  }
  
  public void fatalError (org.xml.sax.SAXParseException e)
    throws org.xml.sax.SAXException
  {
  //  Logger.getLogger("ColourWordKindSAX2Handler").log(Level.SEVERE, 
  //    e.toString() );
    //throw e;
  }
    
  public void startElement (
    String uri, 
    String localName,
    String qName, 
    org.xml.sax.Attributes attributes
    )
    throws org.xml.sax.SAXException
  {
    String strGrammarPartName;
    //_strCurrentXMLelement = localName;
    if( localName.equals("translation") )
    {
      _translationPossibility = new TranslationPossibility();
    }
    else if( localName.equals("translations") )
    {
      _translationPossibilities = new TranslationPossibilities();
    }
    else if( localName.equals(
        //"word" is the leaf XML element of the parse tree.
        "word")
      )
    {
      _bTranslationXMLelement = true;
      strGrammarPartName = //attributes.getValue("grammar_part_name");
        //Name of the previous XML element.
        //_strCurrentXMLelement;
        //Name of the previous "grammar part" XML element's "name" attribute.
        _strGrammarPartName;
      if( strGrammarPartName != null )
      {
        String strTranslation = attributes.getValue("translation");
  //      _vector.addElement(new WordAndGrammarPartName(strTranslation, 
  //        strGrammarPartName) );
        final String concatenation_ID = attributes.getValue("concatenation_ID");
        _translationPossibility.add(new WordAndGrammarPartName(
          strTranslation, strGrammarPartName, concatenation_ID) );
      }
    }
    else
    {
      _bTranslationXMLelement = false;
      _strCurrentXMLelement = localName;
      if( localName.equals("parseTreeNode") )
        _strGrammarPartName = attributes.getValue("name");
    }
      //http://de.wikipedia.org/wiki/Cascading_Style_Sheets#Beispiel:
  }  
}
