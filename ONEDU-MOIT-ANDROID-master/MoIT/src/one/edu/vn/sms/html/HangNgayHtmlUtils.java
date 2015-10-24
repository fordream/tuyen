package one.edu.vn.sms.html;

import java.util.List;

import com.vnp.core.common.CommonAndroid;
import com.vnp.core.common.ImageLoader;
import com.vnp.core.common.LogUtils;
import com.vnp.core.common.VnpFileCache;

import android.app.Activity;
import android.content.Context;
import one.edu.vn.sms.R;
import vnp.org.moit.adapter.DiemHangNgay;
import vnp.org.moit.utils.MOITUtils;

public class HangNgayHtmlUtils {
	public static int width = 0;

	public static void setWidth(Activity activity) {
		width= CommonAndroid.DEVICEID.getWidth(activity);
	}

	public static String getPath(Context context, String name) {
		VnpFileCache vnpFileCache = new VnpFileCache(context, "html");
		String fileName = vnpFileCache.getFile("1").getParent() + "/" + name + ".html";

		return fileName;
	}

	public static void post(List<DiemHangNgay> list, Context context, String name) {
		StringBuilder builder = new StringBuilder();
		builder.append(getCss());
		builder.append(getHtmlTop());
		for (int i = 0; i < list.size(); i++) {
			DiemHangNgay diemHangNgay = list.get(i);

			if (i == 0) {
				builder.append(getTrTop(diemHangNgay.mon, diemHangNgay.loai, diemHangNgay.diem));
			} else {
				builder.append(getTrTitem(diemHangNgay.mon, diemHangNgay.loai, diemHangNgay.diem));
			}
		}
		builder.append(getHtmlBot());
		int d50 = ImageLoader.convertDipToPixels(context.getResources().getDimension(R.dimen.dimen_40dp), context);
		int d12 = ImageLoader.convertDipToPixels(context.getResources().getDimension(R.dimen.dimen_12dp), context);
		CommonAndroid.saveToFile(builder.toString().replace("50px", d50 + "").replace("15px", d12 + ""), getPath(context, name));
	}

	public static String getCss() {

		StringBuilder builder = new StringBuilder();
		builder.append("<style>").append("\n");
		builder.append("	.table-setting{").append("\n");
		builder.append("		border-width: thin;border-spacing: 0px;border-style: none;border-color: black;").append("\n");
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
		builder.append("<table border='1' width='99%' class='table-setting'>").append("\n");
		return builder.toString();
	}

	public static String getTrTop(String name, String type, String diem) {
		StringBuilder builder = new StringBuilder();
		builder.append("	<tr height='50px'>").append("\n");
		builder.append("		<td width='33%'  class='table-header' >name1</td>").append("\n");
		builder.append("		<td width='33%' class='table-header' >name2</td>").append("\n");
		builder.append("		<td width='33%' class='table-header' >diem100</td>").append("\n");
		builder.append("	</tr>").append("\n");
		builder.append("	").append("\n");
		return builder.toString().replace("name1", name).replace("name2", type).replace("diem100", diem);
	}

	public static String getTrTitem(String name, String type, String diem) {

		if ("-1".equals(diem))
			diem = "-";
		int res = MOITUtils.getResDrawable(diem, null);
		String none = "none";
		if (res == R.drawable.tvang) {
			none = "vang";
		} else if (res == R.drawable.tbac) {
			none = "bac";
		}

		StringBuilder builder = new StringBuilder();
		builder.append("	<tr height='50px'>").append("\n");
		builder.append("		<td width='33%'  class='table-header-none' >name1</td>").append("\n");
		builder.append("		<td width='33%' class='table-header-none' >name2</td>").append("\n");
		builder.append("		<td width='33%' class='table-header-none' >").append("\n");
		builder.append("			<div width='50px' height='50px' align ='center' >").append("\n");
		builder.append("				<center class='nonexxx'><div class='diem' class = 'text-white'>diem100</div></center>	").append("\n");
		builder.append("			</div>").append("\n");
		builder.append("		</td>").append("\n");
		builder.append("	</tr>").append("\n");
		builder.append("	").append("\n");
		return builder.toString().replace("name1", name).replace("name2", type).replace("diem100", diem).replace("nonexxx", none);
	}

	public static String getHtmlBot() {
		StringBuilder builder = new StringBuilder();
		builder.append("</table>").append("\n");
		return builder.toString();
	}

}
