package view;

import javax.swing.*;

import model.Model;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import controller.*;

public class Frame extends JFrame {
    public static final int LADO = 36;
    public static final int LADO2 = 18;
    public static final int LADO3 = 12;
    public static final int LADO4 = 9;
    public static final int LARG_DEFAULT = 22*LADO;
    public static final int ALT_DEFAULT = 16*LADO;
    int[] inicial = {0,0};

    //private Panel panel;

    public Frame (Model model){
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Super Ludo");

        /*
        Panel p = new Panel();
        p.setModel(model);
        
        */
        //View view = new View();
        Controller controller = new Controller();
        View view = controller.view;
        //view.setModel(model);

        getContentPane().add(view);

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
                int dado = model.lancaDado();
                view.updateDado(dado);
                view.updateVez();
                view.repaint();
                view.Jogou = false;
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
