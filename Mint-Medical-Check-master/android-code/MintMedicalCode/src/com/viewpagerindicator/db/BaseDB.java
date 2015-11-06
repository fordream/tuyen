package com.viewpagerindicator.db;

import android.content.Context;

import com.vnp.core.callback.CallBack;
import com.vnp.core.callback.ExeCallBack;
import com.vnp.core.datastore.DataStore;

public abstract class BaseDB {
	public Context context;

	public BaseDB(Context context) {
		this.context = context;
	}

	public void save(String data) {
		DataStore.getInstance().save(getKey(), data);
	}

	public String getDataStr() {
		return DataStore.getInstance().get(getKey(), "");
	}

	public abstract Object getData();

	public abstract String getKey();

	public Object getData(String id) {
		return null;
	}

	public CallBack getCallBack() {
		return null;
	}

	public final void exeCallBack() {
		if (getCallBack() != null) {
			new ExeCallBack().executeAsynCallBack(getCallBack());
		}
	}
}