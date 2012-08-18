package scriptblock.options.miscellaneous;

import scriptblock.managers.ScriptManager;
import scriptblock.options.SBOption;

public class Amount extends SBOption{
	
	public Amount(ScriptManager scriptManager) 
		{ super(scriptManager, "amount", "@amount:") ; }
	
	
	@SuppressWarnings("deprecation")
	public boolean isValid()
	{
		
		String amount = scriptLine.replace(this.getSyntax(), "");
		int remain = Integer.parseInt(amount)-1 ;
		String world = blockCoords.world ;
		String coords= blockCoords.getCoords() ;
		String fullCoords = blockCoords.getFullCoords() ;
		
		if (remain > 0){
			scriptLine = scriptLine.replaceFirst(amount, String.valueOf(remain) );
			script.set(index, scriptLine);
			mapManager.blocksMap.put( coords , script);
			
		//saving blocksMap and BlocksData.yml
			
			config.removeProperty(world+"."+coords+".");
			config.setProperty(world+"."+coords+".", script);
			config.save();
			
			log.info("Block ("+fullCoords+") Remaining Charge : "+remain);
					
		} 
		else {
			
		//saving blocksMap and BlocksData.yml
			mapManager.blocksMap.remove(fullCoords);
			config.removeProperty(world+"."+coords);
			config.save();
			
			
			log.info("["+plugin.getName()+"] No More Charge at : "+fullCoords+"...");
			log.info("["+plugin.getName()+"] "+fullCoords+" removed from BlocksData.yml !");
		}
		
		return true ;
	}
}
