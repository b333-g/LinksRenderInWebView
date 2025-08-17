# 🌐 Links Render in WebView – Android App

A simple and elegant Android application that displays and handles website links using a native `WebView`. Designed with a floating action button menu, social media integration, and modern WebView settings for enhanced compatibility and performance.

---

##  Features

- Renders links inside a `WebView` (supports JavaScript, DOM storage, caching)
-  Handles internal and external links (e.g., `x.com`, Instagram, YouTube, etc.)
-  Custom error page for broken or blocked URLs
-  Floating Action Button (FAB) with social and navigation shortcuts
-  Light JavaScript injection for visual customization
-  Drawer menu with basic navigation options
- Dimming overlay for clean modal menu interactions

---

##  Tech Stack

- **Java**
- **Android SDK**
- `WebView`, `WebViewClient`, `WebChromeClient`
- Material Components (FAB, CardView, Navigation Drawer)

---

##  Project Structure Overview

```plaintext
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/example/linksrenderinwebview/
│   │       │   └── MainActivity.java
│   │       ├── res/
│   │       │   └── layout/
│   │       │       └── activity_main.xml
│   │       └── AndroidManifest.xml
│   └── build.gradle
