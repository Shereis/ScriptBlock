package scriptblock.options.time;

import scriptblock.managers.ScriptManager;
import scriptblock.options.SBOption;


public class Delay extends SBOption implements Runnable {
	
	public Delay(ScriptManager scriptManager)
		{ super(scriptManager, "delay", "@delay:") ; }
	
	public boolean isValid()
	{		
		scriptLine = scriptLine.replaceFirst("@delay:", "");
		long delay = ( Long.parseLong(scriptLine) )*20;		
		mapManager.delayList.add( blockCoords.getFullCoords() ) ;
		
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, this , delay) ;
		
		return false ;
	}
		
	public void run() 
	{
		mapManager.delayList.remove( blockCoords.getFullCoords() ) ;
		optionHandler.readScript( index+1 ) ; 
	}

	
	
}
