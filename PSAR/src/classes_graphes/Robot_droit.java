package classes_graphes;


import java.util.ArrayList;

import jbotsim.Node;
import jbotsim.Point;
import jbotsim.Topology;
import jbotsimx.ui.JViewer;

public class Robot_droit extends Node {

	// Variables
	// ***************************************************************************************************************************************
	private Noeud actuel; 
	private int nb_noeud;
	private int nb_robot;
	private State state = State.Righter;
	private Direction dir = Direction.Right;
	private Point lastlocation;
	private int id = (int) (Math.random() * 20);
	private int rightStep = 0; // utiliser dans MINDISCOVERY
	private int walkStep = 0; // utiliser dans HeadOrTailWalkerEndDiscovery
	private int idMin = -1;
	private int idPotentialMin = -1;
	private int idHeadWalker = -1;
	private ArrayList<Robot_droit> nodeMate = new ArrayList<>(); // robots sur le même noeud que lui
	private ArrayList<Integer> nodeMateId = new ArrayList<>(); // id des robots sur le même noeud que lui
	private ArrayList<Integer> walkerMate = new ArrayList<>(); // utilisé dans HeadWalkerWithoutWalkerMate
	private ArrayList<Node> locations;
	private Point target;
	private boolean AR_gauche, AR_droit; // LES ARÊTES GAUCHE ET DROITE A L'INSTANT D'AVANT
	private boolean AR_gaucheT, AR_droitT;
	// Constructeur ***********************************************************

	public Robot_droit(Noeud n,int nb_robot,int nb_noeud) {
		this.nb_robot=nb_robot;
		this.nb_noeud=nb_noeud;
		this.actuel=n;
		setIcon("/home/celia/Téléchargements/images.png");
		setSize(16);
	}
	// PREDICATES***********************************************************************************************************************************************

	public boolean minDiscovery() {
		int i;
		if (this.rightStep == (4 * this.id * nb_noeud)) {
			
			return true;
		} else {
			for (i = 0; i < nodeMate.size(); i++) {
				if (this.state.equals(State.PotentialMin)
						&& (nodeMate.get(i).getState().equals(State.Righter) && this.id < nodeMateId.get(i)))
					return true;

				if (nodeMate.get(i).getIDmin() == this.id && this.id != nodeMateId.get(i))
					return true;

				if ((nodeMate.get(i).getState().equals(State.DumbSearcher)
						|| nodeMate.get(i).getState().equals(State.PotentialMin))
						&& this.id < nodeMate.get(i).getIDpotentialMin() && this.id != nodeMateId.get(i))
					return true;

			}
		}
		return false;
	}

	public boolean gathering() {
		return (this.getSizeRobots() == nb_robot - 1) ? true : false;
	}

	public boolean weakGathering() {
		int i;
		if (this.getSizeRobots() == nb_robot - 2) {
			if (this.state.equals(State.MinTailWalker) || this.state.equals(State.MinWaitingWalker))
				return true;
			for (i = 0; i < nodeMate.size(); i++) {
				if (nodeMate.get(i).getState().equals(State.MinTailWalker)
						|| nodeMate.get(i).getState().equals(State.MinWaitingWalker))
					return true;
			}
		}
		return false;
	}

	public boolean headWalkerWithoutWalkerMate() {
		return (this.state.equals(State.HeadWalker) && this.existsEdge(Direction.Left, -1) && !this.hasmoved()
				&& !nodeMateId.equals(walkerMate)) ? true : false;
	}

	public boolean leftWalker() {
		return (this.state.equals(State.LeftWalker)) ? true : false;
	}

	public boolean headOrTailWalkerEndDiscovery() {
		return (this.headOrTailWalker() && nb_noeud == walkStep) ? true : false;

	}

	public boolean headOrTailWalker() {
		return (this.state.equals(State.HeadWalker) || this.state.equals(State.MinTailWalker)
				|| this.state.equals(State.TailWalker)) ? true : false;
	}

	public boolean allButTwoWaitingWalker() {
		int i;
		if (this.getSizeRobots() == nb_robot - 3) {
			if (!this.waitingWalker())
				return false;
			for (i = 0; i < nodeMate.size(); i++) {
				if (!nodeMate.get(i).waitingWalker())
					return false;
			}
			return true;
		}
		return false;
	}

