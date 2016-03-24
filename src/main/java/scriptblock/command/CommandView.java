package scriptblock.command;

import java.util.Iterator;
import java.util.LinkedList;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import scriptblock.BlockCoords;
import scriptblock.command.BindScript;
import scriptblock.command.CommandHandler;
import scriptblock.managers.ScriptManager;

public class CommandView extends BindScript {
   public CommandView(ScriptManager scriptManager, Player commandsender, CommandHandler.CommandType commandType) {
      super(scriptManager, commandsender, commandType);
   }

   public boolean isValid() {
      this.commandSender.sendMessage(ChatColor.GREEN + "[" + this.plugin.getName() + "] Click on a block to view the Script...");
      return true;
   }

   public boolean onEvent(BlockCoords blockcoords) {
      if(!this.canAccessScript(blockcoords)) {
         this.commandSender.sendMessage(this.statusCancelled);
         return true;
      } else {
         LinkedList script = (LinkedList)this.mapManager.blocksMap.get(blockcoords.getFullCoords());
         if(script == null) {
            this.commandSender.sendMessage(ChatColor.RED + "[" + this.plugin.getName() + "] There is no Script bound to this block !");
            this.commandSender.sendMessage(ChatColor.RED + "[" + this.plugin.getName() + "] View Status Cancelled !");
            return true;
         } else {
            Iterator var4 = script.iterator();

            while(var4.hasNext()) {
               String command = (String)var4.next();
               this.commandSender.sendMessage("[" + this.plugin.getName() + "] " + command);
            }

            return true;
         }
      }
   }
}
