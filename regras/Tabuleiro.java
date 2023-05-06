package regras;

import cores.*;
import java.util.*;

// Tabuleiro representa o Tabuleiro de Ludo, e implementa 2 dos 3 métodos passados para Model.
public class Tabuleiro {
    // obs: com o tabuleiro correto em mãos, recontar TODO NÚMERO DE CASAS!!!

    // (Tomaz) Todas essas variáveis estão aqui só até o Ivan liberar o tabuleiro oficial. Depois disso, SUBSTITUIR POR NÚMEROS!!
    int abrigos[] = {10, 23, 36, 49, 0};
    
    // Tabuleiro é representado por Arrays de Casas.
    Casa tabuleiro [][] = new Casa [4][58];
    
    // Matriz de Piões guarda os "ponteiros" para todos os Piões, garantido que sempre serão facilmente acessíveis pela sua cor e índice.
    // (Tomaz) Guardado como arrayPioes[cor][indice]
    Piao arrayPioes[][] = new Piao[4][4];
    
    // Conjunto de Casas guarda quais são as casas que tem barreiras.
    ArrayList<HashSet<Casa>> barreiras = new ArrayList<HashSet<Casa>>();

    Piao ultimoPiaoMovimentado;
    int qtdSeisRolados = 0;
    
    private Casa casasNormais[] = new Casa [52];
    // bloco de inicialização
    {
        int j = 0;
        for (int i = 0; i < 52; i++) {
            // casas de saída:
            if (i%13 == 0) casasNormais[i] = new Casa(i+1, Tipo.saida, Cor.values()[i/13]);

            // abrigos:
            else if (this.abrigos[j] == i) casasNormais[abrigos[j++]] = new Casa(i+1,Tipo.abrigo);
            
            // casas padrão:
            else casasNormais[i] = new Casa(i+1,Tipo.padrao);
        }

        for (Cor cor: Cor.values()) {
            int i = 0;
            this.tabuleiro[cor.ordinal()][0] = new Casa(0, Tipo.inicial, cor);
            for (i = 1;  i < 52; i++) this.tabuleiro[cor.ordinal()][i] = casasNormais[((i-1)+cor.ordinal()*13)%52];
            for (i = 52; i < 57; i++) this.tabuleiro[cor.ordinal()][i] = new Casa(i, Tipo.retaFinal, cor);
            this.tabuleiro[cor.ordinal()][i] = new Casa(57, Tipo.vitoria, cor);

            // inicializa os Piões:
            for (i = 0; i < 4; i++) {
                arrayPioes[cor.ordinal()][i] = new Piao(cor,i, this);
                this.getInicial(cor).inserePiao(arrayPioes[cor.ordinal()][i]);
            }
            
            // inicializa o conjunto de barreiras:
            barreiras.add(new HashSet<Casa>());
        }
    }

    // getInicial(Cor cor) retorna a Casa inicial da Cor cor
    public Casa getInicial (Cor cor) {
        return tabuleiro[cor.ordinal()][0];
    }
    
    // getFinal(Cor cor) retorna a Casa inicial da Cor cor
    public Casa getFinal (Cor cor) {
        return tabuleiro[cor.ordinal()][57];
    }

    // getSaida(Cor cor) retorna a Casa de saida da Cor cor
    public Casa getSaida (Cor cor) {
        return tabuleiro[cor.ordinal()][1];
    }
    
    // getPiao (Cor c, int i) retorna o "ponteiro" (objeto) para o Pião de Cor c e índice i
    public Piao getPiao (Cor c, int i) {
        return arrayPioes[c.ordinal()][i];
    }
    
    // isEmptyInicial(Cor cor) retorna TRUE se a casa inicial da Cor cor estiver vazia e FALSE caso ainda tenham Piões nela
    public boolean isEmptyInicial (Cor cor) {
        return this.getInicial(cor).isEmpty();
    }
    
    // search(int pos, Cor corJogador) retorna a Casa correspondente à pos-ésima posição no percurso do jogador de Cor corJogador
    public Casa search (int pos, Cor corJogador) {
        return tabuleiro[corJogador.ordinal()][pos];
    }
    
    // search(Piao p) é polimórfica com a outra search(), e retorna a Casa em que o Pião p está.
    public Casa search (Piao p) {
        return this.search(p.getPosicao(), p.getCor());
    }
    
