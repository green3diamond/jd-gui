package org.jd.gui.view.component;

import org.fife.ui.rsyntaxtextarea.DocumentRange;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

class ClassFilePageTest {

    private HashMap<String, TypePage.DeclarationData> initDeclarations() {
        TypePage.DeclarationData data = new TypePage.DeclarationData(0, 1, "Test", "test", "I");
        HashMap<String, TypePage.DeclarationData> declarations = new HashMap<>();

        declarations.put("Test", data);
        declarations.put("test/Test", data);

        declarations.put("Test-attributeInt-I", data);
        declarations.put("Test-attributeBoolean-Z", data);
        declarations.put("Test-attributeArrayBoolean-[[Z", data);
        declarations.put("Test-attributeString-Ljava/lang/String;", data);

        declarations.put("test/Test-attributeInt-I", data);
        declarations.put("test/Test-attributeBoolean-Z", data);
        declarations.put("test/Test-attributeArrayBoolean-[[Z", data);
        declarations.put("test/Test-attributeString-Ljava/lang/String;", data);

        declarations.put("Test-getInt-()I", data);
        declarations.put("Test-getString-()Ljava/lang/String;", data);
        declarations.put("Test-add-(JJ)J", data);
        declarations.put("Test-createBuffer-(I)[C", data);

        declarations.put("test/Test-getInt-()I", data);
        declarations.put("test/Test-getString-()Ljava/lang/String;", data);
        declarations.put("test/Test-add-(JJ)J", data);
        declarations.put("test/Test-createBuffer-(I)[C", data);

        return declarations;
    }

    private TreeMap<Integer, HyperlinkPage.HyperlinkData> initHyperlinks() {
        TreeMap<Integer, HyperlinkPage.HyperlinkData> hyperlinks = new TreeMap<>();
        hyperlinks.put(0, new TypePage.HyperlinkReferenceData(0, 1, new TypePage.ReferenceData("java/lang/Integer", "MAX_VALUE", "I", "Test")));
        hyperlinks.put(1, new TypePage.HyperlinkReferenceData(0, 1, new TypePage.ReferenceData("java/lang/Integer", "toString", "()Ljava/lang/String;", "Test")));
        return hyperlinks;
    }

    private ArrayList<TypePage.StringData> initStrings() {
        ArrayList<TypePage.StringData> strings = new ArrayList<>();
        strings.add(new TypePage.StringData(0, 3, "abc", "Test"));
        return strings;
    }

    @Test
    void testMatchFragmentAndAddDocumentRange_exactField() {
        HashMap<String, TypePage.DeclarationData> declarations = initDeclarations();
        ArrayList<DocumentRange> ranges = new ArrayList<>();

        ClassFilePage.matchFragmentAndAddDocumentRange("Test-attributeBoolean-Z", declarations, ranges);
        assertEquals(1, ranges.size());
    }

    @Test
    void testMatchFragmentAndAddDocumentRange_qualifiedField() {
        HashMap<String, TypePage.DeclarationData> declarations = initDeclarations();
        ArrayList<DocumentRange> ranges = new ArrayList<>();

        ClassFilePage.matchFragmentAndAddDocumentRange("test/Test-attributeBoolean-Z", declarations, ranges);
        assertEquals(1, ranges.size());
    }

    @Test
    void testMatchFragmentAndAddDocumentRange_wildcardPackage() {
        HashMap<String, TypePage.DeclarationData> declarations = initDeclarations();
        ArrayList<DocumentRange> ranges = new ArrayList<>();

        ClassFilePage.matchFragmentAndAddDocumentRange("*/Test-attributeBoolean-Z", declarations, ranges);
        assertEquals(2, ranges.size());
    }

    @Test
    void testMatchFragmentAndAddDocumentRange_method() {
        HashMap<String, TypePage.DeclarationData> declarations = initDeclarations();
        ArrayList<DocumentRange> ranges = new ArrayList<>();

        ClassFilePage.matchFragmentAndAddDocumentRange("Test-createBuffer-(I)[C", declarations, ranges);
        assertEquals(1, ranges.size());
    }

    @Test
    void testMatchFragmentAndAddDocumentRange_wildcardMethodDescriptor() {
        HashMap<String, TypePage.DeclarationData> declarations = initDeclarations();
        ArrayList<DocumentRange> ranges = new ArrayList<>();

        ClassFilePage.matchFragmentAndAddDocumentRange("*/Test-getString-(*)?", declarations, ranges);
        assertEquals(2, ranges.size());
    }

    @Test
    void testMatchQueryAndAddDocumentRange_stringSearch() {
        HashMap<String, String> parameters = new HashMap<>();
        HashMap<String, TypePage.DeclarationData> declarations = initDeclarations();
        TreeMap<Integer, HyperlinkPage.HyperlinkData> hyperlinks = initHyperlinks();
        ArrayList<TypePage.StringData> strings = initStrings();
        ArrayList<DocumentRange> ranges = new ArrayList<>();

        parameters.put("highlightPattern", "ab");
        parameters.put("highlightFlags", "s");
        parameters.put("highlightScope", null);

        ClassFilePage.matchQueryAndAddDocumentRange(parameters, declarations, hyperlinks, strings, ranges);
        assertEquals(1, ranges.size());
    }

    @Test
    void testMatchScope_nullOrEmpty() {
        assertTrue(ClassFilePage.matchScope(null, "java/lang/String"));
        assertTrue(ClassFilePage.matchScope("", "java/lang/String"));
    }

    @Test
    void testMatchScope_exactMatch() {
        assertTrue(ClassFilePage.matchScope("java/lang/String", "java/lang/String"));
    }

    @Test
    void testMatchScope_wildcardMatch() {
        assertTrue(ClassFilePage.matchScope("*/lang/String", "java/lang/String"));
        assertTrue(ClassFilePage.matchScope("*/String", "java/lang/String"));
        assertTrue(ClassFilePage.matchScope("*/Test", "Test"));
    }
}
