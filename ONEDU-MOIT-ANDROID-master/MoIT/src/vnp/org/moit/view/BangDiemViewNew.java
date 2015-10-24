package vnp.org.moit.view;

import java.util.List;

import one.edu.vn.sms.R;
import one.edu.vn.sms.S1S2S3Activity;
import vnp.org.moit.adapter.BangDiemHangNgayAdapter;
import vnp.org.moit.adapter.BangDiemTongKetAdapter;
import vnp.org.moit.db.EduStudent;
import vnp.org.moit.dialog.ChonTruongOrHocSinhDialog;
import vnp.org.moit.dialog.FeedBackDialog;
import vnp.org.moit.service.EduSevice;
import vnp.org.moit.service.EduSevice.TypeMore;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.vnp.core.common.CommonAndroid;

//vnp.org.moit.view.BangDiemView
public class BangDiemViewNew extends LinearLayout {
	private String studentId = null;
	private vnp.org.moit.view.BangDiemScrollView inbox_list;
	private BangDiemHangNgayAdapter bangDiemAdapter;
	private BangDiemTongKetAdapter adapterBangDiemTongKetAdapter;

	public BangDiemViewNew(Context context) {
		super(context);
		CommonAndroid.getView(context, R.layout.bangdiem_new, this);
		init();
	}

	public BangDiemViewNew(Context context, AttributeSet attrs) {
		super(context, attrs);
		CommonAndroid.getView(context, R.layout.bangdiem_new, this);
		init();
	}

	public BangDiemViewNew(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		CommonAndroid.getView(context, R.layout.bangdiem_new, this);
		init();
	}

	private void init() {
		inbox_list = CommonAndroid.getView(this, R.id.bangdiem_list_ll);
		bangDiemAdapter = new BangDiemHangNgayAdapter(getContext(),
				findViewById(R.id.bangdiem_empty_txt)) {

			@Override
			public void phanhoi(String smsid, String strPhanHoi, String title) {
				new FeedBackDialog(getContext(), smsid, strPhanHoi, title)
						.show();
			}

			@Override
			public void chiase(String smsid, Bitmap bitmap) {
				s1s2s3Activity.chiase(smsid, bitmap);
			}
		};

		adapterBangDiemTongKetAdapter = new BangDiemTongKetAdapter(
				getContext(), findViewById(R.id.bangdiem_empty_txt)) {
			@Override
			public void phanhoi(String smsid, String namhocStudenId,
					String title) {
				new FeedBackDialog(getContext(), smsid, namhocStudenId, title)
						.show();
			}

			@Override
			public void chiase(String smsid, Bitmap bitmap) {
				s1s2s3Activity.chiase(smsid, bitmap);
			}
		};
		// inbox_list.setAdapter(bangDiemAdapter);

		findViewById(R.id.bangdiem_top).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						EduStudent eduStudent = new EduStudent(getContext());
						List<String> schoolIds = eduStudent.getStudentIds();
						if (schoolIds.size() >= 2) {
							new ChonTruongOrHocSinhDialog(s1s2s3Activity, true,
									studentId) {
								public void onClickChooseAll() {
									super.onClickChooseAll();
									studentId = null;
									CommonAndroid.setText(
											BangDiemViewNew.this,
											R.id.bangdiem_top_text,
											getContext().getString(
													R.string.tatcahocsinh));
									updateData();
								};

								public void onItemClickListener(
										boolean isStudent, String idStr,
										String name) {
									super.onItemClickListener(isStudent, idStr,
											name);
									studentId = idStr;
									CommonAndroid.setText(BangDiemViewNew.this,
											R.id.bangdiem_top_text, name);
									updateData();
								};
							}.show();
						}
					}
				});

		RadioGroup group = CommonAndroid.getView(this, R.id.bangdiem_rg);
		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				updateData();
				loadMore();
			}
		});
		updateData();
	}

	private void updateData() {
		RadioButton group = CommonAndroid.getView(this, R.id.bangdiem_r1);

		if (group.isChecked()) {
			inbox_list.setAdapter(bangDiemAdapter);
			bangDiemAdapter.getFilter().filter(studentId);
			bangDiemAdapter.notifyDataSetChanged();
		} else {
			inbox_list.setAdapter(adapterBangDiemTongKetAdapter);
			adapterBangDiemTongKetAdapter.getFilter().filter(studentId);
			adapterBangDiemTongKetAdapter.notifyDataSetChanged();
		}

	}

	private void loadMore() {
		Intent intent = new Intent(getContext(), EduSevice.class);
		intent.putExtra("api", "loadmore");
		getContext().startService(intent);
	}

	public void setVisibility(boolean checked) {
		setVisibility(checked ? VISIBLE : GONE);
		if (checked) {
			updateData();
		}

	}

	private S1S2S3Activity s1s2s3Activity;

	public void setActivity(S1S2S3Activity s1s2s3Activity) {
		this.s1s2s3Activity = s1s2s3Activity;
	}

	public void updateData(TypeMore typeMore, String id) {
		// FIXME || typeMore == TypeMore.SMSList

		if (typeMore == TypeMore.SMSList
				|| typeMore == TypeMore.LOADMOREMarkTableGet) {
			updateData();
		}
	}

	public void reloadToFirst() {
		updateData();
		//inbox_list.setSelection(0);
	}
}