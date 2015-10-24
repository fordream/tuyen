package vnp.org.moit.service;

import android.content.Context;
import android.os.Bundle;

import com.vnp.core.service.asyn.ExecuteAPI;

public class TopicGetImagesRunable extends IMRunable {
	private String topicId;

	public TopicGetImagesRunable(Context context, String topicId) {
		super(context, "TopicGetImages", null);
		this.topicId = topicId;

	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
	}

	@Override
	public void run() {
		Bundle extras = createBundle();
		extras.putString("topicId", topicId);
		ExecuteAPI exception = execute(extras, "TopicGetImages");
		finishexecute(exception);
	}
}