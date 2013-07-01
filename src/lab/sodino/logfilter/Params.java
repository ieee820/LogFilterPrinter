package lab.sodino.logfilter;

import java.util.ArrayList;

public class Params {
	public String name;
	public String[] values;
	public static Params createParams(String paramContent) {
		if(paramContent == null){
			return null;
		}
		paramContent = paramContent.trim();
		if(paramContent.length() == 0){
			return null;
		}
		Params param = new Params();
		if(paramContent.contains(LogParser.COLON)){
			int idxColon = paramContent.indexOf(LogParser.COLON);
			String name = paramContent.substring(0, idxColon);
			param.name = name;
			String value = paramContent.substring(idxColon + LogParser.COLON.length());
			String []arr = value.split(",");
			if(arr != null && arr.length > 0){
				param.values = arr;
			}
		}else{
			param.name = paramContent;
		}
		return param;
	}
}
