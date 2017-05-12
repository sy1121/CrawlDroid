package edu.iscas.expdroid.model;

import java.util.List;

public class TActivity {
     public String name;
     public List<TState> states; 
     
     public TActivity(String aname,List<TState> astates){
    	 name=aname;
    	 states=astates;
     }

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if(obj==null) return false;
	    TActivity act=(TActivity)obj;
	    //suppose no activity name is same 
		return this.name.equals(act.name);
	}
     
     
     
}
