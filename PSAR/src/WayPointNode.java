import jbotsim.Node;
import jbotsim.Point;

import java.util.LinkedList;
import java.util.Queue;

public class WayPointNode extends Node{
    Queue<Point> waypoint = new LinkedList<Point>();
    double step = 1;

    public void addPoint(Point p){
        waypoint.add(p);
    }
    public void setStep(double step){
        this.step = step;
    }
    public void onClock() {
        if (! waypoint.isEmpty()) {
            Point next = waypoint.peek();
            setDirection(next);
            if (distance(next) > step)
            move(step);
            else {
                move(distance(next));
                waypoint.poll();
            }
        }
    }
}