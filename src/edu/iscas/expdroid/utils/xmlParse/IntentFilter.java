
package edu.iscas.expdroid.utils.xmlParse;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class IntentFilter {
   private String action;
   private String category;
   private String data;
   private Map<String, String> mAttributes = new LinkedHashMap<String, String>();
   
	   public String getAction() {
		return action;
	}
	
	public String getCategory() {
		return  mAttributes.get("category");
	}
	
	public String getData() {
		return  mAttributes.get("data");
	}
	
	public Map<String, String> getmAttributes() {
		return mAttributes;
	}
	
	public void setAction(String action) {
		this.action = action;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
	
	public void setData(String data) {
		this.data = data;
	}
	
	public void setmAttributes(Map<String, String> mAttributes) {
		this.mAttributes = mAttributes;
	}
	
	
	   public void addAtrribute(String key, String value) {
	       mAttributes.put(key, value);
	   }
	
	   public Map<String, String> getAttributes() {
	       return Collections.unmodifiableMap(mAttributes);
	   }
	   
		public String getAttributeByKey(String key){
		    	return mAttributes.get(key)+"";
		}
}
