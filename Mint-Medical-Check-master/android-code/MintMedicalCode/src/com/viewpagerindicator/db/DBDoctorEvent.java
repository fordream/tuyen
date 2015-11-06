package com.viewpagerindicator.db;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import vnp.mr.mintmedical.item.Doctor;
import vnp.mr.mintmedical.item.DoctorEvent;
import android.content.Context;

public class DBDoctorEvent extends BaseDB {
	String idDoctor;
	public DBDoctorEvent(Context context, String idDortor) {
		super(context);
		this.idDoctor = idDortor;
	}

	@Override
	public Object getData() {
		List<Object> list = new ArrayList<Object>();
		try {
			JSONArray array = new JSONArray(getDataStr());
			for (int i = 0; i < array.length(); i++) {
				DoctorEvent item = new DoctorEvent();
				item.id = array.getJSONObject(i).getString("id");
				item.doctor_id = array.getJSONObject(i).getString("doctor_id");
				item.title = array.getJSONObject(i).getString("title");
				item.start = array.getJSONObject(i).getString("start");
				item.end = array.getJSONObject(i).getString("end");
				item.status = array.getJSONObject(i).getString("status");
				item.url = array.getJSONObject(i).getString("url");
				list.add(item);
			}
		} catch (JSONException e) {
		}

		return list;
	}

	@Override
	public String getKey() {
		return idDoctor + "DBDoctorEvent";
	}

}
