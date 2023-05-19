package graphic;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.Map;
import java.util.HashMap;
import cores.*;

public class Panel extends JPanel implements Command {
    public Map<String, Command> getFuncaoCor(Graphics g){
        Graphics2D g2d=(Graphics2D) g;
        Map<String, Command> commands = new HashMap<String, Command>();
        commands.put("vermelho", new Command() {public void invoke() {g2d.setPaint(Color.RED);}});
        commands.put("verde", new Command() {public void invoke() {g2d.setPaint(Color.GREEN);}});
        commands.put("amarelo", new Command() {public void invoke() {g2d.setPaint(Color.YELLOW);}});
        commands.put("azul", new Command() {public void invoke() {g2d.setPaint(Color.BLUE);}});
        return commands;
    }

    public void parteTabuleiro(Graphics g, String cor, int x, int y, Map<String, Command> commands){
        
        Graphics2D g2d=(Graphics2D) g;
        Rectangle2D rt1=new Rectangle2D.Double(x, y, 240, 240);

        commands.get(cor).invoke();
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
            commands.get(cor).invoke();
            g2d.fill(rt1); 
        }


        rt1 = new Rectangle2D.Double(x+40, y+40, 40, 40 );
        commands.get(cor).invoke();
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
        Map<String, Command> commands = getFuncaoCor(g);
        parteTabuleiro(g, "vermelho", 300,50, commands);

        g2d.rotate(-1.57079632679, 300+240, 410);

        parteTabuleiro(g, "azul", 300,410-240, commands);

        //g2d.rotate(-1.57079632679, 300-240, 410);

        //parteTabuleiro(g, "verde", 300+240,410, commands);
    }
}
