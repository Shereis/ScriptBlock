package scriptblock.options.miscellaneous;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import scriptblock.managers.ScriptManager;
import scriptblock.options.SBOption;
import scriptblock.options.Option.Permissible;

public class ItemCost extends SBOption implements Permissible {
	
	public ItemCost(ScriptManager scriptManager) 
		{ super(scriptManager, "itemcost", "$item:"); }

	@Override
	public boolean isValid() {
        scriptLine = scriptLine.replace(this.getSyntax(), "");

        //String[] itemInfos = scriptLine.split(":") ;		
        String[] itemInfos = scriptLine.split(" ");
        int requiredAmount = Integer.parseInt( itemInfos[1] );
        int requiredItemId ;
        short metaData = 0 ;

        //int requiredItemId = Integer.parseInt(itemInfos[0]) ;
        if ( itemInfos[0].contains(":") ) 
        {
        	String[] itemData = itemInfos[0].split(":") ;
            requiredItemId = Integer.parseInt( itemData[0] );
            metaData = Short.parseShort( itemData[1] );
        } 
        else 
        	{ requiredItemId = Integer.parseInt(itemInfos[0]); }
        
        
        ItemStack[] inventory = player.getInventory().getContents();

        int amount = 0 ;
        List <Integer> validStackIndexList = new ArrayList <Integer> (inventory.length) ;
        
        for (int i = 0; i < inventory.length; i++) 
        {
            if (inventory[i] != null && inventory[i].getTypeId() == requiredItemId && inventory[i].getDurability() == metaData) 
            {
                amount = inventory[i].getAmount();

                requiredAmount -= amount;
                                   
                if (requiredAmount <= 0) 
                {
                    for (int index : validStackIndexList ) 
                    	{  inventory[index] = null; }
                    
                    if (requiredAmount == 0) 
                    	{ inventory[i] = null; }
                    else 
                    	{ inventory[i] = new ItemStack(requiredItemId, (requiredAmount * -1), metaData); }
                    
                    player.getInventory().setContents(inventory);
                    
                    return true;
                }
                else
                	{ validStackIndexList.add(i); }

            }

        }

        player.sendMessage(ChatColor.RED + "["+plugin.getName()+"] You don't have the required item(s) to use this !");

        return false;
    }
}


