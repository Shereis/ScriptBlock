package scriptblock.options;

import java.util.LinkedList;

import scriptblock.managers.ScriptManager;
import scriptblock.options.miscellaneous.Amount;
import scriptblock.options.miscellaneous.Cost;
import scriptblock.options.miscellaneous.ItemCost;
import scriptblock.options.permissions.BypassGroup;
import scriptblock.options.permissions.BypassOp;
import scriptblock.options.permissions.BypassPerm;
import scriptblock.options.time.Cooldown;
import scriptblock.options.time.Delay;

import static scriptblock.options.permissions.PermOptions.*;
import static scriptblock.options.chat.ChatOptions.*;


public class OptionManager {

	private ScriptManager scriptManager;
	private LinkedList <Option> optionsList ;
	
	public Option cost ;
	public Option itemCost ;
	public Option amount ;
	public Option delay ;
	public Option cooldown ;
	public Option bypassOp ;
	public Option bypassGroup ;
	public Option bypassPerm ;
	
	public Option permission ;
	public Option group ;
	public Option command ;
	public Option say ;
	public Option toPlayer ;
	public Option groupAdd ;
	public Option groupRemove ;
	public Option permAdd ;
	public Option permRemove ;
	
	
	public OptionManager(ScriptManager scriptManager)
	{
		this.scriptManager = scriptManager ;
		this.optionsList = scriptManager.getMapManager().optionsList ;
	}
	
	public void registerDefaultOptions()
	{
		cost 		= new Cost(scriptManager) ;
		itemCost 	= new ItemCost(scriptManager);
		amount		= new Amount(scriptManager);
		delay		= new Delay(scriptManager);
		cooldown 	= new Cooldown(scriptManager);
		bypassOp 	= new BypassOp(scriptManager);
		bypassGroup = new BypassGroup(scriptManager);
		bypassPerm 	= new BypassPerm(scriptManager);
		
		permission	= new Permission(scriptManager);
		group 		= new Group(scriptManager) ;
		command 	= new Command(scriptManager) ;
		say 		= new Say (scriptManager) ;
		toPlayer 	= new ToPlayer(scriptManager) ;
		groupAdd 	= new GroupAdd(scriptManager) ;
		groupRemove = new GroupRemove(scriptManager) ;
		permAdd 	= new PermAdd(scriptManager) ;
		permRemove 	= new PermRemove(scriptManager) ;
				
		addOption(cost) ;
		addOption(itemCost) ;
		addOption(amount) ;
		addOption(delay) ;
		addOption(cooldown) ;
		addOption(bypassOp) ;
		addOption(bypassGroup) ;
		addOption(bypassPerm) ;
		
		addOption(permission) ;
		addOption(group) ;
		addOption(command) ;
		addOption(say) ;
		addOption(toPlayer) ;
		addOption(groupAdd) ;
		addOption(groupRemove) ;
		addOption(permAdd) ;
		addOption(permRemove) ;		
	}
	
	public void addOption(Option option)
		{ optionsList.add(option) ; }
	
	public void addOption(int index, Option option)
		{ optionsList.add(index, option) ; }
	
	public void removeOption(int index)
		{ optionsList.remove(index) ; }
	
	public LinkedList <Option> getOptionList()
		{ return this.optionsList ; }
	
	public void setOptionList(LinkedList <Option> optionList)
		{ this.optionsList = optionList ; }
		
}
