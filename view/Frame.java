package view;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import controller.*;



public class Frame extends JFrame {
    public static final int LADO = 36;
    public static final int LARG_DEFAULT = 22*LADO;
    public static final int ALT_DEFAULT = 16*LADO;

    public Frame () {
        
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setTitle("Super Ludo");

            Controller controller = Controller.create();
            View view = controller.view;

            getContentPane().add(view);

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

        // if (controller.jogoAcabou()){
            if (true){
                JOptionPane.showMessageDialog(this, "Jogo acabou!\n\nColocação dos jogadores:\n", //+ controller.getColocacaoJogadores(),   // Message text
                                        "Fim de Jogo", JOptionPane.INFORMATION_MESSAGE);
    
                System.out.printf("Jogo acabou!");
                int opcao = JOptionPane.showOptionDialog(this, "Deseja continuar jogando?", "Continuar jogando?", 
                                                    JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE, null,  new Object[]{"Continua", "Encerra"}, "Continua");
                
                System.out.printf("foi: %d", opcao);
                if (opcao == 0) {
                    // jogadores vao continuar a jogar - botao continua apertado
                    controller.novoJogo();
                } else {
                   System.exit(0);
                }
            }
    
    }
}
