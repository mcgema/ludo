package viewPack;

import observer.*;
import cores.*;
import controllerPack.FacadeC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


class Frame extends JFrame implements ObserverLudo {
    private static Frame singleton;

    private static final int LADO = 36;
    private static final int LARG_DEFAULT = 22*LADO;
    private static final int ALT_DEFAULT = 16*LADO;
    private FacadeC controller = FacadeC.getController();
    private View view = View.create();
    
    private JButton novoJogoButton =     new JButton("Novo Jogo");
    private JButton carregarJogoButton = new JButton("Carregar Jogo");
    private JButton salvarJogoButton =   new JButton("Salvar Jogo");
    private JButton lancarDadoButton =   new JButton("Lançar Dado");
    private JButton dado1Button =        new JButton("1");
    private JButton dado2Button =        new JButton("2");
    private JButton dado3Button =        new JButton("3");
    private JButton dado4Button =        new JButton("4");
    private JButton dado5Button =        new JButton("5");
    private JButton dado6Button =        new JButton("6");

    {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Super Ludo");

        getContentPane().add(view);

        novoJogoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.novoJogo();
            }
        });

        carregarJogoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.carregarJogo();
            }
        });

        salvarJogoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.salvarJogo();
            }
        });

        
        lancarDadoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.lancaDado();
            }
        });

        dado1Button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.lancaDado(1);
            }
        });

        dado2Button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.lancaDado(2);
            }
        });

        dado3Button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.lancaDado(3);
            }
        });
        
        dado4Button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.lancaDado(4);
            }
        });
        
        dado5Button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.lancaDado(5);
            }
        });

        dado6Button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.lancaDado(6);
            }
        });

        JPanel buttonsPanel = new JPanel();

        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.add(novoJogoButton);
        buttonsPanel.add(Box.createVerticalStrut(10)); // adiciona espacamento vertical de 10 pixels
        buttonsPanel.add(carregarJogoButton);
        buttonsPanel.add(Box.createVerticalStrut(10));
        buttonsPanel.add(salvarJogoButton);
        buttonsPanel.add(Box.createVerticalStrut(10)); 
        buttonsPanel.add(lancarDadoButton);
        buttonsPanel.add(Box.createVerticalStrut(10)); 
        buttonsPanel.add(dado1Button);
        buttonsPanel.add(Box.createVerticalStrut(10));
        buttonsPanel.add(dado2Button);
        buttonsPanel.add(Box.createVerticalStrut(10));
        buttonsPanel.add(dado3Button);
        buttonsPanel.add(Box.createVerticalStrut(10));
        buttonsPanel.add(dado4Button);
        buttonsPanel.add(Box.createVerticalStrut(10));
        buttonsPanel.add(dado5Button);
        buttonsPanel.add(Box.createVerticalStrut(10));
        buttonsPanel.add(dado6Button);
        buttonsPanel.add(Box.createVerticalStrut(10));

        getContentPane().add(buttonsPanel, BorderLayout.EAST);

        setSize(LARG_DEFAULT, ALT_DEFAULT);
        setVisible(true);
    }

    private Frame () {
        // bloqueado
    }

    protected static Frame getFrame () {
        if (singleton == null) singleton = new Frame();
        return singleton;
    }

    protected JButton getBotaoDado () {
        return lancarDadoButton;
    }

    public void notify (ObservableLudo model) {
        Object[] data = (Object[]) model.get();
        Object[][] dataPosPioes = (Object[][]) data[0];
        boolean podeDado = ((int) data[2]) == 0;
        lancarDadoButton.setEnabled(podeDado);
        dado1Button.setEnabled(podeDado);
        dado2Button.setEnabled(podeDado);
        dado3Button.setEnabled(podeDado);
        dado4Button.setEnabled(podeDado);
        dado5Button.setEnabled(podeDado);
        dado6Button.setEnabled(podeDado);

        int[] placar = new int[4];
        for (int i = 0; i < 4; i++) {
            placar[i] = 0;
            for (int j = 0; j < 4; j++) {
                placar[i] += (int) dataPosPioes[i][j];
            }
        }
        boolean jogoAcabou = false;
        for (int i = 0; i < 4; i++) {
            if (placar[i] == 57*4) {
                jogoAcabou = true;
                break;
            }
        }
        if (!jogoAcabou) return;

        Cor[] podium = new Cor[16];
        for (Cor c: Cor.values()) {
            int colocacao = 0;
            for (int i = 0; i < 4; i++) {
                if (placar[i] > placar[c.ordinal()]) colocacao = colocacao + 1;
            }
            podium[4*colocacao + c.ordinal()] = c;
        }

        String colocacaoString = "";
        for (int i = 0; i < 16; i++) if (podium[i] != null) colocacaoString = colocacaoString + (1+(i/4)) + "o lugar: " + podium[i].toString() + "\n";


        JOptionPane.showMessageDialog(this, "Jogo acabou!\n\nColocação dos jogadores:\n"+ colocacaoString,"Fim de Jogo", JOptionPane.INFORMATION_MESSAGE);

        System.out.printf("Jogo acabou!");
        int opcao = JOptionPane.showOptionDialog (
            this,
            "Iniciar novo jogo?", 
            "Continuar jogando?",
            JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,
            null,
            new Object[]{"Novo Jogo", "Encerra"},
            "Novo Jogo"
        );
        
        System.out.printf("foi: %d", opcao);
        if (opcao == 0) {
            // jogadores vao continuar a jogar - botao continua apertado
            controller.novoJogo();
        }
        else {
            System.exit(0);
        }
        
    }
}
