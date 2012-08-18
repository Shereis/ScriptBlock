package scriptblock.command;

import java.util.LinkedList;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import scriptblock.BlockCoords;
import scriptblock.ScriptBlock;
import scriptblock.command.CommandHandler.CommandType;
import scriptblock.managers.FileManager;
import scriptblock.managers.MapManager;
import scriptblock.managers.PermManager;
import scriptblock.managers.ScriptManager;

public abstract class BindScript
{
	protected ScriptBlock scriptBlock = ScriptBlock.getInstance() ;
	protected Logger log = ScriptBlock.log ;
	protected JavaPlugin plugin ;
	
	protected ScriptManager scriptManager;
	protected CommandType commandType;

	
	protected FileManager fileManager;
	protected MapManager mapManager ;
	protected PermManager permManager ;
	
	protected Player commandSender ;

	protected String statusCancelled  ;
	protected String[] noAccessPermMsg  ;

	
	public BindScript(ScriptManager scriptManager, Player commandSender)
		{ this(scriptManager, commandSender, null) ; }
	
	public BindScript(ScriptManager scriptManager, Player commandSender, CommandType commandType)
	{
		this.plugin = scriptManager.getPlugin() ;
		this.scriptManager = scriptManager ;
		this.commandType = commandType ;
		this.mapManager = scriptManager.getMapManager() ;
		this.fileManager = scriptManager.getFileManager() ;
		this.permManager = scriptManager.getPermManager() ;
		this.commandSender = commandSender ;
		
		this.statusCancelled = new String( ChatColor.RED + "["+plugin.getName()+"] "+commandType.name()+" status cancelled !" ) ;
		this.noAccessPermMsg = 
				new String[] { permManager.noPermMsg ,
					( ChatColor.RED + "["+plugin.getName()+"] You can't \""+commandType.name()+"\" scripts you don't own!" ) } ;		 
	}

	public abstract boolean onEvent(BlockCoords blockcoords) ;
		
	
	protected boolean canAccessScript(BlockCoords blockCoords)
	{
		LinkedList <String> commandList = mapManager.blocksMap.get( blockCoords.getFullCoords() ) ;
		
		String perm = new String( "modify" + PermManager.sep + commandType.name() ) ;
		
		if ( commandList == null ) return true ;
		
		String firstLine = commandList.getFirst() ;
		
		if ( firstLine.startsWith(CommandCreate.authorNode) )
		{
			
			firstLine = firstLine.replaceFirst(CommandCreate.authorNode, "") ;
			String[] scriptInfos = firstLine.split("/");
			String authorName = scriptInfos[0] ;
			String authorGroup = scriptInfos[1] ;						
			
			if ( !commandSender.getName().equals(authorName) && !permManager.hasSBPerm(commandSender, perm + PermManager.sep + authorGroup , false  ) )
			{
				commandSender.sendMessage(noAccessPermMsg) ;
				return false ;
			}		
			else
				return true ;
		}

		
		if (permManager.hasSBPerm(commandSender, perm, false) )return true ;
		
		else {			
			commandSender.sendMessage(noAccessPermMsg) ;		
			return false ;	
		
		}		
	}

	public CommandType getCommandType() 
		{ return commandType; }	
}
