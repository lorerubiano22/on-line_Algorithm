package alg;

import java.util.Collections;
import java.util.LinkedList;

public class Manti_Instance {
	private static LinkedList<Node> interNodes= new LinkedList<Node> ();

	public static  LinkedList<Node> getIntermedianNodes(Node a, Node b) {
		Node origin = null;
		Node end= null;
		LinkedList<Node> nodes= new LinkedList<Node>();
		float X1=0;
		float Y1=0;
		float X2=0;
		float Y2=0;
		float X3=0;
		float Y3=0;
		float X4=0;
		float Y4=0;
		float X5=0;
		float Y5=0;
		boolean reverse=false;
		if(a.getId()>b.getId()) {
			origin = new Node(b.getId(),b.getX(),b.getY(),b.getProfit());
			end= new Node(a.getId(),a.getX(),a.getY(),a.getProfit());
			reverse=true;
		}
		else {
			origin = new Node(a.getId(),a.getX(),a.getY(),a.getProfit());
			end= new Node(b.getId(),b.getX(),b.getY(),b.getProfit());
		}
		/*Zero*/
		/* (0,16)*/
		if(origin.getId()==0 && end.getId()==16) {
			X1=	10.77227f;
			Y1=	-74.85629f;
			Node n= new Node(0,X1,Y1,0);
			nodes.add(n);
			X2=	10.75603f;
			Y2=-74.80567f;
			n= new Node(1,X2,Y2,0);
			nodes.add(n);
		}


		/* (0,2)*/
		if(origin.getId()==0 && end.getId()==2) {
			X1=	10.78061f;
			Y1=	-74.92267f;
			Node n= new Node(0,X1,Y1,0);
			nodes.add(n);
			X2=10.75871f;
			Y2=	-74.94232f;
			n= new Node(1,X2,Y2,0);
			nodes.add(n);
		}

		/* (0,6)*/
		if(origin.getId()==0 && end.getId()==6) {
			X1=10.79139f;
			Y1=-74.90326f;
			X2=10.72144f;
			Y2=-74.91234f;
			X3=10.6762f;
			Y3=-74.90515f;
			Node n= new Node(0,X1,Y1,0);
			nodes.add(n);
			n= new Node(1,X2,Y2,0);
			nodes.add(n);
			n= new Node(2,X3,Y3,0);
			nodes.add(n);
		}


		/* (0,16)*/
		if(origin.getId()==0 && end.getId()==16) {
			X1=10.77737f;
			Y1=-74.85659f;
			X2=10.75603f;
			Y2=-74.80567f;
			Node n= new Node(0,X1,Y1,0);
			nodes.add(n);
			n= new Node(1,X2,Y2,0);
			nodes.add(n);

		}
		/* (1,3)*/
		if(origin.getId()==1 && end.getId()==3) {
			X1=10.74831f;
			Y1=-74.75529f;
			Node n= new Node(0,X1,Y1,0);
			nodes.add(n);
		}

		/* (1,16)*/
		if(origin.getId()==1 && end.getId()==16) {
			X1=10.4479578f;
			Y1=-74.9570559f;
			Node n= new Node(0,X1,Y1,0);
			nodes.add(n);
		}

		/* (2,6)*/
		if(origin.getId()==2 && end.getId()==6) {
			X1=10.70851f;
			Y1=-74.95028f;
			X2=10.67685f;
			Y2=-74.93704F;
			Node n= new Node(0,X1,Y1,0);
			nodes.add(n);
			n= new Node(1,X2,Y2,0);
			nodes.add(n);

		}


		/* (2,7)*/
		if(origin.getId()==2 && end.getId()==7) {
			X1=10.71711f;
			Y1=-74.98189f;
			X2=10.6919f;
			Y2=-74.99184f;
			Node n= new Node(0,X1,Y1,0);
			nodes.add(n);
			n= new Node(1,X2,Y2,0);
			nodes.add(n);

		}



		/* (3,4)*/
		if(origin.getId()==3 && end.getId()==4) {
			X1=10.73152f;
			Y1=-74.75835f;
			Node n= new Node(0,X1,Y1,0);
			nodes.add(n);
		}

		/* (3,5)*/
		if(origin.getId()==3 && end.getId()==5) {
			X1=10.72254f;
			Y1=-74.75383f;
			X2=10.68944f;
			Y2=-74.74758f;
			X3=10.67814f;
			Y3=-74.74852f;
			X4=10.6579f;
			Y4=-74.76123f;
			Node n= new Node(0,X1,Y1,0);
			nodes.add(n);
			n= new Node(1,X2,Y2,0);
			nodes.add(n);
			n= new Node(2,X3,Y3,0);
			nodes.add(n);
			n= new Node(3,X4,Y4,0);
			nodes.add(n);
		}

		/* (4,6)*/
		if(origin.getId()==4 && end.getId()==6) {
			X1=10.71111f;
			Y1=-74.78449f;
			X2=10.67889f;
			Y2=-74.83058f;
			X3=10.6695f;
			Y3=-74.846f;
			X4=10.67178f;
			Y4=-74.86837f;
			X5=10.66183f;
			Y5=-74.89503f;

			Node n= new Node(0,X1,Y1,0);
			nodes.add(n);
			n= new Node(1,X2,Y2,0);
			nodes.add(n);
			n= new Node(2,X3,Y3,0);
			nodes.add(n);
			n= new Node(3,X4,Y4,0);
			nodes.add(n);
			n= new Node(4,X5,Y5,0);
			nodes.add(n);
		}


		/* (4,16)*/
		if(origin.getId()==4 && end.getId()==16) {
			X1=10.75491f;
			Y1=-74.76518f;
			X2=10.74513f;
			Y2=-74.76777f;
			X3=10.73487f;
			Y3=-74.7652f;
			Node n= new Node(0,X1,Y1,0);
			nodes.add(n);
			n= new Node(1,X2,Y2,0);
			nodes.add(n);
			n= new Node(2,X3,Y3,0);
			nodes.add(n);
		}



		/* (5,10)*/
		if(origin.getId()==5 && end.getId()==10) {
			X1=10.61532f;
			Y1=-74.76803f;
			X2=	10.5782f;
			Y2=-74.78725f;
			X3=10.53236f;
			Y3=-74.81234f;

			Node n= new Node(0,X1,Y1,0);
			nodes.add(n);
			n= new Node(1,X2,Y2,0);
			nodes.add(n);
			n= new Node(2,X3,Y3,0);
			nodes.add(n);
		}

		/* (5,18) ok*/
		if(origin.getId()==5 && end.getId()==18) {
			X1=10.63367f;
			Y1=-74.76548f;
			Node n= new Node(0,X1,Y1,0);
			nodes.add(n);
		}

		/* (6,7)*/
		if(origin.getId()==6 && end.getId()==7) {
			X1=10.64f;
			Y1=-74.95688f;
			X2=10.65273f;
			Y2=-74.99293f;
			Node n= new Node(0,X1,Y1,0);
			nodes.add(n);
			n= new Node(1,X2,Y2,0);
			nodes.add(n);

		}

		/* (6,8)*/
		if(origin.getId()==6 && end.getId()==8) {
			X1=10.61726f;
			Y1=-74.88439f;
			Node n= new Node(0,X1,Y1,0);
			nodes.add(n);
		}

		/* (6,13)*/
		if(origin.getId()==6 && end.getId()==13) {
			X1=10.60734f;
			Y1=-74.92755f;
			X2=10.56915f;
			Y2=-74.95714f;
			X3=10.50856f;
			Y3=-74.94218f;
			Node n= new Node(0,X1,Y1,0);
			nodes.add(n);
			n= new Node(1,X2,Y2,0);
			nodes.add(n);
			n= new Node(2,X3,Y3,0);
			nodes.add(n);
					}

		/* (7,17) ok*/
		if(origin.getId()==7 && end.getId()==17) {
			X1=10.65755f;
			Y1=-75.04541f;
			X2=10.60474f;
			Y2=-75.11699f;
			X3=10.53324f;
			Y3=-75.07249f;
			Node n= new Node(0,X1,Y1,0);
			nodes.add(n);
			n= new Node(1,X2,Y2,0);
			nodes.add(n);
			n= new Node(2,X3,Y3,0);
			nodes.add(n);
		}


		/* (8,9)*/
		if(origin.getId()==8 && end.getId()==9) {
			X1=10.56354f;
			Y1=-74.85714f;
			Node n= new Node(0,X1,Y1,0);
			nodes.add(n);
		}

		/* (8,18) ok*/
		if(origin.getId()==8 && end.getId()==18) {
			X1=10.62174f;
			Y1=-74.83708f;
			X2=10.62992f;
			Y2=-74.81395f;
			Node n= new Node(0,X1,Y1,0);
			nodes.add(n);
			n= new Node(1,X2,Y2,0);
			nodes.add(n);

		}


		/* (9,10)*/
		if(origin.getId()==9 && end.getId()==10) {
			X1=10.52034f;
			Y1=-74.84821f;
			Node n= new Node(0,X1,Y1,0);
			nodes.add(n);
		}


		/* (9,11)*/
		if(origin.getId()==9 && end.getId()==11) {
			X1=10.51491f;
			Y1=-74.87713f;
			X2=10.47706f;
			Y2=-74.87948f;
			Node n= new Node(0,X1,Y1,0);
			nodes.add(n);
			n= new Node(1,X2,Y2,0);
			nodes.add(n);
		}

		/* (10,12)*/
		if(origin.getId()==10 && end.getId()==12) {
			X1=10.49145f;
			Y1=-74.82487f;
			X2=10.47605f;
			Y2=-74.82494f;
			X3=10.45858f;
			Y3=-74.83404f;
			Node n= new Node(0,X1,Y1,0);
			nodes.add(n);
			n= new Node(1,X2,Y2,0);
			nodes.add(n);
			n= new Node(2,X3,Y3,0);
			nodes.add(n);
		}


		/* (11,12)*/
		if(origin.getId()==11 && end.getId()==12) {
			X1=10.44844f;
			Y1=-74.86417f;
			X2=10.44503f;
			Y2=-74.85006f;
			Node n= new Node(0,X1,Y1,0);
			nodes.add(n);
			n= new Node(1,X2,Y2,0);
			nodes.add(n);
		}

		/* (11,19) ok*/
		if(origin.getId()==11 && end.getId()==19) {
			X1=10.44637f;
			Y1=-74.88896f;
			X2=10.4398f;
			Y2=-74.90656f;
			Node n= new Node(0,X1,Y1,0);
			nodes.add(n);
			n= new Node(1,X2,Y2,0);
			nodes.add(n);
		}

		/* (12,21) ok*/
		if(origin.getId()==12 && end.getId()==21) {
			X1=10.41651f;
			Y1=-74.83824f;
			X2=10.40708f;
			Y2=-74.85441f;
			X3=10.37885f;
			Y3=-74.87032f;
			Node n= new Node(0,X1,Y1,0);
			nodes.add(n);
			n= new Node(1,X2,Y2,0);
			nodes.add(n);
			n= new Node(2,X3,Y3,0);
			nodes.add(n);
		}

		/* (13,19) ok*/
		if(origin.getId()==13 && end.getId()==19) {
			X1=10.45366f;
			Y1=-74.95225f;
			X2=10.44934f;
			Y2=-74.93494f;
			Node n= new Node(0,X1,Y1,0);
			nodes.add(n);
			n= new Node(1,X2,Y2,0);
			nodes.add(n);
		}

		/* (13,14)*/
		if(origin.getId()==13 && end.getId()==14) {
			X1=10.45787f;
			Y1=-74.99571f;
			X2=10.46461f;
			Y2=-75.04396f;
			Node n= new Node(0,X1,Y1,0);
			nodes.add(n);
			n= new Node(1,X2,Y2,0);
			nodes.add(n);
		}

		/* (14,17) ok*/
		if(origin.getId()==14 && end.getId()==17) {
			X1=10.41258f;
			Y1=-75.12824f;
			X2=10.46015f;
			Y2=-75.13407f;
			Node n= new Node(0,X1,Y1,0);
			nodes.add(n);
			n= new Node(1,X2,Y2,0);
			nodes.add(n);
		}

		/* (14,20) ok*/
		if(origin.getId()==14 && end.getId()==20) {
			X1=10.3775f;
			Y1=-75.04876f;
			X2=10.37029f;
			Y2=-75.03287f;
			Node n= new Node(0,X1,Y1,0);
			nodes.add(n);
			n= new Node(1,X2,Y2,0);
			nodes.add(n);
		}

		/* (15,20) ok*/
		if(origin.getId()==15 && end.getId()==20) {
			X1=10.34422f;
			Y1=-74.98472f;
			Node n= new Node(0,X1,Y1,0);
			nodes.add(n);
		}

		/* (15,21) ok*/
		if(origin.getId()==15 && end.getId()==21) {
			X1=10.33899f;
			Y1=-74.94854f;
			X2=10.35865f;
			Y2=-74.93557f;
			X3=10.37484f;
			Y3=-74.89918f;

			Node n= new Node(0,X1,Y1,0);
			nodes.add(n);
			n= new Node(1,X2,Y2,0);
			nodes.add(n);
			n= new Node(2,X3,Y3,0);
			nodes.add(n);
		}


		if(reverse) {
			Collections.reverse(nodes);
		}

		for(Node nn:nodes) {
			interNodes.add(nn);
		}
		return nodes;
	}

}
