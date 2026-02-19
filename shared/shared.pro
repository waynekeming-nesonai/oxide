TEMPLATE = subdirs

SUBDIRS = \
    liboxide \
    libblight_protocol \
    libblight \
    libblight_client \
    qpa

liboxide.depends = libblight
libblight.depends = libblight_protocol
libblight_client.depends = libblight
qpa.depends = libblight liboxide
contains(DEFINES, SENTRY){
    SUBDIRS += sentry
    liboxide.depends += sentry
}else{
    SUBDIRS += cpptrace
    liboxide.depends += cpptrace
}
linux-oe-g++{
    # EPAPER is reMarkable-specific, disabled for QEMU/i.MX6 builds
    # SUBDIRS += epaper
    # liboxide.depends += epaper
}
INSTALLS += $$SUBDIRS
