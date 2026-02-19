SUMMARY = "Oxide display server (blight)"
HOMEPAGE = "https://github.com/Eeems/oxide"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${WORKDIR}/git/LICENSE.md;md5=18d20bab41ca98b14abb804986939a30"

SRC_URI = "git://github.com/waynekeming-nesonai/oxide.git;protocol=https;branch=master"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git/shared/libblight"

DEPENDS = "qtbase libblight_protocol"
PV = "1.0+git${SRCPV}"

inherit qt6-qmake systemd

EXTRA_QMAKEVARS_PRE += "CONFIG+=qt PREFIX=/usr"

SYSTEMD_SERVICE:${PN} = "blight.service"

do_install:append() {
    # Install headers manually since qmake extra targets don't run properly
    install -d ${D}${includedir}/libblight
    for header in clock.h connection.h dbus.h debug.h libblight_global.h libblight.h meta.h socket.h types.h; do
        install -m 0644 ${S}/$header ${D}${includedir}/libblight/ || true
    done
    # Install main header
    echo "#pragma once" > ${D}${includedir}/libblight.h
    echo '#include "libblight/libblight.h"' >> ${D}${includedir}/libblight.h
}

FILES:${PN} = "${bindir}/blight ${libdir}/lib*.so*"
FILES:${PN}-dev = "${includedir}/* ${libdir}/pkgconfig"
