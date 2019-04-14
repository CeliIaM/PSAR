package classes_graphes;

import java.util.ArrayList;

import jbotsim.Node;
import jbotsim.Topology;

public class Robot_gestionnaire extends Node {
	private BRE graph;
	private Topology tp;
	
		public Robot_gestionnaire(BRE graph,Topology tp) {
			// TODO Auto-generated constructor stub
			this.graph=graph;
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
	       graph.modifer();
	       graph.afficher(tp);
	       try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
}
