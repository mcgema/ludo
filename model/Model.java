package model;

import cores.*;

import java.io.FileWriter;
import java.io.IOException;

import java.util.*;
import observer.*;
import controller.*;

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
    private Controller cont;

    {
    	System.out.printf("Model iniciado!\n\n\n");
    }

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
            corVez = Cor.vermelho;
            jogoAcabou = false;
            qtdPioes[0] = 0;
            qtdPioes[1] = 0;
            qtdPioes[2] = 0;
            qtdPioes[3] = 0;
            qtdSeisRolados = 0;
            ultimoPiaoMovido = tabuleiro.arrayPioes[0][0];
            dadoAtual = 0;
        }
    }

    public void set(List<String> lread){
        if (lread.size() == 25){
            corVez = Cor.valueOf(lread.get(0));
            for (int i=0; i<4; i++) qtdPioes[i] = Integer.valueOf(lread.get(i+1));
            for (int i=0; i<4; i++){
                // ordem obedece cor
                Cor cor = Cor.valueOf(lread.get(5*(i+1)));
                for (int j = 0; j < 4; j++) {
                    tabuleiro.arrayPioes[cor.ordinal()][j].setPosicao(Integer.valueOf(lread.get(5*(i+1)+j+1)));
                }
            }
        }
    }
    
    public void escreverJogo(FileWriter saidaTxt) {
        try{
            saidaTxt.write(corVez.toString());
            saidaTxt.write(System.lineSeparator());
            saidaTxt.write(String.valueOf(qtdPioes[0]));
            saidaTxt.write(System.lineSeparator());
            saidaTxt.write(String.valueOf(qtdPioes[1]));
            saidaTxt.write(System.lineSeparator());
            saidaTxt.write(String.valueOf(qtdPioes[2]));
            saidaTxt.write(System.lineSeparator());
            saidaTxt.write(String.valueOf(qtdPioes[3]));
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
    public int lancaDado () {
        System.out.printf("Model.lancaDado(): vez do %s!\n", corVez.toString());
        int resultado = Dado.rolar();
        dadoAtual = resultado;
        if (resultado == 6) {
            qtdSeisRolados++;
            if (qtdSeisRolados > 2) {
                // caso o joagor tenha rolado o 3o 6 seguido...
                ultimoPiaoMovido.reset();
                updateVez();
                System.out.println("Cod1: qtd 6 > 2");
                dadoAtual = 0;
                return 0;
            }
            int qtdBarreiras = tabuleiro.barreiras.get(corVez.ordinal()).size();
            if (qtdBarreiras > 0) {
                Piao piaoQuebrado = null;
                Iterator<Casa> iterator = tabuleiro.barreiras.get(corVez.ordinal()).iterator();
                if (qtdBarreiras == 2) {    
                    Piao piaoBarreira1 = iterator.next().getPiao();
                    Piao piaoBarreira2 = iterator.next().getPiao();
                    if (!tabuleiro.podeMover(piaoBarreira1, resultado)) {
                        if (tabuleiro.podeMover(piaoBarreira2, resultado)) piaoQuebrado = piaoBarreira2;
                    }
                    else {
                        if (tabuleiro.podeMover(piaoBarreira2, resultado)) {
                            if (piaoBarreira2.getPosicao() > piaoBarreira1.getPosicao()) piaoQuebrado = piaoBarreira2;
                            else piaoQuebrado = piaoBarreira1;
                        }
                        else piaoQuebrado = piaoBarreira1;
                    }
                }
                else if (qtdBarreiras == 1) {
                    Piao piaoBarreira = iterator.next().getPiao();
                    if (tabuleiro.isLivreParaMover(piaoBarreira, resultado)) piaoQuebrado = piaoBarreira;
                }
                if (piaoQuebrado != null) {
                    tabuleiro.move(piaoQuebrado, resultado);
                    ultimoPiaoMovido = piaoQuebrado;
                    updateVez();
                    System.out.println("Cod2: barreira quebrada automaticamente");
                    
                    dadoAtual = 0;
                    return 0;
                }
            }
        }
        else if (resultado == 5) {
            System.out.println("Model.lancaDado: resultado = 5!");
            Piao p = tabuleiro.getInicial(corVez).getPiao();
            if (tabuleiro.getInicial(corVez).getQtdPioes() > 0 && tabuleiro.move(p,1)) {
                ultimoPiaoMovido = p;
                updateVez();
                System.out.println("Cod3: 5 => inicia piao");
                dadoAtual = 0;
                return 0;
            }
        }
        if (!tabuleiro.existeJogadaPermitida(corVez, resultado)) {
            System.out.println("Model.lancaDado: nenhuma jogada possível");
            updateVez();
            System.out.println("Cod4: sem jogadas");
            dadoAtual = 0;
            return 0;
        }
        boolean tudoZerado = true;
        for (int i = 0; i < 4; i++) {
            if (tabuleiro.arrayPioes[corVez.ordinal()][i].getPosicao() != 0) {
                tudoZerado = false;
                break;
            }
        }
        if (tudoZerado && resultado != 5) {
            updateVez();
            System.out.println("Cod5: nada pra iniciar");
            dadoAtual = 0;
            return 0;
        }
        
        dadoAtual = resultado;
        return resultado;
    }
    
    public boolean fimDoJogo() {
    	if (jogoAcabou) tabuleiro.termina();
    	return jogoAcabou;
    }

    
    // tentamoverPiao(corPiao, idPiao, casas) tenta mover o "idPiao-ésimo" Pião de cor "corPiao" "casas" casas para a frente. retorna TRUE em caso de sucesso e FALSE em caso de falha.
    public boolean tentaMoverPiao (Cor corPiao, int idPiao, int casas) {
        Piao p = tabuleiro.getPiao(corPiao, idPiao);
        boolean retorno = tabuleiro.move(p, casas);
        jogoAcabou = !tabuleiro.getStatus();
        if (retorno) {
            ultimoPiaoMovido = p;
            this.atualiza();
            if (casas != 6) updateVez();// se deu 6 no dado a vez não muda!!
        }
        return retorno;
    }

    public void updateVez(){
        corVez = Cor.values()[(corVez.ordinal()+1)%4];
        System.out.println("Vez passada para o "+corVez.toString());
        this.atualiza();
        qtdSeisRolados = 0;
        dadoAtual = 0;
        cont.refresh();
    }

    public Cor getVez(){
        return corVez;
    }

	public void addObserver(ObserverLudo o) {
		lob.add(o);
        cont = (Controller) o;
	}
	
	public void removeObserver(ObserverLudo o) {
		lob.remove(o);
	}

    private void atualiza() {
        ListIterator<ObserverLudo> li = lob.listIterator();
        while(li.hasNext()) li.next().notify(this);
    }

    public Object getPioes() {
        Object listaPioes[][] = new Object[4][4];

        for (Cor cor: Cor.values()) {
            for (int i = 0; i < 4; i++) {
                listaPioes[cor.ordinal()][i] = tabuleiro.arrayPioes[cor.ordinal()][i].getPosicao();
            }
        }
        return listaPioes;
    }
}