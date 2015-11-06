package com.viewpagerindicator.db;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import vnp.mr.mintmedical.item.Clinic;
import vnp.mr.mintmedical.item.Doctor;
import vnp.mr.mintmedical.item.VisitType;
import android.content.Context;

public class DBConfig extends BaseDB {

	public DBConfig(Context context) {
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
		List<Object> list = (List<Object>) getData();
		for (Object doctor : list) {
			if (doctor_id.equals(((Clinic) doctor).id)) {
				return doctor;
			}
		}
		return null;
	}

	@Override
	public String getKey() {
		return "DBConfig";
	}

	public List<VisitType> getVisitTypeList() {
		List<VisitType> list = new ArrayList<VisitType>();
		try {
			JSONArray jsonArray = new JSONObject(getDataStr())
					.getJSONArray("visit_type");
			for (int i = 0; i < jsonArray.length(); i++) {
				VisitType visitType = new VisitType(jsonArray.getJSONObject(i));
				list.add(visitType);
			}
		} catch (JSONException e) {
		}
		return list;
	}

	public String getRegisterUrl() {
		try {
			return new JSONObject(getDataStr()).getString("register_link");
		} catch (JSONException e) {
		}
		return null;
	}

	public String getForgotUrl() {
		try {
			return new JSONObject(getDataStr()).getString("forgot_link");
		} catch (JSONException e) {
		}
		return null;
	}

	public VisitType getVisitType(String id) {
		List<VisitType> list = getVisitTypeList();

		for (VisitType visitType : list) {
			if (id.equals(visitType.key)) {
				return visitType;
			}
		}
		return null;
	}
}
