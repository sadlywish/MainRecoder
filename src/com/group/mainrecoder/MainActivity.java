package com.group.mainrecoder;


import com.example.mainrecoder.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		if (pref.getString("recoderType", null)==null) {
			pref.edit().putString("recoderType", "amr").commit();
		}
		RecoderFactory.setPref(pref);
		FileManagement.setPref(pref);
		FileManagement.setSuffixs(getResources().getStringArray(R.array.recoderEntity));
		Intent intent = new Intent(this, RecoderActivity.class);
		startActivity(intent);
	}

}
