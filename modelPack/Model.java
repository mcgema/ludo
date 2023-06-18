package modelPack;

import cores.*;

import java.io.FileWriter;
import java.io.IOException;

import java.util.*;
import observer.*;

// Model é a fachada das regras, e é a única classe pública da 1a iteração.
class Model implements ObservableLudo {
    public Tabuleiro tabuleiro = Tabuleiro.create();
    public Cor corVez = Cor.vermelho;
    private int qtdSeisRolados = 0;
    private Piao ultimoPiaoMovido = tabuleiro.arrayPioes[0][0];
    public int dadoAtual = 0;
    private static Model singleton;
    private boolean bonusDeCaptura = false;
    List<ObserverLudo> lob = new ArrayList<ObserverLudo>();

    private Model() {
        // construtor bloqueado pelo singleton
    }

    protected static Model create () {
        if (singleton == null) singleton = new Model();
        return singleton;
    }

    protected void reset() {
        for (Cor c: Cor.values()) for (int i = 0; i < 4; i++) {
            System.out.printf("Casa inicial do %8s: %d -> ",c.toString(),tabuleiro.tabuleiro[c.ordinal()][1].getQtdPioes());
            tabuleiro.arrayPioes[c.ordinal()][i].reset();
            System.out.printf("%d peões\n",tabuleiro.tabuleiro[c.ordinal()][1].getQtdPioes());
        }
        dump();
        corVez = Cor.vermelho;
        qtdSeisRolados = 0;
        ultimoPiaoMovido = tabuleiro.arrayPioes[0][0];
        dadoAtual = 0;
        bonusDeCaptura = false;
        for (ObserverLudo o: lob) o.notify(this);
        for (int i = 0; i < 4; i++) this.lancaDado(5);
    }

    protected void set(List<String> lread){
        if (lread.size() == 21){
            corVez = Cor.valueOf(lread.get(0));
            for (int i=0; i<4; i++){
                // ordem obedece cor
                Cor cor = Cor.valueOf(lread.get(5*i+1));
                for (int j = 0; j < 4; j++) {
                    tabuleiro.setPiao(tabuleiro.arrayPioes[cor.ordinal()][j], Integer.valueOf(lread.get(5*i+j+2)));
                }
            }
        }
        for (ObserverLudo o: lob) o.notify(this);
    }
    
    protected void escreverJogo(FileWriter saidaTxt) {
        try {
            saidaTxt.write(corVez.toString());
            saidaTxt.write(System.lineSeparator());
            for (Cor c: Cor.values()) {
                saidaTxt.write(c.toString());
                saidaTxt.write(System.lineSeparator());
                for (int i = 0; i < 4; i++) {
                    saidaTxt.write(String.valueOf(tabuleiro.arrayPioes[c.ordinal()][i].getPosicao()));
                    saidaTxt.write(System.lineSeparator());
                }
            }
            saidaTxt.flush();
            saidaTxt.close();
        }  catch (IOException error) {
            error.printStackTrace();
        }
    }

    // lancaDado() lanca um dado virtual de 6 lados, retornando um inteiro dentre {1, 2, 3, 4, 5, 6} com chance pseudo-aleatória.
    // também realiza jogadas forçadas, retornando 0 caso ocorram.
    protected int lancaDado () {
        System.out.println("Model.lancaDado(): fui chamada!");
        System.out.printf("Model.lancaDado(): Lançando dado normal: ", corVez.toString());
        int resultado = Dado.rolar();
        return lancaDado(resultado);
    }

    protected int lancaDado (int forcado) {
        if (forcado > 6) return 0;
        if (forcado < 0) return 0;
        System.out.println("Model.lancaDado("+forcado+"): fui chamada!");
        System.out.printf("Model.lancaDado(%d): vez do %s!\n", forcado, corVez.toString());
        dadoAtual = forcado;
        tentaJogadaForcada();
        return dadoAtual;
    }

