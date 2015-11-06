package vnp.mr.mintmedical.fragment;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;

import vnp.mr.mintmedical.DateUtils;
import vnp.mr.mintmedical.R;
import vnp.mr.mintmedical.S4ManagerActivity;
import vnp.mr.mintmedical.base.MBaseFragment;
import vnp.mr.mintmedical.base.MintUtils;
import vnp.mr.mintmedical.item.Clinic;
import vnp.mr.mintmedical.item.Doctor;
import vnp.mr.mintmedical.item.DoctorEvent;
import vnp.mr.mintmedical.item.S4DItem;
import vnp.mr.mintmedical.item.S4Item;
import vnp.mr.mintmedical.service.VNPLocationUtils;
import vnp.mr.mintmedical.view.AvatarView;
import android.content.Context;
import android.location.Location;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.internal.ie;
import com.google.android.gms.maps.model.LatLng;
import com.viewpagerindicator.db.DBClinic;
import com.viewpagerindicator.db.DBConfig;
import com.viewpagerindicator.db.DBDoctor;
import com.viewpagerindicator.db.DBDoctorEvent;
import com.viewpagerindicator.db.DBS4DItem;
import com.vnp.core.callback.ExeCallBack;
import com.vnp.core.callback.ExeCallBackOption;
import com.vnp.core.callback.ResClientCallBack;
import com.vnp.core.common.BaseAdapter;
import com.vnp.core.common.BaseAdapter.CommonGenderView;
import com.vnp.core.service.RestClient;
import com.vnp.core.view.CustomLinearLayoutView;

public class S4D extends MBaseFragment {
	// Appointment
	private ListView listView;

	@Override
	public void onResume() {
		super.onResume();
		listView = (ListView) findViewById(R.id.s4dlistView);

		udapteUi();

	}

	private void udapteUi() {
		// List<Object> list = new ArrayList<Object>();

		// type = 1 : A-> B -> C -> D -> E -> F
		// 2 : A-> B -> G -> C -> D -> E -> F
		// 3 : A-> H -> B -> C -> D -> E -> F
		String url = MintUtils.URL_AVAILABILITY;
		url = String.format(url, dataSendToServer.typeS4BVisitType,
				dataSendToServer.typeS4AAppointment);
		if (dataSendToServer.typeS4AAppointment == 1) {
			// all Doctor Event
			// DBDoctor dbDoctor = new DBDoctor(getActivity());
			// List<Object> doctors = (List<Object>) dbDoctor.getData();
			// for (Object object : doctors) {
			// DBDoctorEvent dbDoctorEvent = new DBDoctorEvent(getActivity(),
			// ((Doctor) object).id);
			// list.addAll((List<Object>) dbDoctorEvent.getData());
			// }
		} else if (dataSendToServer.typeS4AAppointment == 2) {
			String idOffice = dataSendToServer.typeS4GSelectOffice.id;
			// all Event of clinc
			url = url + "&office_id=" + idOffice;
			// String idOffice = dataSendToServer.typeS4GSelectOffice.id;
			// DBDoctor dbDoctor = new DBDoctor(getActivity());
			// List<Object> doctors = (List<Object>) dbDoctor.getData();
			// for (Object object : doctors) {
			// String idDoctor = ((Doctor) object).id;
			// String idOfficeOfDoctor = ((Doctor) object).clinic_id;
			// if (idOfficeOfDoctor != null &&
			// idOfficeOfDoctor.trim().equals(idOffice)) {
			// DBDoctorEvent dbDoctorEvent = new DBDoctorEvent(getActivity(),
			// idDoctor);
			// list.addAll((List<Object>) dbDoctorEvent.getData());
			// }
			// }
		} else if (dataSendToServer.typeS4AAppointment == 3) {
			Doctor doctor = dataSendToServer.typeS4HSelectDoctor;
			String idDoctor = doctor.id;
			// all event of a doctor
			url = url + "&doctor_id=" + idDoctor;
			// Doctor doctor = dataSendToServer.typeS4HSelectDoctor;
			// String idDoctor = doctor.id;
			// DBDoctorEvent dbDoctorEvent = new DBDoctorEvent(getActivity(),
			// idDoctor);
			// list.addAll((List<Object>) dbDoctorEvent.getData());
		}

		final String URL = url;
		ExeCallBack exeCallBack = new ExeCallBack();
		exeCallBack.setExeCallBackOption(new ExeCallBackOption(getActivity(),
				true, R.string.loadding));
		exeCallBack.executeAsynCallBack(new ResClientCallBack() {
			@Override
			public void onCallBack(Object arg0) {
				RestClient restClient = (RestClient) arg0;
				if (restClient.getResponseCode() == 200) {
					String data = restClient.getResponse();
					DBS4DItem dbs4dItem = new DBS4DItem(getActivity());
					dbs4dItem.save(data);

					List<Object> list = (List<Object>) dbs4dItem.getData();

//					S4DItem item = new S4DItem(null);
//					item.visit_type = "1";
//					item.appointment_type = "1";
//					item.avatar = null;
//					item.doctor_id = "1";
//					item.doctor_level = "Master";
//					item.fullname = "Abc";
//					item.id = "1";
//					item.time = "2014-11-10";
//					item.start = "2014-11-10";
//					item.latitude = "0";
//					item.longitude = "0";
//					item.office_id = "1";
//					item.title = "aaaa";
//					item.office_address = "Viet name, ha noi";
//					list.add(item);
//					list.add(item);
//					list.add(item);
//					list.add(item);
//					list.add(item);
//					list.add(item);
//					list.add(item);
//					list.add(item);
//					list.add(item);
					updateUI(list);
				}
			}

			@Override
			public String getUrl() {
				return URL;
			}
		});

	}

