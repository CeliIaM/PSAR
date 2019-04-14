package classes_graphes;

import jbotsim.Topology;
import jbotsimx.ui.JViewer;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ST st=new ST(5);
		Topology tp=new Topology();
		st.afficher(tp);
		new JViewer(tp);
		tp.start();
	}

}
