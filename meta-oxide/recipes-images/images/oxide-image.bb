SUMMARY = "Oxide launcher image for QEMU i.MX6"

IMAGE_INSTALL = "\
    packagegroup-core-boot \
    packagegroup-core-ssh-openssh \
    ${CORE_IMAGE_EXTRA_INSTALL} \
    oxide-display \
    oxide-launcher \
    liboxide \
    libblight \
    oxide-qpa \
    bash \
    coreutils \
    strace \
    gdb \
"

IMAGE_FEATURES += "splash ssh-server-openssh"

LICENSE = "MIT"

inherit core-image

# Set QEMU environment
QB_MEM = "-m 512"
QB_MACHINE = "-machine sabrelite"
QB_KERNEL_OPT = "-console tty"
QB_DTB = ""

IMAGE_FSTYPES = "tar.gz ext4"
