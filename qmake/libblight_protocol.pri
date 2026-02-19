contains(DEFINES, LIBBLIGHT_PROTOCOL_PRIVATE){
    LIBS_PRIVATE += -L$$OUT_PWD/../../shared/libblight_protocol -lblight_protocol
}else{
    LIBS += -L$$OUT_PWD/../../shared/libblight_protocol -lblight_protocol
}
INCLUDEPATH += $$OUT_PWD/../../shared/libblight_protocol/include

linux-oe-g++{
    # EPAPER is reMarkable-specific, disabled for i.MX6 builds
    # DEFINES += EPAPER
}
