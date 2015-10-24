package vnp.org.moit.service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import vnp.org.moit.db.EduAccount;
import vnp.org.moit.db.EduMarkTableGet;
import vnp.org.moit.db.ServerStatus;
import vnp.org.moit.service.EduSevice.TypeMore;
import android.content.Context;
import android.os.Bundle;

import com.vnp.core.common.CommonAndroid;
import com.vnp.core.common.LogUtils;
import com.vnp.core.service.asyn.ExecuteAPI;

public class MarkTableGetRunable extends IMRunable {
	private List<String> studentids = new ArrayList<String>();

	public MarkTableGetRunable(Context context, List<String> studentid) {
		super(context, "MarkTableGet", null);
		this.studentids.addAll(studentid);// = studentid;

	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
	}

	@Override
	public void run() {

		String phone = new EduAccount(getContext()).getPhoneActive();
		if (CommonAndroid.isBlank(phone)) {
			return;
		}
		ServerStatus.getIn().init(getContext());
		ServerStatus.getIn().set(ServerStatus.LOADMOREMarkTableGet, true);

		for (String studentid : studentids) {

			loadMoreEMarkTableGet(studentid);
		}
		ServerStatus.getIn().set(ServerStatus.LOADMOREMarkTableGet, false);
		((EduSevice) getContext()).sendBoardCastForUpdate(TypeMore.LOADMOREMarkTableGet, null);
	}

	private void loadMoreEMarkTableGet(String studentid) {
		List<String> deletes = new ArrayList<String>();
		EduMarkTableGet eduInbox = new EduMarkTableGet(getContext());
		deletes.addAll(eduInbox.getListNamHocs(studentid));

		Bundle extras = createBundle();
		extras.putString("studentid", studentid);
		ExecuteAPI executeAPI = execute(extras, "MarkTableGet");
		removeFromListDelete(deletes, executeAPI.getParseData());
		finishexecute(executeAPI);
//		LogUtils.e("Checked : studentid", studentid + ":" + executeAPI.getParseData());
		for (String smsId : deletes) {
			// new EduMarkTableGet(getContext()).deleteBySmsId(studentid,
			// smsId);
		}

		((EduSevice) getContext()).sendBoardCastForUpdate(TypeMore.LOADMOREMarkTableGet, null);
	}

	private void removeFromListDelete(List<String> deletes, String parseData) {
		try {
			JSONArray array = new JSONArray(parseData);
			for (int i = 0; i < array.length(); i++) {
				// FIXME
				String smsId = CommonAndroid.getString(array.getJSONObject(i), "id");
				deletes.remove(smsId);
			}
		} catch (Exception e) {
		}
	}
}