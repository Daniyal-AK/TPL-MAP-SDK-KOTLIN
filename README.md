# TPL Maps SDK Sample

This repository demonstrates the use of the TPL Maps SDK in an Android application built with Kotlin. The sample app showcases how to integrate the TPL Maps SDK to provide map functionalities, including searching for places, displaying the current location, and interacting with the map.

## Features

- **Map Integration**: Displays a map using the TPL Maps SDK.
- **Place Search**: Allows users to search for places using the SDK's search functionality.
- **Current Location**: Centers the map on the user's current location.
- **Zoom Controls**: Provides buttons to zoom in and out on the map.
- **Reverse Geocoding**: Displays the address of the location under the map's center point.

## Prerequisites

- **Android Studio**: Use the latest stable version.
- **Kotlin**: Ensure Kotlin is set up in your Android project.
- **API Key**: Obtain an API key from TPL Maps and add it to the `AndroidManifest.xml` file.

## Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/tplmaps-sdk-sample.git
cd tplmaps-sdk-sample
```

### 2. Open in Android Studio

1. Open Android Studio.
2. Select "Open an existing project".
3. Navigate to the cloned repository and open it.

### 3. Add Your API Key

Obtain an API key from TPL Maps and add it to your `AndroidManifest.xml` file:

```xml
<meta-data
    android:name="com.tplmaps.sdk.API_KEY"
    android:value="YOUR_API_KEY_HERE"/>
```

### 4. Build and Run the Project

1. Connect an Android device or start an emulator.
2. Click on the "Run" button in Android Studio.

## Dependencies

The app uses the following dependencies:

```gradle
dependencies {
    implementation 'androidx.core:core-ktx:1.8.0'
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'com.google.android.material:material:1.5.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'

    // TPL MAPS SDK
    implementation "com.tpl.maps.sdk:places:2.0.1"
    implementation "com.tpl.maps.sdk:maps:2.0.1"
}
```

## Project Structure

- **MainActivity**: The main activity that initializes and manages the map, search functionality, and UI interactions.
- **PlacesAdapter**: A RecyclerView adapter for displaying search results.
- **Layouts**: The project uses XML-based layouts with ViewBinding enabled.

## Author

- **Muhammad Hasnain Altaf** - [GitHub](https://github.com/Hasnain17)
```
