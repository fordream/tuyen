package vnp.org.moit.db;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;

import com.acv.cheerz.db.SkypeTable;
import com.vnp.core.common.CommonAndroid;

public class EduInbox extends SkypeTable {

	public EduInbox(Context context) {
		super(context, EduDBProvider.class);
		addColumns("phoneactive");
		addColumns("studentid");
		addColumns("smsid");
		addColumns("schoolid");
		addColumns("sender");
		// 5 --> thong bao diem hang ngay
		// 0 .. sen sms choi boi
		// bang theo doi hoc sinh
		// gui diem phay

		// ThongbaoTuNhapSo = 0,
		// ThongBaoDanhSachExcel = 1,
		// ThongBaoDanhSachTuyBien = 2,
		// ThongBaoDanhSachLop = 3,
		// BangTheoDoiHocSinh = 4,
		// DiemHangNgay = 5,
		// SucKhoeCanNang = 6,
		// DiemPhay = 7,
		// ThongBaoDanhSachGiaoVien = 8

		addColumns("sendtype");
		addColumns("timepost");
		addColumns("sms");
		addColumns("jSonMark");
		addColumns("sms_tiengviet");
	}

	public Cursor query() {
		String phone = new EduAccount(getContext()).getPhoneActive();
		String where = String.format("%s = '%s' ", "phoneactive", phone);
		return querry(where, "timepost DESC");
	}

	@Override
	public void add(JSONObject object) {
		String phone = new EduAccount(getContext()).getPhoneActive();
		String smsid = CommonAndroid.getString(object, "smsid");
		try {
			object.put("phoneactive", phone);
		} catch (JSONException e) {
		}

		String where = String.format("%s = '%s' and %s = '%s'", "phoneactive", phone, "smsid", smsid);
		if (has(where)) {
			update(createContentValuesFormJsonObject(object), where);
		} else {
			super.add(object);
		}
	}

	public void deleteAll() {
		String phone = new EduAccount(getContext()).getPhoneActive();
		String where = String.format("%s = '%s' ", "phoneactive", phone);
		delete(where);
	}

	public List<String> getListIds() {
		List<String> list = new ArrayList<String>();

		String phone = new EduAccount(getContext()).getPhoneActive();
		String where = String.format("%s = '%s' ", "phoneactive", phone);

		Cursor cursor = querry(where);
		while (cursor != null && cursor.moveToNext()) {
			String smsid = CommonAndroid.getString(cursor, "smsid");
			if (!CommonAndroid.isBlank(smsid) && !list.contains(smsid)) {
				list.add(smsid);
			}
		}

		if (cursor != null)
			cursor.close();
		return list;
	}

	public void deleteBySmsId(String smsId) {
		String where = String.format("%s = '%s' ", "smsid", smsId);
		delete(where);
	}

	public Cursor queryByStudentId(String studentId) {
		String phone = new EduAccount(getContext()).getPhoneActive();
		String where = String.format("%s = '%s' ", "phoneactive", phone);

		if (!CommonAndroid.isBlank(studentId)) {
			where = String.format("%s and %s = '%s'", where, "studentid", studentId);
		}
		return querry(where, "timepost DESC");
	}

	public Cursor queryByStudentIdNotDiem(String studentId) {
		// ThongbaoTuNhapSo = 0,
		// ThongBaoDanhSachExcel = 1,
		// ThongBaoDanhSachTuyBien = 2,
		// ThongBaoDanhSachLop = 3,
		// BangTheoDoiHocSinh = 4,
		// DiemHangNgay = 5,
		// SucKhoeCanNang = 6,
		// DiemPhay = 7,
		// ThongBaoDanhSachGiaoVien = 8
		String phone = new EduAccount(getContext()).getPhoneActive();
		String where = String.format("%s = '%s' and sendtype in (0,1,2,3,4,6,8) ", "phoneactive", phone);

		if (!CommonAndroid.isBlank(studentId)) {
			where = String.format("%s and %s = '%s' and sendtype in (0,1,2,3,4,6,8) ", where, "studentid", studentId);
		}
		return querry(where, "timepost DESC");
	}

	public Cursor queryByStudentIdBangDiemHangNgay(String studentId, boolean isDiemHangNgay) {
		// ThongbaoTuNhapSo = 0,
		// ThongBaoDanhSachExcel = 1,
		// ThongBaoDanhSachTuyBien = 2,
		// ThongBaoDanhSachLop = 3,
		// BangTheoDoiHocSinh = 4,
		// DiemHangNgay = 5,
		// SucKhoeCanNang = 6,
		// DiemPhay = 7,
		// ThongBaoDanhSachGiaoVien = 8
		String phone = new EduAccount(getContext()).getPhoneActive();
		String where = String.format("%s = '%s'  and sendtype in (5) ", "phoneactive", phone);

		if (!CommonAndroid.isBlank(studentId)) {
			where = String.format("%s and %s = '%s'  and sendtype in (5)", where, "studentid", studentId);
		}
		return querry(where, "timepost DESC");
	}

	public Cursor queryByStudentIdCanam(String studentId) {
		// ThongbaoTuNhapSo = 0,
		// ThongBaoDanhSachExcel = 1,
		// ThongBaoDanhSachTuyBien = 2,
		// ThongBaoDanhSachLop = 3,
		// BangTheoDoiHocSinh = 4,
		// DiemHangNgay = 5,
		// SucKhoeCanNang = 6,
		// DiemPhay = 7,
		// ThongBaoDanhSachGiaoVien = 8
		String phone = new EduAccount(getContext()).getPhoneActive();
		String where = String.format("%s = '%s'  and sendtype in (7) ", "phoneactive", phone);

		if (!CommonAndroid.isBlank(studentId)) {
			where = String.format("%s and %s = '%s'  and sendtype in (7)", where, "studentid", studentId);
		}
		return querry(where, "timepost DESC");
	}

	public Cursor queryByStudentId(String studentId, boolean isDiemHangNgay) {
		String phone = new EduAccount(getContext()).getPhoneActive();
		String where = String.format("%s = '%s' ", "phoneactive", phone);

		if (!CommonAndroid.isBlank(studentId)) {
			where = String.format("%s and %s = '%s'", where, "studentid", studentId);
		}
		return querry(where, "timepost DESC");
	}
}