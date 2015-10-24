package vnp.org.moit.service;

import java.io.File;
import java.util.Set;

import one.edu.vn.sms.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import vnp.org.moit.db.EduAccount;
import vnp.org.moit.db.EduArticleAddComment;
import vnp.org.moit.db.EduArticleGetLike;
import vnp.org.moit.db.EduArticleGetList;
import vnp.org.moit.db.EduInbox;
import vnp.org.moit.db.EduMarkTableGet;
import vnp.org.moit.db.EduPersion;
import vnp.org.moit.db.EduStudent;
import vnp.org.moit.db.EduTopic;
import vnp.org.moit.db.EduTopicCommentGetList;
import vnp.org.moit.db.EduTopicGetLike;
import vnp.org.moit.db.EduTopicImage;
import vnp.org.moit.dialog.EduProgressDialog;
import vnp.org.moit.utils.MOITUtils;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.Settings.Secure;

import com.vnp.core.common.CommonAndroid;
import com.vnp.core.common.LogUtils;
import com.vnp.core.common.VnpFileCache;
import com.vnp.core.gcm.GcmBroadcastReceiver;
import com.vnp.core.gcm.GcmBroadcastReceiver.RegisterCallBackRegisterId;
import com.vnp.core.service.RequestMethod;
import com.vnp.core.service.RestClient;
import com.vnp.core.service.RestClient.IFacebookAvatarCallBack;
import com.vnp.core.service.RestClientCallBack;

public class SynEduSevice {
	public static final int RecordsPerPage = 200;
	private ProgressDialog progressDialog;
	private String api;
	private Context context;
	private String service_url;
	private String phone = "";
	private boolean needShowDialog = false;

	private EduAccount eduAccount;
	private EduStudent eduStudent;
	private EduArticleGetList eduNewFeed;
	private EduTopic eduTopic;

	/**
	 * 
	 * @param context
	 * @param api
	 * @param needShowDialog
	 */
	public SynEduSevice(Context context, String api, boolean needShowDialog) {
		this.context = context;
		this.api = api;
		service_url = context.getString(R.string.service_url);
		this.needShowDialog = needShowDialog;

		if (needShowDialog) {
			progressDialog = new EduProgressDialog(context);
		}

		eduAccount = new EduAccount(context);
		eduStudent = new EduStudent(context);
		eduNewFeed = new EduArticleGetList(context);
		eduTopic = new EduTopic(context);
	}

	public enum ErrorType {
		REGISTERIDNULL, EXECEPTION, HAVENOTNETWORK
	}

