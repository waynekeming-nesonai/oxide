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

do_configure:prepend() {
    # Workaround for qmltyperegistrar not being in the correct location in NXP BSP
    mkdir -p ${RECIPE_SYSROOT_NATIVE}/usr/libexec
    if [ ! -f ${RECIPE_SYSROOT_NATIVE}/usr/libexec/qmltyperegistrar ]; then
        # Find and symlink qmltyperegistrar from sysroot-destdir
        for dir in ${TMPDIR}/work/*/qtdeclarative-native/*/sysroot-destdir/*/usr/libexec; do
            if [ -f "$dir/qmltyperegistrar" ]; then
                ln -sf "$dir/qmltyperegistrar" ${RECIPE_SYSROOT_NATIVE}/usr/libexec/qmltyperegistrar || true
                break
            fi
        done
    fi
}

FILES:${PN} = "${libdir}/lib*.so* ${bindir}/* ${datadir}/oxide*"
FILES:${PN}-dev = "${includedir}/liboxide* ${libdir}/lib*.so ${libdir}/pkgconfig"
