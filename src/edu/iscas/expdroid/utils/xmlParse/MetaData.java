package edu.iscas.expdroid.utils.xmlParse;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class MetaData {
	 private Map<String, String> mAttributes = new LinkedHashMap<String, String>();
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
