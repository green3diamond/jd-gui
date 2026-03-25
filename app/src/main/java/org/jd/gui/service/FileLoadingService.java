/*
 * Copyright (c) 2008-2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.gui.service;

import org.jd.gui.api.API;
import org.jd.gui.model.configuration.Configuration;
import org.jd.gui.spi.FileLoader;
import org.jd.gui.view.MainView;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileLoadingService {
    protected final API api;
    protected final Configuration configuration;
    protected final MainView mainView;

    public FileLoadingService(API api, Configuration configuration, MainView mainView) {
        this.api = api;
        this.configuration = configuration;
        this.mainView = mainView;
    }

    public void openFile(File file) {
        openFiles(Collections.singletonList(file));
    }

    @SuppressWarnings("unchecked")
    public void openFiles(List<File> files) {
        ArrayList<String> errors = new ArrayList<>();

        for (File file : files) {
            // Check input file
            if (file.exists()) {
                FileLoader loader = api.getFileLoader(file);
                if ((loader != null) && !loader.accept(api, file)) {
                    errors.add("Invalid input fileloader: '" + file.getAbsolutePath() + "'");
                }
            } else {
                errors.add("File not found: '" + file.getAbsolutePath() + "'");
            }
        }

        if (errors.isEmpty()) {
            for (File file : files) {
                if (api.openURI(file.toURI())) {
                    configuration.addRecentFile(file);
                    mainView.updateRecentFilesMenu(configuration.getRecentFiles());
                }
            }
        } else {
            StringBuilder messages = new StringBuilder();
            int index = 0;

            for (String error : errors) {
                if (index > 0) {
                    messages.append('\n');
                }
                if (index >= 20) {
                    messages.append("...");
                    break;
                }
                messages.append(error);
                index++;
            }

            JOptionPane.showMessageDialog(mainView.getMainFrame(), messages.toString(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
