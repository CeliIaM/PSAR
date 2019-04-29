package classes_graphes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jbotsim.Link;
import jbotsim.Topology;
import jbotsimx.ui.JViewer;

public class COT implements Graphe {

	// Variables.

	// Proba de disparition d'une arête + eventual missing edge
	private double proba_disparition_simple;
	private double proba_missing_egde;

	// Nombre de noeuds du graphe
	private int nb_noeud;

	// Temps du tirage de l'enventual missing edge
	private int temps_tirage_missing_edge;

	// Aretes disparues
	private Map<Link, Integer> disparu;

	// Noeuds du graphe
	private ArrayList<Noeud> graph;

	// Liens présents dans le graphe
	private ArrayList<Link> liens;

	// Liste de tous les liens
	private ArrayList<Link> every_liens;

	// On comptabilise le temps pour savoir quand tirer l'eventual missing edge
	private int pas_de_temps;

	// Borne temps
	private int temps_max;

	// Constructeur
	public COT(int noeud, double proba_disparition_simple, double proba_missing_egde, int temps_tirage_missing_edge,
			int temps_max) {
		this.proba_disparition_simple = proba_disparition_simple;
		this.proba_missing_egde = proba_missing_egde;
		this.temps_tirage_missing_edge = temps_tirage_missing_edge;
		this.temps_max = temps_max;
		nb_noeud = noeud;
		disparu = new HashMap<>();
		graph = new ArrayList<Noeud>();
		liens = new ArrayList<Link>();
		every_liens = new ArrayList<Link>();
		pas_de_temps = 0;
		for (int i = 0; i < nb_noeud; i++) {
			graph.add(new Noeud());
		}
		for (int i = 0; i < nb_noeud; i++) {
			graph.get(i).setSucc(graph.get((i + 1) % nb_noeud));
			graph.get(i).setPred(graph.get(Math.floorMod(i + 1, nb_noeud)));
		}
		for (int i = 0; i < nb_noeud; i++) {
			Link l = new Link(graph.get(i), graph.get((i + 1) % nb_noeud));
			liens.add(l);
			every_liens.add(l);

		}

	}

	// Methods
	public ArrayList<Noeud> getGraph() {
		return graph;
	}

	public void setGraph(ArrayList<Noeud> graph) {
		this.graph = graph;
	}

	@Override 
	public void modifier() {

		pas_de_temps--;

		if (pas_de_temps == 0 && Math.random() * 100 < proba_missing_egde) {
			System.out.println("Missing edge");
			int indice = (int) (Math.random() * every_liens.size());
			liens.remove(every_liens.get(indice));
		}

		ArrayList<Link> supp = new ArrayList<Link>();
		List<Link> dis = new ArrayList<Link>(disparu.keySet());

		for (int i = 0; i < liens.size(); i++) {
			if (Math.random() * 100 <= proba_disparition_simple) {
				supp.add(liens.get(i));
			}
		}

		for (int i = 0; i < supp.size(); i++) {
			int temps = (int) (Math.random() * temps_max);
			temps_max = temps_max + temps;
			disparu.put(supp.get(i), (int) (Math.random() * temps));
			liens.remove(supp.get(i));

		}

		for (int i = 0; i < dis.size(); i++) {
			Link k = dis.get(i);
			int val = disparu.get(k);
			disparu.replace(k, val - 1);
			k = dis.get(i);
			val = disparu.get(k);
			if (disparu.get(dis.get(i)) <= 0) {
				liens.add(k);
				disparu.remove(k);
			}
		}

	}

	public void afficher(Topology tp) {
		System.out.println(liens.size());
		int taille_arête = 100;
		int largeure = (tp.getWidth() + nb_noeud * 5);
		for (int i = 0; i < nb_noeud; i++) {
			if (i == nb_noeud - 1 || i == 0) {
				tp.addNode(tp.getWidth(), largeure - i * taille_arête, graph.get(i));

			} else {

				tp.addNode(tp.getHeight(), largeure - i * taille_arête, graph.get(i));
				tp.addNode(tp.getHeight() + 2 * (tp.getWidth() - tp.getHeight()), largeure - i * taille_arête,
						graph.get(graph.size() - i));
				nb_noeud--;
			}
		}
		tp.clearLinks();
		for (int i = 0; i < liens.size(); i++) {
			tp.addLink(liens.get(i));
		}
	}

	



}
