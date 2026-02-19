SUMMARY = "Oxide display server (blight)"
HOMEPAGE = "https://github.com/Eeems/oxide"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${WORKDIR}/git/LICENSE.md;md5=18d20bab41ca98b14abb804986939a30"

SRC_URI = "git://github.com/waynekeming-nesonai/oxide.git;protocol=https;branch=master"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git/shared/libblight"

DEPENDS = "qtbase libblight-protocol"
PV = "1.0+git${SRCPV}"

inherit qt6-qmake

EXTRA_QMAKEVARS_PRE += "CONFIG+=qt PREFIX=/usr"
EXTRA_QMAKEVARS_PRE += "QMAKE_CFLAGS+=-I${STAGING_INCDIR}/libblight_protocol"
EXTRA_QMAKEVARS_PRE += "QMAKE_CXXFLAGS+=-I${STAGING_INCDIR}/libblight_protocol"

do_install:append() {
    # Install headers that qmake extra targets might miss
    install -d ${D}${includedir}/libblight
    for header in clock.h connection.h dbus.h debug.h libblight_global.h libblight.h meta.h socket.h types.h; do
        if [ -f ${S}/$header ]; then
            install -m 0644 ${S}/$header ${D}${includedir}/libblight/ || true
        fi
    done
    # Install main header
    echo "#pragma once" > ${D}${includedir}/libblight.h
    echo '#include "libblight/libblight.h"' >> ${D}${includedir}/libblight.h
}

FILES:${PN} = "${libdir}/lib*.so*"
FILES:${PN}-dev = "${includedir}/* ${libdir}/pkgconfig"
