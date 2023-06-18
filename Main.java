import java.util.*;

import controllerPack.FacadeC;
import viewPack.*;

// A Main serve para testar.
public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        FacadeV view = FacadeV.getView();
        FacadeC controller = FacadeC.getController();
        controller.novoJogo();
        int dadoForcado = sc.nextInt();
        while (dadoForcado > 0) {
            
            if (dadoForcado == 10) controller.debug();
            else controller.lancaDado(dadoForcado);

            dadoForcado = sc.nextInt();
        }
        sc.close();
    }
}