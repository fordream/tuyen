package one.edu.vn.sms.html;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class GemCodeHtml {

	public GemCodeHtml() {
	}

	public static void main(String[] args) {

//		 String fileName =
//		 "C:\\Users\\truongvv\\Documents\\GitHub\\ONEDU-MOIT\\MoIT\\assets\\html\\testtongket.html";
//		 BufferedReader br = null;
//		 try {
//		
//		 String sCurrentLine;
//		
//		 br = new BufferedReader(new FileReader(fileName));
//		
//		 while ((sCurrentLine = br.readLine()) != null) {
//		 System.out.println(String.format("builder.append(\"%s\").append(\"\\n\");",
//		 sCurrentLine));
//		 }
//		
//		 } catch (IOException e) {
//		 e.printStackTrace();
//		 } finally {
//		 try {
//		 if (br != null)
//		 br.close();
//		 } catch (IOException ex) {
//		 ex.printStackTrace();
//		 }
//		 }

		System.out.println(TongKetHtmlUtils.getCss());
		System.out.println(TongKetHtmlUtils.getHtmlTop());
		System.out.println(TongKetHtmlUtils.getTrTop("mon","10","10","10"));
		System.out.println(TongKetHtmlUtils.getTrTitem("mon","7","7","10"));
		System.out.println(TongKetHtmlUtils.getTrTitem("mon","5","10","10"));
		System.out.println(TongKetHtmlUtils.getTrTitem("mon","8","10","10"));
		System.out.println(TongKetHtmlUtils.getTrLast("mon","9","10","10"));
		System.out.println(TongKetHtmlUtils.getHtmlBot());
	}

}
