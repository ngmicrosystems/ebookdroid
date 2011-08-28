package org.ebookdroid.core.settings;

import org.ebookdroid.R;
import org.ebookdroid.core.log.LogContext;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

public class SettingsActivity extends BaseSettingsActivity {

    private static final LogContext LCTX = LogContext.ROOT.lctx("Settings");

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            addPreferencesFromResource(R.xml.preferences);
        } catch (final ClassCastException e) {
            LCTX.e("Shared preferences are corrupt! Resetting to default values.");

            final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            final SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.commit();

            PreferenceManager.setDefaultValues(this, R.xml.preferences, true);
            addPreferencesFromResource(R.xml.preferences);
        }

        decoratePreferences("rotation", "brightness");
        decoratePreferences("tapsize", "scrollheight");
        decoratePreferences("pagesinmemory", "maximagesize");
        decoratePreferences("brautoscandir");
        decoratePreferences("align", "animationType");
        decoratePreferences("book_align", "book_animationType");

        if (SettingsManager.getBookSettings() == null) {
            final Preference bookPrefs = findPreference("book_prefs");
            if (bookPrefs != null) {
                final PreferenceScreen preferenceScreen = getPreferenceScreen();
                preferenceScreen.removePreference(bookPrefs);
            }
        }

    }

    @Override
    protected void onPause() {
        SettingsManager.onSettingsChanged();
        super.onPause();
    }

}
