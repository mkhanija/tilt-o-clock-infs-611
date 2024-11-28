# **Tilt-o-Clock: Step-by-Step Guide to Running the App**

Welcome to **Tilt-o-Clock**, an innovative alarm clock application that requires you to physically engage with your device to dismiss alarms. This guide will walk you through the steps to set up, build, and run the Tilt-o-Clock app on your Android device.

---

## **Table of Contents**

1. [Prerequisites](#prerequisites)
2. [Project Setup](#project-setup)
   - [Clone the Repository](#clone-the-repository)
   - [Open the Project in Android Studio](#open-the-project-in-android-studio)
3. [Build and Run the App](#build-and-run-the-app)
   - [Sync Gradle Files](#sync-gradle-files)
   - [Build the Project](#build-the-project)
   - [Run the App on a Device](#run-the-app-on-a-device)
4. [Testing the App](#testing-the-app)
   - [Setting an Alarm](#setting-an-alarm)
   - [Dismissing the Alarm](#dismissing-the-alarm)
5. [Additional Notes](#additional-notes)
   - [Troubleshooting](#troubleshooting)
   - [Customization](#customization)
   - [Contributing](#contributing)
6. [Contact Information](#contact-information)

---

## **Prerequisites**

Before you begin, ensure that you have the following installed on your development machine:

- **Operating System**: Windows, macOS, or Linux
- **[Android Studio](https://developer.android.com/studio)** (version 2022.1.1 or later)
- **Java Development Kit (JDK)** (version 11 or later)
- **Android SDK** (API level 24 or higher)
- **Android Device** (for testing)
  - Device should have a **gyroscope sensor**.
  - **USB Debugging** enabled in developer options.

---

## **Project Setup**

### **Clone the Repository**

Obtain the source code for the Tilt-o-Clock app from GitHub.

#### **Option A: Clone with Git**

If you have Git installed, open your terminal and run:

```bash
git clone https://github.com/mkhanija/tilt-o-clock-infs-611.git
```

#### **Option B: Download ZIP**

1. Visit the repository page: `https://github.com/mkhanija/tilt-o-clock-infs-611`.
2. Click on the **"Code"** button.
3. Select **"Download ZIP"**.
4. Extract the downloaded ZIP file to your desired location.

---

### **Open the Project in Android Studio**

1. **Launch Android Studio**.

2. **Open the Project**:

   - Click **"Open an existing Android Studio project"**.
   - Navigate to the `tilt-o-clock-infs-611` folder.
   - Click **"OK"**.

3. **Wait for Indexing**:

   - Android Studio will load the project and prompt you to sync Gradle files or update plugins. Proceed as required.

---

## **Build and Run the App**

### **Sync Gradle Files**

Ensure all Gradle dependencies are configured correctly:

1. Click the **"Sync Project with Gradle Files"** icon in the toolbar.
2. Wait for Gradle to finish downloading dependencies.

---

### **Build the Project**

- **Clean the Project (Optional)**:

  ```bash
  Build > Clean Project
  ```

- **Rebuild the Project**:

  ```bash
  Build > Rebuild Project
  ```

---

### **Run the App on a Device**

#### **Prepare Your Android Device**

1. **Enable Developer Options**:

   - Go to **"Settings > About phone"**.
   - Tap **"Build number"** seven times to unlock developer options.

2. **Enable USB Debugging**:

   - Go to **"Settings > System > Developer options"**.
   - Turn on **"USB debugging"**.

3. **Connect the Device**:

   - Use a USB cable to connect your device to your computer.

#### **Run the App**

1. Click the **"Run"** button in Android Studio.
2. Select your connected device in the **"Select Deployment Target"** dialog.
3. Wait for the APK to install and the app to launch on your device.

---

## **Testing the App**

### **Setting an Alarm**

1. **Launch Tilt-o-Clock**.
2. **Set an Alarm**:

   - Tap **"Select Time"** to open the time picker.
   - Choose your desired alarm time and tap **"OK"**.
   - Tap **"Set Alarm"** to schedule the alarm.

---

### **Dismissing the Alarm**

1. **Alarm Trigger**:

   - At the set time, the alarm will sound, and the dismissal screen will appear.

2. **Interactive Dismissal**:

   - Tilt your device to move the blue cursor over the red target.
   - Tap the screen when the blue cursor aligns with the red target to dismiss the alarm.

---

## **Additional Notes**

### **Troubleshooting**

- **App Crashes on Launch**:
  - Ensure all dependencies are properly configured in `build.gradle.kts`.
  - Verify that the device has a gyroscope sensor.

- **Alarm Does Not Sound**:
  - Check the `alarm_sound.mp3` file in `app/src/main/res/raw/`.

- **Cursor Not Moving**:
  - Test the gyroscope sensor functionality on your device.

---

### **Customization**

- **Change Alarm Sound**:
  - Replace `alarm_sound.mp3` in `res/raw/` with your preferred audio file.

- **Adjust UI**:
  - Modify colors and layouts in `ui.theme` or `TiltToDisableAlarmScreen.kt`.

---

### **Contributing**

- **Issues**:
  - Open an issue on GitHub for bugs or feature suggestions.
- **Pull Requests**:
  - Fork the repository, make changes, and submit a pull request.

---

## **Contact Information**

For support, contact:

- **Email**: mkhanija@gmu.edu
- **GitHub**: [mkhanija](https://github.com/mkhanija)