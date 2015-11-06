package vnp.mr.mintmedical;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import vnp.mr.mintmedical.base.MBaseActivity;
import vnp.mr.mintmedical.base.MintUtils;
import vnp.mr.mintmedical.item.Clinic;
import vnp.mr.mintmedical.item.Doctor;
import vnp.mr.mintmedical.item.DoctorEvent;
import vnp.mr.mintmedical.item.S4Item;
import vnp.mr.mintmedical.service.VNPLocationUtils;
import vnp.mr.mintmedical.view.AvatarView;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.viewpagerindicator.db.DBClinic;
import com.viewpagerindicator.db.DBDoctor;
import com.viewpagerindicator.db.DBDoctorEvent;
import com.viewpagerindicator.db.DBS4Appointment;
import com.vnp.core.common.BaseAdapter;
import com.vnp.core.common.BaseAdapter.CommonGenderView;
import com.vnp.core.common.ImageLoaderUtils;
import com.vnp.core.view.CustomLinearLayoutView;

public class S4Activity extends MBaseActivity {
	// Appointment
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		HeaderView headerView = getView(R.id.activitymain_headerview);
		OnClickListener l = new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(S4Activity.this,
						S4ManagerActivity.class));
			}
		};
		headerView.updateTextButtonRight(R.string.s4new);
		headerView.setOnClickListener(l);
		findViewById(R.id.s4_button1).setOnClickListener(l);

		headerView.showButton(true, true);
		headerView.updateText(R.string.s4);

		headerView.setOnClickListenerButtonBack(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (dbUserLogin.isLogin()) {
			// s4first
			// R.id.s4late; as S4I
			// TODO
		}

		resize.resizeSacle(findViewById(R.id.s4first), 300,
				LayoutParams.MATCH_PARENT);

		resize.resizeSacle(findViewById(R.id.s4first1),
				LayoutParams.MATCH_PARENT, 180);

		resize.resizeSacle(findViewById(R.id.s4_button1), 530 / 2, 90 / 2);
		resize.resizeSacle(findViewById(R.id.s4_text2), 580 / 2, 192 / 2);
		resize.setTextsize(findViewById(R.id.s4_text1),
				MintUtils.TEXTSIZE_ITEM2 + 8);
		// resize.setTextsize(findViewById(R.id.s4_text2), 25);
		// resize.setTextsize(findViewById(R.id.s4_text3), 15);

		// s4first
		// s4_listview
		//
		DBS4Appointment appointment = new DBS4Appointment(this);
		List<Object> listData = (List<Object>) appointment.getData();
		List<Object> list = new ArrayList<Object>();

		List<Object> doctorList = (List<Object>) new DBDoctor(this).getData();
		List<Object> clinsList = (List<Object>) new DBClinic(this).getData();
		List<Object> eventList = new ArrayList<Object>();
		for (Object doctor : doctorList) {
			eventList.addAll((List<Object>) new DBDoctorEvent(this,
					((Doctor) doctor).id).getData());
		}

		for (Object object : listData) {
			S4Item s4Item = (S4Item) object;

			S4ItemForShow itemForShow = new S4ItemForShow(s4Item);

			for (Object event : eventList) {
				if (((DoctorEvent) event).id
						.equals(s4Item.typeS4DAvailabilityDoctorEventId)) {
					itemForShow.doctorEvent = (DoctorEvent) event;
					break;
				}
			}

			if (itemForShow.doctorEvent != null)
				for (Object doctor : doctorList) {
					if (itemForShow.doctorEvent.doctor_id
							.equals(((Doctor) doctor).id)) {
						itemForShow.doctor = (Doctor) doctor;
						break;
					}

				}
			if (itemForShow.doctor != null)
				for (Object clins : clinsList) {
					if (itemForShow.doctor.clinic_id
							.equals(((Clinic) clins).id)) {
						itemForShow.clins = (Clinic) clins;
						break;
					}
				}

			list.add(itemForShow);
		}

		Comparator<? super Object> s = new Comparator<Object>() {
			@Override
			public int compare(Object lhs, Object rhs) {
				if (((S4ItemForShow) lhs).s4Item.isUpcoming) {
					return 1;
				}
				if (((S4ItemForShow) lhs).s4Item.isUpcoming == ((S4ItemForShow) rhs).s4Item.isUpcoming) {
					return 1;
				}
				return 0;
			}
		};

		int count = 0;
		for (Object object : list) {
			if (((S4ItemForShow) object).s4Item.isUpcoming) {
				count++;
			}
		}
		for (Object object : list) {
			((S4ItemForShow) object).count = count;
		}
		Collections.sort(list, s);
		if (list.size() == 0) {
			findViewById(R.id.s4first).setVisibility(View.VISIBLE);
			findViewById(R.id.s4_listview).setVisibility(View.GONE);
		} else {
			findViewById(R.id.s4first).setVisibility(View.GONE);
			findViewById(R.id.s4_listview).setVisibility(View.VISIBLE);

			ListView listView = (ListView) findViewById(R.id.s4_listview);
			setupListView(listView);
			listView.setAdapter(new BaseAdapter(getMContext(), list,
					new CommonGenderView() {

						@Override
						public CustomLinearLayoutView getView(Context arg0,
								Object arg1) {
							return new S4ItemView(arg0);
						}
					}) {
				@Override
				public View getView(int position, View convertView,
						ViewGroup parent) {
					View view = super.getView(position, convertView, parent);

					if (position == 0) {
						((S4ItemView) view).showHeadder(true);
					} else {
						// TODO
						if (((S4ItemForShow) getItem(position)).s4Item.isUpcoming == ((S4ItemForShow) getItem(position - 1)).s4Item.isUpcoming) {
							((S4ItemView) view).showHeadder(false);
						} else {
							((S4ItemView) view).showHeadder(true);
						}
					}

					return view;
				}
			});
		}
	}

	private class S4ItemForShow {
		public Clinic clins;
		public Doctor doctor;
		public DoctorEvent doctorEvent;
		private S4Item s4Item;
		public int count;

		public S4ItemForShow(S4Item s4Item) {
			this.s4Item = s4Item;
		}
	}

	@Override
	public int getLayout() {
		return R.layout.s4activity;
	}

	private Context getMContext() {
		return this;
	}

	private class S4ItemView extends CustomLinearLayoutView {

		public S4ItemView(Context context) {
			super(context);
			init(R.layout.s4itemview);
		}

		@Override
		public void init(int res) {
			super.init(res);
			AvatarView avatarView = (AvatarView) findViewById(R.id.s4ditemview_avatarView1);
			avatarView.setType(5);
		}

		public void showHeadder(boolean b) {
			findViewById(R.id.s4ditemview_header).setVisibility(
					b ? View.VISIBLE : View.GONE);
		}

		@Override
		protected void onAttachedToWindow() {
			super.onAttachedToWindow();

			resize.resizeSacle(findViewById(R.id.s4_1), 300,
					LayoutParams.WRAP_CONTENT);
			resize.resizeSacle(findViewById(R.id.s4ditemview_header), 300,
					LayoutParams.WRAP_CONTENT);
			resize.resizeSacle(findViewById(R.id.s4itemview_content_main), 300,
					LayoutParams.WRAP_CONTENT);

			resize.resizeSacle(this, LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT);
			resize.setTextsize(findViewById(R.id.s4ditemview_textheader),
					MintUtils.TEXTSIZE_S4_HEADER);

			resize.setTextsize(findViewById(R.id.s4ditemview_textview1),
					MintUtils.TEXTSIZE_ITEM1);
			resize.setTextsize(findViewById(R.id.s4ditemview_textview2),
					MintUtils.TEXTSIZE_ITEM2);
			resize.setTextsize(findViewById(R.id.s4ditemview_textview3),
					MintUtils.TEXTSIZE_ITEM2);

			// resize.resizeSacle(findViewById(R.id.s4ditemview_img1),
			// MintUtils.IMGITEM_WIDTH, MintUtils.IMGITEM_WIDTH);

			resize.resizeSacle(findViewById(R.id.s4ditemview_content), 237,
					LayoutParams.WRAP_CONTENT);
			resize.resizeSacle(findViewById(R.id.s4ditemview_img2),
					MintUtils.IMGITEM_WIDTH2, MintUtils.IMGITEM_WIDTH2);

		}

		@Override
		public void setGender() {
			S4ItemForShow item = (S4ItemForShow) getData();
			DoctorEvent doctorEvent = item.doctorEvent;
			Doctor doctor = item.doctor;
			Clinic clinic = item.clins;
			if (clinic == null) {
				clinic = new Clinic();
			}
			if (doctor == null) {
				doctor = new Doctor();
			}
			if (doctorEvent == null) {
				doctorEvent = new DoctorEvent();
			}

			String message = item.count + " upcoming appointments";
			if (!item.s4Item.isUpcoming) {
				message = "Appointment History";
			}

			setText(R.id.s4ditemview_textheader, message);

			AvatarView avatarView = (AvatarView) findViewById(R.id.s4ditemview_avatarView1);
			avatarView.loadAvartar(doctor.avatar);

			setText(R.id.s4ditemview_textview1, DateUtils.getDate(doctorEvent.start) + " " + DateUtils.getHHMM(doctorEvent.start));
			setText(R.id.s4ditemview_textview2,
					String.format("%s ,%s", doctor.name, doctor.level));
			setText(R.id.s4ditemview_textview3, clinic.address);

		}

		private void setText(int s4ditemviewTextheader, String day) {
			((TextView) findViewById(s4ditemviewTextheader)).setText(day);
		}
	}
}