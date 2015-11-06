package vnp.mr.mintmedical;

public class DateUtils {
	public static String getDate(String str) {
		try {
			int index = 0;
			if (str.toLowerCase().contains("July".toLowerCase())) {
				index = 1;
			}

			return str.substring(0, index + 15);
		} catch (Exception exception) {
			return str;
		}
	}

	public static final String getYYYYMMDD(String date) {
		try {
			return getYear(date) + getMonth(date) + getDay(date);

		} catch (Exception exception) {
			exception.printStackTrace();
		}

		return "";
	}

	public static String getHHMM(String str) {
		try{
		int index = 0;
		if (str.toLowerCase().contains("July".toLowerCase())) {
			index = 1;
		}
		return str.substring(index + 16, index + 21);
		}catch(Exception ewException){
			return str;
		}
	}

	private static String getYear(String str) {
		int index = 0;
		if (str.toLowerCase().contains("July".toLowerCase())) {
			index = 1;
		}

		return str.substring(index + 11, index + 15);
	}

	private static String getDay(String str) {
		int index = 0;
		if (str.toLowerCase().contains("July".toLowerCase())) {
			index = 1;
		}
		return str.substring(index + 8, index + 10);
	}

	private static String getMonth(String str) {
		String[] months = new String[] { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", // July
				"Aug", "Sep", "Oct", "Nov", "Dec" };
		for (int i = 0; i < months.length; i++) {
			if (str.toLowerCase().contains(months[i].toLowerCase())) {
				return i < 9 ? "0" + (i + 1) : (i + 1) + "";
			}
		}

		return 0 + "";

	}
}
