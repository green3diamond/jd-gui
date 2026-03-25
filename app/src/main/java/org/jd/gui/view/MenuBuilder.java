/*
 * Copyright (c) 2008-2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.gui.view;

import org.jd.gui.service.platform.PlatformService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class MenuBuilder {

    /**
     * Builds the main menu bar for the application.
     *
     * @param openAction              Action for opening files
     * @param closeAction             Action for closing the current tab
     * @param saveAction              Action for saving the current file
     * @param saveAllSourcesAction    Action for saving all decompiled sources
     * @param exitAction              Action for exiting the application
     * @param copyAction              Action for copying selected text
     * @param pasteAction             Action for pasting log content
     * @param selectAllAction         Action for selecting all text
     * @param findAction              Action for opening the find panel
     * @param openTypeAction          Action for opening a type by name
     * @param openTypeHierarchyAction Action for opening the type hierarchy
     * @param goToAction              Action for going to a specific line
     * @param backwardAction          Action for navigating backward in history
     * @param forwardAction           Action for navigating forward in history
     * @param searchAction            Action for searching across files
     * @param jdWebSiteAction         Action for opening the JD website
     * @param jdGuiIssuesAction       Action for opening JD-GUI issues page
     * @param jdCoreIssuesAction      Action for opening JD-Core issues page
     * @param preferencesAction       Action for opening preferences
     * @param aboutAction             Action for showing the about dialog
     * @param recentFiles             The "Recent Files" submenu
     * @param browser                 Whether the desktop browser is supported
     * @return a fully constructed JMenuBar
     */
    public static JMenuBar buildMenuBar(
            Action openAction,
            Action closeAction,
            Action saveAction,
            Action saveAllSourcesAction,
            Action exitAction,
            Action copyAction,
            Action pasteAction,
            Action selectAllAction,
            Action findAction,
            Action openTypeAction,
            Action openTypeHierarchyAction,
            Action goToAction,
            Action backwardAction,
            Action forwardAction,
            Action searchAction,
            Action jdWebSiteAction,
            Action jdGuiIssuesAction,
            Action jdCoreIssuesAction,
            Action preferencesAction,
            Action aboutAction,
            JMenu recentFiles,
            boolean browser) {

        int menuShortcutKeyMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();
        JMenuBar menuBar = new JMenuBar();

        // File menu
        JMenu menu = new JMenu("File");
        menuBar.add(menu);
        menu.add(openAction).setAccelerator(KeyStroke.getKeyStroke('O', menuShortcutKeyMask));
        menu.addSeparator();
        menu.add(closeAction).setAccelerator(KeyStroke.getKeyStroke('W', menuShortcutKeyMask));
        menu.addSeparator();
        menu.add(saveAction).setAccelerator(KeyStroke.getKeyStroke('S', menuShortcutKeyMask));
        menu.add(saveAllSourcesAction).setAccelerator(KeyStroke.getKeyStroke('S', menuShortcutKeyMask | InputEvent.ALT_DOWN_MASK));
        menu.addSeparator();
        menu.add(recentFiles);
        if (!PlatformService.getInstance().isMac()) {
            menu.addSeparator();
            menu.add(exitAction).setAccelerator(KeyStroke.getKeyStroke('X', InputEvent.ALT_DOWN_MASK));
        }

        // Edit menu
        menu = new JMenu("Edit");
        menuBar.add(menu);
        menu.add(copyAction).setAccelerator(KeyStroke.getKeyStroke('C', menuShortcutKeyMask));
        menu.add(pasteAction).setAccelerator(KeyStroke.getKeyStroke('V', menuShortcutKeyMask));
        menu.addSeparator();
        menu.add(selectAllAction).setAccelerator(KeyStroke.getKeyStroke('A', menuShortcutKeyMask));
        menu.addSeparator();
        menu.add(findAction).setAccelerator(KeyStroke.getKeyStroke('F', menuShortcutKeyMask));

        // Navigation menu
        menu = new JMenu("Navigation");
        menuBar.add(menu);
        menu.add(openTypeAction).setAccelerator(KeyStroke.getKeyStroke('T', menuShortcutKeyMask));
        menu.add(openTypeHierarchyAction).setAccelerator(KeyStroke.getKeyStroke('H', menuShortcutKeyMask));
        menu.addSeparator();
        menu.add(goToAction).setAccelerator(KeyStroke.getKeyStroke('L', menuShortcutKeyMask));
        menu.addSeparator();
        menu.add(backwardAction).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, InputEvent.ALT_DOWN_MASK));
        menu.add(forwardAction).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, InputEvent.ALT_DOWN_MASK));

        // Search menu
        menu = new JMenu("Search");
        menuBar.add(menu);
        menu.add(searchAction).setAccelerator(KeyStroke.getKeyStroke('S', menuShortcutKeyMask | InputEvent.SHIFT_DOWN_MASK));

        // Help menu
        menu = new JMenu("Help");
        menuBar.add(menu);
        if (browser) {
            menu.add(jdWebSiteAction);
            menu.add(jdGuiIssuesAction);
            menu.add(jdCoreIssuesAction);
            menu.addSeparator();
        }
        menu.add(preferencesAction).setAccelerator(KeyStroke.getKeyStroke('P', menuShortcutKeyMask | InputEvent.SHIFT_DOWN_MASK));
        if (!PlatformService.getInstance().isMac()) {
            menu.addSeparator();
            menu.add(aboutAction).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
        }

        return menuBar;
    }
}
