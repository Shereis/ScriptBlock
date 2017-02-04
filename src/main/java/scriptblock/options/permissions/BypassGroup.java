package scriptblock.options.permissions;

import scriptblock.managers.ScriptManager;
import scriptblock.options.Option;
import scriptblock.options.SBOption;

public class BypassGroup extends SBOption implements Option.Permissible {
   public BypassGroup(ScriptManager scriptManager) {
      super(scriptManager, "bypass_group", "@bypassGROUP:");
   }

   public boolean isValid() {
      this.scriptLine = this.scriptLine.replaceFirst(this.getSyntax(), "");
      String access = this.scriptLine.substring(0, this.scriptLine.indexOf(" /"));
      this.scriptLine = this.scriptLine.replaceFirst(access + " /", "");
      if(!this.vaultPerm.playerInGroup(this.player, access)) {
         try {
            this.vaultPerm.playerAddGroup(this.player, access);
            this.player.performCommand(this.scriptLine);
         } finally {
            this.vaultPerm.playerRemoveGroup(this.player, access);
         }
      } else {
         this.player.performCommand(this.scriptLine);
      }

      return true;
   }
}
