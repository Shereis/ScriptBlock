package scriptblock.options;

import java.util.LinkedList;
import scriptblock.managers.ScriptManager;
import scriptblock.options.Option;
import scriptblock.options.chat.ChatOptions;
import scriptblock.options.miscellaneous.Amount;
import scriptblock.options.miscellaneous.Cost;
import scriptblock.options.miscellaneous.ItemCost;
import scriptblock.options.permissions.BypassGroup;
import scriptblock.options.permissions.BypassOp;
import scriptblock.options.permissions.BypassPerm;
import scriptblock.options.permissions.PermOptions;
import scriptblock.options.time.Cooldown;
import scriptblock.options.time.Delay;

public class OptionManager {
   private ScriptManager scriptManager;
   private LinkedList optionsList;
   public Option cost;
   public Option itemCost;
   public Option amount;
   public Option delay;
   public Option cooldown;
   public Option bypassOp;
   public Option bypassGroup;
   public Option bypassCsl;
   public Option bypassPerm;
   public Option permission;
   public Option group;
   public Option command;
   public Option say;
   public Option toPlayer;
   public Option groupAdd;
   public Option groupRemove;
   public Option permAdd;
   public Option permRemove;

   public OptionManager(ScriptManager scriptManager) {
      this.scriptManager = scriptManager;
      this.optionsList = scriptManager.getMapManager().optionsList;
   }

   public void registerDefaultOptions() {
      this.cost = new Cost(this.scriptManager);
      this.itemCost = new ItemCost(this.scriptManager);
      this.amount = new Amount(this.scriptManager);
      this.delay = new Delay(this.scriptManager);
      this.cooldown = new Cooldown(this.scriptManager);
      this.bypassOp = new BypassOp(this.scriptManager);
      this.bypassGroup = new BypassGroup(this.scriptManager);
      this.bypassPerm = new BypassPerm(this.scriptManager);
      this.bypassCsl = new PermOptions.BypassConsole(this.scriptManager);
      this.permission = new PermOptions.Permission(this.scriptManager);
      this.group = new PermOptions.Group(this.scriptManager);
      this.command = new ChatOptions.Command(this.scriptManager);
      this.say = new ChatOptions.Say(this.scriptManager);
      this.toPlayer = new ChatOptions.ToPlayer(this.scriptManager);
      this.groupAdd = new PermOptions.GroupAdd(this.scriptManager);
      this.groupRemove = new PermOptions.GroupRemove(this.scriptManager);
      this.permAdd = new PermOptions.PermAdd(this.scriptManager);
      this.permRemove = new PermOptions.PermRemove(this.scriptManager);
      this.addOption(this.cost);
      this.addOption(this.itemCost);
      this.addOption(this.amount);
      this.addOption(this.delay);
      this.addOption(this.cooldown);
      this.addOption(this.bypassOp);
      this.addOption(this.bypassGroup);
      this.addOption(this.bypassPerm);
      this.addOption(this.permission);
      this.addOption(this.group);
      this.addOption(this.command);
      this.addOption(this.say);
      this.addOption(this.toPlayer);
      this.addOption(this.groupAdd);
      this.addOption(this.groupRemove);
      this.addOption(this.permAdd);
      this.addOption(this.permRemove);
   }

   public void addOption(Option option) {
      this.optionsList.add(option);
   }

   public void addOption(int index, Option option) {
      this.optionsList.add(index, option);
   }

   public void removeOption(int index) {
      this.optionsList.remove(index);
   }

   public LinkedList getOptionList() {
      return this.optionsList;
   }

   public void setOptionList(LinkedList optionList) {
      this.optionsList = optionList;
   }
}
