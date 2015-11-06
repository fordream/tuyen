package vnp.mr.mintmedical;

import com.viewpagerindicator.db.DBClinic;

import vnp.mr.mintmedical.base.MBaseActivity;
import vnp.mr.mintmedical.base.MBaseFragment;
import vnp.mr.mintmedical.base.MBaseFragment.IMBaseFragmentOnClickListener;
import vnp.mr.mintmedical.fragment.S4A;
import vnp.mr.mintmedical.fragment.S4B;
import vnp.mr.mintmedical.fragment.S4C;
import vnp.mr.mintmedical.fragment.S4D;
import vnp.mr.mintmedical.fragment.S4E;
import vnp.mr.mintmedical.fragment.S4F;
import vnp.mr.mintmedical.fragment.S4G;
import vnp.mr.mintmedical.fragment.S4H;
import vnp.mr.mintmedical.item.Clinic;
import vnp.mr.mintmedical.item.Doctor;
import vnp.mr.mintmedical.item.DoctorEvent;
import vnp.mr.mintmedical.item.S4DItem;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

public class S4ManagerActivity extends MBaseActivity {
	private DataSendToServer dataSendToServer = new DataSendToServer();
	private String id_clins;
	private S4A s4a;
	// type = 1 : A-> B -> C -> D -> E -> F
	// 2 : A-> B -> G -> C -> D -> E -> F
	// 3 : A-> H -> B -> C -> D -> E -> F
	private int type = 0;
	private HeaderView headerView;
	private Fragment current;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		headerView = getView(R.id.activitymain_headerview);
		headerView.setOnClickListenerButtonBack(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		headerView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (current != null && current instanceof S4F) {
					// done
					finish();
				} else {
					// cancel
					finish();
				}
			}
		});

		id_clins = getIntent().getStringExtra("id");
		if (id_clins == null) {
			s4a = new S4A();
			s4a.setOnClickListener(s4ainOnclick);

			changeFragemtn(R.id.content_layout, s4a);
			current = s4a;
			type = 0;
		} else {
			s4ainOnclick.onClick(null, 2);
		}

	}

	private IMBaseFragmentOnClickListener s4ainOnclick = new IMBaseFragmentOnClickListener() {
		@Override
		public void onClick(Object dialog, int which) {
			type = which;
			MBaseFragment baseFragment = null;
			dataSendToServer.typeS4AAppointment = type;

			if (type == 1) {
				// B
				baseFragment = new S4B();
				baseFragment.setOnClickListener(s4binOnclick);
			} else if (type == 2) {
				// B
				baseFragment = new S4B();
				baseFragment.setOnClickListener(s4binOnclick);
			} else if (type == 3) {
				// H
				baseFragment = new S4H();
				baseFragment.setOnClickListener(s4hinOnclick);
			}

			if (baseFragment != null) {
				baseFragment.setParent(s4a);
				changeFragemtn(R.id.content_layout, baseFragment);
			}
		}
	};

	public void changeFragemtn(int r_id_content_frame, Fragment rFragment) {
		super.changeFragemtn(r_id_content_frame, rFragment);
		if (current != null && current instanceof MBaseFragment) {
			((MBaseFragment) current).onHiddenKeyBoard();
		}

		current = rFragment;

		if (current instanceof S4A || current instanceof S4F) {
			headerView.showButton(false, true);

			if (current instanceof S4A) {
				headerView.updateTextButtonRight(R.string.cancel);
			} else {
				headerView.updateTextButtonRight(R.string.done);
			}
		} else {
			headerView.showButton(true, true);
			headerView.updateTextButtonRight(R.string.cancel);
		}

		if (current != null && current instanceof MBaseFragment) {
			headerView.updateText(((MBaseFragment) current).getHeaderRes());
		}
	};

	private IMBaseFragmentOnClickListener s4hinOnclick = new IMBaseFragmentOnClickListener() {

		@Override
		public void onClick(Object dialog, int which) {
			dataSendToServer.typeS4HSelectDoctor = (Doctor) dialog;
			MBaseFragment baseFragment = new S4B();
			baseFragment.setParent(current);
			baseFragment.setOnClickListener(s4binOnclick);
			changeFragemtn(R.id.content_layout, baseFragment);
		}
	};

	private IMBaseFragmentOnClickListener s4ginOnclick = new IMBaseFragmentOnClickListener() {

		@Override
		public void onClick(Object dialog, int which) {

			dataSendToServer.typeS4GSelectOffice = (Clinic) dialog;

			MBaseFragment baseFragment = new S4C();
			baseFragment.setParent(current);
			baseFragment.setOnClickListener(s4cinOnclick);
			changeFragemtn(R.id.content_layout, baseFragment);
		}
	};

	private IMBaseFragmentOnClickListener s4binOnclick = new IMBaseFragmentOnClickListener() {

		@Override
		public void onClick(Object dialog, int which) {

			dataSendToServer.typeS4BVisitType = dialog.toString();

			if (type == 1 || type == 3) {
				// to s4c
				MBaseFragment baseFragment = new S4C();
				baseFragment.setParent(current);
				baseFragment.setOnClickListener(s4cinOnclick);
				changeFragemtn(R.id.content_layout, baseFragment);
			} else if (type == 2) {
				if (id_clins != null) {
					s4ginOnclick.onClick(new DBClinic(S4ManagerActivity.this).getData(id_clins), 0);
				} else {
					MBaseFragment baseFragment = new S4G();
					baseFragment.setParent(current);
					baseFragment.setOnClickListener(s4ginOnclick);
					changeFragemtn(R.id.content_layout, baseFragment);
				}
			}
		}
	};
	private IMBaseFragmentOnClickListener s4cinOnclick = new IMBaseFragmentOnClickListener() {

		@Override
		public void onClick(Object dialog, int which) {
			dataSendToServer.typeS4CReason = dialog.toString();
			S4D baseFragment = new S4D();
			baseFragment.setDataSendToServer(dataSendToServer);
			baseFragment.setParent(current);
			baseFragment.setOnClickListener(s4dinOnclick);
			changeFragemtn(R.id.content_layout, baseFragment);
		}
	};
	private IMBaseFragmentOnClickListener s4dinOnclick = new IMBaseFragmentOnClickListener() {

		@Override
		public void onClick(Object item, int which) {
			dataSendToServer.typeS4DAvailability = (S4DItem) item;
			S4E baseFragment = new S4E();
			baseFragment.setDataSendToServer(dataSendToServer);
			baseFragment.setData(item);
			baseFragment.setParent(current);
			baseFragment.setOnClickListener(s4einOnclick);
			changeFragemtn(R.id.content_layout, baseFragment);
		}
	};

	private IMBaseFragmentOnClickListener s4einOnclick = new IMBaseFragmentOnClickListener() {

		@Override
		public void onClick(Object dialog, int which) {
			MBaseFragment baseFragment = new S4F();
			baseFragment.setParent(current);
			baseFragment.setOnClickListener(s4finOnclick);
			changeFragemtn(R.id.content_layout, baseFragment);
		}
	};

	private IMBaseFragmentOnClickListener s4finOnclick = new IMBaseFragmentOnClickListener() {
		@Override
		public void onClick(Object dialog, int which) {
			finish();
		}
	};

	@Override
	public void onBackPressed() {
		Fragment parent = null;
		if (current != null && current instanceof MBaseFragment) {
			parent = ((MBaseFragment) current).getParent();
			((MBaseFragment) current).onHiddenKeyBoard();
		}

		if (current instanceof S4F) {
			return;
		}

		if (parent == null)
			super.onBackPressed();
		else {
			changeFragemtn(R.id.content_layout, parent);
			current = parent;
			if (parent instanceof S4A) {
				type = 0;
			}
		}
	}

	@Override
	public int getLayout() {
		return R.layout.ms4manageractivity;
	}

	public class DataSendToServer {
		public int typeS4AAppointment;
		public String typeS4BVisitType;
		public String typeS4CReason;
		public S4DItem typeS4DAvailability;
		public Clinic typeS4GSelectOffice;
		public Doctor typeS4HSelectDoctor;
	}
}