	public boolean waitingWalker() {
		return (this.state.equals(State.WaitingWalker) || this.state.equals(State.MinWaitingWalker)) ? true : false;
	}

	public boolean potentialMinOrSearherWithMinWaiting(Robot_droit r) {
		if ((this.searcher() || this.state.equals(State.PotentialMin)) && r.getState().equals(State.MinWaitingWalker))
			return true;
		return false;
	}

	public boolean righterWithMinWaiting(Robot_droit r) {
		return (this.state.equals(State.Righter) && r.getState().equals(State.MinWaitingWalker)) ? true : false;
	}

	public boolean notWalkerWithHeadWalker(Robot_droit r) {
		return ((this.searcher() || this.potentialMinOrRighter()) && (r.getState().equals(State.HeadWalker))) ? true
				: false;
	}

	public boolean notWalkerWithTailWalker(Robot_droit r) {
		return ((this.searcher() || this.potentialMinOrRighter()) && (r.getState().equals(State.MinTailWalker))) ? true
				: false;
	}

	public boolean potentialMinWithAwareSeacher(Robot_droit r) {
		return (this.state.equals(State.PotentialMin) && r.getState().equals(State.AwareSearcher)) ? true : false;
	}

	public boolean allButOneRighter() {
		int i;
		if (this.getSizeRobots() == nb_robot - 2) {
			if (!this.state.equals(State.Righter))
				return false;
			for (i = 0; i < nodeMate.size(); i++) {
				if (!nodeMate.get(i).getState().equals(State.Righter))
					return false;
			}
			return true;
		}
		return false;
	}

	public boolean righterWithSearcher(Robot_droit r) {
		return (this.state.equals(State.Righter) && r.searcher()) ? true : false;
	}

	public boolean potentialMinOrRighter() {
		return (this.state.equals(State.Righter) || this.state.equals(State.PotentialMin)) ? true : false;
	}

	public boolean dumbSearcherMinRevelation() {
		int i;
		if (this.state.equals(State.DumbSearcher)) {
			for (i = 0; i < nodeMate.size(); i++) {
				if (nodeMate.get(i).getState().equals(State.Righter) && this.idPotentialMin < nodeMateId.get(i))
					return true;
			}
		}
		return false;
	}

	public boolean dumbSearcherWithAwareSearcher(Robot_droit r) {
		return (this.state.equals(State.DumbSearcher) && r.getState().equals(State.AwareSearcher)) ? true : false;
	}

	public boolean searcher() {
		return (this.state.equals(State.AwareSearcher) || this.state.equals(State.DumbSearcher)) ? true : false;
	}

	// fonctions en plus
	// ************************************************************************************************************

	public boolean existsEdge(Direction r, int t) {
		if(t==-1) {
			if (r.equals(Direction.Left))
				return AR_gauche;
			else {
				return AR_droit;
			}
		}else if(t==0) {
			if (r.equals(Direction.Left))
				return AR_gaucheT;
			else {
				return AR_droitT;
			}
		}
		return false;
		
	}

	public int getSizeRobots() {
		int i, res = 0;
		for (i = 0; i < nodeMate.size(); i++) {
			if (this.id != nodeMateId.get(i))
				res++;
		}
		return res;
	}

	public State getState() {
		return this.state;
	}

	public ArrayList<Integer> getWalkerMate() {
		return walkerMate;
	}

	public int getIDHeadWalker() {
		return idHeadWalker;
	}

	public int getID() {
		return id;
	}

	public int getIDmin() {
		return idMin;
	}

	public int getIDpotentialMin() {
		return idPotentialMin;
	}

	boolean hasmoved() {
		return (lastlocation.equals(target)) ? false : true;
	}

	public int getWalkStep() {
		return walkStep;
	}

	// Fonctions d'actions *************************************************************************************************************************

	void stopMoving() {
		dir = Direction.none;
	}

	void moveLeft() {
		dir = Direction.Left;
	}

	void becomeLeftWalker() {
		dir = Direction.none;
		state = State.LeftWalker;
	}

