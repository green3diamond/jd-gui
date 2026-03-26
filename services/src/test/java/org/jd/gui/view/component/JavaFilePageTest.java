package org.jd.gui.view.component;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class JavaFilePageTest {

    private HashMap<String, TypePage.DeclarationData> initDeclarations() {
        TypePage.DeclarationData data = new TypePage.DeclarationData(0, 1, "Test", "test", "I");
        HashMap<String, TypePage.DeclarationData> declarations = new HashMap<>();

        declarations.put("Test", data);
        declarations.put("test/Test", data);
        declarations.put("*/Test", data);

        declarations.put("Test-attributeInt-I", data);
        declarations.put("Test-attributeBoolean-Z", data);
        declarations.put("Test-attributeArrayBoolean-[[Z", data);
        declarations.put("Test-attributeString-Ljava/lang/String;", data);

        declarations.put("test/Test-attributeInt-I", data);
        declarations.put("test/Test-attributeBoolean-Z", data);
        declarations.put("test/Test-attributeArrayBoolean-[[Z", data);
        declarations.put("test/Test-attributeString-Ljava/lang/String;", data);

        return declarations;
    }

    @Test
    void testDeclarationDataCreation() {
        TypePage.DeclarationData data = new TypePage.DeclarationData(10, 20, "MyType", "myField", "I");
        assertEquals(10, data.startPosition);
        assertEquals(30, data.endPosition); // startPosition + length = 10 + 20
        assertEquals("MyType", data.typeName);
        assertEquals("myField", data.name);
        assertEquals("I", data.descriptor);
    }

    @Test
    void testDeclarationsMapPopulation() {
        HashMap<String, TypePage.DeclarationData> declarations = initDeclarations();
        assertFalse(declarations.isEmpty());
        assertTrue(declarations.containsKey("Test"));
        assertTrue(declarations.containsKey("test/Test"));
        assertTrue(declarations.containsKey("*/Test"));
    }

    @Test
    void testDeclarationsContainFieldEntries() {
        HashMap<String, TypePage.DeclarationData> declarations = initDeclarations();
        assertNotNull(declarations.get("Test-attributeInt-I"));
        assertNotNull(declarations.get("test/Test-attributeBoolean-Z"));
        assertNotNull(declarations.get("Test-attributeArrayBoolean-[[Z"));
    }
}
