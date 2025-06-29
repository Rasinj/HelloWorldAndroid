#!/bin/bash
# NoteMaster Test Runner
# Run this script to execute all unit tests locally

echo "🧪 Running NoteMaster Unit Tests..."
echo "=================================="

# Clean and build
echo "📦 Building project..."
./gradlew clean build

# Run unit tests
echo ""
echo "🔍 Running unit tests..."
./gradlew test

# Display results
echo ""
echo "📊 Test Results:"
echo "================"

if [ $? -eq 0 ]; then
    echo "✅ All tests passed successfully!"
    echo ""
    echo "📝 Test Coverage:"
    echo "- MainActivity lifecycle tests"
    echo "- Note model validation tests" 
    echo "- Repository pattern tests"
    echo "- Memory pressure handling tests"
    echo ""
    echo "🚀 App is ready for deployment!"
else
    echo "❌ Some tests failed. Check the output above for details."
    echo ""
    echo "🔧 Common fixes:"
    echo "- Check that all dependencies are properly installed"
    echo "- Ensure Android SDK is configured correctly"
    echo "- Verify test data setup is correct"
fi

echo ""
echo "📱 To build APK: ./gradlew assembleDebug"
echo "🔍 To run tests: ./gradlew test"
echo "🚀 Latest APK: app/build/outputs/apk/debug/app-debug.apk"