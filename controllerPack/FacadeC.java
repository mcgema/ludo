package controllerPack;

import observer.*;
import cores.*;

public class FacadeC {
    Controller controller = Controller.create();
    private static FacadeC singleton;

    private FacadeC () {
        // bloqueado pelo singleton
    }

    public static FacadeC getController () {
        if (singleton == null) singleton = new FacadeC();
        return singleton;
    }

    public int lancaDado () {
        return controller.lancaDado();
    }

    public int lancaDado (int num) {
        return controller.lancaDado(num);
    }

    public void addObserver (ObserverLudo o) {
        controller.addObserver(o);
    }

    public boolean movePiao (Cor c, int indice) {
        return controller.movePiao (c, indice);
    }

    public void novoJogo () {
        controller.novoJogo();
    }

    public void salvarJogo () {
        controller.salvarJogo();
    }

    public void carregarJogo () {
        controller.carregarJogo();
    }

    public void debug () {
        //controller.debug();
    }

    public void comecaJogo () {
        controller.comecaJogo();
    }
}
