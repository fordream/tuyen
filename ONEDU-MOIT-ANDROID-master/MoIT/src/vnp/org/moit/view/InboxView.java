package vnp.org.moit.view;

import java.util.List;

import one.edu.vn.sms.R;
import one.edu.vn.sms.S1S2S3Activity;
import vnp.org.moit.adapter.InboxAdapter;
import vnp.org.moit.db.EduStudent;
import vnp.org.moit.dialog.ChonTruongOrHocSinhDialog;
import vnp.org.moit.dialog.FeedBackDialog;
import vnp.org.moit.service.EduSevice.TypeMore;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.vnp.core.common.CommonAndroid;

//vnp.org.moit.view.InboxView
public class InboxView extends LinearLayout {
	private String studentId = null;
	private ListView inbox_list;
	private InboxAdapter newFeedAdapter;

	public InboxView(Context context) {
		super(context);
		CommonAndroid.getView(context, R.layout.inbox, this);
		init();
	}

	public InboxView(Context context, AttributeSet attrs) {
		super(context, attrs);
		CommonAndroid.getView(context, R.layout.inbox, this);
		init();
	}

	public InboxView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		CommonAndroid.getView(context, R.layout.inbox, this);
		init();
	}

	private void init() {
		inbox_list = CommonAndroid.getView(this, R.id.inbox_list);
		newFeedAdapter = new InboxAdapter(getContext(), findViewById(R.id.inbox_empty_txt)) {

			@Override
			public void phanhoi(String smsid, String strstrPhanHoi, String title) {
				new FeedBackDialog(getContext(), smsid,strstrPhanHoi,  title).show();
			}

			@Override
			public void chiase(String smsid, Bitmap bitmap) {
				s1s2s3Activity.chiase(smsid, bitmap);
			}
		};
		inbox_list.setAdapter(newFeedAdapter);

		findViewById(R.id.inbox_top).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				EduStudent eduStudent = new EduStudent(getContext());
				List<String> schoolIds = eduStudent.getStudentIds();
				if (schoolIds.size() >= 2) {
					new ChonTruongOrHocSinhDialog(s1s2s3Activity, true, studentId) {
						public void onClickChooseAll() {
							super.onClickChooseAll();
							studentId = null;
							CommonAndroid.setText(InboxView.this, R.id.inbox_top_text, getContext().getString(R.string.tatcahocsinh));
							updateData();
						};

						public void onItemClickListener(boolean isStudent, String idStr, String name) {
							super.onItemClickListener(isStudent, idStr, name);
							studentId = idStr;
							CommonAndroid.setText(InboxView.this, R.id.inbox_top_text, name);
							updateData();
						};
					}.show();
				}
			}
		});
		
		updateData();

	}

	private void updateData() {

		newFeedAdapter.getFilter().filter(studentId);
		newFeedAdapter.notifyDataSetChanged();
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
		if (typeMore == TypeMore.SMSList) {
			updateData();
		}
	}

	public void reloadToFirst() {
		updateData();
		inbox_list.setSelection(0);
	}
}