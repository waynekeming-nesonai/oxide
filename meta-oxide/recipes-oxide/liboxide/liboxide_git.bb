SUMMARY = "Oxide application framework library"
HOMEPAGE = "https://github.com/Eeems/oxide"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${WORKDIR}/git/LICENSE.md;md5=18d20bab41ca98b14abb804986939a30"

SRC_URI = "git://github.com/waynekeming-nesonai/oxide.git;protocol=https;branch=master"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git/shared/liboxide"

DEPENDS = "qtbase qtdeclarative libblight-protocol libblight systemd"
PV = "1.0+git${SRCPV}"

inherit qt6-qmake

# Disable sentry for Yocto builds
EXTRA_QMAKEVARS_PRE += "CONFIG+=qt FEATURES=-sentry PREFIX=/usr"
EXTRA_QMAKEVARS_PRE += "QMAKE_CFLAGS+=-I${STAGING_INCDIR}/libblight"
EXTRA_QMAKEVARS_PRE += "QMAKE_CXXFLAGS+=-I${STAGING_INCDIR}/libblight"
EXTRA_QMAKEVARS_PRE += "QMAKE_CFLAGS+=-I${STAGING_INCDIR}/libblight_protocol"
EXTRA_QMAKEVARS_PRE += "QMAKE_CXXFLAGS+=-I${STAGING_INCDIR}/libblight_protocol"

do_install:append() {
    # Move files from /opt to /usr if installed there
    if [ -d ${D}/opt ]; then
        mkdir -p ${D}${prefix}
        mv ${D}/opt/* ${D}${prefix}/ || true
        rmdir ${D}/opt || true
    fi

    # Manually install headers from build directory
    if [ -d ${B}/include/liboxide ]; then
        install -d ${D}${includedir}/liboxide
        install -m 0644 ${B}/include/liboxide/*.h ${D}${includedir}/liboxide/
    fi
    if [ -f ${B}/include/liboxide.h ]; then
        install -m 0644 ${B}/include/liboxide.h ${D}${includedir}/
    fi
}

FILES:${PN} = "${libdir}/lib*.so* ${bindir}/* ${datadir}/oxide*"
FILES:${PN}-dev = "${includedir}/liboxide* ${libdir}/lib*.so ${libdir}/pkgconfig"
