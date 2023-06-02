package graphic;

import javax.swing.*;

import model.Model;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Frame extends JFrame {
    public static final int LADO = 36;
    public static final int LADO2 = 18;
    public static final int LADO3 = 12;
    public static final int LADO4 = 9;
    public static final int LARG_DEFAULT = 1200;
    public static final int ALT_DEFAULT = 700;
    int[] inicial = {(LARG_DEFAULT - 15 * LADO) / 2, (ALT_DEFAULT - 15 * LADO) / 2};

    //private Panel panel;

    public Frame(Model model){
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Super Ludo");

        Panel p = new Panel();
        p.setModel(model);
        getContentPane().add(p);

        JButton novoJogoButton = new JButton("Novo Jogo");
        JButton carregarJogoButton = new JButton("Carregar Jogo");
        JButton salvarJogoButton = new JButton("Salvar Jogo");
        JButton lancarDadoButton = new JButton("Lançar Dado");

        novoJogoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Handle Novo Jogo button click
            }
        });

        carregarJogoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Handle Carregar Jogo button click
            }
        });

        salvarJogoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Handle Salvar Jogo button click
            }
        });

       
        lancarDadoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int resultado = Model.lancaDado(); 

                // Update dado no Panel
                p.setResultadoDado(resultado);
                model.updateVez();
                p.repaint();

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
}
