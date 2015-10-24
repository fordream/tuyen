package vnp.org.moit.service;

import org.json.JSONArray;

import vnp.org.moit.db.EduAccount;
import vnp.org.moit.service.EduSevice.TypeMore;
import vnp.org.moit.utils.MOITUtils;
import one.edu.vn.sms.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.vnp.core.common.CommonAndroid;
import com.vnp.core.gcm.GcmBroadcastReceiver;
import com.vnp.core.service.asyn.ExecuteAPI;
import com.vnp.core.service.asyn.VNPServiceAsynTask;

public class IMRunable extends VNPServiceAsynTask {
	private String phone;

	public IMRunable(Context context, String api, String phone) {
		super(context, api);
		this.phone = phone;
	}

	public String getServiceUrl() {
		return getContext().getString(R.string.service_url);
	}

	public Bundle createBundle() {
		Bundle extras = super.createBundle();

		String registerId = GcmBroadcastReceiver.registerReturnRegisterId(getContext());
		String deviceId = GcmBroadcastReceiver.getDeviceId(getContext());

		if (MOITUtils.ISFacebooktest) {
			registerId = 1 + "";
			deviceId = 1 + "";
		}
		extras.putString("deviceId", registerId);
		extras.putString("OSID", deviceId);

		String name = new EduAccount(getContext()).getNameActive();
		extras.putString("ownername", CommonAndroid.isBlank(name) ? "noname" : name);

		phone = CommonAndroid.isBlank(phone) ? new EduAccount(getContext()).getPhoneActive() : phone;
		extras.putString("phone", phone);

		extras.putString("RecordsPerPage", SynEduSevice.RecordsPerPage + "");
		extras.putString("deviceType", "Android");

		return extras;
	}

	private String parseCode, parseData, parseDescription;

	public String getParseCode() {
		return parseCode;
	}

	public void setParseCode(String parseCode) {
		this.parseCode = parseCode;
	}

	public String getParseData() {
		return parseData;
	}

	public void setParseData(String parseData) {
		this.parseData = parseData;
	}

	public String getParseDescription() {
		return parseDescription;
	}

	public void setParseDescription(String parseDescription) {
		this.parseDescription = parseDescription;
	}

	public boolean isSuccess(ExecuteAPI exception) {
		return "0".equals(exception.getParseCode());
	}

	final public void finishexecute(ExecuteAPI execute) {
		String api = execute.getApi();
		String parseCode = execute.getParseCode();
		String parseData = execute.getParseData();
		String parseDescription = execute.getParseDescription();
		int responseCode = execute.getClient().getResponseCode();
		String responseMessage = execute.getClient().getErrorMessage();
		String response = execute.getClient().getResponse();
		Bundle extras = execute.getExtras();
		SynEduSevice synEduSevice = new SynEduSevice(getContext(), api, false);
		synEduSevice.onSucssesOnBackground(parseCode, parseDescription, parseData, responseCode, responseMessage, response, extras);
	}

	public void run(ExecuteAPI execute) {

	}

	public boolean canLoadMore(String parseData) {
		boolean needLoadMore = false;
		try {
			if (new JSONArray(parseData).length() == SynEduSevice.RecordsPerPage) {
				needLoadMore = true;
			}
		} catch (Exception exception) {

		}

		return needLoadMore;
	}

	public void sendBoardCastForUpdate(TypeMore typeMore, String id) {
		Intent intent = new Intent("vnp.org.moit.updateui");
		intent.putExtra("TypeMore", typeMore);
		intent.putExtra("id", id);
		intent.putExtra("title", id);
		getContext().sendBroadcast(intent);
	}
}