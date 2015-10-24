package one.edu.vn.sms;

import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import vnp.org.moit.db.EduInbox;
import vnp.org.moit.service.EduSevice;
import vnp.org.moit.service.SMSCallbackRunable;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.vnp.core.common.CommonAndroid;
import com.vnp.core.common.LogUtils;

//vnp.org.moit.GcmBroadcastReceiver
public class GcmBroadcastReceiver extends com.vnp.core.gcm.GcmBroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent != null && intent.getExtras() != null) {
			GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
			String messageType = gcm.getMessageType(intent);
			String message = intent.getExtras().getString("message");

			Bundle extras = intent.getExtras();
			String ccontent = extras.getString("content");
			String title = extras.getString("gcm.notification.title");
			String text = extras.getString("gcm.notification.text");
			String icon = extras.getString("gcm.notification.icon");
			String content = extras.getString("content");

			EduInbox eduInbox = new EduInbox(context);
			try {
				eduInbox.add(new JSONArray(content));
			} catch (JSONException e1) {
			}
			Set<String> keys = extras.keySet();
			for (String key : keys) {
				LogUtils.e("AAAAAAAAAAAAA", key +":" +extras.getShort(key));
			}

			try {
				JSONArray array = new JSONArray(content);
				for (int i = 0; i < array.length(); i++) {
					JSONObject objwct = array.getJSONObject(i);
					new EduInbox(context).add(objwct);

					String smsid = CommonAndroid.getString(objwct, "smsid");

					new SMSCallbackRunable(context, smsid, null).start();
				}
			} catch (Exception e) {
			}
			Intent intents = new Intent(context, EduSevice.class);
			intents.putExtra("api", "notification");
			intents.putExtra("id", "create");
			intents.putExtra("title", title);
			context.startService(intents);
		}
	}
}
