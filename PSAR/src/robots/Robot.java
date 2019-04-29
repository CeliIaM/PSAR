package robots;

import java.lang.String;
import java.util.ArrayList;
import java.util.Random;

import jbotsim.Node;
import jbotsim.Point;
import jbotsim.Topology;
import jbotsimx.ui.JViewer;

public class Robot extends Node{
	private ArrayList<Point> locations;
	private Point target;

	public Robot() {
		 setIcon("/home/celia/Téléchargements/images.png");
		 setSize(16);
	}
	
	    @Override
	    public void onPreClock() {  // LOOK
	        locations = new ArrayList<>();
	        for (Node node : getSensedNodes())
	        	if (!node.getClass().equals(this.getClass()))
	            locations.add(node.getLocation());
	    }
	    @Override
	    public void onClock(){      // COMPUTE
	    	System.out.println("Dans le compute");
	    	target=locations.get(0);
	    	setDirection(target);
//	        if(!locations.isEmpty())
//	            target = locations.get((new Random()).nextInt(locations.size()));
	    }
	    @Override
	    public void onPostClock(){  // MOVE
//	        if(target == null)
//	            return;
//
	        setDirection(target);
	        move(Math.min(1, distance(target)));
	    }
	
	public static void main(String[] args){
		Topology tp = new Topology();
		tp.addNode(new Robot());
		
		new JViewer(tp);
		tp.start();
	}
}
