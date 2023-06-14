package view;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import cores.*;
import observer.*;
import controller.*;

public class View extends JPanel implements MouseListener, ObserverLudo {
    private static View singleton;

    public static final int LADO = 36, LADO2 = 18, LADO3 = 12, LADO4 = 9;
    public static final int LARG_DEFAULT = 22*LADO;
    public static final int ALT_DEFAULT = 16*LADO;

    static Color colorG = new Color(34, 139, 34); 
    static Color colorY = new Color(255, 239, 0);
    static Color colorB = new Color(13, 101, 189);
    static Color colorR = new Color(218, 0, 0);

    static int[][] lutX = {
        {0, 1, 2, 3, 4, 5, 6,  6,  6,  6,  6,  6,  7,  8,  8,  8,  8,  8, 8, 9, 10, 11, 12, 13, 14, 14, 14, 13, 12, 11, 10, 9, 8, 8, 8, 8, 8, 8, 7, 6, 6, 6, 6, 6, 6, 5, 4, 3, 2, 1, 0, 0, 1, 2, 3, 4, 5, 6},
        {9, 8, 8, 8, 8, 8, 9, 10, 11, 12, 13, 14, 14, 14, 13, 12, 11, 10, 9, 8, 8, 8,  8,  8,  8,  7,  6,  6,  6,  6,  6,  6,  5, 4, 3, 2, 1, 0, 0, 0, 1, 2, 3, 4, 5, 6, 6, 6, 6, 6, 6, 7, 7, 7, 7, 7, 7, 6},
        {9, 13, 12, 11, 10, 9, 8, 8, 8, 8, 8, 8, 7, 6, 6, 6, 6, 6, 6, 5, 4, 3, 2, 1, 0, 0, 0, 1, 2, 3, 4, 5, 6, 6, 6, 6, 6, 6, 7, 8, 8, 8, 8, 8, 8, 9, 10, 11, 12, 13, 14, 14, 13, 12, 11, 10, 9, 6},
        {0, 6, 6, 6, 6, 6, 5, 4, 3, 2, 1, 0, 0, 0, 1, 2, 3, 4, 5, 6, 6, 6, 6, 6, 6, 7, 8, 8, 8, 8, 8, 8, 9, 10, 11, 12, 13, 14, 14, 14, 13, 12, 11, 10, 9, 8, 8, 8, 8, 8, 8, 7, 7, 7, 7, 7, 7, 6}
    };
    static int[][] lutY = {
        {0, 6, 6, 6, 6, 6, 5, 4, 3, 2, 1, 0, 0, 0, 1, 2, 3, 4, 5, 6, 6, 6, 6, 6, 6, 7, 8, 8, 8, 8, 8, 8, 9, 10, 11, 12, 13, 14, 14, 14, 13, 12, 11, 10, 9, 8, 8, 8, 8, 8, 8, 7, 7, 7, 7, 7, 7, 6},
        {0, 1, 2, 3, 4, 5, 6, 6, 6, 6, 6, 6, 7, 8, 8, 8, 8, 8, 8, 9, 10, 11, 12, 13, 14, 14, 14, 13, 12, 11, 10, 9, 8, 8, 8, 8, 8, 8, 7, 6, 6, 6, 6, 6, 6, 5, 4, 3, 2, 1, 0, 0, 1, 2, 3, 4, 5, 6},
        {9, 8, 8, 8, 8, 8, 9, 10, 11, 12, 13, 14, 14, 14, 13, 12, 11, 10, 9, 8, 8, 8,  8,  8,  8,  7,  6,  6,  6,  6,  6,  6,  5, 4, 3, 2, 1, 0, 0, 0, 1, 2, 3, 4, 5, 6, 6, 6, 6, 6, 6, 7, 7, 7, 7, 7, 7, 6},
        {9, 13, 12, 11, 10, 9, 8, 8, 8, 8, 8, 8, 7, 6, 6, 6, 6, 6, 6, 5, 4, 3, 2, 1, 0, 0, 0, 1, 2, 3, 4, 5, 6, 6, 6, 6, 6, 6, 7, 8, 8, 8, 8, 8, 8, 9, 10, 11, 12, 13, 14, 14, 13, 12, 11, 10, 9, 6}
    };
    static int[] lutSize = {
        6*LADO, LADO, LADO, LADO, LADO, LADO, LADO, LADO, LADO, LADO, LADO, LADO, LADO, LADO, LADO, LADO, LADO, LADO, LADO, LADO, LADO, LADO, LADO, LADO, LADO, LADO, LADO, LADO, LADO, LADO, LADO, LADO, LADO, LADO, LADO, LADO, LADO, LADO, LADO, LADO, LADO, LADO, LADO, LADO, LADO, LADO, LADO, LADO, LADO, LADO, LADO, LADO, LADO, LADO, LADO, LADO, LADO, 3*LADO
    };

