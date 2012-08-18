package scriptblock.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import scriptblock.managers.FileManager;
import scriptblock.managers.MapManager;
import scriptblock.managers.PermManager;
import scriptblock.managers.ScriptManager;

public class CommandHandler implements CommandExecutor{
	
	
	public JavaPlugin plugin;
	public ScriptManager scriptManager;
	
//	public final String[] permArray ;
		
	public Player commandsender ;
	public String[] body ;

	protected MapManager mapManager;
	private PermManager perm ;
	private String pluginCommand;


	
	public CommandHandler(ScriptManager scriptManager, String pluginCommand)
	{
		this.scriptManager = scriptManager ;
		this.pluginCommand = pluginCommand ;
		this.mapManager = scriptManager.getMapManager() ;
		this.plugin = scriptManager.getPlugin() ;
		this.perm = scriptManager.getPermManager() ;
//		this.permArray = new String[] { scriptManager.getName() , "command" , null }	;
		
	//Registering commands to Bukkit.	
//		for ( CommandType command : CommandType.values() )
			{ plugin.getCommand( pluginCommand+scriptManager.getName() ).setExecutor(this) ; }
	}
			
	public enum CommandType
	{		
		create,
		add,
		remove ,
		view,
		reload ;			
	}
			
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] body) 
	{				
		if (sender instanceof Player) 
		{	
			this.body = body ;
			this.commandsender = (Player) sender ;
			
			String playerName = commandsender.getName() ;			
			
			for ( CommandType commandType : CommandType.values() )
			{
				if ( body.length > 0 && body[0].equalsIgnoreCase( commandType.name() ) )
				{						
//					permArray[2] = commandType.name() ;
					
					if ( perm.hasSBPerm(commandsender, "command" + PermManager.sep + commandType.name() , true) )
					{							
						body[0] = null ;						
						
						switch(commandType)
						{
							case create : 						
								
								CommandCreate commandCreate = new CommandCreate( scriptManager, body, commandsender, commandType  );									
								if ( commandCreate.isValid() ) 
									{ mapManager.commandsWaitingMap.put(playerName, commandCreate); }
								
								break ;	
							
							case add :  
								
								CommandAdd commandAdd = new CommandAdd ( scriptManager, body, commandsender, commandType );									
								if ( commandAdd.isValid() )
									{ mapManager.commandsWaitingMap.put(playerName, commandAdd); }
								
								break ;	
																	
							case remove : 
								
								CommandRemove commandRemove = new CommandRemove( scriptManager, commandsender, commandType ) ;
								if ( commandRemove.isValid() )
									{ mapManager.commandsWaitingMap.put(playerName, commandRemove); }
								
								break ;
								
							case view :
								
								CommandView commandView = new CommandView(scriptManager, commandsender, commandType) ;							
								if ( commandView.isValid() )
									{ mapManager.commandsWaitingMap.put(playerName, commandView); }
								
								break ;
								
							case reload : 
								
								FileManager fileManager = scriptManager.getFileManager() ;
								fileManager.loadScriptFile() ;
								fileManager.loadConfigFile() ;
								commandsender.sendMessage(ChatColor.GREEN+ "["+plugin.getName()+"] "+fileManager.getScriptDataFile().getName()+" is now reloaded !");
								commandsender.sendMessage(ChatColor.GREEN+ "["+plugin.getName()+"] "+fileManager.getConfigFile().getName()+" is now reloaded !");
								
								break ;
	
						}												
					}					
					
					return true ;
					
				}
			}			
		}
		
		return false;
	}		
}
