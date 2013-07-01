package lab.sodino.logfilter;

public class Tab {
	public String name;
	public int idx = -1;
	public String [] values;
	public boolean decode = false;
	public Tab(String tab) {
		this.name = tab;
	}

	public boolean equals(Object obj){
		if(obj == null || obj instanceof Tab == false){
			return false;
		}
		Tab objTab = (Tab) obj;
		if(objTab.name != null && objTab.name.equals(name)){
			return true;
		}
		return false;
	}
	
	public static Tab createTab(String content){
		if(content == null){
			return null;
		}
		content = content.trim();
		if(content.length() == 0){
			return null;
		}
		Tab tab = null;
		if(content.contains(LogParser.COLON)){
			int idxColon = content.indexOf(LogParser.COLON);
			String name = content.substring(0, idxColon);
			tab = new Tab(name);
			String value = content.substring(idxColon + LogParser.COLON.length());
			String []arr = value.split(",");
			if(arr != null && arr.length > 0){
				tab.values = arr;
			}
		}else{
			tab = new Tab(content);
		}
		return tab;
	}

	public boolean isValiableValue(String content) {
		if(values == null || values.length == 0){
			return true;
		}
		for(String str:values){
			if(str.equals(content)){
				return true;
			}
		}
		return false;
	} 
}
