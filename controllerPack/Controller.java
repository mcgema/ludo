package controllerPack;

import cores.*;
import modelPack.FacadeM;

import javax.swing.*;
import observer.*;

import java.util.List;
import java.util.ArrayList;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileWriter;

class Controller {
    private static Controller singleton;
    private FacadeM model = FacadeM.getModel();
    private boolean jogadorJaJogou = false;
    {
        novoJogo();
    }

    private Controller() {
        // Construtor bloqueado pelo Singleton
    }
    
    protected static Controller create() {
        if (singleton == null) singleton = new Controller();
        return singleton;
    }

    protected void addObserver (ObserverLudo o) {
        model.addObserver(o);
    }

    protected boolean movePiao(Cor c, int indice) {
        if (!jogadorJaJogou) return false;
        int resultado = model.tentaMoverPiao(c, indice);
        if (resultado == 1) {
            jogadorJaJogou = false;
        }
        return (resultado > 0);
    }
    
    protected int lancaDado () {
        int resultado = model.lancaDado();
        jogadorJaJogou = true;
        return resultado;
    }

    protected int lancaDado (int forcado) {
        int resultado = model.lancaDado(forcado);
        jogadorJaJogou = true;
        return resultado;
    }

    protected void novoJogo() {
        model.reset();
    }

    protected void salvarJogo() {
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

    protected void carregarJogo() {
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
            }
        }
    }

    protected void setBotaoDado (JButton botao) {
        //botaoDado = botao;
    }

    /*
    public void debug () {
        System.out.println("View:");
        view.dump();
        System.out.println("\nModel:");
        model.dump();
    }
    */

}