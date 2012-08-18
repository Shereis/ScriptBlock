package scriptblock;

public class BlockCoords {

	public String world;
	public int x;
	public int y;
	public int z;
	
	private String coords ;
	private String fullCoords ;

	public BlockCoords(String world, int x, int y, int z)
	{
		this.world = world ;
		this.x = x ;
		this.y = y ;
		this.z = z ;
		this.coords = ( x+","+y+","+z ) ;
		this.fullCoords = ( world+","+coords ) ;
	}
	
	public String getCoords()
		{ return coords ; }
	
	public String getFullCoords()
		{ return fullCoords ; }
}
