package vnp.mr.mintmedical;

import java.util.ArrayList;
import java.util.List;

import vnp.mr.mintmedical.base.MBaseActivity;
import vnp.mr.mintmedical.service.MintService;
import vnp.mr.mintmedical.view.S2ItemView;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.viewpagerindicator.db.DBHome;
import com.vnp.core.common.BaseAdapter;
import com.vnp.core.common.BaseAdapter.CommonGenderView;
import com.vnp.core.view.CustomLinearLayoutView;

public class S2Activity extends MBaseActivity {
	private static final int REQUESTLOGIN = 0;
	private ListView listView;
	private HeaderView headerView;

	@Override
	public int getLayout() {
		return R.layout.mainactivity;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		registerNofitication(true);

		headerView = getView(R.id.activitymain_headerview);

		headerView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (dbUserLogin.isLogin()) {
					logout();
				} else {
					login();
				}
			}
		});

		headerView.updateButton(dbUserLogin.isLogin());
		listView = getView(R.id.mainactivity_listview);

		List<Object> list = new ArrayList<Object>();
		templete(list);

		listView.setAdapter(new BaseAdapter(this, list, new CommonGenderView() {
			@Override
			public CustomLinearLayoutView getView(Context context, Object data) {
				return new S2ItemView(context);
			}
		}));

		setupListView(listView);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				S2Item item = (S2Item) parent.getItemAtPosition(position);
				if (item.res == R.drawable.main_img_7
						|| item.res == R.drawable.main_img_4) {
					gotoPage(item);
				} else {
					if (dbUserLogin.isLogin()) {
						gotoPage(item);
					} else {
						login();
					}
				}
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	protected void login() {
		DialogInterface.OnClickListener onClick = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				headerView.updateButton(dbUserLogin.isLogin());
				MintService.startService(S2Activity.this);
				loadHome();
			}

		};
		new S3Activity(this, onClick).show();
	}

	@Override
	protected void onResume() {
		super.onResume();
		loadHome();
	}

	private void logout() {
		dbUserLogin.saveLogin(false);
		headerView.updateButton(dbUserLogin.isLogin());
		startActivity(new Intent(this, S1Activity.class));
		finish();
	}

	private void gotoPage(S2Item item) {
		startActivity(new Intent(this, item.clazz));
	}

	private void templete(List<Object> list) {
		list.add(new S2Item(R.drawable.main_img_1, "Appointments",
				"Make or Cancel", S4Activity.class));
		list.add(new S2Item(R.drawable.main_img_2, "Treat me Now",
				"Request treatment for common issues", S5Activity.class));
		list.add(new S2Item(R.drawable.main_img_3, "Prescriptions",
				"Request prescription renewal", S6Activity.class));
		list.add(new S2Item(R.drawable.main_img_4, "Locations", null,
				S7Activity.class));
		list.add(new S2Item(R.drawable.main_img_5, "Contact",
				"Send email or call", S8Activity.class));
		list.add(new S2Item(R.drawable.main_img_6, "My Account",
				"View or update profile", S9Activity.class));
		list.add(new S2Item(R.drawable.main_img_7, "About Mint Medical", null,
				S10Activity.class));

		list.add(new S2Item(R.drawable.main_img_8, "New And Alerts", "",
				S11Activity.class));
	}

	private void registerNofitication(boolean isRegister) {
		if (isRegister) {
			GcmBroadcastReceiver.register(this);
		}
	}

	public class S2Item {
		public int res;
		public String header;
		public String suHeader;
		public Class clazz;

		public S2Item(int i, String string, String string2, Class clazz) {
			res = i;
			header = string;
			suHeader = string2;
			this.clazz = clazz;
		}
	}

	private void loadHome() {
		new DBHome(this).exeCallBack();
	}
}