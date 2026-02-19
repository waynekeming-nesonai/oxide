SUMMARY = "Oxide display server (blight)"
HOMEPAGE = "https://github.com/Eeems/oxide"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${WORKDIR}/git/LICENSE.md;md5=18d20bab41ca98b14abb804986939a30"

SRC_URI = "git://github.com/waynekeming-nesonai/oxide.git;protocol=https;branch=master \
           file://blight.service \
           file://qemu-env.conf"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git/applications/display-server"

DEPENDS = "qtbase qtdeclarative liboxide libblight"
PV = "1.0+git${SRCPV}"

inherit qt6-qmake systemd

EXTRA_QMAKEVARS_PRE += "CONFIG+=qt FEATURES=-sentry PREFIX=/usr"

SYSTEMD_SERVICE:${PN} = "blight.service"

do_install:append() {
    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${WORKDIR}/blight.service ${D}${systemd_system_unitdir}/

    # QEMU environment configuration
    install -d ${D}${sysconfdir}/default
    install -m 0644 ${WORKDIR}/qemu-env.conf ${D}${sysconfdir}/default/oxide
}

FILES:${PN} = "${bindir}/blight ${systemd_system_unitdir}/blight.service ${sysconfdir}/default/oxide"
