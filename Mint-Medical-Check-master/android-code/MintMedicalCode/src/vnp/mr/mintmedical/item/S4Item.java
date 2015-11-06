package vnp.mr.mintmedical.item;

import vnp.mr.mintmedical.base.BaseItem;

public class S4Item extends BaseItem {
	public S4Item() {
	}

	public String id;
	public String typeS4AAppointment;
	public String typeS4BVisitType;
	public String typeS4DAvailabilityDoctorEventId;
	public String typeS4CReason;
	
	
	public boolean isUpcoming ;
	public String toJson() {
		String parttern = "	\"%s\":\"%s\"";
		StringBuilder builder = new StringBuilder();
		builder.append("{\n");
		builder.append(String.format(parttern,"id", "")).append(",\n");
		builder.append(String.format(parttern,"typeS4AAppointment", typeS4AAppointment)).append(",\n");
		builder.append(String.format(parttern,"typeS4BVisitType", typeS4BVisitType)).append(",\n");
		builder.append(String.format(parttern,"typeS4DAvailabilityDoctorEventId", typeS4DAvailabilityDoctorEventId)).append(",\n");
		builder.append(String.format(parttern,"typeS4CReason", typeS4CReason)).append(",\n");
		builder.append(String.format(parttern,"isUpcoming", isUpcoming)).append("\n");
		builder.append("}");
		return builder.toString();
	}
}
