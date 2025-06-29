#!/bin/bash

# Optimized Android SDK setup script for GitHub Actions
# Uses pre-installed SDK components to reduce build time

set -e

echo "Setting up Android SDK with pre-installed components..."

# GitHub Actions Ubuntu runners come with Android SDK pre-installed
# Set up environment variables to use the pre-installed SDK
export ANDROID_HOME=$ANDROID_SDK_ROOT
export ANDROID_SDK_HOME=$ANDROID_SDK_ROOT

# Print SDK information for debugging
echo "Android SDK Root: $ANDROID_SDK_ROOT"
echo "Android Home: $ANDROID_HOME"

# List available SDK components
echo "Available SDK platforms:"
$ANDROID_SDK_ROOT/cmdline-tools/latest/bin/sdkmanager --list | grep "platforms;" | head -10

echo "Available build-tools:"
$ANDROID_SDK_ROOT/cmdline-tools/latest/bin/sdkmanager --list | grep "build-tools;" | head -10

# Install only missing components that we specifically need
# Check if our required API level and build-tools are already installed
if ! $ANDROID_SDK_ROOT/cmdline-tools/latest/bin/sdkmanager --list_installed | grep -q "platforms;android-33"; then
  echo "Installing Android API 33..."
  $ANDROID_SDK_ROOT/cmdline-tools/latest/bin/sdkmanager "platforms;android-33"
fi

if ! $ANDROID_SDK_ROOT/cmdline-tools/latest/bin/sdkmanager --list_installed | grep -q "build-tools;33.0.2"; then
  echo "Installing Build Tools 33.0.2..."
  $ANDROID_SDK_ROOT/cmdline-tools/latest/bin/sdkmanager "build-tools;33.0.2"
fi

# Accept all licenses
yes | $ANDROID_SDK_ROOT/cmdline-tools/latest/bin/sdkmanager --licenses >/dev/null 2>&1 || true

echo "Android SDK setup completed successfully!"
echo "Installed components:"
$ANDROID_SDK_ROOT/cmdline-tools/latest/bin/sdkmanager --list_installed | grep -E "(platforms;|build-tools;)"