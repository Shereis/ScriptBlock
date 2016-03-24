package scriptblock.options.miscellaneous;

import java.util.ArrayList;
import java.util.Iterator;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import scriptblock.managers.ScriptManager;
import scriptblock.options.Option;
import scriptblock.options.SBOption;

public class ItemCost extends SBOption implements Option.Permissible {
   public ItemCost(ScriptManager scriptManager) {
      super(scriptManager, "itemcost", "$item:");
   }

   public boolean isValid() {
      this.scriptLine = this.scriptLine.replace(this.getSyntax(), "");
      String[] itemInfos = this.scriptLine.split(" ");
      int requiredAmount = Integer.parseInt(itemInfos[1]);
      short metaData = 0;
      int requiredItemId;
      if(itemInfos[0].contains(":")) {
         String[] inventory = itemInfos[0].split(":");
         requiredItemId = Integer.parseInt(inventory[0]);
         metaData = Short.parseShort(inventory[1]);
      } else {
         requiredItemId = Integer.parseInt(itemInfos[0]);
      }

      ItemStack[] var11 = this.player.getInventory().getContents();
      boolean amount = false;
      ArrayList validStackIndexList = new ArrayList(var11.length);

      for(int i = 0; i < var11.length; ++i) {
         if(var11[i] != null && var11[i].getTypeId() == requiredItemId && var11[i].getDurability() == metaData) {
            int var12 = var11[i].getAmount();
            requiredAmount -= var12;
            if(requiredAmount <= 0) {
               int index;
               for(Iterator var10 = validStackIndexList.iterator(); var10.hasNext(); var11[index] = null) {
                  index = ((Integer)var10.next()).intValue();
               }

               if(requiredAmount == 0) {
                  var11[i] = null;
               } else {
                  var11[i] = new ItemStack(requiredItemId, requiredAmount * -1, metaData);
               }

               this.player.getInventory().setContents(var11);
               return true;
            }

            validStackIndexList.add(Integer.valueOf(i));
         }
      }

      this.player.sendMessage(ChatColor.RED + "[" + this.plugin.getName() + "] You don\'t have the required item(s) to use this !");
      return false;
   }
}
