import regras.*;
import cores.*;

// A Main serve para testar.
// (Tomaz) Usar Model em vez de Tester para testar DEPOIS DE MODEL ESTAR COMPLETA!!!
public class Main {
    public static void main (String[] args) {
        Model jogo = new Model();
        Tester teste = new Tester();

        for (int i = 0; i < 65; i++) {
            jogo.movePiao(Cor.vermelho, 0, 1);
        }
        for (int i = 0; i < 65; i++) {
            jogo.movePiao(Cor.amarelo, 0, 1);
        }
        for (int i = 0; i < 65; i++) {
            jogo.movePiao(Cor.vermelho, 1, 1);
            jogo.movePiao(Cor.vermelho, 2, 1);
        }
        for (int i = 0; i < 65; i+=5) {
            jogo.movePiao(Cor.vermelho, 3, 1);
        }
        for (int i = 0; i < 65; i+=5) {
            jogo.movePiao(Cor.azul, 3, 1);
        }
    }
}
