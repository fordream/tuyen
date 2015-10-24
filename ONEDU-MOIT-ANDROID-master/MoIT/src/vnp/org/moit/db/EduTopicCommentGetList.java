package vnp.org.moit.db;

import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.acv.cheerz.db.SkypeTable;

public class EduTopicCommentGetList extends SkypeTable {

	public EduTopicCommentGetList(Context context) {
		super(context, EduDBProvider.class);
		addColumns("ID");
		addColumns("TopicID");
		addColumns("Phone");
		addColumns("Des");
		addColumns("OwnerName");
		addColumns("createTime");
		addColumns("avatar");
	}

	public Cursor queryByTopicId(String TopicID) {
		String where = String.format("%s = '%s' ", "TopicID", TopicID);
		return querry(where,"createTime DESC");
	}

	public void addEduTopicCommentGetList(JSONObject object) {
//		ContentValues values = new ContentValues();
		try {
//			Set<String> colums = getColumns();
//			for (String column : colums) {
//				if (object.has(column))
//					values.put(column, object.getString(column));
//			}
			ContentValues values = createContentValuesFormJsonObject(object);
			String Id = object.getString("ID");
			String TopicID = object.getString("TopicID");
			String where = String.format("%s = '%s' and %s = '%s' ", "Id", Id, "TopicID", TopicID);
			if (has(where)) {
				getContext().getContentResolver().update(getContentUri(), values, where, null);
			} else {
				getContext().getContentResolver().insert(getContentUri(), values);
			}
		} catch (Exception ex) {
		}
	}
}