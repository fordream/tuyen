package one.edu.vn.sms;

import java.io.File;

import one.edu.vn.sms.html.TongKetHtmlUtils;

import org.json.JSONObject;

import vnp.org.moit.db.EduAccount;
import vnp.org.moit.db.ServerStatus;
import vnp.org.moit.dialog.HelloDialog;
import vnp.org.moit.dialog.ThongBaoDialog;
import vnp.org.moit.service.CheckPhoneRunable;
import vnp.org.moit.service.EduSevice;
import vnp.org.moit.service.EduSevice.TypeMore;
import vnp.org.moit.service.LoginFaceRunable;
import vnp.org.moit.service.MarKImageUploadRunable;
import vnp.org.moit.service.SynEduSevice;
import vnp.org.moit.utils.MOITUtils;
import vnp.org.moit.view.HomeView;
import android.annotation.SuppressLint;
import android.app.AlertDialog.Builder;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.acv.cheerz.db.DataStore;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.avnp.BaseFacebookActivity;
import com.facebook.avnp.Facebook415Utils;
import com.facebook.avnp.Facebook415Utils.IFacebookListener;
import com.facebook.login.LoginResult;
import com.facebook.share.Sharer.Result;
//import com.facebook.Response;
//import com.facebook.model.GraphUser;
import com.soundcloud.android.crop.Crop;
import com.vnp.core.common.CommonAndroid;
import com.vnp.core.common.GalleryCameraChooser;
import com.vnp.core.common.VnpFileCache;

public class S1S2S3Activity extends BaseFacebookActivity implements OnClickListener {
	private void setBackgroundEditText(int res, boolean isRadius, boolean errorBackground) {
		if (isRadius) {
			CommonAndroid.getView(this, res).setBackgroundResource(errorBackground ? R.drawable.bg_editext_radius_error : R.drawable.bg_editext_radius);
		} else {
			CommonAndroid.getView(this, res).setBackgroundResource(errorBackground ? R.drawable.bg_editext_quare_error : R.drawable.bg_editext_quare);
		}
	}

	private ThongBaoDialog thongBaoDialog;
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			TypeMore typeMore = (TypeMore) intent.getSerializableExtra("TypeMore");
			String id = intent.getStringExtra("id");

