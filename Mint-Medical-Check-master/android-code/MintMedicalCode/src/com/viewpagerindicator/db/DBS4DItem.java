package com.viewpagerindicator.db;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import vnp.mr.mintmedical.item.S4DItem;
import android.content.Context;

public class DBS4DItem extends BaseDB {

	public DBS4DItem(Context context) {
		super(context);
	}

	@Override
	public Object getData() {
		List<Object> list = new ArrayList<Object>();
		try {
			JSONArray array = new JSONArray(getDataStr());
			for (int i = 0; i < array.length(); i++) {
				String time = array.getJSONObject(i).getString("time");
				JSONArray array2 = array.getJSONObject(i).getJSONArray("array");
				
				for(int index = 0; index < array2.length() ; index ++){
					S4DItem item = new S4DItem(array2.getJSONObject(index));
					item.time = time;
					list.add(item);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return list;
	}

	@Override
	public String getKey() {
		return "DBS4DItem";
	}
}