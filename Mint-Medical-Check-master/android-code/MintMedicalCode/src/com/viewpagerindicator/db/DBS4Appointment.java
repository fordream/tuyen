package com.viewpagerindicator.db;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import vnp.mr.mintmedical.item.S4Item;
import vnp.mr.mintmedical.item.UserLogin;
import android.content.Context;

public class DBS4Appointment extends BaseDB {

	public DBS4Appointment(Context context) {
		super(context);
	}

	@Override
	public Object getData() {
		String getDataStr = String.format("[%s]", getDataStr());
		List<Object> list = new ArrayList<Object>();
		try {
			JSONArray array = new JSONArray(getDataStr);
			for (int i = 0; i < array.length(); i++) {
				S4Item item = new S4Item();
				item.id = array.getJSONObject(i).getString("id");
				item.typeS4AAppointment = array.getJSONObject(i).getString("typeS4AAppointment");
				item.typeS4BVisitType = array.getJSONObject(i).getString("typeS4BVisitType");
				item.typeS4DAvailabilityDoctorEventId = array.getJSONObject(i).getString("typeS4DAvailabilityDoctorEventId");
				item.typeS4CReason = array.getJSONObject(i).getString("typeS4CReason");
				item.isUpcoming  = array.getJSONObject(i).getBoolean("isUpcoming");
				list.add(item);
			}
		} catch (JSONException e) {
		}

		return list;
	}

	@Override
	public String getKey() {
		DBUserLogin dbUserLogin = new DBUserLogin(context);
		UserLogin userLogin = (UserLogin)dbUserLogin.getData();
		return userLogin.custId + "DBS4Appointment";
	}

	public void save(S4Item s4Item) {
		String data = getDataStr();
		if(data.equals("")){
			save(s4Item.toJson());
		}else{
			save(String.format("%s,%s",data,s4Item.toJson()));
		}
	}

}
