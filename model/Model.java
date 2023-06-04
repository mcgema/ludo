package model;

import cores.*;

// Model é a fachada das regras, e é a única classe pública da 1a iteração.
public class Model {
    public Tabuleiro tabuleiro = new Tabuleiro();
    public Cor vez = Cor.valueOf("vermelho");
    boolean jogoAcabou = false;
    public int qtdPeaos[] = {0, 0, 0, 0};
    private int qtdSeisRolados = 0;
    private Piao ultimoPiaoMovido = null;
    {
    	System.out.printf("\n\n\n");
    }
    // movePiao(corPiao, idPiao, casas) tenta mover o "idPiao-ésimo" Pião de cor "corPiao" "casas" casas para a frente. retorna TRUE em caso de sucesso e FALSE em caso de falha.
    public boolean movePiao (Cor corPiao, int idPiao, int casas) {
    	Piao p = tabuleiro.getPiao(corPiao, idPiao);
		//System.out.printf("\n>>> move(%s, %d) = ", p.dumpString(),casas);
    	boolean retorno = tabuleiro.move(p, casas);
		//System.out.printf("%s\t",retorno?"permitido (T):":"proibido (F):");
		//tabuleiro.search(p).dump();
		//System.out.printf("%s\n",(tabuleiro.barreiras.get(p.getCor().ordinal()).toString()));
		
		jogoAcabou = !tabuleiro.getStatus();
        if (retorno) ultimoPiaoMovido = p;
        return retorno;
    }
    
    // lancaDado() lanca um dado virtual de 6 lados, retornando um inteiro dentre {1, 2, 3, 4, 5, 6} com chance pseudo-aleatória.
    public int lancaDado () {
        int resultado = Dado.rolar();
        if (resultado == 6) {
            qtdSeisRolados++;
            if (qtdSeisRolados > 2) {
                // caso o joagor tenha rolado o 3o 6 seguido...
                ultimoPiaoMovido.reset();
                updateVez();
                return 0;
            }
            if (tabuleiro.barreiras.get(vez.ordinal()).size() > 1) {
                System.out.println("incompleto");
            }
        }
        else if (resultado == 5) {
            if (tabuleiro.getInicial(vez).getQtdPioes() > 0 && tabuleiro.move(tabuleiro.getInicial(vez).getPiao(),1)) {
                updateVez();
                return 0;
            }
        }
        return 99;
    }

    public boolean podeJogar (Cor corDoJogador, int resultadoDado) {
    	return tabuleiro.jogadorPodeJogar(corDoJogador, resultadoDado);
    }
    
    public boolean fimDoJogo() {
    	if (jogoAcabou) tabuleiro.termina();
    	return jogoAcabou;
    }

    public boolean tentaMoverPiao (Cor corPiao, int idPiao, int casas) {
        return movePiao(corPiao,idPiao,casas);
    }

    // comunicacao com graphics
    public int posicaoPiao(Cor c, int i){
        Piao p = tabuleiro.getPiao(c, i);
        return p.getPosicao();
    }


    public Cor updateVez(){
        vez = Cor.values()[(vez.ordinal()+1)%4];
        return vez;
    }

    public Cor getVez(){
        return vez;
    }

    public Casa[][] getTabuleiros(){
        return tabuleiro.getTabuleiro();
    }

    public Casa converteCoordenadas(int x, int y, int LADO) { //[mc] funciona apenas para cor vermelha!!!!
        int indiceJogador = vez.ordinal();
        Casa[] tabuleiroJogador = getTabuleiros()[indiceJogador];
        int tabuleiroTamanho = 56;
    
        //  verifica se apertou em coordenas que pode se jogar
        if (x < 0 || y < 0 || x > 15 * LADO || y > 15 * LADO) {
            return null; // retorna null se coordenadas fora de área de jogo
        }
    
        // ajuste das coordenadas para área de jogo
        x -= LADO;
        y -= LADO;
    
        // Calcula posicao baseada nas coordenas clicadas
        int posicao = -1;
        int coluna = x / LADO;
        int linha = y / LADO;
    
        // parte do oeste 
        if (coluna < 6 && linha >= 6 && linha <= 8) {
            if (linha == 6) posicao += coluna;
            else if (linha == 7) posicao += 51 + coluna;
            else posicao += 50 - coluna;
        } 
        // parte do norte
        else if (coluna >= 6 && coluna <= 8 && linha < 6) {
            if (coluna == 6) posicao += 12 - linha;
            else if (coluna == 7 && linha == 0) posicao += 13;
            else if (coluna == 8) posicao += 14 + linha;
        } 
        // parte do leste
        else if (coluna >= 9 && linha >= 6 && linha <= 8) {
            if (linha == 6) posicao += 20 + coluna - 9;
            else if (linha == 7 && coluna == 14) posicao += 26;
            else if (linha == 8) posicao += 27 +  14 - coluna;
        } 
        // parte do sul
        else if (coluna >= 6 && coluna <= 8 && linha > 8) {
            if (coluna == 8) posicao += 33 + linha - 8;
            else if (coluna == 7 && linha == 14) posicao += 39;
            else if (coluna == 6) posicao += 40 + 14 - linha;
        }
    
        if (posicao >= 0 && posicao < tabuleiroTamanho) {
            return tabuleiroJogador[posicao];
        } else {
            return null; // returna null para posicoes invalidas
        }
    }
    
}
