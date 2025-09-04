# Quick Start Guide - Java Calculator

## 🚀 Get Started in 3 Steps

### Step 1: Prerequisites
Ensure you have Java 11+ and Maven installed:
```bash
java --version    # Should show Java 11 or higher
mvn --version     # Should show Maven 3.6 or higher
```

### Step 2: Build the Application
Run the build script for your platform:

**Windows:**
```cmd
build-and-run.bat
```

**Linux/macOS:**
```bash
chmod +x build-and-run.sh
./build-and-run.sh
```

**Or manually with Maven:**
```bash
mvn clean compile package
mvn javafx:run
```

### Step 3: Use the Calculator

#### Mouse/Touch Controls
- Click number buttons (0-9)
- Click operation buttons (+, -, ×, ÷)
- Click "=" for results
- Click "AC" to clear all
- Click "CE" to clear current entry
- Click "←" for backspace

#### Keyboard Controls
- **Numbers**: 0-9 keys
- **Operations**: +, -, *, / keys
- **Calculate**: Enter or = key
- **Clear All**: Escape key
- **Clear Entry**: Delete key
- **Backspace**: Backspace key
- **Decimal**: . (period) key

## 📋 Example Calculations

```
Basic: 5 + 3 = 8
Decimal: 3.14 + 2.86 = 6
Negative: -5 + 8 = 3
Chained: 5 + 3 × 2 = 16
```

## 🛠️ Development

### Run Tests
```bash
mvn test
```

### View Test Coverage
```bash
mvn jacoco:report
# Open: target/site/jacoco/index.html
```

### VS Code Integration
Open the project in VS Code and use:
- **Ctrl+Shift+P** → "Tasks: Run Task" → "Run Calculator"
- **F5** to debug

## 📁 Project Structure

```
src/
├── main/java/com/calculator/
│   ├── CalculatorApp.java      # Main entry point
│   ├── CalculatorModel.java    # Business logic
│   ├── CalculatorView.java     # User interface
│   └── CalculatorController.java # Event handling
└── test/java/com/calculator/   # Unit & integration tests
```

## ⚡ Features

- ✅ Basic arithmetic (+, -, ×, ÷)
- ✅ Decimal number support
- ✅ Negative numbers
- ✅ Error handling (division by zero)
- ✅ Keyboard shortcuts
- ✅ Modern JavaFX UI
- ✅ 80%+ test coverage

## 🐛 Troubleshooting

**JavaFX issues?**
```bash
mvn javafx:run -Djavafx.args="--add-modules javafx.controls,javafx.fxml"
```

**Build fails?**
```bash
mvn clean
mvn compile
```

**Need help?** Check `README.md` for detailed documentation.

---

**Happy Calculating! 🧮**
