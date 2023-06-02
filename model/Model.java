package model;

import cores.*;

// Model é a fachada das regras, e é a única classe pública da 1a iteração.
public class Model {
    public Tabuleiro tabuleiro = new Tabuleiro();
    public Cor vez = Cor.valueOf("vermelho");
    boolean jogoAcabou = false;
    {
    	System.out.printf("\n\n\n");
    }
    // movePiao(corPiao, idPiao, casas) tenta mover o "idPiao-ésimo" Pião de cor "corPiao" "casas" casas para a frente. retorna TRUE em caso de sucesso e FALSE em caso de falha.
    public boolean movePiao (Cor corPiao, int idPiao, int casas) {
    	Piao p = tabuleiro.getPiao(corPiao, idPiao);
		System.out.printf("\n>>> move(%s, %d) = ", p.dumpString(),casas);
    	boolean retorno = tabuleiro.move(p, casas);
		System.out.printf("%s\t",retorno?"permitido (T):":"proibido (F):");
		tabuleiro.search(p).dump();
		System.out.printf("%s\n",(tabuleiro.barreiras.get(p.getCor().ordinal()).toString()));
		
		jogoAcabou = !tabuleiro.getStatus();
        return retorno;
    }
    
    // lancaDado() lanca um dado virtual de 6 lados, retornando um inteiro dentre {1, 2, 3, 4, 5, 6} com chance pseudo-aleatória.
    public static int lancaDado () {
        return Dado.rolar();
    }

    public boolean podeJogar (Cor corDoJogador, int resultadoDado) {
    	return tabuleiro.jogadorPodeJogar(corDoJogador, resultadoDado);
    }
    
    public boolean fimDoJogo() {
    	if (jogoAcabou) tabuleiro.termina();
    	return jogoAcabou;
    }

    // comunicacao com graphics
    public int posicaoPiao(Cor c, int i){
        Piao p = tabuleiro.getPiao(c, i);
        return p.getPosicao();
    }
    public Cor updateVez(){
        Cor c = vez;
        if (c.ordinal() < 3){
            vez = Cor.values()[c.ordinal()+1];
        }
        else {
            vez = Cor.values()[0];
        }
        return vez;
    }

    public Cor getVez(){
        return vez;
    }
}
