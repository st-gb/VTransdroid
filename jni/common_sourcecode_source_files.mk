COMMON_SOURCECODE_ROOT_PATH := ../../common_sourcecode/

#	$(COMMON_SOURCECODE_ROOT_PATH)/Controller/Logger/Appender/AppendingFileOutput.cpp\
# $(COMMON_SOURCECODE_ROOT_PATH)/Controller/Logger/OutputHandler/StdCoutLogWriter.cpp\
#	$(COMMON_SOURCECODE_ROOT_PATH)/Windows/ErrorCode/ErrorCodeFromGetLastErrorToString.cpp\
	$(COMMON_SOURCECODE_ROOT_PATH)/Windows/ErrorCode/GetErrorMessageFromLastErrorCode.cpp\
	$(COMMON_SOURCECODE_ROOT_PATH)/Windows/ErrorCode/LocalLanguageMessageFromErrorCode.cpp\
	$(COMMON_SOURCECODE_ROOT_PATH)/Windows/Logger/ConsoleFormatter.cpp\
	$(COMMON_SOURCECODE_ROOT_PATH)/Windows/Logger/LogEntryOutputter.cpp\
	$(COMMON_SOURCECODE_ROOT_PATH)/Windows/multithread/Thread.cpp\
	$(COMMON_SOURCECODE_ROOT_PATH)/Windows/multithread/Win32EventBasedCondition.cpp\
	$(COMMON_SOURCECODE_ROOT_PATH)/Windows/multithread/Win32EventBasedEvent.cpp\
	$(COMMON_SOURCECODE_ROOT_PATH)/Xerces/ConvertXercesStringToStdWstring.cpp\
	$(COMMON_SOURCECODE_ROOT_PATH)/Xerces/DocumentLocationSAX2Handler.cpp\
	$(COMMON_SOURCECODE_ROOT_PATH)/Xerces/XercesAttributesHelper.cpp\
	$(COMMON_SOURCECODE_ROOT_PATH)/Xerces/XercesString.cpp\

COMMON_SOURCECODE_SOURCE_FILES.MK_SOURCE_FILES := \
	$(COMMON_SOURCECODE_ROOT_PATH)/Controller/character_string/format_as_string.cpp\
	$(COMMON_SOURCECODE_ROOT_PATH)/Controller/character_string/stdtstr.cpp\
	$(COMMON_SOURCECODE_ROOT_PATH)/Controller/Logger/Appender/FormattedLogEntryProcessor.cpp\
	$(COMMON_SOURCECODE_ROOT_PATH)/Controller/Logger/Appender/RollingFileOutput.cpp\
	$(COMMON_SOURCECODE_ROOT_PATH)/Controller/Logger/Formatter/I_LogFormatter.cpp\
 	$(COMMON_SOURCECODE_ROOT_PATH)/Controller/Logger/Formatter/HTMLlogFormatter.cpp\
	$(COMMON_SOURCECODE_ROOT_PATH)/Controller/Logger/Formatter/Log4jFormatter.cpp\
	$(COMMON_SOURCECODE_ROOT_PATH)/Controller/Logger/Logger.cpp\
	$(COMMON_SOURCECODE_ROOT_PATH)/Controller/Logger/LogLevel.cpp\
	$(COMMON_SOURCECODE_ROOT_PATH)/Controller/Logger/OutputHandler/I_LogEntryOutputter.cpp\
	$(COMMON_SOURCECODE_ROOT_PATH)/Controller/Logger/OutputHandler/StdOfStreamLogWriter.cpp\
	$(COMMON_SOURCECODE_ROOT_PATH)/Controller/Logger/preprocessor_logging_macros.cpp\
	$(COMMON_SOURCECODE_ROOT_PATH)/data_structures/Trie/byteTrie/ByteTrie.cpp\
	$(COMMON_SOURCECODE_ROOT_PATH)/data_structures/Trie/byteTrie/Trie.cpp\
	$(COMMON_SOURCECODE_ROOT_PATH)/Linux/EnglishMessageFromErrorCode/EnglishMessageFromErrorCode.cpp\
	$(COMMON_SOURCECODE_ROOT_PATH)/Linux/FileSystem/GetCurrentWorkingDir/GetCurrentWorkingDir.cpp\
	$(COMMON_SOURCECODE_ROOT_PATH)/multithread/I_Thread.cpp\
