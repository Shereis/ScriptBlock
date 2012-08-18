package scriptblock.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import scriptblock.command.BindScript;
import scriptblock.options.Option;


public class MapManager {	
	
	public HashMap < String, BindScript > commandsWaitingMap ;
	public HashMap < String, LinkedList <String> > blocksMap ;
	public HashMap < String, long[] > cooldownMap ;	
	public LinkedList< Option > optionsList ;
	public List <String> delayList ;
	
	public MapManager()
	{ 
		this.commandsWaitingMap = new HashMap<String, BindScript>() ;		
		this.blocksMap = new HashMap < String , LinkedList < String>> () ;
		this.cooldownMap = new HashMap < String, long[]>() ;
		this.optionsList = new LinkedList < Option >() ;
		this.delayList = new ArrayList< String >() ;		
	}
	
}
