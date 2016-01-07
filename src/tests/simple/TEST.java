package tests.simple;
class Shared {
	private int refcount = 0 ;
 	private static long counter = 0;
	private final long id = counter++;
	public Shared() {
		System.out.println("Creating "+ this);
	}
	public void addRef() { refcount++;}
	
	protected void  dispose() {
		if( --refcount == 0 ) {
			System.out.println("Disposing "+this);
		}
	}
	public String toString() { return "Shard "+this;}
}

class Composing {
	private Shared shard;
	private static long counter = 0;
	private final long id = counter++;
	public Composing(Shared shared) {
		System.out.println("Creating "+this);
		this.shard = shard;
		this.shard.addRef();
	}
	protected void dispose() {
		System.out.println("dispose "+this);
		shard.dispose();
	}
	public String toString() {return "Composing " + id;}
}
public class TEST {
	public static void main(String[] args) {
		Shared shared = new Shared();
		Composing[] composing = { new Composing(shared),new Composing(shared),new Composing(shared),new Composing(shared),new Composing(shared),new Composing(shared)};
		for(Composing c : composing){
			c.dispose();
		}
	}

}
