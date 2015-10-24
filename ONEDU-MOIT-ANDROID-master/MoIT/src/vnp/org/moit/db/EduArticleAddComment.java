package vnp.org.moit.db;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;

import com.acv.cheerz.db.SkypeTable;
import com.vnp.core.common.CommonAndroid;

public class EduArticleAddComment extends SkypeTable {

	public EduArticleAddComment(Context context) {
		super(context, EduDBProvider.class);
		addColumns("ID");
		addColumns("ArticelId");
		addColumns("Phone");
		addColumns("Des");
		addColumns("createTime");
		addColumns("avatar");
		addColumns("ownnerName");
		addColumns("OwnerName");
	}

	public void add(boolean isAdd, String parseData) {
		try {
			if (isAdd) {
				JSONObject jsonObject = new JSONObject(parseData);
				String ID = CommonAndroid.getString(jsonObject, "ID");
				String ArticelId = CommonAndroid.getString(jsonObject, "ArticelId");
				String where = String.format("%s = '%s' and %s = '%s'", "ID", ID, "ArticelId", ArticelId);
				ContentValues values = createContentValuesFormJsonString(parseData);
				if (has(where)) {
					update(values, where);
				} else {
					insert(values);
				}
			} else {
				JSONArray array = new JSONArray(parseData);
				for (int i = 0; i < array.length(); i++) {
					JSONObject jsonObject = array.getJSONObject(i);
					String ID = CommonAndroid.getString(jsonObject, "ID");
					String ArticelId = CommonAndroid.getString(jsonObject, "ArticelId");
					String where = String.format("%s = '%s' and %s = '%s'", "ID", ID, "ArticelId", ArticelId);
					ContentValues values = createContentValuesFormJsonObject(jsonObject);
					if (has(where)) {
						update(values, where);
					} else {
						insert(values);
					}
				}
			}
		} catch (Exception exception) {
		}
	}
}