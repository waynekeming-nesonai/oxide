SUMMARY = "Oxide application framework library"
HOMEPAGE = "https://github.com/Eeems/oxide"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${WORKDIR}/git/LICENSE.md;md5=18d20bab41ca98b14abb804986939a30"

SRC_URI = "git://github.com/waynekeming-nesonai/oxide.git;protocol=https;branch=master"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git/shared/liboxide"

DEPENDS = "qtbase qtdeclarative libblight systemd"
PV = "1.0+git${SRCPV}"

inherit qt6-qmake

# Disable sentry for Yocto builds
EXTRA_QMAKEVARS_PRE += "CONFIG+=qt FEATURES=-sentry PREFIX=/usr"

do_configure:append() {
    # Create symlinks to libblight headers so qmake can find them
    mkdir -p ${B}/../../shared/libblight/include
    for header in clock.h connection.h dbus.h debug.h libblight_global.h libblight.h meta.h socket.h types.h; do
        if [ -f ${WORKDIR}/git/shared/libblight/$header ]; then
            ln -sf ${WORKDIR}/git/shared/libblight/$header ${B}/../../shared/libblight/include/$header || true
        fi
    done
}

FILES:${PN} = "${libdir}/lib*.so* ${bindir}/* ${datadir}/oxide*"
FILES:${PN}-dev = "${includedir}/liboxide* ${libdir}/lib*.so ${libdir}/pkgconfig"
