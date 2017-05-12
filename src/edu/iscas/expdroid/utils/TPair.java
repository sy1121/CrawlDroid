package edu.iscas.expdroid.utils;

import java.io.Serializable;

public class TPair implements Serializable{
   public String  key;
   public String  value;
   public TPair(String k,String v){
	   this.key=k;
	   this.value=v;
   }
   
   public TPair(){
	   
   }
  
}
