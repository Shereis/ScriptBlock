package scriptblock.options.miscellaneous;

import org.bukkit.ChatColor;

import scriptblock.managers.ScriptManager;
import scriptblock.options.SBOption;

public class Cost extends SBOption {
		
	public Cost(ScriptManager scriptManager)
		{ super(scriptManager, "cost", "$cost:") ; }
	
	public boolean isValid()
	{
		if ( eco != null ){
			
			scriptLine = scriptLine.replace(this.getSyntax(), "");
			
			double cost = Double.parseDouble(scriptLine);
			String playerName = player.getName() ;
			
			if ( eco.getBalance( playerName) >= cost ) {
				
				eco.withdrawPlayer( playerName, cost );
				player.sendMessage( ChatColor.RED+"["+plugin.getName()+"] Your Account has been debited of "
									+ ChatColor.WHITE+eco.format(cost)
									+ ChatColor.RED+" !" );				
				return true ;
			}
			else {
				
				player.sendMessage(ChatColor.RED+"["+plugin.getName()+"] You don't have enough money to use this !");
				return false ;
			}								
		}
		else {
			
			log.info("Error while performing the Script on : "+blockCoords.getFullCoords() );
			log.info("Reason : No Economy plugin detected !]");
			
			player.sendMessage(ChatColor.RED+"["+plugin.getName()+"] Error ! No Economy Plugin Detected !");
			player.sendMessage(ChatColor.RED+"["+plugin.getName()+"] Please, Contact your Administrator.");
			
			return false ;
		}								
	}
}
