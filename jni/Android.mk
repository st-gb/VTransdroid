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
#
LOCAL_PATH := $(call my-dir)
#print $(LOCAL_PATH)

#include $(CLEAR_VARS)
#LOCAL_MODULE    := MiniXML 
#LOCAL_SRC_FILES := libMiniXML.a
#include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
#APP_STL                 := stlport_static

include $(CLEAR_VARS)  #/cygdrive/c/devel/android-ndk-r9b-windows-x86/android-ndk-r9b/platforms/android-14/arch-arm/usr/include
include $(LOCAL_PATH)/source_files.mk
include $(LOCAL_PATH)/static_libs.mk


include $(LOCAL_PATH)/common_sourcecode_source_files.mk
#include $(LOCAL_PATH)/../../common_sourcecode/VTransdroid_source_files.mk
include $(LOCAL_PATH)/VTrans3_source_files.mk
#see http://stackoverflow.com/questions/12551951/ndk-build-library-outside-main-project-source-tree
# Must be an absolute path if outside of dir of THIS makefile?!
#include $(LOCAL_PATH)/../../VTrans3\VTransdroid_source_files.mk
include $(LOCAL_PATH)/MiniXML_source_files.mk

LOCAL_CFLAGS = -IT:/SourceCodeManagement/VTrans3/ \
  -IT:\SourceCodeManagement\common_sourcecode

#   -IC:/devel/android-ndk-r9b-windows-x86/android-ndk-r9b/sources/cxx-stl/gnu-libstdc++/4.8/include
  #-IC:/devel/android-ndk-r9b-windows-x86/android-ndk-r9b/sources/cxx-stl/stlport/stlport
LOCAL_CPPFLAGS = -IT:/SourceCodeManagement/VTrans3/ \
  -IT:\SourceCodeManagement\common_sourcecode\Controller\MSVC_adaption \
  -IT:\SourceCodeManagement\common_sourcecode \
  -IC:\devel\MiniXML\mxml-2.7 \
  -IC:\devel\stlsoft-1.9.117-hdrs\stlsoft-1.9.117\include \
  -DUSE_OWN_LOGGER \
  -DCOMPILE_LOGGER_MULTITHREAD_SAFE \
  -LC:\devel\android-ndk-r9b-windows-x86\android-ndk-r9b\samples\hello-jni\jni \
  -l$(LOCAL_PATH)/libMiniXML.a \
  -O3 \
  -fPIC
  #-IC:\devel\xerces-c\3.1.1\src \
  #-LC:\devel\android-ndk-r9b-windows-x86\android-ndk-r9b\sources\cxx-stl\stlport\libs\armeabi-v7a \
  #-lstlport_static

#T:/SourcecodeManagement/VTrans3/Attributes/EnglishWord.cpp
LOCAL_MODULE    := VTrans

# LOCAL_STATIC_LIBRARIES := libMiniXML.a

#LOCAL_SRC_FILES := Attributes\GermanWord.cpp \
                   hello-jni.c
#hello-jni.cpp 
LOCAL_SRC_FILES := $(MINI_XML_LIB_SOURCE_FILES) \
 $(VTRANS3_SOURCE_FILES.MK_SOURCE_FILES) \
 $(COMMON_SOURCECODE_SOURCE_FILES.MK_SOURCE_FILES)

#LOCAL_STATIC_LIBRARIES += MiniXML
#LOCAL_LDLIBS += 
#$(LOCAL_PATH)/libMiniXML.a

include $(BUILD_SHARED_LIBRARY)
