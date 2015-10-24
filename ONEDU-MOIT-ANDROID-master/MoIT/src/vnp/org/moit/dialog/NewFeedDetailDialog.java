package vnp.org.moit.dialog;

import one.edu.vn.sms.R;
import vnp.org.moit.adapter.NewCommentAdapter;
import vnp.org.moit.db.EduArticleGetLike;
import vnp.org.moit.db.EduArticleGetList;
import vnp.org.moit.db.EduStudent;
import vnp.org.moit.service.EduSevice;
import vnp.org.moit.service.EduSevice.TypeMore;
import vnp.org.moit.service.LinkUrlDataRunable;
import vnp.org.moit.service.SynEduSevice;
import vnp.org.moit.service.SynEduSevice.IDetailCallBack;
import vnp.org.moit.utils.MOITUtils;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.vnp.core.base.BaseAdialog;
import com.vnp.core.common.CommonAndroid;

public class NewFeedDetailDialog extends BaseAdialog implements android.view.View.OnClickListener {
	private void setBackgroundEditText(int res, boolean errorBackground) {
		CommonAndroid.getView(this, res).setBackgroundResource(errorBackground ? R.drawable.bg_editext_radius_error : R.drawable.bg_editext_radius_white);
	}

	private HeaderDetailView headerDetailView;
	private ListView feeddetail_list;
	private String newFeedId;
	private NewCommentAdapter newCommentAdapter;
	Activity activity;

