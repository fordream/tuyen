package vnp.org.moit.utils;

import org.json.JSONObject;

import android.content.Context;

public class ParseUtils {

	public void execute(String str, Context context) {
		try {
			JSONObject json = new JSONObject(str);
			String code = json.getString("code");
			String description = json.getString("description");
			String data = json.getString("data");
			onParseSuccess(code, description, data);
		} catch (Exception e) {
			onErrorParseJson();
		}
	}

	public void onParseSuccess(String code, String description, String data) {

	}

	public void onErrorParseJson() {

	}
}
