# To-Do List Android App

Android-based To-Do List application built with **Kotlin**, **Room Database**, and **MVVM** architecture.

Submitted as a college project. Full project report is in [`PROJECT_REPORT.md`](./PROJECT_REPORT.md).

---

## Features

- Add, edit, delete tasks
- Mark tasks as completed (with strikethrough visual)
- 3 priority levels (Low / Medium / High) with color-coded indicators
- Optional due date with date picker
- Filter tabs: **All** / **Active** / **Completed**
- Bulk "Clear Completed" action
- Empty state when there are no tasks
- Material Design UI
- 100% offline — data is stored only on the device via Room

---

## Tech Stack

- **Language:** Kotlin 2.0.0
- **Min SDK:** Android 7.0 (API 24)
- **Target SDK:** Android 14 (API 34)
- **Architecture:** MVVM (Model–View–ViewModel)
- **Persistence:** Room 2.6.1 (SQLite abstraction layer)
- **Async:** Kotlin Coroutines + Flow + LiveData
- **UI:** Material Components for Android, RecyclerView, ConstraintLayout
- **Build:** Gradle 8.5 with Kotlin DSL, KSP for Room compiler

---

## How to Build and Run

### Prerequisites

1. **Android Studio Hedgehog (2023.1.1)** or later — download from https://developer.android.com/studio
2. **JDK 17** (bundled with Android Studio)
3. An Android device OR an emulator running Android 7.0 (API 24) or higher

### Steps

1. **Open the project**
   - Launch Android Studio
   - Click **File → Open** and select this folder (`todo-app`)
   - Wait for Gradle sync to complete (~1–5 minutes on first open while it downloads dependencies)

2. **Run the app**
   - Connect an Android device via USB (with USB debugging enabled) OR start an emulator from `Tools → Device Manager`
   - Click the green **▶ Run** button (or press `Shift + F10`)
   - The app installs and launches on the selected device

3. **Build a release APK** (for submission)
   - In Android Studio: `Build → Generate Signed Bundle / APK → APK`
   - Or from terminal:
     ```bash
     ./gradlew assembleRelease
     ```
   - APK output: `app/build/outputs/apk/release/app-release.apk`

---

## Project Structure

```
todo-app/
├── README.md                       ← this file
├── PROJECT_REPORT.md               ← full college report
├── settings.gradle.kts
├── build.gradle.kts                ← root-level build config
├── gradle.properties
└── app/
    ├── build.gradle.kts            ← app-level dependencies
    ├── proguard-rules.pro
    └── src/main/
        ├── AndroidManifest.xml
        ├── java/com/example/todoapp/
        │   ├── TodoApp.kt          ← Application class
        │   ├── data/               ← Room database layer
        │   │   ├── Task.kt
        │   │   ├── TaskDao.kt
        │   │   ├── TaskDatabase.kt
        │   │   └── TaskRepository.kt
        │   └── ui/                 ← presentation layer
        │       ├── MainActivity.kt
        │       ├── TaskAdapter.kt
        │       ├── TaskViewModel.kt
        │       └── AddEditTaskDialog.kt
        └── res/
            ├── layout/             ← UI XML
            ├── menu/               ← overflow menu
            ├── values/             ← strings, colors, themes
            └── xml/                ← backup rules
```

---

## Architecture (MVVM)

```
View (Activity / Dialog / Adapter)
   ↕  observes LiveData / forwards user actions
ViewModel (TaskViewModel)
   ↕  uses repository
Repository (TaskRepository)
   ↕  calls DAO
Room Database (TaskDao → SQLite)
```

This separation:
- Keeps UI code free of database logic
- Allows the ViewModel to survive configuration changes (rotation, theme change)
- Makes the codebase easy to test
- Lets the database update the UI reactively via Flow → LiveData

---

## Submission Checklist (for college)

Before submitting:

1. **Fill in your details** in [`PROJECT_REPORT.md`](./PROJECT_REPORT.md):
   - Your name
   - Roll number
   - Course / semester / department
   - College / university name
   - Guide's name
   - Submission date

2. **Run the app** on a phone or emulator and take **8–10 screenshots**:
   - Empty state
   - Add task dialog
   - Main list with multiple tasks (showing different priorities)
   - A completed task with strikethrough
   - All / Active / Completed tab views
   - Delete confirmation dialog
   - Clear completed dialog
   - Edit task dialog

3. **Insert the screenshots** into the report under "Section 8 — Screenshots".

4. **Convert the report to PDF**:
   - Open `PROJECT_REPORT.md` in any markdown editor (Typora, VS Code with markdown PDF extension, etc.)
   - Export to PDF
   - OR upload to Google Docs / Word and export there

5. **Zip the project** (excluding `build/`, `.gradle/`, `.idea/` folders):
   ```bash
   zip -r todo-app-submission.zip todo-app/ -x "*/build/*" "*/.gradle/*" "*/.idea/*"
   ```

6. **Submit** both the PDF report and the zipped source code.

---

## License

This project is submitted for academic purposes. Source code is free to learn from and adapt.
