package scriptblock.options.permissions;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import scriptblock.managers.ScriptManager;
import scriptblock.options.Option;
import scriptblock.options.SBOption;

public class PermOptions {
   public static class BypassConsole extends SBOption {
      public BypassConsole(ScriptManager scriptManager) {
         super(scriptManager, "console", "@CONSOLE /");
      }

      public boolean isValid() {
         this.scriptLine = this.scriptLine.replaceAll(this.getSyntax(), "");
         Server server = this.scriptBlock.getServer();
         server.dispatchCommand(server.getConsoleSender(), this.scriptLine);
         return true;
      }
   }

   public static class Group extends SBOption implements Option.Permissible {
      public Group(ScriptManager scriptManager) {
         super(scriptManager, "group", "@group:");
      }

      public boolean isValid() {
         this.scriptLine = this.scriptLine.replace(this.getSyntax(), "");
         if(!this.permManager.playerInGroup(this.player, this.scriptLine)) {
            this.player.sendMessage(ChatColor.RED + "[ScriptBlock] Only members of the " + this.scriptLine + " group can use this !!");
            return false;
         } else {
            return true;
         }
      }
   }

   public static class GroupAdd extends SBOption implements Option.Permissible {
      public GroupAdd(ScriptManager scriptManager) {
         super(scriptManager, "group_add", "@groupADD:");
      }

      public boolean isValid() {
         String group = this.scriptLine.replaceFirst(this.getSyntax(), "");
         if(!this.permManager.playerInGroup(this.player, group)) {
            this.permManager.playerAddGroup(this.player, group);
         }

         return true;
      }
   }

   public static class GroupRemove extends SBOption implements Option.Permissible {
      public GroupRemove(ScriptManager scriptManager) {
         super(scriptManager, "group_remove", "@groupREMOVE:");
      }

      public boolean isValid() {
         String group = this.scriptLine.replaceFirst(this.getSyntax(), "");
         this.permManager.playerRemoveGroup(this.player, group);
         return true;
      }
   }

   public static class PermAdd extends SBOption implements Option.Permissible {
      public PermAdd(ScriptManager scriptManager) {
         super(scriptManager, "perm_add", "@permADD:");
      }

      public boolean isValid() {
         this.scriptLine = this.scriptLine.replace(this.getSyntax(), "");
         if(!this.scriptBlock.getPerm().has(this.player, this.scriptLine)) {
            if(!this.scriptLine.contains("/")) {
               this.permManager.playerAdd(this.player, this.scriptLine);
            } else {
               String world = this.scriptLine.substring(0, this.scriptLine.indexOf("/"));
               this.scriptLine = this.scriptLine.replaceFirst(world + "/", "");
               this.permManager.playerAdd(world, this.player, this.scriptLine);
            }
         }

         return true;
      }
   }

   public static class PermRemove extends SBOption implements Option.Permissible {
      public PermRemove(ScriptManager scriptManager) {
         super(scriptManager, "perm_remove", "@permREMOVE:");
      }

      public boolean isValid() {
         this.scriptLine = this.scriptLine.replace(this.getSyntax(), "");
         if(!this.scriptBlock.getPerm().has(this.player, this.scriptLine)) {
            if(!this.scriptLine.contains("/")) {
               this.permManager.playerAdd(this.player, this.scriptLine);
            } else {
               String world = this.scriptLine.substring(0, this.scriptLine.indexOf("/"));
               this.scriptLine = this.scriptLine.replaceFirst(world + "/", "");
               this.permManager.playerAdd(world, this.player, this.scriptLine);
            }
         }

         return true;
      }
   }

   public static class Permission extends SBOption implements Option.Permissible {
      public Permission(ScriptManager scriptManager) {
         super(scriptManager, "permission", "@perm:");
      }

      public boolean isValid() {
         this.scriptLine = this.scriptLine.replace(this.getSyntax(), "");
         return this.permManager.has(this.player, this.scriptLine);
      }
   }
}
