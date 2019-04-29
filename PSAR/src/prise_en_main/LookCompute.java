package prise_en_main;
import jbotsim.Node;
import jbotsim.Point;

import java.util.ArrayList;
import java.util.Random;

public class LookCompute extends Node{
    ArrayList<Point> locations;
    Point target;

    @Override
    public void onPreClock() {  // LOOK
        locations = new ArrayList<Point>();
        for (Node node : getSensedNodes())
            locations.add(node.getLocation());
    }
    @Override
    public void onClock(){      // COMPUTE
        if(!locations.isEmpty())
            target = locations.get((new Random()).nextInt(locations.size()));
    }
    @Override
    public void onPostClock(){  // MOVE
        if(target == null)
            return;

        setDirection(target);
        move(Math.min(1, distance(target)));
    }
}