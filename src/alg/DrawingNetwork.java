package alg;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class DrawingNetwork extends JPanel {
	// public UpdateRoadInformation revealedRoadInformation;
	LinkedList<Edge> Network = new LinkedList<Edge>();
	private BufferedImage image;
	public int minX = Integer.MAX_VALUE;
	public int maxX = Integer.MIN_VALUE;
	public int minY = Integer.MAX_VALUE;
	public int maxY = Integer.MIN_VALUE;
	public int x ;
	public int y ;
	Test aTest;



	public DrawingNetwork(ArrayList<Edge> arrayList, Test t) {
		aTest=t;
		for(Edge e:arrayList) {
			Network.add(e);
		}

		JFrame f = new JFrame("Road_Network");

		f.add(this);

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

		int wScreen = maxX - minX;
		int hScreen = maxY - minY;

		f.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		f.setSize((wScreen), (hScreen));
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int w = f.getSize().width;
		int h = f.getSize().height;
		x = (dim.width - w) / 2;
		y = (dim.height - h) / 2;

		BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = img.createGraphics();
		g2d.drawImage(img,x, y, w, h, this);
		f.setLocation(x, y);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.getContentPane();
		f.pack();
		f.setVisible(true);


	}

	@Override
	public void paintComponent(Graphics g2) {

		Graphics2D g = (Graphics2D) g2.create();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		super.paintComponent(g);
		// super.paintComponent(g);
		this.setBackground(Color.WHITE);
		if(aTest.getInstanceName().equals("p7.2.a") ) {
			for (Edge e : Network) {
				if (e.getOrigin().getId() == 0 && e.getOrigin().getTypeNode() > 1) {
					g.setColor(Color.BLACK);
					g.fillRect((int) e.getOrigin().getX() * 5 + 300, (int) e.getOrigin().getY() * 5 + 170, 3, 3);
					g.setColor(Color.BLACK);
					//// g.drawString(""+e.getOrigin().getId(), (int) e.getOrigin().getX()*2+300,
					//// (int) e.getOrigin().getY()*2+100+6);
					// g.drawString(""+e.getOrigin().getId(), (int) e.getOrigin().getX()*2+700,
					//// (int) e.getOrigin().getY()*2+100+6);
					g.drawString("" + e.getOrigin().getId(), (int) e.getOrigin().getX() * 5 + 300,
							(int) e.getOrigin().getY() * 5 + 170 + 6);
					g.drawLine((int) e.getOrigin().getX() * 5 + 300, (int) e.getOrigin().getY() * 5 + 170,
							(int) e.getEnd().getX() * 5 + 300, (int) e.getEnd().getY() * 5 + 170);

				}

				else {
					if (e.getOrigin().getTypeNode() > 1 && e.getOrigin().getId() != 0) {
						g.setColor(Color.RED);
						g.fillOval((int) e.getOrigin().getX() * 5 + 300, (int) e.getOrigin().getY() * 5 + 170, 3, 3);
						g.setColor(Color.BLACK);
						//// g.drawString(""+e.getOrigin().getId(), (int) e.getOrigin().getX()*2+300,
						//// (int) e.getOrigin().getY()*2+100+6);
						// g.drawString(""+e.getOrigin().getId(), (int) e.getOrigin().getX()*2+700,
						//// (int) e.getOrigin().getY()*2+100+6);
						g.drawString("" + e.getOrigin().getId(), (int) e.getOrigin().getX() * 5 + 300,
								(int) e.getOrigin().getY() * 5 + 170 + 6);
						g.setColor(Color.BLACK);
						g.drawLine((int) e.getOrigin().getX() * 5 + 300, (int) e.getOrigin().getY() * 5 + 170,
								(int) e.getEnd().getX() * 5 + 300, (int) e.getEnd().getY() * 5 + 170);
					} else {
						if (e.getOrigin().getTypeNode() == 1 && e.getOrigin().getId() != 0) {
							g.setColor(Color.YELLOW);
							g.fillOval((int) e.getOrigin().getX() * 5 + 300, (int) e.getOrigin().getY() * 5 + 170, 3, 3);
							g.setColor(Color.BLACK);
							//// g.drawString(""+e.getOrigin().getId(), (int) e.getOrigin().getX()*2+300,
							//// (int) e.getOrigin().getY()*2+100+6);
							// g.drawString(""+e.getOrigin().getId(), (int) e.getOrigin().getX()*2+700,
							//// (int) e.getOrigin().getY()*2+100+6);
							g.drawString("" + e.getOrigin().getId(), (int) e.getOrigin().getX() * 5 + 300,
									(int) e.getOrigin().getY() * 5 + 170 + 6);
							g.setColor(Color.BLACK);
							g.drawLine((int) e.getOrigin().getX() * 5 + 300, (int) e.getOrigin().getY() * 5 + 170,
									(int) e.getEnd().getX() * 5 + 300, (int) e.getEnd().getY() * 5 + 170);

						} else {
							// g.setColor(Color.PINK);
							// g.fillOval((int) e.getOrigin().getX(), (int) e.getOrigin().getY(), 4,4);
							g.setColor(Color.BLACK);
							g.drawLine((int) e.getOrigin().getX() * 5 + 300, (int) e.getOrigin().getY() * 5 + 170,
									(int) e.getEnd().getX() * 5 + 300, (int) e.getEnd().getY() * 5 + 170);

						}
					}
				}
			}
			Edge last = Network.getLast();

			if (last.getOrigin().getId() == 0 && last.getOrigin().getTypeNode() > 1) {
				g.setColor(Color.BLACK);
				g.fillRect((int) last.getOrigin().getX() * 5 + 300, (int) last.getOrigin().getY() * 5 + 170, 3, 3);
				g.setColor(Color.BLACK);
				g.drawLine((int) last.getOrigin().getX() * 5 + 300, (int) last.getOrigin().getY() * 5 + 170,
						(int) last.getEnd().getX() * 5 + 300, (int) last.getEnd().getY() * 5 + 170);
				g.drawString("" + last.getOrigin().getId(), (int) last.getOrigin().getX() * 5 + 300,
						(int) last.getOrigin().getY() * 5 + 170 + 6);
			} else {
				if (last.getOrigin().getTypeNode() > 1 && last.getOrigin().getId() != 0) {
					g.setColor(Color.RED);
					g.fillOval((int) last.getOrigin().getX() * 5 + 300, (int) last.getOrigin().getY() * 5 + 170, 3, 3);
					g.setColor(Color.BLACK);
					//// g.drawString(""+last.getOrigin().getId(), (int)
					//// last.getOrigin().getX()*2+300, (int) last.getOrigin().getY()*2+100+6);
					// g.drawString(""+last.getOrigin().getId(), (int)
					//// last.getOrigin().getX()*2+700, (int) last.getOrigin().getY()*2+100+6);
					g.drawString("" + last.getOrigin().getId(), (int) last.getOrigin().getX() * 5 + 300,
							(int) last.getOrigin().getY() * 5 + 170 + 6);
					g.setColor(Color.BLACK);
					g.drawLine((int) last.getOrigin().getX() * 5 + 300, (int) last.getOrigin().getY() * 5 + 170,
							(int) last.getEnd().getX() * 5 + 300, (int) last.getEnd().getY() * 5 + 170);
				} else {
					if (last.getOrigin().getTypeNode() == 1 && last.getOrigin().getId() != 0) {
						g.setColor(Color.YELLOW);
						g.fillOval((int) last.getOrigin().getX() * 5 + 300, (int) last.getOrigin().getY() * 5 + 170, 3, 3);
						g.setColor(Color.BLACK);
						//// g.drawString(""+last.getOrigin().getId(), (int)
						//// last.getOrigin().getX()*2+300, (int) last.getOrigin().getY()*2+100+6);
						// g.drawString(""+last.getOrigin().getId(), (int)
						//// last.getOrigin().getX()*2+700, (int) last.getOrigin().getY()*2+100+6);
						g.drawString("" + last.getOrigin().getId(), (int) last.getOrigin().getX() * 5 + 300,
								(int) last.getOrigin().getY() * 5 + 170 + 6);
						g.setColor(Color.BLACK);
						g.drawLine((int) last.getOrigin().getX() * 5 + 300, (int) last.getOrigin().getY() * 5 + 170,
								(int) last.getEnd().getX() * 5 + 300, (int) last.getEnd().getY() * 5 + 170);
					} else {
						// g.setColor(Color.PINK);
						// g.fillOval((int) last.getOrigin().getX(), (int) last.getOrigin().getY(),
						// 4,4);
						g.setColor(Color.BLACK);
						g.drawLine((int) last.getOrigin().getX() * 5 + 300, (int) last.getOrigin().getY() * 5 + 170,
								(int) last.getEnd().getX() * 5 + 300, (int) last.getEnd().getY() * 5 + 170);

					}

				}
			}
		}





		if(aTest.getInstanceName().equals("p5.2.a") ) {
			for (Edge e : Network) {
				if (e.getOrigin().getId() == 0 && e.getOrigin().getTypeNode() > 1) {
					g.setColor(Color.BLACK);
					g.fillRect((int) e.getOrigin().getX() * 2 + 300, (int) e.getOrigin().getY() * 2 + 170, 3, 3);
					g.setColor(Color.BLACK);
					//// g.drawString(""+e.getOrigin().getId(), (int) e.getOrigin().getX()*2+300,
					//// (int) e.getOrigin().getY()*2+100+6);
					// g.drawString(""+e.getOrigin().getId(), (int) e.getOrigin().getX()*2+700,
					//// (int) e.getOrigin().getY()*2+100+6);
					g.drawString("" + e.getOrigin().getId(), (int) e.getOrigin().getX() * 2 + 300,
							(int) e.getOrigin().getY() * 2 + 170 + 6);
					g.drawLine((int) e.getOrigin().getX() * 2 + 300, (int) e.getOrigin().getY() * 2 + 170,
							(int) e.getEnd().getX() * 2 + 300, (int) e.getEnd().getY() * 2 + 170);

				}

				else {
					if (e.getOrigin().getTypeNode() > 1 && e.getOrigin().getId() != 0) {
						g.setColor(Color.RED);
						g.fillOval((int) e.getOrigin().getX() * 2 + 300, (int) e.getOrigin().getY() * 2 + 170, 3, 3);
						g.setColor(Color.BLACK);
						//// g.drawString(""+e.getOrigin().getId(), (int) e.getOrigin().getX()*2+300,
						//// (int) e.getOrigin().getY()*2+100+6);
						// g.drawString(""+e.getOrigin().getId(), (int) e.getOrigin().getX()*2+700,
						//// (int) e.getOrigin().getY()*2+100+6);
						g.drawString("" + e.getOrigin().getId(), (int) e.getOrigin().getX() * 2 + 300,
								(int) e.getOrigin().getY() * 2 + 170 + 6);
						g.setColor(Color.BLACK);
						g.drawLine((int) e.getOrigin().getX() * 2 + 300, (int) e.getOrigin().getY() * 2 + 170,
								(int) e.getEnd().getX() * 2 + 300, (int) e.getEnd().getY() * 2 + 170);
					} else {
						if (e.getOrigin().getTypeNode() == 1 && e.getOrigin().getId() != 0) {
							g.setColor(Color.YELLOW);
							g.fillOval((int) e.getOrigin().getX() * 2 + 300, (int) e.getOrigin().getY() * 2 + 170, 3, 3);
							g.setColor(Color.BLACK);
							//// g.drawString(""+e.getOrigin().getId(), (int) e.getOrigin().getX()*2+300,
							//// (int) e.getOrigin().getY()*2+100+6);
							// g.drawString(""+e.getOrigin().getId(), (int) e.getOrigin().getX()*2+700,
							//// (int) e.getOrigin().getY()*2+100+6);
							g.drawString("" + e.getOrigin().getId(), (int) e.getOrigin().getX() * 2 + 300,
									(int) e.getOrigin().getY() * 2 + 170 + 6);
							g.setColor(Color.BLACK);
							g.drawLine((int) e.getOrigin().getX() * 2 + 300, (int) e.getOrigin().getY() * 2 + 170,
									(int) e.getEnd().getX() * 2 + 300, (int) e.getEnd().getY() * 2 + 170);

						} else {
							// g.setColor(Color.PINK);
							// g.fillOval((int) e.getOrigin().getX(), (int) e.getOrigin().getY(), 4,4);
							g.setColor(Color.BLACK);
							g.drawLine((int) e.getOrigin().getX() * 2 + 300, (int) e.getOrigin().getY() * 2 + 170,
									(int) e.getEnd().getX() * 2 + 300, (int) e.getEnd().getY() * 2 + 170);

						}
					}
				}
			}
			Edge last = Network.getLast();

			if (last.getOrigin().getId() == 0 && last.getOrigin().getTypeNode() > 1) {
				g.setColor(Color.BLACK);
				g.fillRect((int) last.getOrigin().getX() * 2 + 300, (int) last.getOrigin().getY() * 2 + 170, 3, 3);
				g.setColor(Color.BLACK);
				g.drawLine((int) last.getOrigin().getX() * 2 + 300, (int) last.getOrigin().getY() * 2 + 170,
						(int) last.getEnd().getX() * 2 + 300, (int) last.getEnd().getY() * 2 + 170);
				g.drawString("" + last.getOrigin().getId(), (int) last.getOrigin().getX() * 2 + 300,
						(int) last.getOrigin().getY() * 2 + 170 + 6);
			} else {
				if (last.getOrigin().getTypeNode() > 1 && last.getOrigin().getId() != 0) {
					g.setColor(Color.RED);
					g.fillOval((int) last.getOrigin().getX() * 2 + 300, (int) last.getOrigin().getY() * 2 + 170, 3, 3);
					g.setColor(Color.BLACK);
					//// g.drawString(""+last.getOrigin().getId(), (int)
					//// last.getOrigin().getX()*2+300, (int) last.getOrigin().getY()*2+100+6);
					// g.drawString(""+last.getOrigin().getId(), (int)
					//// last.getOrigin().getX()*2+700, (int) last.getOrigin().getY()*2+100+6);
					g.drawString("" + last.getOrigin().getId(), (int) last.getOrigin().getX() * 2 + 300,
							(int) last.getOrigin().getY() * 2 + 170 + 6);
					g.setColor(Color.BLACK);
					g.drawLine((int) last.getOrigin().getX() * 2 + 300, (int) last.getOrigin().getY() * 2 + 170,
							(int) last.getEnd().getX() * 2 + 300, (int) last.getEnd().getY() * 2 + 170);
				} else {
					if (last.getOrigin().getTypeNode() == 1 && last.getOrigin().getId() != 0) {
						g.setColor(Color.YELLOW);
						g.fillOval((int) last.getOrigin().getX() * 2 + 300, (int) last.getOrigin().getY() * 2 + 170, 3, 3);
						g.setColor(Color.BLACK);
						//// g.drawString(""+last.getOrigin().getId(), (int)
						//// last.getOrigin().getX()*2+300, (int) last.getOrigin().getY()*2+100+6);
						// g.drawString(""+last.getOrigin().getId(), (int)
						//// last.getOrigin().getX()*2+700, (int) last.getOrigin().getY()*2+100+6);
						g.drawString("" + last.getOrigin().getId(), (int) last.getOrigin().getX() * 2 + 300,
								(int) last.getOrigin().getY() * 2 + 170 + 6);
						g.setColor(Color.BLACK);
						g.drawLine((int) last.getOrigin().getX() * 2 + 300, (int) last.getOrigin().getY() * 2 + 170,
								(int) last.getEnd().getX() * 2 + 300, (int) last.getEnd().getY() * 2 + 170);
					} else {
						// g.setColor(Color.PINK);
						// g.fillOval((int) last.getOrigin().getX(), (int) last.getOrigin().getY(),
						// 4,4);
						g.setColor(Color.BLACK);
						g.drawLine((int) last.getOrigin().getX() * 2 + 300, (int) last.getOrigin().getY() * 2 + 170,
								(int) last.getEnd().getX() * 2 + 300, (int) last.getEnd().getY() * 2 + 170);

					}

				}
			}
		}

		if(!aTest.getInstanceName().equals("p7.2.a") && !aTest.getInstanceName().equals("p6.2.a") && !aTest.getInstanceName().equals("p3.2.a") && !aTest.getInstanceName().equals("p4.2.a") && !aTest.getInstanceName().equals("p5.2.a")) {
			for (Edge e : Network) {
				if (e.getOrigin().getId() == 0 && e.getOrigin().getTypeNode() > 1) {
					g.setColor(Color.BLACK);
					g.fillRect((int) e.getOrigin().getX() * 2 + 300, (int) e.getOrigin().getY() * 2 + 100, 3, 3);
					g.setColor(Color.BLACK);
					//// g.drawString(""+e.getOrigin().getId(), (int) e.getOrigin().getX()*2+300,
					//// (int) e.getOrigin().getY()*2+100+6);
					// g.drawString(""+e.getOrigin().getId(), (int) e.getOrigin().getX()*2+700,
					//// (int) e.getOrigin().getY()*2+100+6);
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
						//// g.drawString(""+e.getOrigin().getId(), (int) e.getOrigin().getX()*2+300,
						//// (int) e.getOrigin().getY()*2+100+6);
						// g.drawString(""+e.getOrigin().getId(), (int) e.getOrigin().getX()*2+700,
						//// (int) e.getOrigin().getY()*2+100+6);
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
							//// g.drawString(""+e.getOrigin().getId(), (int) e.getOrigin().getX()*2+300,
							//// (int) e.getOrigin().getY()*2+100+6);
							// g.drawString(""+e.getOrigin().getId(), (int) e.getOrigin().getX()*2+700,
							//// (int) e.getOrigin().getY()*2+100+6);
							g.drawString("" + e.getOrigin().getId(), (int) e.getOrigin().getX() * 2 + 300,
									(int) e.getOrigin().getY() * 2 + 100 + 6);
							g.setColor(Color.BLACK);
							g.drawLine((int) e.getOrigin().getX() * 2 + 300, (int) e.getOrigin().getY() * 2 + 100,
									(int) e.getEnd().getX() * 2 + 300, (int) e.getEnd().getY() * 2 + 100);

						} else {
							// g.setColor(Color.PINK);
							// g.fillOval((int) e.getOrigin().getX(), (int) e.getOrigin().getY(), 4,4);
							g.setColor(Color.BLACK);
							g.drawLine((int) e.getOrigin().getX() * 2 + 300, (int) e.getOrigin().getY() * 2 + 100,
									(int) e.getEnd().getX() * 2 + 300, (int) e.getEnd().getY() * 2 + 100);

						}
					}
				}
			}
			Edge last = Network.getLast();

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
					//// g.drawString(""+last.getOrigin().getId(), (int)
					//// last.getOrigin().getX()*2+300, (int) last.getOrigin().getY()*2+100+6);
					// g.drawString(""+last.getOrigin().getId(), (int)
					//// last.getOrigin().getX()*2+700, (int) last.getOrigin().getY()*2+100+6);
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
						//// g.drawString(""+last.getOrigin().getId(), (int)
						//// last.getOrigin().getX()*2+300, (int) last.getOrigin().getY()*2+100+6);
						// g.drawString(""+last.getOrigin().getId(), (int)
						//// last.getOrigin().getX()*2+700, (int) last.getOrigin().getY()*2+100+6);
						g.drawString("" + last.getOrigin().getId(), (int) last.getOrigin().getX() * 2 + 300,
								(int) last.getOrigin().getY() * 2 + 100 + 6);
						g.setColor(Color.BLACK);
						g.drawLine((int) last.getOrigin().getX() * 2 + 300, (int) last.getOrigin().getY() * 2 + 100,
								(int) last.getEnd().getX() * 2 + 300, (int) last.getEnd().getY() * 2 + 100);
					} else {
						// g.setColor(Color.PINK);
						// g.fillOval((int) last.getOrigin().getX(), (int) last.getOrigin().getY(),
						// 4,4);
						g.setColor(Color.BLACK);
						g.drawLine((int) last.getOrigin().getX() * 2 + 300, (int) last.getOrigin().getY() * 2 + 100,
								(int) last.getEnd().getX() * 2 + 300, (int) last.getEnd().getY() * 2 + 100);

					}

				}
			}
		}
		if(aTest.getInstanceName().equals("p6.2.a")) {
			for (Edge e : Network) {
				if (e.getOrigin().getId() == 0 && e.getOrigin().getTypeNode() > 1) {
					g.setColor(Color.BLACK);
					g.fillRect((int) e.getOrigin().getX() * 2 + 300, (int) e.getOrigin().getY() * 2 +170, 3, 3);
					g.setColor(Color.BLACK);
					//// g.drawString(""+e.getOrigin().getId(), (int) e.getOrigin().getX()*2+300,
					//// (int) e.getOrigin().getY()*2+100+6);
					// g.drawString(""+e.getOrigin().getId(), (int) e.getOrigin().getX()*2+700,
					//// (int) e.getOrigin().getY()*2+100+6);
					g.drawString("" + e.getOrigin().getId(), (int) e.getOrigin().getX() * 2 + 300,
							(int) e.getOrigin().getY() * 2 +170 + 6);
					g.drawLine((int) e.getOrigin().getX() * 2 + 300, (int) e.getOrigin().getY() * 2 +170,
							(int) e.getEnd().getX() * 2 + 300, (int) e.getEnd().getY() * 2 +170);

				}

				else {
					if (e.getOrigin().getTypeNode() > 1 && e.getOrigin().getId() != 0) {
						g.setColor(Color.RED);
						g.fillOval((int) e.getOrigin().getX() * 2 + 300, (int) e.getOrigin().getY() * 2 +170, 3, 3);
						g.setColor(Color.BLACK);
						//// g.drawString(""+e.getOrigin().getId(), (int) e.getOrigin().getX()*2+300,
						//// (int) e.getOrigin().getY()*2+100+6);
						// g.drawString(""+e.getOrigin().getId(), (int) e.getOrigin().getX()*2+700,
						//// (int) e.getOrigin().getY()*2+100+6);
						g.drawString("" + e.getOrigin().getId(), (int) e.getOrigin().getX() * 2 + 300,
								(int) e.getOrigin().getY() * 2 +170 + 6);
						g.setColor(Color.BLACK);
						g.drawLine((int) e.getOrigin().getX() * 2 + 300, (int) e.getOrigin().getY() * 2 +170,
								(int) e.getEnd().getX() * 2 + 300, (int) e.getEnd().getY() * 2 +170);
					} else {
						if (e.getOrigin().getTypeNode() == 1 && e.getOrigin().getId() != 0) {
							g.setColor(Color.YELLOW);
							g.fillOval((int) e.getOrigin().getX() * 2 + 300, (int) e.getOrigin().getY() * 2 +170, 3, 3);
							g.setColor(Color.BLACK);
							//// g.drawString(""+e.getOrigin().getId(), (int) e.getOrigin().getX()*2+300,
							//// (int) e.getOrigin().getY()*2+100+6);
							// g.drawString(""+e.getOrigin().getId(), (int) e.getOrigin().getX()*2+700,
							//// (int) e.getOrigin().getY()*2+100+6);
							g.drawString("" + e.getOrigin().getId(), (int) e.getOrigin().getX() * 2 + 300,
									(int) e.getOrigin().getY() * 2 +170 + 6);
							g.setColor(Color.BLACK);
							g.drawLine((int) e.getOrigin().getX() * 2 + 300, (int) e.getOrigin().getY() * 2 +170,
									(int) e.getEnd().getX() * 2 + 300, (int) e.getEnd().getY() * 2 +170);

						} else {
							// g.setColor(Color.PINK);
							// g.fillOval((int) e.getOrigin().getX(), (int) e.getOrigin().getY(), 4,4);
							g.setColor(Color.BLACK);
							g.drawLine((int) e.getOrigin().getX() * 2 + 300, (int) e.getOrigin().getY() * 2 +170,
									(int) e.getEnd().getX() * 2 + 300, (int) e.getEnd().getY() * 2 +170);

						}
					}
				}
			}
			Edge last = Network.getLast();

			if (last.getOrigin().getId() == 0 && last.getOrigin().getTypeNode() > 1) {
				g.setColor(Color.BLACK);
				g.fillRect((int) last.getOrigin().getX() * 2 + 300, (int) last.getOrigin().getY() * 2 +170, 3, 3);
				g.setColor(Color.BLACK);
				g.drawLine((int) last.getOrigin().getX() * 2 + 300, (int) last.getOrigin().getY() * 2 +170,
						(int) last.getEnd().getX() * 2 + 300, (int) last.getEnd().getY() * 2 +170);
				g.drawString("" + last.getOrigin().getId(), (int) last.getOrigin().getX() * 2 + 300,
						(int) last.getOrigin().getY() * 2 +170 + 6);
			} else {
				if (last.getOrigin().getTypeNode() > 1 && last.getOrigin().getId() != 0) {
					g.setColor(Color.RED);
					g.fillOval((int) last.getOrigin().getX() * 2 + 300, (int) last.getOrigin().getY() * 2 +170, 3, 3);
					g.setColor(Color.BLACK);
					//// g.drawString(""+last.getOrigin().getId(), (int)
					//// last.getOrigin().getX()*2+300, (int) last.getOrigin().getY()*2+100+6);
					// g.drawString(""+last.getOrigin().getId(), (int)
					//// last.getOrigin().getX()*2+700, (int) last.getOrigin().getY()*2+100+6);
					g.drawString("" + last.getOrigin().getId(), (int) last.getOrigin().getX() * 2 + 300,
							(int) last.getOrigin().getY() * 2 +170 + 6);
					g.setColor(Color.BLACK);
					g.drawLine((int) last.getOrigin().getX() * 2 + 300, (int) last.getOrigin().getY() * 2 +170,
							(int) last.getEnd().getX() * 2 + 300, (int) last.getEnd().getY() * 2 +170);
				} else {
					if (last.getOrigin().getTypeNode() == 1 && last.getOrigin().getId() != 0) {
						g.setColor(Color.YELLOW);
						g.fillOval((int) last.getOrigin().getX() * 2 + 300, (int) last.getOrigin().getY() * 2 +170, 3, 3);
						g.setColor(Color.BLACK);
						//// g.drawString(""+last.getOrigin().getId(), (int)
						//// last.getOrigin().getX()*2+300, (int) last.getOrigin().getY()*2+100+6);
						// g.drawString(""+last.getOrigin().getId(), (int)
						//// last.getOrigin().getX()*2+700, (int) last.getOrigin().getY()*2+100+6);
						g.drawString("" + last.getOrigin().getId(), (int) last.getOrigin().getX() * 2 + 300,
								(int) last.getOrigin().getY() * 2 +170 + 6);
						g.setColor(Color.BLACK);
						g.drawLine((int) last.getOrigin().getX() * 2 + 300, (int) last.getOrigin().getY() * 2 +170,
								(int) last.getEnd().getX() * 2 + 300, (int) last.getEnd().getY() * 2 +170);
					} else {
						// g.setColor(Color.PINK);
						// g.fillOval((int) last.getOrigin().getX(), (int) last.getOrigin().getY(),
						// 4,4);
						g.setColor(Color.BLACK);
						g.drawLine((int) last.getOrigin().getX() * 2 + 300, (int) last.getOrigin().getY() * 2 +170,
								(int) last.getEnd().getX() * 2 + 300, (int) last.getEnd().getY() * 2 +170);

					}

				}
			}
		}

		/////

		if(aTest.getInstanceName().equals("p3.2.a")) {

			for (Edge e : Network) {
				if (e.getOrigin().getId() == 0 && e.getOrigin().getTypeNode() > 1) {
					g.setColor(Color.BLACK);
					g.fillRect((int) e.getOrigin().getX() * 2 + 300, (int) e.getOrigin().getY() * 2 -100, 3, 3);
					g.setColor(Color.BLACK);
					//// g.drawString(""+e.getOrigin().getId(), (int) e.getOrigin().getX()*2+300,
					//// (int) e.getOrigin().getY()*2+100+6);
					// g.drawString(""+e.getOrigin().getId(), (int) e.getOrigin().getX()*2+700,
					//// (int) e.getOrigin().getY()*2+100+6);
					g.drawString("" + e.getOrigin().getId(), (int) e.getOrigin().getX() * 2 + 300,
							(int) e.getOrigin().getY() * 2 -100 + 6);
					g.drawLine((int) e.getOrigin().getX() * 2 + 300, (int) e.getOrigin().getY() * 2 -100,
							(int) e.getEnd().getX() * 2 + 300, (int) e.getEnd().getY() * 2 -100);

				}

				else {
					if (e.getOrigin().getTypeNode() > 1 && e.getOrigin().getId() != 0) {
						g.setColor(Color.RED);
						g.fillOval((int) e.getOrigin().getX() * 2 + 300, (int) e.getOrigin().getY() * 2 -100, 3, 3);
						g.setColor(Color.BLACK);
						//// g.drawString(""+e.getOrigin().getId(), (int) e.getOrigin().getX()*2+300,
						//// (int) e.getOrigin().getY()*2+100+6);
						// g.drawString(""+e.getOrigin().getId(), (int) e.getOrigin().getX()*2+700,
						//// (int) e.getOrigin().getY()*2+100+6);
						g.drawString("" + e.getOrigin().getId(), (int) e.getOrigin().getX() * 2 + 300,
								(int) e.getOrigin().getY() * 2 -100 + 6);
						g.setColor(Color.BLACK);
						g.drawLine((int) e.getOrigin().getX() * 2 + 300, (int) e.getOrigin().getY() * 2 -100,
								(int) e.getEnd().getX() * 2 + 300, (int) e.getEnd().getY() * 2 -100);
					} else {
						if (e.getOrigin().getTypeNode() == 1 && e.getOrigin().getId() != 0) {
							g.setColor(Color.YELLOW);
							g.fillOval((int) e.getOrigin().getX() * 2 + 300, (int) e.getOrigin().getY() * 2 -100, 3, 3);
							g.setColor(Color.BLACK);
							//// g.drawString(""+e.getOrigin().getId(), (int) e.getOrigin().getX()*2+300,
							//// (int) e.getOrigin().getY()*2+100+6);
							// g.drawString(""+e.getOrigin().getId(), (int) e.getOrigin().getX()*2+700,
							//// (int) e.getOrigin().getY()*2+100+6);
							g.drawString("" + e.getOrigin().getId(), (int) e.getOrigin().getX() * 2 + 300,
									(int) e.getOrigin().getY() * 2 -100 + 6);
							g.setColor(Color.BLACK);
							g.drawLine((int) e.getOrigin().getX() * 2 + 300, (int) e.getOrigin().getY() * 2 -100,
									(int) e.getEnd().getX() * 2 + 300, (int) e.getEnd().getY() * 2 -100);

						} else {
							// g.setColor(Color.PINK);
							// g.fillOval((int) e.getOrigin().getX(), (int) e.getOrigin().getY(), 4,4);
							g.setColor(Color.BLACK);
							g.drawLine((int) e.getOrigin().getX() * 2 + 300, (int) e.getOrigin().getY() * 2 -100,
									(int) e.getEnd().getX() * 2 + 300, (int) e.getEnd().getY() * 2 -100);

						}
					}
				}
			}
			Edge last = Network.getLast();

			if (last.getOrigin().getId() == 0 && last.getOrigin().getTypeNode() > 1) {
				g.setColor(Color.BLACK);
				g.fillRect((int) last.getOrigin().getX() * 2 + 300, (int) last.getOrigin().getY() * 2 -100, 3, 3);
				g.setColor(Color.BLACK);
				g.drawLine((int) last.getOrigin().getX() * 2 + 300, (int) last.getOrigin().getY() * 2 -100,
						(int) last.getEnd().getX() * 2 + 300, (int) last.getEnd().getY() * 2 -100);
				g.drawString("" + last.getOrigin().getId(), (int) last.getOrigin().getX() * 2 + 300,
						(int) last.getOrigin().getY() * 2 -100 + 6);
			} else {
				if (last.getOrigin().getTypeNode() > 1 && last.getOrigin().getId() != 0) {
					g.setColor(Color.RED);
					g.fillOval((int) last.getOrigin().getX() * 2 + 300, (int) last.getOrigin().getY() * 2 -100, 3, 3);
					g.setColor(Color.BLACK);
					//// g.drawString(""+last.getOrigin().getId(), (int)
					//// last.getOrigin().getX()*2+300, (int) last.getOrigin().getY()*2+100+6);
					// g.drawString(""+last.getOrigin().getId(), (int)
					//// last.getOrigin().getX()*2+700, (int) last.getOrigin().getY()*2+100+6);
					g.drawString("" + last.getOrigin().getId(), (int) last.getOrigin().getX() * 2 + 300,
							(int) last.getOrigin().getY() * 2 -100 + 6);
					g.setColor(Color.BLACK);
					g.drawLine((int) last.getOrigin().getX() * 2 + 300, (int) last.getOrigin().getY() * 2 -100,
							(int) last.getEnd().getX() * 2 + 300, (int) last.getEnd().getY() * 2 -100);
				} else {
					if (last.getOrigin().getTypeNode() == 1 && last.getOrigin().getId() != 0) {
						g.setColor(Color.YELLOW);
						g.fillOval((int) last.getOrigin().getX() * 2 + 300, (int) last.getOrigin().getY() * 2 -100, 3, 3);
						g.setColor(Color.BLACK);
						//// g.drawString(""+last.getOrigin().getId(), (int)
						//// last.getOrigin().getX()*2+300, (int) last.getOrigin().getY()*2+100+6);
						// g.drawString(""+last.getOrigin().getId(), (int)
						//// last.getOrigin().getX()*2+700, (int) last.getOrigin().getY()*2+100+6);
						g.drawString("" + last.getOrigin().getId(), (int) last.getOrigin().getX() * 2 + 300,
								(int) last.getOrigin().getY() * 2 -100 + 6);
						g.setColor(Color.BLACK);
						g.drawLine((int) last.getOrigin().getX() * 2 + 300, (int) last.getOrigin().getY() * 2 -100,
								(int) last.getEnd().getX() * 2 + 300, (int) last.getEnd().getY() * 2 -100);
					} else {
						// g.setColor(Color.PINK);
						// g.fillOval((int) last.getOrigin().getX(), (int) last.getOrigin().getY(),
						// 4,4);
						g.setColor(Color.BLACK);
						g.drawLine((int) last.getOrigin().getX() * 2 + 300, (int) last.getOrigin().getY() * 2 -100,
								(int) last.getEnd().getX() * 2 + 300, (int) last.getEnd().getY() * 2 -100);

					}

				}
			}}
		if(aTest.getInstanceName().equals("p4.2.a") ) {
			for (Edge e : Network) {
				if (e.getOrigin().getId() == 0 && e.getOrigin().getTypeNode() > 1) {
					g.setColor(Color.BLACK);
					g.fillRect((int) e.getOrigin().getX() * 2 + 300, (int) e.getOrigin().getY() * 2 -20, 3, 3);
					g.setColor(Color.BLACK);
					//// g.drawString(""+e.getOrigin().getId(), (int) e.getOrigin().getX()*2+300,
					//// (int) e.getOrigin().getY()*2+100+6);
					// g.drawString(""+e.getOrigin().getId(), (int) e.getOrigin().getX()*2+700,
					//// (int) e.getOrigin().getY()*2+100+6);
					g.drawString("" + e.getOrigin().getId(), (int) e.getOrigin().getX() * 2 + 300,
							(int) e.getOrigin().getY() * 2 -20 + 6);
					g.drawLine((int) e.getOrigin().getX() * 2 + 300, (int) e.getOrigin().getY() * 2 -20,
							(int) e.getEnd().getX() * 2 + 300, (int) e.getEnd().getY() * 2 -20);

				}

				else {
					if (e.getOrigin().getTypeNode() > 1 && e.getOrigin().getId() != 0) {
						g.setColor(Color.RED);
						g.fillOval((int) e.getOrigin().getX() * 2 + 300, (int) e.getOrigin().getY() * 2 -20, 3, 3);
						g.setColor(Color.BLACK);
						//// g.drawString(""+e.getOrigin().getId(), (int) e.getOrigin().getX()*2+300,
						//// (int) e.getOrigin().getY()*2+100+6);
						// g.drawString(""+e.getOrigin().getId(), (int) e.getOrigin().getX()*2+700,
						//// (int) e.getOrigin().getY()*2+100+6);
						g.drawString("" + e.getOrigin().getId(), (int) e.getOrigin().getX() * 2 + 300,
								(int) e.getOrigin().getY() * 2 -20 + 6);
						g.setColor(Color.BLACK);
						g.drawLine((int) e.getOrigin().getX() * 2 + 300, (int) e.getOrigin().getY() * 2 -20,
								(int) e.getEnd().getX() * 2 + 300, (int) e.getEnd().getY() * 2 -20);
					} else {
						if (e.getOrigin().getTypeNode() == 1 && e.getOrigin().getId() != 0) {
							g.setColor(Color.YELLOW);
							g.fillOval((int) e.getOrigin().getX() * 2 + 300, (int) e.getOrigin().getY() * 2 -20, 3, 3);
							g.setColor(Color.BLACK);
							//// g.drawString(""+e.getOrigin().getId(), (int) e.getOrigin().getX()*2+300,
							//// (int) e.getOrigin().getY()*2+100+6);
							// g.drawString(""+e.getOrigin().getId(), (int) e.getOrigin().getX()*2+700,
							//// (int) e.getOrigin().getY()*2+100+6);
							g.drawString("" + e.getOrigin().getId(), (int) e.getOrigin().getX() * 2 + 300,
									(int) e.getOrigin().getY() * 2 -20 + 6);
							g.setColor(Color.BLACK);
							g.drawLine((int) e.getOrigin().getX() * 2 + 300, (int) e.getOrigin().getY() * 2 -20,
									(int) e.getEnd().getX() * 2 + 300, (int) e.getEnd().getY() * 2 -20);

						} else {
							// g.setColor(Color.PINK);
							// g.fillOval((int) e.getOrigin().getX(), (int) e.getOrigin().getY(), 4,4);
							g.setColor(Color.BLACK);
							g.drawLine((int) e.getOrigin().getX() * 2 + 300, (int) e.getOrigin().getY() * 2 -20,
									(int) e.getEnd().getX() * 2 + 300, (int) e.getEnd().getY() * 2 -20);

						}
					}
				}
			}
			Edge last = Network.getLast();

			if (last.getOrigin().getId() == 0 && last.getOrigin().getTypeNode() > 1) {
				g.setColor(Color.BLACK);
				g.fillRect((int) last.getOrigin().getX() * 2 + 300, (int) last.getOrigin().getY() * 2 -20, 3, 3);
				g.setColor(Color.BLACK);
				g.drawLine((int) last.getOrigin().getX() * 2 + 300, (int) last.getOrigin().getY() * 2 -20,
						(int) last.getEnd().getX() * 2 + 300, (int) last.getEnd().getY() * 2 -20);
				g.drawString("" + last.getOrigin().getId(), (int) last.getOrigin().getX() * 2 + 300,
						(int) last.getOrigin().getY() * 2 -20 + 6);
			} else {
				if (last.getOrigin().getTypeNode() > 1 && last.getOrigin().getId() != 0) {
					g.setColor(Color.RED);
					g.fillOval((int) last.getOrigin().getX() * 2 + 300, (int) last.getOrigin().getY() * 2 -20, 3, 3);
					g.setColor(Color.BLACK);
					//// g.drawString(""+last.getOrigin().getId(), (int)
					//// last.getOrigin().getX()*2+300, (int) last.getOrigin().getY()*2+100+6);
					// g.drawString(""+last.getOrigin().getId(), (int)
					//// last.getOrigin().getX()*2+700, (int) last.getOrigin().getY()*2+100+6);
					g.drawString("" + last.getOrigin().getId(), (int) last.getOrigin().getX() * 2 + 300,
							(int) last.getOrigin().getY() * 2 -20 + 6);
					g.setColor(Color.BLACK);
					g.drawLine((int) last.getOrigin().getX() * 2 + 300, (int) last.getOrigin().getY() * 2 -20,
							(int) last.getEnd().getX() * 2 + 300, (int) last.getEnd().getY() * 2 -20);
				} else {
					if (last.getOrigin().getTypeNode() == 1 && last.getOrigin().getId() != 0) {
						g.setColor(Color.YELLOW);
						g.fillOval((int) last.getOrigin().getX() * 2 + 300, (int) last.getOrigin().getY() * 2 -20, 3, 3);
						g.setColor(Color.BLACK);
						//// g.drawString(""+last.getOrigin().getId(), (int)
						//// last.getOrigin().getX()*2+300, (int) last.getOrigin().getY()*2+100+6);
						// g.drawString(""+last.getOrigin().getId(), (int)
						//// last.getOrigin().getX()*2+700, (int) last.getOrigin().getY()*2+100+6);
						g.drawString("" + last.getOrigin().getId(), (int) last.getOrigin().getX() * 2 + 300,
								(int) last.getOrigin().getY() * 2 -20 + 6);
						g.setColor(Color.BLACK);
						g.drawLine((int) last.getOrigin().getX() * 2 + 300, (int) last.getOrigin().getY() * 2 -20,
								(int) last.getEnd().getX() * 2 + 300, (int) last.getEnd().getY() * 2 -20);
					} else {
						// g.setColor(Color.PINK);
						// g.fillOval((int) last.getOrigin().getX(), (int) last.getOrigin().getY(),
						// 4,4);
						g.setColor(Color.BLACK);
						g.drawLine((int) last.getOrigin().getX() * 2 + 300, (int) last.getOrigin().getY() * 2 -20,
								(int) last.getEnd().getX() * 2 + 300, (int) last.getEnd().getY() * 2 -20);

					}

				}
			}
		}
		image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);


		printingImage(g);


	}


	public void printingImage(Graphics2D g) {
		try {
			String file="c";
			PrintWriter bw = new PrintWriter(file);
			bw.println("Revelead network");
			bw.print(g.drawImage(image, x, y, null));

			bw.flush();
			bw.close();
		} catch (IOException e) {

		}

	}

}
