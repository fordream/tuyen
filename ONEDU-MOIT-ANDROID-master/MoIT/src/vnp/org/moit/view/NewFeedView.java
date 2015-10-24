package vnp.org.moit.view;

import java.util.List;

import one.edu.vn.sms.R;
import one.edu.vn.sms.S1S2S3Activity;
import vnp.org.moit.adapter.test.TestNewFeedAdapter;
import vnp.org.moit.db.EduStudent;
import vnp.org.moit.dialog.ChonTruongOrHocSinhDialog;
import vnp.org.moit.dialog.NewFeedDetailDialog;
import vnp.org.moit.service.EduSevice.TypeMore;
import android.content.Context;
import android.database.Cursor;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.vnp.core.common.CommonAndroid;

public class NewFeedView extends LinearLayout {
	private String schoolId;
	private ListView list;
	private TestNewFeedAdapter newFeedAdapter;

	public NewFeedView(Context context) {
		super(context);
		CommonAndroid.getView(context, R.layout.new_feed, this);
		init();
	}

	public NewFeedView(Context context, AttributeSet attrs) {
		super(context, attrs);
		CommonAndroid.getView(context, R.layout.new_feed, this);
		init();
	}

	public NewFeedView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		CommonAndroid.getView(context, R.layout.new_feed, this);
		init();
	}

	private NewFeedDetailDialog newFeedDetailDialog;

	private void init() {
		list = CommonAndroid.getView(this, R.id.new_feed_list);

		newFeedAdapter = new TestNewFeedAdapter(getContext(), findViewById(R.id.newfeed_empty_txt));
		list.setAdapter(newFeedAdapter);
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String newFeedId = CommonAndroid.getString((Cursor) parent.getItemAtPosition(position), "Id");

				newFeedDetailDialog = new NewFeedDetailDialog(s1s2s3Activity, newFeedId) {
					public void dismiss() {
						super.dismiss();
						NewFeedView.this.updateData();
					};
				};

				newFeedDetailDialog.show();
			}
		});

		findViewById(R.id.newffed_top).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				EduStudent eduStudent = new EduStudent(getContext());
				List<String> schoolIds = eduStudent.getSchoolIds();
				if (schoolIds.size() >= 2) {
					new ChonTruongOrHocSinhDialog(getContext(), false, schoolId) {
						public void onClickChooseAll() {
							super.onClickChooseAll();
							schoolId = null;
							TextView newffed_top_text = CommonAndroid.getView(NewFeedView.this, R.id.newffed_top_text);
							newffed_top_text.setText(getContext().getString(R.string.tatcacactruong));
							updateData();
						};

						public void onItemClickListener(boolean isStudent, String idStr, String name) {
							super.onItemClickListener(isStudent, idStr, name);
							schoolId = idStr;
							TextView newffed_top_text = CommonAndroid.getView(NewFeedView.this, R.id.newffed_top_text);
							newffed_top_text.setText(name);
							updateData();
						};
					}.show();
				}
			}
		});
		
		updateData();
	}

	private void updateData() {
		EduStudent eduStudent = new EduStudent(getContext());
		List<String> schoolIds = eduStudent.getSchoolIds();
		if (schoolIds.size() == 1) {
			TextView newffed_top_text = CommonAndroid.getView(NewFeedView.this, R.id.newffed_top_text);
			schoolId = schoolIds.get(0);
			newffed_top_text.setText(eduStudent.getSchoolName(schoolId));
		}
		// newFeedAdapter = new NewFeedAdapter(getContext(), emptyView);
		// list.setAdapter(newFeedAdapter);

		newFeedAdapter.getFilter().filter(schoolId);
	}

	public void setVisibility(boolean checked) {
		setVisibility(checked ? VISIBLE : GONE);
		if (checked)
			updateData();
	}

	private S1S2S3Activity s1s2s3Activity;

	public void setActivity(S1S2S3Activity s1s2s3Activity) {
		this.s1s2s3Activity = s1s2s3Activity;
	}

	public void updateData(TypeMore typeMore, String id) {
		if (typeMore == TypeMore.ArticleList) {
			updateData();
		} else if (typeMore == typeMore.ArticleCommentList && newFeedDetailDialog != null) {
			newFeedDetailDialog.updateData(typeMore, id);
		}
	}
}