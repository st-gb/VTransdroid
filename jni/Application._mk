#http://stackoverflow.com/questions/5234071/stlport-undefined-references
#"Turns out I had the APP_STL := stlport_static in the wrong file. It goes in 
# Application.mk. Not Android.mk. Not too sure why that matters though."

#http://stackoverflow.com/questions/12127817/android-ics-4-0-ndk-newstringutf-is-crashing-down-the-app:
#"I had this problem when I change the file Application.mk From this line: 
# APP_STL := stlport_static To: APP_STL := gnustl_static"

#https://groups.google.com/forum/#!msg/android-ndk/1Q4Pp5mkpYU/i8iFayGWeOkJ
#gnustl_static
APP_STL := stlport_static
# from http://stackoverflow.com/questions/4663291/android-ndk-r5-and-support-of-c-exception
# (for Apache Xerces)
APP_CPPFLAGS += -fexceptions

#from http://www.cocos2d-x.org/wiki/Build_HelloWorld_on_android_x86_platform
APP_ABI := x86