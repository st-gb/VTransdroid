package vtrans.dynlib;

import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
*
* @author Stefan
*/
public class SAX2serializer
  extends
  //Use the full name because "DefaultHandler" does not tell very much about it.
  org.xml.sax.helpers.DefaultHandler
{
  boolean _bTranslationXMLelement = false;
  String _xmlData;
  java.util.TreeMap<String,String> _grammarpartname2CSSname;
  private String _strCurrentXMLelement;
  private String _strGrammarPartName;
  
//	private Vector<WordAndGrammarPartName> _vector = new Vector<WordAndGrammarPartName>();
//public Vector<Vector> _translationsVector = new Vector<Vector>();
	private TranslationPossibility _translationPossibility = null;
	private TranslationPossibilities _translationPossibilities = null;
	public TranslatedText _translatedText = new TranslatedText();

  public SAX2serializer(String xmlData)
  {
    _xmlData = xmlData;
//    Logger.getLogger( this.getClass().getName() ).log(Level.INFO, null );
  }

//  /** This callback method is called when characters between an XML start and 
//  * XML end tag appear. */
//  public void characters( char[] ar_ch, int start, int length)
//    throws org.xml.sax.SAXException 
//  {
//    if( _bTranslationXMLelement)
//    {
////        String strGermanTranslation = new String (ar_ch, start, length);
////        _vector.addElement(new WordAndGrammarPartName(strGermanTranslation, ) );
//    }
//  }
  
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
//      _translationsVector.addElement(_vector);
    	_translationPossibilities.add(_translationPossibility);
//      _vector = new Vector<WordAndGrammarPartName>();
//      _translationPossibility = new TranslationPossibility();
    }
    if( //_strCurrentXMLelement
        localName.equals(//"sentence"
          "translations")
      )
    {
    	_translatedText.add(_translationPossibilities);
//    	_translationPossibilities = new TranslationPossibilities();
    }
//	    }
//	    catch(SAXException sax_e)
//	    {
//	      
//	    }
  }
  
  public void fatalError (org.xml.sax.SAXParseException e)
    throws org.xml.sax.SAXException
  {
//    Logger.getLogger("ColourWordKindSAX2Handler").log(Level.SEVERE, 
//      e.toString() );
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
//        _vector.addElement(new WordAndGrammarPartName(strTranslation, 
//      		strGrammarPartName) );
        _translationPossibility.add(new WordAndGrammarPartName(
      		strTranslation, strGrammarPartName) );
      }
    }
    else
    {
      _bTranslationXMLelement = false;
      _strCurrentXMLelement = localName;
      if( localName.equals("grammar_part") )
        _strGrammarPartName = attributes.getValue("name");
    }
      //http://de.wikipedia.org/wiki/Cascading_Style_Sheets#Beispiel:
  }
  
	protected static String serializeXML(String xml)
	{
//	XMLReader 
	String s = "";
	InputSource is = new InputSource(new StringReader(xml) );
	//from http://www.mkyong.com/java/how-to-read-xml-file-in-java-sax-parser/
	SAXParserFactory factory = SAXParserFactory.newInstance();
	SAXParser saxParser;
  try {
    saxParser = factory.newSAXParser();
		SAX2serializer sax2s = new SAX2serializer(xml);
		saxParser.parse(is, sax2s);
//		_translations = sax2s._translationsVector;
		TranslatedText tt = sax2s._translatedText;
		
//		final int numTranslations = _translations.size();
//		Iterator<WordAndGrammarPartName> iters [] = new Iterator<WordAndGrammarPartName>
//		  [numTranslations];
		
		Iterator<TranslationPossibilities> iterSerialTranslPoss = tt.getVector().iterator();
		while( iterSerialTranslPoss.hasNext() )
		{
			TranslationPossibilities tps = iterSerialTranslPoss.next();
			Vector<TranslationPossibility> tpVec = tps.getVector();
			if( tpVec.size() > 1 )
			{
				s += "{";
				Iterator<TranslationPossibility> translPossIter = tps.getVector().iterator();
				TranslationPossibility tp = translPossIter.next();
				s += addTranslations(tp);
				while(translPossIter.hasNext() )
				{
					s += ";";
					tp = translPossIter.next();
					s += addTranslations(tp);
				}
				s += "}";
			}
			else if(tpVec.size() > 0 )
			{
	//			Vector<WordAndGrammarPartName> translation = tpVec.elementAt(0);
				TranslationPossibility tp = tpVec.elementAt(0);
				s += addTranslations(tp);
			}
		}
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
  return s;
}

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
}

class TranslationPossibility
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

class TranslationPossibilities
{
	Vector<TranslationPossibility> _translationPossibilities = new 
			Vector<TranslationPossibility>();

	public void add(TranslationPossibility translationPossibility) {
		_translationPossibilities.addElement(translationPossibility);	  
  }

	public Vector<TranslationPossibility> getVector() {
	  return _translationPossibilities;
  }
}

class TranslatedText
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
