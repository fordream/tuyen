package com.viewpagerindicator.db;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import vnp.mr.mintmedical.item.Clinic;
import vnp.mr.mintmedical.item.Doctor;
import android.content.Context;

public class DBClinic extends BaseDB {

	public DBClinic(Context context) {
		super(context);
	}

	@Override
	public Object getData() {
		List<Object> list = new ArrayList<Object>();
		try {
			JSONArray array = new JSONArray(getDataStr());
			for (int i = 0; i < array.length(); i++) {
				Clinic item = new Clinic();
				item.id = array.getJSONObject(i).getString("id");
				item.name = array.getJSONObject(i).getString("name");
				item.address = array.getJSONObject(i).getString("address");
				item.latitude = array.getJSONObject(i).getString("latitude");
				item.longitude = array.getJSONObject(i).getString("longitude");
				item.phone = array.getJSONObject(i).getString("phone");
				item.status = array.getJSONObject(i).getString("status");
				item.zipcode = array.getJSONObject(i).getString("zipcode");
				item.contact = array.getJSONObject(i).getString("contact");
				list.add(item);
			}
		} catch (JSONException e) {
		}

		return list;
	}

	public Object getData(String doctor_id) {
		List<Object> list = (List<Object> )getData();
		for(Object doctor : list){
			if(doctor_id.equals(((Clinic)doctor).id)){
				return doctor;
			}
		}
		return null;
	}

	@Override
	public String getKey() {
		return "DBClinic";
	}

}
