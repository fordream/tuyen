package vnp.org.moit.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import one.edu.vn.sms.R;
import vnp.org.moit.db.EduAccount;
import vnp.org.moit.db.EduStudent;
import vnp.org.moit.service.EduSevice;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.vnp.core.common.CommonAndroid;

public class MOITUtils {
	public static final boolean ISFacebooktest = false;

	public static String convertPhone(String phone) {
		while (phone != null && phone.startsWith("+")) {
			phone = phone.substring(1, phone.length());
		}

		while (phone != null && phone.startsWith("0")) {
			phone = phone.substring(1, phone.length());
		}

		if (phone != null && phone.startsWith("840")) {
			phone = "84" + phone.substring(3, phone.length());
		} else if (phone != null && phone.startsWith("84")) {

		} else {
			phone = "84" + phone;
		}
		return phone;
	}

	public static boolean verifyPhone(String s1_str) {
		List<String> list = new ArrayList<String>();
		list.add("09");
		list.add("+849");
		list.add("+8409");
		list.add("849");
		list.add("8409");
		list.add("0849");

		// add
		list.add("0841");
		list.add("01");
		list.add("841");
		list.add("+841");
		list.add("08401");
		list.add("+8401");
		for (String str : list) {
			if (s1_str.startsWith(str)) {
				return true;
			}
		}

		return false;
	}

	public static String parseNumberLikeOrNumberComment(String str) {
		try {
			Long.parseLong(str);
		} catch (Exception ex) {
			str = "0";
		}
		return str;
	}

	public static String parseDateDMY(String date, Context context) {
		String strDate = date;
		if (CommonAndroid.isBlank(date)) {
			strDate = "";
		} else {
			date = date.replace("/Date(", "").replace(")/", "");
			try {
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(Long.parseLong(date));
				int year = calendar.get(Calendar.YEAR);
				int month = calendar.get(Calendar.MONTH) + 1;
				int day = calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH);

				strDate = String.format("%s/%s/%s", (day < 10 ? "0" : "") + day, (month < 10 ? "0" : "") + month, year);
			} catch (Exception ex) {
				strDate = "";
			}
		}

