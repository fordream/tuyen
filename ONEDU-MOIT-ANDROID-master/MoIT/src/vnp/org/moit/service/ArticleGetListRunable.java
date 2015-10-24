package vnp.org.moit.service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import vnp.org.moit.db.EduAccount;
import vnp.org.moit.db.EduArticleGetList;
import vnp.org.moit.db.ServerStatus;
import vnp.org.moit.service.EduSevice.TypeMore;
import android.content.Context;
import android.os.Bundle;

import com.vnp.core.common.CommonAndroid;
import com.vnp.core.service.asyn.ExecuteAPI;

public class ArticleGetListRunable extends IMRunable {
	private List<String> deletes = new ArrayList<String>();
	String idSchools;

	public ArticleGetListRunable(Context context, String schooldId) {
		super(context, "ArticleGetList", null);
		this.idSchools = schooldId;
	}

	@Override
	public void run() {
		ServerStatus.getIn().set(ServerStatus.LOADMORETINTUC, true);
		String phone = new EduAccount(getContext()).getPhoneActive();
		if (CommonAndroid.isBlank(phone)) {
			return;
		}
		EduArticleGetList eduInbox = new EduArticleGetList(getContext());
		deletes.addAll(eduInbox.getListIds(idSchools));
		loadMoreNewFeed(phone, idSchools, 1);
		ServerStatus.getIn().set(ServerStatus.LOADMORETINTUC, false);
	}

	private void loadMoreNewFeed(String phone, final String schooldId, int page) {
		Bundle extras = createBundle();
		extras.putString("Page", page + "");
		extras.putString("schoolid", schooldId);
		ExecuteAPI executeAPI = execute(extras, "ArticleGetList");

		finishexecute(executeAPI);
		removeFromListDelete(executeAPI.getParseData());
		loadFeedDataRunable(executeAPI.getParseData());
		if (canLoadMore(executeAPI.getParseData())) {
			((EduSevice) getContext()).sendBoardCastForUpdate(TypeMore.ArticleList, schooldId);
			loadMoreNewFeed(phone, schooldId, page + 1);
		} else if ("0".equals(executeAPI.getParseCode())) {
			for (String smsId : deletes) {
				new EduArticleGetList(getContext()).deleteById(schooldId, smsId);
			}
			((EduSevice) getContext()).sendBoardCastForUpdate(TypeMore.ArticleList, schooldId);
			
		}
	}

	private void removeFromListDelete(String parseData) {
		try {
			JSONArray array = new JSONArray(parseData);
			for (int i = 0; i < array.length(); i++) {
				String smsId = CommonAndroid.getString(array.getJSONObject(i), "Id");
				deletes.remove(smsId);
			}
		} catch (Exception e) {
		}
	}

	private void loadFeedDataRunable(String parseData) {
		try {
			JSONArray array = new JSONArray(parseData);
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = array.getJSONObject(i);
				String id = CommonAndroid.getString(object, "Id");
				String linkUrl = CommonAndroid.getString(object, "linkUrl");
				String SchoolId = CommonAndroid.getString(object, "SchoolId");
				new LinkUrlDataRunable(getContext(), id, linkUrl, SchoolId).start();
			}
		} catch (Exception e) {
		}
	}
}