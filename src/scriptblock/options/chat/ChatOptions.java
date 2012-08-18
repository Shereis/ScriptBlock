package scriptblock.options.chat;

import scriptblock.managers.ScriptManager;
import scriptblock.options.SBOption;

public class ChatOptions {
	
		
	public static class Say extends SBOption {

		public Say(ScriptManager scriptManager) 
			{ super(scriptManager, "say", "@say "); }

		@Override
		public boolean isValid() 
		{		
			scriptLine = scriptLine.replaceFirst( getSyntax(), "");
			player.chat(scriptLine);		
			return true;
		}
	}



	public static class Command extends SBOption {

		public Command(ScriptManager scriptManager) 
			{ super(scriptManager, "command", "@command /"); }

		@Override
		public boolean isValid() 
		{
			scriptLine = scriptLine.replaceFirst( getSyntax(), "");
			player.performCommand(scriptLine);		
			return true ;
		}
		
	}


	public static class ToPlayer extends SBOption {
		
		public ToPlayer (ScriptManager scriptManager)
			{ super(scriptManager, "toplayer" , "@player "); }

		@Override
		public boolean isValid() 
		{
			scriptLine = scriptLine.replaceFirst( getSyntax(), "");
			player.sendMessage(scriptLine);		
			return true;
		}
	}



	
	
}


