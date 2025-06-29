# Hello World Android App

A minimal Android application that displays "Hello, World!" built with GitHub Actions CI/CD.

## Project Structure

```
HelloWorld/
├── .github/workflows/
│   ├── android-build.yml    # Standard Android CI build
│   └── docker-build.yml     # Docker-based build (manual trigger)
├── app/
│   ├── build.gradle
│   └── src/main/
│       ├── AndroidManifest.xml
│       └── java/com/example/helloworld/
│           └── MainActivity.java
├── build.gradle
├── settings.gradle
├── gradle.properties
└── README.md
```

## Build Methods

### 1. GitHub Actions (Recommended)
The repository is configured with automated CI/CD using GitHub Actions:

- **Trigger**: Automatic on push to `main`/`master` branch
- **Environment**: Ubuntu runner with Android SDK
- **Output**: APK artifact and GitHub release

### 2. Docker Build
Alternative Docker-based build for enhanced isolation:

- **Trigger**: Manual workflow dispatch or push to `docker-build` branch
- **Environment**: CircleCI Android Docker image
- **Output**: APK artifact

## Local Development

If you want to build locally (requires Android SDK):

```bash
# Make gradlew executable
chmod +x gradlew

# Build debug APK
./gradlew assembleDebug

# Find APK at:
# app/build/outputs/apk/debug/app-debug.apk
```

## Installation

### From GitHub Releases
1. Go to the [Releases](../../releases) page
2. Download the latest `app-debug.apk`
3. Install on your Android device (enable "Install from Unknown Sources")

### From GitHub Actions Artifacts
1. Go to [Actions](../../actions) tab
2. Click on the latest successful build
3. Download the `android-apk` artifact
4. Extract and install the APK

## Technical Details

- **Min SDK**: Android 5.0 (API 21)
- **Target SDK**: Android 13 (API 33)
- **Compile SDK**: Android 13 (API 33)
- **Build Tools**: 33.0.2
- **Gradle Plugin**: 7.4.2
- **Java**: 17

## CI/CD Features

- ✅ Automated builds on push
- ✅ APK artifact upload
- ✅ Automated GitHub releases
- ✅ Gradle caching for faster builds
- ✅ Docker-based builds available
- ✅ Multi-trigger support (push, PR, manual)

## Troubleshooting ARM64/Termux Issues

This project was initially developed in Termux on ARM64 Android, which has compatibility issues with standard Android build tools. The GitHub Actions CI solves this by:

1. Using x86_64 Ubuntu runners
2. Proper Android SDK installation
3. Compatible AAPT2 binaries
4. Isolated build environment

For local ARM64/Termux development, consider:
- Using the GitHub Actions builds
- Cross-compilation setup
- Alternative build systems like Bazel