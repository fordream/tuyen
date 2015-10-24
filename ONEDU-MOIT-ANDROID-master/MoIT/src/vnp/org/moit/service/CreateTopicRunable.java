package vnp.org.moit.service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import vnp.org.moit.db.EduTopic;
import vnp.org.moit.dialog.EduProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.vnp.core.common.CommonAndroid;
import com.vnp.core.service.asyn.ExecuteAPI;

public class CreateTopicRunable extends IMRunable {
	private String title;
	private String description;
	private String schoolid;

	private List<Bitmap> bits = new ArrayList<Bitmap>();
	private EduProgressDialog progressDialog;

	public CreateTopicRunable(Context context, String title, String description, String schoolid, List<Bitmap> bit) {
		super(context, "TopicUploadImage", null);
		this.title = title;
		this.description = description;
		this.schoolid = schoolid;
		this.bits = bit;
		progressDialog = new EduProgressDialog(context);
		progressDialog.show();
	}

	private String message = "";
	private boolean isSuccess = false;

	public boolean isSuccess() {
		return isSuccess;
	}

	@Override
	public void run() {
		/**
		 * create topic
		 */
		Bundle extrasCreate = createBundle();
		if (bits.size() >= 1) {
			Bitmap b = CommonAndroid.getScaledBitmap(bits.get(0), 100, 100);
			extrasCreate.putString("mainImgBase64", CommonAndroid.bitmapToBase64(b));
		} else {
			extrasCreate.putString("mainImgBase64", "");
		}

		ExecuteAPI exception = execute(extrasCreate, "TopicAdd");
		finishexecute(exception);

		if (isSuccess(exception)) {
			isSuccess = true;
			pushImage(exception.getParseData());
		}
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		progressDialog.dismiss();
	}

	private void pushImage(String parseData) {
		try {
			new EduTopic(getContext()).addTopic(new JSONObject(parseData));
			String topicId = CommonAndroid.getString(new JSONObject(parseData), "ID");

			for (int i = 0; i < bits.size(); i++) {
				Bundle bundle = createBundle();
				bundle.putString("topicId", topicId);
				String imgBase64 = CommonAndroid.bitmapToBase64(bits.get(i));
				bundle.putString("imgBase64", imgBase64);
				execute(bundle, "TopicUploadImage");
				Thread.sleep(1000);
			}

			// load images
			Bundle extras = createBundle();
			extras.putString("topicId", topicId);
			ExecuteAPI executeAPI = execute(extras, "TopicGetImages");
			finishexecute(executeAPI);

		} catch (Exception e) {
		}
	}

	@Override
	public Bundle createBundle() {
		Bundle extras = super.createBundle();
		extras.putString("title", title);
		extras.putString("description", description);
		extras.putString("schoolid", schoolid);
		return extras;
	}
}
