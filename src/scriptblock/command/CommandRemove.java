package scriptblock.command;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import scriptblock.BlockCoords;
import scriptblock.SLAPI;
import scriptblock.command.CommandHandler.CommandType;
import scriptblock.managers.ScriptManager;

@SuppressWarnings("deprecation")
public class CommandRemove extends BindScript{
		
	public CommandRemove(ScriptManager scriptManager, Player commandsender, CommandType commandType) 
		{ super(scriptManager, commandsender, commandType) ; }

	public boolean isValid()
	{
		commandSender.sendMessage(ChatColor.GREEN + "["+plugin.getName()+"] Click on a block to remove the script...");
		return true;
	}

	@Override
	public boolean onEvent(BlockCoords blockCoords) {
		
		if ( mapManager.blocksMap.containsKey( blockCoords.getFullCoords() )) 
		{	
			if ( !canAccessScript(blockCoords) )
				{ commandSender.sendMessage(statusCancelled) ; return true ; }
		
			mapManager.blocksMap.remove( blockCoords.getFullCoords() );
			
		//Saving blocksMap					
			fileManager.getScriptConfig().removeProperty(blockCoords.world+"."+blockCoords.getCoords()+".");
			fileManager.getScriptConfig().save();
			
			commandSender.sendMessage(ChatColor.GREEN + "["+plugin.getName()+"] Text Successfully removed !");
		}
		else {
			commandSender.sendMessage(ChatColor.RED + "["+plugin.getName()+"] There is no Script bound to this !");
			commandSender.sendMessage(statusCancelled);
			return true ;
		}
		
	//Removing CoolDown and delay...	
		if (mapManager.cooldownMap.containsKey( blockCoords.getFullCoords() )){
			mapManager.cooldownMap.remove( blockCoords.getFullCoords() );
			
			try {
				SLAPI.save( mapManager.cooldownMap, fileManager.getCooldownDataFile().getPath() );
				log.info("cooldown removed from CoolDownData.dat!");
			} 
			catch (Exception e){
				log.info("[ERROR] while saving cooldownBlockMap to CoolDownData.dat ![ERROR]");
				log.info("at "+e.getMessage() );
			}					
		}
		
		if ( mapManager.delayList.contains( blockCoords.getFullCoords() )) mapManager.delayList.remove( blockCoords.getFullCoords() );
		
		return true ;
	}

}
