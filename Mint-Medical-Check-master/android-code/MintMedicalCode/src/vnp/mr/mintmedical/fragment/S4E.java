package vnp.mr.mintmedical.fragment;

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
import vnp.mr.mintmedical.item.VisitType;
import vnp.mr.mintmedical.service.VNPLocationUtils;
import vnp.mr.mintmedical.view.AvatarView;
import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.viewpagerindicator.db.DBClinic;
import com.viewpagerindicator.db.DBConfig;
import com.viewpagerindicator.db.DBDoctor;
import com.viewpagerindicator.db.DBS4Appointment;
import com.vnp.core.common.ImageLoaderUtils;

@SuppressLint("ValidFragment")
public class S4E extends MBaseFragment implements OnClickListener {
	SupportMapFragment fragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fragment = new S4MapFagment();
		getActivity().getSupportFragmentManager().beginTransaction()
				.replace(R.id.s4e_map, fragment).commit();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, container, savedInstanceState);
		AvatarView avatarView = (AvatarView) v
				.findViewById(R.id.s4e_avatarView1);
		avatarView.setType(1);
		return v;
	}

	@Override
	public void onResume() {
		super.onResume();
		resize.resizeSacle(findViewById(R.id.s4e_s1), 300,
				LayoutParams.WRAP_CONTENT);
		resize.resizeSacle(findViewById(R.id.s4e_map),
				LayoutParams.MATCH_PARENT, 200);

		resize.resizeSacle(findViewById(R.id.s4e_bnt), 538 / 2, 96 / 2);

		resize.setTextsize(findViewById(R.id.s4e_text1),
				MintUtils.TEXTSIZE_ITEM1);
		resize.setTextsize(findViewById(R.id.s4e_text2),
				MintUtils.TEXTSIZE_ITEM1);
		resize.setTextsize(findViewById(R.id.s4e_text3),
				MintUtils.TEXTSIZE_ITEM2);
		resize.setTextsize(findViewById(R.id.s4e_text4),
				MintUtils.TEXTSIZE_ITEM2);
		resize.setTextsize(findViewById(R.id.s4e_text5),
				MintUtils.TEXTSIZE_ITEM1); 	 	
		resize.setTextsize(findViewById(R.id.s4e_text6),
				MintUtils.TEXTSIZE_ITEM2);

		findViewById(R.id.s4e_bnt).setOnClickListener(this);

		// Object object = getData();
		// if (getData() != null && getData() instanceof DoctorEvent) {
		S4DItem s4dItem = dataSendToServer.typeS4DAvailability;
		// DoctorEvent item = (DoctorEvent) getData();
		// Doctor doctor = (Doctor) new DBDoctor(getActivity())
		// .getData(item.doctor_id);
		// if (doctor == null)
		// doctor = new Doctor();
		// Clinic clinic = (Clinic) new DBClinic(getActivity())
		// .getData(doctor.clinic_id);
		// if (clinic == null)
		// clinic = new Clinic();

		((TextView) findViewById(R.id.s4e_text1)).setText( DateUtils.getDate(s4dItem.start));
		((TextView) findViewById(R.id.s4e_text2)).setText(DateUtils.getHHMM(s4dItem.start));

		((TextView) findViewById(R.id.s4e_text3)).setText(String.format(
				"%s ,%s", s4dItem.fullname, s4dItem.doctor_level));
		((TextView) findViewById(R.id.s4e_text4))
				.setText(s4dItem.office_address);

		VisitType visitType = new DBConfig(getActivity())
				.getVisitType(dataSendToServer.typeS4BVisitType);

		String visitTYpeStr = "";
		if (visitType != null)
			visitTYpeStr = visitType.value;
		// if (dataSendToServer.typeS4BVisitType == 1) {
		// visitTYpeStr = getString(R.string.briefvisit);
		// } else if (dataSendToServer.typeS4BVisitType == 2) {
		// visitTYpeStr = getString(R.string.standardvisit);
		// } else if (dataSendToServer.typeS4BVisitType == 3) {
		// visitTYpeStr = getString(R.string.physicalexam);
		// }

		((TextView) findViewById(R.id.s4e_text5)).setText(visitTYpeStr);
		((TextView) findViewById(R.id.s4e_text6))
				.setText(dataSendToServer.typeS4CReason);
		AvatarView avatarView = (AvatarView) findViewById(R.id.s4e_avatarView1);
		avatarView.loadAvartar(s4dItem.avatar);
		// }
	}

	@Override
	public int getLayout() {
		return R.layout.s4e;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.s4e_bnt) {
			// 1 : A-> B -> C -> D -> E -> F
			// 2 : A-> B -> G -> C -> D -> E -> F
			// 3 : A-> H -> B -> C -> D -> E -> F

			String typeS4CReason = dataSendToServer.typeS4CReason;
			S4DItem typeS4DAvailability = dataSendToServer.typeS4DAvailability;
			String typeS4BVisitType = dataSendToServer.typeS4BVisitType;
			int typeS4AAppointment = dataSendToServer.typeS4AAppointment;

			if (typeS4AAppointment == 1) {
			} else if (typeS4AAppointment == 2) {
				// Clinic clinic = dataSendToServer.typeS4GSelectOffice;
			}
			if (typeS4AAppointment == 3) {
				// Doctor doctorS4H = dataSendToServer.typeS4HSelectDoctor;
			}

			// send

			S4Item s4Item = new S4Item();
			s4Item.id = "";
			s4Item.typeS4AAppointment = typeS4AAppointment + "";
			s4Item.typeS4BVisitType = typeS4BVisitType + "";
			s4Item.typeS4CReason = typeS4CReason;
			s4Item.typeS4DAvailabilityDoctorEventId = typeS4DAvailability.id;

			// s4Item.isUpcoming = true;
			DBS4Appointment appointment = new DBS4Appointment(getActivity());
			appointment.save(s4Item);
			// TODO Send server
			getOnClickListener().onClick(getData(), 0);
		}
	}

	@Override
	public int getHeaderRes() {
		return R.string.s4e_header;
	}

	private S4ManagerActivity.DataSendToServer dataSendToServer;

	public void setDataSendToServer(
			S4ManagerActivity.DataSendToServer dataSendToServer) {
		this.dataSendToServer = dataSendToServer;
	}

	private class S4MapFagment extends MapFagment {
		@Override
		public void onResume() {
			super.onResume();

			try {
				BitmapDescriptor icon = null;

				try {
					icon = BitmapDescriptorFactory
							.fromResource(R.drawable.s7bpoint);
				} catch (Exception exception) {

				}
				S4DItem s4dItem = dataSendToServer.typeS4DAvailability;
				LatLng mLocation = new LatLng(
						Double.parseDouble(s4dItem.latitude),
						Double.parseDouble(s4dItem.longitude));
				//GoogleMap googleMap = getMap();
				addMaker(s4dItem.latitude, s4dItem.longitude, icon);
				
//				MarkerOptions myMarkerOptions = new MarkerOptions()
//						.position(mLocation);
//				if (myMarkerOptions != null) {
//					googleMap.addMarker(myMarkerOptions);
//				}

				getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(
						new LatLng(mLocation.latitude, mLocation.longitude),
						ROOMSIZE));
			} catch (Exception exception) {
			}

		}
	}
}