    Controller cont;
    int[][] posPioes = new int[4][4];
    Cor corVez = Cor.vermelho;
    int dadoAtual = 0;

    {
        addMouseListener(this);
    }

    private View() {
        // construtor bloqueado pelo Singleton
    }

    public static View create() {
        if (singleton == null) singleton = new View();
        return singleton;
    }

    public void notify(ObservableLudo model) {
        Object[] data = (Object[]) model.get();
        Object[][] dataPosPioes = (Object[][]) data[0];
        corVez = (Cor) data[1];
        dadoAtual = (int) data[2];

        for (int i = 0; i < 4; i++) for (int j = 0; j < 4; j++) posPioes[i][j] = (int) dataPosPioes[i][j];
        

        this.repaint();
    }
    
    public void updateCont (Controller c) {
        cont = c;
    }
    

    Cor peaoNaMesmaCasa (Cor cor, int pos) {
        int idxPiao = -1;
        for (int i = 0; i < 4; i++) {
            if (posPioes[cor.ordinal()][i] == pos) {
                if (idxPiao < 0) idxPiao = i;
                else return cor;
            }
        }
        if (pos > 51) return null;  // se pião na reta final e não encontrou da mesma cor, não tem

        for (Cor c: Cor.values()) {
            if (c == cor) continue;
            for (int i = 0; i < 4; i++) {
                if (posPioes[c.ordinal()][i] == 0) continue;

                int corOrigem = cor.ordinal();
                int corFim = c.ordinal();
                int posOrigem = posPioes[corOrigem][idxPiao];
                int posFim = (posOrigem+13*((corOrigem-corFim+4)%4))%52;
                if (posPioes[c.ordinal()][i] == posFim) return c;
            }
        }
        return null;
    }

    static Color getCor (Cor c) {
        switch (c) {
            case vermelho:
                return colorR;
            case amarelo:
                return colorY;
            case verde:
                return colorG;
            default:
                return colorB;
        }
    }

    void desenhaPiao (Graphics g, Cor c, int pos) {
        if (pos > 57) return;
        if (pos < 1) return;
        Graphics2D g2d = (Graphics2D) g;
        BasicStroke stroke = new BasicStroke(1.0f);
        g2d.setStroke(stroke);
        Ellipse2D piao = new Ellipse2D.Double(LADO*lutX[c.ordinal()][pos] + (LADO-LADO2)/2, LADO*lutY[c.ordinal()][pos] + (LADO-LADO2)/2, LADO2, LADO2);
        g2d.setPaint(getCor(c));
        g2d.fill(piao);
        g2d.setPaint(Color.WHITE);
        g2d.draw(piao);
    }

    void desenhaPiao (Graphics g, Cor c, int pos, Cor prev) {
        if (pos > 57) return;
        if (pos < 1) return;
        Graphics2D g2d = (Graphics2D) g;
        BasicStroke stroke = new BasicStroke(1.0f);
        g2d.setStroke(stroke);
        Ellipse2D piaoOld = new Ellipse2D.Double(LADO*lutX[c.ordinal()][pos] + 4, LADO*lutY[c.ordinal()][pos] + 4, LADO2+10, LADO2+10);
        g2d.setPaint(getCor(prev));
        g2d.fill(piaoOld);
        g2d.setPaint(Color.WHITE);
        g2d.draw(piaoOld);
        if (prev == c) {
            Ellipse2D espaco = new Ellipse2D.Double(LADO*lutX[c.ordinal()][pos] + 7, LADO*lutY[c.ordinal()][pos] + 7, LADO2+4, LADO2+4);
            g2d.fill(espaco);
        }
        Ellipse2D piao = new Ellipse2D.Double(LADO*lutX[c.ordinal()][pos] + (LADO-LADO2)/2, LADO*lutY[c.ordinal()][pos] + (LADO-LADO2)/2, LADO2, LADO2);
        g2d.setPaint(getCor(c));
        g2d.fill(piao);
    }
    
