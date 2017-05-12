package edu.iscas.expdroid.utils.xmlParse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TActivity {
     private int no;
	 private String name;
     private Map<String, String> mAttributes = new LinkedHashMap<String, String>();
     private List<IntentFilter> filters=new ArrayList<IntentFilter>();
     private MetaData metadata;
     
	 public void addAtrribute(String key, String value) {
         mAttributes.put(key, value);
     }

     public Map<String, String> getAttributes() {
         return Collections.unmodifiableMap(mAttributes);
     }
     
     public int getNo() {
 		return no;
 	}

 	public void setNo(int no) {
 		this.no = no;
 	}

 	public String getName() {
 		return getAttributeByKey("name");
 	}

 	public void setName(String name) {
 		this.name = name;
 	}

 	public Map<String, String> getmAttributes() {
 		return mAttributes;
 	}

 	public void setmAttributes(Map<String, String> mAttributes) {
 		this.mAttributes = mAttributes;
 	}
 	
 	public String getAttributeByKey(String key){
 	    	return mAttributes.get(key)+"";
 	}


	public MetaData getMetadata() {
		return metadata;
	}

	public void setMetadata(MetaData metadata) {
		this.metadata = metadata;
	}
	
	public boolean isExported(){
		return null!=getAttributeByKey("exported")&&getAttributeByKey("exported").equals("true");
	}

	public List<IntentFilter> getFilters() {
		return filters;
	}

	public void setFilters(List<IntentFilter> filters) {
		this.filters = filters;
	}

}
