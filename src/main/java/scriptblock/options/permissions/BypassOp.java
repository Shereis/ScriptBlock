package scriptblock.options.permissions;

import scriptblock.managers.ScriptManager;
import scriptblock.options.SBOption;

public class BypassOp extends SBOption {
   public BypassOp(ScriptManager scriptManager) {
      super(scriptManager, "bypass_op", "@bypass /");
   }

   public boolean isValid() {
      this.scriptLine = this.scriptLine.replaceFirst(this.getSyntax(), "");
      if(!this.player.isOp()) {
         try {
            this.player.setOp(true);
            this.player.performCommand(this.scriptLine);
         } finally {
            this.player.setOp(false);
         }
      } else {
         this.player.performCommand(this.scriptLine);
      }

      return true;
   }
}
