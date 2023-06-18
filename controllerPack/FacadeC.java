package controllerPack;

import java.awt.Container;

import javax.swing.JButton;
import observer.*;
import cores.*;

public class FacadeC {
    Controller controller = Controller.create();
    private static FacadeC singleton;

    {
        controller.view.updateController(this);
    }

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

    public void addContentPane (Container c) {
        c.add(controller.view);
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

    public void setBotaoDado (JButton b) {
        controller.setBotaoDado(b);
    }

    public void debug () {
        controller.debug();
    }
}