	private void updateUI(List<Object> list) {
		listView.setAdapter(new BaseAdapter(getActivity(), list,
				new CommonGenderView() {
					@Override
					public CustomLinearLayoutView getView(Context arg0,
							Object arg1) {
						return new S4DItemView(arg0);
					}
				}) {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = super.getView(position, convertView, parent);

				if (position == 0) {
					((S4DItemView) view).showHeadder(true);
				} else {
					String start1 = ((S4DItem) getItem(position)).time;
					String start2 = ((S4DItem) getItem(position - 1)).time;
					if (start1.equals(start2)) {
						// if
						// (DateUtils.getYYYYMMDD(start1).equals(DateUtils.getYYYYMMDD(start2)))
						// {
						((S4DItemView) view).showHeadder(false);
					} else {
						((S4DItemView) view).showHeadder(true);
					}
				}
				return view;
			}
		});

		setupListView(listView);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				getOnClickListener().onClick(
						parent.getItemAtPosition(position), 0);
			}
		});
	}

	@Override
	public int getLayout() {
		return R.layout.s4d;
	}

	@Override
	public int getHeaderRes() {
		return R.string.s4d_header;
	}

	private class S4DItemView extends CustomLinearLayoutView {

		public S4DItemView(Context context) {
			super(context);
			init(R.layout.s4ditemview);
		}

		public void showHeadder(boolean b) {
			findViewById(R.id.s4ditemview_header).setVisibility(
					b ? View.VISIBLE : View.GONE);
		}

		@Override
		protected void onAttachedToWindow() {
			super.onAttachedToWindow();
			resize.resizeSacle(this, LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT);

			resize.resizeSacle(findViewById(R.id.s4ditem_1), 300,
					LayoutParams.WRAP_CONTENT);
			resize.setTextsize(findViewById(R.id.s4ditemview_textheader),
					MintUtils.TEXTSIZE_S4_HEADER);

			resize.setTextsize(findViewById(R.id.s4ditemview_textview1),
					MintUtils.TEXTSIZE_ITEM1);
			resize.setTextsize(findViewById(R.id.TextView01),
					MintUtils.TEXTSIZE_ITEM1);
			
			resize.setTextsize(findViewById(R.id.s4ditemview_textview2),
					MintUtils.TEXTSIZE_ITEM2);
			resize.setTextsize(findViewById(R.id.TextView02),
					MintUtils.TEXTSIZE_ITEM2);
			
			resize.setTextsize(findViewById(R.id.s4ditemview_textview3),
					
					
					MintUtils.TEXTSIZE_ITEM2);
			resize.setTextsize(findViewById(R.id.s4ditemview_textview4),
					MintUtils.TEXTSIZE_ITEM2);

			resize.resizeSacle(findViewById(R.id.s4ditemview_header), 300,
					LayoutParams.WRAP_CONTENT);
			resize.resizeSacle(findViewById(R.id.s4itemview_content_main), 300,
					LayoutParams.WRAP_CONTENT);
			resize.resizeSacle(findViewById(R.id.s4ditemview_img2),
					MintUtils.IMGITEM_WIDTH2, MintUtils.IMGITEM_WIDTH2);
		}

		@Override
		public void setGender() {
			S4DItem item = (S4DItem) getData();
			// DoctorEvent item = (DoctorEvent) getData();
			// String doctorID = item.doctor_id;
			// Doctor doctor = (Doctor) new DBDoctor(getContext())
			// .getData(item.doctor_id);
			// if (doctor == null)
			// doctor = new Doctor();
			// Clinic clinic = (Clinic) new DBClinic(getContext())
			// .getData(doctor.clinic_id);
			// if (clinic == null)
			// clinic = new Clinic();

			setText(R.id.s4ditemview_textheader, DateUtils.getDate(item.start));
			setText(R.id.s4ditemview_textview1, DateUtils.getHHMM(item.start));
			setText(R.id.s4ditemview_textview2,
					String.format("%s ,%s", item.fullname, item.doctor_level));
			setText(R.id.s4ditemview_textview3, item.office_address);

			AvatarView avatarView = (AvatarView) findViewById(R.id.s4ditemviewavatarView1);
			avatarView.loadAvartar(item.avatar);
			setText(R.id.s4ditemview_textview4, "");

			try {
				Location last = VNPLocationUtils.getInstance().lastKnownLocation;
				LatLng latLng1 = new LatLng(last.getLatitude(),
						last.getLongitude());
				LatLng latLng2 = new LatLng(Double.parseDouble(item.latitude),
						Double.parseDouble(item.longitude));
				setText(R.id.s4ditemview_textview4,
						String.format("%s km",
								VNPLocationUtils.getDistance(latLng1, latLng2)));
			} catch (Exception exception) {
				setText(R.id.s4ditemview_textview4, String.format("100 km"));
			}
		}

		private void setText(int s4ditemviewTextheader, String day) {
			((TextView) findViewById(s4ditemviewTextheader)).setText(day);
		}
	}

	S4ManagerActivity.DataSendToServer dataSendToServer;

	public void setDataSendToServer(
			S4ManagerActivity.DataSendToServer dataSendToServer) {
		this.dataSendToServer = dataSendToServer;
	}

}