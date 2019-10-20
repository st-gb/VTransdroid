package vtrans.dynlib;

public class InitFunction {

  public enum Init_return_codes
  {//TODO important:1st enum value has numeric value 0?
    success,
    vocabularyFilePathIsEmpty,
    loadingVocabularyFileFailed,
    creatingLogFileFailed,
    loadingMainConfigFileFailed
  };
  
  static final String InitMessage [] = new String [] { 
		"success",
		"vocabulary file path is empty",
		"loading vocabulary file failed",
		"creating log file failed",
		"loading main config file failed"
	};
  
	public static String getInitMessage(final byte by)
	{
		if( by < InitMessage.length )
			return InitMessage[by];
		return "";
	}
}
