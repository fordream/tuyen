package vnp.org.moit.db;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.acv.cheerz.db.SkypeTable;
import com.vnp.core.common.CommonAndroid;

public class EduArticleGetLike extends SkypeTable {

	public EduArticleGetLike(Context context) {
		super(context, EduDBProvider.class);
		addColumns("ArticleId");
		addColumns("Phone");
	}

	public void add(JSONArray jsonArray, String ArticleId) {
		String where = String.format("ArticleId = '%s'", ArticleId);
		delete(where);
		add(jsonArray);
	}

	@Override
	public void add(JSONObject object) {
		String TopicId = CommonAndroid.getString(object, "ArticleId");
		String Phone = CommonAndroid.getString(object, "Phone");
		String where = String.format("ArticleId ='%s' and Phone = '%s'", TopicId, Phone);
		if (has(where)) {
			update(createContentValuesFormJsonObject(object), where);
		} else {
			super.add(object);
		}
	}

	public boolean isLike(String ArticleId) {
		String phone = new EduAccount(getContext()).getPhoneActive();
		String where = String.format("ArticleId ='%s' and Phone = '%s'", ArticleId, phone);
		return has(where);
	}

}