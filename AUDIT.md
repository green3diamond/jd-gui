# JD-GUI Codebase Audit

## 1. Java and Gradle Versions

| Component | Current Version |
|-----------|----------------|
| Java Source/Target Compatibility | 1.8 |
| Gradle Wrapper | 5.2.1 |
| Gradle Distribution | gradle-5.2.1-bin.zip |

## 2. Modules and Responsibilities

| Module | Version | Responsibility |
|--------|---------|---------------|
| **api** | 1.0.0 | Pure API module: defines SPI interfaces (`org.jd.gui.spi`), API interface (`org.jd.gui.api.API`), feature interfaces, and model interfaces. No external dependencies. |
| **app** | 1.6.6 | Application module: entry points (`App`, `OsxApp`), controllers (`MainController` + 7 sub-controllers), Swing views (9 view classes), view components (6 classes), renderers (2 classes), service registries (14 singleton service classes), utility classes (5), and model classes (3). |
| **services** | 1.6.6 | Service provider implementations: container models (8), service providers (59 across indexer/loader/saver/type/treenode/actions/preferences), view components (21 text/code page classes), and utility classes (18). |

**Total Java source files:** 226 (35 in api, 58 in app, 126 in services, plus generated ANTLR sources)

## 3. All Swing Components in the View Layer

### App Module Views (`app/src/main/java/org/jd/gui/view/`)

| Class | Type | Layout Manager | Key Components |
|-------|------|---------------|----------------|
| `MainView` | JFrame wrapper | BorderLayout, CardLayout, BoxLayout | JMenuBar, JToolBar, JComboBox (find), JCheckBox, MainTabbedPanel |
| `AboutView` | JDialog | BorderLayout, GridLayout(2,2) | JLabel (logo), version labels, JButton |
| `GoToView` | JDialog | Box layout | JTextField, JLabel, JButton (OK/Cancel) |
| `OpenTypeView` | JDialog | BorderLayout, Box | JTextField, JList (custom renderer), JScrollPane, JButton |
| `OpenTypeHierarchyView` | JDialog | BorderLayout, Box | Tree (custom JTree), JScrollPane, JButton |
| `PreferencesView` | JDialog | BorderLayout, Box | JScrollPane, dynamic PreferencesPanels, JButton |
| `SaveAllSourcesView` | JDialog | Box layout | JLabel, JProgressBar, JButton (Cancel) |
| `SearchInConstantPoolsView` | JDialog | BorderLayout, GridLayout(2,1), Box | JTextField, 8x JCheckBox, Tree, JScrollPane, JButton |
| `SelectLocationView` | JDialog (undecorated) | BorderLayout | JLabel, Tree, popup-style display |

### App Module View Components (`app/src/main/java/org/jd/gui/view/component/`)

| Class | Extends | Purpose |
|-------|---------|---------|
| `IconButton` | JButton | Compact icon-only button for toolbars |
| `List` | JList | Custom list with TreeNodeData renderer, input map overrides |
| `Tree` | JTree | Custom tree with input map overrides, accessibility |
| `panel/MainTabbedPanel` | TabbedPanel | Main content area with CardLayout (empty state vs tabs) |
| `panel/TabbedPanel` | JPanel | Tab management with close buttons, context menus |
| `panel/TreeTabbedPanel` | JPanel | Master-detail: JSplitPane with Tree + TabbedPanel |

### App Module Renderers

| Class | Implements | Purpose |
|-------|-----------|---------|
| `OpenTypeListCellRenderer` | ListCellRenderer\<OpenTypeListCellBean\> | Type search result rendering |
| `TreeNodeRenderer` | TreeCellRenderer (raw) | Tree node icon + label rendering |

### Services Module View Components (`services/src/main/java/org/jd/gui/view/component/`)

