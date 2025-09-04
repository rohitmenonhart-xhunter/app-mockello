# Mockello - AI & Full-Stack Development Company Landing App

<div align="center">
  <img src="https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white" alt="Android"/>
  <img src="https://img.shields.io/badge/Kotlin-0095D5?style=for-the-badge&logo=kotlin&logoColor=white" alt="Kotlin"/>
  <img src="https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white" alt="Jetpack Compose"/>
  <img src="https://img.shields.io/badge/Material%20Design-757575?style=for-the-badge&logo=material-design&logoColor=white" alt="Material Design"/>
</div>

## 📱 About

Mockello is a modern Android application showcasing our AI and full-stack development company. Built with cutting-edge technologies including Jetpack Compose, Material 3, and custom animations, this app serves as both a portfolio and landing page for Mockello's services.

### 🌟 Key Features

- **Modern UI/UX**: Built with Jetpack Compose and Material 3
- **Custom Typography**: BitcountGridSingle font family for unique branding
- **Rich Animations**: Spring physics, fade transitions, and interactive elements
- **Responsive Design**: Optimized for various Android screen sizes
- **Company Showcase**: Highlights AI research, technology stack, and partnerships

## 🏗️ Architecture & Technologies

### Frontend
- **Jetpack Compose**: Modern declarative UI toolkit
- **Material 3**: Latest Material Design components
- **Kotlin**: 100% Kotlin codebase
- **Custom Animations**: Advanced animation framework with spring physics

### Design System
- **Custom Color Palette**: Orange-themed brand colors
- **Typography**: BitcountGridSingle font family
- **Components**: Reusable UI components with consistent styling
- **Animations**: Interactive elements with smooth transitions

### Build System
- **Gradle Kotlin DSL**: Modern build configuration
- **Version Catalogs**: Centralized dependency management
- **Android Gradle Plugin**: 8.5.2 (Java 8 compatible)

## 🚀 Quick Start

### Prerequisites
- Android Studio Hedgehog | 2023.1.1 or later
- JDK 8 or later
- Android SDK API 34
- Git

### Clone & Run
```bash
git clone https://github.com/rohitmenonhart-xhunter/app-mockello.git
cd app-mockello
./gradlew installDebug
```

## 🖥️ Windows Development Setup

### 1. Install Required Software

#### Download & Install:
1. **Android Studio** (Latest Stable)
   - Download from: https://developer.android.com/studio
   - Choose "Android Studio Hedgehog" or later
   - Install with default settings

2. **Git for Windows**
   - Download from: https://git-scm.com/download/win
   - Install with default settings
   - Choose "Git from the command line and also from 3rd-party software"

3. **JDK 8 or later**
   - Android Studio includes a bundled JDK
   - Alternatively, download Oracle JDK or OpenJDK

### 2. Configure Android Studio

#### First-time Setup:
```
1. Open Android Studio
2. Complete the setup wizard
3. Install Android SDK (API 34 recommended)
4. Install Android SDK Build-Tools
5. Install Android Emulator (optional)
```

#### Configure SDK:
```
1. File → Settings → Appearance & Behavior → System Settings → Android SDK
2. SDK Platforms: Install Android 14 (API 34)
3. SDK Tools: Ensure latest build-tools are installed
```

### 3. Clone Repository on Windows

#### Using Git Bash:
```bash
# Open Git Bash in your desired directory
git clone https://github.com/rohitmenonhart-xhunter/app-mockello.git
cd app-mockello
```

#### Using Command Prompt:
```cmd
# Open Command Prompt
git clone https://github.com/rohitmenonhart-xhunter/app-mockello.git
cd app-mockello
```

### 4. Open Project in Android Studio

```
1. File → Open
2. Navigate to the cloned 'app-mockello' folder
3. Select the folder and click 'OK'
4. Wait for Gradle sync to complete
5. If prompted, trust the project
```

### 5. Build & Run

#### Using Android Studio:
```
1. Wait for Gradle sync to complete
2. Click the 'Build' button or Ctrl+F9
3. Connect Android device or start emulator
4. Click 'Run' button or Shift+F10
```

#### Using Command Line:
```bash
# In project root directory
./gradlew clean
./gradlew installDebug
```

### 6. Windows-Specific Configurations

#### Environment Variables:
```
ANDROID_HOME: C:\Users\[YourUsername]\AppData\Local\Android\Sdk
JAVA_HOME: C:\Program Files\Android\Android Studio\jbr
```

#### Gradle Properties (if needed):
Create/edit `gradle.properties` in project root:
```properties
org.gradle.jvmargs=-Xmx2048m -Dfile.encoding=UTF-8
android.useAndroidX=true
android.enableJetifier=true
```