    protected int tentaJogadaForcada () {
        processo: {
            if (dadoAtual == 6) {
                qtdSeisRolados++;
                if (qtdSeisRolados > 2) { // caso o jogador tenha rolado o 3o 6 seguido...
                    System.out.println("Model.lancaDado("+dadoAtual+"): Cod1: qtd 6 > 2");

                    if (ultimoPiaoMovido.getPosicao() < 52) ultimoPiaoMovido.reset();

                    updateVez();
                    break processo;
                }
                
                Piao piaoBarreiraQuebravel = tabuleiro.getPiaoBarreiraQuebravel(corVez, dadoAtual, false);
                if (piaoBarreiraQuebravel != null) {
                    System.out.println("Model.lancaDado("+dadoAtual+"): Cod2: barreira quebrada automaticamente");

                    tentaMoverPiao(piaoBarreiraQuebravel.getCor(), piaoBarreiraQuebravel.getIndice(), 6);   // quebra barreira

                    //updateVez();  // essa linha faz com que uma barreira desfeita deixe jogar de novo mesmo assim por ser um 6.

                    break processo;
                }
            }
            

            else {
                if (dadoAtual == 5 && !tabuleiro.isEmptyInicial(corVez)) {

                    Piao p = tabuleiro.getInicial(corVez).getPiao();
                    if (tabuleiro.getInicial(corVez).getQtdPioes() > 0) {
                        tentaMoverPiao(p.getCor(), p.getIndice(), dadoAtual);
                        break processo;
                    }
                }
            }
            
            if (!tabuleiro.existeJogadaPermitida (corVez, dadoAtual, bonusDeCaptura)) { // se não existe nenhuma jogada possível...
                System.out.println("Model.lancaDado("+dadoAtual+"): Cod4: sem jogadas");

                updateVez();
                break processo;
            }

            boolean tudoZerado = true;

            for (int i = 0; i < 4; i++) {
                if (tabuleiro.arrayPioes[corVez.ordinal()][i].getPosicao() != 0) {
                    tudoZerado = false;
                    break;
                }
            }
            
            if (tudoZerado && dadoAtual != 5) {
                System.out.println("Model.lancaDado("+dadoAtual+"): Cod5: nada pra iniciar");

                updateVez();
                break processo;
            }
        }
        for (ObserverLudo o: lob) o.notify(this);
        return dadoAtual;

    }
    
    protected int tentaMoverPiao (Cor corPiao, int idPiao, int casas) {
        return tentaMoverPiao (corPiao, idPiao);
    }

    // tentamoverPiao(corPiao, idPiao, casas) tenta mover o "idPiao-ésimo" Pião de cor "corPiao" "casas" casas para a frente. retorna TRUE em caso de sucesso e FALSE em caso de falha.
    protected int tentaMoverPiao (Cor corPiao, int idPiao) {
        System.out.println("Model.tentaMoverPiao("+corPiao.toString()+","+idPiao+","+dadoAtual+"): fui chamada!");
        Piao p = tabuleiro.getPiao(corPiao, idPiao);
        int retorno = tabuleiro.move(p, dadoAtual, bonusDeCaptura);
        if (retorno != 0) {
            ultimoPiaoMovido = p;
            if (retorno == 2) {
                bonusDeCaptura = true;
                dadoAtual = 6;
            }
            else {
                if (dadoAtual != 6 || bonusDeCaptura) {
                    updateVez(); // se deu 6 no dado a vez não muda!!
                    bonusDeCaptura = false;
                }
                else /* dadoAtual == 6 */ dadoAtual = 0;
            }
            for (ObserverLudo o: lob) o.notify(this);
        }
        return retorno;
    }

    protected void updateVez () {
        System.out.println("Model.updateVez(): fui chamada!");

        corVez = Cor.values()[(corVez.ordinal()+1)%4];
        System.out.println("\n\n-------------------------------------------------------\nModel.updateVez(): Vez passada para o "+corVez.toString()+"\n-------------------------------------------------------\n\n");

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

    protected void dump () {
        for (Cor c: Cor.values()) {
            System.out.printf("%8s:\t",c.toString());
            for (int i = 0; i < 4; i++) {
                System.out.printf("%2d, ", tabuleiro.arrayPioes[c.ordinal()][i].getPosicao());
            }
            System.out.println(";");
        }

        tabuleiro.tabudump();
    }
}