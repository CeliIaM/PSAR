import jbotsim.Color;
import jbotsim.Link;
import jbotsim.Message;
import jbotsim.Node;

public class TestNoeud extends Node{
	boolean alone=true;

	public void onStart() {
		setColor(Color.green);
	}

	public void onLinkAdded(Link link) {
		setColor(Color.red);
	}
	@Override
	public void onLinkRemoved(Link link) {
		if (!hasNeighbors())
			setColor(Color.green);
	}


}