    void desenhaPioesInicial (Graphics g, Cor c, int qtd) {
        if (qtd < 1) return;
        Graphics2D g2d = (Graphics2D) g;
        g2d.setPaint(getCor(c));
        for (int i=0; i<2; i++){
            for (int j=0; j<2; j++){
                if (2*i+j == qtd) return;
                Ellipse2D e1 = new Ellipse2D.Double(LADO*lutX[c.ordinal()][0]+63+i*72, LADO*lutY[c.ordinal()][0]+63+j*72, LADO2, LADO2);
                g2d.fill(e1);
            }
        }
    }

    static private void parteTabuleiro(Graphics g, Cor cor1, Cor cor2) {
        int x = 0, y = 0;
        Graphics2D g2d = (Graphics2D) g;
        Rectangle2D rect1=new Rectangle2D.Double(x, y, LADO*6, LADO*6);
        g2d.setPaint(getCor(cor1));
        g2d.fill(rect1);

        //circulos dos peoes e peoes
        Ellipse2D e1 = new Ellipse2D.Double();
        for (int i=0; i<2; i++){
            for (int j=0; j<2; j++){
                e1 = new Ellipse2D.Double(x+LADO+LADO2+2*i*LADO, y+LADO+LADO2+2*j*LADO, LADO, LADO);
                g2d.setPaint(Color.WHITE);
                g2d.fill(e1);
            }
        }

        BasicStroke stroke = new BasicStroke(3.0f);
        g2d.setStroke(stroke);
        rect1 = new Rectangle2D.Double(x+LADO, y+LADO, 4*LADO, 4*LADO);
        g2d.setPaint(getCor(cor2));
        g2d.draw(rect1);

        // ajuste novamente tamanho da linha 
        stroke = new BasicStroke(1.0f);
        g2d.setStroke(stroke);
        y += 6*LADO;

        // linha de casas da cor
        for (int j=0; j<5; j++){
            rect1 = new Rectangle2D.Double(x+LADO+LADO*j, y+LADO, LADO, LADO );
            g2d.setPaint(getCor(cor1));
            g2d.fill(rect1); 
        }

         // casa inicial
         rect1 = new Rectangle2D.Double(x+LADO, y, LADO, LADO);
         g2d.setPaint(getCor(cor1));
         g2d.fill(rect1);
 
         // trocou x com y 
         int[] xPoints1 =  {x + LADO + LADO3, x + LADO + LADO3, x + LADO + 2*LADO3}; 
         int[] yPoints1 = {y + LADO3, y + 2*LADO3, y + LADO2}; 
 
         g2d.setPaint(Color.WHITE);
         g2d.fillPolygon(xPoints1, yPoints1, 3); 

         //casa preta
         rect1 = new Rectangle2D.Double(x+LADO, y+2*LADO, LADO, LADO);
         g2d.setPaint(Color.BLACK);
         g2d.fill(rect1);
     
        // casas brancas
        for (int j=0; j<3; j++){
            for (int i=0; i<6; i++){
                rect1 = new Rectangle2D.Double(x+LADO*i, y+LADO*j, LADO, LADO);
                g2d.setPaint(Color.BLACK);
                g2d.draw(rect1); 
            }
        }
        // triangulo central
        int[] xPoints = {x + LADO*6, x + LADO*6, x + LADO*7 + LADO2}; // X coordenadas dos vérticecs de triangulo
        int[] yPoints = {y,          y + LADO*3, y + LADO   + LADO2}; // Y coordenadas dos vérticecs de triangulo
        g2d.setPaint(getCor(cor1));
        g2d.fillPolygon(xPoints, yPoints, 3);
    }

