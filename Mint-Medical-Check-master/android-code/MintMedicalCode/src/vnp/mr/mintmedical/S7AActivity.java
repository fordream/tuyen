package vnp.mr.mintmedical;

import java.util.ArrayList;
import java.util.List;

import vnp.mr.mintmedical.base.MintUtils;
import vnp.mr.mintmedical.fragment.MapFagment;
import vnp.mr.mintmedical.item.Clinic;
import vnp.mr.mintmedical.service.MintService;
import vnp.mr.mintmedical.service.VNPLocationUtils;
import vnp.mr.mintmedical.view.AvatarView;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.viewpagerindicator.db.DBClinic;
import com.viewpagerindicator.db.DBUserLogin;
import com.vnp.core.common.BaseAdapter;
import com.vnp.core.common.BaseAdapter.CommonGenderView;
import com.vnp.core.common.VNPResize;
import com.vnp.core.view.CustomLinearLayoutView;

@SuppressLint("ValidFragment")
public class S7AActivity extends FragmentActivity implements
		OnItemClickListener {
	private HeaderView headerView;
	public VNPResize resize = VNPResize.getInstance();
	private Clinic clinic;

	public <T extends View> T getView(int res) {
		@SuppressWarnings("unchecked")
		T view = (T) findViewById(res);
		return view;
	}

	public void setupListView(View view) {
		if (view != null && view instanceof ListView) {
			ListView listView = (ListView) view;
			listView.setDivider(null);
			listView.setSelector(R.drawable.listview_selected);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(getLayout());

		clinic = (Clinic) new DBClinic(this).getData(getIntent()
				.getStringExtra("id"));

		headerView = getView(R.id.aboutactivity_headerview);
		headerView.updateText(R.string.office);
		headerView.showButton(true, false);
		headerView.setOnClickListenerButtonBack(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		ListView listView = getView(R.id.aboutactivity_listview);
		listView.setOnItemClickListener(this);
		setupListView(listView);
		listView.setAdapter(new BaseAdapter(this, getlData(),
				new CommonGenderView() {
					@Override
					public CustomLinearLayoutView getView(Context context,
							Object data) {
						return new AboutActivityItemView(context);
					}
				}));

		changeFragemtn(R.id.aboutactivity_img, new S7AMapFragment());

		if (clinic != null) {
			((TextView) findViewById(R.id.s7atext1)).setText(clinic.name);
			((TextView) findViewById(R.id.s7atext2)).setText(clinic.address);
		} else {
			((TextView) findViewById(R.id.s7atext1)).setText("");
			((TextView) findViewById(R.id.s7atext2)).setText("");
		}
	}

	private List<Object> getlData() {

		List<Object> data = new ArrayList<Object>();
		Intent intent = new Intent(this, S4ManagerActivity.class);
		if (clinic != null)
			intent.putExtra("id", clinic.id);
		data.add(new S7Item(R.drawable.s7a1, "Book an Appointment", null,
				intent));
		Intent intentCall = null;
		if (clinic != null) {
			String url = "tel:" + clinic.phone;
			intentCall = new Intent(Intent.ACTION_CALL, Uri.parse(url));
		}

		data.add(new S7Item(R.drawable.s7a2, "Call this Office", "", intentCall));

		Intent intentDirections = new Intent(android.content.Intent.ACTION_VIEW);

		if (clinic != null) {
			String url = "http://maps.google.com/maps?saddr=%s,%s&daddr=%s,%s&mode=driving";

			Location a = VNPLocationUtils.getInstance().lastKnownLocation;
			String lat = "";
			String lon = "";
			if (a != null) {
				lat = a.getLatitude() + "";
				lon = a.getLongitude() + "";
			}

			url = String.format(url, lat, lon, clinic.latitude,
					clinic.longitude);

			intentDirections = new Intent(android.content.Intent.ACTION_VIEW,
					Uri.parse(url));
		}

		data.add(new S7Item(R.drawable.s7a3, "Deiving Directions", "",
				intentDirections));

		return data;
	}

	public void changeFragemtn(int r_id_content_frame, Fragment rFragment) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction ft = fragmentManager.beginTransaction();
		ft.replace(r_id_content_frame, rFragment);
		ft.commit();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// resize.resizeSacle(findViewById(R.id.aboutactivity_main), 300,
		// android.widget.LinearLayout.LayoutParams.MATCH_PARENT);
		resize.resizeSacle(findViewById(R.id.aboutactivity_img),
				LayoutParams.MATCH_PARENT, 200);
		resize.resizeSacle(findViewById(R.id.s7a_content1), 300,
				android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
		resize.setTextsize(findViewById(R.id.s7atext1),
				MintUtils.TEXTSIZE_ITEM1);
		resize.setTextsize(findViewById(R.id.s7atext2),
				MintUtils.TEXTSIZE_ITEM2);

	}

	public int getLayout() {
		return R.layout.s7aativity;
	}

	private class AboutActivityItemView extends CustomLinearLayoutView {
		AvatarView avatarView;

		public AboutActivityItemView(Context context) {
			super(context);
			init(R.layout.s7aitemview);
		}

		public AboutActivityItemView(Context context, AttributeSet attrs) {
			super(context, attrs);
			init(R.layout.s7aitemview);
		}

		@Override
		public void init(int res) {
			super.init(res);
			avatarView = (AvatarView) findViewById(R.id.avatarView1);
			avatarView.setType(3);
		}

		@Override
		protected void onAttachedToWindow() {
			super.onAttachedToWindow();
			resize.resizeSacle(findViewById(R.id.s7itemview_main), 300,
					LayoutParams.WRAP_CONTENT);

			resize.resizeSacle(findViewById(R.id.aboutactivityitemview_ll1),
					300, 50);

			resize.resizeSacle(findViewById(R.id.aboutactivityitemview_img2),
					MintUtils.IMGITEM_WIDTH2, MintUtils.IMGITEM_WIDTH2);

			resize.resizeSacle(findViewById(R.id.aboutactivityitemview_text1),
					287 - 40, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);

			resize.setTextsize(findViewById(R.id.aboutactivityitemview_text1),
					MintUtils.TEXTSIZE_ITEM1);
		}

		@Override
		public void setGender() {
			if (getData() instanceof S7Item) {
				S7Item item = (S7Item) getData();
				((TextView) findViewById(R.id.aboutactivityitemview_text1))
						.setText(item.header);
				avatarView.loadAvartar(item.res);
			}
		}
	}

	private class S7Item {
		int res;
		String header;
		String suHeader;
		Intent intent;

		public S7Item(int i, String string, String string2, Intent intent) {
			res = i;
			header = string;
			this.intent = intent;
			suHeader = string2;

		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		try {

			S7Item item = (S7Item) parent.getItemAtPosition(position);

			if (item.res == R.drawable.s7a1) {
				DBUserLogin dbUserLogin = new DBUserLogin(this);

				if (!dbUserLogin.isLogin()) {
					DialogInterface.OnClickListener onClick = new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Toast.makeText(S7AActivity.this, "login success",
									Toast.LENGTH_SHORT).show();
						}
					};
					new S3Activity(this, onClick).show();

					return;
				}
			}
			startActivity(item.intent);
		} catch (Exception exception) {

		}
	}

	@SuppressLint("ValidFragment")
	private class S7AMapFragment extends MapFagment {

		@Override
		public void onResume() {
			super.onResume();

			if (clinic != null) {
				BitmapDescriptor icon = null;

				try {
					icon = BitmapDescriptorFactory
							.fromResource(R.drawable.s7bpoint);
				} catch (Exception exception) {

				}
				addMaker(clinic.latitude, clinic.longitude, icon);
				gotoPosition(clinic.latitude, clinic.longitude);
			}
		}
	}
}