package graphic;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.time.Year;

import cores.*;

public class Panel extends JPanel {

    public void parteTabuleiro(Graphics g, int cor, int x, int y){
        Graphics2D g2d=(Graphics2D) g;
        Rectangle2D rt1=new Rectangle2D.Double(x, y, 240, 240);
        // MELHORAR

        if (cor == 0){
            g2d.setPaint(Color.RED);

        }
        else{ 
            g2d.setPaint(Color.GREEN);}
        g2d.fill(rt1);
        y += 240;
        for (int j=0; j<3; j++){
            for (int i=0; i<6; i++){
                rt1 = new Rectangle2D.Double(x+40*i, y+40*j, 40, 40 );
                g2d.setPaint(Color.BLACK);
                g2d.draw(rt1); 
            }
        }

        for (int j=0; j<5; j++){
            rt1 = new Rectangle2D.Double(x+40+40*j, y+40, 40, 40 );
            // MELHORAR COM DICT
            if (cor == 0){
                g2d.setPaint(Color.RED);
    
            }
            else{ 
                g2d.setPaint(Color.GREEN);}
            g2d.fill(rt1); 
        }


        rt1 = new Rectangle2D.Double(x+40, y+40, 40, 40 );
        if (cor == 0){
            g2d.setPaint(Color.RED);

        }
        else{ 
            g2d.setPaint(Color.GREEN);}
        g2d.fill(rt1); 

    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d=(Graphics2D) g;
        //g.drawString("Maria Clara Werneck", 500,250);
        // Desenha retângulo
        Rectangle2D rt=new Rectangle2D.Double(300,50,600,600);
        g2d.setPaint(Color.BLACK);
        g2d.draw(rt);

        //QUADRADOS MENORES
        for (int i=0; i<4; i++){
            Rectangle2D rt1=new Rectangle2D.Double(300,50,240,240);
            g2d.setPaint(Color.RED);
            g2d.fill(rt1);


        }
        parteTabuleiro(g, 0, 300,50);

        g2d.rotate(-1.57079632679, 300+240, 410);

        parteTabuleiro(g, 1, 300,410-240);
       // g2d.rotate(-1.57079632679);


        // Rectangle2D rt2=new Rectangle2D.Double(300,410,240,240);
        // g2d.setPaint(Color.BLUE);
        // g2d.fill(rt2);
        // Rectangle2D rt3=new Rectangle2D.Double(660,50,240,240);
        // g2d.setPaint(Color.GREEN);
        // g2d.fill(rt3);
        // Rectangle2D rt4=new Rectangle2D.Double(660,410,240,240);
        // g2d.setPaint(Color.YELLOW);
        // g2d.fill(rt4);
        // int x = 660;
        // for (int i=0; i<6; i++){
        //     Rectangle2D r=new Rectangle2D.Double(x,50+40*i,40,40);
        //     g2d.setPaint(Color.BLACK);
        //     g2d.draw(r);
        // }
        //g2d.fill(rt);
    }
}
