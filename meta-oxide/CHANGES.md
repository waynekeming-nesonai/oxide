# Oxide QEMU Compatibility Changes

This document describes the changes made to Oxide to support running in QEMU ARM with i.MX6 Yocto BSP.

## Modified Files

### 1. shared/liboxide/devicesettings.h
- Added `QEMU` to the `DeviceType` enum

### 2. shared/liboxide/devicesettings.cpp
- `readDeviceType()`: Added QEMU environment variable check
- `getDeviceName()`: Added case for QEMU device type
- `getTouchEnvSetting()`: Added QEMU-specific empty settings (no rotation)
- `getTouchWidth()`: Added QEMU default of 1024
- `getTouchHeight()`: Added QEMU default of 768

### 3. applications/display-server/main.cpp
- Modified RM2FB check to skip when `QEMU` environment variable is set
- Allows blight to run without rm2fb in QEMU mode

### 4. applications/display-server/guithread.cpp
- Modified `sendUpdate()` to skip MXCFB ioctl calls when `QEMU` environment variable is set
- Framebuffer updates work via standard QPainter operations

## New Files

### Yocto Layer Structure
```
meta-oxide/
├── conf/layer.conf                 # Layer configuration
├── README.md                       # Layer documentation
├── setup-yocto.sh                  # Setup script
├── recipes-images/
│   └── images/
│       └── oxide-image.bb          # Image recipe
├── recipes-oxide/
│   ├── libblight/
│   │   └── libblight_git.bb
│   ├── libblight_protocol/
│   │   └── libblight_protocol_git.bb
│   ├── liboxide/
│   │   └── liboxide_git.bb
│   ├── oxide-display/
│   │   ├── oxide-display_git.bb
│   │   └── files/
│   │       ├── blight.service      # systemd service file
│   │       └── qemu-env.conf       # QEMU environment config
│   ├── oxide-launcher/
│   │   └── oxide-launcher_git.bb
│   └── oxide-qpa/
│       └── oxide-qpa_git.bb
└── recipes-qt/
    └── qt6/
        └── qt6-git_%.bbappend      # Qt6 configuration
```

## Environment Variables

### QEMU=1
When set, enables QEMU compatibility mode:
- Device type detection returns QEMU
- Hardware-specific checks are bypassed
- MXCFB ioctls are skipped

### QT_QPA_PLATFORM
Set to `oxide:enable_fonts:freetype:freetype` for proper display operation

## Testing in QEMU

### Boot Test Checklist
- [ ] QEMU boots successfully with the oxide-image
- [ ] Blight display server starts (check `systemctl status blight`)
- [ ] No MXCFB errors in dmesg (QEMU mode skips these)

### Functional Test Checklist
- [ ] Oxide launcher starts
- [ ] QML UI renders (using software backend)
- [ ] Input devices work (keyboard/mouse via virtio)
- [ ] D-Bus services are running

### Debug Commands
```bash
# Check blight status
systemctl status blight

# Check display server logs
journalctl -u blight -f

# Check D-Bus services
busctl list

# Verify QEMU mode is enabled
echo $QEMU
```

## Known Limitations

1. **EPaper Simulation**: Current implementation bypasses EPaper-specific features. No waveform simulation or partial update optimization.

2. **Touch Input**: Uses virtio keyboard/mouse instead of touchscreen emulation. Touch coordinates are set to QEMU defaults (1024x768).

3. **Wacom Digitizer**: Not emulated. Stylus input won't work in QEMU.

4. **Hardware Buttons**: Physical button events are not available in QEMU.

## Future Enhancements

1. **EPaper Simulation Mode**: Implement realistic EPaper rendering for testing

2. **VNC/RDP Output**: Enable remote viewing of the display

3. **Touchscreen Emulation**: Use QEMU tablet device for touch input

4. **Wacom Digitizer Emulation**: Add USB device emulation for stylus

5. **Automated Test Suite**: Create integration tests for QEMU environment
