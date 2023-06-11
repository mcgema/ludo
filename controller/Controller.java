package controller;

import model.Model;
import view.View;
import observer.*;
import cores.*;


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

    public boolean movePiao(Cor c, int indice, int pos, int dado) {
        return model.tentaMoverPiao(c, indice, pos, dado);
    }

    public int[][] getPosPioes () {
        return model.getPosPioes();
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
}
