import cores.*;
import regras.*;

public class Guloso {
	Model model = new Model();
	
    public void greedy(Cor corDoJogador) {
    	System.out.printf("\n------------------------------------\nJogador %s:\n\n", corDoJogador.toString());
    	int dado = Model.lancaDado();
    	if (!model.podeJogar(corDoJogador, dado)) return;
    	for (int i = 0; i < 4; i++) if (model.movePiao(corDoJogador, i, dado)) break;
    }
}
