QT += gui
QT += quick
QT += dbus

CONFIG += c++11
CONFIG += qml_debug
linux-oe-g++{
    # qtquickcompiler requires qmlcachegen which may not be available in Yocto builds
    # CONFIG += qtquickcompiler
}
!linux-oe-g++{
    CONFIG += qtquickcompiler
}
CONFIG += precompile_header

SOURCES += \
    main.cpp \
    controller.cpp \
    appitem.cpp

RESOURCES += qml.qrc

TARGET = oxide
include(../../qmake/common.pri)

target.path = /opt/bin
applications.files = ../../assets/opt/usr/share/applications/codes.eeems.oxide.oxide
applications.path = /opt/usr/share/applications/
icons.files = ../../assets/opt/usr/share/icons/oxide/702x702/splash/oxide.png
icons.path  = /opt/usr/share/icons/oxide/702x702/splash/
configFile.files = ../../assets/etc/oxide.conf
configFile.path  = /opt/etc/

linux-oe-g++{
    # Use standard FHS paths for Yocto builds
    target.path = /usr/bin
    applications.path = /usr/share/applications/
    icons.path = /usr/share/icons/oxide/702x702/splash/
    configFile.path = /etc/
}

INSTALLS += target
INSTALLS += applications
INSTALLS += icons
INSTALLS += configFile

DISTFILES += \
    ../../assets/etc/dbus-1/system.d/org.freedesktop.login1.conf \
    ../../assets/etc/oxide.conf

HEADERS += \
    controller.h \
    appitem.h \
    mxcfb.h \
    notificationlist.h \
    oxide_stable.h \
    wifinetworklist.h

PRECOMPILED_HEADER = \
    oxide_stable.h

include(../../qmake/liboxide.pri)
