package scriptblock.options.permissions;

import scriptblock.managers.ScriptManager;
import scriptblock.options.SBOption;


public class BypassOp extends SBOption {
	
	public BypassOp(ScriptManager scriptManager) 
		{ super(scriptManager, "bypass_op", "@bypass /"); }

	@Override
	public boolean isValid() 
	{
		scriptLine = scriptLine.replaceFirst(this.getSyntax(), "");		
		
		if (player.isOp() == false)
		{	
			try {
				player.setOp(true);	
				player.performCommand(scriptLine);									
			}
			finally { player.setOp(false); }
		}		
		else player.performCommand(scriptLine);
		
		
		return true ;
	}

	

}
