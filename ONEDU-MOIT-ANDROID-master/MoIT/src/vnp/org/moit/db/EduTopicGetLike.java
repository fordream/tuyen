package vnp.org.moit.db;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.acv.cheerz.db.SkypeTable;
import com.vnp.core.common.CommonAndroid;

public class EduTopicGetLike extends SkypeTable {

	public EduTopicGetLike(Context context) {
		super(context, EduDBProvider.class);
		addColumns("TopicId");
		addColumns("Phone");
	}

	@Override
	public void add(JSONObject object) {
		String TopicId = CommonAndroid.getString(object, "TopicId");
		String Phone = CommonAndroid.getString(object, "Phone");
		String where = String.format("TopicId ='%s' and Phone = '%s'", TopicId, Phone);
		if (has(where)) {
			update(createContentValuesFormJsonObject(object), where);
		} else {
			super.add(object);
		}
	}

	public void add(JSONArray jsonArray, String topicId) {
		String where = String.format("TopicId = '%s'", topicId);
		delete(where);
		add(jsonArray);
	}

	public boolean isLike(String TopicId) {
		String phone = new EduAccount(getContext()).getPhoneActive();
		String where = String.format("TopicId ='%s' and Phone = '%s'", TopicId, phone);
		return has(where);
	}
}