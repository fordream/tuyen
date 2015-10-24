package vnp.org.moit.db;

import org.json.JSONObject;

import vnp.org.moit.utils.MOITUtils;
import android.content.Context;
import android.database.Cursor;

import com.acv.cheerz.db.SkypeTable;
import com.vnp.core.common.CommonAndroid;

public class EduPersion extends SkypeTable {

	public EduPersion(Context context) {
		super(context, EduDBProvider.class);
		addColumns("phone");
		addColumns("name");
		addColumns("avatar");
	}

	public String getAvatar(String phone) {
		String avatar = "";
		Cursor cursor = querry(String.format("%s = '%s'", "phone", phone));
		if (cursor != null) {
			if (cursor.moveToNext()) {
				avatar = CommonAndroid.getString(cursor, "avatar");
			}
			cursor.close();
		}

		return avatar;
	}

	@Override
	public void add(JSONObject object) {
		try {
			String phone = MOITUtils.convertPhone(CommonAndroid.getString(object, "phone"));
			String name = CommonAndroid.getString(object, "name");
			String avatar = CommonAndroid.getString(object, "avatar");

			if (CommonAndroid.isBlank(avatar)) {
				object.remove("avatar");
			}

			if (CommonAndroid.isBlank(name)) {
				object.remove("name");
			}

			String where = String.format("%s ='%s'", "phone", phone);

			if (has(where)) {
				update(createContentValuesFormJsonObject(object), where);
			} else {
				super.add(object);
			}
		} catch (Exception ex) {

		}
	}

	public void add(String phone, String name, String avatar) {
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("phone", phone);
			jsonObject.put("name", name);
			jsonObject.put("avatar", avatar);
			add(jsonObject);
		} catch (Exception ex) {
		}
	}
}