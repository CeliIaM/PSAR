package classes_graphes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jbotsim.Link;
import jbotsim.Node;
import jbotsim.Topology;
import jbotsimx.ui.JViewer;

public class BRE {
	private double proba;
	private int nb_noeud;
	private int temps;
	private Map<Link,Integer> disparu;
	private ArrayList<Noeud> graph;
	private ArrayList<Link> liens;



	

	public BRE(int noeud,double proba, int temps) {
		this.proba=proba;
		this.temps=temps;
		nb_noeud=noeud;
		disparu=new HashMap<>();
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
	
	public ArrayList<Noeud> getGraph() {
		return graph;
	}

	public void setGraph(ArrayList<Noeud> graph) {
		this.graph = graph;
	}
	
	// Penser à la proba de réappariton avant random au moment de l'ajout dans la hash map
	public void modifer() {
		ArrayList<Link> supp=new ArrayList<Link>();
		List<Link >dis = new ArrayList<Link>(disparu.keySet());

		for (int i=0;i<liens.size();i++) {
			double n=Math.random()*100;
			if (n<=proba) {
				supp.add(liens.get(i));
			}
		}
		for (int i=0;i<supp.size();i++) {
			disparu.put(supp.get(i),(int)(Math.random()*temps));
			liens.remove(supp.get(i));

		}
		
		for (int i=0;i<dis.size();i++) {
			Link k=dis.get(i);
			int val=disparu.get(k);
			disparu.replace(k, val-1);
			k=dis.get(i);
			val=disparu.get(k);
			if(disparu.get(dis.get(i))<=0) {
				liens.add(k);
				disparu.remove(k);
			}
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
		BRE st=new BRE(5, 100,1);
		Topology tp=new Topology();
		st.afficher(tp);
		new JViewer(tp);
		tp.start();

	}

}