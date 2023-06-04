package controller;
import cores.*;
import model.*;

public class Algoritmo {
	Model model = new Model();
	
    public void greedy(Cor corDoJogador) {
    	System.out.printf("\n------------------------------------\nJogador %s:\n\n", corDoJogador.toString());
    	int dado = model.lancaDado();
		if (dado == 0) return;
    	for (int i = 0; i < 4; i++) if (model.tentaMoverPiao(corDoJogador, i, dado)) break;
    }
}
