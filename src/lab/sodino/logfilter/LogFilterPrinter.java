package lab.sodino.logfilter;


public class LogFilterPrinter {
	private static String pathInput = "D:\\Test\\20130630\\pkg.txt";
	public static void main(String[]args){
		LogParser parser = new LogParser();
		parser.setInputPath(pathInput);
		// ���������Tab��:��ͷ��
		parser.addTabItem("hardware_os");
		parser.addTabItem("qua");
		parser.addTabItem("upload_apn");
		parser.addTabItem("event_value");
		parser.addTabItem("event_time");
		
		// ������Ҫִ��URLDecode��Tab��
		parser.decodeTabItem("hardware_os");
		parser.decodeTabItem("event_value");
		
		// ����event_value����Ҫ�������
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