linux-oe-g++{
    # epaper is reMarkable-specific, disable for i.MX6 builds
    # LIBS += -L$$OUT_PWD/../../shared/epaper -lqsgepaper
    # INCLUDEPATH += $$OUT_PWD/../../shared/epaper
    DEFINES += EPAPER DISABLE_EPAPER
}
