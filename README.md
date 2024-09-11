# MeaTube Android Project

This README explains how to set up and run the MeaTube Android project.

## Prerequisites

- Git
- Android Studio (latest version recommended)
- Java Development Kit (JDK) 11 or later
- Android SDK (will be managed by Android Studio)

## Setup

1. Clone the repository:
   ```
   git clone https://github.com/Oran-Zafrani/FooTube-Android.git
   cd FooTube-Android
   ```

2. Open Android Studio

3. Select "Open an Existing Project" and navigate to the cloned repository folder

4. Android Studio will automatically download necessary dependencies and build the project

## Running the Project

1. In Android Studio, select an emulator or connect a physical Android device

2. Click the "Run" button (green play icon) or use the keyboard shortcut (usually Shift + F10)

This will build the app and install it on the selected emulator or device.

## Work Process

1. Planning: We started by planning the project, defining requirements and needs, creating a scrum project in the [Jira platform](https://sbar1998.atlassian.net/jira/software/projects/MTA/boards/2), adding issues to each developer, and creating a basic interface design.

2. Project Setup: We created the Android project using Android Studio and set up the necessary configurations.

3. Development:
   - Created basic activities and fragments
   - Implemented RecyclerViews for displaying video lists
   - Integrated Material Design components for a modern UI
   - Maintained data representation consistency between Android and web, for making the following work on server much fluent
   - Added mock data to simulate representation on the Android app and website

4. Testing: We performed manual tests on various Android devices and emulators to ensure functionality and compatibility

## Troubleshooting

If you encounter issues:
1. Ensure all prerequisites are installed and up-to-date
2. Try "File" > "Invalidate Caches / Restart" in Android Studio
3. Update Android Studio to the latest version
4. Sync the project with Gradle files

For more information, refer to the [Android Developer documentation](https://developer.android.com/docs).
