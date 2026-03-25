/*
 * Copyright (c) 2008-2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.gui.service;

import org.jd.gui.api.feature.IndexesChangeListener;
import org.jd.gui.api.feature.PreferencesChangeListener;
import org.jd.gui.api.model.Indexes;
import org.jd.gui.model.configuration.Configuration;

import javax.swing.*;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.Future;

public class PreferencesManager {
    protected final Configuration configuration;

    public PreferencesManager(Configuration configuration) {
        this.configuration = configuration;
    }

    public void checkPreferencesChange(JComponent page) {
        if (page instanceof PreferencesChangeListener) {
            Map<String, String> preferences = configuration.getPreferences();
            Integer currentHashcode = Integer.valueOf(preferences.hashCode());
            Integer lastHashcode = (Integer) page.getClientProperty("preferences-hashCode");

            if (!currentHashcode.equals(lastHashcode)) {
                ((PreferencesChangeListener) page).preferencesChanged(preferences);
                page.putClientProperty("preferences-hashCode", currentHashcode);
            }
        }
    }

    public void checkIndexesChange(JComponent page, Collection<Future<Indexes>> collectionOfFutureIndexes) {
        if (page instanceof IndexesChangeListener) {
            Integer currentHashcode = Integer.valueOf(collectionOfFutureIndexes.hashCode());
            Integer lastHashcode = (Integer) page.getClientProperty("collectionOfFutureIndexes-hashCode");

            if (!currentHashcode.equals(lastHashcode)) {
                ((IndexesChangeListener) page).indexesChanged(collectionOfFutureIndexes);
                page.putClientProperty("collectionOfFutureIndexes-hashCode", currentHashcode);
            }
        }
    }

    public Map<String, String> getPreferences() {
        return configuration.getPreferences();
    }
}
