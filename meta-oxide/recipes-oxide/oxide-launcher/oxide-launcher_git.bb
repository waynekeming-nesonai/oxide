SUMMARY = "Oxide launcher application"
HOMEPAGE = "https://github.com/Eeems/oxide"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${WORKDIR}/git/LICENSE.md;md5=18d20bab41ca98b14abb804986939a30"

SRC_URI = "git://github.com/waynekeming-nesonai/oxide.git;protocol=https;branch=master"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git/applications/launcher"

DEPENDS = "qtbase qtdeclarative liboxide"
PV = "1.0+git${SRCPV}"

inherit qmake5

EXTRA_QMAKEVARS_PRE += "PREFIX=/usr"

FILES:${PN} = "${bindir}/oxide ${datadir}/oxide*"
