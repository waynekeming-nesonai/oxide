SUMMARY = "Oxide display server (blight)"
HOMEPAGE = "https://github.com/Eeems/oxide"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${WORKDIR}/git/LICENSE.md;md5=18d20bab41ca98b14abb804986939a30"

SRC_URI = "git://github.com/waynekeming-nesonai/oxide.git;protocol=https;branch=master"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git/shared/libblight"

DEPENDS = "qtbase libblight_protocol"
PV = "1.0+git${SRCPV}"

inherit qmake5 systemd

EXTRA_QMAKEVARS_PRE += "CONFIG+=qt PREFIX=/usr"

SYSTEMD_SERVICE:${PN} = "blight.service"

FILES:${PN} = "${bindir}/blight ${libdir}/lib*.so*"
FILES:${PN}-dev = "${includedir}/libblight* ${libdir}/pkgconfig"
