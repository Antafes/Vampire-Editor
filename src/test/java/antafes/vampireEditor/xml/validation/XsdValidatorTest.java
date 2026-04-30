/*
 * This file is part of Vampire Editor.
 *
 * Vampire Editor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Vampire Editor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Vampire Editor. If not, see <http://www.gnu.org/licenses/>.
 *
 * @package Vampire Editor
 * @author Marian Pollzien <map@wafriv.de>
 * @copyright (c) 2026, Marian Pollzien
 * @license https://www.gnu.org/licenses/lgpl.html LGPLv3
 */

package antafes.vampireEditor.xml.validation;

import antafes.vampireEditor.BaseTest;
import antafes.vampireEditor.VampireEditor;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.xml.sax.SAXParseException;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Tests for guaranteed XSD 1.1 validation with xsd:assert rules.
 *
 * This test class verifies:
 * - Strict schema `character-strict.xsd` is used consistently
 * - XSD-1.1 assertions are properly enforced
 * - Strict validator availability is verified
 */
@Test
public class XsdValidatorTest extends BaseTest {

    @BeforeClass
    public void beforeClass() {
        try {
            new VampireEditor();
            System.out.println("\n========== XSD Validator Test Suite ==========");
            System.out.println("Using validator implementation: " + XsdValidator.getValidatorImplementation());
            System.out.println("Using schema: character-strict.xsd");
            System.out.println("============================================\n");
        } catch (Exception e) {
            throw new RuntimeException("Test setup failed", e);
        }
    }

    /**
     * Verifies non-NPC character MUST have clan and road.
     * This tests the xsd:assert rule.
     */
    public void testNonNpcMissingClanViolatesAssertion() throws Exception {
        if (XsdValidator.getValidatorImplementation().equals("Unavailable")) {
            throw new SkipException("Strict validation requires an XSD 1.1 validator");
        }

        String invalidXml = """
            <?xml version="1.0" encoding="UTF-8"?>
            <character id="test-non-npc" isNpc="false">
                <name>Test Character</name>
                <generation>3</generation>
                <experience>0</experience>
                <demeanor>Deviant</demeanor>
                <concept>Soldier</concept>
                <attributes>
                    <attribute key="strength">3</attribute>
                    <attribute key="dexterity">3</attribute>
                    <attribute key="stamina">3</attribute>
                    <attribute key="charisma">3</attribute>
                    <attribute key="manipulation">3</attribute>
                    <attribute key="appearance">3</attribute>
                    <attribute key="perception">3</attribute>
                    <attribute key="intelligence">3</attribute>
                    <attribute key="wits">3</attribute>
                </attributes>
                <abilities>
                    <ability key="ability1">0</ability>
                    <ability key="ability2">0</ability>
                    <ability key="ability3">0</ability>
                    <ability key="ability4">0</ability>
                    <ability key="ability5">0</ability>
                    <ability key="ability6">0</ability>
                    <ability key="ability7">0</ability>
                    <ability key="ability8">0</ability>
                    <ability key="ability9">0</ability>
                    <ability key="ability10">0</ability>
                    <ability key="ability11">0</ability>
                    <ability key="ability12">0</ability>
                    <ability key="ability13">0</ability>
                    <ability key="ability14">0</ability>
                    <ability key="ability15">0</ability>
                    <ability key="ability16">0</ability>
                    <ability key="ability17">0</ability>
                    <ability key="ability18">0</ability>
                    <ability key="ability19">0</ability>
                    <ability key="ability20">0</ability>
                    <ability key="ability21">0</ability>
                    <ability key="ability22">0</ability>
                    <ability key="ability23">0</ability>
                    <ability key="ability24">0</ability>
                    <ability key="ability25">0</ability>
                    <ability key="ability26">0</ability>
                    <ability key="ability27">0</ability>
                    <ability key="ability28">0</ability>
                    <ability key="ability29">0</ability>
                    <ability key="ability30">0</ability>
                </abilities>
                <advantages>
                    <advantage key="advantage1">0</advantage>
                </advantages>
                <willpower>6</willpower>
                <bloodPool>10</bloodPool>
            </character>
            """;

        java.nio.file.Path tmpFile = java.nio.file.Paths.get(System.getProperty("java.io.tmpdir"), "test-invalid.xml");
        java.nio.file.Files.write(tmpFile, invalidXml.getBytes(StandardCharsets.UTF_8));

        try (InputStream schemaInputStream = VampireEditor.getFileInJar("character-strict.xsd")) {
            XsdValidator.validate(tmpFile.toFile(), schemaInputStream);
            Assert.fail("Validation should have failed: non-NPC missing clan and road");
        } catch (SAXParseException e) {
            // Expected: xsd:assert should catch the violation
            Assert.assertTrue(
                e.getMessage().contains("cvc-assertion") ||
                e.getMessage().contains("assert") ||
                e.getMessage().contains("Non-NPC") ||
                e.getMessage().contains("clan") ||
                e.getMessage().contains("road"),
                "Error message should mention the assertion: " + e.getMessage()
            );
        }
    }