    static public void desenhaTabuleiro(Graphics g){
        Graphics2D g2d=(Graphics2D) g;
        parteTabuleiro(g, Cor.vermelho, Cor.verde);
        g2d.rotate(-1.57079632679, 270, 270);
        parteTabuleiro(g, Cor.azul, Cor.vermelho);
        g2d.rotate(-1.57079632679, 270, 270);
        parteTabuleiro(g, Cor.amarelo, Cor.azul);
        g2d.rotate(-1.57079632679, 270, 270);
        parteTabuleiro(g, Cor.verde, Cor.amarelo);
        g2d.rotate(-1.57079632679, 270, 270);
    }

    public void desenhaDado(Graphics g, int resultado, Cor cor){
        System.out.println("View.desenhaDado("+resultado+") -- getDado deu "+dadoAtual);
        Graphics2D g2d = (Graphics2D) g;
        Image dado;
        resultado = dadoAtual;
        switch(resultado){
            case 1:
                dado = Toolkit.getDefaultToolkit().getImage("imagens/Dado1.png");
                break;
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
            default:
                Rectangle2D dadoEmBranco = new Rectangle2D.Double(16*LADO, 12*LADO, 2*LADO, 2*LADO);
                g2d.setPaint(getCor(cor));
                g2d.fill(dadoEmBranco);
                BasicStroke stroke = new BasicStroke(6.0f);
                g2d.setStroke(stroke);
                g2d.draw(dadoEmBranco);
                return;
        }
        g2d.drawImage(dado, 16*LADO, 12*LADO, 2*LADO, 2*LADO, this);
        BasicStroke stroke = new BasicStroke(6.0f);
        g2d.setStroke(stroke);
        Rectangle2D rect1 = new Rectangle2D.Double(16*LADO, 12*LADO, 2*LADO, 2*LADO);
        g2d.setPaint(getCor(cor));
        g2d.draw(rect1); 
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        Rectangle2D rect = new Rectangle2D.Double(0, 0, 15*LADO, 15*LADO);
        g2d.setPaint(Color.BLACK);
        g2d.draw(rect);

        desenhaTabuleiro(g);
        for (Cor cor: Cor.values()) {
            int qtdInicio = 0;
            for (int i = 0; i < 4; i++) {
                if (posPioes[cor.ordinal()][i] == 0) qtdInicio++;
                Cor corOutroPiao = peaoNaMesmaCasa(cor, posPioes[cor.ordinal()][i]);
                if (corOutroPiao != null) {
                    desenhaPiao(g, cor, posPioes[cor.ordinal()][i], corOutroPiao);
                    System.out.println("Encontrei "+cor.toString()+"["+posPioes[cor.ordinal()][i]+"] = "+corOutroPiao.toString());
                }
                else desenhaPiao(g, cor, posPioes[cor.ordinal()][i]);
            }
            desenhaPioesInicial(g, cor, qtdInicio);
        }
        desenhaDado(g, dadoAtual, corVez);
    }

    public void mouseClicked(MouseEvent m) {
        for (int i = 1; i<57; i++) { // começa do 1 já que o jogador nunca escolhe sair com um piao
            // acha a posicao que apertei
            if (LADO*lutX[corVez.ordinal()][i] <= m.getX() && m.getX() <= LADO*lutX[corVez.ordinal()][i] + lutSize[i] &&
                LADO*lutY[corVez.ordinal()][i] <= m.getY() && m.getY() <= LADO*lutY[corVez.ordinal()][i] + lutSize[i]) {
                System.out.printf("View: CLIQUEI NA CASA: %s[%d]\n", corVez.toString(), i);
                for (int j=0; j<4; j++) {
                    if (posPioes[corVez.ordinal()][j] == i){
                        // tem um peao da cor nessa posicao
                        System.out.printf("View: peão %s encontrado na posição %s[%d]!\n", corVez.toString(), corVez.toString(), i);
                        cont.movePiao(corVez, j, dadoAtual);
                    }
                }
            }
       }
       this.repaint();
    }

    public void mousePressed(MouseEvent m) {}
    public void mouseEntered(MouseEvent m) {}
    public void mouseReleased(MouseEvent m) {}
    public void mouseExited(MouseEvent m) {}

    // [tom] deveria estar no controller, não?
    public void carregarJogo(){
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "JTexto", "txt");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
        System.out.println("You chose to open this file: " +
                chooser.getSelectedFile().getName());
        }
    }
}
