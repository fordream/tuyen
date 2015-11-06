package vnp.mr.mintmedical.item;

import vnp.mr.mintmedical.base.BaseItem;

public class DoctorEvent extends BaseItem {
	public DoctorEvent() {
	}

	public DoctorEvent(String temp) {
		start = temp;
	}

	public String id, title, doctor_id, start, end, status, url;
}
