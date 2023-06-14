package model;

import cores.*;
import java.util.*;
import observer.*;

// Model é a fachada das regras, e é a única classe pública da 1a iteração.
public class Model implements ObservableLudo {
    public Tabuleiro tabuleiro = Tabuleiro.create();
    public Cor corVez = Cor.vermelho;
    boolean jogoAcabou = false;
    //public int qtdPioes[] = {0, 0, 0, 0}; // [tom] não sei o que isso era...
    private int qtdSeisRolados = 0;
    private Piao ultimoPiaoMovido = tabuleiro.arrayPioes[0][0];
    public int dadoAtual = 0;
    private static Model singleton;
    private boolean devePassarVez = true;
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
            //qtdPioes[i] = 0;
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
        System.out.println("Model.lancaDado(): fui chamada!");
        System.out.printf("Model.lancaDado(): Lançando dado normal: ", corVez.toString());
        int resultado = Dado.rolar();
        return lancaDado(resultado);
    }

    public int lancaDado (int forcado) {
        System.out.println("Model.lancaDado("+forcado+"): fui chamada!");
        System.out.printf("Model.lancaDado(%d): vez do %s!\n", forcado, corVez.toString());
        int resultado = forcado;
        dadoAtual = resultado;

        processo: {
            if (resultado == 6) {
                devePassarVez = false;
                qtdSeisRolados++;
                // System.out.println("--- point: cod 1 ---");
                if (qtdSeisRolados > 2) { // caso o jogador tenha rolado o 3o 6 seguido...
                    System.out.println("Model.lancaDado("+forcado+"): Cod1: qtd 6 > 2");

                    if (ultimoPiaoMovido.getPosicao() < 52) ultimoPiaoMovido.reset();
                    updateVez();
                    dadoAtual = 0;
                    break processo;
                }
                
                // System.out.println("--- point: cod 2 ---");
                Piao piaoBarreiraQuebravel = tabuleiro.getPiaoBarreiraQuebravel(corVez, resultado, dadoAtual==6 && devePassarVez);
                if (piaoBarreiraQuebravel != null) {
                    System.out.println("Model.lancaDado("+forcado+"): Cod2: barreira quebrada automaticamente");
                    tabuleiro.move(piaoBarreiraQuebravel, resultado, dadoAtual==6 && devePassarVez);

                    ultimoPiaoMovido = piaoBarreiraQuebravel;
                    //updateVez();
                    dadoAtual = 6;
                    break processo;
                }
            }
            

            else {
                devePassarVez = true;
                if (resultado == 5 && !tabuleiro.isEmptyInicial(corVez)) {
                    // System.out.println("--- point: cod 3 ---");
                    Piao p = tabuleiro.getInicial(corVez).getPiao();
                    if (tabuleiro.getInicial(corVez).getQtdPioes() > 0) {
                        int tentativaDeMovimento = tabuleiro.move(p,resultado, dadoAtual==6 && devePassarVez);
                        if (tentativaDeMovimento > 0) {
                            ultimoPiaoMovido = p;
                            if (tentativaDeMovimento > 1) {
                                dadoAtual = 6;
                                devePassarVez = true;
                                break processo;
                            }
                            else {
                                dadoAtual = 0;
                                updateVez();
                                break processo;
                            }
                        }
                    }
                }
            }
            
            // System.out.println("--- point: cod 4 ---");
            if (!tabuleiro.existeJogadaPermitida(corVez, resultado, dadoAtual==6 && devePassarVez)) { // se não existe nenhuma jogada possível...
                System.out.println("Model.lancaDado("+forcado+"): Cod4: sem jogadas");

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
            
            // System.out.println("--- point: cod 5 ---");
            if (tudoZerado && resultado != 5) {
                System.out.println("Model.lancaDado("+forcado+"): Cod5: nada pra iniciar");

                updateVez();
                dadoAtual = 0;
                break processo;
            }

            dadoAtual = resultado;
        }
        for (ObserverLudo o: lob) o.notify(this);
        return dadoAtual;
    }
    
    protected boolean fimDoJogo() {
        System.out.println("Model.fimDoJogo(): fui chamada!");
    	if (jogoAcabou) tabuleiro.termina();
    	return jogoAcabou;
    }
    
    // tentamoverPiao(corPiao, idPiao, casas) tenta mover o "idPiao-ésimo" Pião de cor "corPiao" "casas" casas para a frente. retorna TRUE em caso de sucesso e FALSE em caso de falha.
    public boolean tentaMoverPiao (Cor corPiao, int idPiao, int casas) {
        System.out.println("Model.tentaMoverPiao("+corPiao.toString()+","+idPiao+","+casas+"): fui chamada!");
        Piao p = tabuleiro.getPiao(corPiao, idPiao);
        int retorno = tabuleiro.move(p, dadoAtual, dadoAtual==6 && devePassarVez);
        jogoAcabou = !tabuleiro.getStatus();
        if (retorno != 0) {
            ultimoPiaoMovido = p;
            if (retorno == 2) {
                dadoAtual = 6;
                devePassarVez = true;
            }
            else if (devePassarVez) updateVez(); // se deu 6 no dado a vez não muda!!
            for (ObserverLudo o: lob) o.notify(this);
            return true;
        }
        return false;
    }

    protected void updateVez(){
        System.out.println("Model.updateVez(): fui chamada!");
        devePassarVez = true;
        corVez = Cor.values()[(corVez.ordinal()+1)%4];
        System.out.println("\n\nModel.updateVez(): Vez passada para o "+corVez.toString());
        for (ObserverLudo o: lob) o.notify(this);
        qtdSeisRolados = 0;
        dadoAtual = 0;
        for (ObserverLudo o: lob) o.notify(this);
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

    public void dump () {
        for (Cor c: Cor.values()) {
            System.out.printf("%s: ",c.toString());
            for (int i = 0; i < 4; i++) {
                System.out.printf("%d, ", tabuleiro.arrayPioes[c.ordinal()][i].getPosicao());
            }
            System.out.println(";");
        }

        tabuleiro.tabudump();
    }
}