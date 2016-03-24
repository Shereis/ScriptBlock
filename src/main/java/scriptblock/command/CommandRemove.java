package scriptblock.command;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import scriptblock.BlockCoords;
import scriptblock.SLAPI;
import scriptblock.command.BindScript;
import scriptblock.command.CommandHandler;
import scriptblock.managers.ScriptManager;

public class CommandRemove extends BindScript {
   public CommandRemove(ScriptManager scriptManager, Player commandsender, CommandHandler.CommandType commandType) {
      super(scriptManager, commandsender, commandType);
   }

   public boolean isValid() {
      this.commandSender.sendMessage(ChatColor.GREEN + "[" + this.plugin.getName() + "] Click on a block to remove the script...");
      return true;
   }

   public boolean onEvent(BlockCoords blockCoords) {
      if(this.mapManager.blocksMap.containsKey(blockCoords.getFullCoords())) {
         if(!this.canAccessScript(blockCoords)) {
            this.commandSender.sendMessage(this.statusCancelled);
            return true;
         } else {
            this.mapManager.blocksMap.remove(blockCoords.getFullCoords());
            this.fileManager.getScriptConfig().removeProperty(blockCoords.world + "." + blockCoords.getCoords() + ".");
            this.fileManager.getScriptConfig().save();
            this.commandSender.sendMessage(ChatColor.GREEN + "[" + this.plugin.getName() + "] Text Successfully removed !");
            if(this.mapManager.cooldownMap.containsKey(blockCoords.getFullCoords())) {
               this.mapManager.cooldownMap.remove(blockCoords.getFullCoords());

               try {
                  SLAPI.save(this.mapManager.cooldownMap, this.fileManager.getCooldownDataFile().getPath());
                  this.log.info("cooldown removed from CoolDownData.dat!");
               } catch (Exception var3) {
                  this.log.info("[ERROR] while saving cooldownBlockMap to CoolDownData.dat ![ERROR]");
                  this.log.info("at " + var3.getMessage());
               }
            }

            if(this.mapManager.delayList.contains(blockCoords.getFullCoords())) {
               this.mapManager.delayList.remove(blockCoords.getFullCoords());
            }

            return true;
         }
      } else {
         this.commandSender.sendMessage(ChatColor.RED + "[" + this.plugin.getName() + "] There is no Script bound to this !");
         this.commandSender.sendMessage(this.statusCancelled);
         return true;
      }
   }
}
