import cores.*;
// A Main serve para testar.
public class Main {
    public static void main (String[] args) {
        Algoritmo g = new Algoritmo();
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
    }
}
