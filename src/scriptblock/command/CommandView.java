package scriptblock.command;

import java.util.LinkedList;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import scriptblock.BlockCoords;
import scriptblock.command.CommandHandler.CommandType;
import scriptblock.managers.ScriptManager;

public class CommandView extends BindScript {

	public CommandView(ScriptManager scriptManager, Player commandsender, CommandType commandType) 
		{ super(scriptManager, commandsender, commandType); }

	public boolean isValid()
	{
		commandSender.sendMessage(ChatColor.GREEN + "["+plugin.getName()+"] Click on a block to view the Script...");
		return true ;
	}
	
	@Override
	public boolean onEvent(BlockCoords blockcoords) 
	{
		if ( !canAccessScript(blockcoords) )
			{ commandSender.sendMessage(statusCancelled) ; return true ; }
		
		LinkedList<String> script = mapManager.blocksMap.get( blockcoords.getFullCoords() );
		if ( script != null ) 
		{						
			for (String command : script){
				commandSender.sendMessage("["+plugin.getName()+"] "+command);
			}
			return true ;		
		}
		else {
			commandSender.sendMessage(ChatColor.RED + "["+plugin.getName()+"] There is no Script bound to this block !");
			commandSender.sendMessage(ChatColor.RED + "["+plugin.getName()+"] View Status Cancelled !");
			return true ;
		}
	}
}
