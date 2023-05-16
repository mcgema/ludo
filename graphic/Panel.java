package graphic;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;


public class Panel extends JPanel {
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d=(Graphics2D) g;
        //g.drawString("Maria Clara Werneck", 500,250);
        // Desenha retângulo
        double leftX=300.0;
        double topY=50.0;
        double larg=600.0;
        double alt=600.0;
        Rectangle2D rt=new Rectangle2D.Double(leftX,topY,larg,alt);
        g2d.setPaint(Color.BLACK);
        g2d.draw(rt);

        //QUADRADOS MENORES
        Rectangle2D rt1=new Rectangle2D.Double(300,50,240,240);
        g2d.setPaint(Color.BLUE);
        g2d.fill(rt1);
        Rectangle2D rt2=new Rectangle2D.Double(300,410,240,240);
        g2d.setPaint(Color.RED);
        g2d.fill(rt2);
        Rectangle2D rt3=new Rectangle2D.Double(660,50,240,240);
        g2d.setPaint(Color.YELLOW);
        g2d.fill(rt3);
        Rectangle2D rt4=new Rectangle2D.Double(660,410,240,240);
        g2d.setPaint(Color.GREEN);
        g2d.fill(rt4);

        //g2d.fill(rt);
    }
}
