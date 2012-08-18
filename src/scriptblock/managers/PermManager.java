package scriptblock.managers;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import scriptblock.ScriptBlock;

import net.milkbowl.vault.permission.Permission;

public class PermManager {

	public final static String sep = "." ;
	public final String noPermMsg  ;
		
	private Permission perm;

	private final String scriptManagerName;
	private final String pluginName ;
	private final String smanagerNode ;
	


	public PermManager(Permission perm, String scriptManagerName , JavaPlugin plugin)
	{
		this.perm = perm ;
		this.scriptManagerName = scriptManagerName ;
		this.pluginName = plugin.getName().toLowerCase();
		this.smanagerNode = pluginName+sep+scriptManagerName ;
		this.noPermMsg = new String(ChatColor.RED +"["+plugin.getName()+"]"+" You don't have permission !") ;
	}
/*	
	public boolean hasSBPerm(Player player, String node, boolean message)
	{ 
		if ( perm.has(player, name+sep+node) || perm.has(player, name+sep+"*") || player.isOp() ) return true ; 
		else if (message) return noPerm(player) ;
		else return false ;
	}
*/		
	public boolean playerInGroup(Player player, String group)
		{ return perm.playerInGroup(player, group) ; }
	
	public boolean playerAddGroup(Player player, String group)
		{ return perm.playerAddGroup(player, group) ; }
	
	public boolean playerRemoveGroup(Player player , String group)
		{ return perm.playerRemoveGroup(player, group) ; }
	
	public boolean playerAdd(Player player, String permission) 
		{ return perm.playerAdd(player, permission) ; }
	
	public boolean playerAdd(String world, Player player, String permission)
		{ return perm.playerAdd(world, player.getName() , permission) ; }
	
	public boolean playerRemove(Player player, String permission)
		{ return perm.playerRemove(player, permission) ; }
	
	public boolean playerRemove(String world, Player player, String permission) 
		{ return perm.playerRemove(world, player.getName(), permission) ; }

	public boolean has(Player player, String permission)
		{ return perm.has(player, permission) ; }
	
	private boolean hasSBmainPerm(Player player)
		{ return perm.has(player, pluginName + sep + "*") ; }
	
	private boolean hasScriptManagerPerm(Player player)
	{
		if (perm.has(player, smanagerNode + sep + "*") || hasSBmainPerm(player) || player.isOp() ) return true ;
		else return false ; 
	}
	
	public boolean hasSBPerm(Player player, String node, String type, boolean showMsg )
	{ 			
		if ( perm.has(player, smanagerNode + sep + node + sep + type ) 
				|| perm.has( player, smanagerNode + sep + node + sep + "*" )
				|| hasScriptManagerPerm(player) ) 
		{ return true ; }
		
		else if (showMsg)  { return noPerm(player) ; }
		else return false ;
	}
	
	public boolean hasSBPerm(Player player, String[] typesArray, boolean showMsg)
	{			
		if ( hasScriptManagerPerm(player) ) return true ;
		
		StringBuilder permBuilder = new StringBuilder().append(smanagerNode) ;
		
		for (int i = 0; i < typesArray.length ; i++ )
		{	
			String type = typesArray[i] ;
			permBuilder.append(sep+type) ;	
			
			if (i == (typesArray.length-1) && perm.has( player, permBuilder.toString() )  )
					{  return true ; }
			
			else if ( perm.has(player, permBuilder.toString()+sep+"*" ) ) return true ;			
	
		}
		
		if (showMsg)  { return noPerm(player) ; }
		else return false ;		
	}

	
	public boolean hasSBPerm(Player player, String permNode, boolean showMsg)
	{
		if ( perm.has( player, smanagerNode + sep + permNode ) ) return true ;
		
		String lastnode ;
		while ( permNode.contains(sep) )
		{
			lastnode = permNode.substring( permNode.lastIndexOf(sep), permNode.length() ) ;
			permNode = permNode.replaceAll(lastnode, "") ;
			
			if ( perm.has( player, smanagerNode + sep + permNode + "*") ) return true ;
		}
		
		if (  hasScriptManagerPerm(player) ) return true ;		
		else if (showMsg)  { return noPerm(player) ; }
		else return false ;
	}

/*	
	public boolean hasSBPerm(Player player, String[] typesArray, boolean showMsg)
	{	
		StringBuilder permBuilder = new StringBuilder().append(name) ;
		for (String type : typesArray)
			{ permBuilder.append(sep+type) ; }
		
		if (permHas(player,))
		
		if (showMsg)  { return noPerm(player) ; }
		else return false ;
		
	}
*/	

	private boolean noPerm(Player player)
	{ 
		player.sendMessage(noPermMsg) ; 
		return false ;
	}
	public String getSmanagerNode() {
		return smanagerNode;
	}

}
