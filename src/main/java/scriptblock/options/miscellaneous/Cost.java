package scriptblock.options.miscellaneous;

import org.bukkit.ChatColor;
import scriptblock.managers.ScriptManager;
import scriptblock.options.SBOption;

public class Cost extends SBOption {
   public Cost(ScriptManager scriptManager) {
      super(scriptManager, "cost", "$cost:");
   }

   public boolean isValid() {
      if(this.eco != null) {
         this.scriptLine = this.scriptLine.replace(this.getSyntax(), "");
         double cost = Double.parseDouble(this.scriptLine);
         String playerName = this.player.getName();
         if(this.eco.getBalance(playerName) >= cost) {
            this.eco.withdrawPlayer(playerName, cost);
            this.player.sendMessage(ChatColor.RED + "[" + this.plugin.getName() + "] Your Account has been debited of " + ChatColor.WHITE + this.eco.format(cost) + ChatColor.RED + " !");
            return true;
         } else {
            this.player.sendMessage(ChatColor.RED + "[" + this.plugin.getName() + "] You don\'t have enough money to use this !");
            return false;
         }
      } else {
         this.log.info("Error while performing the Script on : " + this.blockCoords.getFullCoords());
         this.log.info("Reason : No Economy plugin detected !]");
         this.player.sendMessage(ChatColor.RED + "[" + this.plugin.getName() + "] Error ! No Economy Plugin Detected !");
         this.player.sendMessage(ChatColor.RED + "[" + this.plugin.getName() + "] Please, Contact your Administrator.");
         return false;
      }
   }
}