    /**
     * Verifies generation must be between 1 and 15.
     * Tests the xsd:assert constraint.
     */
    public void testGenerationOutOfRangeViolatesAssertion() throws Exception {
        if (XsdValidator.getValidatorImplementation().equals("Unavailable")) {
            throw new SkipException("Strict validation requires an XSD 1.1 validator");
        }

        String invalidXml = """
            <?xml version="1.0" encoding="UTF-8"?>
            <character id="test-gen" isNpc="false">
                <name>Test Character</name>
                <clan>Brujah</clan>
                <generation>16</generation>
                <experience>0</experience>
                <demeanor>Deviant</demeanor>
                <concept>Soldier</concept>
                <attributes>
                    <attribute key="strength">3</attribute>
                    <attribute key="dexterity">3</attribute>
                    <attribute key="stamina">3</attribute>
                    <attribute key="charisma">3</attribute>
                    <attribute key="manipulation">3</attribute>
                    <attribute key="appearance">3</attribute>
                    <attribute key="perception">3</attribute>
                    <attribute key="intelligence">3</attribute>
                    <attribute key="wits">3</attribute>
                </attributes>
                <abilities>
                    <ability key="ability1">0</ability>
                    <ability key="ability2">0</ability>
                    <ability key="ability3">0</ability>
                    <ability key="ability4">0</ability>
                    <ability key="ability5">0</ability>
                    <ability key="ability6">0</ability>
                    <ability key="ability7">0</ability>
                    <ability key="ability8">0</ability>
                    <ability key="ability9">0</ability>
                    <ability key="ability10">0</ability>
                    <ability key="ability11">0</ability>
                    <ability key="ability12">0</ability>
                    <ability key="ability13">0</ability>
                    <ability key="ability14">0</ability>
                    <ability key="ability15">0</ability>
                    <ability key="ability16">0</ability>
                    <ability key="ability17">0</ability>
                    <ability key="ability18">0</ability>
                    <ability key="ability19">0</ability>
                    <ability key="ability20">0</ability>
                    <ability key="ability21">0</ability>
                    <ability key="ability22">0</ability>
                    <ability key="ability23">0</ability>
                    <ability key="ability24">0</ability>
                    <ability key="ability25">0</ability>
                    <ability key="ability26">0</ability>
                    <ability key="ability27">0</ability>
                    <ability key="ability28">0</ability>
                    <ability key="ability29">0</ability>
                    <ability key="ability30">0</ability>
                </abilities>
                <advantages>
                    <advantage key="advantage1">0</advantage>
                </advantages>
                <road key="humanity">5</road>
                <willpower>6</willpower>
                <bloodPool>10</bloodPool>
            </character>
            """;

        java.nio.file.Path tmpFile = java.nio.file.Paths.get(System.getProperty("java.io.tmpdir"), "test-gen-invalid.xml");
        java.nio.file.Files.write(tmpFile, invalidXml.getBytes(StandardCharsets.UTF_8));

        try (InputStream schemaInputStream = VampireEditor.getFileInJar("character-strict.xsd")) {
            XsdValidator.validate(tmpFile.toFile(), schemaInputStream);
            Assert.fail("Validation should have failed: generation out of range");
        } catch (SAXParseException e) {
            // Expected: xsd:assert should catch the violation
            Assert.assertTrue(
                e.getMessage().contains("cvc-assertion") ||
                e.getMessage().contains("assert") ||
                e.getMessage().contains("generation"),
                "Error message should mention generation constraint: " + e.getMessage()
            );
        }
    }