			if (typeMore == TypeMore.ArticleList) {
				homeView.updateData(typeMore, id);
			} else if (typeMore == TypeMore.ArticleCommentList) {
				homeView.updateData(typeMore, id);
			} else if (typeMore == TypeMore.SMSList || typeMore == TypeMore.LOADMOREMarkTableGet) {
				homeView.updateData(typeMore, id);
			} else if (typeMore == TypeMore.TopicCommentList) {
				homeView.updateData(typeMore, id);
			} else if (typeMore == TypeMore.TopicList) {
				homeView.updateData(typeMore, id);
			} else if (typeMore == TypeMore.NOTIFICATION) {
				if (findViewById(R.id.home_main).getVisibility() == View.VISIBLE) {
					String title = intent.getStringExtra("title");
					if (thongBaoDialog != null && thongBaoDialog.isShowing()) {
						return;
					}
					thongBaoDialog = new ThongBaoDialog(S1S2S3Activity.this, title, null, getString(R.string.ok));
					thongBaoDialog.show();

					gotoHome(true);
				}
			} else if (typeMore == TypeMore.registerIdverifyCode) {
				new ThongBaoDialog(S1S2S3Activity.this, id, null, getString(R.string.ok)).show();
			}
		}
	};
	private HomeView homeView;
	private GalleryCameraChooser galleryCameraChooser = new GalleryCameraChooser() {
		@Override
		public void onGallery(Bitmap bitmap) {

			if (findViewById(R.id.s3_main).getVisibility() == View.VISIBLE) {
				cropImage(bitmap);
			} else {
				imageForCreateTopPic(bitmap);
			}
		}

		@Override
		public void onGalleryError() {

		}

		@Override
		public void onCamera(Bitmap bitmap) {
			if (findViewById(R.id.s3_main).getVisibility() == View.VISIBLE) {
				cropImage(bitmap);
			} else {
				imageForCreateTopPic(bitmap);
			}
		}
	};
	private vnp.org.moit.view.CreateTopicView createTopicView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TongKetHtmlUtils.setWidth(this);
		setContentView(R.layout.s1s2s3);
		clearTempFile();
		TextView s2_txt_guilai = ((TextView) findViewById(R.id.s2_txt_guilai));
		s2_txt_guilai.setText(Html.fromHtml(String.format(getString(R.string.guilai), " <u>", "</u>")));
		s2_txt_guilai.setOnClickListener(this);

		homeView = CommonAndroid.getView(this, R.id.home_main);
		findViewById(R.id.s3_img_avatar).setOnClickListener(this);
		findViewById(R.id.s1_btn).setOnClickListener(this);
		findViewById(R.id.s2_btn_1).setOnClickListener(this);
		findViewById(R.id.s2_btn_2).setOnClickListener(this);
		findViewById(R.id.s3_btn_dangnhap).setOnClickListener(this);
		findViewById(R.id.s3_btn_loginfacebook).setOnClickListener(this);
		createTopicView = CommonAndroid.getView(this, R.id.create_topic);
		createTopicView.setActivity(this);
		IntentFilter filter = new IntentFilter("vnp.org.moit.updateui");
		registerReceiver(broadcastReceiver, filter);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				String type = getIntent().getStringExtra("type");
				if (!CommonAndroid.isBlank(type) && "notification".equals(type)) {
					gotoHome(true);
				} else {
					String phone = new EduAccount(S1S2S3Activity.this).getPhoneActive();
					String name = new EduAccount(S1S2S3Activity.this).getNameActive();
					boolean verifyCode = new EduAccount(S1S2S3Activity.this).getverifycode();

					if (!CommonAndroid.isBlank(phone)) {
						if (!verifyCode) {
							nextUI(findViewById(R.id.splash), findViewById(R.id.s1_main));
						} else {
							checkPhoneOnStart();
						}
					} else {
						nextUI(findViewById(R.id.splash), findViewById(R.id.s1_main));
					}
				}
			}

		}, 1000);
		// createNotification(100, "alo");

		Intent intent = new Intent(this, EduSevice.class);
		intent.putExtra("type", "notification");
		intent.putExtra("id", "delete");
		startService(intent);
		
		
		//startActivity(new Intent(this, MainActivity.class));
		
	}

	@SuppressLint("NewApi")
	public void createNotification(int NOTIFY_ME_ID, String title) {

		final NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

		Intent intent = new Intent(this, EduSevice.class);
		intent.putExtra("api", "notification");
		intent.putExtra("id", "callscreen");
		intent.putExtra("title", title);
		intent.putExtra("title", title);
		PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, 0);

		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
			// API 16 onwards
			Notification.Builder builder = new Notification.Builder(this);
			builder.setAutoCancel(false).setContentIntent(pendingIntent).setContentText(title).setContentTitle(this.getString(R.string.app_name)).setOngoing(true).setSmallIcon(R.drawable.ic_launcher)
					.setWhen(System.currentTimeMillis());
			Notification notification = builder.build();
			notification.flags = Notification.DEFAULT_LIGHTS & Notification.FLAG_AUTO_CANCEL;
			mNotificationManager.notify(NOTIFY_ME_ID, notification);
		} else {
			// API 15 and earlier
			NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
			builder.setAutoCancel(false).setContentIntent(pendingIntent).setContentText(title).setContentTitle(getString(R.string.app_name)).setOngoing(true).setSmallIcon(R.drawable.ic_launcher)
					.setWhen(System.currentTimeMillis());
			Notification notification = builder.getNotification();
			notification.flags = Notification.DEFAULT_LIGHTS & Notification.FLAG_AUTO_CANCEL;
			mNotificationManager.notify(NOTIFY_ME_ID, notification);
		}

		try {
			Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
			r.play();
		} catch (Exception e) {
		}
	}

	private void checkPhoneOnStart() {

		if (CommonAndroid.NETWORK.haveConnected(this)) {
			new CheckPhoneRunable(this) {
				protected void onPostExecute(String result) {
					super.onPostExecute(result);
					if ("0".equals(getParseCode())) {
						nextUI(findViewById(R.id.splash), findViewById(R.id.s2_main));
					} else if ("6".equals(getParseCode())) {
						startNew();
					} else {
						startNew();
					}
				};
			}.start();
		} else {
			startNew();
		}

	}

	private void startNew() {
		String name = new EduAccount(S1S2S3Activity.this).getNameActive();
		String phone = new EduAccount(S1S2S3Activity.this).getPhoneActive();
		if (!CommonAndroid.isBlank(name)) {
			gotoHome(false);
		} else {
			nextUI(findViewById(R.id.splash), findViewById(R.id.s3_main));
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(broadcastReceiver);
	}

	private void clearTempFile() {
		VnpFileCache memoryUtils = new VnpFileCache(this, "VnpFileCache");
		String fileName = memoryUtils.getPathCacheExternalMemory() + "temp.png";
		String outfileName = memoryUtils.getPathCacheExternalMemory() + "outtemp.png";
		new File(fileName).delete();
		new File(outfileName).delete();
	}

	protected void cropImage(Bitmap bitmap) {

		VnpFileCache memoryUtils = new VnpFileCache(this, "VnpFileCache");
		new File(memoryUtils.getPathCacheExternalMemory()).mkdirs();
		String fileName = memoryUtils.getPathCacheExternalMemory() + "temp.png";
		String outfileName = memoryUtils.getPathCacheExternalMemory() + "outtemp.png";
		// bitmap = CommonAndroid.getScaledBitmap(bitmap, 80, 80);
		CommonAndroid.saveBitmapTofile(bitmap, new File(fileName));

		Uri inputUri = Uri.fromFile(new File(fileName));
		Uri outputUri = Uri.fromFile(new File(outfileName));
		Crop.of(inputUri, outputUri).asSquare().start(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		galleryCameraChooser.onActivityResult(this, requestCode, resultCode, data);

		if (requestCode == Crop.REQUEST_CROP && resultCode == RESULT_OK) {
			VnpFileCache memoryUtils = new VnpFileCache(this, "VnpFileCache");
			new File(memoryUtils.getPathCacheExternalMemory()).mkdirs();

			String outfileName = memoryUtils.getPathCacheExternalMemory() + "outtemp.png";
			Bitmap bitmap = BitmapFactory.decodeFile(outfileName);
			bitmap = CommonAndroid.getScaledBitmap(bitmap, 80, 80);

			CommonAndroid.saveBitmapTofile(bitmap, new File(outfileName));
			ImageView s3_img_avatar = (ImageView) findViewById(R.id.s3_img_avatar);
			s3_img_avatar.setImageBitmap(bitmap);
		}

	}

	@Override
	public void onClick(View v) {
		if (R.id.s1_btn == v.getId()) {
			if (!CommonAndroid.NETWORK.haveConnected(S1S2S3Activity.this)) {
				new ThongBaoDialog(S1S2S3Activity.this, S1S2S3Activity.this.getString(R.string.thuchienkhongthanhcong), null, S1S2S3Activity.this.getString(R.string.ok)).show();
				return;
			}
			verifyPhone(true);
		} else if (R.id.s2_txt_guilai == v.getId()) {
			if (!CommonAndroid.NETWORK.haveConnected(S1S2S3Activity.this)) {
				new ThongBaoDialog(S1S2S3Activity.this, S1S2S3Activity.this.getString(R.string.thuchienkhongthanhcong), null, S1S2S3Activity.this.getString(R.string.ok)).show();
				return;
			}
			new ThongBaoDialog(S1S2S3Activity.this, getString(R.string.s2_xac_nhan_gui_lai), getString(R.string.huy), getString(R.string.dongy)) {
				public void onClickButton2() {
					ResendVerifyCode();
					// verifyPhone(false);

				}

			}.show();

		} else if (R.id.s3_img_avatar == v.getId()) {
			chooseTypeImage(null);
		} else if (R.id.s2_btn_1 == v.getId()) {
			backUI(findViewById(R.id.s1_main), findViewById(R.id.s2_main));
		} else if (R.id.s2_btn_2 == v.getId()) {
			if (!CommonAndroid.NETWORK.haveConnected(S1S2S3Activity.this)) {
				new ThongBaoDialog(S1S2S3Activity.this, S1S2S3Activity.this.getString(R.string.thuchienkhongthanhcong), null, S1S2S3Activity.this.getString(R.string.ok)).show();
				return;
			}

			verifyCode();
		} else if (R.id.s3_btn_dangnhap == v.getId()) {
			if (!CommonAndroid.NETWORK.haveConnected(S1S2S3Activity.this)) {
				new ThongBaoDialog(S1S2S3Activity.this, S1S2S3Activity.this.getString(R.string.thuchienkhongthanhcong), null, S1S2S3Activity.this.getString(R.string.ok)).show();
				return;
			}
			checkLogin();
		} else if (R.id.s3_btn_loginfacebook == v.getId()) {
			if (!CommonAndroid.NETWORK.haveConnected(S1S2S3Activity.this)) {
				new ThongBaoDialog(S1S2S3Activity.this, S1S2S3Activity.this.getString(R.string.thuchienkhongthanhcong), null, S1S2S3Activity.this.getString(R.string.ok)).show();
				return;
			}
			loginFacebook();
		}
	}

	private void ResendVerifyCode() {
		CommonAndroid.hiddenKeyBoard(this);

		EditText s1_edt = CommonAndroid.getView(this, R.id.s1_edt);
		final String phone = MOITUtils.convertPhone(s1_edt.getText().toString().trim());
		setBackgroundEditText(R.id.s1_edt, true, false);

		SynEduSevice synEduSevice = new SynEduSevice(this, "ResendVerifyCode", true) {
			@Override
			public void onError(ErrorType errorType, Exception exception) {
				super.onError(errorType, exception);
				new ThongBaoDialog(S1S2S3Activity.this, getString(R.string.error_network), null, getString(R.string.ok)).show();
			}

			@Override
			public void onSucsses(String parseCode, String parseDescription, String parseData, int responseCode, String responseMessage, String response) {
				super.onSucsses(parseCode, parseDescription, parseData, responseCode, responseMessage, response);
				if ("0".equals(parseCode)) {
					String message = getString(R.string.s2_xac_nhan_gui_lai_5p);
					new ThongBaoDialog(S1S2S3Activity.this, message, null, getString(R.string.ok)).show();
				} else {
					onError(ErrorType.EXECEPTION, null);
				}
			}
		};
		Bundle extras = new Bundle();
		extras.putString("phone", phone);
		synEduSevice.execute(extras);
	};

	private boolean isLoginFace = true;

	private void loginFacebook() {
		isLoginFace = true;
		Facebook415Utils.getInstance().login();
	}

	private void registerFacebook(final String snUsername, final String facebookId, final String snInfo) {

		LoginFaceRunable faceRunable = new LoginFaceRunable(this, snUsername, snInfo, facebookId) {
			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);

				if (isSuccess()) {
					gotoHome(false);
				} else {
					new ThongBaoDialog(S1S2S3Activity.this, getMessage(), null, getString(R.string.dong)).show();
					// new ThongBaoDialog(S1S2S3Activity.this, parseDescription,
					// null, getString(R.string.ok)).show();
				}
			}
		};
		faceRunable.start();

		// SynEduSevice synEduSevice = new SynEduSevice(this, "LoginFaceBook",
		// true) {
		// @Override
		// public void onError(ErrorType errorType, Exception exception) {
		// super.onError(errorType, exception);
		// new ThongBaoDialog(S1S2S3Activity.this,
		// getString(R.string.error_network), null,
		// getString(R.string.ok)).show();
		// }
		//
		// @Override
		// public void onSucsses(String parseCode, String parseDescription,
		// String parseData, int responseCode, String responseMessage, String
		// response) {
		// super.onSucsses(parseCode, parseDescription, parseData, responseCode,
		// responseMessage, response);
		//
		// if ("0".equals(parseCode)) {
		// checkNextHome(true);
		// } else {
		// new ThongBaoDialog(S1S2S3Activity.this, parseDescription, null,
		// getString(R.string.ok)).show();
		// }
		// }
		// };
		// synEduSevice.executeLoginFacebook(snUsername, snInfo, facebookId);

	}

	private void checkLogin() {
		String s3_name = ((TextView) findViewById(R.id.s3_name)).getText().toString().trim();
		String s3_mk = ((TextView) findViewById(R.id.s3_mk)).getText().toString().trim();

		setBackgroundEditText(R.id.s3_name, false, false);
		if (CommonAndroid.isBlank(s3_name)) {
			new ThongBaoDialog(S1S2S3Activity.this, getString(R.string.vuilongnhapten), null, getString(R.string.ok)).show();
			setBackgroundEditText(R.id.s3_name, false, true);
			return;
		}

		VnpFileCache memoryUtils = new VnpFileCache(this, "VnpFileCache");
		String outfileName = memoryUtils.getPathCacheExternalMemory() + "outtemp.png";
		if (!new File(outfileName).exists()) {
			new ThongBaoDialog(S1S2S3Activity.this, getString(R.string.bancochackhongnhapanhdaidien), getString(R.string.boqua), getString(R.string.nhapdanh)) {
				public void onClickButton1() {
					login();
				};

				public void onClickButton2() {
					chooseTypeImage(new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							login();
						}
					});
				};
			}.show();

			return;
		}
		login();
	}

	private void login() {
		final String s3_name = ((TextView) findViewById(R.id.s3_name)).getText().toString().trim();
		String s3_mk = ((TextView) findViewById(R.id.s3_mk)).getText().toString().trim();
		VnpFileCache memoryUtils = new VnpFileCache(this, "VnpFileCache");
		String outfileName = memoryUtils.getPathCacheExternalMemory() + "outtemp.png";
		String avatarBase64 = CommonAndroid.fileImageToBase64(this, outfileName);

		if (CommonAndroid.isBlank(avatarBase64))
			avatarBase64 = "";

		executeLogin(s3_name, avatarBase64);
	}

	private void executeLogin(String s3_name, String avatarBase64) {
		SynEduSevice synEduSevice = new SynEduSevice(this, "UpdateUser", true) {
			@Override
			public void onError(ErrorType errorType, Exception exception) {
				super.onError(errorType, exception);
				new ThongBaoDialog(S1S2S3Activity.this, getString(R.string.error_network), null, getString(R.string.ok)).show();
			}

			@Override
			public void onSucsses(String parseCode, String parseDescription, String parseData, int responseCode, String responseMessage, String response) {
				super.onSucsses(parseCode, parseDescription, parseData, responseCode, responseMessage, response);
				if ("0".equals(parseCode)) {
					checkNextHome(true);
					return;
				}

				if (!CommonAndroid.isBlank(parseDescription)) {
					new ThongBaoDialog(S1S2S3Activity.this, parseDescription, null, getString(R.string.ok)).show();
				} else {
					onError(ErrorType.HAVENOTNETWORK, null);
				}
			}
		};
		Bundle extras = new Bundle();
		extras.putString("name", s3_name);
		extras.putString("avatarBase64", avatarBase64);

		synEduSevice.execute(extras);
	}

	private void verifyCode() {
		TextView s2_edt_1 = CommonAndroid.getView(this, R.id.s2_edt_1);
		TextView s2_edt_2 = CommonAndroid.getView(this, R.id.s2_edt_2);
		TextView s2_edt_3 = CommonAndroid.getView(this, R.id.s2_edt_3);
		TextView s2_edt_4 = CommonAndroid.getView(this, R.id.s2_edt_4);

		String s2_edt_1Str = s2_edt_1.getText().toString().trim();
		String s2_edt_2Str = s2_edt_2.getText().toString().trim();
		String s2_edt_3Str = s2_edt_3.getText().toString().trim();
		String s2_edt_4Str = s2_edt_4.getText().toString().trim();

		setBackgroundEditText(R.id.s2_edt_1, false, false);
		setBackgroundEditText(R.id.s2_edt_2, false, false);
		setBackgroundEditText(R.id.s2_edt_3, false, false);
		setBackgroundEditText(R.id.s2_edt_4, false, false);

		if (CommonAndroid.isBlank(s2_edt_1Str) || CommonAndroid.isBlank(s2_edt_2Str) || CommonAndroid.isBlank(s2_edt_3Str) || CommonAndroid.isBlank(s2_edt_4Str)) {
			new ThongBaoDialog(S1S2S3Activity.this, getString(R.string.s2_input), null, getString(R.string.ok)).show();

			setBackgroundEditText(R.id.s2_edt_1, false, CommonAndroid.isBlank(s2_edt_1Str));
			setBackgroundEditText(R.id.s2_edt_2, false, CommonAndroid.isBlank(s2_edt_2Str));
			setBackgroundEditText(R.id.s2_edt_3, false, CommonAndroid.isBlank(s2_edt_3Str));
			setBackgroundEditText(R.id.s2_edt_4, false, CommonAndroid.isBlank(s2_edt_4Str));
			return;
		}
		String code = s2_edt_1Str + s2_edt_2Str + s2_edt_3Str + s2_edt_4Str;
		EditText s1_edt = CommonAndroid.getView(this, R.id.s1_edt);
		final String s1_str = MOITUtils.convertPhone(s1_edt.getText().toString().trim());

		SynEduSevice synEduSevice = new SynEduSevice(this, "verifyCode", true) {
			@Override
			public void onError(ErrorType errorType, Exception exception) {
				super.onError(errorType, exception);
			}

			public void onSucsses(String parseCode, String parseDescription, String parseData, int responseCode, String responseMessage, String response) {
				super.onSucsses(parseCode, parseDescription, parseData, responseCode, responseMessage, response);
				if ("0".equals(parseCode)) {
					reCheckPhone();
				} else if (!CommonAndroid.isBlank(parseCode)) {
					setBackgroundEditText(R.id.s2_edt_1, false, true);
					setBackgroundEditText(R.id.s2_edt_2, false, true);
					setBackgroundEditText(R.id.s2_edt_3, false, true);
					setBackgroundEditText(R.id.s2_edt_4, false, true);
					new ThongBaoDialog(S1S2S3Activity.this, parseDescription, null, getString(R.string.ok)).show();
				} else {
					if (CommonAndroid.isBlank(parseDescription))
						parseDescription = getString(R.string.s2_xacnhan_fail);
					new ThongBaoDialog(S1S2S3Activity.this, parseDescription, null, getString(R.string.ok)).show();
				}
			};
		};
		Bundle extras = new Bundle();
		extras.putString("code", code);
		extras.putString("phone", s1_str);
		synEduSevice.execute(extras);
	}

	protected void reCheckPhone() {
		SynEduSevice synEduSevice = new SynEduSevice(this, "checkPhone", true) {
			@Override
			public void onError(ErrorType errorType, Exception exception) {
				super.onError(errorType, exception);
				nextUI(findViewById(R.id.s2_main), findViewById(R.id.s3_main));
			}

			@Override
			public void onSucsses(String parseCode, String parseDescription, String parseData, int responseCode, String responseMessage, String response) {
				super.onSucsses(parseCode, parseDescription, parseData, responseCode, responseMessage, response);
				String nameActive = new EduAccount(S1S2S3Activity.this).getNameActive();

				if (CommonAndroid.isBlank(nameActive)) {
					nextUI(findViewById(R.id.s2_main), findViewById(R.id.s3_main));
				} else {
					gotoHome(false);
				}
			}
		};
		Bundle extras = new Bundle();
		synEduSevice.execute(extras);
	}

	private void chooseTypeImage(DialogInterface.OnClickListener onClick) {
		Builder builder = new Builder(this);
		builder.setItems(new String[] { "Gallery", "Camera" }, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which == 0) {
					galleryCameraChooser.startGalleryChooser(S1S2S3Activity.this);
				} else {
					galleryCameraChooser.startCameraChooser(S1S2S3Activity.this);
				}
			}
		});
		builder.setNegativeButton(R.string.boqua, onClick);
		builder.show();
	}

	private void verifyPhone(final boolean isS1_btn) {
		CommonAndroid.hiddenKeyBoard(this);

		EditText s1_edt = CommonAndroid.getView(this, R.id.s1_edt);
		final String phone = MOITUtils.convertPhone(s1_edt.getText().toString().trim());
		setBackgroundEditText(R.id.s1_edt, true, false);
		if (!MOITUtils.verifyPhone(phone)) {
			new ThongBaoDialog(S1S2S3Activity.this, getString(R.string.str001), null, getString(R.string.ok)).show();
			setBackgroundEditText(R.id.s1_edt, true, true);
			return;
		}

		SynEduSevice synEduSevice = new SynEduSevice(this, "checkPhone", true) {
			@Override
			public void onError(ErrorType errorType, Exception exception) {
				super.onError(errorType, exception);
				new ThongBaoDialog(S1S2S3Activity.this, getString(R.string.error_network), null, getString(R.string.ok)).show();
			}

			@Override
			public void onSucsses(String parseCode, String parseDescription, String parseData, int responseCode, String responseMessage, String response) {
				super.onSucsses(parseCode, parseDescription, parseData, responseCode, responseMessage, response);
				((TextView) findViewById(R.id.s2_number)).setText(phone);

				if ("0".equals(parseCode)) {
					if (isS1_btn) {
						((TextView) findViewById(R.id.s2_edt_1)).setText("");
						((TextView) findViewById(R.id.s2_edt_2)).setText("");
						((TextView) findViewById(R.id.s2_edt_3)).setText("");
						((TextView) findViewById(R.id.s2_edt_4)).setText("");
						nextUI(findViewById(R.id.s1_main), findViewById(R.id.s2_main));
					}
				} else if ("6".equals(parseCode)) {
					String name = new EduAccount(S1S2S3Activity.this).getNameActive();
					if (CommonAndroid.isBlank(name)) {
						nextUI(findViewById(R.id.s1_main), findViewById(R.id.s3_main));
					} else {
						checkNextHome(false);
					}
				} else if ("9".equals(parseCode) || "8".equals(parseCode)) {
					new ThongBaoDialog(S1S2S3Activity.this, parseDescription, null, getString(R.string.ok)).show();
				} else if ("10".equals(parseCode)) {
					((TextView) findViewById(R.id.s2_edt_1)).setText("");
					((TextView) findViewById(R.id.s2_edt_2)).setText("");
					((TextView) findViewById(R.id.s2_edt_3)).setText("");
					((TextView) findViewById(R.id.s2_edt_4)).setText("");
					nextUI(findViewById(R.id.s1_main), findViewById(R.id.s2_main));

					// setBackgroundEditText(R.id.s1_edt, true, true);
					// new ThongBaoDialog(S1S2S3Activity.this, parseDescription,
					// null, getString(R.string.ok)).show();
				} else {

					if (!CommonAndroid.isBlank(parseDescription)) {
						new ThongBaoDialog(S1S2S3Activity.this, parseDescription, null, getString(R.string.ok)).show();
					} else
						onError(ErrorType.HAVENOTNETWORK, null);
				}
			}
		};
		Bundle extras = new Bundle();
		extras.putString("phone", phone);
		synEduSevice.execute(extras);

	}

	private void checkNextHome(boolean needLoadChild) {
		if (needLoadChild) {
			SynEduSevice synEduSevice = new SynEduSevice(this, "checkPhone", true) {
				@Override
				public void onError(ErrorType errorType, Exception exception) {
					super.onError(errorType, exception);
					new ThongBaoDialog(S1S2S3Activity.this, getString(R.string.error_network), null, getString(R.string.ok)).show();
				}

				@Override
				public void onSucsses(String parseCode, String parseDescription, String parseData, int responseCode, String responseMessage, String response) {
					super.onSucsses(parseCode, parseDescription, parseData, responseCode, responseMessage, response);

					if ("0".equals(parseCode)) {
						gotoHome(false);
					} else if ("6".equals(parseCode)) {
						gotoHome(false);
					} else {
						onError(ErrorType.HAVENOTNETWORK, null);
					}
				}
			};
			Bundle extras = new Bundle();
			synEduSevice.execute(extras);
		} else {
			gotoHome(false);
		}
	}

	private void gotoHome(boolean isByNotifycation) {

		if (CommonAndroid.NETWORK.haveConnected(this)) {
			ServerStatus.getIn().setAllLoadMore();
		}
		HomeView homeView = CommonAndroid.getView(this, R.id.home_main);
		homeView.setActivity(this);
		homeView.loadFirstData(isByNotifycation);
		// loadmore all
		Intent intent = new Intent(this, EduSevice.class);
		intent.putExtra("api", "loadmore");
		startService(intent);

		if (findViewById(R.id.splash).getVisibility() == View.VISIBLE) {
			nextUI(findViewById(R.id.splash), findViewById(R.id.home_main));
		} else if (findViewById(R.id.s1_main).getVisibility() == View.VISIBLE) {
			nextUI(findViewById(R.id.s1_main), findViewById(R.id.home_main));
		} else if (findViewById(R.id.s3_main).getVisibility() == View.VISIBLE) {
			nextUI(findViewById(R.id.s3_main), findViewById(R.id.home_main));
		} else if (findViewById(R.id.s2_main).getVisibility() == View.VISIBLE) {
			nextUI(findViewById(R.id.s2_main), findViewById(R.id.home_main));
		}

		DataStore.getInstance().init(this);
		if (DataStore.getInstance().get("first-use", true)) {
			DataStore.getInstance().save("first-use", false);
			new HelloDialog(this).show();
		}
	}

	@Override
	public void onBackPressed() {
		CommonAndroid.hiddenKeyBoard(this);
		if (isAnimationRunning) {
			return;
		}
		if (createTopicView.getVisibility() == View.VISIBLE) {
			backUI(findViewById(R.id.home_main), createTopicView);
			homeView.updateTopic();
			return;
		}
		if (findViewById(R.id.s2_main).getVisibility() == View.VISIBLE) {
			backUI(findViewById(R.id.s1_main), findViewById(R.id.s2_main));
			return;
		}
		super.onBackPressed();
	}

	private boolean isAnimationRunning = false;

	private void nextUI(final View v1, final View v2) {
		v1.setVisibility(View.VISIBLE);
		v2.setVisibility(View.VISIBLE);
		isAnimationRunning = true;
		Animation right_in = AnimationUtils.loadAnimation(this, R.anim.right_in);
		Animation left_out = AnimationUtils.loadAnimation(this, R.anim.left_out);

		right_in.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				isAnimationRunning = false;
				v1.setVisibility(View.GONE);
			}
		});
		v1.startAnimation(left_out);
		v2.startAnimation(right_in);
	}

	private void backUI(final View v1, final View v2) {
		v1.setVisibility(View.VISIBLE);
		v2.setVisibility(View.VISIBLE);
		isAnimationRunning = true;
		Animation right_out = AnimationUtils.loadAnimation(this, R.anim.right_out);
		Animation left_in = AnimationUtils.loadAnimation(this, R.anim.left_in);

		right_out.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				isAnimationRunning = false;
				v2.setVisibility(View.GONE);
			}
		});
		v1.startAnimation(left_in);
		v2.startAnimation(right_out);
	}

	public void openChooserImageForTopic() {
		chooseTypeImage(null);
	}

	private void imageForCreateTopPic(Bitmap bit) {
		try {
			bit = CommonAndroid.scaleBitmap(bit, 750);
		} catch (Exception ex) {

		} catch (Error e) {

		}
		createTopicView.imageForCreateTopPic(bit);
	}

	public void createTopicDialog(String schoolId) {

		// Intent intent = new Intent(this, CreateTopicActivity.class);
		// intent.putExtra("schoolid", schoolId);
		// startActivity(intent);

		createTopicView.setVisibility(View.VISIBLE);
		createTopicView.reset(schoolId);
		nextUI(findViewById(R.id.home_main), createTopicView);
	}

	private Bitmap shareBitmap;

	public void chiase(String smsid, final Bitmap bitmap) {
		shareBitmap = bitmap;

		// if (Facebook415Utils.getInstance().isLogin()) {
		// sharePhoto();
		// } else {
		new ThongBaoDialog(this, R.string.banchuadangnhapfacebook, R.string.loginfacebook, R.string.chuyentoitrangcanhan) {
			public void onClickButton1() {
				// isLoginFace = false;
				// Facebook415Utils.getInstance().login();
				sharePhoto();
			};

			public void onClickButton2() {
				Intent intent = new Intent("android.intent.category.LAUNCHER");
				intent.setClassName("com.facebook.katana", "com.facebook.katana.LoginActivity");
				startActivity(intent);
			};
		}.show();
		// }
	}

	@Override
	public IFacebookListener createIFacebookListener() {
		return new IFacebookListener() {

			@Override
			public void onShareSuccess(Result result) {
				new ThongBaoDialog(S1S2S3Activity.this, getString(R.string.sharethanhcong), 0, R.string.dong).show();
			}

			@Override
			public void onShareError(FacebookException error) {
				new ThongBaoDialog(S1S2S3Activity.this, S1S2S3Activity.this.getString(R.string.thuchienkhongthanhcong), null, getString(R.string.dong)).show();
			}

			@Override
			public void onShareCancel() {

			}

			@Override
			public void onLoginSuccess(LoginResult loginResult) {
				if (isLoginFace) {

					try {
						Profile profile = Facebook415Utils.getInstance().getProfile();
						if (profile != null) {
						} else {
						}
						String name = profile.getName();
						String id = profile.getId();
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("id", id);
						jsonObject.put("name", name);
						jsonObject.put("FirstName", profile.getFirstName());
						jsonObject.put("LastName", profile.getLastName());
						jsonObject.put("MiddleName", profile.getMiddleName());
						registerFacebook(name, id, jsonObject.toString());
					} catch (Exception exception) {
					}
				} else {
					sharePhoto();
				}
			}

			@Override
			public void onLoginError(FacebookException exception) {
			}

			@Override
			public void onLoginCancel() {

			}

			@Override
			public void onCurrentProfileChangedOfProfileTracker(Profile oldProfile, Profile currentProfile) {

			}
		};
	}

	protected void sharePhoto() {
		// Facebook415Utils.getInstance().onClickPostPhoto(shareBitmap,
		// getString(R.string.share_face_title));

		// Facebook415Utils.getInstance().shareDialogShow();
		// String url =
		// "https://www.facebook.com/sharer/sharer.php?u=http%3A//i.9mobi.vn/cf/images/2015/03/nkk/hinh-anh-dep-4.jpg";
		new MarKImageUploadRunable(this, CommonAndroid.bitmapToBase64(shareBitmap)).start();
	}
}