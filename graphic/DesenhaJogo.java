package graphic;
import javax.swing.*;

public class DesenhaJogo extends JFrame {
    //[mc] como acessar as constantes iguais a essas em panel direto?
    public static final int LARG_DEFAULT=1200;
    public static final int ALT_DEFAULT=700;

    public DesenhaJogo() {
        setSize(LARG_DEFAULT,ALT_DEFAULT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Super Ludo");
        getContentPane().add(new Panel());
    }
}

