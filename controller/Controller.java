package controller;

import model.Model;
import view.View;
import observer.*;
import cores.*;


public class Controller implements ObserverTom {
    private static Controller singleton;
    public View view = View.create();
    Model model = Model.iniciaModel();
    {
        model.addObserver(this);
        view.updateCont(this);
    }
    private Controller() {

    }
    public static Controller create() {
        if (singleton == null) singleton = new Controller();
        return singleton;
    }

    public void notify(ObservableIF o) {
        //[mc] nao entendi e funciona sem
       // int[][] listaPioes = (int[][]) o.getPioes();
       // view.updatePioes(listaPioes);
        //System.out.printf("\n Controller.model.corVez=%s\n", model.getVez().toString());

        //view.updateVez(model.getVez());
    }

    public boolean movePiao(Cor c, int indice, int pos, int dado) {
        //System.out.printf("Controller.movePiao(%s)\n", c.toString());
        return model.tentaMoverPiao(c, indice, pos, dado);
    }

    public int[][] getPosPioes () {
        return model.getPosPioes();
    }

    public Cor getVez () {
        return model.getVez();
    }
}
