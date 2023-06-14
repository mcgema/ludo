package model;

import cores.*;
import java.util.*;
import observer.*;

// Model é a fachada das regras, e é a única classe pública da 1a iteração.
public class Model implements ObservableLudo {
    public Tabuleiro tabuleiro = Tabuleiro.create();
    public Cor corVez = Cor.vermelho;
    boolean jogoAcabou = false;
    public int qtdPioes[] = {0, 0, 0, 0};
    private int qtdSeisRolados = 0;
    private Piao ultimoPiaoMovido = tabuleiro.arrayPioes[0][0];
    public int dadoAtual = 0;
    private static Model singleton;
    List<ObserverLudo> lob = new ArrayList<ObserverLudo>();

    private Model() {
        // construtor bloqueado pelo singleton
    }

    public static Model create () {
        if (singleton == null) singleton = new Model();
        return singleton;
    }

    public void reset() {
        for (Cor c: Cor.values()) for (int i = 0; i < 4; i++) {
            tabuleiro.arrayPioes[c.ordinal()][i].reset();
            qtdPioes[i] = 0;
        }
        corVez = Cor.vermelho;
        jogoAcabou = false;
        qtdSeisRolados = 0;
        ultimoPiaoMovido = tabuleiro.arrayPioes[0][0];
        dadoAtual = 0;
        for (ObserverLudo o: lob) o.notify(this);
    }
    
    // lancaDado() lanca um dado virtual de 6 lados, retornando um inteiro dentre {1, 2, 3, 4, 5, 6} com chance pseudo-aleatória.
    // também realiza jogadas forçadas, retornando 0 caso ocorram.
    public int lancaDado () {
        System.out.printf("Lançando dado normal: ", corVez.toString());
        int resultado = Dado.rolar();
        return lancaDado(resultado);
    }

    public int lancaDado (int forcado) {
        System.out.printf("Model.lancaDado(): vez do %s!\n", corVez.toString());
        int resultado = forcado;
        dadoAtual = resultado;

        processo: {
            if (resultado == 6) {
                qtdSeisRolados++;
                if (qtdSeisRolados > 2) { // caso o jogador tenha rolado o 3o 6 seguido...
                    System.out.println("Cod1: qtd 6 > 2");

                    ultimoPiaoMovido.reset();
                    updateVez();
                    dadoAtual = 0;
                    break processo;
                }
                
                Piao piaoBarreiraQuebravel = tabuleiro.getPiaoBarreiraQuebravel(corVez, resultado);
                if (piaoBarreiraQuebravel != null) {
                    System.out.println("Cod2: barreira quebrada automaticamente");
                    tabuleiro.move(piaoBarreiraQuebravel, resultado);

                    ultimoPiaoMovido = piaoBarreiraQuebravel;
                    updateVez();
                    dadoAtual = 0;
                    break processo;
                }
            }

            else if (resultado == 5) {
                Piao p = tabuleiro.getInicial(corVez).getPiao();
                if (tabuleiro.getInicial(corVez).getQtdPioes() > 0 && tabuleiro.move(p,1)) { // se um pião pôde ser iniciado...
                    System.out.println("Cod3: 5 => inicia piao");

                    ultimoPiaoMovido = p;
                    updateVez();
                    dadoAtual = 0;
                    return 0;
                }
            }

            if (!tabuleiro.existeJogadaPermitida(corVez, resultado)) { // se não existe nenhuma jogada possível...
                System.out.println("Cod4: sem jogadas");

                updateVez();
                dadoAtual = 0;
                break processo;
            }

            boolean tudoZerado = true;

            for (int i = 0; i < 4; i++) {
                if (tabuleiro.arrayPioes[corVez.ordinal()][i].getPosicao() != 0) {
                    tudoZerado = false;
                    break;
                }
            }

            if (tudoZerado && resultado != 5) {
                System.out.println("Cod5: nada pra iniciar");

                updateVez();
                dadoAtual = 0;
                break processo;
            }

            dadoAtual = resultado;
        }
        for (ObserverLudo o: lob) o.notify(this);
        return dadoAtual;
    }
    
    public boolean fimDoJogo() {
    	if (jogoAcabou) tabuleiro.termina();
    	return jogoAcabou;
    }
    
    // tentamoverPiao(corPiao, idPiao, casas) tenta mover o "idPiao-ésimo" Pião de cor "corPiao" "casas" casas para a frente. retorna TRUE em caso de sucesso e FALSE em caso de falha.
    public boolean tentaMoverPiao (Cor corPiao, int idPiao, int casas) {
        Piao p = tabuleiro.getPiao(corPiao, idPiao);
        boolean retorno = tabuleiro.move(p, dadoAtual);
        jogoAcabou = !tabuleiro.getStatus();
        if (retorno) {
            ultimoPiaoMovido = p;
            for (ObserverLudo o: lob) o.notify(this);;
            if (casas != 6) updateVez();// se deu 6 no dado a vez não muda!!
        }
        return retorno;
    }

    public void updateVez(){
        corVez = Cor.values()[(corVez.ordinal()+1)%4];
        System.out.println("Vez passada para o "+corVez.toString());
        for (ObserverLudo o: lob) o.notify(this);;
        qtdSeisRolados = 0;
        dadoAtual = 0;
        for (ObserverLudo o: lob) o.notify(this);;

    }

    // interface Observable

	public void addObserver(ObserverLudo o) {
		lob.add(o);
	}
	
	public void removeObserver(ObserverLudo o) {
		lob.remove(o);
	}

    public Object get() {
        Object[] data = new Object[3];

        Object listaPioes[][] = new Object[4][4];
        for (Cor cor: Cor.values()) {
            for (int i = 0; i < 4; i++) {
                listaPioes[cor.ordinal()][i] = tabuleiro.arrayPioes[cor.ordinal()][i].getPosicao();
            }
        }

        Object vez = corVez;
        Object dado = dadoAtual;

        data[0] = listaPioes;
        data[1] = vez;
        data[2] = dado;

        return data;
    }
}