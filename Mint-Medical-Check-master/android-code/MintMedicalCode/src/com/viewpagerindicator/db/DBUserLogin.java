package com.viewpagerindicator.db;

import vnp.mr.mintmedical.item.UserLogin;

import com.vnp.core.datastore.DataStore;

import android.content.Context;

public class DBUserLogin extends BaseDB {

	public DBUserLogin(Context context) {
		super(context);
	}

	public boolean isLogin() {
		return DataStore.getInstance().get("ISLOGIN", false);
	}

	public void saveLogin(boolean checked) {
		DataStore.getInstance().save("ISLOGIN", checked);
	}

	@Override
	public Object getData() {
		UserLogin userLogin = new UserLogin();
		if(isLogin()){
			userLogin.parse(getDataStr());
		}
		return userLogin;
	}

	@Override
	public String getKey() {
		return "DBUserLogin";
	}
}