	void walk() {
		if ((id == idHeadWalker && !walkerMate.equals(nodeMateId))
				|| (id != idHeadWalker && nodeMateId.contains(idHeadWalker))) {
			dir = Direction.none;

		} else {
			dir = Direction.Right;

		}
		if (dir == Direction.Right && existsEdge(Direction.Right, 0)) {
			walkStep = walkStep + 1;
		}
	}

	void initiateWalk() {
		idHeadWalker = id;
		for (int i = 0; i < nodeMateId.size(); i++) {
			if (idHeadWalker < nodeMateId.get(i)) {
				idHeadWalker = nodeMateId.get(i);
			}
		}
		for (int i = 0; i < nodeMateId.size(); i++) {
			walkerMate.add(nodeMateId.get(i));
		}
		if (id == idHeadWalker) {
			state = State.HeadWalker;
			return;
		}
		if (state == State.MinWaitingWalker) {
			state = State.MinTailWalker;
			return;
		}
		state = State.TailWalker;
	}

	void becomeWaitingWalker(Robot_droit r_prime) {
		state = State.WaitingWalker;
		idPotentialMin = r_prime.getID();
		idMin = r_prime.getID();
		dir = Direction.none;

	}

	void becomeMinWaitingWalker() {
		state = State.MinWaitingWalker;
		idPotentialMin = id;
		idMin = id;
		dir = Direction.none;
	}

	void becomeAwareSearcher(Robot_droit r_prime) {
		state = State.AwareSearcher;
		dir = Direction.Right;
		if (r_prime.getState() == State.DumbSearcher) {
			idPotentialMin = r_prime.getIDpotentialMin();
			idMin = r_prime.getIDpotentialMin();
			return;
		}
		
		idPotentialMin = r_prime.getIDmin();
		idMin = r_prime.getIDmin();
	}

	void becomeTailWalker(Robot_droit r_prime) {
		state = State.TailWalker;
		idPotentialMin = r_prime.getIDpotentialMin();
		idMin = r_prime.getIDmin();
		idHeadWalker = r_prime.getIDHeadWalker();
		walkStep = r_prime.getWalkStep();
		walkerMate = new ArrayList<>();
		for (int i = 0; i < r_prime.getWalkerMate().size(); i++) {
			walkerMate.add(r_prime.getWalkerMate().get(i));
		}

	}

	void moveRight() {
		dir = Direction.Right;
		if (existsEdge(dir, 0)) {
			rightStep = rightStep + 1;
		}
	}

	void initiateSearch() {
		idPotentialMin = id;
		for (int i = 0; i < nodeMateId.size(); i++) {
			if (idPotentialMin < nodeMateId.get(i)) {
				idPotentialMin = nodeMateId.get(i);
			}
		}
		if (idPotentialMin == id) {
			state = State.PotentialMin;
		} else {
			state = State.DumbSearcher;
		}
		if (state == State.PotentialMin && existsEdge(dir, 0)) {
			rightStep = rightStep + 1;
		}
	}

	void search() {
		boolean max=true;
		for(int i=0;i<nodeMateId.size();i++) {
			if (id<nodeMateId.get(i)) {
				max=false;
				break;
			}
		}
		if (nodeMate.size()>=1 && max ) {
			dir=Direction.Left;
			return;
		}
		if (nodeMate.size()>=1 && !max ) {
			dir=Direction.Right;
			return;
		}
	

	}

	// L M C
	// ******************************************************************************************************************************

