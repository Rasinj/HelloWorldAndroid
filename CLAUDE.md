# NoteMaster Android Application

## Overview
NoteMaster is a beautiful, minimalistic note-taking Android application built with Kotlin. The app features a warm, productivity-focused design with category-based note organization and comprehensive crash prevention through extensive unit testing.

## Architecture
- **Language**: Kotlin
- **UI**: Programmatic UI (no XML layouts to avoid build issues)
- **Pattern**: Repository pattern for data management
- **Testing**: Comprehensive unit test suite with JUnit, Mockito, and Robolectric
- **Design**: Material Design principles with warm color palette

## Key Files and Structure

### Core Application Files
- `app/src/main/java/com/example/helloworld/MainActivity.kt` - Main activity with programmatic UI
- `app/src/main/java/com/example/helloworld/Note.kt` - Note data model and category system
- `app/build.gradle` - Dependencies and build configuration

### Testing Files
- `app/src/test/java/com/example/helloworld/MainActivityTest.kt` - Activity lifecycle tests
- `app/src/test/java/com/example/helloworld/NoteModelTest.kt` - Data model validation
- `app/src/test/java/com/example/helloworld/NoteRepositoryTest.kt` - Repository pattern tests
- `run-tests.sh` - Local test runner script

### Configuration Files
- `app/src/main/res/values/colors.xml` - Light theme color palette
- `app/src/main/res/values-night/colors.xml` - Dark theme color palette
- `.github/workflows/android-build.yml` - CI/CD pipeline

## Common Commands

### Building
```bash
# Build debug APK
./gradlew assembleDebug

# Clean and build
./gradlew clean build

# Build release APK
./gradlew assembleRelease
```

### Testing
```bash
# Run all unit tests
./gradlew test

# Run tests with coverage
./gradlew testDebugUnitTest

# Use the test runner script
./run-tests.sh
```

### Development Workflow
```bash
# Check for lint issues
./gradlew lint

# Check dependencies
./gradlew dependencies

# View build info
./gradlew properties
```

## Design System

### Color Palette
The app uses a carefully chosen warm color scheme optimized for productivity:
- **Background**: #FEFBF3 (warm cream)
- **Primary Text**: #2D2A26 (dark brown)
- **Secondary Text**: #6B6459 (medium brown)
- **Category Colors**: Each note category has distinct colors (Personal: #8E44AD, Work: #2980B9, etc.)

### Note Categories
Six predefined categories with emojis and colors:
1. **Personal** 👤 - Purple (#8E44AD)
2. **Work** 💼 - Blue (#2980B9)
3. **Study** 📚 - Teal (#16A085)
4. **Creative** 🎨 - Pink (#E91E63)
5. **Ideas** 💡 - Orange (#FF9800)
6. **Tasks** ✅ - Brown (#795548)

## Development Guidelines

### Programmatic UI Approach
This project uses programmatic UI creation instead of XML layouts to avoid build issues encountered during development. Key principles:
- Create all UI elements in Kotlin code
- Use LinearLayout as primary container
- Apply styling through code (colors, fonts, padding)
- Handle click events with lambdas

### Testing Strategy
Comprehensive testing covers:
- **Activity Lifecycle**: onCreate, onResume, onPause tests
- **Data Models**: Note validation and edge cases
- **Repository Pattern**: Mocking and dependency injection
- **Memory Pressure**: Low memory scenario handling

### Build Considerations
- **Target SDK**: 33 (Android 13)
- **Min SDK**: 21 (Android 5.0)
- **Kotlin Version**: Uses kotlin-android plugin
- **Dependencies**: Material Design, AndroidX libraries, comprehensive test suite

## Troubleshooting

### Common Issues
1. **App Crashes on Startup**
   - Run unit tests: `./run-tests.sh`
   - Check MainActivity onCreate method
   - Verify all dependencies are properly configured

2. **Build Failures**
   - Clean and rebuild: `./gradlew clean build`
   - Check Gradle wrapper permissions: `chmod +x gradlew`
   - Verify Android SDK configuration

3. **ARM64/Termux Compatibility**
   - Use GitHub Actions CI/CD for builds
   - Local builds may have AAPT2 compatibility issues
   - Docker builds available as alternative

### Testing Best Practices
- Always run tests before commits
- Use the test runner script for comprehensive validation
- Mock external dependencies in unit tests
- Test edge cases and error conditions

## CI/CD Pipeline
Automated building with GitHub Actions:
- **Trigger**: Push to main/master branch
- **Environment**: Ubuntu runner with Android SDK
- **Output**: APK artifacts and GitHub releases
- **Features**: Gradle caching, automated versioning

## Future Development
When extending the application:
1. Maintain programmatic UI approach
2. Add comprehensive tests for new features
3. Follow existing color scheme and design patterns
4. Use repository pattern for data management
5. Test thoroughly on multiple Android versions

## Architecture Decisions
- **Programmatic UI**: Chosen over XML to avoid build tool compatibility issues
- **Warm Color Scheme**: Selected for productivity and readability
- **Category System**: Provides organization without complexity
- **Comprehensive Testing**: Prevents crashes and ensures stability
- **Repository Pattern**: Enables future database integration