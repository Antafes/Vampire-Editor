# XSD Validation System - Strict XSD 1.1 (Test Verification)

## ✅ Setup Summary

Strict XSD 1.1 validation currently runs in tests and uses the strict schema `character-strict.xsd`.

### 1. ✅ Strict XSD 1.1 validator as test dependency
- **File:** `pom.xml`
- **Dependency:** `org.opengis.cite.xerces:xercesImpl-xsd11:2.12-beta-r1667115`
- **Scope:** `test`
- **Purpose:** real XSD 1.1 validator with `xsd:assert` support for test verification

### 2. ✅ No fallback in test validator
- **File:** `src/test/java/antafes/vampireEditor/xml/validation/XsdValidator.java`
- **Behavior in test scope:**
  - uses only `org.apache.xerces.jaxp.validation.XMLSchema11Factory`
  - no fallback to JDK/XSD 1.0
  - fails hard if no XSD 1.1 validator is available

### 3. ⚠ Runtime note
- Production `CharacterStorage` JAXB loading (`src/main/java/.../entity/storage/CharacterStorage.java`) does **not** currently invoke `XsdValidator`.
- Strict XSD 1.1 assertion enforcement is therefore test-time verification unless validator + dependency are moved to main/runtime.

### 4. ✅ Only one strict schema is used for verification

#### `character-strict.xsd` - STRICT SCHEMA (XSD 1.1)
- **File:** `src/main/resources/character-strict.xsd`
- **Compatibility:** only with an XSD 1.1 validator
- **Rules:**
  - Non-NPCs must have `clan` and `road`
  - `generation` must be between `1` and `15`
- **Mechanism:** `xsd:assert`

### 5. ✅ Verification
The relevant strict tests were executed successfully.

```text
Tests run: 13, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

---

## 🔄 Usage in Code

### Strict validation in tests
```java
try (InputStream schemaInputStream = VampireEditor.getFileInJar("character-strict.xsd")) {
    XsdValidator.validate(xmlFile, schemaInputStream);
}
```

### Validator identification
```java
String implementation = XsdValidator.getValidatorImplementation();
// Example: "Xerces XMLSchema11Factory (XSD 1.1)"
```

---

## 📋 Architecture Details

### `XsdValidator`
- **Location:** `src/test/java/antafes/vampireEditor/xml/validation/XsdValidator.java`
- **Responsible for:**
  - creating the strict `SchemaFactory`
  - loading the XSD 1.1 validator
  - validating against `character-strict.xsd`
  - clear failure behavior without fallback

### `XsdValidatorTest`
- validates XSD 1.1 assertions directly
- verifies validator detection
- uses only `character-strict.xsd`

### `CharacterStorageTest`
- validates saved XML files exclusively against `character-strict.xsd`

---

## ✅ Final Checklist

- [✅] old XSD 1.0 test path removed
- [✅] strict verification uses `character-strict.xsd`
- [✅] test validator is hard-wired to XSD 1.1
- [✅] relevant tests successfully verified

**Status: ✅ Strict XSD 1.1 verification active in tests**