	public void onError(ErrorType errorType, Exception exception) {
		if (needShowDialog && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}

	public void onSucsses(String parseCode, String parseDescription, String parseData, int responseCode, String responseMessage, String response) {
		if (needShowDialog && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}

		if ("checkPhone".equals(api) && "0".equals(parseCode) && needShowDialog) {
		}

		if ("ArticleAddComment".equals(api)) {

		}
		if ("LoginFaceBook".equals(api) && "0".equals(parseCode)) {

		}
	}

	private void updateStatusVerifycode(String verifyCode) {
		try {
			JSONObject json = new JSONObject();
			json.put("verifycode", verifyCode);
			json.put("phone", phone);

			new EduAccount(context).add(json);
		} catch (Exception e) {

		}
	}

	public void onSucssesOnBackground(String parseCode, String parseDescription, String parseData, int responseCode, String responseMessage, String response, Bundle extras) {
		if (CommonAndroid.isBlank(phone)) {
			phone = extras.getString("phone");
		}
		LogUtils.e(api, parseData);
		if ("checkPhone".equals(api) && "10".equals(parseCode)) {
			updateStatusVerifycode("0");
		} else if ("checkPhone".equals(api) && "6".equals(parseCode)) {
			updaeCheckPhone(parseData);
			updateStatusVerifycode("1");
		} else if ("checkPhone".equals(api) && "0".equals(parseCode)) {
			updateStatusVerifycode("0");
			VnpFileCache memoryUtils = new VnpFileCache(context, "VnpFileCache");
			new File(memoryUtils.getPathCacheExternalMemory()).mkdirs();
			String outfileName = memoryUtils.getPathCacheExternalMemory() + "codec.txt";

			Bundle xextras = new Bundle();
			xextras.putString("checkPhone-code", parseData);
			String registerId = GcmBroadcastReceiver.getRegisterId(context);
			if (MOITUtils.ISFacebooktest) {
				registerId = 1 + "";
			}

			xextras.putString("checkPhone-registerID", registerId);
			CommonAndroid.saveToFile(xextras, outfileName);

		} else if ("verifyCode".equals(api) && "0".equals(parseCode)) {
			addAccount(phone, null, null, null, null, null, null, null);
			updateStatusVerifycode("1");
		} else if ("LoginFaceBook".equals(api) && "0".equals(parseCode)) {
			String snUsername = extras.getString("snUsername");
			String avatarBase64 = extras.getString("avatarBase64");
			String snInfo = extras.getString("snInfo");
			if (CommonAndroid.isBlank(snInfo)) {
				snInfo = "";
			}
			String avatar = String.format("https://graph.facebook.com/%s/picture?type=large", extras.getString("facebookId"));
			avatar = null;
			addAccount(phone, snUsername, avatarBase64, null, null, null, null, snInfo);
			updateStatusVerifycode("1");
			new EduPersion(context).add(phone, snUsername, avatar);

			// update avatar
			SynEduSevice synEduSevice = new SynEduSevice(context, "UpdateUser", false) {
				@Override
				public void onError(ErrorType errorType, Exception exception) {
					super.onError(errorType, exception);
				}

				@Override
				public void onSucsses(String parseCode, String parseDescription, String parseData, int responseCode, String responseMessage, String response) {
					super.onSucsses(parseCode, parseDescription, parseData, responseCode, responseMessage, response);
				}
			};
			Bundle mextras = new Bundle();
			mextras.putString("name", snUsername);
			mextras.putString("avatarBase64", avatarBase64);

			synEduSevice.execute(mextras);

		} else if ("UpdateUser".equals(api) && "0".equals(parseCode)) {
			String name = extras.getString("name");
			// String avatarBase64 = extras.getString("avatarBase64");
			addAccount(phone, name, null, null, null, null, null, null);
			updateStatusVerifycode("1");
			new EduPersion(context).add(phone, name, null);
		} else if ("ArticleGetList".equals(api) && "0".equals(parseCode)) {

			ArticleGetList(parseData);

		} else if ("TopicGetList".equals(api) && "0".equals(parseCode)) {

			TopicGetList(parseData);

		} else if ("TopicGetImages".equals(api) && "0".equals(parseCode)) {
			TopicGetImages(parseData);

		} else if ("TopicCommentGetList".equals(api) && "0".equals(parseCode)) {
			TopicCommentGetList(parseData);
		} else if ("TopicCommentAdd".equals(api) && "0".equals(parseCode)) {

			TopicCommentAdd(parseData);

		} else if ("ArticleGetCommentList".equals(api) && "0".equals(parseCode)) {
			ArticleAddComment(false, parseData);

		} else if ("ArticleAddComment".equals(api) && "0".equals(parseCode)) {
			ArticleAddComment(true, parseData);

		} else if ("SMSGetList".equals(api) && "0".equals(parseCode)) {

			SMSGetList(parseData);
		} else if ("ArticleGetDetail".equals(api) && "0".equals(parseCode)) {
			ArticleGetDetail(parseData);
		} else if ("ArticleGetLike".equals(api) && "0".equals(parseCode)) {

			ArticleGetLike(parseData, extras.getString("articleId"));
		} else if ("TopicGetDetail".equals(api) && "0".equals(parseCode)) {
			TopicGetDetail(parseData);

		} else if ("TopicAdd".equals(api) && "0".equals(parseCode)) {
			TopicAdd(parseData);

		} else if ("TopicGetLike".equals(api) && "0".equals(parseCode)) {

			TopicGetLike(parseData, extras.getString("topicId"));
		} else if ("TopicUploadImage".equals(api) && "0".equals(parseCode)) {
		} else if ("TopicLike".equals(api) && "0".equals(parseCode)) {
		} else if ("ArticleLike".equals(api) && "0".equals(parseCode)) {
		} else if ("TopicCommentReport".equals(api) && "0".equals(parseCode)) {
		} else if ("ArticleCommentReport".equals(api) && "0".equals(parseCode)) {
		} else if ("SMSFeedback".equals(api) && "0".equals(parseCode)) {
		} else if ("MarkTableGet".equals(api) && "0".equals(parseCode)) {
			addMarkTableGet(parseData, extras);
		}
	}

	private void addMarkTableGet(String parseData, Bundle extras) {
		String studentid = extras.getString("studentid");
		LogUtils.e("Checked : studentid", studentid + " : " + parseData);
		try {
			JSONArray array = new JSONArray(parseData);
			for (int i = 0; i < array.length(); i++) {
				String js = array.getString(i);
				JSONArray array2 = new JSONArray(js);
				for (int j = 0; j < array2.length(); j++) {
					JSONObject object = array2.getJSONObject(j);
					object.put("studentid", studentid);
					new EduMarkTableGet(context).add(object);
				}
			}
		} catch (Exception e) {
			LogUtils.e("Checked : studentid", e);
		}
	}

	private void TopicGetLike(String parseData, String topicId) {
		try {
			new EduTopicGetLike(context).add(new JSONArray(parseData), topicId);
		} catch (Exception e) {
		}
	}

	private void ArticleGetLike(String parseData, String ArticleId) {

		try {
			new EduArticleGetLike(context).add(new JSONArray(parseData), ArticleId);
		} catch (JSONException e) {
		}
	}

	private void SMSGetList(String parseData) {
		EduInbox eduTopicCommentGetList = new EduInbox(context);
		try {

			JSONArray array = new JSONArray(parseData);
			for (int i = 0; i < array.length(); i++)
				eduTopicCommentGetList.add(array.getJSONObject(i));
		} catch (Exception ex) {
		}
	}

	private void ArticleAddComment(boolean isAdd, String parseData) {
		EduArticleAddComment newFeedComment = new EduArticleAddComment(context);
		newFeedComment.add(isAdd, parseData);
		if (isAdd) {
			try {
				JSONObject json = new JSONObject(parseData);
				String Phone = CommonAndroid.getString(json, "Phone");
				String OwnerName = CommonAndroid.getString(json, "OwnerName");
				String avatar = CommonAndroid.getString(json, "avatar");
				String ownnerName = CommonAndroid.getString(json, "ownnerName");
				if (CommonAndroid.isBlank(OwnerName)) {
					OwnerName = ownnerName;
				}

				new EduPersion(context).add(Phone, OwnerName, avatar);
			} catch (Exception e) {
			}
		} else {
			try {
				JSONArray array = new JSONArray(parseData);
				for (int i = 0; i < array.length(); i++) {
					JSONObject json = array.getJSONObject(i);
					String Phone = CommonAndroid.getString(json, "Phone");
					String OwnerName = CommonAndroid.getString(json, "OwnerName");
					String avatar = CommonAndroid.getString(json, "avatar");
					String ownnerName = CommonAndroid.getString(json, "ownnerName");
					if (CommonAndroid.isBlank(OwnerName)) {
						OwnerName = ownnerName;
					}

					new EduPersion(context).add(Phone, OwnerName, avatar);
				}
			} catch (Exception exception) {

			}
		}

	}

	private void TopicCommentAdd(String parseData) {
		try {
			JSONObject json = new JSONObject(parseData);
			EduTopicCommentGetList eduTopicCommentGetList = new EduTopicCommentGetList(context);
			eduTopicCommentGetList.addEduTopicCommentGetList(json);

			String Phone = CommonAndroid.getString(json, "Phone");
			String OwnerName = CommonAndroid.getString(json, "OwnerName");
			String avatar = CommonAndroid.getString(json, "avatar");
			new EduPersion(context).add(Phone, OwnerName, avatar);
		} catch (Exception exception) {
		}
	}

	private void TopicCommentGetList(String parseData) {
		EduTopicCommentGetList eduTopicCommentGetList = new EduTopicCommentGetList(context);
		try {
			JSONArray array = new JSONArray(parseData);
			for (int i = 0; i < array.length(); i++) {
				eduTopicCommentGetList.addEduTopicCommentGetList(array.getJSONObject(i));
				String Phone = CommonAndroid.getString(array.getJSONObject(i), "Phone");
				String OwnerName = CommonAndroid.getString(array.getJSONObject(i), "OwnerName");
				String avatar = CommonAndroid.getString(array.getJSONObject(i), "avatar");

				new EduPersion(context).add(Phone, OwnerName, avatar);
			}
		} catch (Exception ex) {
		}
	}

	private void TopicGetImages(String parseData) {
		EduTopicImage eduTopicImage = new EduTopicImage(context);
		try {
			JSONArray array = new JSONArray(parseData);
			for (int i = 0; i < array.length(); i++)
				eduTopicImage.addTopicImage(array.getJSONObject(i));
		} catch (Exception ex) {
		}
	}

	private void TopicGetList(String parseData) {
		try {
			JSONArray array = new JSONArray(parseData);
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = array.getJSONObject(i);
				eduTopic.addTopic(object);
			}
		} catch (Exception e) {
		}
	}

