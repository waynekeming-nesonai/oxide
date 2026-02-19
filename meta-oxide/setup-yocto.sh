#!/bin/bash
# Setup script for Oxide Yocto build environment

set -e

echo "=== Oxide Yocto Setup Script ==="
echo ""

# Check if we're in the oxide directory
if [ ! -d "meta-oxide" ]; then
    echo "Error: meta-oxide directory not found."
    echo "Please run this script from the oxide repository root."
    exit 1
fi

# Check if repo tool is installed
if ! command -v repo &> /dev/null; then
    echo "Installing repo utility..."
    mkdir -p ~/bin
    curl https://storage.googleapis.com/git-repo-downloads/repo > ~/bin/repo
    chmod a+x ~/bin/repo
    export PATH=$PATH:~/bin
fi

# Ask for installation directory
read -p "Enter directory for Yocto setup (default: ~/oxide-yocto): " YOCTO_DIR
YOCTO_DIR=${YOCTO_DIR:-~/oxide-yocto}

# Create directory and setup
echo ""
echo "Setting up Yocto environment in $YOCTO_DIR..."
mkdir -p "$YOCTO_DIR"
cd "$YOCTO_DIR"

# Initialize repo
if [ ! -d ".repo" ]; then
    echo "Initializing NXP imx-manifest..."
    repo init -u https://github.com/nxp-imx/imx-manifest.git -b imx-linux-walnascar -m imx-6.12.34-2.1.0.xml
else
    echo "Repo already initialized, skipping..."
fi

# Sync repositories
echo ""
echo "Syncing repositories (this will take a while)..."
repo sync

# Setup build directory
echo ""
echo "Setting up build directory..."
MACHINE=imx6qsabresd DISTRO=fsl-imx-fb source imx-setup-release.sh -b build-oxide

# Add meta-oxide layer
echo ""
echo "Adding meta-oxide layer..."
echo "BBLAYERS += \"\${BBLAYERS}/$PWD/../meta-oxide\"" >> build-oxide/conf/bblayers.conf

# Get the absolute path of the oxide repository
OXIDE_PATH=$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)

# Create a symlink to meta-oxide in the Yocto directory
echo ""
echo "Creating symlink to meta-oxide layer..."
ln -sf "$OXIDE_PATH/meta-oxide" "$YOCTO_DIR/meta-oxide"

echo ""
echo "=== Setup Complete ==="
echo ""
echo "To build the oxide image:"
echo "  cd $YOCTO_DIR/build-oxide"
echo "  source conf/env.conf"
echo "  bitbake oxide-image"
echo ""
echo "To run in QEMU:"
echo "  runqemu generic-armv7a"
