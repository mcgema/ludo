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
        //[mc] nao entendi e funciona sem
       // int[][] listaPioes = (int[][]) o.getPioes();
       // view.updatePioes(listaPioes);
        //model.updateVez();
    }

    public boolean movePiao(Cor c, int indice,  int dado) {
        // System.out.printf("cor movePiao: %s", c.toString());
        return model.tentaMoverPiao(c, indice, dado);
    }

    public int tentaIniciarMover (Cor c, int dado){
        return model.tentaIniciarMover (c, dado);
    }
}
