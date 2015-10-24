package vnp.org.moit.service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import vnp.org.moit.db.EduAccount;
import vnp.org.moit.db.EduInbox;
import vnp.org.moit.db.ServerStatus;
import vnp.org.moit.service.EduSevice.TypeMore;
import android.content.Context;
import android.os.Bundle;

import com.vnp.core.common.CommonAndroid;
import com.vnp.core.service.asyn.ExecuteAPI;

public class SMSGetListRunable extends IMRunable {

	public SMSGetListRunable(Context context) {
		super(context, "SMSGetList", null);

	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
	}

	private List<String> deletes = new ArrayList<String>();

	@Override
	public void run() {
		ServerStatus.getIn().set(ServerStatus.LOADMORESMS, true);
		String phone = new EduAccount(getContext()).getPhoneActive();
		if (CommonAndroid.isBlank(phone)) {
			return;
		}

		EduInbox eduInbox = new EduInbox(getContext());
		deletes.addAll(eduInbox.getListIds());
		loadMoreInbox(phone, 1);
		
		ServerStatus.getIn().set(ServerStatus.LOADMORESMS, false);
	}

	private void loadMoreInbox(String phone, int page) {
		Bundle extras = createBundle();
		extras.putString("Page", page + "");

		ExecuteAPI executeAPI = execute(extras, "SMSGetList");
		removeFromListDelete(executeAPI.getParseData());
		finishexecute(executeAPI);
		if (canLoadMore(executeAPI.getParseData())) {
			((EduSevice) getContext()).sendBoardCastForUpdate(TypeMore.SMSList, null);
			loadMoreInbox(phone, page + 1);
		} else if ("0".equals(executeAPI.getParseCode())) {
			for (String smsId : deletes) {
				new EduInbox(getContext()).deleteBySmsId(smsId);
			}
			((EduSevice) getContext()).sendBoardCastForUpdate(TypeMore.SMSList, null);
		}

	}

	private void removeFromListDelete(String parseData) {
		try {
			JSONArray array = new JSONArray(parseData);
			for (int i = 0; i < array.length(); i++) {
				String smsId = CommonAndroid.getString(array.getJSONObject(i), "smsid");
				deletes.remove(smsId);
			}
		} catch (Exception e) {
		}
	}
}