| Class | Extends | Purpose |
|-------|---------|---------|
| `AbstractTextPage` | JPanel | Base: RSyntaxTextArea with Eclipse theme, code folding |
| `TextPage` | AbstractTextPage | Copy, select-all, save support |
| `HyperlinkPage` | TextPage | Clickable hyperlinks in decompiled code |
| `CustomLineNumbersPage` | HyperlinkPage | Remapped line numbers for decompiled code |
| `TypePage` | CustomLineNumbersPage | Declarations, references, type navigation |
| `ClassFilePage` | TypePage | .class file decompilation via JD-Core |
| `JavaFilePage` | TypePage | .java source display with ANTLR parsing |
| `DynamicPage` | JPanel | Switches between ClassFilePage and JavaFilePage |
| `LogPage` | HyperlinkPage | Log file with stack trace hyperlinks |
| `ManifestFilePage` | HyperlinkPage | MANIFEST.MF with class reference hyperlinks |
| `XmlFilePage` | TypeReferencePage | XML with Spring bean class detection |
| `TypeReferencePage` | HyperlinkPage | Abstract base for pages with type references |
| `OneTypeReferencePerLinePage` | TypeReferencePage | Text files with one type per line |
| `EjbJarXmlFilePage` | TypeReferencePage | ejb-jar.xml with XPath-based type references |
| `WebXmlFilePage` | TypeReferencePage | web.xml with servlet/filter class references |
| `ModuleInfoFilePage` | ClassFilePage | Java 9+ module-info.class decompilation |
| `RoundMarkErrorStrip` | Custom | Custom error/notice marker visualization |

## 4. SPI Interfaces (`org.jd.gui.spi`)

| Interface | Methods | Purpose |
|-----------|---------|---------|
| `ContainerFactory` | `getType()`, `accept(API, Path)`, `make(API, Entry, Path)` | Creates Container implementations for archive types |
| `ContextualActionsFactory` | `make(API, Entry, String)` | Creates right-click context menu actions |
| `FileLoader` | `getExtensions()`, `getDescription()`, `accept(API, File)`, `load(API, File)` | Loads files by extension |
| `Indexer` | `getSelectors()`, `getPathPattern()`, `index(API, Entry, Indexes)` | Indexes entries for search |
| `PanelFactory` | `getTypes()`, `make(API, Container)` | Creates main editor panels |
| `PasteHandler` | `accept(Object)`, `paste(API, Object)` | Handles clipboard paste |
| `PreferencesPanel` | `getPanel()`, `loadPreferences(Map)`, `savePreferences(Map)`, `arePreferencesValid()`, + 5 more | Plugin preferences UI |
| `SourceLoader` | `getSource(API, Entry)`, `loadSource(API, Entry)`, `loadSourceFile(API, Entry)` | Loads/decompiles source code |
| `SourceSaver` | `getSelectors()`, `getPathPattern()`, `getSourcePath(Entry)`, `save(...)`, `saveContent(...)` | Saves decompiled sources |
| `TreeNodeFactory` | `getSelectors()`, `getPathPattern()`, `make(API, Entry)` | Creates tree nodes for file tree |
| `TypeFactory` | `getSelectors()`, `getPathPattern()`, `make(API, Entry)`, `make(API, Entry, String)` | Creates Type objects for type hierarchy |
| `UriLoader` | `getSchemes()`, `accept(API, URI)`, `load(API, URI)` | Loads content from URI schemes |

**Nested interfaces:** `PreferencesPanel.PreferencesPanelChangeListener`, `SourceSaver.Controller`, `SourceSaver.Listener`

## 5. Dependencies — Current vs Latest Versions

| Dependency | Current | Latest Stable | Module | Scope |
|-----------|---------|---------------|--------|-------|
| com.fifesoft:rsyntaxtextarea | 3.0.4 | 3.5.3 | services | compile |
| org.ow2.asm:asm | 7.1 | 9.7.1 | services | compile |
| org.jd:jd-core | 1.1.3 | 1.1.3 | services | compile |
| org.antlr:antlr4-runtime | 4.5 | 4.13.2 | services | compile |
| org.antlr:antlr4 | 4.5 | 4.13.2 | services | antlr4 |
| com.yuvimasory:orange-extensions | 1.3.0 | 1.3.0 | app | provided |
| junit:junit | 4.12 | 4.13.2 (JUnit 5: 5.11.4) | services | test |
| com.netflix.nebula:gradle-ospackage-plugin | 5.3.0 | 11.10.1 | root | buildscript |
| edu.sc.seis.gradle:launch4j | 2.4.4 | 3.0.6 | root | buildscript |
| net.sf.proguard:proguard-gradle | 6.1.0 | 7.6.1 (com.guardsquare) | root | buildscript |