	@Override
	public void onPreClock() { // LOOK

		// On remet les compteurs à zéro
		AR_droit=false;
		AR_gauche=false;
		AR_droitT=false;
		AR_gaucheT=false;
		nodeMate=new ArrayList<>();
		nodeMateId=new ArrayList<>();
		System.err.println("ID robot est "+ id);
		//On regarde si les aretes existent
		for (Node n : this.getNeighbors() ) {
			if (this.getLocation().equals(n.getLocation()) && !n.getClass().equals(this.getClass())) {
				actuel=(Noeud)n;
			}
		}
		for (Node n : actuel.getNeighbors()) {
			if (n.getClass().equals(this.getClass())) {
				continue;
			}
			Noeud node=(Noeud) n;
			if (node.equals(actuel.getPred())) {
				AR_gaucheT=true;
			}
			if (node.equals(actuel.getSucc())) {
				AR_droitT=true;
			}
		}

		//Je cherche les robots sur le même noeud
		for (Node node : getSensedNodes()) {
			if (node.getClass().equals(this.getClass())) {
			/*	if(((Robot_droit)node).equals(this)) {
					continue;
				}*/
				nodeMate.add((Robot_droit)node);
				nodeMateId.add(((Robot_droit)node).getID());
			}
		}
		for (int i=0;i<nodeMate.size();i++) {
		//	Robot_droit n=(Robot_droit)node;
			if(nodeMate.get(i).equals(this)) {
				continue;
			}
			System.out.println(nodeMate.get(i).getState());
			
		}
		locations = new ArrayList<>();
        for (Node node : getSensedNodes()) {
        	if (!node.getClass().equals(this.getClass())) {
        		locations.add(node);
        	}
        }
		
	}

	@Override
	public void onClock() { // COMPUTE

		boolean phase_ok=false;
		//Terminaison

		if (gathering() && !phase_ok) {
			phase_ok=true;
			System.out.println("Gathering ok "+ id);
			return;
		}

		if (weakGathering()&& !phase_ok ) {
			phase_ok=true;
			System.out.println("Weak Gathering ok "+ id);
			return;
		}

		//Phase T

		//T1
		if (leftWalker() && !phase_ok) {
			System.out.println("Phase T1 "+ id);
			moveLeft();
			phase_ok=true;

		}

		//T2
		if (headWalkerWithoutWalkerMate() && !phase_ok) {
			System.out.println("Phase T2 "+ id);
			becomeLeftWalker();
			phase_ok=true;
		}

		//T3
		if (headOrTailWalkerEndDiscovery() && !phase_ok) {
			stopMoving();
			System.out.println("Phase T3 "+ id);
			phase_ok=true;
		}


		//Phase W

		//W1
		if (headOrTailWalker() && !phase_ok) {
			walk();
			System.out.println("Phase W1 "+ id);
			phase_ok=true;
		}

		//Phase K

		//K1
		if (allButTwoWaitingWalker() && !phase_ok) {
			System.out.println("Phase K1 "+ id);
			initiateWalk();
			phase_ok=true;
		}

		//K2
		if (waitingWalker() && !phase_ok) {
			System.out.println("Phase K2 "+ id);
			stopMoving();
			phase_ok=true;
		}

		//K3
		for(int i=0;i<nodeMate.size();i++) {
			if (potentialMinOrSearherWithMinWaiting(nodeMate.get(i)) && !phase_ok) {
				System.out.println("Phase K3 "+ id);
				becomeWaitingWalker(nodeMate.get(i));
				phase_ok=true;
				break;
			}
		}

		//K4
		for (int i=0; i<nodeMate.size();i++) {
			if (righterWithMinWaiting(nodeMate.get(i)) && existsEdge(Direction.Right, 0) && !phase_ok){
				System.out.println("Phase K4 "+ id);
				becomeAwareSearcher(nodeMate.get(i));
				phase_ok=true;
				break;
			}
		}


		//Phase M

		//M1
		if (potentialMinOrRighter() && minDiscovery() && !phase_ok) {
			System.out.println("Phase M1 "+ id);
			
			becomeMinWaitingWalker();
			phase_ok=true;

		}

		//M2
		for (int i=0; i<nodeMate.size();i++) {
			if (notWalkerWithHeadWalker(nodeMate.get(i)) && existsEdge(Direction.Right, 0) && !phase_ok){
				System.out.println("Phase M2 "+ id);
				becomeAwareSearcher(nodeMate.get(i));
				phase_ok=true;
				break;
			}
		}

		//M3
		for (int i=0; i<nodeMate.size();i++) {
			if (notWalkerWithHeadWalker(nodeMate.get(i)) && !phase_ok){
				System.out.println("Phase M3 "+ id);
				becomeAwareSearcher(nodeMate.get(i));
				phase_ok=true;
				break;
			}
		}

		//M4
		for (int i=0; i<nodeMate.size();i++) {
			if (notWalkerWithTailWalker(nodeMate.get(i)) && !phase_ok){
				System.out.println("Phase M4 "+ id);
				becomeTailWalker(nodeMate.get(i));
				phase_ok=true;
				break;
			}
		}

		//M5
		for (int i=0; i<nodeMate.size();i++) {
			if (potentialMinWithAwareSeacher(nodeMate.get(i)) && !phase_ok){
				System.out.println("Phase M5 "+ id);
				becomeAwareSearcher(nodeMate.get(i));
				search();
				phase_ok=true;
				break;
			}
		}

		//M6
		if (allButOneRighter() && !phase_ok) {
			System.out.println("Phase M6 "+ id);
			initiateSearch();
			phase_ok=true;

		}


		//M7
		for (int i=0; i<nodeMate.size();i++) {
			if (righterWithSearcher(nodeMate.get(i)) && !phase_ok){
				System.out.println("Phase M7 "+ id);
				becomeAwareSearcher(nodeMate.get(i));
				search();
				phase_ok=true;
				break;
			}
		}

		//M8
		if (potentialMinOrRighter() && !phase_ok) {
			System.out.println("Phase M8 "+ id);
			moveRight();
			phase_ok=true;

		}

		//M9
		if (dumbSearcherMinRevelation() && !phase_ok) {
			System.out.println("Phase M9 "+ id);
			becomeAwareSearcher(this);
			search();
			phase_ok=true;

		}

		//M10
		for (int i=0; i<nodeMate.size();i++) {
			if (dumbSearcherWithAwareSearcher(nodeMate.get(i)) && !phase_ok){
				System.out.println("Phase M10 "+ id);
				becomeAwareSearcher(nodeMate.get(i));
				search();
				phase_ok=true;
				break;
			}
		}

		//M11
		if (searcher() && !phase_ok) {
			System.out.println("Phase M11 "+ id);
			search();
			phase_ok=true;
		}

		lastlocation=this.getLocation();
	//	target=null;

		if (locations.size()>0) {
    		for (Node n: locations.get(0).getNeighbors()) {
    			if (!n.getClass().equals(this.getClass())) {
    				
    				Noeud node=(Noeud)n;
    			
    				if (dir==Direction.Right && node.equals(actuel.getPred())) {
    				//if (dir==Direction.Right && locations.get(0).equals(node.getPred())) {
    					
    					target=node.getLocation();
    					return;
    				}
    				if (dir==Direction.Left && node.equals(actuel.getSucc())) {
    				//if (dir==Direction.Left && locations.get(0).equals(node.getSucc())) {
    				
    					target=node.getLocation();
    					return;
    				}
    			}
    		}
			
		}
	
	
    	//Sinon je ne bouge pas
    	target=this.getLocation();
    	

	}

