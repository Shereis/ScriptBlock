package scriptblock.options.permissions;

import scriptblock.managers.ScriptManager;
import scriptblock.options.Option.Permissible;
import scriptblock.options.SBOption;


public class BypassGroup extends SBOption implements Permissible {

	public BypassGroup(ScriptManager scriptManager) 
		{ super(scriptManager, "bypass_group", "@bypassGROUP:"); }

	@Override
	public boolean isValid() {
		
		scriptLine = scriptLine.replaceFirst(this.getSyntax(), "");
		String access = scriptLine.substring(0, scriptLine.indexOf(" /"));
		scriptLine = scriptLine.replaceFirst(access+" /", "");
		
		if ( !vaultPerm.playerInGroup(player, access) )
		{
			try {
				vaultPerm.playerAddGroup(player, access);
				player.performCommand(scriptLine);
			} 
			finally { vaultPerm.playerRemoveGroup(player, access); }
			
		} else player.performCommand(scriptLine);
		
		return true ;
	}
}
