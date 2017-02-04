package scriptblock.managers;

import net.milkbowl.vault.permission.Permission;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class PermManager {
   public static final String sep = ".";
   public final String noPermMsg;
   private Permission perm;
   private final String scriptManagerName;
   private final String pluginName;
   private final String smanagerNode;

   public PermManager(Permission perm, String scriptManagerName, JavaPlugin plugin) {
      this.perm = perm;
      this.scriptManagerName = scriptManagerName;
      this.pluginName = plugin.getName().toLowerCase();
      this.smanagerNode = this.pluginName + "." + scriptManagerName;
      this.noPermMsg = new String(ChatColor.RED + "[" + plugin.getName() + "]" + " You don\'t have permission !");
   }

   public boolean playerInGroup(Player player, String group) {
      return this.perm.playerInGroup(player, group);
   }

   public boolean playerAddGroup(Player player, String group) {
      return this.perm.playerAddGroup(player, group);
   }

   public boolean playerRemoveGroup(Player player, String group) {
      return this.perm.playerRemoveGroup(player, group);
   }

   public boolean playerAdd(Player player, String permission) {
      return this.perm.playerAdd(player, permission);
   }

   public boolean playerAdd(String world, Player player, String permission) {
      return this.perm.playerAdd(world, player.getName(), permission);
   }

   public boolean playerRemove(Player player, String permission) {
      return this.perm.playerRemove(player, permission);
   }

   public boolean playerRemove(String world, Player player, String permission) {
      return this.perm.playerRemove(world, player.getName(), permission);
   }

   public boolean has(Player player, String permission) {
      return this.perm.has(player, permission);
   }

   private boolean hasSBmainPerm(Player player) {
      return this.perm.has(player, this.pluginName + "." + "*");
   }

   private boolean hasScriptManagerPerm(Player player) {
      return this.perm.has(player, this.smanagerNode + "." + "*") || this.hasSBmainPerm(player) || player.isOp();
   }

   public boolean hasSBPerm(Player player, String node, String type, boolean showMsg) {
      return !this.perm.has(player, this.smanagerNode + "." + node + "." + type) && !this.perm.has(player, this.smanagerNode + "." + node + "." + "*") && !this.hasScriptManagerPerm(player)?(showMsg?this.noPerm(player):false):true;
   }

   public boolean hasSBPerm(Player player, String[] typesArray, boolean showMsg) {
      if(this.hasScriptManagerPerm(player)) {
         return true;
      } else {
         StringBuilder permBuilder = (new StringBuilder()).append(this.smanagerNode);

         for(int i = 0; i < typesArray.length; ++i) {
            String type = typesArray[i];
            permBuilder.append("." + type);
            if(i == typesArray.length - 1 && this.perm.has(player, permBuilder.toString())) {
               return true;
            }

            if(this.perm.has(player, permBuilder.toString() + "." + "*")) {
               return true;
            }
         }

         if(showMsg) {
            return this.noPerm(player);
         } else {
            return false;
         }
      }
   }

   public boolean hasSBPerm(Player player, String permNode, boolean showMsg) {
      if(this.perm.has(player, this.smanagerNode + "." + permNode)) {
         return true;
      } else {
         while(permNode.contains(".")) {
            String lastnode = permNode.substring(permNode.lastIndexOf("."), permNode.length());
            permNode = permNode.replaceAll(lastnode, "");
            if(this.perm.has(player, this.smanagerNode + "." + permNode + "*")) {
               return true;
            }
         }

         return this.hasScriptManagerPerm(player)?true:(showMsg?this.noPerm(player):false);
      }
   }

   private boolean noPerm(Player player) {
      player.sendMessage(this.noPermMsg);
      return false;
   }

   public String getSmanagerNode() {
      return this.smanagerNode;
   }
}
