# Remove patches that don't apply to the Qt5 version in NXP BSP
SRC_URI:remove = "file://0001-Use-OE_QMAKE_PATH_EXTERNAL_HOST_BINS-to-locate-qmlca.patch"
SRC_URI:remove = "file://0002-qv4compiler-fix-build-with-gcc-15.patch"
