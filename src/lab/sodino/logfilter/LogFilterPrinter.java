package lab.sodino.logfilter;


public class LogFilterPrinter {
	private static String pathInput = "E:\\Test\\20130910\\all.txt";
	public static void main(String[]args){
		LogParser parser = new LogParser();
		parser.setInputPath(pathInput);
		// 设置输出的Tab项:表头项
//		parser.addTabItem("hardware_os");
		parser.addTabItem("qua");
		parser.addTabItem("upload_apn");
		parser.addTabItem("event_code");
		parser.addTabItem("event_value");
		parser.addTabItem("event_time");
		
		// 设置需要执行URLDecode的Tab项
		parser.decodeTabItem("hardware_os");
		parser.decodeTabItem("event_value");
		
		// 设置event_value中需要输出的项
		parser.addEventParam("param_NetworkInfo");
		parser.addEventParam("param_NetworkType");
		parser.addEventParam("param_respcode");
		parser.addEventParam("param_try_count");
		parser.addEventParam("param_ipUrl");
		parser.addEventParam("param_step");
		parser.addEventParam("param_epId");
		parser.addEventParam("param_eId");
		parser.addEventParam("param_key_seq");
		parser.addEventParam("param_encrypt_keys");
		parser.addEventParam("param_resp_content_type");
//		parser.addEventParam("param_isConnected");
		parser.addEventParam("param_error");
		Params lastParams = null;
		for(int i = 0;i < arrFailCode.length;i ++){
			if(lastParams != null){
				parser.removeEventParam(lastParams);
			}
			
			System.out.println("Print FailCode:" + arrFailCode[i].failCode +" detail:"+ arrFailCode[i].detail);
			lastParams = parser.addEventParam("param_FailCode:" + arrFailCode[i].failCode);
			parser.print();
			System.out.println("---------------------------------end----------------------------\n\n");
		}
	}
	
	
	public static FailCodeUnit []arrFailCode = new FailCodeUnit[]{
		new FailCodeUnit(11000,"过滤码1"),
		new FailCodeUnit(11001,"过滤码2"),
};
}