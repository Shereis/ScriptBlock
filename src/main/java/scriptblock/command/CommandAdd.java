package scriptblock.command;

import java.util.LinkedList;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import scriptblock.BlockCoords;
import scriptblock.SLAPI;
import scriptblock.command.CommandCreate;
import scriptblock.command.CommandHandler;
import scriptblock.command.CreateManager;
import scriptblock.managers.ScriptManager;

public class CommandAdd extends CreateManager {
   private String[] commandBody;

   public CommandAdd(ScriptManager scriptManager, String[] commandBody, Player commandSender, CommandHandler.CommandType commandType) {
      super(scriptManager, commandSender, commandType);
      this.commandBody = commandBody;
   }

   public boolean isValid() {
      if(this.commandBody.length == 0) {
         this.commandSender.sendMessage(ChatColor.RED + "[" + this.plugin.getName() + "] You must add argument !");
         return false;
      } else {
         String playerName = this.commandSender.getName();
         boolean alreadyExist = false;
         Object obj = this.mapManager.commandsWaitingMap.get(playerName);
         if(obj != null) {
            alreadyExist = true;
            if(obj instanceof CommandCreate) {
               CommandCreate tempList = (CommandCreate)obj;
               this.commandList = tempList.commandList;
            } else if(obj instanceof CommandAdd) {
               CommandAdd tempList1 = (CommandAdd)obj;
               this.commandList = tempList1.commandList;
            } else {
               this.commandSender.sendMessage(ChatColor.RED + "[" + this.plugin.getName() + "] ERROR in CommandAdd !");
            }
         } else {
            this.commandList = new LinkedList();
         }

         LinkedList tempList2 = new LinkedList();
         String text = this.ArrayToString(this.commandBody);
         tempList2 = this.CommandScript(text, tempList2);
         if(!this.opCheck(tempList2)) {
            return false;
         } else if(this.optionCheck(tempList2)) {
            this.commandList.addAll(tempList2);
            if(alreadyExist) {
               this.commandSender.sendMessage(ChatColor.GREEN + "[" + this.plugin.getName() + "] Command Successfully added to the Script !");
               this.commandSender.sendMessage(ChatColor.GREEN + "[" + this.plugin.getName() + "] Click on a block to bind the Script to it...");
               return false;
            } else {
               this.commandSender.sendMessage(ChatColor.GREEN + "[" + this.plugin.getName() + "] Use /sbadd command to add another script or...");
               this.commandSender.sendMessage(ChatColor.GREEN + "[" + this.plugin.getName() + "] Click on a block to add the Script to it !");
               return true;
            }
         } else {
            return false;
         }
      }
   }

   public boolean onEvent(BlockCoords blockcoords) {
      if(this.mapManager.blocksMap.containsKey(blockcoords.getFullCoords())) {
         this.add(blockcoords);
         return true;
      } else {
         this.commandSender.sendMessage(ChatColor.RED + "[" + this.plugin.getName() + "] There is no script on this block.");
         this.commandSender.sendMessage(this.statusCancelled);
         return true;
      }
   }

   public boolean add(BlockCoords blockcoords) {
      if(!this.canAccessScript(blockcoords)) {
         this.commandSender.sendMessage(this.statusCancelled);
         return true;
      } else {
         String world = blockcoords.world;
         String FullBlockCoords = blockcoords.getFullCoords();
         String blockCoords = blockcoords.getCoords();
         LinkedList finalList = (LinkedList)this.mapManager.blocksMap.get(FullBlockCoords);
         finalList.addAll(this.commandList);
         this.mapManager.blocksMap.put(FullBlockCoords, finalList);
         this.fileManager.getScriptConfig().setProperty(world + "." + blockCoords + ".", finalList);
         this.fileManager.getScriptConfig().save();
         if(this.mapManager.cooldownMap.containsKey(FullBlockCoords)) {
            this.mapManager.cooldownMap.remove(FullBlockCoords);

            try {
               SLAPI.save(this.mapManager.cooldownMap, this.fileManager.getCooldownDataFile().getPath());
               this.log.info("cooldown Saved to " + this.fileManager.getCooldownDataFile().getName() + " !");
            } catch (Exception var7) {
               this.log.info("[ERROR] while saving cooldownBlockMap to " + this.fileManager.getCooldownDataFile().getName() + " ![ERROR]");
               this.log.info("at " + var7.getMessage());
            }
         }

         this.commandSender.sendMessage(ChatColor.GREEN + "[" + this.plugin.getName() + "] Text Successfully bound !");
         return true;
      }
   }
}
