SUMMARY = "Oxide display server protocol library"
HOMEPAGE = "https://github.com/Eeems/oxide"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${WORKDIR}/git/LICENSE.md;md5=18d20bab41ca98b14abb804986939a30"

SRC_URI = "git://github.com/waynekeming-nesonai/oxide.git;protocol=https;branch=master"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git/shared/libblight_protocol"

# Explicitly set PN because bitbake can't handle multiple underscores correctly
PN = "libblight-protocol"

DEPENDS = "qtbase qtbase-native"
PV = "1.0+git${SRCPV}"

inherit qt6-qmake

EXTRA_QMAKEVARS_PRE += "CONFIG+=qt PREFIX=/usr"

do_install:append() {
    # Move files from /opt to /usr if installed there
    if [ -d ${D}/opt ]; then
        mkdir -p ${D}${prefix}
        mv ${D}/opt/* ${D}${prefix}/ || true
        rmdir ${D}/opt || true
    fi
    # Install headers that qmake extra targets might miss
    install -d ${D}${includedir}/libblight_protocol
    for header in libblight_protocol.h; do
        if [ -f ${S}/$header ]; then
            install -m 0644 ${S}/$header ${D}${includedir}/libblight_protocol/ || true
        fi
    done
}

FILES:${PN} = "${libdir}/lib*.so* ${includedir}/libblight_protocol*"
FILES:${PN}-dev = "${includedir}/* ${libdir}/lib*.so ${libdir}/pkgconfig"
