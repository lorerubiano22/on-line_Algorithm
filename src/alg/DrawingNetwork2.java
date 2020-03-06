package alg;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.imageio.ImageIO;


public class DrawingNetwork2 {
	static int width = 250;
	static int height = 250;

	public static void drawingNetwork(ArrayList<Edge> arrayList, Test aTest) {
		LinkedList<Edge> Network = new LinkedList<Edge>();
		int minX=0;
		int minY=0;
		int maxX=0;
		int maxY=0;

		for(Edge e:arrayList) {
			Network.add(e);
		}

		for (Edge e : Network) {
			if (minX > e.getOrigin().getX()) {// buscando el menor valor de X
				minX = (int) e.getOrigin().getX();
			}

			if (maxX < e.getOrigin().getX()) {// buscando el maximo valor de X
				maxX = (int) e.getOrigin().getX();
			}

			if (minY > e.getOrigin().getY()) { // buscando el min valor de Y
				minY = (int) e.getOrigin().getY();
			}
			if (maxY < e.getOrigin().getY()) { // buscando el maximo valor de Y
				maxY = (int) e.getOrigin().getY();
			}
		}

		width = maxX - minX;
		height = maxY - minY;
		drawingEdgesNodes(arrayList,aTest);
	}

	private static void drawingEdgesNodes(ArrayList<Edge> net, Test aTest) {
		// Constructs a BufferedImage of one of the predefined image types.
		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		// Create a graphics which can be used to draw into the buffered image
		Graphics2D g = bufferedImage.createGraphics();


		if(!aTest.getInstanceName().equals("p7.2.a") && !aTest.getInstanceName().equals("p6.2.a") && !aTest.getInstanceName().equals("p3.2.a") && !aTest.getInstanceName().equals("p4.2.a") && !aTest.getInstanceName().equals("p5.2.a")) {
			for (Edge e : net) {
				if (e.getOrigin().getId() == 0 && e.getOrigin().getTypeNode() > 1) {
					g.setColor(Color.BLACK);
					g.fillRect((int) e.getOrigin().getX() * 2 + 300, (int) e.getOrigin().getY() * 2 + 100, 3, 3);
					g.setColor(Color.BLACK);
					g.drawString("" + e.getOrigin().getId(), (int) e.getOrigin().getX() * 2 + 300,
							(int) e.getOrigin().getY() * 2 + 100 + 6);
					g.drawLine((int) e.getOrigin().getX() * 2 + 300, (int) e.getOrigin().getY() * 2 + 100,
							(int) e.getEnd().getX() * 2 + 300, (int) e.getEnd().getY() * 2 + 100);

				}

				else {
					if (e.getOrigin().getTypeNode() > 1 && e.getOrigin().getId() != 0) {
						g.setColor(Color.RED);
						g.fillOval((int) e.getOrigin().getX() * 2 + 300, (int) e.getOrigin().getY() * 2 + 100, 3, 3);
						g.setColor(Color.BLACK);
						g.drawString("" + e.getOrigin().getId(), (int) e.getOrigin().getX() * 2 + 300,
								(int) e.getOrigin().getY() * 2 + 100 + 6);
						g.setColor(Color.BLACK);
						g.drawLine((int) e.getOrigin().getX() * 2 + 300, (int) e.getOrigin().getY() * 2 + 100,
								(int) e.getEnd().getX() * 2 + 300, (int) e.getEnd().getY() * 2 + 100);
					} else {
						if (e.getOrigin().getTypeNode() == 1 && e.getOrigin().getId() != 0) {
							g.setColor(Color.YELLOW);
							g.fillOval((int) e.getOrigin().getX() * 2 + 300, (int) e.getOrigin().getY() * 2 + 100, 3, 3);
							g.setColor(Color.BLACK);
							g.drawString("" + e.getOrigin().getId(), (int) e.getOrigin().getX() * 2 + 300,
									(int) e.getOrigin().getY() * 2 + 100 + 6);
							g.setColor(Color.BLACK);
							g.drawLine((int) e.getOrigin().getX() * 2 + 300, (int) e.getOrigin().getY() * 2 + 100,
									(int) e.getEnd().getX() * 2 + 300, (int) e.getEnd().getY() * 2 + 100);

						} else {

							g.setColor(Color.BLACK);
							g.drawLine((int) e.getOrigin().getX() * 2 + 300, (int) e.getOrigin().getY() * 2 + 100,
									(int) e.getEnd().getX() * 2 + 300, (int) e.getEnd().getY() * 2 + 100);

						}
					}
				}
			}
			Edge last = net.get(net.size()-1);

			if (last.getOrigin().getId() == 0 && last.getOrigin().getTypeNode() > 1) {
				g.setColor(Color.BLACK);
				g.fillRect((int) last.getOrigin().getX() * 2 + 300, (int) last.getOrigin().getY() * 2 + 100, 3, 3);
				g.setColor(Color.BLACK);
				g.drawLine((int) last.getOrigin().getX() * 2 + 300, (int) last.getOrigin().getY() * 2 + 100,
						(int) last.getEnd().getX() * 2 + 300, (int) last.getEnd().getY() * 2 + 100);
				g.drawString("" + last.getOrigin().getId(), (int) last.getOrigin().getX() * 2 + 300,
						(int) last.getOrigin().getY() * 2 + 100 + 6);
			} else {
				if (last.getOrigin().getTypeNode() > 1 && last.getOrigin().getId() != 0) {
					g.setColor(Color.RED);
					g.fillOval((int) last.getOrigin().getX() * 2 + 300, (int) last.getOrigin().getY() * 2 + 100, 3, 3);
					g.setColor(Color.BLACK);
					g.drawString("" + last.getOrigin().getId(), (int) last.getOrigin().getX() * 2 + 300,
							(int) last.getOrigin().getY() * 2 + 100 + 6);
					g.setColor(Color.BLACK);
					g.drawLine((int) last.getOrigin().getX() * 2 + 300, (int) last.getOrigin().getY() * 2 + 100,
							(int) last.getEnd().getX() * 2 + 300, (int) last.getEnd().getY() * 2 + 100);
				} else {
					if (last.getOrigin().getTypeNode() == 1 && last.getOrigin().getId() != 0) {
						g.setColor(Color.YELLOW);
						g.fillOval((int) last.getOrigin().getX() * 2 + 300, (int) last.getOrigin().getY() * 2 + 100, 3, 3);
						g.setColor(Color.BLACK);
						g.drawString("" + last.getOrigin().getId(), (int) last.getOrigin().getX() * 2 + 300,
								(int) last.getOrigin().getY() * 2 + 100 + 6);
						g.setColor(Color.BLACK);
						g.drawLine((int) last.getOrigin().getX() * 2 + 300, (int) last.getOrigin().getY() * 2 + 100,
								(int) last.getEnd().getX() * 2 + 300, (int) last.getEnd().getY() * 2 + 100);
					} else {

						g.setColor(Color.BLACK);
						g.drawLine((int) last.getOrigin().getX() * 2 + 300, (int) last.getOrigin().getY() * 2 + 100,
								(int) last.getEnd().getX() * 2 + 300, (int) last.getEnd().getY() * 2 + 100);
					}

				}
			}
		}

		g.dispose();


		File file = new File("myimage.png");
		try {
			ImageIO.write(bufferedImage, "png", file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Save as JPEG
		file = new File("myimage.jpg");
		try {
			ImageIO.write(bufferedImage, "jpg", file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}




	}


}
