package one.edu.vn.sms.html;

import java.util.List;

import one.edu.vn.sms.R;
import vnp.org.moit.adapter.DiemCaNam;
import vnp.org.moit.utils.MOITUtils;
import android.app.Activity;
import android.content.Context;

import com.vnp.core.common.CommonAndroid;
import com.vnp.core.common.ImageLoader;
import com.vnp.core.common.VnpFileCache;

public class TongKetHtmlUtils {
	public static int width = 0;

	public static void setWidth(Activity activity) {
		width = CommonAndroid.DEVICEID.getWidth(activity);
		ImageLoader.convertDipToPixels(width, activity);
	}

	public static String getPath(Context context, String name) {
		VnpFileCache vnpFileCache = new VnpFileCache(context, "html");
		String fileName = vnpFileCache.getFile("1").getParent() + "/" + name + ".html";

		return fileName;
	}

	public static void post(String name, List<DiemCaNam> list, Context context) {
		StringBuilder builder = new StringBuilder();
		builder.append(getCss());
		builder.append(getHtmlTop());
		for (int i = 0; i < list.size(); i++) {
			DiemCaNam diemHangNgay = list.get(i);

			if (i == 0) {
				builder.append(getTrTop(diemHangNgay.mon, diemHangNgay.hk1, diemHangNgay.hk2, diemHangNgay.cn));
			} else if (i == list.size() - 1) {
				builder.append(getTrLast(diemHangNgay.mon, diemHangNgay.hk1, diemHangNgay.hk2, diemHangNgay.cn));
			} else {
				builder.append(getTrTitem(diemHangNgay.mon, diemHangNgay.hk1, diemHangNgay.hk2, diemHangNgay.cn));
			}
		}
		builder.append(getHtmlTop());
		float scale = ((float) width) / 640f;
		int d50 = ImageLoader.convertDipToPixels(context.getResources().getDimension(R.dimen.dimen_40dp), context);
		int d12 = ImageLoader.convertDipToPixels(context.getResources().getDimension(R.dimen.dimen_12dp), context);
		CommonAndroid.saveToFile(builder.toString().replace("50px", d50 + "px").replace("15px", (d12) + "px"), getPath(context, name));
	}

	public static String getCss() {
		StringBuilder builder = new StringBuilder();
		builder.append("<style>").append("\n");
		builder.append("	.table-setting{").append("\n");
		builder.append("		border-style: sold; border-width:1px;border-spacing: 0px;border-color: #585858;").append("\n");
		builder.append("	}").append("\n");
		builder.append("	.table-header{").append("\n");
		builder.append("		font-size:15px;").append("\n");
		builder.append("		background-color: #75cbc4;text-align: center;").append("\n");
		builder.append("		color: #585858;").append("\n");
		builder.append("	}").append("\n");
		builder.append("	").append("\n");
		builder.append("	.table-header-none{").append("\n");
		builder.append("		font-size:15px;").append("\n");
		builder.append("		text-align: center;").append("\n");
		builder.append("		color: #585858;").append("\n");
		builder.append("	}").append("\n");
		builder.append("	").append("\n");
		builder.append("	.table-header-bot{").append("\n");
		builder.append("		font-size:15px;").append("\n");
		builder.append("		text-align: center;").append("\n");
		builder.append("		color: #585858;").append("\n");
		builder.append("		font-weight: bold;").append("\n");
		builder.append("	}").append("\n");
		builder.append("	").append("\n");
		builder.append("	.diem{").append("\n");
		builder.append("		height: 50px; width: 50px; text-align: center;display: table-cell; vertical-align: middle;").append("\n");
		builder.append("	}").append("\n");
		builder.append("	.vang{").append("\n");
		builder.append("		color: while;").append("\n");
		builder.append("		text-align: center;background-image: url(tvang.png); ").append("\n");
		builder.append("		height: 50px; width: 50px; background-size: 50px 50px;").append("\n");
		builder.append("		color: #FFFFFF;").append("\n");
		builder.append("	}").append("\n");
		builder.append("	.bac{").append("\n");
		builder.append("		").append("\n");
		builder.append("		text-align: center;background-image: url(tbac.png); ").append("\n");
		builder.append("		height: 50px; width: 50px; background-size: 50px 50px;").append("\n");
		builder.append("		color: #FFFFFF;").append("\n");
		builder.append("	}").append("\n");
		builder.append("	.dong{").append("\n");
		builder.append("		").append("\n");
		builder.append("		text-align: center;background-image: url(tdong.png); ").append("\n");
		builder.append("		height: 50px; width: 50px; background-size: 50px 50px;").append("\n");
		builder.append("		color: #FFFFFF;").append("\n");
		builder.append("	}").append("\n");
		builder.append("	.none{	").append("\n");
		builder.append("		text-align: center;background-image: url(transfer.png); ").append("\n");
		builder.append("		height: 50px; width: 50px; background-size: 50px 50px;").append("\n");
		builder.append("		color: #585858;").append("\n");
		builder.append("	}").append("\n");
		builder.append("	").append("\n");
		builder.append("	.trdiv{").append("\n");
		builder.append("		height: 50px;").append("\n");
		builder.append("	}").append("\n");
		builder.append("	.tddiv{").append("\n");
		builder.append("		height: 50px; width: 50px;").append("\n");
		builder.append("		").append("\n");
		builder.append("	}").append("\n");
		builder.append("</style>").append("\n");

		return builder.toString();
	}

