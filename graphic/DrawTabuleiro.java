package graphic;
import javax.swing.*;

public class DrawTabuleiro extends JFrame {
    final int LARG_DEFAULT=1200;
    final int ALT_DEFAULT=700;

    public DrawTabuleiro() {
        setSize(LARG_DEFAULT,ALT_DEFAULT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Super Ludo");
        getContentPane().add(new Panel());
    }
}