## 6. Anonymous Inner Classes

**Total: 53 instances across 27 files**

### By Type

| Anonymous Class Type | Count | Files |
|---------------------|-------|-------|
| AbstractAction | 9 | AboutView, OpenTypeHierarchyView, SearchInConstantPoolsView(2), OpenTypeView, PreferencesView, GoToView, SelectLocationView, SaveAllSourcesView |
| MouseAdapter | 8 | OpenTypeHierarchyView, SearchInConstantPoolsView, OpenTypeView, AbstractTextPage, HyperlinkPage, TabbedPanel, TreeTabbedPanel, SelectLocationView |
| KeyAdapter | 8 | MainView, SearchInConstantPoolsView(2), OpenTypeView(2), GoToView, OpenTypeHierarchyView, SelectLocationView |
| TreeExpansionListener | 3 | OpenTypeHierarchyView, SearchInConstantPoolsView, TreeTabbedPanel |
| FocusAdapter | 3 | SearchInConstantPoolsView(2), SelectLocationView |
| DocumentListener | 3 | SearchInConstantPoolsView, OpenTypeView, GoToView |
| WindowAdapter | 2 | SelectLocationView, SaveAllSourcesView |
| ClassVisitor (ASM) | 3 | JarContainerEntryUtil, ClassFileTypeFactoryProvider(2) |
| LinkedHashMap (custom) | 2 | SearchInConstantPoolsController, OpenTypeController |
| BiConsumer | 1 | SearchInConstantPoolsController |
| TriConsumer | 1 | SearchInConstantPoolsController |
| Runnable | 1 | InterProcessCommunicationUtil |
| FocusListener | 1 | OpenTypeView |
| MouseListener | 1 | TabbedPanel |
| ChangeListener | 1 | MainView |
| PageChangeListener | 1 | MainView |
| JTabbedPane (custom) | 1 | TabbedPanel |
| TreeNodeRenderer (custom) | 1 | TreeTabbedPanel |
| Container (custom) | 1 | AbstractFileLoaderProvider |
| JComponent (custom) | 1 | MavenOrgSourceLoaderPreferencesProvider |
| Type.Field | 1 | ClassFileTypeFactoryProvider |
| Type.Method | 1 | ClassFileTypeFactoryProvider |

## 7. Raw Types and Unchecked Casts

### Raw Collection Types

| Location | Raw Type | Description |
|----------|----------|-------------|
| `Indexes.java:67` | `Map<String, Collection>` | **Root cause** — interface returns raw Collection |
| 19+ files | `Map<String, Collection>` | All callers of `Indexes.getIndex()` inherit raw type |
| `ContainerPanelFactoryProvider.java:161` | `Collection value` | Raw Collection variable |
| `ContainerPanelFactoryProvider.java:164` | `new ArrayList()` | Raw ArrayList instantiation |
| `List.java:36` | `ListCellRenderer` | Raw ListCellRenderer implementation |
| `TreeNodeRenderer.java:17` | `TreeCellRenderer` | Raw TreeCellRenderer (no generic) |

### @SuppressWarnings("unchecked") Count

**Total: 107 instances across 45 files**

Key files: MainController (8), TreeTabbedPanel (6), SearchInConstantPoolsView (4), SelectLocationView (4), OpenTypeController (4)

### Unchecked Casts

| Location | Cast | Context |
|----------|------|---------|
| `MainController:492` | `(List<File>)` | Drag-and-drop transfer data |
| `MainController:675` | `(Future<Indexes>)` | Client property retrieval |
| `MainView:271,377` | `(T)page` | Generic type parameter casts |
| `SearchInConstantPoolsView` (8 locations) | `(T)` | Generic tree node casts |
| `MainTabbedPanel:80,83` | `(T)` | Client property and component casts |
| `TreeTabbedPanel:61,88` | `(T)` | Tree node casts |
| `InterProcessCommunicationUtil:33` | `(String[])` | Object deserialization |

---

*Audit generated as part of JD-GUI modernization effort.*
