package vnp.mr.mintmedical.item;

import org.json.JSONObject;

import vnp.mr.mintmedical.base.BaseItem;

public class UserLogin extends BaseItem {
	public UserLogin() {
	}

	public String custId, email, status, custName;

	public void parse(String dataStr) {
		try {
			JSONObject jsonObject = new JSONObject(dataStr);
			custId = jsonObject.getString("custId");
			email = jsonObject.getString("email");
			status = jsonObject.getString("status");
			custName = jsonObject.getString("custName");
		} catch (Exception exception) {

		}
	}
}