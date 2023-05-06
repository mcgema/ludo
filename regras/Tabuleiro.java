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
    boolean fimDeJogo = false;
    
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
        
        // assume-se que o jogador VERMELHO (0) sempre começa:
        ultimoPiaoMovimentado = arrayPioes[0][0];
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
    
    public boolean getStatus () {
    	return !fimDeJogo;
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
        
        if (!podeMover(p, qtdCasas)) {
        	System.out.printf("F-00\n");
        	return false;
        }

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
        	fimDeJogo = true;
        	//this.termina();
        	//System.out.println("move() chamou termina");
        }

        ultimoPiaoMovimentado = p;
        return true;
    }   

    boolean isLivreParaMover (Piao p, int qtdCasas) {
        // se o pião está para sair, ele só anda uma casa, independente do dado:
        if (p.getPosicao() == 0) qtdCasas = 1;

        // se tem uma barreira no caminho, o pião não pode se mover (código F-01):
        if (p.isBarreiraNoCaminho(qtdCasas)) {
            System.out.printf("F-01\n");
            return false;
        }

        // se o valor rolado no dado for maior que a quantidade de casas até o final, o pião não pode se mover (código F-02):
        if (p.distFinal() < qtdCasas) {
            System.out.printf("F-02\n");
            return false;
        }

        // ...mas se tem exatamente essa quantidade de casas (e não tem barreiras), ele pode:
        if (p.distFinal() == qtdCasas) return true;

        Casa destino = this.search(p.getPosicao()+qtdCasas, p.getCor());
        System.out.printf("\ndestino:\t");
        destino.dump();
        System.out.printf("\n");
        switch (destino.getQtdPioes()) {

            // se tiverem dois piões na casa de destino do movimento (e ela não é o final -- esse caso já foi analisado!), o pião não se move (código F-03):
            case 2:
                System.out.printf("F-03\n");
                return false;
            
            case 1:
                Piao piaoDestino = destino.getPiao();
                if (piaoDestino.getCor() == p.getCor()) {

                    // se tiver 1 pião no destino e ele for da mesma cor que o seu e ele estiver em uma casa de saída (que impede barreiras), ele não pode se mover (código F-04):
                    if (destino.getTipo() == Tipo.saida) {
                        System.out.printf("F-04\n");
                        return false;
                    }

                    // ...mas se ele não estiver em uma casa de saída, ele se move (e forma uma barreira):
                    else return true; // forma barreira: delegado à move()
                }
                else {

                    // se tiver 1 pião no destino e ele for de uma cor diferente e ele estiver em um abrigo OU casa padrão, pode mover:
                    if (destino.getTipo() != Tipo.saida) return true; // capturar piao é papel da move()

                    // se tiver 1 pião no destino e ele for de uma cor diferente e o destino for uma casa de saída da cor do seu pião ou da cor do oponente, pode se mover:
                    if (destino.getCor() == p.getCor() || destino.getCor() == piaoDestino.getCor()) return true;

                    // ...mas, se tiver um pião e ele for de uma cor diferente e o destino for uma casa de saída de uma cor diferente das dos dois, ele não pode mover (código F-05):
                    System.out.printf("F-05\n");
                    return false;
                }
            
            default:
                // se não tiverem piões na casa de destino ele pode se mover:
                return true;
        }
    }

    boolean bloqueadoPeloDado (Piao p, int resultadoDado) {
        switch(resultadoDado) {
            case 6:

                // caso o jogador tenha barreiras...
                if (barreiras.get(p.getCorNum()).size() > 0) {
                    Iterator<Casa> iterator = barreiras.get(p.getCorNum()).iterator();
                    boolean existeBarreiraQuebravel = false;
                    while (iterator.hasNext()) {
                        Casa casaComBarreira = iterator.next();

                        // ...e a barreira possa ser quebrada...
                        if (this.isLivreParaMover(casaComBarreira.getPiao(),resultadoDado)) {
                            existeBarreiraQuebravel = true;

                            // ...e o pião em questão é dessa barreira, ele pode se mover:
                            if (p.getPosicao() == casaComBarreira.getPiao().getPosicao()) return false;
                        }
                    }

                    // caso exista barreira quebrável mas esse pião não seja dela, ele não move;
                    // caso não exista barreira quebrável, nada dos dados impede o pião de mover:
                    return (!existeBarreiraQuebravel);
                }
                // se rolou 6 mas não tem barreiras e rolou menos de dois 6, nada dos dados impede de mover:
                return false;

            case 5:
                // se o jogador não tiver piões para iniciar, nada dos dados impede o pião de mover:
                if (this.getInicial(p.getCor()).getQtdPioes() == 0) return false;

                // se o jogador tiver piões para iniciar, mas eles estão bloqueados, nada dos dados impede os piões de mover (move() vai bloquear esse pião):
                if (this.isLivreParaMover(this.getInicial(p.getCor()).getPiao(), 1)) return false;

                // caso o jogador tenha piões para iniciar e esses piões possam ser iniciados só eles podedm se mover:
                if (p.getPosicao() == 0) return false;

                // ...ou seja: se o jogador pode iniciar piões e esse pião já está iniciado, ele não move (código F-07):
                System.out.printf("F-07\n");
                return true;
                
            default:
                // se o dado rolou 1, 2, 3 ou 4, ele não influencia na possibilidade de movimentação:
                return false; 
        }
    }

    boolean podeMover (Piao p, int resultadoDado) {
        return this.isLivreParaMover(p, resultadoDado) && !bloqueadoPeloDado(p, resultadoDado);
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
    
    // jogadorPodeJogar (Cor corDoJogador, int resultadoDado) retorna TRUE se o jogador pode jogar e FALSE caso sua rodada deva ser pulada.
    public boolean jogadorPodeJogar (Cor corDoJogador, int resultadoDado) {
        if (resultadoDado == 6 && corDoJogador == ultimoPiaoMovimentado.getCor()) qtdSeisRolados++;
        else qtdSeisRolados = 0;
    	System.out.println(qtdSeisRolados);

        if (qtdSeisRolados == 3) {
            // caso três 6 consecutivos sejam tirados no dado, o jogador não tem rodada (movimento forçado do pião):
            if (ultimoPiaoMovimentado.distFinal() > 5) ultimoPiaoMovimentado.reset();

            System.out.printf("Vo-seis-agerou...\n");
            return false;
        }
        
        boolean piaoPodeMover = false;
        for (Piao p: arrayPioes[corDoJogador.ordinal()]) {
        	if (podeMover(p,resultadoDado)) piaoPodeMover = true;
        }
        
        // se nenhum pião pode se mover, o jogador não tem rodada:
        return piaoPodeMover;
    }
}
