package vnp.mr.mintmedical.fragment;

import java.util.List;

import vnp.mr.mintmedical.R;
import vnp.mr.mintmedical.base.MBaseFragment;
import vnp.mr.mintmedical.view.OfficeItemView;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.viewpagerindicator.db.DBClinic;
import com.vnp.core.common.BaseAdapter;
import com.vnp.core.common.BaseAdapter.CommonGenderView;
import com.vnp.core.view.CustomLinearLayoutView;

public class S4G extends MBaseFragment implements OnClickListener {
	// Appointment
	private ListView listView;

	@Override
	public void onResume() {
		super.onResume();
		listView = (ListView) findViewById(R.id.s4dlistView);
		updateUI();
	}

	private void updateUI() {
		listView.setAdapter(new BaseAdapter(getActivity(),
				(List<Object>) new DBClinic(getActivity()).getData(),
				new CommonGenderView() {
					@Override
					public CustomLinearLayoutView getView(Context arg0,
							Object arg1) {
						return new OfficeItemView(arg0);
					}
				}));

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
		return R.layout.s4g;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.id1) {

		} else if (v.getId() == R.id.id2) {

		} else if (v.getId() == R.id.id3) {

		}
	}

	@Override
	public int getHeaderRes() {
		return R.string.s4g_header;
	}

	// private class S4GItemView extends CustomLinearLayoutView {
	//
	// public S4GItemView(Context context) {
	// super(context);
	// init(R.layout.s4gitemview);
	// }
	//
	// @Override
	// protected void onAttachedToWindow() {
	// super.onAttachedToWindow();
	// resize.resizeSacle(this,
	// LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
	// resize.setTextsize(findViewById(R.id.s4gitemview_textview1),
	// MintUtils.TEXTSIZE_ITEM1);
	// resize.setTextsize(findViewById(R.id.s4gitemview_textview2),
	// MintUtils.TEXTSIZE_ITEM2);
	// resize.setTextsize(findViewById(R.id.s4gitemview_textview3),
	// MintUtils.TEXTSIZE_ITEM2);
	//
	// resize.resizeSacle(findViewById(R.id.s4gitemview_content_main),
	// 300,LayoutParams.WRAP_CONTENT);
	// resize.resizeSacle(findViewById(R.id.s4gitemview_content),
	// 287,LayoutParams.WRAP_CONTENT);
	// resize.resizeSacle(findViewById(R.id.s4gitemview_img2),
	// MintUtils.IMGITEM_WIDTH2, MintUtils.IMGITEM_WIDTH2);
	// }
	//
	// @Override
	// public void setGender() {
	// Clinic item = (Clinic) getData();
	// setText(R.id.s4gitemview_textview1, item.name);
	// setText(R.id.s4gitemview_textview2, item.address);
	//
	// setText(R.id.s4gitemview_textview3, "");
	//
	// try{
	// Location last = VNPLocationUtils.getInstance().lastKnownLocation;
	// LatLng latLng1 = new LatLng(last.getLatitude(), last.getLongitude());
	// LatLng latLng2 = new LatLng(Double.parseDouble(item.latitude),
	// Double.parseDouble(item.longitude));
	// setText(R.id.s4gitemview_textview3,
	// String.format("%s km", VNPLocationUtils.getDistance(latLng1, latLng2))
	// );
	// }catch(Exception exception){
	// }
	// }
	//
	//
	// private void setText(int res, String day) {
	// ((TextView) findViewById(res)).setText(day);
	// }
	// }
}