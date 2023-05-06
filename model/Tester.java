package model;
import cores.*;

// Tester é um jeito tosco de testar se tudo tá funcionando. Não é pra estar no arquivo final.
class Tester {
	Tabuleiro t1;
    {
        t1 = new Tabuleiro();
        Piao r0 = t1.getPiao(Cor.vermelho,0);
        Piao r1 = t1.getPiao(Cor.vermelho,1);
        Piao r2 = t1.getPiao(Cor.vermelho,2);
        Piao r3 = t1.getPiao(Cor.vermelho,3);
        Piao y2 = t1.getPiao(Cor.amarelo,2);
        t1.search(r0).dump();System.out.printf("\n");
        t1.search(r1).dump();System.out.printf("\n");
        t1.search(r2).dump();System.out.printf("\n");
        t1.search(r3).dump();System.out.printf("\n");
        for (int i = 0; i < 57; i++) {
            this.testaMove(r1,1);
        }
        for (int i = 0; i < 57; i++) {
            this.testaMove(r2,1);
        }
        for (int i = 0; i < 57; i++) {
            this.testaMove(y2,1);
        }
        this.testaMove(r3,5);
        this.testaMove(r3,2);
        this.testaMove(r0,1);
        this.testaMove(r0,1);
        this.testaMove(r0,1);
        for (int i = 2; i < 57; i++) {
            this.testaMove(r3,1);
            this.testaMove(r0,1);
        }
    }
    
    protected void testaMove (Piao p, int i) {
    	System.out.printf("move(%s, %d) = %s\t", p.dumpString(),i,t1.move(p, i)?"TRUE ":"FALSE");
        t1.search(p).dump();
        System.out.printf("%s\n",(t1.barreiras.get(p.getCor().ordinal()).toString()));
    }
}