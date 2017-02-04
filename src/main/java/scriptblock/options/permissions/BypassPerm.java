package scriptblock.options.permissions;

import scriptblock.managers.ScriptManager;
import scriptblock.options.Option;
import scriptblock.options.SBOption;

public class BypassPerm extends SBOption implements Option.Permissible {
   private boolean hadPerm;

   public BypassPerm(ScriptManager scriptManager) {
      super(scriptManager, "bypass_perm", "@bypassPERM:");
   }

   public boolean isValid() {
      this.scriptLine = this.scriptLine.replaceFirst(this.getSyntax(), "");
      String access = this.scriptLine.substring(0, this.scriptLine.indexOf(" /"));
      this.scriptLine = this.scriptLine.replaceFirst(access + " /", "");
      if(!this.vaultPerm.playerHas(this.player, access)) {
         this.vaultPerm.playerAdd(this.player, access);
      } else {
         this.hadPerm = true;
      }

      try {
         this.player.performCommand(this.scriptLine);
      } finally {
         if(!this.hadPerm) {
            this.vaultPerm.playerRemove(this.player, access);
         }

      }

      this.hadPerm = false;
      return true;
   }
}
