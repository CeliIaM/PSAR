package classes_graphes;

import java.util.ArrayList;

import jbotsim.Link;
import jbotsim.Topology;
import jbotsimx.ui.JViewer;

public class AC implements Graphe {

	private double proba;
	private int nb_noeud;
	private int temps_max;
	private Link disparu=null;
	private int temps_disparu;
	private ArrayList<Noeud> graph;
	private ArrayList<Link> liens;
	
	
	public ArrayList<Noeud> getGraph() {
		return graph;
	}

	public void setGraph(ArrayList<Noeud> graph) {
		this.graph = graph;
	}




	public AC(int noeud,double proba, int temps) {
		this.proba=proba;
		this.temps_max=temps;
		nb_noeud=noeud;
		graph=new ArrayList<Noeud>();
		liens=new ArrayList<Link>();
		for (int i=0;i<nb_noeud;i++) {
			graph.add(new Noeud());
		}
		for (int i=0;i<nb_noeud;i++) {
			graph.get(i).setSucc(graph.get((i+1)%nb_noeud));
			graph.get(i).setPred(graph.get(Math.floorMod(i+1,nb_noeud)));
		}
		for(int i=0;i<nb_noeud;i++) {
			liens.add(new Link(graph.get(i),graph.get((i+1)%nb_noeud)));


		}

	}
	// Penser à la proba de réappariton avant random au moment de l'ajout dans la hash map
	public void modifier() {
	
		if(disparu!=null) {
			if(temps_disparu==0) {
				liens.add(disparu);
				disparu=null;
			}else {
				temps_disparu--;
			}
			
		}
		
		if(Math.random()*100<proba && disparu==null) {
			int indice=(int) (Math.random()*liens.size());
			int temps =(int) (Math.random()*temps_max);
			disparu=liens.get(indice);
			temps_disparu=temps;
			liens.remove(indice);
			temps_max=temps_max+temps;
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


	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		AC ac=new AC(5, 100,5);
		Topology tp=new Topology();
		ac.afficher(tp);
		new JViewer(tp);
		tp.start();

	}


}