		return strDate;
	}

	public static long toMinisecon(String time) {
		try {
			time = time.substring(0, time.indexOf("."));
			String year = time.substring(0, 4);
			String month = time.substring(5, 7);
			String day = time.substring(8, 10);

			String h = time.substring(11, 13);
			String m = time.substring(14, 16);
			String s = time.substring(17, 19);

			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.YEAR, Integer.parseInt(year));
			calendar.set(Calendar.MONTH, Integer.parseInt(month) - 1);
			calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
			calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(h));
			calendar.set(Calendar.MINUTE, Integer.parseInt(m));
			calendar.set(Calendar.SECOND, Integer.parseInt(s));
			return calendar.getTimeInMillis();
		} catch (Exception ex) {
			return 0;
		}
	}

	public static String parseDate(String date, Context context) {
		String strDate = date;
		if (CommonAndroid.isBlank(date)) {
			strDate = "";
		} else {
			if (date.contains(".")) {
				date = toMinisecon(date) + "";
			} else {
				date = date.replace("/Date(", "").replace(")/", "");
			}
			try {
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(Long.parseLong(date));
				int year = calendar.get(Calendar.YEAR);
				int month = calendar.get(Calendar.MONTH) + 1;
				int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
				int day = calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH);
				int hour = calendar.get(Calendar.HOUR_OF_DAY);
				int minus = calendar.get(Calendar.MINUTE);

				Long distance = System.currentTimeMillis() - Long.parseLong(date);
				distance = distance / 1000;

				if (distance < 60 * 60) {
					// trong vong 1h
					strDate = String.format(context.getString(R.string.xphhuttruoc), distance / 60);
				} else if (isTrogngay(Long.parseLong(date))) {
					// trong vong 24h
					strDate = String.format(context.getString(R.string.xtiengtruoc), distance / (60 * 60));
				} else if (isNgayHomeQua(Long.parseLong(date))) {
					// la hom qua
					strDate = String.format(context.getString(R.string.homqua), hour, minus);
				} else if (isTrongTuan(Long.parseLong(date))) {
					// dayOfWeek
					String thu = getThu(Long.parseLong(date), context);
					strDate = String.format(context.getString(R.string.trongtuan), thu, hour, minus);
				} else {
					strDate = String.format(context.getString(R.string.tuantruoc), (day < 10 ? "0" : "") + day, (month < 10 ? "0" : "") + month, year, hour, minus);
				}
			} catch (Exception ex) {
				strDate = "";
			}
		}

		return strDate;
	}

	public static String getThu(long parseLong, Context context) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(parseLong);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		if (dayOfWeek == 1) {
			return context.getString(R.string.chunhat);
		} else if (dayOfWeek == 2) {
			return context.getString(R.string.thu2);
		} else if (dayOfWeek == 3) {
			return context.getString(R.string.thu3);
		} else if (dayOfWeek == 4) {
			return context.getString(R.string.thu4);
		} else if (dayOfWeek == 5) {
			return context.getString(R.string.thu5);
		} else if (dayOfWeek == 6) {
			return context.getString(R.string.thu6);
		} else if (dayOfWeek == 7) {
			return context.getString(R.string.thu7);
		}
		return "";
	}

	private static boolean isTuanTruoc(long parseLong) {
		Calendar CurentCalendar = Calendar.getInstance();
		CurentCalendar.setTimeInMillis(System.currentTimeMillis());

		int curentDAY_OF_YEAR = CurentCalendar.get(Calendar.WEEK_OF_YEAR);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(parseLong);

		int DAY_OF_YEAR = calendar.get(Calendar.WEEK_OF_YEAR);

		return curentDAY_OF_YEAR - DAY_OF_YEAR == 1;
	}

	private static boolean isTrongTuan(long parseLong) {
		Calendar CurentCalendar = Calendar.getInstance();
		CurentCalendar.setTimeInMillis(System.currentTimeMillis());

		int curentWeekOfYear = CurentCalendar.get(Calendar.WEEK_OF_YEAR);
		int curentdayOfYear = CurentCalendar.get(Calendar.DAY_OF_YEAR);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(parseLong);
		int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
		int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
		return curentdayOfYear - dayOfYear >= 2 && curentdayOfYear - dayOfYear <= 7;// curentWeekOfYear
																					// -
																					// weekOfYear
																					// ==
																					// 0;
	}

	private static boolean isNgayHomeQua(long parseLong) {
		Calendar CurentCalendar = Calendar.getInstance();
		CurentCalendar.setTimeInMillis(System.currentTimeMillis());

		int curentDAY_OF_YEAR = CurentCalendar.get(Calendar.DAY_OF_YEAR);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(parseLong);
		int DAY_OF_YEAR = calendar.get(Calendar.DAY_OF_YEAR);

		return curentDAY_OF_YEAR - DAY_OF_YEAR == 1;
	}

	private static String toYYYYMD(int curentYear, int curentMonth, int curentDay) {
		return String.format("%s/%s%s/%s%s", curentYear, (curentMonth + 1) < 10 ? "0" : "", curentMonth, curentDay < 10 ? "0" : "", curentDay);
	}

	private static boolean isTrogngay(long parseLong) {
		Calendar CurentCalendar = Calendar.getInstance();
		CurentCalendar.setTimeInMillis(System.currentTimeMillis());

		int curentDay = CurentCalendar.get(Calendar.DAY_OF_YEAR);

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(parseLong);
		int day = calendar.get(Calendar.DAY_OF_YEAR);
		return curentDay == day;
	}

	public static boolean isStartHtml(String imgPath) {
		if (imgPath != null) {
			imgPath = imgPath.trim();
			return imgPath.startsWith("http") || imgPath.startsWith("https");
		}
		return false;
	}

	public static ApiParseCode getApiParseCode(String parseCode) {
		ApiParseCode apiParseCode = ApiParseCode.NONE;
		Map<String, ApiParseCode> map = new HashMap<String, ApiParseCode>();
		map.put("0", ApiParseCode.Success);
		map.put("1", ApiParseCode.Fail);
		map.put("2", ApiParseCode.Exist);
		map.put("3", ApiParseCode.NotExist);
		map.put("4", ApiParseCode.Reject);
		map.put("5", ApiParseCode.Loop);
		map.put("6", ApiParseCode.Registed);
		map.put("7", ApiParseCode.NotAccepted);
		map.put("8", ApiParseCode.SystemError);
		map.put("9", ApiParseCode.PhoneFormatInvalid);

		if (!CommonAndroid.isBlank(parseCode) && map.containsKey(parseCode)) {
			apiParseCode = map.get(parseCode);
		}

		return apiParseCode;
	}

	@SuppressLint("NewApi")
	public static void createNotification(Context context, int NOTIFY_ME_ID, String title) {
		final NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

		Intent intent = new Intent(context, EduSevice.class);
		intent.putExtra("api", "notification");
		intent.putExtra("id", "callscreen");
		intent.putExtra("title", title);
		PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);

		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
			// API 16 onwards
			Notification.Builder builder = new Notification.Builder(context);
			builder.setAutoCancel(false).setContentIntent(pendingIntent).setContentText(title).setContentTitle(context.getString(R.string.app_name)).setOngoing(true)
					.setSmallIcon(R.drawable.ic_launcher).setWhen(System.currentTimeMillis());
			Notification notification = builder.build();
			notification.flags = Notification.DEFAULT_LIGHTS & Notification.FLAG_AUTO_CANCEL;
			mNotificationManager.notify(NOTIFY_ME_ID, notification);
		} else {
			// API 15 and earlier
			NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
			builder.setAutoCancel(false).setContentIntent(pendingIntent).setContentText(title).setContentTitle(context.getString(R.string.app_name)).setOngoing(true)
					.setSmallIcon(R.drawable.ic_launcher).setWhen(System.currentTimeMillis());
			Notification notification = builder.getNotification();
			notification.flags = Notification.DEFAULT_LIGHTS & Notification.FLAG_AUTO_CANCEL;
			mNotificationManager.notify(NOTIFY_ME_ID, notification);
		}

		try {
			Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			Ringtone r = RingtoneManager.getRingtone(context, notification);
			r.play();
		} catch (Exception e) {
		}
	}

	public static String getTimeyyyMMddhhmmss() {

		// yyyyMMdd hh:mm:ss
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minus = calendar.get(Calendar.MINUTE);
		int ss = calendar.get(Calendar.SECOND);
		StringBuilder builder = new StringBuilder();
		builder.append(year);
		builder.append((month < 10 ? "0" : "")).append(month);
		builder.append((day < 10 ? "0" : "")).append(day);
		builder.append(" ");
		builder.append((hour < 10 ? "0" : "")).append(hour).append(":");
		builder.append((minus < 10 ? "0" : "")).append(minus).append(":");
		builder.append((ss < 10 ? "0" : "")).append(ss);
		return builder.toString();
	}

	public static boolean isDiem(String diem) {
		try {
			Float.valueOf(diem);
			return true;
		} catch (Exception exception) {
		}
		return false;
	}

	public static int getResColor(String hk1, Context context) {
		// • BẠC = Dành cho môn học có điểm Từ 7.0 và dƣới 8.0
		// • VÀNG = Dành cho môn học có điểm từ 8.0 và dƣới 9.0
		// • KIM CƢƠNG = Dành cho môn học có điểm từ 9.0 tới 10
		// • KO Có huy hiệu (Giống thƣờng) nếu điểm dƣới 7.0
		// (Design có thể theo 1 phong cách khác ấn tượng/Phù
		// hợp
		// hơn)
		int color = R.color.color_none;
		float diem = 0;
		try {
			diem = Float.valueOf(hk1);

		} catch (Exception e) {
		}
		if (diem >= 9.0f) {
			color = R.color.color_vang;
		} else if (diem >= 8.0f) {
			color = R.color.color_bac;
		} else if (diem >= 7.0f) {
			color = R.color.color_dong;
		}

		return context.getResources().getColor(color);
	}

	public static int getResDrawable(String hk1, Context context) {
		// • BẠC = Dành cho môn học có điểm Từ 7.0 và dƣới 8.0
		// • VÀNG = Dành cho môn học có điểm từ 8.0 và dƣới 9.0
		// • KIM CƢƠNG = Dành cho môn học có điểm từ 9.0 tới 10
		// • KO Có huy hiệu (Giống thƣờng) nếu điểm dƣới 7.0
		// (Design có thể theo 1 phong cách khác ấn tượng/Phù
		// hợp
		// hơn)
		int color = R.drawable.transfer;
		float diem = 0;
		try {
			diem = Float.valueOf(hk1);

		} catch (Exception e) {
		}
		if (diem >= 9.0f) {
			color = R.drawable.tvang;
		} else if (diem >= 8.0f) {
			color = R.drawable.tbac;
		} else if (diem >= 7.0f) {
			// color = R.drawable.tdong;
		}

		return color;
	}

	public static boolean isChuaCodiem(String diem) {
		float diemf = -1;
		try {
			diemf = Float.valueOf(diem);
		} catch (Exception e) {

		}
		return diemf == -1;
	}

	/**
	 * 
	 * @param context
	 * @param studentId
	 * @param sms_tiengviet
	 * @param type
	 *            0 --> inbox 1 --> diem hang ngay 2 --> bang diem tong ket
	 * @return
	 */
	public static String getPhanHoi(Context context, String studentId, String sms_tiengviet, int type, String NamHoc) {

		String studentName = new EduStudent(context).getStudenName(studentId);
		String namePhuHuynh = new EduAccount(context).getNameActive();

		String comment = null;

		if (type == 0 || type == 1) {
			if (CommonAndroid.isBlank(studentName)) {
				comment = String.format(context.getString(R.string.phuhuyenphanhoitinnhan), sms_tiengviet);
			} else {
				comment = String.format(context.getString(R.string.phuhuynhhocsinhphanhoi), sms_tiengviet);
			}
		} else if (type == 2) {
			comment = String.format(context.getString(R.string.phanhoibangdiemtongket), studentName);
		}

		return comment;
	}

	public static void setAsLink(View parent, int res, int str) {
		try {
			TextView textView = CommonAndroid.getView(parent, res);
			textView.setText(Html.fromHtml(String.format("<u>%s</u>", parent.getContext().getString(str))));
		} catch (Exception ex) {
		}
	}

	public static String getTitle(Context context, String studentId, String sms_tiengviet, int type, String NamHoc) {
		String studentName = new EduStudent(context).getStudenName(studentId);
		String namePhuHuynh = new EduAccount(context).getNameActive();
		String comment = "";
		if (type == 2) {
			comment = String.format(context.getString(R.string.title1), namePhuHuynh, NamHoc);
		} else {
			comment = String.format(context.getString(R.string.title2), namePhuHuynh);
		}

		return comment;
	}

	public static Bitmap getImgBangDiem(View v) {

		// ;
		// Bitmap b = Bitmap.createBitmap(v.getLayoutParams().width,
		// v.getLayoutParams().height, Bitmap.Config.ARGB_8888);
		// Canvas c = new Canvas(b);
		// v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
		// v.draw(c);

		// v.setDrawingCacheEnabled(true);
		// Bitmap bitmap = v.getDrawingCache();
		// v.setDrawingCacheEnabled(false);

		return CommonAndroid.getImgBangDiem(v);
	}
}