package one.edu.vn.sms;

import org.com.atmarkcafe.sky.ParseNotificationUtils;

import vnp.org.moit.db.ServerStatus;

import com.vnp.core.activity.BaseApplication;
import com.vnp.core.common.CommonAndroid;
import com.vnp.core.common.FontsUtils;
import com.vnp.core.common.ImageLoader;
import com.vnp.core.common.LogUtils;
import com.vnp.core.gcm.GcmBroadcastReceiver;

public class MoITApplication extends BaseApplication {
	@Override
	public void onCreate() {
		super.onCreate();

		ImageLoader.getInstance(this);
		new ParseNotificationUtils().init(this);

		ServerStatus.getIn().init(this);

		GcmBroadcastReceiver.setSenderId("804255416582", this);

		/**
		 * fonts
		 */
		CommonAndroid.FONT.getInstance().init(this);
		CommonAndroid.FONT.getInstance().addTypeFaces("font/OpenSans-BoldItalic.ttf");
		CommonAndroid.FONT.getInstance().addTypeFaces("font/OpenSans-ExtraBold.ttf");
		CommonAndroid.FONT.getInstance().addTypeFaces("font/OpenSans-ExtraBoldItalic.ttf");
		CommonAndroid.FONT.getInstance().addTypeFaces("font/OpenSans-Italic.ttf");
		CommonAndroid.FONT.getInstance().addTypeFaces("font/OpenSans-Light.ttf");
		CommonAndroid.FONT.getInstance().addTypeFaces("font/OpenSans-LightItalic.ttf");
		CommonAndroid.FONT.getInstance().addTypeFaces("font/OpenSans-Bold.ttf");//
		CommonAndroid.FONT.getInstance().addTypeFaces("font/OpenSans-Regular.ttf");//
		CommonAndroid.FONT.getInstance().addTypeFaces("font/OpenSans-Semibold.ttf");//
		CommonAndroid.FONT.getInstance().addTypeFaces("font/OpenSans-SemiboldItalic.ttf");

		/**
		 * Change default font
		 */

		FontsUtils.getInstance().init(this);
		
		FontsUtils.getInstance().onCreateForChangeDefault("SANS", "font/OpenSans-Light.ttf");
		FontsUtils.getInstance().onCreateForChangeDefault("SERIF", "font/OpenSans-Semibold.ttf");
		FontsUtils.getInstance().onCreateForChangeDefault("MONOSPACE", "font/OpenSans-Bold.ttf");
	}
}