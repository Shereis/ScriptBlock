package scriptblock.command;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import scriptblock.BlockCoords;
import scriptblock.SLAPI;
import scriptblock.command.BindScript;
import scriptblock.command.CommandHandler;
import scriptblock.managers.ScriptManager;
import scriptblock.options.Option;

public abstract class CreateManager extends BindScript {
   protected LinkedList commandList;

   public CreateManager(ScriptManager scriptManager, Player player, CommandHandler.CommandType commandType) {
      super(scriptManager, player, commandType);
      String consoleMsg = "[" + this.plugin.getName() + "] " + this.commandSender.getName() + " performed command \"" + commandType.name() + "\"...";
      this.log.info(consoleMsg);
   }

   protected abstract boolean isValid();

   protected boolean create(BlockCoords blockcoords) {
      String FullBlockCoords = blockcoords.getFullCoords();
      if(!this.canAccessScript(blockcoords)) {
         return false;
      } else {
         this.mapManager.blocksMap.put(FullBlockCoords, this.commandList);
         this.fileManager.getScriptConfig().setProperty(blockcoords.world + "." + blockcoords.getCoords() + ".", this.commandList);
         this.fileManager.getScriptConfig().save();
         if(this.mapManager.cooldownMap.containsKey(FullBlockCoords)) {
            this.mapManager.cooldownMap.remove(FullBlockCoords);

            try {
               SLAPI.save(this.mapManager.cooldownMap, this.fileManager.getCooldownDataFile().getPath());
               this.log.info("cooldown Saved to CoolDownData.dat!");
            } catch (Exception var4) {
               this.log.info("[ERROR] while saving cooldownBlockMap to CoolDownData.dat ![ERROR]");
               this.log.info("at " + var4.getMessage());
            }
         }

         this.mapManager.delayList.remove(FullBlockCoords);
         this.commandSender.sendMessage(ChatColor.GREEN + "[" + this.plugin.getName() + "] Text Successfully bound !");
         return true;
      }
   }

   public boolean optionCheck(LinkedList list) {
      boolean haveOption = false;

      for(int i = 0; i < list.size(); ++i) {
         String scriptLine = (String)list.get(i);
         Iterator var6 = this.mapManager.optionsList.iterator();

         while(var6.hasNext()) {
            Option option = (Option)var6.next();
            if(scriptLine.startsWith(option.getSyntax())) {
               haveOption = true;
               String optionNode = new String("option." + option.getName());
               String param = null;
               if(option instanceof Option.Permissible) {
                  param = scriptLine.replaceFirst(option.getSyntax(), "");
                  if(param.contains(" /")) {
                     param = param.substring(0, param.indexOf(" /"));
                  }
               }

               String permMessage = ChatColor.RED + "[" + this.plugin.getName() + "] You need permission to use: " + ChatColor.WHITE + option.getSyntax();
               String consoleMsg1 = "[" + this.plugin.getName() + "] Option \"" + option.getName() + "\" added";
               if(param != null) {
                  String perm = new String(optionNode + "." + param);
                  if(!this.permManager.hasSBPerm(this.commandSender, perm, false)) {
                     this.commandSender.sendMessage(permMessage + param + ChatColor.RED + " !!");
                     list = null;
                     return false;
                  }

                  if(option.showConsole()) {
                     this.log.info(consoleMsg1 + " with param \"" + param + "\" !");
                  }
               } else {
                  if(!this.permManager.hasSBPerm(this.commandSender, optionNode, false)) {
                     this.commandSender.sendMessage(permMessage + ChatColor.RED + " !!");
                     list = null;
                     return false;
                  }

                  if(option.showConsole()) {
                     this.log.info(consoleMsg1 + "...");
                  }
               }
               break;
            }
         }

         if(!haveOption) {
            this.commandSender.sendMessage(ChatColor.RED + "[" + this.plugin.getName() + "] BAD SCRIPT !");
            list = null;
            return false;
         }
      }

      return true;
   }

   public boolean opCheck(LinkedList list) {
      if(!this.commandSender.isOp() && this.ContainsOpCommands(list)) {
         this.commandSender.sendMessage(ChatColor.RED + "[" + this.plugin.getName() + "] Only Ops can Write Op\'s Commands !!!");
         list = null;
         return false;
      } else {
         return true;
      }
   }

   protected boolean ContainsOpCommands(LinkedList commandList) {
      String[] opCommands = new String[]{"op", "deop", "save-all", "save-off", "save-on", "stop", "ban-ip", "ban", "pardon-ip", "pardon", "gamemode", "kick", "whitelist", "?"};
      Iterator iter = commandList.iterator();

      while(true) {
         String n;
         Option bypassOp;
         do {
            if(!iter.hasNext()) {
               return false;
            }

            n = (String)iter.next();
            new String();
            bypassOp = this.scriptManager.getOptionManager().bypassOp;
         } while(!n.startsWith(bypassOp.getName()));

         for(int i = 0; i < opCommands.length; ++i) {
            String m = n.replaceFirst(bypassOp.getSyntax(), "");
            m = m.toLowerCase();
            if(m.startsWith(opCommands[i])) {
               return true;
            }
         }
      }
   }

   protected LinkedList CommandScript(String text, LinkedList commandList) {
      new String();
      if(text.contains("[") && text.contains("]")) {
         while(text.contains("[") && text.contains("]")) {
            int a = text.indexOf("[") + 1;
            int b = text.indexOf("]");
            String finalText = text.substring(a, b);
            text = text.replace(finalText, "");
            text = text.replace("[]", "");
            commandList.add(finalText);
         }
      } else {
         commandList.add(text);
      }

      return commandList;
   }

   protected String ArrayToString(String[] arg) {
      new String();
      StringBuilder stringBuilder = new StringBuilder();
      Iterator iter = Arrays.asList(arg).iterator();

      while(iter.hasNext()) {
         String s = (String)iter.next();
         if(s != null && !s.isEmpty()) {
            stringBuilder.append(" ").append(s);
         }
      }

      String text = stringBuilder.toString().replaceFirst(" ", "");
      return text;
   }
}
