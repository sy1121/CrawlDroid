package edu.iscas.expdroid.utils.xmlParse;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import edu.iscas.expdroid.shelltools.Rec;
import edu.iscas.expdroid.utils.FileOper;

public class ParseXML {
	 private static DefaultHandler defhandler;
     private static  XMLReader reader;
	 public static void init(DefaultHandler handler){
		    try {   
		    // 创建解析工厂  
	        SAXParserFactory factory = SAXParserFactory.newInstance();  
	        // 创建解析器  
	        SAXParser parser = factory.newSAXParser();  
	        // 得到读取器 
		    reader = parser.getXMLReader();
	        // 设置内容处理器  
	        defhandler = handler;  
	        reader.setContentHandler(handler);
		    } catch (SAXException | ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
     }
	 
	 
	 public static List<TActivity> getActs(String filePath){
		 // 读取xml文档  
	        try {
				reader.parse(filePath);
			} catch (IOException | SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
	        
	        ArrayList<TActivity> list = ((ActivityHandler)defhandler).getActivitys();
	        TActivity act;  
	        for(int i = 0; i < list.size(); i++) {  
	        	act = new TActivity();  
	        	act = (TActivity) list.get(i);  
	            System.out.println("No."+act.getNo()+" name="+act.getName()+"isExpored="+(act.isExported()||!act.getFilters().isEmpty()));
	            
	        }  
	        return list;
	 }
	 
	 public static void storeToFile(String xmlFilePath,String targetFile){
		 // 读取xml文档  
	        try {
				reader.parse(xmlFilePath);
			} catch (IOException | SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
	        
	        ArrayList<TActivity> list = ((ActivityHandler)defhandler).getActivitys();
	        TActivity act; 
	        StringBuffer sb=new StringBuffer();
	        sb.append("\n");
	        sb.append("count of activities: "+list.size()+"\n");
	        for(int i = 0; i < list.size(); i++) {  
	        	act = (TActivity) list.get(i);  
	        	String fullActName=act.getName().startsWith(".")?Rec.testedPackage+act.getName():act.getName();
	        	sb.append("No."+act.getNo()+" name="+fullActName+" isExpored="+(act.isExported()||!act.getFilters().isEmpty())+"\n");
	           // System.out.println("No."+act.getNo()+" name="+act.getName()+"isExpored="+(act.isExported()||!act.getFilters().isEmpty()));
	        }  
	        sb.append("\n");
	        FileOper.createFile(new File(targetFile), true);
	        FileOper.WriteStringToFile(targetFile, sb.toString());
	 }
	 
	 public List<String> getExploredActivities(String filePath){
		 List<String> rlist=new ArrayList<String>();
		 List<TActivity> acts=getActs(filePath);
		 for(TActivity act:acts){
			 if(act.isExported()||!act.getFilters().isEmpty()){
				 String newName=act.getName().charAt(0)=='.'?act.getName().substring(1):act.getName();
				 rlist.add(newName);
			      System.out.println(newName+" is explored activity");
			 }
		 }
		 return rlist;
	 }
	 
	/* public static void main(String[] args){
		 ParseXML parse=new ParseXML();
		 parse.init(new ActivityHandler());
		 String filePath="D:\\as-projects\\CloudShare\\AndroidManifest.xml";
		 List<TActivity> list=parse.getActs(filePath);
	 }*/
}
