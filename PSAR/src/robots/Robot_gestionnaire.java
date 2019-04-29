package robots;

import java.util.ArrayList;

import classes_graphes.Graphe;
import jbotsim.Node;
import jbotsim.Topology;

public class Robot_gestionnaire extends Node {
	private Graphe graphe;
	private Topology tp;
	
		public Robot_gestionnaire(Graphe graph,Topology tp) {
			// TODO Auto-generated constructor stub
			this.graphe=graph;
			this.tp=tp;
		}
	   @Override
	    public void onPreClock() {  // LOOK
	       
	    }
	    @Override
	    public void onClock(){      // COMPUTE
	    	
	    }
	    @Override
	    public void onPostClock(){  // MOVE
	       graphe.modifier();
	       graphe.afficher(tp);
	       try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
}