	public static String getHtmlTop() {
		StringBuilder builder = new StringBuilder();
		// style='width:640px'
		builder.append(" <body ><table  width='100%' class='table-setting'>").append("\n");
		return builder.toString();
	}

	public static String getTrTop(String name, String hk1, String hk2, String cn) {

		StringBuilder builder = new StringBuilder();
		builder.append("	<tr height='50px'>").append("\n");
		builder.append("		<td width='25%'  class='table-header' >name1</td>").append("\n");
		builder.append("		<td width='25%' class='table-header' >name2</td>").append("\n");
		builder.append("		<td width='25%' class='table-header' >diem100</td>").append("\n");
		builder.append("		<td width='25%' class='table-header' >diem101</td>").append("\n");
		builder.append("	</tr>").append("\n");
		builder.append("	").append("\n");
		return builder.toString().replace("name1", name).replace("name2", hk1).replace("diem100", hk2).replace("diem101", cn);
	}

	public static String getTrTitem(String name, String hk1, String hk2, String cn) {
		if ("-1".equals(hk1))
			hk1 = "-";
		if ("-1".equals(hk2))
			hk2 = "-";
		if ("-1".equals(cn))
			cn = "-";
		int res = MOITUtils.getResDrawable(hk1, null);
		String noneHk1 = "none";
		if (res == R.drawable.tvang) {
			noneHk1 = "vang";
		} else if (res == R.drawable.tbac) {
			noneHk1 = "bac";
		}
		int resHk2 = MOITUtils.getResDrawable(hk2, null);
		String noneHk2 = "none";
		if (resHk2 == R.drawable.tvang) {
			noneHk2 = "vang";
		} else if (resHk2 == R.drawable.tbac) {
			noneHk2 = "bac";
		}

		int resCn = MOITUtils.getResDrawable(cn, null);
		String noneCn = "none";
		if (resCn == R.drawable.tvang) {
			noneCn = "vang";
		} else if (resCn == R.drawable.tbac) {
			noneCn = "bac";
		}

		StringBuilder builder = new StringBuilder();
		builder.append("	<tr height='50px'>").append("\n");
		builder.append("		<td width='25%'  class='table-header-none' >name1</td>").append("\n");

		builder.append("		<td width='25%' class='table-header-none' >").append("\n");
		builder.append("			<div width='50px' height='50px' align ='center' >").append("\n");
		builder.append("				<center class='nonehk1'><div class='diem' class = 'text-white'>name2</div></center>	").append("\n");
		builder.append("			</div>").append("\n");
		builder.append("		</td>").append("\n");

		builder.append("		<td width='25%' class='table-header-none' >").append("\n");
		builder.append("			<div width='50px' height='50px' align ='center' >").append("\n");
		builder.append("				<center class='nonehk2'><div class='diem' class = 'text-white'>diem100</div></center>	").append("\n");
		builder.append("			</div>").append("\n");
		builder.append("		</td>").append("\n");

		builder.append("		<td width='25%' class='table-header-none' >").append("\n");
		builder.append("			<div width='50px' height='50px' align ='center' >").append("\n");
		builder.append("				<center class='nonecn'><div class='diem' class = 'text-white'>diem101</div></center>	").append("\n");
		builder.append("			</div>").append("\n");
		builder.append("		</td>").append("\n");

		builder.append("	</tr>").append("\n");
		builder.append("	").append("\n");
		return builder.toString().replace("name1", name).replace("name2", hk1).replace("diem100", hk2).replace("diem101", cn).replace("nonehk1", noneHk1).replace("nonehk2", noneHk2)
				.replace("nonecn", noneCn);
	}

