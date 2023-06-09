import view.*;
//import model.*;
import controller.*;
import java.util.*;

// A Main serve para testar.
public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Frame f = new Frame();
        f.setVisible(true);
        Controller controller = Controller.create();
        controller.novoJogo();
        int dadoForcado = sc.nextInt();
        while (dadoForcado != 0) {
            /*
            if (dadoForcado == 10) {
                System.out.println("View:");
                View.create().dump();
                System.out.println("\nModel:");
                Model.create().dump();
            }
            else */{
                controller.lancaDado(dadoForcado);
            }
            dadoForcado = sc.nextInt();
        }
        sc.close();
    }
}