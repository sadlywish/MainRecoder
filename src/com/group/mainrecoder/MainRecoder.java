package com.group.mainrecoder;

import java.io.IOException;

import javax.security.auth.callback.Callback;

import android.app.Application;

import com.kii.cloud.storage.Kii;
import com.kii.cloud.storage.KiiUser;
import com.kii.cloud.storage.callback.KiiUserCallBack;

public class MainRecoder extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		Kii.initialize(AppConstants.APP_ID, AppConstants.APP_KEY,
				AppConstants.APP_SITE);
		KiiUser.logIn(new KiiUserCallBack() {
			@Override
			public void onLoginCompleted(int token, KiiUser user,
					Exception exception) {
				// TODO Auto-generated method stub
				super.onLoginCompleted(token, user, exception);
				System.out.println("login");
			}
		}, "sas", "543888");
	}
}
