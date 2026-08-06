# 🔐 CipherVault

> **A Java cryptography toolkit implementing Caesar, Vigenère, and Playfair ciphers over a 64-character ASCII space — with a JavaFX GUI.**

[![Java](https://img.shields.io/badge/Java-17+-orange?style=flat-square&logo=openjdk)](https://openjdk.org/)
[![JavaFX](https://img.shields.io/badge/JavaFX-GUI-blue?style=flat-square)](https://openjfx.io/)
[![JUnit 5](https://img.shields.io/badge/Tests-JUnit_5-green?style=flat-square)](https://junit.org/junit5/)
[![Status](https://img.shields.io/badge/Status-In_Progress-yellow?style=flat-square)]()

---

## 📖 Overview

CipherVault implements three classical encryption schemes over a **64-character ASCII alphabet** (space `0x20` through underscore `0x5F`), making it capable of encrypting not just letters but numbers, punctuation, and spaces too.

The project ships with:
- A utility class (`CryptoManager`) with all cipher logic
- A JavaFX desktop GUI (`FXMainPane` / `FXDriver`) for interactive encryption/decryption
- A full JUnit 5 test suite covering boundary conditions, wrap-around, and round-trip correctness

---

## 🔡 The 64-Character Alphabet

All three ciphers operate within this range instead of the traditional A–Z (26 chars):

```
Range:  ASCII 32 (' ') → ASCII 95 ('_')
Size:   64 characters
```

This means keys and shifts wrap modulo **64**, not 26.

---

## ⚙️ Ciphers

### 🔁 Caesar Cipher
A shift cipher. Each character is shifted by a fixed integer key within the 64-char space.

```
Encrypt:  newIndex = (charIndex + key) % 64
Decrypt:  newIndex = (charIndex - key + 64) % 64
```

**Example:** `"HELLO"` with key `3` → `"KHOOR"`

---

### 🔑 Vigenère Cipher
A polyalphabetic cipher. The key is repeated to match plaintext length; each character is shifted by the corresponding key character's position in the 64-char alphabet.

```
Encrypt:  newIndex = (plainIndex + keyIndex) % 64
Decrypt:  newIndex = (encIndex - keyIndex + 64) % 64
```

**Example:** `"SECURE DATA"` with key `"KEY"` → encrypted cyclically per character

---

### ♟️ Playfair Cipher
An 8×8 digraph cipher (adapted from the classic 5×5 Polybius square). Encrypts two characters at a time using a keyword-generated matrix built from `ALPHABET64`.

**Matrix rules:**
| Pair position | Encryption | Decryption |
|---|---|---|
| Same row | Shift right (+1 col) | Shift left (−1 col) |
| Same column | Shift down (+1 row) | Shift up (−1 row) |
| Rectangle | Swap columns | Swap columns (same) |

**Padding:** Odd-length strings are padded with `'X'`. Duplicate pairs are also handled per standard Playfair rules.

---

## 🖥️ GUI

The JavaFX interface (`FXMainPane`) provides:
- Radio button selection between all three ciphers
- Input fields for plaintext, key, encrypted output, and decrypted output
- Encrypt / Decrypt / Clear / Exit buttons

```
┌──────────────────────────────────────────┐
│  ○ Vigenère   ○ Playfair   ○ Caesar      │
├──────────────────────────────────────────┤
│  Plain text:  [________________________] │
│  Encrypted:   [________________________] │
│  Decrypted:   [________________________] │
│  Key / Shift: [________________________] │
├──────────────────────────────────────────┤
│    [Encrypt]  [Decrypt]  [Clear]  [Exit] │
└──────────────────────────────────────────┘
```

---

## 🗂️ Project Structure

```
CipherVault/
├── src/
│   ├── main/java/BobsCircus/
│   │   ├── CryptoManager.java          # Core cipher logic (static utility class)
│   │   ├── FXMainPane.java             # JavaFX UI layout and button handlers
│   │   └── FXDriver.java               # Application entry point (extends Application)
│   └── test/java/BobsCircus/
│       ├── CryptoManagerPublicTest.java # Basic correctness tests
│       └── CryptoManagerGFATest.java   # Boundary + edge case tests
├── doc/
│   └── AOngomefen_Assignment3.pdf      # Pseudo-code and UML design document
├── build.gradle                        # Gradle build config (JavaFX + JUnit 5)
├── settings.gradle
├── gradlew                             # Gradle wrapper (macOS/Linux)
├── gradlew.bat                         # Gradle wrapper (Windows)
└── README.md
```

---

## 🧪 Running Tests

Tests use **JUnit 5**. Run them with the Gradle wrapper (no Gradle install needed):

```bash
./gradlew test
```

### Test coverage includes:
- `isStringInBounds` — upper and lower boundary characters (`' '` and `'_'`)
- **Caesar** — zero key, wrap-around at boundaries, round-trip correctness
- **Vigenère** — single char, boundary chars, multi-word strings, round-trip
- **Playfair** — duplicate letters, odd-length strings, symbols, single chars, round-trip

---

## 🚀 Running the App

Requires **JDK 17+**. JavaFX is managed automatically by the Gradle build.

```bash
# Build and run from Terminal (no Gradle install needed)
./gradlew run
```

The Gradle wrapper (`gradlew`) automatically downloads the correct Gradle version and JavaFX native libraries for your platform (including Apple Silicon / M1).

Or open in **IntelliJ IDEA** or **Eclipse** and import as a Gradle project.

---

## 📐 Design

See [`docs/design.pdf`](docs/design.pdf) for:
- Full pseudo-code for all 6 methods
- UML class diagram

### UML Summary

```
+------------------------------------------+
|             CryptoManager                |
+------------------------------------------+
| - LOWER_RANGE : char = ' '               |
| - UPPER_RANGE : char = '_'               |
| - RANGE       : int = 64                 |
| - ALPHABET64  : String                   |
+------------------------------------------+
| + isStringInBounds(plainText) : boolean  |
| + caesarEncryption(text, key) : String   |
| + caesarDecryption(text, key) : String   |
| + vigenereEncryption(text, key) : String |
| + vigenereDecryption(text, key) : String |
| + playfairEncryption(text, key) : String |
| + playfairDecryption(text, key) : String |
+------------------------------------------+
```

---

## 📋 TODO

- [x] Implement `caesarEncryption` / `caesarDecryption`
- [x] Implement `vigenereEncryption` / `vigenereDecryption`
- [x] Implement `playfairEncryption` / `playfairDecryption`
- [x] Add Javadoc to all public methods
- [x] Add `build.gradle`

---

## 👤 Author

**Andrea Ongomefen**        

