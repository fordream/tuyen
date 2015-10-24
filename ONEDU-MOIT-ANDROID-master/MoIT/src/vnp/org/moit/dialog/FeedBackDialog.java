package vnp.org.moit.dialog;

import one.edu.vn.sms.R;
import vnp.org.moit.service.SynEduSevice;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.acv.cheerz.db.DataStore;
import com.vnp.core.base.BaseAdialog;
import com.vnp.core.common.CommonAndroid;
import com.vnp.core.common.LogUtils;

public class FeedBackDialog extends BaseAdialog implements android.view.View.OnClickListener {
	String smsid;
	private String keyShow = "";
	private String keyTitle = "";

	private void setBackgroundEditText(int res, boolean errorBackground) {
		CommonAndroid.getView(this, res).setBackgroundResource(errorBackground ? R.drawable.bg_editext_quare_error : android.R.color.transparent);
	}

	public FeedBackDialog(Context context, String smsid, String keyShow, String keyTitle) {
		super(context);
		this.smsid = smsid;
		this.keyShow = keyShow;
		this.keyTitle = keyTitle;
		setCancelable(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setCancelable(true);
		CommonAndroid.setText(this, R.id.forum_top_text, String.format(getContext().getString(R.string.phanhoitinnhan), smsid));
		CommonAndroid.setText(this, R.id.forum_top_text, String.format(getContext().getString(R.string.phanhoitinnhan), ""));
		findViewById(R.id.newfeeddetail_back).setOnClickListener(this);
		findViewById(R.id.forum_top_dang).setOnClickListener(this);
		EditText editText = CommonAndroid.getView(this, R.id.editText1);
		DataStore.getInstance().init(getContext());
		editText.setText(keyShow);
	}

	@Override
	public int getLayout() {
		return R.layout.feedback;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.newfeeddetail_back) {
			dismiss();
		} else if (v.getId() == R.id.forum_top_dang) {
			final String str = ((EditText) findViewById(R.id.editText1)).getText().toString();
			setBackgroundEditText(R.id.editText1, false);
			if (CommonAndroid.isBlank(str)) {
				new ThongBaoDialog(getContext(), getContext().getString(R.string.banphainhapfeedback), null, getContext().getString(R.string.ok)).show();
				setBackgroundEditText(R.id.editText1, true);
				return;
			}

			if (!CommonAndroid.NETWORK.haveConnected(getContext())) {
				new ThongBaoDialog(getContext(), getContext().getString(R.string.thuchienkhongthanhcong), null, getContext().getString(R.string.ok)).show();
				return;
			}

			SynEduSevice synEduSevice = new SynEduSevice(getContext(), "SMSFeedback", true) {
				@Override
				public void onError(ErrorType errorType, Exception exception) {
					super.onError(errorType, exception);
					DataStore.getInstance().save(keyShow, str);
					new ThongBaoDialog(getContext(), getContext().getString(R.string.thuchienkhongthanhcong), null, getContext().getString(R.string.dong)).show();
				}

				@Override
				public void onSucsses(String parseCode, String parseDescription, String parseData, int responseCode, String responseMessage, String response) {
					super.onSucsses(parseCode, parseDescription, parseData, responseCode, responseMessage, response);

					DataStore.getInstance().save(keyShow, str);

					new ThongBaoDialog(getContext(), getContext().getString(R.string.feedbackthanhcong), null, getContext().getString(R.string.dong)) {
						public void onClickButton2() {
							FeedBackDialog.this.dismiss();
						};
					}.show();

				}
			};
			String comment = keyTitle + "\n" + str;
			LogUtils.e("commentxxx", comment);
			Bundle extras = new Bundle();
			extras.putString("smsid", smsid);
			extras.putString("desciption", comment);
			synEduSevice.execute(extras);
		}
	}
}