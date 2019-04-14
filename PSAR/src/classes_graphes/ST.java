package classes_graphes;

import java.util.ArrayList;

import jbotsim.Link;
import jbotsim.Node;
import jbotsim.Topology;
import jbotsimx.ui.JViewer;

public class ST {
	private int nb_noeud;
	private ArrayList<Noeud> graph;
	private ArrayList<Link> liens;
	public ST(int noeud) {
		nb_noeud=noeud;
		graph=new ArrayList<Noeud>();
		liens=new ArrayList<Link>();
		for (int i=0;i<nb_noeud;i++) {
			graph.add(new Noeud());
		}
		for (int i=0;i<nb_noeud;i++) {
			graph.get(i).setSucc(graph.get((i+1)%nb_noeud));
			graph.get(i).setPred(graph.get(Math.floorMod(i-1,nb_noeud)));
		}
		for(int i=0;i<nb_noeud;i++) {
			liens.add(new Link(graph.get(i),graph.get((i+1)%nb_noeud)));
			
		
	}
}


	public void afficher(Topology tp) {
	    System.out.println(liens.size());
	    int taille_arête=100;
	    int largeure=(tp.getWidth()+nb_noeud*5);
	    for (int i=0;i<nb_noeud;i++){
	        if(i==nb_noeud -1|| i ==0) {
	            tp.addNode(tp.getWidth(),largeure-i*taille_arête,graph.get(i));
	            
	        }else{
	         
	            tp.addNode(tp.getHeight(),largeure-i*taille_arête,graph.get(i));
	            tp.addNode(tp.getHeight()+2*(tp.getWidth()-tp.getHeight()),largeure-i*taille_arête,graph.get(graph.size()-i));
	            nb_noeud --;
	        }
	    }
	    tp.clearLinks();
	    for (int i=0;i<liens.size();i++) {
	        tp.addLink(liens.get(i));
	    }
	      }  



public ArrayList<Noeud> getGraph() {
	return graph;
}


public void setGraph(ArrayList<Noeud> graph) {
	this.graph = graph;
}


public ArrayList<Link> getLiens() {
	return liens;
}


public void setLiens(ArrayList<Link> liens) {
	this.liens = liens;
}


}
