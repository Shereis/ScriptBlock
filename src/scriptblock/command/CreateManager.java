package scriptblock.command;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import scriptblock.BlockCoords;
import scriptblock.SLAPI;
import scriptblock.command.CommandHandler.CommandType;
import scriptblock.managers.PermManager;
import scriptblock.managers.ScriptManager;
import scriptblock.options.Option;
import scriptblock.options.Option.Permissible;

@SuppressWarnings("deprecation")
public abstract class CreateManager extends BindScript {

	protected LinkedList < String > commandList ;	
	
	public CreateManager(ScriptManager scriptManager, Player player, CommandType commandType)
	{
		super(scriptManager, player, commandType);
		
		String consoleMsg = ("["+plugin.getName()+"] "+commandSender.getName()+" performed command \""+commandType.name()+"\"...") ;
		log.info(consoleMsg) ;
	}
	
	
	protected abstract boolean isValid() ;
	
	protected boolean create(BlockCoords blockcoords)
	{				
		String FullBlockCoords = blockcoords.getFullCoords() ;
		
		if ( !canAccessScript(blockcoords) ) return false ;
				
		mapManager.blocksMap.put(FullBlockCoords, commandList) ;
		
	//Saving blocksMap to yml .
		
		fileManager.getScriptConfig().setProperty(blockcoords.world+"."+blockcoords.getCoords()+".", commandList);
		fileManager.getScriptConfig().save();
				
	//Removing CoolDown and delay...
		if ( mapManager.cooldownMap.containsKey(FullBlockCoords) ) 
		{				
			mapManager.cooldownMap.remove(FullBlockCoords);			
			try  {				
				SLAPI.save( mapManager.cooldownMap, fileManager.getCooldownDataFile().getPath() );
				log.info("cooldown Saved to CoolDownData.dat!");				
			} 
			catch (Exception e){
				log.info("[ERROR] while saving cooldownBlockMap to CoolDownData.dat ![ERROR]");
				log.info("at "+e.getMessage());
			}					
		}
		
		mapManager.delayList.remove(FullBlockCoords) ;		
		commandSender.sendMessage(ChatColor.GREEN + "["+plugin.getName()+"] Text Successfully bound !");
		return true ;
	}
	

	
	
	//TODO Check it ! There is some error inside !!!
	public boolean optionCheck(LinkedList <String> list)
	{
		boolean haveOption = false ;
		
		for (int i = 0 ; i < list.size() ; i++)
		{	
			String scriptLine = list.get(i) ;
			for ( Option option : mapManager.optionsList )
			{	
				
				if ( scriptLine.startsWith( option.getSyntax() ) )
				{					
					haveOption = true ;
					
					String optionNode = new String ( "option" + PermManager.sep + option.getName() ) ;											
					String param = null ;					
					
					if ( option instanceof Permissible )
					{
						param  = scriptLine.replaceFirst( option.getSyntax(),"" ) ; 
						
						if ( param.contains(" /") )
							{ param = param.substring( 0, param.indexOf(" /") ) ; }
					}
																															
					
					String permMessage = ( ChatColor.RED+"["+plugin.getName()+"] You need permission to use: "+
											ChatColor.WHITE + option.getSyntax() ) ;
																			
					String consoleMsg1 = ( "["+plugin.getName()+"] Option \""+option.getName()+"\" added" ) ;
					
					if ( param != null ) 
					{	
						String perm = new String( optionNode + PermManager.sep + param );
						if ( permManager.hasSBPerm( commandSender, perm , false) )
						{
							if( option.showConsole() )
								{ log.info(consoleMsg1+" with param \""+param+"\" !") ; }
							
							break ;
						}							
						else {								
							commandSender.sendMessage( permMessage + param + ChatColor.RED + " !!" );
							list = null ;
							return false ;
						}							
					}						
					else {
						
						if ( permManager.hasSBPerm( commandSender, optionNode, false) )
						{							
							if ( option.showConsole() ) { log.info(consoleMsg1+"...") ; }
							break ; //TODO test this
						}
						
						else {
							commandSender.sendMessage( permMessage + ChatColor.RED + " !!" );							
							list = null ;
							return false ;				
						}
					}									
				}
			}
			
			if (!haveOption) 
			{ 				
				commandSender.sendMessage(ChatColor.RED+"["+plugin.getName()+"] BAD SCRIPT !");
				list = null ;
				return false ; 
			}
		}
		
		return true ;
	}
	
	
	public boolean opCheck(LinkedList <String> list)
	{
		if ( !commandSender.isOp() ){
			if ( ContainsOpCommands(list) ){
				
				commandSender.sendMessage(ChatColor.RED + "["+plugin.getName()+"] Only Ops can Write Op's Commands !!!");
				list = null ;
				return false;
			}
		}
		return true ;
	}
	
	/** Method to check if the commandList contain bypass Ops command or not ! */
	
	protected boolean ContainsOpCommands(LinkedList<String> commandList)
	{
		String[] opCommands = { "op","deop","save-all",
								"save-off","save-on","stop",
								"ban-ip","ban","pardon-ip",
								"pardon","gamemode","kick",
								"whitelist","?" } ;
		
		Iterator<String> iter = commandList.iterator() ;
		
		while (iter.hasNext())
		{
			String n = iter.next() ;
			String m = new String() ;
			Option bypassOp = scriptManager.getOptionManager().bypassOp ;
			
			if ( n.startsWith( bypassOp.getName() ) )
			{
				for(int i = 0; i < opCommands.length ; i++)
				{
					m = n.replaceFirst( bypassOp.getSyntax(), "");
					m = m.toLowerCase();
					
					if (m.startsWith(opCommands[i]))
						{ return true ; }
				}	
			}		
		}		
		return false;				
	}
		
/** Method to extract commands that are cercled by [] in a String and add it to a list.*/
	
	protected LinkedList<String> CommandScript(String text, LinkedList<String> commandList)
	{		
		String finalText = new String() ;
		
		if (text.contains("[") && text.contains("]"))
		{
			while  (text.contains("[") && text.contains("]"))
			{
				int a = text.indexOf("[")+1 ;
				int b = text.indexOf("]") ;
				finalText = text.substring(a, b) ;	 //extract the command from the text
				text = text.replace(finalText, "") ; //removing the extracted command from the text
				text = text.replace("[]", "") ;		 //removing the [] that was used to isolated the extracted command
				
				commandList.add(finalText);			 //Register command to a list
			}
		} 
		else commandList.add(text);
		
		return commandList;
	}		
	
/** Method to Convert Array in a String*/
	
	protected String ArrayToString(String[] arg)
	{
		String text = new String() ;
		StringBuilder stringBuilder = new StringBuilder() ;
		Iterator<String> iter = Arrays.asList(arg).iterator() ;
		
		while(iter.hasNext())
		{
			String s = iter.next() ;
			if ( s != null && !s.isEmpty() )
				stringBuilder.append(" ").append(s) ;
		}
		
		text = stringBuilder.toString().replaceFirst(" ", "") ;	
		
		return text ;
	}						

		
}
