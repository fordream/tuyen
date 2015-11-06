package vnp.mr.mintmedical.service;

import com.vnp.core.datastore.CommonTable;

public class UserTable extends CommonTable {

	@Override
	public void addColoumList() {
		addColoumName("loginid");
		addColoumName("password");
		addColoumName("custid");
		addColoumName("custname");
		addColoumName("status");
	}

	@Override
	public String getTableIDName() {
		return "custid";
	}

	@Override
	public String getTableName() {
		return "user";
	}
}