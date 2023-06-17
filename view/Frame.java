package view;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import controller.*;
import observer.*;
import cores.*;



public class Frame extends JFrame implements ObserverLudo {
    public static final int LADO = 36;
    public static final int LARG_DEFAULT = 22*LADO;
    public static final int ALT_DEFAULT = 16*LADO;
    Controller controller = Controller.create();

    {
        controller.addObserver(this);
    }

    public Frame () {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Super Ludo");

        controller.addContentPane(getContentPane());

        JButton novoJogoButton =     new JButton("Novo Jogo");
        JButton carregarJogoButton = new JButton("Carregar Jogo");
        JButton salvarJogoButton =   new JButton("Salvar Jogo");
        JButton lancarDadoButton =   new JButton("Lançar Dado");

        controller.setBotaoDado(lancarDadoButton);

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
        
        JPanel buttonsPanel = new JPanel();

        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.add(novoJogoButton);
        buttonsPanel.add(Box.createVerticalStrut(10)); // adiciona espacamento vertical de 10 pixels
        buttonsPanel.add(carregarJogoButton);
        buttonsPanel.add(Box.createVerticalStrut(10));
        buttonsPanel.add(salvarJogoButton);
        buttonsPanel.add(Box.createVerticalStrut(10)); 
        buttonsPanel.add(lancarDadoButton);

        getContentPane().add(buttonsPanel, BorderLayout.EAST);

        
        setSize(LARG_DEFAULT, ALT_DEFAULT);
        setVisible(true);
    }

    public void notify (ObservableLudo model) {
        Object[] data = (Object[]) model.get();
        Object[][] dataPosPioes = (Object[][]) data[0];
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
