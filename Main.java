import view.*;
import model.*;
import java.util.*;

// A Main serve para testar.
public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Frame f = new Frame();
        f.setVisible(true);
        Model model = Model.create();
        int dadoForcado = sc.nextInt();
        while (dadoForcado != 0) {
            model.lancaDado(dadoForcado);
            dadoForcado = sc.nextInt();
        }
    }
}