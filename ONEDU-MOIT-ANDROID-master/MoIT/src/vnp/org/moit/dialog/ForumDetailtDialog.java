package vnp.org.moit.dialog;

import one.edu.vn.sms.R;
import vnp.org.moit.adapter.ForumDetailAdapter;
import vnp.org.moit.db.EduTopic;
import vnp.org.moit.db.EduTopicGetLike;
import vnp.org.moit.service.EduSevice;
import vnp.org.moit.service.EduSevice.TypeMore;
import vnp.org.moit.service.SynEduSevice;
import vnp.org.moit.service.SynEduSevice.Detail;
import vnp.org.moit.service.SynEduSevice.IDetailCallBack;
import vnp.org.moit.utils.MOITUtils;
import vnp.org.moit.view.GalleryView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.vnp.core.base.BaseAdialog;
import com.vnp.core.common.CommonAndroid;

public class ForumDetailtDialog extends BaseAdialog implements android.view.View.OnClickListener {
	private void setBackgroundEditText(int res, boolean errorBackground) {
		CommonAndroid.getView(this, res).setBackgroundResource(errorBackground ? R.drawable.bg_editext_radius_error : R.drawable.bg_editext_radius_white);
	}

	private String topicId;
	private GalleryView galleryView;
	private ListView listView;
	private ForumDetailAdapter forumDetailAdapter;

	public ForumDetailtDialog(Activity context, String topicId) {
		super(context);
		s1S2S3Activity = context;
		this.topicId = topicId;
		setCancelable(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setCancelable(true);
		forumDetailAdapter = new ForumDetailAdapter(getContext());
		galleryView = new GalleryView(getContext());
		galleryView.updateTopicId(topicId);

		listView = (ListView) findViewById(R.id.listView1);
		listView.addHeaderView(galleryView);

		galleryView.setOnClickListener(null);
		listView.setAdapter(forumDetailAdapter);
		forumDetailAdapter.getFilter().filter(topicId);

		findViewById(R.id.newfeeddetail_back).setOnClickListener(this);
		findViewById(R.id.forum_top_dang).setOnClickListener(this);
		findViewById(R.id.newfeeddetail_send).setOnClickListener(this);

		TextView forum_top_time = (TextView) findViewById(R.id.forum_top_time);
		TextView forum_top_text = (TextView) findViewById(R.id.forum_top_text);

		Cursor cursor = new EduTopic(getContext()).queryByTopicId(topicId);
		if (cursor != null) {
			if (cursor.moveToNext()) {
				forum_top_text.setText(CommonAndroid.getString(cursor, "OwnerName"));
				forum_top_time.setText(MOITUtils.parseDate(CommonAndroid.getString(cursor, "createTime"), getContext()));
				galleryView.update(cursor);
			}
			cursor.close();
		}

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {
				new ThongBaoDialog(getContext(), getContext().getString(R.string.banmuonreportcomment), getContext().getString(R.string.huy), getContext().getString(R.string.ok)) {
					public void onClickButton2() {
						Cursor cursor = (Cursor) parent.getItemAtPosition(position);
						String idForReport = CommonAndroid.getString(cursor, "ID");
						TopicCommentReport(idForReport);
					};
				}.show();
			}
		});

		TopicCommentGetList("1");
		getDetail();

