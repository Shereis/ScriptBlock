package scriptblock.options;

public abstract class Option {

	private final String name ;
	private final String syntax ;
	private boolean inDebugMode ;
	
	public Option(String name, String syntax)
	{
		this.name = name ;
		this.syntax = syntax ;
	}
	
	public abstract boolean onOptionCall(OptionHandler optionHandler) ;

	public String getName() 
		{ return name; }

	public String getSyntax() 
		{ return syntax; }
	
	public boolean showConsole()
		{ return inDebugMode ; }
	
	public void setDebugMode(boolean debugMode)
		{ this.inDebugMode = debugMode ; }
	
	public abstract interface Permissible {}
}


