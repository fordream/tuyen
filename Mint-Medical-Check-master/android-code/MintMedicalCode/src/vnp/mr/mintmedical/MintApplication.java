package vnp.mr.mintmedical;

import vnp.mr.mintmedical.service.MinDatabase;
import vnp.mr.mintmedical.service.VNPLocationUtils;

import com.vnp.core.activity.BaseApplication;
import com.vnp.core.datastore.DataStore;

public class MintApplication extends BaseApplication {

	@Override
	public void onCreate() {
		super.onCreate();
		MinDatabase.getInstance().init(this);
		DataStore.getInstance().init(this);
		VNPLocationUtils.getInstance().init(this);
		VNPLocationUtils.getInstance().requestLocationUpdate();
		
	}
}
