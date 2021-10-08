import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Game extends JPanel implements KeyListener{
	
	static JFrame frame;
	static BufferedImage bird, pipe;
	static JLabel points;
	static double x = 30, y = 100, speed = 0;
	static int px = 2000, py = 400;
	static int po = 230; //pipe opening
	static int pi = 333; //pipe interval
	static int ps = 3; //pipe speed
	static ArrayList<Point> pipes = new ArrayList<Point>();
	static final int fps = 60;
	static long time = 0;

	public static void main(String[] args) {
		
		try {
			bird = ImageIO.read(new File("res\\Bird.png"));
			pipe = ImageIO.read(new File("res\\PipeLong.png"));
		}catch (IOException ex) {
			System.out.println("Error");
		}
		
		
		frame = new JFrame();
		frame.setVisible(true);
		frame.setBounds(0, 0, 1000, 1000);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Game panel = new Game();
		panel.setVisible(true);
		panel.setBackground(Color.cyan);
		
		frame.add(panel);
		frame.repaint();
		frame.addKeyListener(panel);
		
		
		Timer timer = new Timer(1000 / fps, new ActionListener(){
			public void actionPerformed(ActionEvent e1) {
				
				if(time % 120 == 0) {
					py = 400 + (int)(Math.random() * 400);
					Point e = new Point(px, py);
					pipes.add(e);
				}
				
				speed = panel.accelerate(speed);
				
				y += speed;
				if(y > 1000 + 96) restart();
				
				
				for(int i = 0; i < pipes.size(); i++) {
					Point p = pipes.get(i);
					p.x -= ps;
					if(p.x + 96 < 0) pipes.remove(p);
					if(p.x < x + 96 - 12 && p.x > x - 96 + 12)
						try {
							checkCollision(p);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				}
				
				time++;
				
				//System.out.println(y);
				
				frame.repaint();;
			}
			
		});
		timer.start();
		
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.black);
		g.drawImage(bird, 0 + (int)x, 0 + (int)y, 96 + (int)x, 96 + (int)y, 0, 0, 16, 16, null);
		
		
		for(Point p : pipes) {
			g.drawImage(pipe, 0 + p.x, p.y, 96 + p.x, p.y + 512*6, 0, 0, 16, 512, null);
			g.drawImage(pipe, 0 + p.x, p.y - po - 512 * 6, 96 + p.x, p.y - po, 0, 512, 16, 0, null);
		}
		
		
	}
	
	public double accelerate(double speed) {
		
		speed += 0.5;
		
		return speed;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		speed = -10;
		System.out.println("key pressed");
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public static void checkCollision(Point pipe) throws InterruptedException {
		if(y + 12 < pipe.y - po || y + 96 - 12 > pipe.y) {
			System.out.println("collision");
			restart();
		}
	}
	
	public static void restart() {
		pipes.clear();
		y = 100;
		speed = 0;
	}
	
}
