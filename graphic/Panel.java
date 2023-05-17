package graphic;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import model.*;

public class Panel extends JPanel {
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d=(Graphics2D) g;
        //g.drawString("Maria Clara Werneck", 500,250);
        // Desenha retângulo
        Rectangle2D rt=new Rectangle2D.Double(300,50,600,600);
        g2d.setPaint(Color.BLACK);
        g2d.draw(rt);

        //QUADRADOS MENORES
        Rectangle2D rt1=new Rectangle2D.Double(300,50,240,240);
        g2d.setPaint(Color.RED);
        g2d.fill(rt1);
        Rectangle2D rt2=new Rectangle2D.Double(300,410,240,240);
        g2d.setPaint(Color.BLUE);
        g2d.fill(rt2);
        Rectangle2D rt3=new Rectangle2D.Double(660,50,240,240);
        g2d.setPaint(Color.GREEN);
        g2d.fill(rt3);
        Rectangle2D rt4=new Rectangle2D.Double(660,410,240,240);
        g2d.setPaint(Color.YELLOW);
        g2d.fill(rt4);
        int x = 660;
        for (int i=0; i<6; i++){
            Rectangle2D r=new Rectangle2D.Double(x,50+40*i,40,40);
            g2d.setPaint(Color.BLACK);
            g2d.draw(r);
        }
        //g2d.fill(rt);
    }
}
