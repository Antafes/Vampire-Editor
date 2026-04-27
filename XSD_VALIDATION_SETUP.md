# XSD Validation System - Strict XSD 1.1 Only

## ✅ Setup Summary

The validation path now exclusively uses the strict schema `character-strict.xsd`.

### 1. ✅ Strict XSD 1.1 validator as test dependency
- **File:** `pom.xml`
- **Dependency:** `org.opengis.cite.xerces:xercesImpl-xsd11:2.12-beta-r1667115`
- **Scope:** `test`
- **Purpose:** real XSD 1.1 validator with `xsd:assert` support

### 2. ✅ No fallback anymore
- **File:** `src/test/java/antafes/vampireEditor/xml/validation/XsdValidator.java`
- **Behavior:**
  - uses only `org.apache.xerces.jaxp.validation.XMLSchema11Factory`
  - no fallback to JDK/XSD 1.0
  - fails hard if no XSD 1.1 validator is available

### 3. ✅ Only one schema remains

#### `character-strict.xsd` - STRICT SCHEMA (XSD 1.1)
- **File:** `src/main/resources/character-strict.xsd`
- **Compatibility:** only with an XSD 1.1 validator
- **Rules:**
  - Non-NPCs must have `clan` and `road`
  - `generation` must be between `1` and `15`
- **Mechanism:** `xsd:assert`

### 4. ✅ Verification
The relevant strict tests were executed successfully.

```text
Tests run: 13, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

---

## 🔄 Usage in Code

### Strict validation
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

- [✅] old XSD 1.0 path removed
- [✅] only `character-strict.xsd` remains in the active validation path
- [✅] XSD 1.1 validator hard-wired
- [✅] relevant tests successfully verified

**Status: ✅ STRICT-SCHEMA-ONLY ACTIVE**
