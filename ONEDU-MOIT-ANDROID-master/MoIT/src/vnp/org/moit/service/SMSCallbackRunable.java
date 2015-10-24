package vnp.org.moit.service;

import vnp.org.moit.db.EduAccount;
import vnp.org.moit.utils.MOITUtils;
import android.content.Context;
import android.os.Bundle;

import com.vnp.core.common.CommonAndroid;
import com.vnp.core.service.asyn.ExecuteAPI;

public class SMSCallbackRunable extends IMRunable {
	private String smsid;
	private String timeReceived;

	public SMSCallbackRunable(Context context, String smsid, String timeReceived) {
		super(context, "SMSCallback", null);
		this.smsid = smsid;
		this.timeReceived = timeReceived;
	}

	@Override
	public void run() {
		String phone = new EduAccount(getContext()).getPhoneActive();
		if (CommonAndroid.isBlank(phone)) {
			return;
		}

		Bundle extras = createBundle();
		extras.putString("smsid", smsid);

		timeReceived = MOITUtils.getTimeyyyMMddhhmmss();
		extras.putString("timeReceived", timeReceived);
		ExecuteAPI executeAPI = execute(extras, "SMSCallback");
		finishexecute(executeAPI);
	}
}