		update();
	}

	private void getDetail() {
		SynEduSevice.executeDetail(getContext(), SynEduSevice.Detail.TOPIC, topicId, new IDetailCallBack() {

			@Override
			public void onSuccess() {

				update();
			}

			@Override
			public void onError() {
				onSuccess();
			}
		});
	}

	private void update() {
		galleryView.update();

		ImageView forum_top_dang = CommonAndroid.getView(this, R.id.forum_top_dang);
		if (new EduTopicGetLike(getContext()).isLike(topicId)) {
			forum_top_dang.setImageResource(R.drawable.heart);
		} else {
			forum_top_dang.setImageResource(R.drawable.heart0);
		}
	}

	private void TopicCommentGetList(String Page) {

		Intent intent = new Intent(getContext(), EduSevice.class);
		intent.putExtra("api", "loadmoretopic");
		intent.putExtra("id", topicId);
		getContext().startService(intent);
	}

	@Override
	public int getLayout() {
		return R.layout.forumdetail;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.newfeeddetail_back) {
			dismiss();
		} else if (v.getId() == R.id.forum_top_dang) {
			if (!CommonAndroid.NETWORK.haveConnected(getContext())) {
				new ThongBaoDialog(getContext(), getContext().getString(R.string.thuchienkhongthanhcong), null, getContext().getString(R.string.ok)).show();
				return;
			}

			SynEduSevice.executeLike(getContext(), Detail.TOPIC, topicId, new IDetailCallBack() {

				@Override
				public void onSuccess() {
					if (new EduTopicGetLike(getContext()).isLike(topicId)) {
						// new ThongBaoDialog(getContext(),
						// getContext().getString(R.string.like_topic_success),
						// null,
						// getContext().getString(R.string.ok)).show();
					} else {
						// new ThongBaoDialog(getContext(),
						// getContext().getString(R.string.unlike_topic_success),
						// null,
						// getContext().getString(R.string.ok)).show();
					}
					update();
				}

				@Override
				public void onError() {
					new ThongBaoDialog(getContext(), getContext().getString(R.string.can_not_like), null, getContext().getString(R.string.ok)).show();
				}
			});
		} else if (v.getId() == R.id.newfeeddetail_send) {
			sendComment();
		}
	}

	private Activity s1S2S3Activity;

	private void sendComment() {

		InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(((EditText) findViewById(R.id.editText1)).getWindowToken(), 0);
		CommonAndroid.hiddenKeyBoard(s1S2S3Activity);
		String description = ((EditText) findViewById(R.id.editText1)).getText().toString().trim();

		setBackgroundEditText(R.id.editText1, CommonAndroid.isBlank(description));
		if (CommonAndroid.isBlank(description)) {
			new ThongBaoDialog(getContext(), getContext().getString(R.string.banchuanhapcomment), null, getContext().getString(R.string.ok)).show();
			return;
		}
		if (!CommonAndroid.NETWORK.haveConnected(getContext())) {
			new ThongBaoDialog(getContext(), getContext().getString(R.string.thuchienkhongthanhcong), null, getContext().getString(R.string.ok)).show();
			return;
		}
		SynEduSevice synEduSevice = new SynEduSevice(getContext(), "TopicCommentAdd", true) {
			@Override
			public void onError(ErrorType errorType, Exception exception) {
				super.onError(errorType, exception);
				new ThongBaoDialog(getContext(), getContext().getString(R.string.cannotsendcomment), null, getContext().getString(R.string.ok)).show();
			}

			@Override
			public void onSucsses(String parseCode, String parseDescription, String parseData, int responseCode, String responseMessage, String response) {
				super.onSucsses(parseCode, parseDescription, parseData, responseCode, responseMessage, response);
				if ("0".equals(parseCode)) {
					((EditText) findViewById(R.id.editText1)).setText("");

					getDetail();
				} else {
					onError(ErrorType.EXECEPTION, null);
				}
				forumDetailAdapter.getFilter().filter(topicId);
			}

		};
		Bundle extras = new Bundle();
		extras.putString("topicId", topicId);
		extras.putString("description", description);
		synEduSevice.execute(extras);
	}

	public void TopicCommentReport(String commentId) {

		if (!CommonAndroid.NETWORK.haveConnected(getContext())) {
			new ThongBaoDialog(getContext(), getContext().getString(R.string.thuchienkhongthanhcong), null, getContext().getString(R.string.ok)).show();
			return;
		}

		SynEduSevice synEduSevice = new SynEduSevice(getContext(), "TopicCommentReport", true) {
			@Override
			public void onError(ErrorType errorType, Exception exception) {
				super.onError(errorType, exception);
				new ThongBaoDialog(getContext(), getContext().getString(R.string.reportfalse), null, getContext().getString(R.string.dong)).show();
			}

			@Override
			public void onSucsses(String parseCode, String parseDescription, String parseData, int responseCode, String responseMessage, String response) {
				super.onSucsses(parseCode, parseDescription, parseData, responseCode, responseMessage, response);
				if ("0".equals(parseCode)) {
					new ThongBaoDialog(getContext(), getContext().getString(R.string.reportsuccess), null, getContext().getString(R.string.dong)).show();
				} else {
					onError(ErrorType.EXECEPTION, null);
				}
			}
		};
		Bundle extras = new Bundle();
		extras.putString("topicId", topicId);
		extras.putString("commentId", commentId);
		synEduSevice.execute(extras);
	}

	public void updateData(TypeMore typeMore, String id) {
		forumDetailAdapter.getFilter().filter(topicId);
	}
}