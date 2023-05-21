package graphic;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.Map;
import java.util.HashMap;
import cores.*;

public class Panel extends JPanel {
    public final int LADO = 36;
    public final int LADO2 = 18;
    public final int LADO3 = 12;
    public final int LADO4 = 9;
    public static final int LARG_DEFAULT=1200;
    public static final int ALT_DEFAULT=700;
    int[] inicial = {(LARG_DEFAULT-15*LADO)/2, (ALT_DEFAULT-15*LADO)/2};

    public int resultadoDado = 5;
    public String vez = "vermelho";
    public Map<String, Command> commands = new HashMap<>();

    Color verde = new Color(34, 139, 34); 
    Color amarelo = new Color(255, 239, 0);
    Color azul = new Color(13, 101, 189);
    Color vermelho = new Color(218, 0, 0);

    public void setResultadoDado(int resultado) {
        this.resultadoDado = resultado;
    }

    public void setVez(String vez) {
        this.vez = vez;
    }

    public void getFuncaoCor(Graphics g){
        Graphics2D g2d=(Graphics2D) g;
        commands.put("vermelho", () -> g2d.setPaint(vermelho));
        commands.put("verde", () -> g2d.setPaint(verde));
        commands.put("amarelo", () -> g2d.setPaint(amarelo));
        commands.put("azul", () -> g2d.setPaint(azul));
    }

    public void parteTabuleiro(Graphics g, String cor1, String cor2, int x, int y){
        Graphics2D g2d=(Graphics2D) g;

        // casa onde começam peoes
        Rectangle2D rt1=new Rectangle2D.Double(x, y, LADO*6, LADO*6);
        commands.get(cor1).invoke();
        g2d.fill(rt1);
        //circulos dos peoes e peoes
        Ellipse2D e1 = new Ellipse2D.Double();
        for (int i=0; i<2; i++){
            for (int j=0; j<2; j++){
                e1 = new Ellipse2D.Double(x+LADO+LADO2+2*i*LADO, y+LADO+LADO2+2*j*LADO, LADO, LADO);
                g2d.setPaint(Color.WHITE);
                g2d.fill(e1); //ou fillOval
                e1 = new Ellipse2D.Double(x+LADO+LADO2+2*i*LADO+LADO4, y+LADO+LADO2+2*j*LADO+LADO4, LADO2, LADO2);
                commands.get(cor1).invoke();
                g2d.fill(e1);
            }
        }

       
        BasicStroke stroke = new BasicStroke(3.0f);
        g2d.setStroke(stroke);
        rt1 = new Rectangle2D.Double(x+LADO, y+LADO, 4*LADO, 4*LADO);
        commands.get(cor2).invoke();
        g2d.draw(rt1);

        // ajuste novamente tamanho da linha 
        stroke = new BasicStroke(1.0f);
        g2d.setStroke(stroke);
        y += 6*LADO;

        // linha de casas da cor
        for (int j=0; j<5; j++){
            rt1 = new Rectangle2D.Double(x+LADO+LADO*j, y+LADO, LADO, LADO );
            commands.get(cor1).invoke();
            g2d.fill(rt1); 
        }
        
         // casa inicial
         rt1 = new Rectangle2D.Double(x+LADO, y, LADO, LADO);
         commands.get(cor1).invoke();
         g2d.fill(rt1);
 
         // trocou x com y 
         int[] xPoints1 =  {x + LADO + LADO3, x + LADO + LADO3, x + LADO + 2*LADO3}; 
         int[] yPoints1 = {y + LADO3, y + 2*LADO3, y + LADO2}; 
 
         g2d.setPaint(Color.WHITE);
         g2d.fillPolygon(xPoints1, yPoints1, 3); 

         //casa preta
         rt1 = new Rectangle2D.Double(x+LADO, y+2*LADO, LADO, LADO);
         g2d.setPaint(Color.BLACK);
         g2d.fill(rt1);
     

        // casas brancas
        for (int j=0; j<3; j++){
            for (int i=0; i<6; i++){
                rt1 = new Rectangle2D.Double(x+LADO*i, y+LADO*j, LADO, LADO);
                g2d.setPaint(Color.BLACK);
                g2d.draw(rt1); 
            }
        }
        // triangulo central
        int[] xPoints = {x + LADO*6, x + LADO*6, x + LADO*7 + LADO2}; // X coordenadas dos vérticecs de triangulo
        int[] yPoints = {y,          y + LADO*3, y + LADO + LADO2}; // Y coordenadas dos vérticecs de triangulo
        commands.get(cor1).invoke();
        g2d.fillPolygon(xPoints, yPoints, 3);
    }

    public void desenhaTabuleiro(Graphics g){
        Graphics2D g2d=(Graphics2D) g;

        parteTabuleiro(g, "vermelho", "verde", inicial[0], inicial[1]);
        g2d.rotate(-1.57079632679, inicial[0]+LADO*6, inicial[1]+9*LADO);
        parteTabuleiro(g, "azul", "vermelho", inicial[0], inicial[1]+LADO*3);
        g2d.rotate(-1.57079632679, inicial[0]+ 9*LADO, inicial[1]+6*LADO);
        parteTabuleiro(g, "amarelo", "azul", inicial[0]-3*LADO,inicial[1]-3*LADO);
        g2d.rotate(-1.57079632679, inicial[0]+LADO*6, inicial[1]+9*LADO);
        parteTabuleiro(g, "verde", "amarelo", inicial[0]+3*LADO, inicial[1]);
    }

    public void desenhaPeao(Graphics g, int x, int y, String cor){ // (x,y) coordenada do ponto mais a esquerda e topo da casa do peao
        Graphics2D g2d=(Graphics2D) g;
        Ellipse2D e1 = new Ellipse2D.Double(x+LADO4, y+LADO4, LADO2, LADO2);
        commands.get(cor).invoke();
        g2d.fill(e1);
    }

    public void desenhaDado(Graphics g, int resultado, String vez){
        Graphics2D g2d = (Graphics2D) g;
        Image dado = Toolkit.getDefaultToolkit().getImage("imagens/Dado1.png"); //caso 1 já feito
        switch(resultado){
            case 2: 
                dado = Toolkit.getDefaultToolkit().getImage("imagens/Dado2.png");
                break;
            case 3: 
                dado = Toolkit.getDefaultToolkit().getImage("imagens/Dado3.png");
                break;
            case 4: 
                dado = Toolkit.getDefaultToolkit().getImage("imagens/Dado4.png");
                break;
            case 5: 
                dado = Toolkit.getDefaultToolkit().getImage("imagens/Dado5.png");
                break;
            case 6: 
                dado = Toolkit.getDefaultToolkit().getImage("imagens/Dado6.png");
                break;         
        }
        int x = inicial[0]+15*LADO;
        int y = inicial[1]-5*LADO;

        g2d.drawImage(dado, x, y, 2*LADO, 2*LADO, this);

        //dado indica de que é vez
        BasicStroke stroke = new BasicStroke(6.0f);
        g2d.setStroke(stroke);
        Rectangle2D rt1 = new Rectangle2D.Double(x, y, 2*LADO, 2*LADO);
        commands.get(vez).invoke();
        g2d.draw(rt1); 

    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d=(Graphics2D) g;
        Rectangle2D rt=new Rectangle2D.Double(inicial[0], inicial[1], 15*LADO, 15*LADO);
        g2d.setPaint(Color.BLACK);
        g2d.draw(rt);
        getFuncaoCor(g);
        desenhaTabuleiro(g);
        // termina (x,y) trocados
        desenhaDado(g2d, this.resultadoDado, this.vez);
    }
}