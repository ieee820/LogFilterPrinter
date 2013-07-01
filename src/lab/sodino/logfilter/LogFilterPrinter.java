package lab.sodino.logfilter;


public class LogFilterPrinter {
	private static String pathInput = "D:\\Test\\20130630\\pkg.txt";
	public static void main(String[]args){
		LogParser parser = new LogParser();
		parser.setInputPath(pathInput);
		// 设置输出的Tab项:表头项
		parser.addTabItem("hardware_os");
		parser.addTabItem("qua");
		parser.addTabItem("upload_apn");
		parser.addTabItem("event_value");
		parser.addTabItem("event_time");
		
		// 设置需要执行URLDecode的Tab项
		parser.decodeTabItem("hardware_os");
		parser.decodeTabItem("event_value");
		
		// 设置event_value中需要输出的项
		parser.addEventParam("param_NetworkType");
		parser.addEventParam("param_IMEI");
		parser.addEventParam("param_encrypt_keys:1002");
		parser.addEventParam("param_epId");
		parser.addEventParam("param_FailCode");
		parser.addEventParam("param_isConnected");
		parser.addEventParam("param_error");
		parser.print();
	}
}