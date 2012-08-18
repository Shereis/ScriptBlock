package scriptblock.command;

import java.util.LinkedList;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import scriptblock.SLAPI;

import scriptblock.BlockCoords;
import scriptblock.command.CommandHandler.CommandType;
import scriptblock.managers.ScriptManager;


@SuppressWarnings("deprecation")
public class CommandAdd extends CreateManager {
	
	private String[] commandBody;
	
	public CommandAdd(ScriptManager scriptManager, String[] commandBody, Player commandSender, CommandType commandType)
	{
		super(scriptManager, commandSender, commandType) ;
		this.commandBody = commandBody ;
	}
	
	public boolean isValid()
	{		
		if (commandBody.length == 0)
		{ 
			commandSender.sendMessage(ChatColor.RED + "["+plugin.getName()+"] You must add argument !") ;
			return false ; 
		}
		
		String playerName = commandSender.getName() ;	
		boolean alreadyExist = false ;
		Object obj = mapManager.commandsWaitingMap.get(playerName) ;
		
		if ( obj != null )
		{
			alreadyExist = true ;
			
			//TODO remove unecessary code VVVVVV
			if (obj instanceof CommandCreate)
			{
				CommandCreate commandCreate = (CommandCreate) obj ;				
				commandList = commandCreate.commandList ;
			}
			else if (obj instanceof CommandAdd)
			{
				CommandAdd commandAdd = (CommandAdd) obj ;
				commandList = commandAdd.commandList ;
			}
			else { commandSender.sendMessage(ChatColor.RED + "["+plugin.getName()+"] ERROR in CommandAdd !") ; }
			
		}						
		else { commandList = new LinkedList<String>();  }
			
		LinkedList<String> tempList = new LinkedList<String>() ;
		String text = ArrayToString(commandBody);
		tempList = CommandScript(text, tempList);
				
		
		if ( !opCheck(tempList) ) 
			{  return false ;  }
		
		else if ( optionCheck(tempList) )
			{ commandList.addAll( tempList ) ; }
		
		else return false ;
		
		if (alreadyExist)
		{ 
			commandSender.sendMessage(ChatColor.GREEN + "["+plugin.getName()+"] Command Successfully added to the Script !");
			commandSender.sendMessage(ChatColor.GREEN + "["+plugin.getName()+"] Click on a block to bind the Script to it...");
			return false ;
		}
		else
		{
			commandSender.sendMessage(ChatColor.GREEN + "["+plugin.getName()+"] Use /sbadd command to add another script or...");
			commandSender.sendMessage(ChatColor.GREEN + "["+plugin.getName()+"] Click on a block to add the Script to it !");
			return true ;
		}
	}


	@Override
	public boolean onEvent(BlockCoords blockcoords) 
	{				
		if ( mapManager.blocksMap.containsKey( blockcoords.getFullCoords() )) {
			add( blockcoords ) ;
			return true ;
		}	
		else {
			
			commandSender.sendMessage(ChatColor.RED + "["+plugin.getName()+"] There is no script on this block.");
			commandSender.sendMessage(statusCancelled) ;
			return true; 			
		}
	}
	
	public boolean add( BlockCoords blockcoords )
	{		
		if ( !canAccessScript(blockcoords) )
			{ commandSender.sendMessage(statusCancelled) ; return true ; }
		
		String world = blockcoords.world ;
		String FullBlockCoords = blockcoords.getFullCoords() ;
		String blockCoords = blockcoords.getCoords() ;
		
		LinkedList <String> finalList = mapManager.blocksMap.get( FullBlockCoords ) ;
		finalList.addAll(commandList) ;
		mapManager.blocksMap.put(FullBlockCoords, finalList) ;
		
	//Saving blocksMap...

		fileManager.getScriptConfig().setProperty(world+"."+blockCoords+".", finalList );
		fileManager.getScriptConfig().save();
		
	//Removing CoolDown and delay...
		
		if (mapManager.cooldownMap.containsKey( FullBlockCoords )) 
		{			
			mapManager.cooldownMap.remove(FullBlockCoords);
			try {
				SLAPI.save(mapManager.cooldownMap, fileManager.getCooldownDataFile().getPath() );
				log.info("cooldown Saved to "+fileManager.getCooldownDataFile().getName()+" !");
			} 
			catch (Exception e) {
				log.info("[ERROR] while saving cooldownBlockMap to "+fileManager.getCooldownDataFile().getName()+" ![ERROR]");
				log.info("at "+e.getMessage());
			}					
		}
		
		commandSender.sendMessage(ChatColor.GREEN + "["+plugin.getName()+"] Text Successfully bound !");
		return true ;
	}
	
}
