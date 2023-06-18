package viewPack;

import javax.swing.JButton;

import controllerPack.FacadeC;
import observer.ObservableLudo;
import observer.ObserverLudo;

public class FacadeV implements ObserverLudo{
    private static FacadeV singleton;
    private View view = View.create();
    private Frame frame = Frame.getFrame();
    private FacadeC controller = FacadeC.getController();

    {
        frame.setVisible(true);
        controller.addObserver(this);
    }

    private FacadeV () {
        //bloqueado
    }

    public static FacadeV getView () {
        if (singleton == null) singleton = new FacadeV();
        return singleton;
    }

    public void notify (ObservableLudo o) {
        view.notify(o);
        frame.notify(o);
    }

    public void dump () {
        view.dump();
    }

    public JButton getBotaoDado () {
        return frame.getBotaoDado();
    }
}