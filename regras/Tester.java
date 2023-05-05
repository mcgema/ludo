package regras;
import cores.*;

// Tester é um jeito tosco de testar se tudo tá funcionando. Não é pra estar no arquivo final.
public class Tester {
    {
        Tabuleiro t1 = new Tabuleiro();
        Piao r0 = t1.getPiao(Cor.vermelho,0);
        Piao r1 = t1.getPiao(Cor.vermelho,1);
        Piao r2 = t1.getPiao(Cor.vermelho,2);
        Piao r3 = t1.getPiao(Cor.vermelho,3);
        Piao y2 = t1.getPiao(Cor.amarelo,2);
        Casa cr1 = t1.search(r1);
        cr1.dump();
        for (int i = 0; i < 57; i++) {
            t1.move(r1,1);
            t1.search(r1).dump();
            System.out.println(t1.barreiras.get(Cor.vermelho.ordinal()).toString());
        }
        for (int i = 0; i < 57; i++) {
            t1.move(r2,1);
            t1.search(r2).dump();
            System.out.println(t1.barreiras.get(Cor.vermelho.ordinal()).toString());
        }
        for (int i = 0; i < 57; i++) {
            t1.move(y2,1);
        }
        for (int i = 0; i < 57; i++) {
            t1.move(r3,1);
            t1.move(r0,1);
            t1.search(r3).dump();
            System.out.println(t1.barreiras.get(Cor.vermelho.ordinal()).toString());
        }
    }
}