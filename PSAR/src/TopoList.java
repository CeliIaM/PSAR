
import jbotsim.Color;
import jbotsim.Link;
import jbotsim.Node;
import jbotsim.Topology;
import jbotsim.event.ConnectivityListener;
import jbotsim.event.TopologyListener;

public class TopoList implements TopologyListener, 
                                         ConnectivityListener{
    public TopoList(Topology tp){
        tp.addTopologyListener(this);
        tp.addConnectivityListener(this);
    }
    @Override // TopologyListener
    public void onNodeAdded(Node node) {
        node.setColor(node.hasNeighbors()?Color.cyan:Color.orange);
    }
    @Override // TopologyListener
    public void onNodeRemoved(Node node) {
    }
    @Override // ConnectivityListener
    public void onLinkAdded(Link link) {
        for (Node node : link.endpoints())
            node.setColor(Color.red);
    }
    @Override // ConnectivityListener
    public void onLinkRemoved(Link link) {
        for (Node node : link.endpoints())
            if (!node.hasNeighbors())
                node.setColor(Color.yellow);
    }
}