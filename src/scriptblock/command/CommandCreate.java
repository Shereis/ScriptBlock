package scriptblock.command;

import java.util.LinkedList;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import scriptblock.BlockCoords;
import scriptblock.command.CommandHandler.CommandType;
import scriptblock.managers.ScriptManager;

public class CommandCreate extends CreateManager {			
	
	public static String authorNode = "Author:" ;
	
	private String[] body ;
	
	public CommandCreate(ScriptManager scriptManager, String[] cmdBody, Player commandSender, CommandType commandType )
	{
		super(scriptManager, commandSender, commandType ) ;		
		this.body = cmdBody ;
	}
	
	public boolean isValid()
	{
		if (body.length > 0) {
			
			String playerName = commandSender.getName() ;
		
			for ( ScriptManager scriptManager : scriptBlock.getScriptManagerList() )
			{		
				if ( !scriptManager.getMapManager().commandsWaitingMap.containsKey(playerName) ) 
					{ continue ; }
				
				else {
					commandSender.sendMessage(ChatColor.RED + "["+plugin.getName()+"] You must validate your previous action !");
					return false ; 
				}					
			} 
			
			String text = ArrayToString(body);
			commandList = new LinkedList<String>();
			commandList = CommandScript(text, commandList);
			
			if ( !opCheck(commandList) ) return false ;			
			else if ( optionCheck(commandList) )
			{ 
				commandList.add( 0 , authorNode+playerName+"/"+scriptBlock.getPerm().getPrimaryGroup(commandSender) ) ;
				commandSender.sendMessage( ChatColor.GREEN + "["+plugin.getName()+"] Click on a block to bind the text to it...") ; 
				return true ;
			}
			else return false ;
		}
		else { commandSender.sendMessage( ChatColor.RED + "["+plugin.getName()+"] You must add arguments !") ;  }
		
		return false ;
	}
	
	@Override
	public boolean onEvent(BlockCoords blockcoords) 
		{ return create(blockcoords); }
}
