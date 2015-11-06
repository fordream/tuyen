package com.viewpagerindicator.db;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import com.vnp.core.common.LogUtils;

import vnp.mr.mintmedical.item.Doctor;
import android.content.Context;
import android.util.Log;

public class DBDoctor extends BaseDB {

	public DBDoctor(Context context) {
		super(context);
	}

	@Override
	public Object getData() {
		
		List<Object> list = new ArrayList<Object>();
		try {
			JSONArray array = new JSONArray(getDataStr());
			for (int i = 0; i < array.length(); i++) {
				Doctor item = new Doctor();
				item.id = array.getJSONObject(i).getString("id");
				item.name = array.getJSONObject(i).getString("name");
				item.email = array.getJSONObject(i).getString("email");
				item.block = array.getJSONObject(i).getString("block");
				item.avatar = array.getJSONObject(i).getString("avatar");
				
				item.clinic_id = array.getJSONObject(i).getString("clinic_id");
				item.contact = array.getJSONObject(i).getString("contact");
				
				item.theme = array.getJSONObject(i).getString("theme");
				item.level = array.getJSONObject(i).getString("level");
				//item.username = array.getJSONObject(i).getString("username");
				
				
				
				
				list.add(item);
			}
		} catch (JSONException e) {
		}

		return list;
	}

	@Override
	public String getKey() {
		return "DBDoctor";
	}

	public Object getData(String doctor_id) {
		List<Object> list = (List<Object> )getData();
		for(Object doctor : list){
			if(doctor_id.equals(((Doctor)doctor).id)){
				return doctor;
			}
		}
		return null;
	}

}
