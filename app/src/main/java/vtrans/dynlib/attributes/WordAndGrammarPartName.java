package vtrans.dynlib.attributes;

public class WordAndGrammarPartName
  /** For inserting into a TreeSet. */
  implements Comparable<WordAndGrammarPartName>
{

	public String _strTranslation;
	public String _strGrammarPartName;
	public int _concatenation_ID = 0;

	public WordAndGrammarPartName(String strTranslation, String strGrammarPartName)
	{
		this._strTranslation = strTranslation;
		this._strGrammarPartName = strGrammarPartName;
	  
  }

  public WordAndGrammarPartName(
    final String strTranslation,
    final String strGrammarPartName, 
    final String concatenation_ID)
  {
    _strTranslation = strTranslation;
    _strGrammarPartName = strGrammarPartName;
    if( concatenation_ID != null )
      _concatenation_ID = Integer.parseInt(concatenation_ID);
  }

  @Override
  public int compareTo(WordAndGrammarPartName another) {
//    int compareResult = _strGrammarPartName.compareTo(another._strGrammarPartName);
//    if( compareResult != 0 )
//      return compareResult;
    int compareResult = _strTranslation.compareTo(another._strTranslation);
    return compareResult;
  }
}
