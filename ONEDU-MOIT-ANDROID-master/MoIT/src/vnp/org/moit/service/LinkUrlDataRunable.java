package vnp.org.moit.service;

import org.json.JSONObject;

import vnp.org.moit.db.EduAccount;
import vnp.org.moit.db.EduArticleGetList;
import android.content.Context;

import com.vnp.core.common.CommonAndroid;
import com.vnp.core.service.RequestMethod;
import com.vnp.core.service.RestClient;

public class LinkUrlDataRunable extends IMRunable {
	private String Id;
	private String linkUrl;
	private String SchoolId;

	public LinkUrlDataRunable(Context context, String Id, String linkUrl, String SchoolId) {
		super(context, "TopicGetImages", null);
		this.Id = Id;
		this.linkUrl = linkUrl;
		this.SchoolId = SchoolId;

	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
	}

	@Override
	public void run() {
		if (!new EduArticleGetList(getContext()).loaded(Id) && !CommonAndroid.isBlank(Id) && !CommonAndroid.isBlank(linkUrl)) {
			RestClient client = new RestClient(linkUrl);
			client.execute(RequestMethod.GET);

			if (!CommonAndroid.isBlank(client.getResponse())) {
				JSONObject object = new JSONObject();
				try {
					object.put("linkUrlData", client.getResponse());
					object.put("Id", Id);
					object.put("SchoolId", SchoolId);

					new EduArticleGetList(getContext()).addNewFeed(new EduAccount(getContext()).getPhoneActive(), object);
				} catch (Exception e) {
				}
			}
		}

	}
}