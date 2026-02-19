# Fix qmltyperegistrar installation in NXP BSP
# The qmltyperegistrar is installed to sysroot-destdir instead of recipe-sysroot-native
do_install:append() {
    install -d ${D}${bindir}
    if [ -f ${B}/bin/qmltyperegistrar ]; then
        install -m 0755 ${B}/bin/qmltyperegistrar ${D}${bindir}/qmltyperegistrar || true
    fi
}
