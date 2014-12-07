VTRANS3_ROOT_PATH := ../../Vtrans3/

#	$(VTRANS3_ROOT_PATH)/VocabularyInMainMem/TUchemnitzEngWordSorted1st/BinarySearchInDictFile_test.cpp\
#	$(VTRANS3_ROOT_PATH)/Controller/DictReaderAndVocAccess/TUchemnitzEngWordSorted1stAndBinarySearch_test.cpp\
#	$(VTRANS3_ROOT_PATH)/IO/ConfigurationReader_test.cpp\

VTRANS3_SOURCE_FILES.MK_SOURCE_FILES := \
	$(VTRANS3_ROOT_PATH)/Attributes/EnglishWord.cpp\
	$(VTRANS3_ROOT_PATH)/Attributes/GermanWord.cpp\
	$(VTRANS3_ROOT_PATH)/Attributes/PositionString.cpp\
	$(VTRANS3_ROOT_PATH)/Attributes/Token.cpp\
	$(VTRANS3_ROOT_PATH)/Attributes/Word.cpp\
	$(VTRANS3_ROOT_PATH)/Controller/DictReaderAndVocAccess/TUchemnitzEngWordSorted1stAndBinarySearch.cpp\
	$(VTRANS3_ROOT_PATH)/Controller/DynLib/dynlib_main.cpp\
	$(VTRANS3_ROOT_PATH)/Controller/DynLib/vtrans_dynlib_VTransDynLibJNI.cpp\
	$(VTRANS3_ROOT_PATH)/Controller/TranslateControllerBaseReturnCodeDescriptons.cpp\
	$(VTRANS3_ROOT_PATH)/Controller/TranslationControllerBase.cpp\
	$(VTRANS3_ROOT_PATH)/IO/ConfigurationReader.cpp\
	$(VTRANS3_ROOT_PATH)/IO/configuration/MainConfigFileReaderBase.cpp\
	$(VTRANS3_ROOT_PATH)/IO/dictionary/DictionaryReaderBase.cpp\
	$(VTRANS3_ROOT_PATH)/IO/dictionary/OpenDictFileException.cpp\
	$(VTRANS3_ROOT_PATH)/IO/dictionary/TUchemnitz/EngWordSorted1st/BinarySearchInDictFile.cpp\
	$(VTRANS3_ROOT_PATH)/IO/dictionary/TUchemnitz/EngWordSorted1st/eachAttributeInSingleLine/EachAttributeInSingleLine.cpp\
	$(VTRANS3_ROOT_PATH)/IO/GenerateXMLtreeFromParseTree.cpp\
	$(VTRANS3_ROOT_PATH)/IO/MiniXML/GrammarRuleFileReader.cpp\
	$(VTRANS3_ROOT_PATH)/IO/MiniXML/MainConfigFileReader.cpp\
	$(VTRANS3_ROOT_PATH)/IO/MiniXML/TranslationRuleFileReader.cpp\
	$(VTRANS3_ROOT_PATH)/IO/MiniXML/TranslationRuleFile_EndXMLelement.cpp\
	$(VTRANS3_ROOT_PATH)/IO/MiniXML/VocAttributeDefintionHandler.cpp\
	$(VTRANS3_ROOT_PATH)/IO/MiniXMLconfigReader.cpp\
	$(VTRANS3_ROOT_PATH)/IO/ParseTree2XMLtreeTraverser.cpp\
	$(VTRANS3_ROOT_PATH)/IO/rules/TranslationRuleFileReaderBase.cpp\
	$(VTRANS3_ROOT_PATH)/IO/TinyXMLconfigReader.cpp\
	$(VTRANS3_ROOT_PATH)/IO/UnknownGrammarPartNameException.cpp\
	$(VTRANS3_ROOT_PATH)/Parse/DirectingLeavesInSingleIterationTraverser.cpp\
	$(VTRANS3_ROOT_PATH)/Parse/DirectingLeavesMultipleIterTraverser.cpp\
	$(VTRANS3_ROOT_PATH)/Parse/DuplicateParseTree.cpp\
	$(VTRANS3_ROOT_PATH)/Parse/GrammarPart.cpp\
	$(VTRANS3_ROOT_PATH)/Parse/KeepTrackOfCurrentParseTreePath.cpp\
	$(VTRANS3_ROOT_PATH)/Parse/ParseByRise.cpp\
	$(VTRANS3_ROOT_PATH)/Parse/ParseByRise_BuildParseTree.cpp\
	$(VTRANS3_ROOT_PATH)/Parse/ParseByRise_GrammarRule.cpp\
	$(VTRANS3_ROOT_PATH)/Translate/AddVocAndTranslDefinitions.cpp\
	$(VTRANS3_ROOT_PATH)/Translate/AttributeTypeAndPosAndSize.cpp\
	$(VTRANS3_ROOT_PATH)/Translate/ConditionsAndTranslation.cpp\
	$(VTRANS3_ROOT_PATH)/Translate/InsertIntoTreeTransverser.cpp\
	$(VTRANS3_ROOT_PATH)/Translate/SetSameConsecutiveIDforLeaves.cpp\
	$(VTRANS3_ROOT_PATH)/Translate/SummarizePersonIndex.cpp\
	$(VTRANS3_ROOT_PATH)/Translate/SyntaxTreePath.cpp\
	$(VTRANS3_ROOT_PATH)/Translate/TransformationRule.cpp\
	$(VTRANS3_ROOT_PATH)/Translate/TransformTreeTraverser.cpp\
	$(VTRANS3_ROOT_PATH)/Translate/TranslatedTreeTraverser.cpp\
	$(VTRANS3_ROOT_PATH)/Translate/TranslateParseByRiseTree.cpp\
	$(VTRANS3_ROOT_PATH)/Translate/TranslateTreeTraverser.cpp\
	$(VTRANS3_ROOT_PATH)/Translate/TranslationRule.cpp\
	$(VTRANS3_ROOT_PATH)/VocabularyInMainMem/CharMappedDictionary.cpp\
	$(VTRANS3_ROOT_PATH)/VocabularyInMainMem/CharStringStdMap/CharStringStdMap.cpp\
	$(VTRANS3_ROOT_PATH)/VocabularyInMainMem/IVocabularyInMainMem.cpp\
	$(VTRANS3_ROOT_PATH)/VocabularyInMainMem/I_WordSearch.cpp\
	$(VTRANS3_ROOT_PATH)/VocabularyInMainMem/TUchemnitzEngWordSorted1st/BinarySearchInDictFile.cpp\
	$(VTRANS3_ROOT_PATH)/VocabularyInMainMem/VocabularyAndTranslation.cpp