	private void TopicGetDetail(String parseData) {
		TopicGetList(parseData);
	}

	private void TopicAdd(String parseData) {
		try {
			JSONObject array = new JSONObject(parseData);
			eduTopic.addTopic(array);

			String phone = CommonAndroid.getString(array, "phone");
			String OwnerName = CommonAndroid.getString(array, "OwnerName");
			new EduPersion(context).add(phone, OwnerName, null);
		} catch (Exception e) {
		}
	}

	private void ArticleGetList(String parseData) {
		try {
			JSONArray array = new JSONArray(parseData);
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = array.getJSONObject(i);
				eduNewFeed.addNewFeed(phone, object);
			}
		} catch (Exception e) {
		}
	}

	private void ArticleGetDetail(String parseData) {
		try {
			JSONArray array = new JSONArray(parseData);
			for (int i = 0; i < array.length(); i++)
				eduNewFeed.addNewFeed(phone, array.getJSONObject(i));
		} catch (Exception e) {
		}
	}

	public void executeLoginFacebook(final String snUsername, final String snInfo, final String facebookId) {
		if (!CommonAndroid.NETWORK.haveConnected(context)) {
			onError(ErrorType.HAVENOTNETWORK, null);
			return;
		} else {
			if (needShowDialog) {
				progressDialog.show();
			}
		}

		RestClient restClient = new RestClient(null);
		restClient.executeLoadAvatarFacebook(facebookId, new IFacebookAvatarCallBack() {

			@Override
			public void onStart() {

			}

			Bitmap bitmapx;

			@Override
			public void onSsucessInBackground(Bitmap bitmap) {
				if (bitmap != null) {
					bitmapx = CommonAndroid.getScaledBitmap(bitmap, 80, 80);
					// new File(outfileName).delete();
					// CommonAndroid.saveBitmapTofile(bitmap, new
					// File(outfileName));
				}
			}

			@Override
			public void onSsucess(Bitmap bitmap) {
				// String avatarBase64 =
				// CommonAndroid.fileImageToBase64(context, outfileName);
				if (bitmapx == null)
					bitmapx = bitmap;
				String avatarBase64 = CommonAndroid.bitmapToBase64(bitmapx);
				if (CommonAndroid.isBlank(avatarBase64))
					avatarBase64 = "";
				executeLogin(snUsername, avatarBase64, snInfo);
			}
		});

	}

	private void executeLogin(final String name, final String avatarBase64, final String snInfo) {
		Bundle extras = new Bundle();
		extras.putString("avatarBase64", avatarBase64);
		extras.putString("snType", "fb");
		extras.putString("snInfo", snInfo);
		extras.putString("snUsername", name);
		execute(extras);

		SynEduSevice synEduSevice = new SynEduSevice(context, "UpdateUser", false) {
			@Override
			public void onError(ErrorType errorType, Exception exception) {
				super.onError(errorType, exception);
				onSucsses(null, null, null, 0, null, null);
			}

			@Override
			public void onSucsses(String parseCode, String parseDescription, String parseData, int responseCode, String responseMessage, String response) {
				super.onSucsses(parseCode, parseDescription, parseData, responseCode, responseMessage, response);
			}
		};

		Bundle mextras = new Bundle();
		mextras.putString("name", name);
		mextras.putString("avatarBase64", avatarBase64);
		synEduSevice.execute(mextras);
	}

	public void execute(final Bundle extras) {
		if (!CommonAndroid.NETWORK.haveConnected(context)) {
			onError(ErrorType.HAVENOTNETWORK, null);
			return;
		} else {
			if (needShowDialog) {
				progressDialog.show();
			}
		}

		/**
		 * add phone if not exits
		 */
		if (!extras.containsKey("phone")) {
			extras.putString("phone", eduAccount.getPhoneActive());
		}

		// if (!extras.containsKey("Phone")) {
		// extras.putString("Phone", extras.getString("phone"));
		// }
		if (!extras.containsKey("deviceType")) {
			extras.putString("deviceType", "Android");
		}
		// if (!extras.containsKey("DeviceType")) {
		// extras.putString("DeviceType", "Android");
		// }

		if (!extras.containsKey("RecordsPerPage")) {
			extras.putString("RecordsPerPage", RecordsPerPage + "");
		}
		String name = eduAccount.getNameActive();
		if (CommonAndroid.isBlank(name)) {
			name = "noname";
		}

		// if ("ArticleAddComment".equals(api)) {
		// if (!extras.containsKey("ownnerName")) {
		// extras.putString("ownnerName", name);
		// }
		// } else {
		if (!extras.containsKey("ownername")) {
			extras.putString("ownername", name);
		}
		// }
		// if (!extras.containsKey("OwnerName")) {
		// // extras.putString("OwnerName", name);
		// }

		String android_id = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
		extras.putString("OSID", android_id);

		/**
		 * get phone
		 */
		phone = extras.getString("phone");

		String registerId = GcmBroadcastReceiver.getRegisterId(context);
		if (MOITUtils.ISFacebooktest) {
			registerId = 1 + "";
			// deviceId = 1 + "";
		}
		if (CommonAndroid.isBlank(registerId)) {
			GcmBroadcastReceiver.registerCallBackRegisterId(context, new RegisterCallBackRegisterId() {

				@Override
				public void onStart() {

				}

				@Override
				public void onCallBack(String registerId, String deviceId) {
					if (CommonAndroid.isBlank(registerId)) {
						onError(ErrorType.REGISTERIDNULL, null);
						return;
					}

					callApi(extras);
				}
			});
		} else {

			callApi(extras);
		}
	}

	private void callApi(final Bundle extras) {

		String registerId = GcmBroadcastReceiver.getRegisterId(context);
		if (MOITUtils.ISFacebooktest) {
			registerId = 1 + "";
			// deviceId = 1 + "";
		}
		extras.putString("deviceId", registerId);

		if (!CommonAndroid.NETWORK.haveConnected(context)) {
			onError(ErrorType.HAVENOTNETWORK, null);
			return;
		}

		RestClient client = new RestClient(service_url + api);
		Set<String> keys = extras.keySet();
		for (String key : keys) {
			client.addParam(key, extras.getString(key));
		}

		client.execute(RequestMethod.POST, new RestClientCallBack() {
			String parseCode = "";
			String parseDescription = "";
			String parseData = "";

			@Override
			public void onSucssesOnBackground(int responseCode, String responseMessage, String response) {
				super.onSucssesOnBackground(responseCode, responseMessage, response);
				try {
					JSONObject json = new JSONObject(response);
					parseCode = json.getString("code");
					parseDescription = json.getString("description");
					parseData = json.getString("data");
				} catch (Exception e) {
				}

				SynEduSevice.this.onSucssesOnBackground(parseCode, parseDescription, parseData, responseCode, responseMessage, response, extras);
			}

			@Override
			public void onSucsses(int responseCode, String responseMessage, String response) {
				super.onSucsses(responseCode, responseMessage, response);

				SynEduSevice.this.onSucsses(parseCode, parseDescription, parseData, responseCode, responseMessage, response);
			}
		});
	}

	/**
	 * when call api check phone
	 * 
	 * @param response
	 */
	private void updaeCheckPhone(String response) {
		try {
			String name = "";
			String avatar = "";
			String sninfo = "";
			JSONArray jsonInfor = new JSONArray(response);
			eduStudent.deleteAll();
			for (int i = 0; i < jsonInfor.length(); i++) {
				JSONObject item = jsonInfor.getJSONObject(i);
				String StudentId = CommonAndroid.getString(item, "StudentId");
				String SchoolId = CommonAndroid.getString(item, "SchoolId");
				String BlockId = CommonAndroid.getString(item, "BlockId");
				String ClassId = CommonAndroid.getString(item, "ClassId");
				String ParentPhone = CommonAndroid.getString(item, "ParentPhone");
				String ParentName = CommonAndroid.getString(item, "ParentName");
				String ParentAvatar = CommonAndroid.getString(item, "ParentAvatar");
				String Name = CommonAndroid.getString(item, "Name");
				String SchoolName = CommonAndroid.getString(item, "SchoolName");
				String SchoolLogo = CommonAndroid.getString(item, "SchoolLogo");
				if (!CommonAndroid.isBlank(CommonAndroid.getString(item, "sninfo"))) {
					sninfo = CommonAndroid.getString(item, "sninfo");
				}

				eduStudent.addStudent(phone, StudentId, ClassId, BlockId, SchoolId, ParentPhone, Name, ParentName, ParentAvatar, SchoolName, SchoolLogo);
				avatar = ParentAvatar;
				name = ParentName;
			}

			new EduPersion(context).add(phone, name, avatar);

			addAccount(phone, name, avatar, null, null, null, null, sninfo);
		} catch (Exception e) {
		}
	}

	/**
	 * 
	 * @author acv-dev-android-02 detail
	 */
	public enum Detail {
		INBOX, NEWFEED, TOPIC, NONE
	}

	public interface IDetailCallBack {
		public void onSuccess();

		public void onError();
	}

	public static void executeDetail(Context context, final Detail detailt, final String TopicIdOrArticleId, final IDetailCallBack detailtCallBack) {
		final EduProgressDialog progressDialog = new EduProgressDialog(context);
		progressDialog.show();
		final Bundle extras = new Bundle();
		extras.putString("topicId", TopicIdOrArticleId);
		extras.putString("articleId", TopicIdOrArticleId);
		extras.putString("Page", "1");
		String api = detailt == Detail.TOPIC ? "TopicGetDetail" : "ArticleGetDetail";
		String apiLike = detailt == Detail.TOPIC ? "TopicGetLike" : "ArticleGetLike";
		String apiImages = detailt == Detail.TOPIC ? "TopicGetImages" : null;

		final SynEduSevice synEduSeviceImages = new SynEduSevice(context, apiImages, false) {
			@Override
			public void onError(ErrorType errorType, Exception exception) {
				super.onError(errorType, exception);
				onSucsses(null, null, null, 0, null, null);
			}

			@Override
			public void onSucsses(String parseCode, String parseDescription, String parseData, int responseCode, String responseMessage, String response) {
				super.onSucsses(parseCode, parseDescription, parseData, responseCode, responseMessage, response);
				progressDialog.dismiss();
				detailtCallBack.onSuccess();
			}
		};

		final SynEduSevice synEduSeviceLike = new SynEduSevice(context, apiLike, false) {
			@Override
			public void onError(ErrorType errorType, Exception exception) {
				super.onError(errorType, exception);
				onSucsses(null, null, null, 0, null, null);
			}

			@Override
			public void onSucsses(String parseCode, String parseDescription, String parseData, int responseCode, String responseMessage, String response) {
				super.onSucsses(parseCode, parseDescription, parseData, responseCode, responseMessage, response);
				synEduSeviceImages.execute(extras);
			}
		};

		final SynEduSevice synEduSevice = new SynEduSevice(context, api, false) {
			@Override
			public void onError(ErrorType errorType, Exception exception) {
				super.onError(errorType, exception);
				onSucsses(null, null, null, 0, null, null);
			}

			@Override
			public void onSucsses(String parseCode, String parseDescription, String parseData, int responseCode, String responseMessage, String response) {
				super.onSucsses(parseCode, parseDescription, parseData, responseCode, responseMessage, response);
				synEduSeviceLike.execute(extras);
			}
		};
		synEduSevice.execute(extras);
	}

	public static void executeLike(Context context, final Detail detailt, final String id, final IDetailCallBack detailtCallBack) {
		final EduProgressDialog progressDialog = new EduProgressDialog(context);
		progressDialog.show();
		final Bundle extras = new Bundle();
		extras.putString("topicId", id);
		extras.putString("articleId", id);
		extras.putString("Page", "1");
		String apiLike = detailt == Detail.TOPIC ? "TopicLike" : "ArticleLike";
		String apiGetDetail = detailt == Detail.TOPIC ? "TopicGetDetail" : "ArticleGetDetail";
		String apiLikeList = detailt == Detail.TOPIC ? "TopicGetLike" : "ArticleGetLike";

		final SynEduSevice synEduSeviceLike = new SynEduSevice(context, apiLikeList, false) {
			@Override
			public void onError(ErrorType errorType, Exception exception) {
				super.onError(errorType, exception);
				onSucsses(null, null, null, 0, null, null);
			}

			@Override
			public void onSucsses(String parseCode, String parseDescription, String parseData, int responseCode, String responseMessage, String response) {
				super.onSucsses(parseCode, parseDescription, parseData, responseCode, responseMessage, response);
				progressDialog.dismiss();
				detailtCallBack.onSuccess();
			}
		};

		final SynEduSevice synEduGetDetail = new SynEduSevice(context, apiGetDetail, false) {
			@Override
			public void onError(ErrorType errorType, Exception exception) {
				super.onError(errorType, exception);
				onSucsses(null, null, null, 0, null, null);
			}

			@Override
			public void onSucsses(String parseCode, String parseDescription, String parseData, int responseCode, String responseMessage, String response) {
				super.onSucsses(parseCode, parseDescription, parseData, responseCode, responseMessage, response);
				synEduSeviceLike.execute(extras);
			}
		};

		final SynEduSevice synEduLike = new SynEduSevice(context, apiLike, false) {
			@Override
			public void onError(ErrorType errorType, Exception exception) {
				super.onError(errorType, exception);
				detailtCallBack.onError();
				progressDialog.dismiss();
			}

			@Override
			public void onSucsses(String parseCode, String parseDescription, String parseData, int responseCode, String responseMessage, String response) {
				super.onSucsses(parseCode, parseDescription, parseData, responseCode, responseMessage, response);
				if ("0".equals(parseCode)) {
					synEduGetDetail.execute(extras);
				} else {
					onError(ErrorType.HAVENOTNETWORK, null);
				}
			}
		};

		synEduLike.execute(extras);
	}

	/**
	 * 
	 * @param parseData
	 * @return
	 */
	public boolean canLoadMore(String parseData) {
		boolean needLoadMore = false;
		try {
			if (new JSONArray(parseData).length() == RecordsPerPage) {
				needLoadMore = true;
			}
		} catch (Exception exception) {

		}

		return needLoadMore;
	}

	public void addAccount(String phone, String name, String avatar, String location, String birthday, String email, String facebookid, String sninfo) {
		try {
			JSONObject jsonObject = new JSONObject();
			if (!CommonAndroid.isBlank(phone)) {
				jsonObject.put("phone", phone);
			}

			if (!CommonAndroid.isBlank(name)) {
				jsonObject.put("name", name);
			}

			if (!CommonAndroid.isBlank(avatar)) {
				jsonObject.put("avatar", avatar);
			}

			if (!CommonAndroid.isBlank(location)) {
				jsonObject.put("location", location);
			}

			if (!CommonAndroid.isBlank(birthday)) {
				jsonObject.put("birthday", birthday);
			}

			if (!CommonAndroid.isBlank(email)) {
				jsonObject.put("email", email);
			}

			if (!CommonAndroid.isBlank(facebookid)) {
				jsonObject.put("facebookid", facebookid);
			}
			if (!CommonAndroid.isBlank(sninfo)) {
				jsonObject.put("sninfo", sninfo);
			}
			jsonObject.put("active", "1");
			// jsonObject.put("verifycode", "1");
			eduAccount.add(jsonObject);
		} catch (Exception exception) {
		}
	}
}