	public static String getTrLast(String name, String hk1, String hk2, String cn) {
		if ("-1".equals(hk1))
			hk1 = "-";
		if ("-1".equals(hk2))
			hk2 = "-";
		if ("-1".equals(cn))
			cn = "-";
		int res = MOITUtils.getResDrawable(hk1, null);
		String noneHk1 = "none";
		if (res == R.drawable.tvang) {
			noneHk1 = "vang";
		} else if (res == R.drawable.tbac) {
			noneHk1 = "bac";
		}
		int resHk2 = MOITUtils.getResDrawable(hk2, null);
		String noneHk2 = "none";
		if (resHk2 == R.drawable.tvang) {
			noneHk2 = "vang";
		} else if (resHk2 == R.drawable.tbac) {
			noneHk2 = "bac";
		}

		int resCn = MOITUtils.getResDrawable(cn, null);
		String noneCn = "none";
		if (resCn == R.drawable.tvang) {
			noneCn = "vang";
		} else if (resCn == R.drawable.tbac) {
			noneCn = "bac";
		}

		StringBuilder builder = new StringBuilder();
		builder.append("	<tr height='50px'>").append("\n");
		builder.append("		<td width='25%'  class='table-header-bot' >name1</td>").append("\n");

		builder.append("		<td width='25%' class='table-header-bot' >").append("\n");
		builder.append("			<div width='50px' height='50px' align ='center' >").append("\n");
		builder.append("				<center class='nonehk1'><div class='diem' class = 'text-white'>name2</div></center>	").append("\n");
		builder.append("			</div>").append("\n");
		builder.append("		</td>").append("\n");

		builder.append("		<td width='25%' class='table-header-bot' >").append("\n");
		builder.append("			<div width='50px' height='50px' align ='center' >").append("\n");
		builder.append("				<center class='nonehk2'><div class='diem' class = 'text-white'>diem100</div></center>	").append("\n");
		builder.append("			</div>").append("\n");
		builder.append("		</td>").append("\n");

		builder.append("		<td width='25%' class='table-header-bot' >").append("\n");
		builder.append("			<div width='50px' height='50px' align ='center' >").append("\n");
		builder.append("				<center class='nonecn'><div class='diem' class = 'text-white'>diem101</div></center>	").append("\n");
		builder.append("			</div>").append("\n");
		builder.append("		</td>").append("\n");

		builder.append("	</tr>").append("\n");
		builder.append("	").append("\n");
		return builder.toString().replace("name1", name).replace("name2", hk1).replace("diem100", hk2).replace("diem101", cn).replace("nonehk1", noneHk1).replace("nonehk2", noneHk2)
				.replace("nonecn", noneCn);
	}

	public static String getHtmlBot() {
		StringBuilder builder = new StringBuilder();
		builder.append("</table> </body>").append("\n");
		return builder.toString();
	}

}
