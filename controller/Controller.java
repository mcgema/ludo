package controller;

import model.Model;
import view.View;
import observer.*;
import cores.*;


public class Controller implements ObserverTom{
    public View view = new View();
    Model model = new Model();
    {
        model.addObserver(this);
        view.updateCont(this);
    }

    public void notify(ObservableIF o) {
        int[][] listaPioes = (int[][]) o.getPioes();
        view.updatePioes(listaPioes);
        view.updateVez(model.getVez());
    }

    public void movePiao(Cor c, int pos, int dado) {
        model.tentaMoverPiao(c, pos, dado);
    }
}
