package vnp.org.moit.db;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.acv.cheerz.db.SkypeTable;
import com.vnp.core.common.CommonAndroid;

public class EduArticleGetList extends SkypeTable {

	public EduArticleGetList(Context context) {
		super(context, EduDBProvider.class);
		addColumns("phoneactive");
		addColumns("Id");
		addColumns("SchoolId");
		addColumns("subDes");
		addColumns("mainDes");
		addColumns("imagePath");
		addColumns("likeNumber");
		addColumns("linkUrl");
		addColumns("createTime");
		addColumns("Title");
		addColumns("commentNumber");
		addColumns("linkUrlData");
	}

	public void addNewFeed(String phone, JSONObject object) {
		try {
			object.put("phoneactive", phone);
		} catch (Exception e) {
		}
		ContentValues values = createContentValuesFormJsonObject(object);

		try {

			String Id = object.getString("Id");
			String SchoolId = object.getString("SchoolId");

			String where = String.format("%s = '%s' and %s = '%s' and %s = '%s' ", "phoneactive", phone, "Id", Id, "SchoolId", SchoolId);
			if (has(where)) {
				getContext().getContentResolver().update(getContentUri(), values, where, null);
			} else {
				getContext().getContentResolver().insert(getContentUri(), values);
			}
		} catch (Exception ex) {

		}
	}

	public Cursor query() {
		String phone = new EduAccount(getContext()).getPhoneActive();
		String where = String.format("%s = '%s' ", "phoneactive", phone);
		return querry(where, "createTime DESC");

	}

	public String getLinkUrl(String newFeedId) {
		String linkUrl = null;
		String phone = new EduAccount(getContext()).getPhoneActive();
		Cursor cursor = querry(String.format("Id = '%s'  and %s = '%s'", newFeedId, "phoneactive", phone));
		if (cursor != null) {
			if (cursor.moveToNext()) {
				linkUrl = CommonAndroid.getString(cursor, "linkUrl");
			}
			cursor.close();
		}
		return linkUrl;
	}

	public String getSchoolId(String newFeedId) {
		String SchoolId = null;
		String phone = new EduAccount(getContext()).getPhoneActive();
		Cursor cursor = querry(String.format("Id = '%s'  and %s = '%s'", newFeedId, "phoneactive", phone));
		if (cursor != null) {
			if (cursor.moveToNext()) {
				SchoolId = CommonAndroid.getString(cursor, "SchoolId");
			}
			cursor.close();
		}
		return SchoolId;
	}

	public String getCreateTime(String newFeedId) {
		String createTime = null;
		String phone = new EduAccount(getContext()).getPhoneActive();
		Cursor cursor = querry(String.format("Id = '%s' and %s = '%s'", newFeedId, "phoneactive", phone));
		if (cursor != null) {
			if (cursor.moveToNext()) {
				createTime = CommonAndroid.getString(cursor, "createTime");
			}
			cursor.close();
		}
		return createTime;
	}

	public Object queryBySchoolId(String SchoolId) {
		String phone = new EduAccount(getContext()).getPhoneActive();
		String where = String.format("%s = '%s' ", "phoneactive", phone);

		if (!CommonAndroid.isBlank(SchoolId)) {
			where = where + String.format(" and %s = '%s'", "SchoolId", SchoolId);
		}
		return querry(where, "createTime DESC");
	}

	public String getLinkUrlData(String newFeedId) {
		String phone = new EduAccount(getContext()).getPhoneActive();
		String createTime = null;
		Cursor cursor = querry(String.format("Id = '%s' and %s ='%s'", newFeedId, "phoneactive", phone));
		if (cursor != null) {
			if (cursor.moveToNext()) {
				createTime = CommonAndroid.getString(cursor, "linkUrlData");
			}
			cursor.close();
		}
		return createTime;
	}

	public boolean loaded(String id) {
		return !CommonAndroid.isBlank(getLinkUrlData(id));
	}

	public String getMainDes(String newFeedId) {
		String phone = new EduAccount(getContext()).getPhoneActive();
		String mainDes = null;
		Cursor cursor = querry(String.format("Id = '%s' and %s ='%s'", newFeedId, "phoneactive", phone));
		if (cursor != null) {
			if (cursor.moveToNext()) {
				mainDes = CommonAndroid.getString(cursor, "mainDes");
			}
			cursor.close();
		}
		return mainDes;
	}

	public void deleteAll() {
		String phone = new EduAccount(getContext()).getPhoneActive();
		String where = String.format("%s = '%s' ", "phoneactive", phone);
		delete(where);
	}

	public List<String> getListIds(String idSchool) {
		String phone = new EduAccount(getContext()).getPhoneActive();
		String where = String.format("%s = '%s' and %s = '%s' ", "phoneactive", phone, "SchoolId", idSchool);
		List<String> list = new ArrayList<String>();
		Cursor cursor = querry(where);
		while (cursor != null && cursor.moveToNext()) {
			String smsid = CommonAndroid.getString(cursor, "Id");
			if (!CommonAndroid.isBlank(smsid) && !list.contains(smsid)) {
				list.add(smsid);
			}
		}

		if (cursor != null)
			cursor.close();
		return list;
	}

	public void deleteById(String schooldId, String smsId) {
		String phone = new EduAccount(getContext()).getPhoneActive();
		String where = String.format("%s = '%s' and %s = '%s'  and %s = '%s' ", "phoneactive", phone, "SchoolId", schooldId, "Id", smsId);
		delete(where);
	}
}