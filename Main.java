import cores.*;
// A Main serve para testar.
public class Main {
    public static void main (String[] args) {
        Guloso g = new Guloso();
        boolean jogoEmAndamento = true;
        while (jogoEmAndamento) {
        	for (Cor jogador: Cor.values()) {
        		g.greedy(jogador);
        		if (g.model.fimDoJogo()) {
        			jogoEmAndamento = false;
        			break;
        		}
        	}
        }
        /*
        model.movePiao(Cor.vermelho, 0, 6);
        model.movePiao(Cor.vermelho, 0, 6);
        model.movePiao(Cor.vermelho, 0, 6);
        model.movePiao(Cor.vermelho, 0, 6);
        */
    }
}
