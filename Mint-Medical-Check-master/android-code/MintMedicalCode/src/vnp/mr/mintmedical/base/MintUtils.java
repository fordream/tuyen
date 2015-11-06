package vnp.mr.mintmedical.base;

public class MintUtils {
	// XXX http://medical.99s.vn/service/get_doctor
	// XXX http://medical.99s.vn/service/get_clinic
	// XXX//http://medical.99s.vn/service/login_customer?email=tu@gmail.com&password=e10adc3949ba59abbe56e057f20f883e
	// http://medical.99s.vn/service/get_events/?doctor_id=20&limit=1

	// http://medical.99s.vn/service/get_availability?doctor_id=22
	// http://medical.99s.vn/service/get_availability?office_id=22
	// http://medical.99s.vn/service/get_availability?doctor_id=22&visit_type=1&appointment_type=1
	// http://medical.99s.vn/service/get_config
	// http://medical.99s.vn/service/get_home

	/**
	 * nmtdhbk@yahoo.com [2:03:24 PM] Shoppie nmtdhbk: pass 123456 nhé [2:03:45
	 * PM] Shoppie nmtdhbk:
	 * http://medical.99s.vn/admin/account/login?return_url=
	 * http://medical.99s.vn/admin
	 */
	public static final String URL_AVAILABILITY = "http://medical.99s.vn/service/get_availability?visit_type=%s&appointment_type=%s";
	public static final String URL_VISITYPE = "http://medical.99s.vn/service/get_config";
	public static final String BASEURL = "http://medical.99s.vn/service/%s";
	public static final String URL_GETEVENT = String.format(BASEURL,
			"get_events");;
	public static final String URL_LOGIN = String.format(BASEURL,
			"login_customer");

	public static final String URL_GETDOCTOR = String.format(BASEURL,
			"get_doctor");
	public static final String URL_GETCLINIC = String.format(BASEURL,
			"get_clinic");
	public static final String URL_MAP = String.format(BASEURL, "map");
	public static final String URL_HOME = String.format(BASEURL, "get_home");
	public static final int TEXTSIZE_BUTTON = 18;
	public static final int TEXTSIZE_EDITTEXT = 18;
	public static final int HEIGHT_BUTTON = 40;
	public static final int HEIGHT_EDITTEXT = 40;
	public static final int IMGITEM_WIDTH = 50;
	public static final int IMGITEM_WIDTH2 = 13;
	public static final int TEXTSIZE_ITEM1 = 20;
	public static final int TEXTSIZE_ITEM2 = 10;
	public static final int TEXTSIZE_ITEM3 = 8;
	public static final int TEXTSIZE_S4_HEADER = 20;
	public static final int TEXTSIZE_ACCOUNT_HEADER = 25;
	public static final int TEXTSIZE_ACCOUNT1 = 18;

}