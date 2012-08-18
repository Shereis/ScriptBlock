package scriptblock.options.permissions;

import org.bukkit.ChatColor;
import org.bukkit.Server;

import scriptblock.managers.ScriptManager;
import scriptblock.options.SBOption;
import scriptblock.options.Option.Permissible ;

public class PermOptions {

	
	public static class Permission extends SBOption implements Permissible {

		public Permission(ScriptManager scriptManager) 
			{ super(scriptManager, "permission", "@perm:"); }

		@Override
		public boolean isValid() 
		{		
			scriptLine = scriptLine.replace( getSyntax(), "") ;
//			player.sendMessage(scriptLine) ;										//DEBUG//
			if ( !permManager.has(player, scriptLine) ) { return false ; }
					
			return true;
		}
	}


	public static class Group extends SBOption implements Permissible {

		public Group(ScriptManager scriptManager) 
			{ super(scriptManager, "group", "@group:"); }

		@Override
		public boolean isValid() 
		{	
			scriptLine = scriptLine.replace( getSyntax(), "") ;		
			if ( !permManager.playerInGroup(player, scriptLine) ) 
			{								
				player.sendMessage(ChatColor.RED+"[ScriptBlock] Only members of the "+scriptLine+" group can use this !!") ;
				return false ;
			}			
			return true ;
		}	
	}


	public static class GroupAdd extends SBOption implements Permissible {

		public GroupAdd(ScriptManager scriptManager)
			{ super(scriptManager, "group_add", "@groupADD:") ; }

		@Override
		public boolean isValid() 
		{
			String group = scriptLine.replaceFirst( getSyntax(), "");
			if ( !permManager.playerInGroup(player, group) ) { permManager.playerAddGroup(player, group); }
			
			return true ;
		}
	}


	public static class GroupRemove extends SBOption  implements Permissible {
		
		public GroupRemove(ScriptManager scriptManager)
			{ super(scriptManager, "group_remove", "@groupREMOVE:") ; }
		
		@Override
		public boolean isValid()
		{
			String group = scriptLine.replaceFirst( getSyntax(), "");
			permManager.playerRemoveGroup(player, group);
			return true ;
		}
	}


	public static class PermAdd extends SBOption implements Permissible {
		
		public PermAdd(ScriptManager scriptManager)
			{ super(scriptManager, "perm_add", "@permADD:") ; }
		
		@Override
		public boolean isValid()
		{
			scriptLine = scriptLine.replace( getSyntax(), "") ;
			if ( !scriptBlock.getPerm().has(player, scriptLine) ) {
				if ( !scriptLine.contains("/") )
					{ permManager.playerAdd(player, scriptLine); }
				
				else{
					String world = scriptLine.substring(0, scriptLine.indexOf("/")) ;
					scriptLine = scriptLine.replaceFirst(world+"/", "") ;
					permManager.playerAdd(world, player, scriptLine) ;
				}
			}	
			return true ;
		}	
	}


	public static class PermRemove extends SBOption implements Permissible {
		
		public PermRemove(ScriptManager scriptManager)
		{ super(scriptManager, "perm_remove" , "@permREMOVE:") ; }
		
		public boolean isValid()
		{
			scriptLine = scriptLine.replace( getSyntax(), "") ;
			if ( !scriptBlock.getPerm().has(player, scriptLine) ) {
				if ( !scriptLine.contains("/") )
					{ permManager.playerAdd(player, scriptLine); }
				
				else{
					String world = scriptLine.substring(0, scriptLine.indexOf("/")) ;
					scriptLine = scriptLine.replaceFirst(world+"/", "") ;
					permManager.playerAdd(world, player, scriptLine) ;
				}
			}
			return true ;
		}
	}

	
	public static class Console extends SBOption {
		
		public Console(ScriptManager scriptManager)
		{ super(scriptManager, "console", "@console /") ; }
		
		public boolean isValid()
		{
			scriptLine = scriptLine.replaceAll(getSyntax(), "") ;
			
			Server server = scriptBlock.getServer() ;			
			server.dispatchCommand(server.getConsoleSender(), scriptLine) ;
			
			return true ;			
		}
	}

}



