package vnp.mr.mintmedical.item;

import org.json.JSONObject;

public class S4DItem {
	public String time;
	public String id, title, doctor_id, start, end, start_tick, end_tick,
			status, appointment_type, visit_type, url;
	public String start_date, start_time, fullname, doctor_level, office_id,
			latitude, longitude, office_address,avatar;

	public S4DItem(JSONObject jsonObject) {
		try {
			id = jsonObject.getString("id");
			title = jsonObject.getString("title");
			doctor_id = jsonObject.getString("doctor_id");
			start = jsonObject.getString("start");
			end = jsonObject.getString("end");
			start_tick = jsonObject.getString("start_tick");
			end_tick = jsonObject.getString("end_tick");
			status = jsonObject.getString("status");
			appointment_type = jsonObject.getString("appointment_type");
			visit_type = jsonObject.getString("visit_type");
			url = jsonObject.getString("url");
			start_date = jsonObject.getString("start_date");
			start_time = jsonObject.getString("start_time");
			fullname = jsonObject.getString("fullname");
			doctor_level = jsonObject.getString("doctor_level");
			office_id = jsonObject.getString("office_id");
			latitude = jsonObject.getString("latitude");
			longitude = jsonObject.getString("longitude");
			office_address = jsonObject.getString("office_address");
			avatar = jsonObject.getString("avatar");
		} catch (Exception exception) {
			

		}

	}
}
