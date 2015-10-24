package vnp.org.moit.db;

import android.content.Context;

import com.acv.cheerz.db.DataStore;

public class ServerStatus {
	private static ServerStatus in = new ServerStatus();

	public static ServerStatus getIn() {
		return in;
	}

	private Context context;
	public static final String LOADMORETINTUC = "LOADMORETINTUC";
	public static final String LOADMORESMS = "LOADMORESMS";
	public static final String LOADMOREDIENDAN = "LOADMOREDIENDAN";
	public static final String LOADMOREMarkTableGet = "LOADMOREMarkTableGet";

	private ServerStatus() {

	}

	public void set(String key, boolean value) {
		DataStore.getInstance().init(context);
		DataStore.getInstance().save(key, value);
	}

	public boolean isLoaded(String key) {
		DataStore.getInstance().init(context);
		return DataStore.getInstance().get(key, false);
	}

	public void init(Context context) {
		this.context = context;
	}

	public void clear() {
		set(LOADMORETINTUC, false);
		set(LOADMORESMS, false);
		set(LOADMOREDIENDAN, false);
	}

	public void setAllLoadMore() {
		set(LOADMORETINTUC, true);
		set(LOADMORESMS, true);
		set(LOADMOREDIENDAN, true);
	}
}