	public NewFeedDetailDialog(Activity context, String newFeedId) {
		super(context);
		this.activity = context;
		this.newFeedId = newFeedId;
		headerDetailView = new HeaderDetailView(context);
		setCancelable(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setCancelable(true);
		findViewById(R.id.newfeeddetail_back).setOnClickListener(this);
		findViewById(R.id.newfeeddetail_like).setOnClickListener(this);
		findViewById(R.id.newfeeddetail_send).setOnClickListener(this);
		feeddetail_list = (ListView) findViewById(R.id.feeddetail_list);
		feeddetail_list.addHeaderView(headerDetailView);
		headerDetailView.setOnClickListener(null);
		EduArticleGetList eduNewFeed = new EduArticleGetList(getContext());
		headerDetailView.loadUrl(newFeedId);
		newCommentAdapter = new NewCommentAdapter(getContext());
		((TextView) findViewById(R.id.textView1)).setText(new EduStudent(getContext()).getSchoolName(eduNewFeed.getSchoolId(newFeedId)));
		((TextView) findViewById(R.id.textView2)).setText(MOITUtils.parseDate(eduNewFeed.getCreateTime(newFeedId), getContext()));

		feeddetail_list.setAdapter(newCommentAdapter);
		feeddetail_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				final String commentId = CommonAndroid.getString((Cursor) parent.getItemAtPosition(position), "ID");
				new ThongBaoDialog(getContext(), getContext().getString(R.string.banmuonreportcomment), getContext().getString(R.string.huy), getContext().getString(R.string.ok)) {
					public void onClickButton2() {

						ArticleCommentReport(commentId);
					};
				}.show();

			}
		});

		update();

		ArticleGetCommentList();
		getDetail();
	}

	private void getDetail() {
		SynEduSevice.executeDetail(getContext(), SynEduSevice.Detail.NEWFEED, newFeedId, new IDetailCallBack() {

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
		newCommentAdapter.getFilter().filter(newFeedId);
		ImageView imageView = CommonAndroid.getView(this, R.id.newfeeddetail_like);
		if (new EduArticleGetLike(getContext()).isLike(newFeedId)) {
			imageView.setImageResource(R.drawable.heart);
		} else {
			imageView.setImageResource(R.drawable.heart0);
		}
	}

	private void ArticleGetCommentList() {
		Intent intent = new Intent(getContext(), EduSevice.class);
		intent.putExtra("api", "loadmorenewffeed");
		intent.putExtra("id", newFeedId);
		getContext().startService(intent);
	}

	@Override
	public int getLayout() {
		return R.layout.newfeeddetail;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.newfeeddetail_back) {
			dismiss();
		} else if (v.getId() == R.id.newfeeddetail_like) {

			if (!CommonAndroid.NETWORK.haveConnected(getContext())) {
				new ThongBaoDialog(getContext(), getContext().getString(R.string.thuchienkhongthanhcong), null, getContext().getString(R.string.ok)).show();
				return;
			}
			SynEduSevice.executeLike(getContext(), SynEduSevice.Detail.NEWFEED, newFeedId, new IDetailCallBack() {

				@Override
				public void onSuccess() {

					if (new EduArticleGetLike(getContext()).isLike(newFeedId)) {
						// new ThongBaoDialog(getContext(),
						// getContext().getString(R.string.likesuccess), null,
						// getContext().getString(R.string.ok)).show();
					} else {
						// new ThongBaoDialog(getContext(),
						// getContext().getString(R.string.unlikesuccess), null,
						// getContext().getString(R.string.ok)).show();
					}

					update();
				}

				@Override
				public void onError() {
					new ThongBaoDialog(getContext(), getContext().getString(R.string.can_not_like), null, getContext().getString(R.string.ok)).show();
				}
			});

		} else if (R.id.newfeeddetail_send == v.getId()) {
			ArticleAddComment();
		}
	}

	private void ArticleCommentReport(String commentId) {
		if (!CommonAndroid.NETWORK.haveConnected(getContext())) {
			new ThongBaoDialog(getContext(), getContext().getString(R.string.thuchienkhongthanhcong), null, getContext().getString(R.string.ok)).show();
			return;
		}
		SynEduSevice synEduSevice = new SynEduSevice(getContext(), "ArticleCommentReport", true) {
			@Override
			public void onSucsses(String parseCode, String parseDescription, String parseData, int responseCode, String responseMessage, String response) {
				super.onSucsses(parseCode, parseDescription, parseData, responseCode, responseMessage, response);
				if ("0".equals(parseCode))
					new ThongBaoDialog(getContext(), getContext().getString(R.string.reportcommentsuccess), null, getContext().getString(R.string.dong)).show();
				else
					onError(ErrorType.HAVENOTNETWORK, null);
			}

			@Override
			public void onError(ErrorType errorType, Exception exception) {
				super.onError(errorType, exception);

				new ThongBaoDialog(getContext(), getContext().getString(R.string.reportfail), null, getContext().getString(R.string.dong)).show();
			}
		};
		Bundle extras = new Bundle();
		extras.putString("commentId", commentId);
		synEduSevice.execute(extras);
	}

	private void ArticleAddComment() {

		if (!CommonAndroid.NETWORK.haveConnected(getContext())) {
			new ThongBaoDialog(getContext(), getContext().getString(R.string.thuchienkhongthanhcong), null, getContext().getString(R.string.ok)).show();
			return;
		}

		InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(((EditText) findViewById(R.id.editText1)).getWindowToken(), 0);
		CommonAndroid.hiddenKeyBoard(activity);
		String txt = ((TextView) findViewById(R.id.editText1)).getText().toString().trim();

		setBackgroundEditText(R.id.editText1, CommonAndroid.isBlank(txt));
		if (CommonAndroid.isBlank(txt)) {
			new ThongBaoDialog(getContext(), getContext().getString(R.string.banchuanhapcomment), null, getContext().getString(R.string.ok)).show();
			return;
		}

		SynEduSevice synEduSevice = new SynEduSevice(getContext(), "ArticleAddComment", true) {
			@Override
			public void onSucsses(String parseCode, String parseDescription, String parseData, int responseCode, String responseMessage, String response) {
				super.onSucsses(parseCode, parseDescription, parseData, responseCode, responseMessage, response);
				if ("0".equals(parseCode)) {
					update();
					((TextView) findViewById(R.id.editText1)).setText("");

					getDetail();
				} else
					onError(ErrorType.HAVENOTNETWORK, null);
			}

			@Override
			public void onError(ErrorType errorType, Exception exception) {
				super.onError(errorType, exception);
				new ThongBaoDialog(getContext(), getContext().getString(R.string.cannotsendcomment), null, getContext().getString(R.string.ok)).show();
			}
		};
		Bundle extras = new Bundle();
		extras.putString("articleId", newFeedId);
		extras.putString("description", txt);
		synEduSevice.execute(extras);

	}

	private class HeaderDetailView extends LinearLayout {
		private WebView feeddetail_web;

		public HeaderDetailView(Context context) {
			super(context);
			CommonAndroid.getView(getContext(), R.layout.feeddetail_content, this);
			feeddetail_web = CommonAndroid.getView(this, R.id.feeddetail_web);
			feeddetail_web.setWebChromeClient(new WebChromeClient());
			feeddetail_web.setWebViewClient(new WebViewClient());
			// feeddetail_web.getSettings().setLoadWithOverviewMode(true);
			// feeddetail_web.getSettings().setUseWideViewPort(true);
			// feeddetail_web.getSettings().setBuiltInZoomControls(true);
		}

		public void loadUrl(final String newFeedId) {
			EduArticleGetList eduArticleGetList = new EduArticleGetList(getContext());
			String url = eduArticleGetList.getLinkUrl(newFeedId);
			String schoolId = eduArticleGetList.getSchoolId(newFeedId);
			loadData(newFeedId);
			if (eduArticleGetList.loaded(newFeedId)) {
				loadData(newFeedId);
			} else {
				LinkUrlDataRunable dataRunable = new LinkUrlDataRunable(getContext(), newFeedId, url, schoolId) {
					@Override
					protected void onPostExecute(String result) {
						super.onPostExecute(result);
						loadData(newFeedId);
					}
				};

				dataRunable.start();
			}

		}

		protected void loadData(String newFeedId) {
			final EduArticleGetList eduArticleGetList = new EduArticleGetList(getContext());
			final String url = eduArticleGetList.getLinkUrl(newFeedId);
			String data = eduArticleGetList.getLinkUrlData(newFeedId);
			String mainDes = eduArticleGetList.getMainDes(newFeedId);

			if (!CommonAndroid.isBlank(data)) {
//				feeddetail_web.getSettings().setLoadWithOverviewMode(true);
//				feeddetail_web.getSettings().setUseWideViewPort(true);
//				feeddetail_web.getSettings().setBuiltInZoomControls(true);
				feeddetail_web.loadDataWithBaseURL("http://one.edu.vn", data, "text/html", "UTF-8", null);
			} else if (!CommonAndroid.isBlank(url) && MOITUtils.isStartHtml(url)) {
//				feeddetail_web.getSettings().setLoadWithOverviewMode(true);
//				feeddetail_web.getSettings().setUseWideViewPort(true);
//				feeddetail_web.getSettings().setBuiltInZoomControls(true);
				feeddetail_web.loadUrl(url);
			} else {
				feeddetail_web.loadDataWithBaseURL("http://one.edu.vn", mainDes, "text/html", "UTF-8", null);
			}
		}
	}

	public void updateData(TypeMore typeMore, String id) {
		update();
	}
}