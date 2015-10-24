package vnp.org.moit.service;

import java.util.List;

import one.edu.vn.sms.S1S2S3Activity;

import org.json.JSONArray;
import org.json.JSONObject;

import vnp.org.moit.db.EduAccount;
import vnp.org.moit.db.EduStudent;
import vnp.org.moit.db.EduTopicImage;
import vnp.org.moit.db.ServerStatus;
import vnp.org.moit.utils.MOITUtils;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import com.acv.cheerz.db.DataStore;
import com.vnp.core.common.CommonAndroid;
import com.vnp.core.common.ImageLoader;

public class EduSevice extends Service {
	public static int NOTIFY_ME_ID = 2034;
	private SMSGetListRunable loadMoreTinNhanRunable;

	@Override
	public void onCreate() {
		super.onCreate();
		loadMoreTinNhanRunable = new SMSGetListRunable(this);
		ServerStatus.getIn().init(this);
		ServerStatus.getIn().clear();
	}

	public EduSevice() {
		super();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	public enum TypeMore {//
		SMSList, //
		TopicList, //
		TopicCommentList, //
		ArticleList, //
		ArticleCommentList, //
		NOTIFICATION, //
		registerIdverifyCode, //
		LOADMOREMarkTableGet//
	};//

	public void sendBoardCastForUpdate(TypeMore typeMore, String id) {
		Intent intent = new Intent("vnp.org.moit.updateui");
		intent.putExtra("TypeMore", typeMore);
		intent.putExtra("id", id);
		intent.putExtra("title", id);
		sendBroadcast(intent);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		if (intent != null && intent.getExtras() != null && intent.getExtras().containsKey("api")) {

			String api = intent.getExtras().getString("api");
			String id = intent.getExtras().getString("id");
			DataStore.getInstance().init(this);
			int mNotificationId = DataStore.getInstance().get("mNotificationId", 0);

			if ("notification".equals(api)) {
				String title = intent.getStringExtra("title");
				if ("create".equals(id)) {

					// update bang diem
					EduStudent eduStudent = new EduStudent(this);
					List<String> idSchools = eduStudent.getSchoolIds();
					new MarkTableGetRunable(this, idSchools).start();
					// show sms push
					updateSMSByNotifycation(intent, title);
				} else if ("callscreen".equals(id)) {
					// check for start
					final NotificationManager mgr = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
					mgr.cancel(NOTIFY_ME_ID + mNotificationId);
					mgr.cancel(NOTIFY_ME_ID + 1);
					mgr.cancel(NOTIFY_ME_ID + 2);
					if (CommonAndroid.isAppRunningOnTop(this)) {
						sendBoardCastForUpdate(TypeMore.NOTIFICATION, title);
					} else {
						Intent mainIntent = new Intent(this, S1S2S3Activity.class);
						mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
						mainIntent.putExtra("type", "notification");
						mainIntent.putExtra("title", title);
						startActivity(mainIntent);
					}
				} else if ("delete".equals(id)) {
					final NotificationManager mgr = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
					for (int i = 0; i <= mNotificationId; i++) {
						mgr.cancel(NOTIFY_ME_ID + mNotificationId);
					}
				}

				return super.onStartCommand(intent, flags, startId);
			}

			String phone = new EduAccount(this).getPhoneActive();

			if ("loadmore".equals(api)) {
				loadMore(phone);
			}

			if ("loadmorenewffeed".equals(api)) {
				loadmoreNewFeedOrTopicComment(true, phone, id, 1);
			}

			if ("loadmoretopic".equals(api)) {
				loadmoreNewFeedOrTopicComment(false, phone, id, 1);
			}

			if ("uploadimgtopic".equals(api)) {
				String topicId = intent.getStringExtra("topicId");
				uploadImgTopic(intent, topicId, 0);
			}
		}

		return super.onStartCommand(intent, flags, startId);
	}

	private void updateSMSByNotifycation(final Intent intent, final String title) {

		DataStore.getInstance().init(EduSevice.this);

		int mNotificationId = DataStore.getInstance().get("mNotificationId", 0);
		if (CommonAndroid.isAppRunningOnTop(EduSevice.this)) {
			sendBoardCastForUpdate(TypeMore.NOTIFICATION, title);
		} else {
			mNotificationId = mNotificationId + 1;
			for (int i = 0; i < mNotificationId; i++) {
				final NotificationManager mgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				mgr.cancel(NOTIFY_ME_ID + i);
			}

			DataStore.getInstance().save("mNotificationId", mNotificationId);

			MOITUtils.createNotification(EduSevice.this, NOTIFY_ME_ID + mNotificationId, title);
		}

	}

	private void uploadImgTopic(final Intent intent, final String topicId, final int position) {
		String key = "imgBase64";
		final String imgBase64 = intent.getStringExtra(key);
		if (!CommonAndroid.isBlank(imgBase64)) {
			SynEduSevice synEduSevice = new SynEduSevice(this, "TopicUploadImage", false) {
				@Override
				public void onError(ErrorType errorType, Exception exception) {
					super.onError(errorType, exception);
				}

				@Override
				public void onSucsses(String parseCode, String parseDescription, String parseData, int responseCode, String responseMessage, String response) {
					super.onSucsses(parseCode, parseDescription, parseData, responseCode, responseMessage, response);
					if ("0".equals(parseCode)) {
					} else {
					}

				}
			};

			Bundle extras = new Bundle();
			extras.putString("topicId", topicId);
			extras.putString("imgBase64", imgBase64);
			synEduSevice.execute(extras);
		}
	}

	private void loadmoreNewFeedOrTopicComment(final boolean isNewFeed, final String phone, final String newFeedId, final int Page) {
		SynEduSevice synEduSevice = new SynEduSevice(this, isNewFeed ? "ArticleGetCommentList" : "TopicCommentGetList", false) {
			@Override
			public void onSucsses(String parseCode, String parseDescription, String parseData, int responseCode, String responseMessage, String response) {
				super.onSucsses(parseCode, parseDescription, parseData, responseCode, responseMessage, response);
				if (canLoadMore(parseData)) {
					loadmoreNewFeedOrTopicComment(isNewFeed, phone, newFeedId, Page + 1);
				}

				sendBoardCastForUpdate(isNewFeed ? TypeMore.ArticleCommentList : TypeMore.TopicCommentList, newFeedId);
			}
		};

		Bundle extras = new Bundle();
		extras.putString("articleId", newFeedId);
		extras.putString("topicId", newFeedId);
		extras.putString("Page", Page + "");
		synEduSevice.execute(extras);

		if ("1".equals(Page)) {
			SynEduSevice synEduSeviceLike = new SynEduSevice(this, isNewFeed ? "ArticleGetLike" : "TopicGetLike", false) {
				@Override
				public void onSucsses(String parseCode, String parseDescription, String parseData, int responseCode, String responseMessage, String response) {
					super.onSucsses(parseCode, parseDescription, parseData, responseCode, responseMessage, response);
				}
			};
			Bundle bundle = new Bundle();
			bundle.putString("topicId", newFeedId);
			bundle.putString("articleId", newFeedId);
			synEduSeviceLike.execute(bundle);
		}
	}

	private void loadMore(String phone) {
		EduStudent eduStudent = new EduStudent(this);
		List<String> idSchools = eduStudent.getSchoolIds();
		List<String> studentIds = eduStudent.getStudentIds();

		if (CommonAndroid.NETWORK.haveConnected(this)) {
			// delete all data
		}

		if (!CommonAndroid.isBlank(phone)) {
			// 0. load checkPhone
			checkPhone();
			// 1. load inbox more
			if (!loadMoreTinNhanRunable.isRunning()) {
				loadMoreTinNhanRunable = new SMSGetListRunable(this);
				loadMoreTinNhanRunable.start();
			}

			
			new MarkTableGetRunable(this, studentIds).start();
			
			for (String schooldId : idSchools) {
				// 2. load new feed more
				// 3. load newfeed comment detail more FIXME
				new ArticleGetListRunable(this, schooldId).start();
			}
			
			for (String schooldId : idSchools) {
				// 4. load forum more
				// 5. load froum comment more FIXME
				new TopicGetListRunable(this, schooldId).start();
			}

			// for (String schooldId : idSchools) {
			// load bang diem
			
			// }

			// update user push infor
			updateUserPushInfor(phone);
		}
	}

	private void checkPhone() {
		SynEduSevice synEduSevice = new SynEduSevice(this, "checkPhone", false) {
			@Override
			public void onError(ErrorType errorType, Exception exception) {
				super.onError(errorType, exception);
			}

			@Override
			public void onSucsses(String parseCode, String parseDescription, String parseData, int responseCode, String responseMessage, String response) {
				super.onSucsses(parseCode, parseDescription, parseData, responseCode, responseMessage, response);
			}
		};

		Bundle extras = new Bundle();
		synEduSevice.execute(extras);
	}

	private void updateUserPushInfor(String phone) {
		SynEduSevice synEduSevice = new SynEduSevice(this, "UpdateUserNotifyInfo", false) {
			@Override
			public void onError(ErrorType errorType, Exception exception) {
				super.onError(errorType, exception);
			}

			@Override
			public void onSucsses(String parseCode, String parseDescription, String parseData, int responseCode, String responseMessage, String response) {
				super.onSucsses(parseCode, parseDescription, parseData, responseCode, responseMessage, response);
			}
		};

		Bundle extras = new Bundle();
		synEduSevice.execute(extras);
	}

	private void loadMoreForum(final String phone, final String schooldId, final int Page) {
		SynEduSevice synEduSevice = new SynEduSevice(this, "TopicGetList", false) {
			@Override
			public void onError(ErrorType errorType, Exception exception) {
				super.onError(errorType, exception);
			}

			@Override
			public void onSucsses(String parseCode, String parseDescription, String parseData, int responseCode, String responseMessage, String response) {
				super.onSucsses(parseCode, parseDescription, parseData, responseCode, responseMessage, response);
				if (canLoadMore(parseData)) {
					loadMoreForum(phone, schooldId, Page + 1);
				}
				sendBoardCastForUpdate(TypeMore.TopicList, schooldId);
			}

			@Override
			public void onSucssesOnBackground(String parseCode, String parseDescription, String parseData, int responseCode, String responseMessage, String response, Bundle extras) {
				super.onSucssesOnBackground(parseCode, parseDescription, parseData, responseCode, responseMessage, response, extras);
				try {
					JSONArray array = new JSONArray(parseData);
					for (int i = 0; i < array.length(); i++) {
						JSONObject object = array.getJSONObject(i);
						String topicId = CommonAndroid.getString(object, "ID");

						if (!CommonAndroid.isBlank(topicId) && !new EduTopicImage(EduSevice.this).loaded(topicId)) {
							TopicGetImagesRunable topicGetImagesRunable = new TopicGetImagesRunable(EduSevice.this, topicId);
							topicGetImagesRunable.start();

						}
						if (!CommonAndroid.isBlank(topicId)) {
							loadmoreNewFeedOrTopicComment(false, phone, topicId, 1);
						}
					}
				} catch (Exception e) {
				}
			}
		};

		Bundle extras = new Bundle();
		extras.putString("schoolid", schooldId);
		extras.putString("Page", Page + "");
		synEduSevice.execute(extras);
	}

	private void loadMoreNewFeed(final String phone, final String schooldId, final int Page) {
		SynEduSevice synEduSevice = new SynEduSevice(this, "ArticleGetList", false) {
			@Override
			public void onError(ErrorType errorType, Exception exception) {
				super.onError(errorType, exception);
			}

			@Override
			public void onSucsses(String parseCode, String parseDescription, String parseData, int responseCode, String responseMessage, String response) {
				super.onSucsses(parseCode, parseDescription, parseData, responseCode, responseMessage, response);
				if (canLoadMore(parseData))
					loadMoreNewFeed(phone, schooldId, Page + 1);

				sendBoardCastForUpdate(TypeMore.ArticleList, schooldId);
			}

			@Override
			public void onSucssesOnBackground(String parseCode, String parseDescription, String parseData, int responseCode, String responseMessage, String response, Bundle extras) {
				super.onSucssesOnBackground(parseCode, parseDescription, parseData, responseCode, responseMessage, response, extras);
				try {
					JSONArray array = new JSONArray(parseData);
					for (int i = 0; i < array.length(); i++) {
						JSONObject object = array.getJSONObject(i);
						String id = CommonAndroid.getString(object, "Id");
						String linkUrl = CommonAndroid.getString(object, "linkUrl");
						String SchoolId = CommonAndroid.getString(object, "SchoolId");

						new LinkUrlDataRunable(EduSevice.this, id, linkUrl, SchoolId).start();

						loadmoreNewFeedOrTopicComment(true, phone, id, 1);
					}
				} catch (Exception e) {
				}
			}
		};

		Bundle extras = new Bundle();
		extras.putString("schoolid", schooldId);
		extras.putString("Page", Page + "");
		synEduSevice.execute(extras);
	}

	private void loadMoreInbox(final String phone, final int Page, final boolean needRepeed, final SynEduSevice synCallBack) {
		SynEduSevice synEduSevice = new SynEduSevice(this, "SMSGetList", false) {
			@Override
			public void onError(ErrorType errorType, Exception exception) {
				super.onError(errorType, exception);
				if (synCallBack != null) {
					synCallBack.onError(errorType, exception);
				}
			}

			@Override
			public void onSucsses(String parseCode, String parseDescription, String parseData, int responseCode, String responseMessage, String response) {
				super.onSucsses(parseCode, parseDescription, parseData, responseCode, responseMessage, response);
				if (synCallBack != null) {
					synCallBack.onSucsses(parseCode, parseDescription, parseData, responseCode, responseMessage, response);
				}

				if (canLoadMore(parseData)) {
					if (needRepeed) {
						loadMoreInbox(phone, Page + 1, needRepeed, synCallBack);
					}

					sendBoardCastForUpdate(TypeMore.SMSList, null);
				}
			}
		};

		Bundle extras = new Bundle();
		extras.putString("Page", Page + "");

		synEduSevice.execute(extras);
	}

	private void loadImageUrl(String url) {
		ImageLoader.getInstance(this).displayImage(url, null, true);
		ImageLoader.getInstance(this).displayImage(url, null, false);
	}
}