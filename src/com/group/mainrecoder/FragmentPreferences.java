/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.group.mainrecoder;

import com.example.mainrecoder.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;

/**
 * Demonstration of PreferenceFragment, showing a single fragment in an
 * activity.
 */
public class FragmentPreferences extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		// Display the fragment as the main content.
		getFragmentManager().beginTransaction()
				.replace(android.R.id.content, new PrefsFragment()).commit();
	}

	@SuppressLint("NewApi")
	public static class PrefsFragment extends PreferenceFragment implements OnPreferenceChangeListener {
		 ListPreference lp;//创建一个ListPreference对象
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			// Load the preferences from an XML resource
			addPreferencesFromResource(R.xml.fragmented_preferences);
			lp = (ListPreference) findPreference("recoderType");
			   //获取ListPreference中的实体内容
			   CharSequence[] entries=lp.getEntries();
			   //获取ListPreference中的实体内容的下标值
			   int index=lp.findIndexOfValue(PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("recoderType", "amr"));
			   //把listPreference中的摘要显示为当前ListPreference的实体内容中选择的那个项目
			   lp.setSummary(entries[index]);
			lp.setOnPreferenceChangeListener(this);
		}

		@Override
		public boolean onPreferenceChange(Preference preference, Object newValue) {
			 if(preference instanceof ListPreference){
				   //把preference这个Preference强制转化为ListPreference类型
				   ListPreference listPreference=(ListPreference)preference;
				   //获取ListPreference中的实体内容
				   CharSequence[] entries=listPreference.getEntries();
				   //获取ListPreference中的实体内容的下标值
				   int index=listPreference.findIndexOfValue((String)newValue);
				   //把listPreference中的摘要显示为当前ListPreference的实体内容中选择的那个项目
				   listPreference.setSummary(entries[index]);
				  }
				  return true;
		}
	}
	
	

}
