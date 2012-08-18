package scriptblock.options.time;

import scriptblock.SLAPI;
import scriptblock.managers.ScriptManager;
import scriptblock.options.SBOption;

public class Cooldown extends SBOption {
	
	private long[] cooldownParams = new long[3];

	public Cooldown(ScriptManager scriptManager) 
		{ super(scriptManager, "cooldown", "@cooldown:") ; }
	
	public boolean isValid()
	{
		scriptLine = scriptLine.replaceFirst(this.getSyntax(), "");
		long delay = ( Long.parseLong(scriptLine) )*60000;
		
		cooldownParams[0] = System.currentTimeMillis();
		cooldownParams[1] = delay;
		cooldownParams[2] = cooldownParams[0] + cooldownParams[1];	//TotalTime
		
	//Saving CooldownDataMap	

		mapManager.cooldownMap.put(blockCoords.getFullCoords(), cooldownParams);
		
		try {
			SLAPI.save(mapManager.cooldownMap, fileManager.getCooldownDataFile().getPath() );
			log.info("CoolDown Data saved !");
		} catch (Exception e){
			log.info("[ERROR] while saving CooldownBlockList to "+fileManager.getCooldownDataFile().getName()+" ![ERROR]");
			log.info("at "+e.getMessage());
		}
		
		return true ;
	}
}
