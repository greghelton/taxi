import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics; 
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseMotionAdapter; 
import java.util.List;
import java.util.ArrayList;
import javax.swing.JComponent;
import java.awt.Image;
import java.awt.Toolkit;

/** 
  *  compile javac -d build -cp build -sourcepath src/main/java src/main/java/Taxi.java
  *  run java -cp build Taxi
  */ 

public class Taxi {
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI(); 
            }
        });
    }

    private static void createAndShowGUI() { 
        JFrame f = new JFrame("Taxi!");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		MyPanel panel = new MyPanel();
        f.add( panel );
		new Thread( panel ).start();
        f.pack();
        f.setVisible(true);
    } 
}

class MyPanel extends JPanel implements Runnable {
	Image image = Toolkit.getDefaultToolkit().getImage("taxi.png"); 
	List<Taxi> taxis = new ArrayList<Taxi>(); 

    class Taxi extends JComponent implements Runnable {
		private int squareX;
	    private int squareY;
	    final private int squareW = 32;
	    final private int squareH = 32;
		/** 
		 *  constructor 
		 */ 
		public Taxi(int x, int y) {
			this.squareX = x;
			this.squareY = y;
			new Thread(this).start();
		}
		@Override 
		public void run() {
			while (true) { 
				if ( squareX == 0 && squareY == 0) { 
					break; // kill thread when taxi falls out of panel
				}
				if ( squareX > 0 ) squareX--; 
				if ( squareY > 0 ) squareY--;
				try {
					Thread.sleep(111);
				 } catch (InterruptedException e){
					continue;
				}
			}
		}
	}
	
	@Override
	public void run() { 
		while (true) { 
			moveTaxis();  
			try { 
				Thread.sleep(100); 
			 } catch (InterruptedException e) { 
				continue; 
			} 
		} 
	} 

    public MyPanel() {
        setBorder(BorderFactory.createLineBorder(Color.black));
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                taxis.add( new Taxi(e.getX(),e.getY()) );
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                taxis.add( new Taxi(e.getX(),e.getY()) );
            }
        }); 
    }
    
    private void moveTaxis() {
        int OFFSET = 1;
		for (Taxi t : taxis) { 
			repaint(t.squareX,t.squareY,t.squareW+OFFSET,t.squareH+OFFSET); 
		}
    }

    public Dimension getPreferredSize() {
        return new Dimension(350,300);
    } 

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);       
        //g.drawString("This is my custom Panel!",10,20);
		for (Taxi t : taxis) { 
			//g.setColor(Color.RED);
			//g.fillRect(t.squareX,t.squareY,t.squareW,t.squareH);
			g.drawImage(image, t.squareX, t.squareY, this); 
			g.setColor(getBackground());
			g.drawRect(t.squareX,t.squareY,t.squareW,t.squareH);
		}
    }
}