    /**
     * Verifies NPC without clan and road passes strict validation.
     */
    public void testNpcWithoutClanAndRoadIsValid() throws Exception {
        if (XsdValidator.getValidatorImplementation().equals("Unavailable")) {
            throw new SkipException("Strict validation requires an XSD 1.1 validator");
        }

        String validXml = """
            <?xml version="1.0" encoding="UTF-8"?>
            <character id="test-npc" isNpc="true">
                <name>Test NPC</name>
                <generation>3</generation>
                <experience>0</experience>
                <demeanor>Deviant</demeanor>
                <concept>Soldier</concept>
                <attributes>
                    <attribute key="strength">3</attribute>
                    <attribute key="dexterity">3</attribute>
                    <attribute key="stamina">3</attribute>
                    <attribute key="charisma">3</attribute>
                    <attribute key="manipulation">3</attribute>
                    <attribute key="appearance">3</attribute>
                    <attribute key="perception">3</attribute>
                    <attribute key="intelligence">3</attribute>
                    <attribute key="wits">3</attribute>
                </attributes>
                <abilities>
                    <ability key="ability1">0</ability>
                    <ability key="ability2">0</ability>
                    <ability key="ability3">0</ability>
                    <ability key="ability4">0</ability>
                    <ability key="ability5">0</ability>
                    <ability key="ability6">0</ability>
                    <ability key="ability7">0</ability>
                    <ability key="ability8">0</ability>
                    <ability key="ability9">0</ability>
                    <ability key="ability10">0</ability>
                    <ability key="ability11">0</ability>
                    <ability key="ability12">0</ability>
                    <ability key="ability13">0</ability>
                    <ability key="ability14">0</ability>
                    <ability key="ability15">0</ability>
                    <ability key="ability16">0</ability>
                    <ability key="ability17">0</ability>
                    <ability key="ability18">0</ability>
                    <ability key="ability19">0</ability>
                    <ability key="ability20">0</ability>
                    <ability key="ability21">0</ability>
                    <ability key="ability22">0</ability>
                    <ability key="ability23">0</ability>
                    <ability key="ability24">0</ability>
                    <ability key="ability25">0</ability>
                    <ability key="ability26">0</ability>
                    <ability key="ability27">0</ability>
                    <ability key="ability28">0</ability>
                    <ability key="ability29">0</ability>
                    <ability key="ability30">0</ability>
                </abilities>
                <advantages>
                    <advantage key="advantage1">0</advantage>
                </advantages>
                <willpower>6</willpower>
                <bloodPool>10</bloodPool>
            </character>
            """;

        java.nio.file.Path tmpFile = java.nio.file.Paths.get(System.getProperty("java.io.tmpdir"), "test-npc-valid.xml");
        java.nio.file.Files.write(tmpFile, validXml.getBytes(StandardCharsets.UTF_8));

        try (InputStream schemaInputStream = VampireEditor.getFileInJar("character-strict.xsd")) {
            XsdValidator.validate(tmpFile.toFile(), schemaInputStream);
            System.out.println("NPC without clan/road validated successfully (strict schema)");
        }
    }

    /**
     * Verifies validator can be identified and logs implementation info.
     */
    public void testValidatorImplementationIdentification() {
        String implementation = XsdValidator.getValidatorImplementation();
        Assert.assertNotNull(implementation);
        Assert.assertTrue(
            !implementation.equals("Unavailable"),
            "Strict path requires an XSD 1.1 validator, got: " + implementation
        );
        System.out.println("\nActive Validator: " + implementation);
        System.out.println("Features: XSD 1.1 strict (xsd:assert enabled)");
    }
}





