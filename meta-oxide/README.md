# meta-oxide

Yocto layer for building Oxide (reMarkable tablet launcher) for QEMU i.MX6.

## Overview

This layer provides recipes for building the Oxide launcher and its dependencies in a Yocto environment, specifically targeting QEMU ARM with NXP's i.MX6 BSP.

## QEMU Mode Support

Oxide has been adapted to run in QEMU without requiring actual reMarkable hardware. When the `QEMU=1` environment variable is set:

- Device detection returns `DeviceType::QEMU`
- RM2FB checks are skipped
- MXCFB ioctl calls are bypassed (framebuffer updates use standard QPainter)
- Touch screen settings use QEMU defaults (1024x768)

## Layer Dependencies

- meta-imx (NXP i.MX BSP layer)
- meta-qt6 (Qt6 layer)

## Recipes

### Libraries

- `libblight_protocol` - Display server protocol library
- `libblight` - Display server library
- `liboxide` - Application framework library
- `oxide-qpa` - Qt Platform Abstraction plugin

### Applications

- `oxide-display` - Display server (blight)
- `oxide-launcher` - Main launcher application

### Images

- `oxide-image` - Complete bootable image for QEMU

## Usage

### 1. Setup NXP BSP

```bash
# Install repo utility
mkdir ~/bin
curl https://storage.googleapis.com/git-repo-downloads/repo > ~/bin/repo
chmod a+x ~/bin/repo
export PATH=$PATH:~/bin

# Clone NXP BSP
mkdir oxide-yocto && cd oxide-yocto
repo init -u https://github.com/nxp-imx/imx-manifest.git -b imx-linux-walnascar -m imx-6.12.34-2.1.0.xml
repo sync
```

### 2. Setup Build

```bash
# Setup build for framebuffer backend (i.MX 6/7)
MACHINE=imx6qsabresd DISTRO=fsl-imx-fb source imx-setup-release.sh -b build-oxide

# Add meta-oxide layer
cd build-oxide
echo "BBLAYERS += \"\${BBLAYERS}/../meta-oxide\"" >> conf/bblayers.conf
```

### 3. Build Image

```bash
# Build the oxide image
bitbake oxide-image
```

### 4. Run in QEMU

```bash
# QEMU command for i.MX6
runqemu generic-armv7a

# Or more specifically:
qemu-system-arm \
    -machine sabrelite \
    -cpu cortex-a9 \
    -m 512M \
    -kernel tmp/deploy/images/imx6qsabresd/zImage \
    -dtb tmp/deploy/images/imx6qsabresd/imx6q-sabrelite.dtb \
    -drive if=sd,file=tmp/deploy/images/imx6qsabresd/oxide-image-imx6qsabresd.ext4,format=raw \
    -append "console=ttymxc0 root=/dev/mmcblk0p2 rootwait" \
    -device virtio-keyboard \
    -device virtio-mouse \
    -display sdl
```

## Testing

Once QEMU boots:

```bash
# Check blight status
systemctl status blight

# Check display server logs
journalctl -u blight -f

# Verify QEMU mode is enabled
echo $QEMU
```

## File Structure

```
meta-oxide/
├── conf/
│   └── layer.conf          # Layer configuration
├── recipes-images/
│   └── images/
│       └── oxide-image.bb  # Image recipe
├── recipes-oxide/
│   ├── libblight/          # Display server library
│   ├── libblight_protocol/ # Protocol library
│   ├── liboxide/           # Application framework
│   ├── oxide-display/      # Display server (blight)
│   │   └── files/
│   │       ├── blight.service
│   │       └── qemu-env.conf
│   ├── oxide-launcher/     # Launcher application
│   └── oxide-qpa/          # QPA plugin
└── recipes-qt/
    └── qt6/
        └── qt6-git_%.bbappend
```

## License

GPL-3.0-or-later

## Contributing

When modifying Oxide code for QEMU compatibility, use the `qEnvironmentVariableIsSet("QEMU")` check to conditionally enable QEMU-specific behavior.
