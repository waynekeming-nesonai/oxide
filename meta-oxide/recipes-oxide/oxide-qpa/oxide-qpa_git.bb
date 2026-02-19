SUMMARY = "Oxide Qt Platform Abstraction plugin"
HOMEPAGE = "https://github.com/Eeems/oxide"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${WORKDIR}/git/LICENSE.md;md5=18d20bab41ca98b14abb804986939a30"

SRC_URI = "git://github.com/waynekeming-nesonai/oxide.git;protocol=https;branch=master"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git/shared/qpa"

DEPENDS = "qtbase liboxide libblight"
PV = "1.0+git${SRCPV}"

inherit qmake5

# Install to Qt plugin directory
EXTRA_QMAKEVARS_PRE += "PREFIX=${prefix}"

FILES:${PN} = "${libdir}/qt6/plugins/platforms/liboxide.so*"
FILES:${PN}-dev = "${libdir}/qt6/plugins/platforms/*.la"
