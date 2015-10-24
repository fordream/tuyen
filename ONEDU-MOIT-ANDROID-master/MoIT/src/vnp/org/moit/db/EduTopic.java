package vnp.org.moit.db;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.acv.cheerz.db.SkypeTable;
import com.vnp.core.common.CommonAndroid;

public class EduTopic extends SkypeTable {

	public EduTopic(Context context) {
		super(context, EduDBProvider.class);
		addColumns("phoneactive");

		addColumns("ID");
		addColumns("SchoolID");
		addColumns("Title");
		addColumns("Des");
		addColumns("OwnerName");
		addColumns("phone");
		addColumns("createTime");
		addColumns("likeNumber");
		addColumns("commentNumber");
		addColumns("MainImage");

		// add column image
		addColumns("img1");
		addColumns("img2");
		addColumns("img3");
		addColumns("img4");
		addColumns("img5");
		addColumns("img6");
	}

	public void addTopic(JSONObject object) {
		String phone = new EduAccount(getContext()).getPhoneActive();
		try {
			object.put("phoneactive", phone);
		} catch (Exception e) {
		}

		ContentValues values = createContentValuesFormJsonObject(object);
		try {

			String Id = object.getString("ID");
			String SchoolId = object.getString("SchoolID");

			String where = String.format("%s = '%s' and %s = '%s' and %s = '%s' ", "phoneactive", phone, "Id", Id, "SchoolId", SchoolId);
			if (has(where)) {
				getContext().getContentResolver().update(getContentUri(), values, where, null);
			} else {
				getContext().getContentResolver().insert(getContentUri(), values);
			}
		} catch (Exception ex) {

		}
	}

	public Cursor queryIdSchool(String idSchool) {
		String phone = new EduAccount(getContext()).getPhoneActive();
		String where = String.format("%s = '%s' ", "phoneactive", phone);

		if (!CommonAndroid.isBlank(idSchool)) {
			where = String.format("%s = '%s' and %s ='%s' ", "phoneactive", phone, "SchoolID", idSchool);
		}
		return querry(where, "createTime DESC");
	}

	public Cursor queryByTopicId(String topicId) {
		String where = String.format("%s = '%s' ", "ID", topicId);
		return querry(where, "createTime DESC");
	}

	public List<String> getListIds(String idSchool) {
		String phone = new EduAccount(getContext()).getPhoneActive();
		String where = String.format("%s = '%s' and %s = '%s' ", "phoneactive", phone, "SchoolID", idSchool);
		List<String> list = new ArrayList<String>();
		Cursor cursor = querry(where);
		while (cursor != null && cursor.moveToNext()) {
			String smsid = CommonAndroid.getString(cursor, "ID");
			if (!CommonAndroid.isBlank(smsid) && !list.contains(smsid)) {
				list.add(smsid);
			}
		}

		if (cursor != null)
			cursor.close();
		return list;
	}

	public void deleteBySmsId(String schoolId, String smsId) {
		String phone = new EduAccount(getContext()).getPhoneActive();
		String where = String.format("%s = '%s' and %s = '%s'  and %s = '%s' ", "phoneactive", phone, "SchoolID", schoolId, "ID", smsId);
		delete(where);
	}
}