    // move(Piao p, int qtdCasas) move o Pião p em qtdCasas Casas.
    // (Tomaz) Essa função realmente altera a Casa em que o Pião p está!!
    public boolean move (Piao p, int resultadoDado) {
    	int qtdCasas = resultadoDado;
    	if (p.getPosicao() == 0) qtdCasas = 1;
        if (qtdCasas == 6 && p.getCor() == ultimoPiaoMovimentado.getCor()) qtdSeisRolados++;
        else qtdSeisRolados = 0;
        
        if (!podeMover(p, this.ultimoPiaoMovimentado, qtdCasas, qtdSeisRolados)) {System.out.printf("F-00\n");return false;}
        

        Casa inicial = this.search(p);
        if (inicial.isBarreira()) barreiras.get(p.getCorNum()).remove(inicial); // caso isso desfaça uma barreira ela é excluída do conjunto
        inicial.removePiao(p);
        p.move(qtdCasas);
        Casa destino = this.search(p);

        // captura pião
        if (destino.getQtdPioes() == 1) if (destino.getTipo() == Tipo.padrao && destino.getPiao().getCor() != p.getCor()) destino.getPiao().reset();

        destino.inserePiao(p);
        if (destino.isBarreira()) barreiras.get(p.getCorNum()).add(destino);    // caso isso crie uma barreira ela é salva no conjunto
        if (destino.getQtdPioes() == 4) {
        	this.termina();
        	System.out.println("move() chamou termina");
        }

        ultimoPiaoMovimentado = p;
        return true;
    }   

    boolean temBloqueio (Piao p, int qtdCasas) {
        if (p.getPosicao() == 0) qtdCasas = 1;
        if (p.isBarreiraNoCaminho(qtdCasas)) {System.out.printf("F-01\n");return false;}
        if (p.distFinal() < qtdCasas) {System.out.printf("F-02\n");return false;}
        if (p.distFinal() == qtdCasas) return true;
        Casa destino = this.search(p.getPosicao()+qtdCasas, p.getCor());
        switch (destino.getQtdPioes()) {
            case 2:
                {System.out.printf("F-03\n");return false;}
            case 1:
                Piao piaoDestino = destino.getPiao();
                if (piaoDestino.getCor() == p.getCor()) {
                    if (destino.getTipo() == Tipo.saida) {System.out.printf("F-04\n");return false;}
                    else return true; // forma barreira: delegado à move()
                }
                else {
                    if (destino.getTipo() == Tipo.abrigo) return true;
                    if (destino.getTipo() != Tipo.saida) return true; // captura piao: papel da move()
                    if (destino.getCor() == p.getCor() || destino.getCor() == piaoDestino.getCor()) return true;
                    {System.out.printf("F-05\n");return false;}
                }
            default:
                return true;
        }
    }

    boolean checaDado (Piao p, int resultadoDado, int qtd6, Piao ultimoPiaoMovimentado) {
        switch(resultadoDado) {
            case 6:
                if (qtd6 == 3) {
                    if (ultimoPiaoMovimentado.distFinal() > 5) ultimoPiaoMovimentado.reset();
                    {System.out.printf("F-06\n");return false;}
                }
                if (barreiras.get(p.getCorNum()).size() > 0) {
                    Iterator<Casa> iterator = barreiras.get(p.getCorNum()).iterator();
                    boolean existeBarreiraQuebravel = false;
                    while (iterator.hasNext()) {
                        Casa casaComBarreira = iterator.next();
                        if (this.temBloqueio(casaComBarreira.getPiao(),resultadoDado)) {
                            existeBarreiraQuebravel = true;
                            if (p.getPosicao() == casaComBarreira.getPiao().getPosicao()) return true;
                        }
                    }
                    return (!existeBarreiraQuebravel);
                }
            case 5:
                if (this.getInicial(p.getCor()).getQtdPioes() == 0) return true;
                if (!this.temBloqueio(this.getInicial(p.getCor()).getPiao(), 1)) return true;
                if (p.getPosicao() == 0) return true;
                {System.out.printf("F-07\n");return false;}
            default:
                return true; 
        }
    }

    boolean podeMover (Piao p, Piao ultimoPiaoMovimentado, int resultadoDado, int qtdSeisRolados) {
        return this.temBloqueio(p, resultadoDado) && checaDado(p, resultadoDado, qtdSeisRolados, ultimoPiaoMovimentado);
    }

    // Função Tabuleiro.captura(Piao p) foi substituída por Piao.reset().
    /*
    // captura(Piao p) captura o Piao p.
    public void captura (Piao p) {
        this.search(p).removePiao(p);
        this.getInicial(p.getCor()).inserePiao(p);
        p.reset();
    }
    */

    // termina() termina o jogo.
    // (Tomaz) Nota: essa função não está imprimindo conforme o enunciado! Ela DEVERIA imprimir as cores dos jogadores em ordem de colocação (aka: de pontos). FALTANDO!
    public void termina () {
        int distanciasTotais[] = new int[4];
        for (Piao[] aux: arrayPioes) {
            for (Piao p: aux) {
                distanciasTotais[p.getCorNum()] += p.getPosicao();
            }
        }
        for (Cor c: Cor.values()) {
            System.out.printf("Jogador %s: %d pontos\n",c.toString(),distanciasTotais[c.ordinal()]);
        }
    }
    
    // Eu não lembro mais o que isso aqui era pra ser.
    public void is (Piao p) {
        System.out.println(p);
    }
}
