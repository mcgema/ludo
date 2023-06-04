package graphic;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.Map;
import java.util.HashMap;
import cores.Cor;
import model.Model;
import java.awt.event.MouseListener;


public class Panel extends JPanel {
    public Model model; //faz algum sentido?
    public static final int LADO = 36, LADO2 = 18, LADO3 = 12, LADO4 = 9;
    public static final int LARG_DEFAULT = 22*LADO;
    public static final int ALT_DEFAULT = 16*LADO;
    int[] inicial = {0,0};
    int iClick, jClick;
    public int resultadoDado = 5;
    int peaox = 0, peaoy = 0;
    public Map<Cor, Command> commands = new HashMap<>();
   // x+LADO+LADO2+2*i*LADO+LADO4, y+LADO+LADO2+2*j*LADO+LADO4
    int[][][] peao = { {{LADO+LADO4+LADO2, LADO+LADO4+LADO2}, {3*LADO+3*LADO4, LADO+3*LADO4}, {3*LADO+3*LADO4, LADO+3*LADO4}, {3*LADO+3*LADO4, 3*LADO+3*LADO4}},
                       {{4*LADO+3*LADO4, LADO+3*LADO4}, {4*LADO+3*LADO4, LADO+3*LADO4}, {4*LADO+3*LADO4, LADO+3*LADO4}, {4*LADO+3*LADO4, 3*LADO+3*LADO4} },
                       { {-2*LADO+3*LADO4, -2*LADO+3*LADO4}, {LADO+3*LADO4, -2*LADO+3*LADO4}, {LADO+3*LADO4, -2*LADO+3*LADO4}, {LADO+3*LADO4, LADO+3*LADO4} },
                       { {LADO+3*LADO4, 4*LADO+3*LADO4}, {3*LADO+3*LADO4, 4*LADO+3*LADO4}, {3*LADO+3*LADO4, 4*LADO+3*LADO4}, {3*LADO+3*LADO4, 6*LADO+3*LADO4} } };

    Color verde = new Color(34, 139, 34); 
    Color amarelo = new Color(255, 239, 0);
    Color azul = new Color(13, 101, 189);
    Color vermelho = new Color(218, 0, 0);

    public void setModel(Model model) { 
        this.model = model; 
    }

    public void setPeao(int x, int y){
        this.peaox = x;
        this.peaoy = y;
    }

    public void setResultadoDado(int resultado) {
        this.resultadoDado = resultado;
    }

    public void getFuncaoCor(Graphics g){
        Graphics2D g2d=(Graphics2D) g;
        commands.put(Cor.vermelho, () -> g2d.setPaint(vermelho));
        commands.put(Cor.verde, () -> g2d.setPaint(verde));
        commands.put(Cor.amarelo, () -> g2d.setPaint(amarelo));
        commands.put(Cor.azul, () -> g2d.setPaint(azul));
    }

    public void parteTabuleiro(Graphics g, Cor cor1, Cor cor2, int x, int y){
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
                // e1 = new Ellipse2D.Double(x+LADO+LADO2+2*i*LADO+LADO4, y+LADO+LADO2+2*j*LADO+LADO4, LADO2, LADO2);
                // commands.get(cor1).invoke();
                // g2d.fill(e1);
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
        parteTabuleiro(g, Cor.vermelho, Cor.verde, inicial[0], inicial[1]);
        g2d.rotate(-1.57079632679, inicial[0]+LADO*6, inicial[1]+9*LADO);
        parteTabuleiro(g, Cor.azul, Cor.vermelho, inicial[0], inicial[1]+LADO*3);
        g2d.rotate(-1.57079632679, inicial[0]+ 9*LADO, inicial[1]+6*LADO);
        parteTabuleiro(g, Cor.amarelo, Cor.azul, inicial[0]-3*LADO,inicial[1]-3*LADO);
        g2d.rotate(-1.57079632679, inicial[0]+LADO*6, inicial[1]+9*LADO);
        parteTabuleiro(g, Cor.verde, Cor.amarelo, inicial[0]+3*LADO, inicial[1]);
    }


    public void desenhaPeao(Graphics g, int x, int y, Cor cor){ // (x,y) coordenada do ponto mais a esquerda e topo da casa do peao
        Graphics2D g2d=(Graphics2D) g;
        Ellipse2D e1 = new Ellipse2D.Double(x, y, LADO2, LADO2);
        commands.get(cor).invoke();
        g2d.fill(e1);
    }

    // public void movePeao(int x, int y) {
    //     peaox = x;
    //     peaoy = y;
    //     repaint();
    // }
    
    public void desenhaDado(Graphics g, int resultado, Cor vez){
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
        int y = inicial[1]-3*LADO;

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
        desenhaDado(g2d, this.resultadoDado, model.getVez());
        // for (Cor cor: Cor.values()) {
        //      for (int j=0; j<4; j++){
        //          desenhaPeao(g2d, this.peao[cor.ordinal()][j][0], this.peao[cor.ordinal()][j][1], cor);
        //      }
        // }
        desenhaPeao(g2d, 32, 32, Cor.verde);
        System.out.printf("x=%d y=%d",this.peao[0][0][0], this.peao[0][0][1] );

    }
}