package vnp.org.moit.service;

import android.content.Context;
import android.os.Bundle;

import com.vnp.core.service.asyn.ExecuteAPI;

public class CheckPhoneRunable extends IMRunable {
//	private EduProgressDialog progressDialog;

	public CheckPhoneRunable(Context context) {
		super(context, "checkPhone", null);

//		progressDialog = new EduProgressDialog(context);
//		progressDialog.show();
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
//		progressDialog.dismiss();
	}

	@Override
	public void run() {

		Bundle extras = createBundle();
		ExecuteAPI exception = execute(extras, "checkPhone");
		finishexecute(exception);

		String parseCode = exception.getParseCode();
		String parseData = exception.getParseData();
		String parseDescription = exception.getParseDescription();

		setParseCode(parseCode);
		setParseData(parseData);
		setParseDescription(parseDescription);
	}
}