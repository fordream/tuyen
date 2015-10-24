package org.com.atmarkcafe.sky;

import android.content.Context;
import android.provider.Settings.Secure;

import com.acv.cheerz.db.DataStore;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.SaveCallback;

public class ParseNotificationUtils {
	public static String Parse_app_key = "uhTxfR1xw1cr2RsaiI2cS3mRFlokz9CwM9etkTiC";
	public static String Parse_client_key = "GjQncXkk4Lt0IosrSSxJy4cezgu6YSM1qoKoWl5V";

	public void init(final Context context) {
		onStartRegister();
		Parse.initialize(context, Parse_app_key, Parse_client_key);
		ParseInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {
				DataStore.getInstance().init(context);

				String android_id = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
				android_id = ParseInstallation.getCurrentInstallation().getInstallationId();
				if (e == null) {
					DataStore.getInstance().save("registerparse", true);
					onRegisterSuccess();
				} else {
					e.printStackTrace();
					onResgisterFail();
				}
			}
		});
	}

	public void onStartRegister() {

	}

	public void onRegisterSuccess() {

	}

	public void onResgisterFail() {

	}

	public boolean isRegister(Context context) {
		DataStore.getInstance().init(context);
		return DataStore.getInstance().get("registerparse", false);
	}

	public String getInstallationId() {
		return ParseInstallation.getCurrentInstallation().getInstallationId();
	}
}