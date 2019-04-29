package prise_en_main;
import jbotsim.Node;
import jbotsim.Point;
import jbotsim.Topology;
import jbotsimx.ui.JViewer;

public class Hello_world{
	public static void main(String[] args){
		Topology tp = new Topology();
		Node n=new Node();
		Node n1=new Node();
		tp.addNode(100, 100,n1);
		tp.addNode(650, 50,n);
		new JViewer(tp);
		tp.start();
	}
}   