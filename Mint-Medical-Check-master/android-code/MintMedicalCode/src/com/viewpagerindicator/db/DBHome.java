package com.viewpagerindicator.db;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import vnp.mr.mintmedical.base.MintUtils;
import vnp.mr.mintmedical.item.Doctor;
import vnp.mr.mintmedical.item.UserLogin;
import android.content.Context;

import com.vnp.core.callback.CallBack;
import com.vnp.core.callback.ResClientCallBack;
import com.vnp.core.service.RestClient;

public class DBHome extends BaseDB {
	DBUserLogin dbUserLogin;

	public DBHome(Context context) {
		super(context);
		dbUserLogin = new DBUserLogin(context);
	}

	@Override
	public Object getData() {
		List<Object> list = new ArrayList<Object>();
		return list;
	}

	@Override
	public String getKey() {
		return getId() + "DBHome";
	}

	public Object getData(String doctor_id) {
		List<Object> list = (List<Object>) getData();
		for (Object doctor : list) {
			if (doctor_id.equals(((Doctor) doctor).id)) {
				return doctor;
			}
		}
		return list;
	}

	public String getId() {
		UserLogin userLogin = (UserLogin) dbUserLogin.getData();
		return userLogin.custId + "";
	}

	@Override
	public CallBack getCallBack() {
		return new ResClientCallBack() {

			@Override
			public void onCallBack(Object arg0) {
				RestClient client = (RestClient) arg0;
				if (client.getResponseCode() == 200) {
					save(client.getResponse());
				}
			}

			@Override
			public String getUrl() {
				return String.format(MintUtils.URL_HOME + "/id=%s", getId());
			}
		};
	}

	public String getAppointments() {
		if (dbUserLogin.isLogin()) {
			try {
				JSONArray array = new JSONArray(getDataStr());
				return array.getJSONObject(0).getString("detailType");
			} catch (Exception exception) {

			}
		}
		return "Make or Cancel";
	}

	public String getPrescription() {
		if (dbUserLogin.isLogin()) {
			try {
				JSONArray array = new JSONArray(getDataStr());
				return array.getJSONObject(2).getString("detailType");
			} catch (Exception exception) {

			}
		}
		return "Request prescription renewal";
	}

	public String getNewAndAlerts() {
		if (dbUserLogin.isLogin()) {
			try {
				JSONArray array = new JSONArray(getDataStr());
				return array.getJSONObject(7).getString("detailType");
			} catch (Exception exception) {

			}
		}
		return "";
	}
}