package vnp.mr.mintmedical;

import java.util.ArrayList;
import java.util.List;

import vnp.mr.mintmedical.fragment.MapFagment;
import vnp.mr.mintmedical.item.Clinic;
import vnp.mr.mintmedical.view.ListViewHeader;
import vnp.mr.mintmedical.view.OfficeItemView;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.viewpagerindicator.db.DBClinic;
import com.vnp.core.common.BaseAdapter;
import com.vnp.core.common.BaseAdapter.CommonGenderView;
import com.vnp.core.view.CustomLinearLayoutView;

@SuppressLint("ValidFragment")
public class S7Activity extends FragmentActivity {
	private ListViewHeader listView;
	private MapFagment mapFagment;

	public int getLayout() {
		return R.layout.mapativity;
	}

	public void setupListView(View view) {
		if (view != null && view instanceof ListView) {
			ListView listView = (ListView) view;
			listView.setDivider(null);
			listView.setSelector(R.drawable.listview_selected);
		}
	}

	@SuppressLint("ValidFragment")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(getLayout());
		HeaderView headerView = (HeaderView) findViewById(R.id.mapactivity_headerview);
		headerView.updateText(R.string.blank);
		headerView.showButton(true, false);
		headerView.setOnClickListenerButtonBack(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		headerView.showControlMap(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				boolean isList = !((RadioButton) group.getChildAt(0))
						.isChecked();
				if (!isList) {
					findViewById(R.id.mapactivity_listviewheader)
							.setVisibility(View.VISIBLE);
					findViewById(R.id.mapactivity_frame).setVisibility(
							View.GONE);
				} else {
					findViewById(R.id.mapactivity_listviewheader)
							.setVisibility(View.GONE);
					findViewById(R.id.mapactivity_frame).setVisibility(
							View.VISIBLE);
				}
			}
		});

		listView = (ListViewHeader) findViewById(R.id.mapactivity_listviewheader);
		setupListView(listView);
		changeFragemtn(R.id.mapactivity_frame, mapFagment = new S7MapFragment());

		updateUi();
	}

	private void updateUi() {
		listView.setAdapter(new BaseAdapter(this, (List<Object>) new DBClinic(
				this).getData(), new CommonGenderView() {
			@Override
			public CustomLinearLayoutView getView(Context arg0, Object arg1) {
				return new OfficeItemView(arg0);
			}
		}));

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Clinic item = (Clinic) parent.getItemAtPosition(position);
				toClins(item.id);
			}
		});
	}

	private void toClins(String id) {
		Intent intent = new Intent(S7Activity.this, S7AActivity.class);
		intent.putExtra("id", id);
		startActivity(intent);
	}

	private ProgressDialog dialog;

	public void showDialogLoading(boolean isShow) {
		if (isShow) {
			if (dialog == null) {
				dialog = ProgressDialog.show(this, null,
						getString(R.string.loadding));
			}
		} else {
			if (dialog != null) {
				dialog.dismiss();
				dialog = null;
			}
		}
	}

	public void changeFragemtn(int r_id_content_frame, Fragment rFragment) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction ft = fragmentManager.beginTransaction();
		ft.replace(r_id_content_frame, rFragment);
		ft.commit();
	}

	private class S7MapFragment extends MapFagment {
		private class S7MapItem {
			Clinic clinic;
			MarkerOptions markerOptions;
		}

		@SuppressLint("ValidFragment")
		@Override
		public void onResume() {
			super.onResume();
			clearMap();
			gotoMyPosition();
			BitmapDescriptor icon = null;

			try {
				icon = BitmapDescriptorFactory
						.fromResource(R.drawable.s7apoint);
			} catch (Exception exception) {

			}
			List<Object> list = (List<Object>) new DBClinic(getActivity())
					.getData();
			for (Object object : list) {
				Clinic clinic = (Clinic) object;
				addMaker(clinic);
			}
		}

		@Override
		public void onInfoWindowClick(Marker arg0) {
			super.onInfoWindowClick(arg0);
			List<Object> list = (List<Object>) new DBClinic(getActivity())
					.getData();
			for (Object object : list) {
				Clinic clinic = (Clinic) object;
				
				if(clinic.name.equals(arg0.getTitle())){
					toClins(clinic.id);
					break;
				}
			}
		}
	}
}