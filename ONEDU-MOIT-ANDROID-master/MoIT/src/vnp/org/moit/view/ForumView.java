package vnp.org.moit.view;

import java.util.List;

import one.edu.vn.sms.R;
import one.edu.vn.sms.S1S2S3Activity;
import vnp.org.moit.adapter.ForumAdapter;
import vnp.org.moit.db.EduStudent;
import vnp.org.moit.dialog.ChonTruongOrHocSinhDialog;
import vnp.org.moit.dialog.ForumDetailtDialog;
import vnp.org.moit.service.EduSevice.TypeMore;
import android.content.Context;
import android.database.Cursor;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.vnp.core.common.CommonAndroid;

//vnp.org.moit.view.ForumView
public class ForumView extends LinearLayout implements OnClickListener {
	private ListView list;
	private ForumAdapter adapter;
	private ForumDetailtDialog forumDetailtDialog;

	public ForumView(Context context) {
		super(context);
		init();
	}

	public ForumView(Context context, AttributeSet attrs) {
		super(context, attrs);

		init();
	}

	public ForumView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		CommonAndroid.getView(getContext(), R.layout.forrum, this);
		list = CommonAndroid.getView(this, R.id.forum_list);
		findViewById(R.id.forum_empty_txt).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				createTopic();
			}
		});
		adapter = new ForumAdapter(getContext(), findViewById(R.id.forum_empty_txt));
		list.setAdapter(adapter);
		findViewById(R.id.forum_top_create).setOnClickListener(this);
		findViewById(R.id.forum_top).setOnClickListener(this);

		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				String topicId = CommonAndroid.getString((Cursor) arg0.getItemAtPosition(arg2), "ID");
				forumDetailtDialog = new ForumDetailtDialog(s1s2s3Activity, topicId) {
					@Override
					public void dismiss() {
						super.dismiss();
						ForumView.this.updateData();
						forumDetailtDialog = null;
					}
				};

				forumDetailtDialog.show();
			}
		});
		
		updateData();
	}

	String schoolId = null;

	private void createTopicDialog(final String schoolId) {
		s1s2s3Activity.createTopicDialog(schoolId);
	};

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.forum_top_create) {
			createTopic();
		} else if (R.id.forum_top == v.getId()) {

			EduStudent eduStudent = new EduStudent(getContext());
			List<String> schoolIds = eduStudent.getSchoolIds();
			if (schoolIds.size() >= 2) {
				new ChonTruongOrHocSinhDialog(getContext(), false, schoolId) {
					public void onClickChooseAll() {
						schoolId = null;
						TextView forum_top_text = CommonAndroid.getView(ForumView.this, R.id.forum_top_text);
						forum_top_text.setText(R.string.tatcacactruong);
						updateData();
					};

					public void onItemClickListener(boolean isStudent, String idStr, String name) {
						TextView forum_top_text = CommonAndroid.getView(ForumView.this, R.id.forum_top_text);
						forum_top_text.setText(name);
						schoolId = idStr;
						updateData();
					};
				}.show();
			}
		}
	}

	private void createTopic() {
		if (CommonAndroid.isBlank(schoolId)) {
			new ChonTruongOrHocSinhDialog(getContext(), false, schoolId) {

				public void onItemClickListener(boolean isStudent, final String idStr, String name) {
					createTopicDialog(idStr);
				}

			}.show();
		} else {
			createTopicDialog(schoolId);
		}

	}

	public void updateData() {

		EduStudent eduStudent = new EduStudent(getContext());
		List<String> schoolIds = eduStudent.getSchoolIds();
		if (schoolIds.size() == 1) {
			TextView newffed_top_text = CommonAndroid.getView(ForumView.this, R.id.forum_top_text);
			schoolId = schoolIds.get(0);
			newffed_top_text.setText(eduStudent.getSchoolName(schoolId));
		}

		adapter.getFilter().filter(schoolId);
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

	// public void imageForCreateTopPic(Bitmap bit) {
	// createTopicDialog.imageForCreateTopPic(bit);
	// }

	public void updateData(TypeMore typeMore, String id) {
		if (typeMore == TypeMore.TopicList) {
			updateData();
		} else if (typeMore == typeMore.TopicCommentList && forumDetailtDialog != null) {
			forumDetailtDialog.updateData(typeMore, id);
		}
	}
}