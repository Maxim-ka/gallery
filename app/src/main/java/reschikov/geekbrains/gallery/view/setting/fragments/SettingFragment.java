package reschikov.geekbrains.gallery.view.setting.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import reschikov.geekbrains.gallery.R;
import reschikov.geekbrains.gallery.Rule;

import static androidx.preference.PreferenceManager.getDefaultSharedPreferences;

public class SettingFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

	private static final String DEF_TYPE_STYLE = "style";
	private static final String KEY_STYLES = "styles";
	private ListPreference listThemes;
	private EditTextPreference textKeyApiPixaBay;
	private SharedPreferences sp;

	@Override
	public void onCreatePreferences(Bundle bundle, String s) {
		setPreferencesFromResource(R.xml.preference, s);
		listThemes = findPreference(KEY_STYLES);
		textKeyApiPixaBay = findPreference(Rule.KEY_PIXABAY);
		listThemes.setSummary(listThemes.getEntry());
		textKeyApiPixaBay.setSummary(textKeyApiPixaBay.getText());
		if (getContext() != null){
			sp = getDefaultSharedPreferences(getContext());
			sp.registerOnSharedPreferenceChangeListener(this);
		}
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		SharedPreferences.Editor editor = sharedPreferences.edit();
		switch (key){
			case KEY_STYLES:
				if (getActivity() == null) return;
				Disposable disposable = Single.just(getResources().getIdentifier(listThemes.getValue(), DEF_TYPE_STYLE, getActivity().getPackageName()))
					.subscribeOn(Schedulers.computation())
					.observeOn(AndroidSchedulers.mainThread())
					.subscribe(integer -> {
						if (getActivity() != null){
							SharedPreferences preferences = getActivity().getSharedPreferences(Rule.NAME_THEME, Context.MODE_PRIVATE);
							SharedPreferences.Editor editorPreferences = preferences.edit();
							editorPreferences.putInt(Rule.KEY_THEME, integer);
							editorPreferences.apply();
							editor.putString(key, listThemes.getValue());
							editor.apply();
							getActivity().recreate();
						}
					});
				break;
			case Rule.KEY_PIXABAY:
				editor.putString(key, textKeyApiPixaBay.getText());
				editor.apply();
				textKeyApiPixaBay.setSummary(textKeyApiPixaBay.getText());
				break;
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		sp.unregisterOnSharedPreferenceChangeListener(this);
	}
}
