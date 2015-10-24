package vnp.org.moit.service;

import one.edu.vn.sms.R;
import vnp.org.moit.dialog.EduProgressDialog;
import vnp.org.moit.dialog.ThongBaoDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.vnp.core.common.CommonAndroid;
import com.vnp.core.service.asyn.ExecuteAPI;

public class MarKImageUploadRunable extends IMRunable {
	private String base64;
	private String url;
	private Dialog dialog;

	public MarKImageUploadRunable(Context context, String base64) {
		super(context, "MarKImageUpload", null);
		this.base64 = base64;
		dialog = new EduProgressDialog(getContext());
		dialog.show();
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		dialog.dismiss();
		if (!CommonAndroid.isBlank(url)) {
			String urlx = "https://www.facebook.com/sharer/sharer.php?u=%s";
			urlx = String.format(urlx, url);
			CommonAndroid.callWeb(getContext(), urlx);
		} else {
			new ThongBaoDialog(getContext(), getContext().getString(R.string.thuchienkhongthanhcong), null, getContext().getString(R.string.ok)).show();
		}

	}

	@Override
	public void run() {
		Bundle extras = createBundle();
		extras.putString("imgBase64", base64);
		
		ExecuteAPI exception = execute(extras, "MarKImageUpload");
		finishexecute(exception);
		url = exception.getParseData();
	}
}