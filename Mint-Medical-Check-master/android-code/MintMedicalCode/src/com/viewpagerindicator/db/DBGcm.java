package com.viewpagerindicator.db;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import vnp.mr.mintmedical.item.Clinic;
import vnp.mr.mintmedical.item.Doctor;
import vnp.mr.mintmedical.item.Gcm;
import android.content.Context;
import android.util.Log;

public class DBGcm extends BaseDB {

	public DBGcm(Context context) {
		super(context);
	}

	@Override
	public void save(String data) {
		String oldData = getDataStr();
		
		if("".equals(oldData)){
			oldData = data;
		}else{
			oldData = data + "," + oldData;
		}
		super.save(oldData);
	}

	@Override
	public Object getData() {
		List<Object> list = new ArrayList<Object>();
		Log.e("AAAAAAAAAA", getDataStr());
		try {
			JSONArray array = new JSONArray("["+getDataStr()+"]");
			for (int i = 0; i < array.length(); i++) {
				Gcm gcm = new Gcm();
				gcm.message = array.getJSONObject(i).getString("message");
				gcm.name = array.getJSONObject(i).getString("name");
				//gcm.type = array.getJSONObject(i).getString("type");
				gcm.time = array.getJSONObject(i).getString("time");
				
				list.add(gcm);
			}
		} catch (JSONException e) {
		}

		return list;
	}


	@Override
	public String getKey() {
		return "DBGcm";
	}

}
