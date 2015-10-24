package vnp.org.moit.service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import vnp.org.moit.db.EduAccount;
import vnp.org.moit.db.EduInbox;
import vnp.org.moit.db.EduTopic;
import vnp.org.moit.db.EduTopicImage;
import vnp.org.moit.db.ServerStatus;
import vnp.org.moit.service.EduSevice.TypeMore;
import android.content.Context;
import android.os.Bundle;

import com.vnp.core.common.CommonAndroid;
import com.vnp.core.service.asyn.ExecuteAPI;

public class TopicGetListRunable extends IMRunable {
	private String schoolId;

	public TopicGetListRunable(Context context, String schoolId) {
		super(context, "TopicGetList", null);
		this.schoolId = schoolId;

	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
	}

	private List<String> deletes = new ArrayList<String>();

	@Override
	public void run() {
		ServerStatus.getIn().set(ServerStatus.LOADMOREDIENDAN, true);
		String phone = new EduAccount(getContext()).getPhoneActive();
		if (CommonAndroid.isBlank(phone)) {
			return;
		}

		EduTopic eduInbox = new EduTopic(getContext());
		deletes.addAll(eduInbox.getListIds(schoolId));
		loadMoreInbox(phone, 1);
		ServerStatus.getIn().set(ServerStatus.LOADMOREDIENDAN, false);
	}

	private void loadMoreInbox(String phone, int page) {
		Bundle extras = createBundle();
		extras.putString("Page", page + "");
		extras.putString("schoolid", schoolId);
		ExecuteAPI executeAPI = execute(extras, "TopicGetList");
		removeFromListDelete(executeAPI.getParseData());
		finishexecute(executeAPI);
		LoadFeedDataRunable(executeAPI.getParseData());
		if (canLoadMore(executeAPI.getParseData())) {
			((EduSevice) getContext()).sendBoardCastForUpdate(TypeMore.TopicList, schoolId);
			loadMoreInbox(phone, page + 1);
		} else if ("0".equals(executeAPI.getParseCode())) {
			for (String smsId : deletes) {
				new EduTopic(getContext()).deleteBySmsId(schoolId, smsId);
			}
			((EduSevice) getContext()).sendBoardCastForUpdate(TypeMore.TopicList, schoolId);
		}

	}

	private void LoadFeedDataRunable(String parseData) {
		try {
			JSONArray array = new JSONArray(parseData);
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = array.getJSONObject(i);
				String topicId = CommonAndroid.getString(object, "ID");

				if (!CommonAndroid.isBlank(topicId) && !new EduTopicImage(getContext()).loaded(topicId)) {
					TopicGetImagesRunable topicGetImagesRunable = new TopicGetImagesRunable(getContext(), topicId);
					topicGetImagesRunable.start();

				}

				if (!CommonAndroid.isBlank(topicId)) {
					// loadmoreNewFeedOrTopicComment(false, phone, topicId, 1);
				}
			}
		} catch (Exception e) {
		}
	}

	private void removeFromListDelete(String parseData) {
		try {
			JSONArray array = new JSONArray(parseData);
			for (int i = 0; i < array.length(); i++) {
				String smsId = CommonAndroid.getString(array.getJSONObject(i), "ID");
				deletes.remove(smsId);
			}
		} catch (Exception e) {
		}
	}
}