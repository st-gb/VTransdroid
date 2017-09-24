# Copyright (C) 2009 The Android Open Source Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

#from http://www.kandroid.org/ndk/docs/ANDROID-MK.html :
# "An Android.mk file must begin with the definition of the LOCAL_PATH variable. 
# It is used to locate source files in the development tree. In this example, 
# the macro function 'my-dir', provided by the build system, is used to return 
# the path of the current directory (i.e. the directory containing the 
# Android.mk file itself)."
LOCAL_PATH := $(call my-dir)
#print $(LOCAL_PATH)

#include $(LOCAL_PATH)/build_miniXML_static_lib.mk

include $(CLEAR_VARS)
#file:///C:/devel/android-ndk-r9b-windows-x86/android-ndk-r9b/docs/PREBUILTS.html:
LOCAL_MODULE := libMiniXML-prebuilt
#TARGET_ARCH_ABI: armeabi, armeabi-v7a, x86, mips
LOCAL_SRC_FILES := $(TARGET_ARCH_ABI)/libMiniXML.a
LOCAL_EXPORT_C_INCLUDES := C:\devel\MiniXML\mxml-2.7
# "the value of LOCAL_SRC_FILES must be a single path to a prebuilt shared library (e.g. foo/libfoo.so), instead of a source file.
# "PREBUILT_STATIC_LIBRARY This is the same as PREBUILT_SHARED_LIBRARY, but for 
# a static library file instead. See docs/PREBUILTS.html for more."
include $(PREBUILT_STATIC_LIBRARY)

#from http://www.kandroid.org/ndk/docs/ANDROID-MK.html :
# "The CLEAR_VARS variable is provided by the build system and points to a 
# special GNU Makefile that will clear many LOCAL_XXX variables for you (e.g. 
# LOCAL_MODULE, LOCAL_SRC_FILES, LOCAL_STATIC_LIBRARIES, etc...), with the 
# exception of LOCAL_PATH. This is needed because all build control files are 
# parsed in a single GNU Make execution context where all variables are global."
include $(CLEAR_VARS)

#APP_STL                 := stlport_static

#include $(CLEAR_VARS)  #/cygdrive/c/devel/android-ndk-r9b-windows-x86/android-ndk-r9b/platforms/android-14/arch-arm/usr/include
#include $(LOCAL_PATH)/source_files.mk
include $(LOCAL_PATH)/static_libs.mk

LOCAL_MODULE := VTrans

include $(LOCAL_PATH)/common_sourcecode_source_files.mk
#include $(LOCAL_PATH)/../../common_sourcecode/VTransdroid_source_files.mk
include $(LOCAL_PATH)/VTrans3_source_files.mk
#see http://stackoverflow.com/questions/12551951/ndk-build-library-outside-main-project-source-tree
# Must be an absolute path if outside of dir of THIS makefile?!
#include $(LOCAL_PATH)/../../VTrans3\VTransdroid_source_files.mk
# include $(LOCAL_PATH)/MiniXML_source_files.mk

LOCAL_CFLAGS = -IT:/SourceCodeManagement/VTrans3/ \
  -IT:\SourceCodeManagement\common_sourcecode

#   -IC:/devel/android-ndk-r9b-windows-x86/android-ndk-r9b/sources/cxx-stl/gnu-libstdc++/4.8/include
  #-IC:/devel/android-ndk-r9b-windows-x86/android-ndk-r9b/sources/cxx-stl/stlport/stlport
#  -LC:\devel\android-ndk-r9b-windows-x86\android-ndk-r9b\samples\hello-jni\jni 
# -l$(LOCAL_PATH)/libMiniXML.a
LOCAL_CPPFLAGS = -IT:/SourceCodeManagement/VTrans3/ \
  -IT:\SourceCodeManagement\common_sourcecode\Controller\MSVC_adaption \
  -IT:\SourceCodeManagement\common_sourcecode \
  -IC:\devel\MiniXML\mxml-2.7 \
  -IC:\devel\stlsoft-1.9.117-hdrs\stlsoft-1.9.117\include \
  -DUSE_OWN_LOGGER \
  -DCOMPILE_LOGGER_MULTITHREAD_SAFE \
  -O3 \
  -fPIC \
  -Wl,-no-undefined
  #-Wl,--unresolved-symbols=ignore-in-object-files
  #-W,--no-undefined
  #-IC:\devel\xerces-c\3.1.1\src \
  #-LC:\devel\android-ndk-r9b-windows-x86\android-ndk-r9b\sources\cxx-stl\stlport\libs\armeabi-v7a \
  #-lstlport_static

#T:/SourcecodeManagement/VTrans3/Attributes/EnglishWord.cpp
#LOCAL_LDFLAGS := -Wl,--unresolved-symbols=ignore-in-object-files
#LOCAL_LDFLAGS := --warn-unresolved-symbols
LOCAL_LDFLAGS := -W,--no-undefined

#LOCAL_SRC_FILES := Attributes\GermanWord.cpp \
                   hello-jni.c
#hello-jni.cpp 
#$(MINI_XML_LIB_SOURCE_FILES)
LOCAL_SRC_FILES := \
 $(VTRANS3_SOURCE_FILES.MK_SOURCE_FILES) \
 $(COMMON_SOURCECODE_SOURCE_FILES.MK_SOURCE_FILES)

#LOCAL_STATIC_LIBRARIES := $(LOCAL_PATH)libMiniXML
LOCAL_STATIC_LIBRARIES := libMiniXML-prebuilt

#LOCAL_STATIC_LIBRARIES += MiniXML
#LOCAL_LDLIBS += 
#$(LOCAL_PATH)/libMiniXML.a

# "The list of static libraries modules (built with BUILD_STATIC_LIBRARY) that 
# should be linked to this module. This only makes sense in shared library modules.
include $(BUILD_SHARED_LIBRARY)