	@Override
	public void onPostClock() { // MOVE
		this.AR_droit=this.AR_droitT;
		this.AR_gauche=this.AR_gaucheT;
		if(target==this.getLocation()) {
			return;
		}
		
		setLocation(target.getX(), target.getY());
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

	// MAIN ()
	// ***********************************************************************************************************************************************

	public static void main(String[] args) {

//		Topology tp = new Topology();
//		ST graph=new ST(10);
//		tp.setSensingRange(1);
//		graph.afficher(tp);
//		int nb_robot=0;
//		for (Noeud n :graph.getGraph()) {
//			if (Math.random()*100<75 && nb_robot<4) {
//				nb_robot++;
//				tp.addNode(n.getLocation().getX(),n.getLocation().getY(),new Robot_droit(n,4,10));
//			}
//		}
//
//		new JViewer(tp);
//		tp.start();
	
			Topology tp = new Topology();
				BRE graph = new BRE(10, 50, 1);
				tp.setSensingRange(1);
				graph.afficher(tp);
				int nb_robot=0;
				tp.addNode(new Robot_gestionnaire(graph, tp));
				for (Noeud n : graph.getGraph()) {
					if (Math.random() * 100 < 75 && nb_robot<4) {
						nb_robot++;
						tp.addNode(n.getLocation().getX(), n.getLocation().getY(),new Robot_droit(n,4,10));
					}
					
				}
		
				new JViewer(tp);
				tp.start();
	}
}