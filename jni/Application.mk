#http://stackoverflow.com/questions/5234071/stlport-undefined-references
#"Turns out I had the APP_STL := stlport_static in the wrong file. It goes in 
# Application.mk. Not Android.mk. Not too sure why that matters though."

#https://groups.google.com/forum/#!msg/android-ndk/1Q4Pp5mkpYU/i8iFayGWeOkJ
#gnustl_static
APP_STL := stlport_static
# from http://stackoverflow.com/questions/4663291/android-ndk-r5-and-support-of-c-exception
# (for Apache Xerces)
APP_CPPFLAGS += -fexceptions