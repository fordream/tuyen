package vnp.org.moit.db;

import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;

import com.acv.cheerz.db.SkypeTable;

public class EduAccount extends SkypeTable {

	public EduAccount(Context context) {
		super(context, EduDBProvider.class);
		addColumns("sninfo");
		addColumns("phone");
		addColumns("name");
		addColumns("avatar");
		addColumns("location");
		addColumns("birthday");
		addColumns("email");
		addColumns("facebookid");
		addColumns("active");// 1 active 0 deactive current time
		addColumns("verifycode"); // 1 active 0 deactive
	}

	public boolean getverifycode() {
		String where = String.format("%s ='%s'", "active", "1");
		Cursor cursor = querry(where);
		String verifycode = "";
		if (cursor != null) {
			if (cursor.moveToNext())
				verifycode = cursor.getString(cursor.getColumnIndex("verifycode"));
			cursor.close();
		}
		return "1".equals(verifycode);
	}

	public String getPhoneActive() {
		String where = String.format("%s ='%s'", "active", "1");
		Cursor cursor = querry(where);
		String phone = "";
		if (cursor != null) {
			if (cursor.moveToNext())
				phone = cursor.getString(cursor.getColumnIndex("phone"));
			cursor.close();
		}
		return phone;
	}

	public String getNameActive() {
		String where = String.format("%s ='%s'", "active", "1");
		Cursor cursor = querry(where);
		String phone = "";
		if (cursor != null) {
			if (cursor.moveToNext())
				phone = cursor.getString(cursor.getColumnIndex("name"));
			cursor.close();
		}
		return phone;
	}

	@Override
	public void add(JSONObject object) {

		try {
			String phone = object.getString("phone");
			String where = String.format("%s ='%s'", "phone", phone);

			if (has(where)) {
				update(createContentValuesFormJsonObject(object), where);
			} else {
				super.add(object);
			}
		} catch (Exception ex) {

		}

	}
}