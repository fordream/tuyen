package vnp.mr.mintmedical;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import vnp.mr.mintmedical.base.DemoMD5;
import vnp.mr.mintmedical.base.MbaseAdialog;
import vnp.mr.mintmedical.base.MintUtils;
import vnp.mr.mintmedical.view.OfficeItemView;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.viewpagerindicator.db.DBConfig;
import com.viewpagerindicator.db.DBUserLogin;
import com.vnp.core.callback.CallBack;
import com.vnp.core.callback.ExeCallBack;
import com.vnp.core.callback.ExeCallBackOption;
import com.vnp.core.callback.ResClientCallBack;
import com.vnp.core.common.BaseAdapter;
import com.vnp.core.common.VNPResize;
import com.vnp.core.common.BaseAdapter.CommonGenderView;
import com.vnp.core.service.RestClient;
import com.vnp.core.service.RestClient.RequestMethod;
import com.vnp.core.view.CustomLinearLayoutView;

public class S3Activity extends MbaseAdialog implements OnClickListener {
	DBUserLogin userLogin;

	public S3Activity(Context context, OnClickListener clickListener) {
		super(context, clickListener);

		userLogin = new DBUserLogin(getContext());
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		findViewById(R.id.loginactivity_button1).setOnClickListener(this);
		findViewById(R.id.loginactivity_button2).setOnClickListener(this);
		findViewById(R.id.loginactivity_butto3).setOnClickListener(this);
		findViewById(R.id.loginactivity_btncancel).setOnClickListener(this);
		onResume();

		ExeCallBack exeCallBack = new ExeCallBack();
		exeCallBack.setExeCallBackOption(new ExeCallBackOption(getContext(),
				true, R.string.loadding));
		exeCallBack.executeAsynCallBack(new ResClientCallBack() {
			@Override
			public void onCallBack(Object arg0) {
				RestClient restClient = (RestClient) arg0;
				DBConfig dbConfig = new DBConfig(getContext());
				if (restClient.getResponseCode() == 200) {
					dbConfig.save(restClient.getResponse());
				}
			}

			@Override
			public String getUrl() {
				return MintUtils.URL_VISITYPE;
			}
		});
	}

	protected void onResume() {
		VNPResize resize = VNPResize.getInstance();
		resize.resizeSacle(findViewById(R.id.loginactivity_img),
				LayoutParams.MATCH_PARENT, 129);
		resize.resizeSacle(findViewById(R.id.loginactivity_button1), 300, 45);
		resize.resizeSacle(findViewById(R.id.loginactivity_button2), 266, 46);
		resize.resizeSacle(findViewById(R.id.loginactivity_butto3), 300, 45);

		resize.resizeSacle(findViewById(R.id.loginactivity_btncancel), 90, 40);
		resize.resizeSacle(findViewById(R.id.loginactivity_text), 530/2, 67);
//		resize.setTextsize(findViewById(R.id.loginactivity_button1),
//				MintUtils.TEXTSIZE_BUTTON);
//		resize.setTextsize(findViewById(R.id.loginactivity_button2),
//				MintUtils.TEXTSIZE_BUTTON);
//		resize.setTextsize(findViewById(R.id.loginactivity_butto3),
//				MintUtils.TEXTSIZE_BUTTON);

		resize.resizeSacle(findViewById(R.id.loginactivity_edt1_background),
				300, 48);
		resize.resizeSacle(findViewById(R.id.loginactivity_edt2_background),
				300, 46);
		resize.resizeSacle(findViewById(R.id.loginactivity_edt1), 280, 48);
		resize.resizeSacle(findViewById(R.id.loginactivity_edt2), 280, 46);

		resize.setTextsize(findViewById(R.id.loginactivity_edt1), 20);
		resize.setTextsize(findViewById(R.id.loginactivity_edt2), 20);

//		resize.setTextsize(findViewById(R.id.loginactivity_text),
//				MintUtils.TEXTSIZE_EDITTEXT);
	}

	public int getLayout() {
		return R.layout.s3ativity;
	}

	@Override
	public void onClick(View v) {
		DBConfig dbConfig = new DBConfig(getContext());
		if (v.getId() == R.id.loginactivity_button1) {
			showDialogLoading(true);
			final String email = ((EditText) findViewById(R.id.loginactivity_edt1))
					.getText().toString();
			final String password = ((EditText) findViewById(R.id.loginactivity_edt2))
					.getText().toString();
			new ExeCallBack().executeAsynCallBack(new CallBack() {
				@Override
				public void onCallBack(Object object) {
					showDialogLoading(false);
					RestClient client = (RestClient) object;
					if (client.getResponseCode() == 200) {
						try {
							JSONObject jsonObject = new JSONObject(client
									.getResponse());
							String custId = jsonObject.getString("custId");
							if (custId != null && !custId.trim().equals("")) {
								userLogin.save(client.getResponse());

								userLogin.saveLogin(true);
								clickListener.onClick(null, 0);

								dismiss();
							} else {
								Toast.makeText(getContext(), "Login Error1!",
										Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							Toast.makeText(getContext(),
									"Login Error! :" + client.getResponse(),
									Toast.LENGTH_SHORT).show();
						}
					} else {
						Toast.makeText(
								getContext(),
								client.getErrorMessage()
										+ client.getResponseCode(),
								Toast.LENGTH_SHORT).show();
					}
				}

				@Override
				public Object execute() {
					RestClient client = new RestClient(MintUtils.URL_LOGIN);
					client.addParam("email", email);

					try {
						client.addParam("password", DemoMD5.MD5(password));
						client.execute(RequestMethod.GET);
					} catch (Exception e) {
						e.printStackTrace();
					}

					return client;
				}
			});

		} else if (v.getId() == R.id.loginactivity_button2) {
			try {
				Intent myIntent = new Intent(Intent.ACTION_VIEW,
						Uri.parse(dbConfig.getForgotUrl()));
				getContext().startActivity(myIntent);
			} catch (Exception exception) {

			}
		} else if (v.getId() == R.id.loginactivity_butto3) {
			try {
				Intent myIntent = new Intent(Intent.ACTION_VIEW,
						Uri.parse(dbConfig.getForgotUrl()));
				getContext().startActivity(myIntent);
			} catch (Exception exception) {
			}
		} else if (v.getId() == R.id.loginactivity_btncancel) {
			dismiss();
		}
	}

	private ProgressDialog dialog;

	public void showDialogLoading(boolean isShow) {
		if (isShow) {
			if (dialog == null) {
				dialog = ProgressDialog.show(getContext(), null, getContext()
						.getString(R.string.loadding));
			}
		} else {
			if (dialog != null) {
				dialog.dismiss();
				dialog = null;
			}
		}

	}

	// public boolean isLogin() {
	// return DataStore.getInstance().get("ISLOGIN", false);
	// }
	//
	// public void saveLogin(boolean checked) {
	// DataStore.getInstance().save("ISLOGIN", checked);
	// }
}