## 📁 Project Structure

```
app-mockello/
├── app/
│   ├── src/main/
│   │   ├── java/com/example/mockello/
│   │   │   ├── MainActivity.kt          # Main UI implementation
│   │   │   └── ui/theme/
│   │   │       ├── Color.kt             # Brand colors
│   │   │       ├── Theme.kt             # Material 3 theme
│   │   │       └── Type.kt              # Typography system
│   │   ├── res/
│   │   │   ├── font/                    # BitcountGridSingle fonts
│   │   │   ├── values/                  # Resources
│   │   │   └── mipmap*/                 # App icons
│   │   └── assets/fonts/                # Additional font files
│   ├── build.gradle.kts                 # App-level build config
│   └── proguard-rules.pro               # ProGuard configuration
├── gradle/
│   └── libs.versions.toml               # Version catalog
├── build.gradle.kts                     # Project-level build config
├── settings.gradle.kts                  # Project settings
└── README.md                            # This file
```

## 🎨 App Sections

### 1. Hero Section
- Company branding and tagline
- "AI + FULL-STACK DEVELOPMENT" banner
- Animated call-to-action buttons
- Modern glass morphism effects

### 2. About Section
- Company description and mission
- Animated content reveal
- Professional layout with orange accent colors

### 3. AI Research Section
- Interactive research area cards
- Agent Orchestration, SLMs, Efficient Frameworks
- Expandable cards with detailed information
- Color-coded categories with smooth animations

### 4. Technology Stack
- Comprehensive tech showcase
- Horizontal scrolling technology cards
- Categories: AI/ML, Backend, Frontend, Mobile, Cloud/DevOps
- Modern card design with gradient backgrounds

### 5. Partnership Section
- Stacia Corps exclusive partnership
- Professional partnership presentation
- Trust and credibility indicators

### 6. CTA Section
- Contact and engagement prompts
- Modern button design with animations
- Clear call-to-action messaging

## 🔧 Development Guidelines

### Code Style
- Follow Kotlin coding conventions
- Use meaningful variable and function names
- Maintain consistent indentation (4 spaces)
- Add comments for complex business logic

### Compose Best Practices
- Use `@Composable` functions for UI components
- Implement proper state management with `remember`
- Utilize `LaunchedEffect` for side effects
- Follow Material 3 design guidelines

### Git Workflow
```bash
# Create feature branch
git checkout -b feature/your-feature-name

# Make changes and commit
git add .
git commit -m "feat: add your feature description"

# Push changes
git push origin feature/your-feature-name

# Create pull request on GitHub
```

## 🐛 Troubleshooting

### Common Windows Issues

#### Gradle Sync Failed:
```
1. File → Invalidate Caches and Restart
2. Delete .gradle folder in project root
3. Run: ./gradlew clean
4. Sync project again
```

#### Build Errors:
```
1. Check JAVA_HOME environment variable
2. Ensure Android SDK is properly installed
3. Verify API level compatibility
4. Clean and rebuild: ./gradlew clean build
```

#### Font Loading Issues:
```
1. Verify font files exist in res/font/ directory
2. Check font file naming (lowercase, underscores)
3. Clean and rebuild project
```

#### Device Connection:
```
1. Enable Developer Options on device
2. Enable USB Debugging
3. Install device drivers if needed
4. Use different USB cable/port
```

## 📝 Customization

### Changing Colors
Edit `app/src/main/java/com/example/mockello/ui/theme/Color.kt`:
```kotlin
val MockelloOrange = Color(0xFFFF5722)        // Primary brand color
val MockelloOrangeLight = Color(0xFFFF8A50)   // Light variant
val MockelloDark = Color(0xFF1A1A1A)          // Dark text color
```

### Updating Content
Edit sections in `MainActivity.kt`:
- Company description in `AnimatedAboutSection`
- Research areas in `ResearchSection`
- Technology stack in `ModernTechStackSection`
- Partnership details in `StunningPartnershipSection`

### Adding New Sections
1. Create new `@Composable` function
2. Add to `LandingPage` column
3. Include appropriate spacing with `AnimatedSpacer`
4. Follow existing animation patterns

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly on different devices
5. Submit a pull request with detailed description

## 📄 License

This project is proprietary software owned by Mockello. All rights reserved.

## 📧 Contact

**Mockello - AI & Full-Stack Development**
- Website: [Coming Soon]
- Email: [Contact Information]
- Partnership: Stacia Corps (Exclusive Partner)

---

<div align="center">
  <p><strong>Built with ❤️ using Jetpack Compose & Material 3</strong></p>
  <p><em>Mockello - Where AI meets Full-Stack Excellence</em></p>
</div>
