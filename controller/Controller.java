package controller;

import model.Model;
import view.View;
import cores.*;
import javax.swing.*;


public class Controller {
    private static Controller singleton;
    public View view = View.create();
    Model model = Model.create();
    JButton botaoDado = null;

    {
        model.addObserver(view);
        view.updateCont(this);
    }

    private Controller() {
        // Construtor bloqueado pelo Singleton
    }
    
    public static Controller create() {
        if (singleton == null) singleton = new Controller();
        return singleton;
    }

    public boolean movePiao(Cor c, int indice, int dado) {
        int resultado = model.tentaMoverPiao(c, indice, dado);
        if (resultado == 1) botaoDado.setEnabled(true);
        return (resultado > 0);
    }
    
    public int lancaDado () {
        int resultado = model.lancaDado();
        if (resultado != 0) botaoDado.setEnabled(false);
        return resultado;
    }

    public int lancaDado (int forcado) {
        int resultado = model.lancaDado(forcado);
        if (resultado != 0) botaoDado.setEnabled(false);
        return resultado;
    }

    public void novoJogo() {
        model.reset();
        botaoDado.setEnabled(true);
    }

    public void carregarJogo() {
        view.carregarJogo();
    }

    public void setBotaoDado (JButton botao) {
        botaoDado = botao;
    }
}
