package lab.sodino.logfilter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class LogParser {
	public static final String COLON = ":";
	public static final String SEMICOLON = ",";
	public static final String SEPERATOR = "	";
	public static final String SEPERATOR1 = "	";
	
	public static final String EVENT_VALUE = "event_value";
	public static final String SIGN_EQUAL = "%3D";
	public static final String SIGN_AND = "%26";
	private ArrayList<Tab> arrTabs = new ArrayList<Tab>();
	private ArrayList<Params> arrParams = new ArrayList<Params>();
	private File fileInput;
	private File fileOutput;
	public void addTabItem(String content) {
		if(content == null || content.length() == 0){
			return;
		}
		Tab tab = Tab.createTab(content);
		if(arrTabs.contains(tab)==false){
			arrTabs.add(tab);
		}
	}

	public Params addEventParam(String paramContent) {
		if(paramContent == null || paramContent.length() == 0){
			return null;
		}
		Params param = Params.createParams(paramContent);
		arrParams.add(param);
		return param;
	}

	public void removeEventParam(Params params) {
		if(arrParams != null){
			arrParams.remove(params);
		}
	}

	public void setInputPath(String input) {
		if(input == null || input.length() == 0){
			return;
		}
		fileInput = new File(input);
		System.out.println(fileInput.getName());
		Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("hh_mm_ss");
        String subfix = "_"+dateFormat.format(date) + ".txt";
		fileOutput = new File(fileInput.getParentFile().getAbsolutePath()+File.separator+(fileInput.getName().replace(".txt", subfix)));
		if(fileOutput.exists()){
			fileOutput.delete();
		}
		try {
			fileOutput.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void print(String hint) {
		if(fileInput == null){
			System.err.println("FileInput is null.");
		}
		
		parser(fileInput,hint);
	}

	private void parser(File file,String hint) {
		if(file == null){
			return;
		}
		
		BufferedReader reader = null;
//		FileOutputStream fos = null;
		RandomAccessFile raFos = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			
//			fos = new FileOutputStream(fileOutput);
			raFos = new RandomAccessFile(fileOutput, "rw");
			raFos.seek(fileOutput.length());
			if(hint != null){
				raFos.write(hint.getBytes());
			}
			//第一句为标题头 
			String tabHeader = "\n";
			for(int i = 0;i < arrTabs.size();i ++){
				tabHeader += arrTabs.get(i).name+SEPERATOR; 
			}
//			fos.write((tabHeader+"\n").getBytes());
			raFos.write((tabHeader+"\n").getBytes());
			String headerLine = reader.readLine();
			String[] arr = headerLine.split(SEPERATOR);
			if(arr == null || arr.length == 0){
				System.err.println(file.getAbsolutePath()+" does not exist any Table Name.");
				return;
			}
			boolean result = initTabIndex(arr);
			if(result == false){
				System.err.println("Init Tab Index fail.");
				return;
			}
			String line = null;
			while((line = reader.readLine())!=null){
				arr = line.split(SEPERATOR);
				if(arr == null || arr.length == 0){
					continue;
				}
				String handleLine = "";
				for(int i =0;i < arr.length;i ++){
					Tab tab = findTabByIndex(i);
					if(tab != null){
						String content = arr[i];
						if(EVENT_VALUE.equals(tab.name)){
							content = filterEventValue(content);
						}
						if(content == null){
							handleLine = "";
							break;
						}
						if(!tab.isValiableValue(content)){
							handleLine = "";
							break;
						}
						if(tab.decode){
							content = urlDecode(content);
						}
						handleLine += content+SEPERATOR;
					}
				}
				if(handleLine.length() > 0 ){
					handleLine += "\n";
					System.out.print(handleLine);
//					fos.write(handleLine.getBytes());
					raFos.write(handleLine.getBytes());
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try{
				if(reader != null){
					reader.close();
				}
//				if(fos != null){
//					fos.close();
//				}
				if(raFos != null){
					raFos.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	private String filterEventValue(String content) {
		String result = null;
		
		if(content == null || content.length() == 0//
			|| arrParams == null || arrParams.size() == 0){
			return null;
		}
		
		for(Params param:arrParams){
			String key = param.name;
			String start = key +SIGN_EQUAL;
			int idxStart = content.indexOf(start);
			if(idxStart == -1){
				if(isFilterValue(param.values, null)){
					continue;
				}else{
					return null;
				}
			}
			String end = SIGN_AND;
			int idxEnd = content.indexOf(end,idxStart + start.length());
			if(idxEnd == -1){
//				end = SEPERATOR;已经是最后一个了，直接将后面的字符串全部赋值为value
				idxEnd = content.length();
			}
			if(idxStart >= 0 && idxEnd > idxStart){
				String value = content.substring(idxStart+start.length(), idxEnd);
				boolean bool = isFilterValue(param.values,value);
				if(bool == false){
					return null;
				}
				if(result == null){
					result = key + SIGN_EQUAL + value;
				}else{
					result = result + SIGN_AND + key + SIGN_EQUAL + value;
				}
//				System.out.println("key["+key +"]value["+value+"]");
			}else{
				continue;
			}
		}
		return result;
	}

	private boolean isFilterValue(String[]arr, String value) {
		if(arr == null || arr.length == 0){
			return true;
		}
		if(value == null){
			return false;
		}
		for(String str : arr){
			if(str.equals(value)){
				return true;
			}
		}
		return false;
	}

	private boolean initTabIndex(String[]arr){
		if(arr == null){
			return false;
		}
		
		for(Tab tab : arrTabs){
			String tabName = tab.name;
			for(int i = 0;i < arr.length;i ++){
				if(arr[i].equals(tabName)){
					tab.idx = i;
				}
			}
		}
		return true;
	}
	
	private Tab findTabByIndex(int index){
		for(Tab tab:arrTabs){
			if(tab.idx == index){
				return tab;
			}
		}
		return null;
	}
	
	private String urlDecode(String encode){
		if(encode == null || encode.length() == 0){
			return null;
		}
		String decode = encode;
		int count = 0;
		while(decode.contains("%") && count < 2){
			count ++;
			try {
				decode = URLDecoder.decode(decode,"utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return decode;
	}

	public void decodeTabItem(String name) {
		if(name == null || name.length() == 0// 
			|| arrTabs == null || arrTabs.size() == 0){
			return ;
		}
		for(Tab tab:arrTabs){
			if(tab.name.equals(name)){
				tab.decode = true;
			}
		}
	}
}