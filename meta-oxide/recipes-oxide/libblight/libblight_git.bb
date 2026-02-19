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

do_install() {
    # Manual install since qmake install doesn't work correctly with bitbake
    install -d ${D}${libdir}
    # Copy library files if they exist
    for lib in ${B}/libblight.so*; do
        if [ -f "$lib" ]; then
            install -m 0755 "$lib" ${D}${libdir}/ || true
        fi
    done
    # Install headers
    install -d ${D}${includedir}/libblight
    for header in clock.h connection.h dbus.h debug.h libblight_global.h libblight.h meta.h socket.h types.h; do
        if [ -f ${S}/$header ]; then
            install -m 0644 ${S}/$header ${D}${includedir}/libblight/ || true
        fi
    done
    # Install main header
    echo "#pragma once" > ${D}${includedir}/libblight.h
    echo '#include "libblight/libblight.h"' >> ${D}${includedir}/libblight.h
    # Install pkgconfig file
    if [ -f ${B}/lib/pkgconfig/blight.pc ]; then
        install -d ${D}${libdir}/pkgconfig
        install -m 0644 ${B}/lib/pkgconfig/blight.pc ${D}${libdir}/pkgconfig/ || true
    fi
}

do_install:append() {
    # Manually install the library since qmake install might not work correctly
    install -d ${D}${libdir}
    if [ -f ${B}/lib/libblight.so* ]; then
        cp -a ${B}/lib/libblight.so* ${D}${libdir}/ || true
    fi
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
    # Install pkgconfig file
    if [ -f ${B}/lib/pkgconfig/blight.pc ]; then
        install -d ${D}${libdir}/pkgconfig
        install -m 0644 ${B}/lib/pkgconfig/blight.pc ${D}${libdir}/pkgconfig/ || true
    fi
}

FILES:${PN} = "${libdir}/lib*.so*"
FILES:${PN}-dev = "${includedir}/* ${libdir}/pkgconfig"
