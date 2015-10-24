package vnp.org.moit.service;

import one.edu.vn.sms.R;
import vnp.org.moit.dialog.EduProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.vnp.core.common.CommonAndroid;
import com.vnp.core.service.RestClient;
import com.vnp.core.service.asyn.ExecuteAPI;

public class LoginFaceRunable extends IMRunable {
	private String snUsername;
	private String snInfo;
	private String facebookId;
	private String imgBase64;
	private EduProgressDialog progressDialog;
	private boolean isSuccess = false;
	private String message = "";

	public String getMessage() {
		return message;
	}

	public LoginFaceRunable(Context context, String snUsername, final String snInfo, final String facebookId) {
		super(context, "LoginFaceBook", null);
		this.snUsername = snUsername;
		this.snInfo = snInfo;
		this.facebookId = facebookId;

		progressDialog = new EduProgressDialog(context);
		progressDialog.show();
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		progressDialog.dismiss();
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	@Override
	public void run() {
		Bitmap avatar = new RestClient(null).executeLoadAvatarFacebook(facebookId);
		avatar = CommonAndroid.scaleBitmap(avatar, 90);
		imgBase64 = CommonAndroid.bitmapToBase64(avatar);

		Bundle extras = createBundle();
		extras.putString("snType", "fb");
		extras.putString("snInfo", snInfo);
		extras.putString("snUsername", snUsername);
		ExecuteAPI exception = execute(extras, "LoginFaceBook");
		finishexecute(exception);
		if (!"0".equals(exception.getParseCode())) {
			message = exception.getParseDescription();

			if (CommonAndroid.isBlank(message))
				message = getContext().getString(R.string.error_network);
			return;
		}

		isSuccess = true;

		Bundle extrasLogin = createBundle();
		extrasLogin.putString("name", snUsername);
		extrasLogin.putString("avatarBase64", imgBase64);
		ExecuteAPI executeAPI = execute(extrasLogin, "UpdateUser");
		finishexecute(executeAPI);
		/**
		 * finish
		 */
	}
}
