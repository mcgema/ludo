import regras.*;
import cores.*;

// A Main serve para testar.
// (Tomaz) Usar Model em vez de Tester para testar DEPOIS DE MODEL ESTAR COMPLETA!!!
public class Main {
    public static void main (String[] args) {
        Tabuleiro jogo = new Tabuleiro();
        Tester teste = new Tester();
        Model model = new Model();
        
        
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
