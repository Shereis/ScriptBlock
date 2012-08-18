package scriptblock.options.permissions;

import scriptblock.managers.ScriptManager;
import scriptblock.options.SBOption;
import scriptblock.options.Option.Permissible;


public class BypassPerm extends SBOption implements Permissible {

	private boolean hadperm ;

	public BypassPerm(ScriptManager scriptManager) 
		{ super(scriptManager, "bypass_perm", "@bypassPERM:"); }

	@Override
	public boolean isValid() 
	{	
		scriptLine = scriptLine.replaceFirst(this.getSyntax(), "");
		String access = scriptLine.substring(0, scriptLine.indexOf(" /"));
		scriptLine = scriptLine.replaceFirst(access+" /", "");
				
		if ( !vaultPerm.playerHas(player, access) )
			{ vaultPerm.playerAdd(player, access); }		
		else hadperm = true ;
		
		try { player.performCommand(scriptLine); }		
		finally  { if (!hadperm) vaultPerm.playerRemove(player, access); }
		
		return true ;
	}

}
