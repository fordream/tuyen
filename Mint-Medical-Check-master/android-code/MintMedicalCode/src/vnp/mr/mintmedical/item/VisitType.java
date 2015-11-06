package vnp.mr.mintmedical.item;

import org.json.JSONObject;

import vnp.mr.mintmedical.base.BaseItem;

public class VisitType extends BaseItem {
	public VisitType(JSONObject jsonObject) {
		try {
			key = jsonObject.getString("key");
			value = jsonObject.getString("value");
			desc = jsonObject.getString("desc");
		} catch (Exception exception) {
		
		}
	}

	public String key, value,desc;

}