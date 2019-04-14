package classes_graphes;

import jbotsim.Node;

public class Noeud extends Node{

	private Node pred;
	private Node succ;
	
	public Noeud() {
		pred=null;
		succ=null;
	}

	public Node getPred() {
		return pred;
	}

	public void setPred(Node pred) {
		this.pred = pred;
	}

	public Node getSucc() {
		return succ;
	}

	public void setSucc(Node succ) {
		this.succ = succ;
	}
	
	
}
