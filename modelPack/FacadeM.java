package modelPack;

import observer.*;

import java.io.FileWriter;
import java.util.*;
import cores.*;

public class FacadeM implements ObservableLudo{
    private static FacadeM singleton;
    private Model model = Model.create();

    private FacadeM () {
        // bloqueado
    }

    public static FacadeM getModel () {
        if (singleton == null) singleton = new FacadeM();
        return singleton;
    }

    public void addObserver (ObserverLudo o) {
        model.addObserver(o);
    }

    public Object get () {
        return model.get();
    }

    public void removeObserver (ObserverLudo o) {
        model.removeObserver(o);
    }
    
    public void reset () {
        model.reset();
    }

    public void set (List<String> lread) {
        model.set(lread);
    }

    public void escreverJogo (FileWriter saidaTxt) {
        model.escreverJogo(saidaTxt);
    }

    public int lancaDado () {
        return model.lancaDado();
    }

    public int lancaDado (int forcado) {
        return model.lancaDado(forcado);
    }

    public int tentaMoverPiao (Cor corPiao, int idPiao) {
        return model.tentaMoverPiao(corPiao, idPiao);
    }

    public void dump () {
        model.dump();
    }
}
