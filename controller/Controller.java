package controller;

import model.Model;
import view.View;
import cores.*;


public class Controller {
    private static Controller singleton;
    public View view = View.create();
    Model model = Model.create();

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
        return model.tentaMoverPiao(c, indice, dado);
    }
    
    public int lancaDado () {
        return model.lancaDado();
    }

    public void novoJogo() {
        model.reset();
    }

    public void carregarJogo() {
        view.carregarJogo();
    }
}
