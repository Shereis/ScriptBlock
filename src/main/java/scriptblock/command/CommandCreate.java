package scriptblock.command;

import java.util.Iterator;
import java.util.LinkedList;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import scriptblock.BlockCoords;
import scriptblock.command.CommandHandler;
import scriptblock.command.CreateManager;
import scriptblock.managers.ScriptManager;

public class CommandCreate extends CreateManager {
   public static String authorNode = "Author:";
   private String[] body;

   public CommandCreate(ScriptManager scriptManager, String[] cmdBody, Player commandSender, CommandHandler.CommandType commandType) {
      super(scriptManager, commandSender, commandType);
      this.body = cmdBody;
   }

   public boolean isValid() {
      if(this.body.length <= 0) {
         this.commandSender.sendMessage(ChatColor.RED + "[" + this.plugin.getName() + "] You must add arguments !");
         return false;
      } else {
         String playerName = this.commandSender.getName();
         Iterator var3 = this.scriptBlock.getScriptManagerList().iterator();

         while(var3.hasNext()) {
            ScriptManager text = (ScriptManager)var3.next();
            if(text.getMapManager().commandsWaitingMap.containsKey(playerName)) {
               this.commandSender.sendMessage(ChatColor.RED + "[" + this.plugin.getName() + "] You must validate your previous action !");
               return false;
            }
         }

         String text1 = this.ArrayToString(this.body);
         this.commandList = new LinkedList();
         this.commandList = this.CommandScript(text1, this.commandList);
         if(!this.opCheck(this.commandList)) {
            return false;
         } else if(this.optionCheck(this.commandList)) {
            this.commandList.add(0, authorNode + playerName + "/" + this.scriptBlock.getPerm().getPrimaryGroup(this.commandSender));
            this.commandSender.sendMessage(ChatColor.GREEN + "[" + this.plugin.getName() + "] Click on a block to bind the text to it...");
            return true;
         } else {
            return false;
         }
      }
   }

   public boolean onEvent(BlockCoords blockcoords) {
      return this.create(blockcoords);
   }
}
