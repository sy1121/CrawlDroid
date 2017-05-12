package edu.iscas.expdroid.model;

import java.awt.event.TextEvent;
import java.util.ArrayList;
import java.util.List;

import edu.iscas.expdroid.model.TEvent.EventType;
import edu.iscas.expdroid.utils.TPair;

public class TStatement {
    public List<TPair> select;
    public List<TPair> performs;
    public EventType type;
    public TStatement(){
        select=new ArrayList<TPair>();
        performs=new ArrayList<TPair>();
        type=EventType.UIEvent;
    }
}
