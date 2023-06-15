package controller;

import model.Model;
import view.View;
import observer.*;
import cores.*;

import java.util.List;
import java.util.ArrayList;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JFileChooser;
import java.io.FileWriter;

public class Controller implements ObserverLudo {
    private static Controller singleton;
    public View view = View.create();
    Model model = Model.create();
    {
        model.addObserver(this);
        view.updateCont(this);
    }
    private Controller() {
        // Singleton
    }
    
    public static Controller create() {
        if (singleton == null) singleton = new Controller();
        return singleton;
    }

    public void notify(ObservableLudo o) {
        
    }

    public boolean movePiao(Cor c, int indice, int dado) {
        return model.tentaMoverPiao(c, indice, dado);
    }

    public int[][] getPosPioes () {
        Object[][] ob1 = (Object[][]) model.getPioes();
        int[][] ret = new int[4][4];
        for (int i = 0; i < 4; i++) for (int j = 0; j < 4; j++) ret[i][j] = (int) ob1[i][j];
        return ret;
    }

    public Cor getVez () {
        return model.getVez();
    }

    public int getDado () {
        return model.dadoAtual;
    }

    public int lancaDado () {
        return model.lancaDado();
    }

    public void refresh() {
        view.repaint();
    }

    public void novoJogo() {
        model.reset();
    }

    public void escreverJogo(FileWriter file) {
        model.escreverJogo(file);
    }

    public void salvarJogo() {
        JFrame parent = new JFrame();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Especifique arquivo para salvar:");   
        FileNameExtensionFilter filter = new FileNameExtensionFilter(".txt", "txt");
        fileChooser.setFileFilter(filter);

        int userSelection = fileChooser.showSaveDialog(parent);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            System.out.println("Salve arquivo: " + fileToSave.getAbsolutePath());
            try{
                FileWriter saidaTxt = new FileWriter(String.valueOf(fileToSave.getAbsolutePath()));
                model.escreverJogo(saidaTxt);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void carregarJogo() {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(".txt", "txt");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(null);
        List<String> listRead = new ArrayList<String>();
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();

            try {
                BufferedReader reader = new BufferedReader(new FileReader(selectedFile.getAbsolutePath()));
                String line;
                while ((line = reader.readLine()) != null) {
                    listRead.add(line);
                }
                reader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (!listRead.isEmpty()){
                model.set(listRead);
                this.refresh();
            }
            System.out.println("You chose to open this file: " + selectedFile.getName());
        }
    }


}
