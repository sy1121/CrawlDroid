package edu.iscas.expdroid.utils.xmlParse;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ActivityHandler extends DefaultHandler {  
    //将读取的内容存放到一个book对象中，存放到list集合中  
      
    ArrayList<TActivity> list = new ArrayList<TActivity>();  
    private TActivity act;  
    private IntentFilter filter;
    private MetaData metadata;
    private int actNo=1;
    @Override  
    public void startElement(String uri, String localName, String qName,  
            Attributes attributes) throws SAXException {  
        if("activity".equals(qName)) {  
        	act = new TActivity();
        	for (int i = 0; i < attributes.getLength(); i++) {
        		act.addAtrribute(attributes.getQName(i).replace("android:", ""), attributes.getValue(i));
            }
        	act.setNo(actNo++);
        }else if("intent-filter".equals(qName)){
             filter=new IntentFilter();
        	for (int i = 0; i < attributes.getLength(); i++) {
        		filter.addAtrribute(attributes.getQName(i).replace("android:", ""), attributes.getValue(i));
            }
        	
        }else if("meta-data".equals(qName)){
             metadata=new MetaData();
        	for (int i = 0; i < attributes.getLength(); i++) {
        		metadata.addAtrribute(attributes.getQName(i).replace("android:", ""), attributes.getValue(i));
            }
        }
    }  
  

    @Override  
    public void endElement(String uri, String localName, String qName)  
            throws SAXException {  
        if("activity".equals(qName)) {  
            list.add(act);  
            act = null;  
        }else if("intent-filter".equals(qName)) {
        	if(act!=null){
        	 act.getFilters().add(filter);
        	 filter=null;
        	}
        }else if("meta-data".equals(qName)){
        	if(act!=null){
        	 act.setMetadata(metadata);
        	 metadata=null;
        	}
        }
    }  
  
      
    public ArrayList<TActivity> getActivitys() {  
        return list;  
    }  
  
  
}  