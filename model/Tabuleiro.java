package model;

import cores.*;
import java.util.*;

// Tabuleiro representa o Tabuleiro de Ludo, e implementa 2 dos 3 métodos passados para Model.
class Tabuleiro {
    private static Tabuleiro singleton;
    
    // Tabuleiro é representado por Arrays de Casas.
    Casa tabuleiro [][] = new Casa [4][58]; //[MC] peao percorre 56 casas -> [Tom] o peao vai da 0 (inicio) até a 57 (vitória). São 58 casas.
    
    // Matriz de Piões guarda os "ponteiros" para todos os Piões, garantido que sempre serão facilmente acessíveis pela sua cor e índice.
    Piao arrayPioes[][] = new Piao[4][4];
    
    // Conjunto de Casas guarda quais são as casas que tem barreiras.
    ArrayList<HashSet<Casa>> barreiras = new ArrayList<HashSet<Casa>>();

    Piao ultimoPiaoMovimentado;
    int qtdSeisRolados = 0;
    boolean fimDeJogo = false;
    
    private Casa casasNormais[] = new Casa [52];
    // bloco de inicialização
    {
        System.out.println("Model.Tabuleiro: Tabuleiro inicializado!");
        for (int i = 0; i < 52; i++) {
            // casas de saída:
            if (i%13 == 0) casasNormais[i] = new Casa(i+1, Tipo.saida, Cor.values()[i/13]);

            // abrigos:
            else if ((i+13)%13 == 9) casasNormais[i] = new Casa(i+1,Tipo.abrigo);
            
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

    private Tabuleiro () {
        // construtor bloqueado por Singleton
    }

    public static Tabuleiro create() {
        if (singleton == null) singleton = new Tabuleiro();
        return singleton;
     }

    // getTabuleiro() retorna tabuleiro de cada cor
    protected Casa[][] getTabuleiro(){
        return tabuleiro;
    }

    // getInicial(Cor cor) retorna a Casa inicial da Cor cor
    protected Casa getInicial (Cor cor) {
        return tabuleiro[cor.ordinal()][0];
    }
    
    // getFinal(Cor cor) retorna a Casa inicial da Cor cor
    protected Casa getFinal (Cor cor) {
        return tabuleiro[cor.ordinal()][57];
    }

    // getSaida(Cor cor) retorna a Casa de saida da Cor cor
    protected Casa getSaida (Cor cor) {
        return tabuleiro[cor.ordinal()][1];
    }
    
    // getPiao (Cor c, int i) retorna o "ponteiro" (objeto) para o Pião de Cor c e índice i
    protected Piao getPiao (Cor c, int i) {
        return arrayPioes[c.ordinal()][i];
    }
    
    protected boolean getStatus () {
    	return !fimDeJogo;
    }
    
    // isEmptyInicial(Cor cor) retorna TRUE se a casa inicial da Cor cor estiver vazia e FALSE caso ainda tenham Piões nela
    protected boolean isEmptyInicial (Cor cor) {
        return this.getInicial(cor).isEmpty();
    }
    
    // search(int pos, Cor corJogador) retorna a Casa correspondente à pos-ésima posição no percurso do jogador de Cor corJogador
    protected Casa search (int pos, Cor corJogador) {
        return tabuleiro[corJogador.ordinal()][pos];
    }
    
    // search(Piao p) é polimórfica com a outra search(), e retorna a Casa em que o Pião p está.
    protected Casa search (Piao p) {
        return this.search(p.getPosicao(), p.getCor());
    }
    
    // move(Piao p, int resultadoDado) move o Pião p em resultadoDado Casas.
    protected int move (Piao p, int resultadoDado, boolean bonusDeCaptura) {
        if (bonusDeCaptura) {
            if (resultadoDado > p.distFinal()) resultadoDado = p.distFinal();
        }
        System.out.println("Tabuleiro.move(["+p.getCor()+p.getIndice()+"],"+resultadoDado+"): fui chamada!");
    	int qtdCasas = resultadoDado;
        boolean houveCaptura = false;
        
        if (!podeMover(p, qtdCasas, bonusDeCaptura)) {
        	System.out.printf("Tabuleiro.move(["+p.getCor()+p.getIndice()+"],"+resultadoDado+"): "+p.getCor()+" "+p.getIndice()+" NAO PODE MOVER - COD. 0: MOVIMENTO BLOQUEADO\n");
            System.out.printf("Tabuleiro.move(["+p.getCor()+p.getIndice()+"],"+resultadoDado+"):  Posições dos %ss: ", p.getCor().toString());
            for (int dor = 0; dor < 4; dor++) System.out.printf("%d, ", arrayPioes[p.getCorNum()][dor].getPosicao());
            System.out.printf("\n");
        	return 0;
        }
    	if (p.getPosicao() == 0) qtdCasas = 1;

        Casa inicial = this.search(p);
        if (inicial.isBarreira()) barreiras.get(p.getCorNum()).remove(inicial); // caso isso desfaça uma barreira ela é excluída do conjunto
        inicial.removePiao(p);
        p.move(qtdCasas);
        Casa destino = this.search(p);

        // captura pião
        if (destino.getQtdPioes() == 1)
            if ((destino.getTipo() == Tipo.padrao && destino.getPiao().getCor() != p.getCor()) ||
                (inicial.getTipo() == Tipo.inicial && destino.getPiao().getCor() != p.getCor())) {
                    destino.getPiao().reset();
                    houveCaptura = true;
                }

        destino.inserePiao(p);
        if (destino.isBarreira()) barreiras.get(p.getCorNum()).add(destino);    // caso isso crie uma barreira ela é salva no conjunto
        if (destino.getQtdPioes() == 4) {
        	fimDeJogo = true;
        }

        ultimoPiaoMovimentado = p;
        System.out.printf("Tabuleiro.move(["+p.getCor()+p.getIndice()+"],"+resultadoDado+"): "+p.getCor()+" "+p.getIndice()+" PODE MOVER - COD. 0: MOVIMENTO PERMITIDO\n");
        System.out.printf("Tabuleiro.move(["+p.getCor()+p.getIndice()+"],"+resultadoDado+"): Posições dos %ss: ", p.getCor().toString());
        for (int dor = 0; dor < 4; dor++) System.out.printf("%d, ", arrayPioes[p.getCorNum()][dor].getPosicao());
        System.out.printf("\n");

        if (houveCaptura) return 2;
        return 1;
    }   

    protected boolean isLivreParaMover (Piao p, int qtdCasas, boolean bonusDeCaptura) {
        System.out.println("Tabuleiro.isLivreParaMover(["+p.getCor()+p.getIndice()+"],"+qtdCasas+"): fui chamada!");
        // se o pião está na casa inicial e o dado não foi 5, ele não pode se mover:
        if (p.getPosicao() == 0 && qtdCasas != 5) {
            System.out.printf("Tabuleiro.isLivreParaMover(["+p.getCor()+p.getIndice()+"],"+qtdCasas+"): "+p.getCor()+" "+p.getIndice()+" NAO PODE MOVER - COD. 6: SÓ SAI COM 5\n");
            return false;
        }

        // se o pião está para sair, ele só anda uma casa, independente do dado:
        if (p.getPosicao() == 0) qtdCasas = 1;

        // se tem uma barreira no caminho, o pião não pode se mover (código F-01):
        if (p.isBarreiraNoCaminho(qtdCasas)) {
            System.out.printf("Tabuleiro.isLivreParaMover(["+p.getCor()+p.getIndice()+"],"+qtdCasas+"): "+p.getCor()+" "+p.getIndice()+" NAO PODE MOVER - COD. 1: BARREIRA NO CAMINHO\n");
            return false;
        }

        // se o valor rolado no dado for maior que a quantidade de casas até o final, o pião não pode se mover (código F-02):
        if (p.distFinal() < qtdCasas && !bonusDeCaptura) {
            System.out.printf("Tabuleiro.isLivreParaMover(["+p.getCor()+p.getIndice()+"],"+qtdCasas+"): "+p.getCor()+" "+p.getIndice()+" NAO PODE MOVER - COD. 2: PASSA DO FINAL\n");
            return false;
        }

        // ...mas se tem exatamente essa quantidade de casas (e não tem barreiras), ele pode:
        if (p.distFinal() == qtdCasas) return true;

        Casa destino = this.search(p.getPosicao()+qtdCasas, p.getCor());
        //System.out.printf("Tabuleiro.isLivre...: destino:\t");
        //destino.dump();
        //System.out.printf("\n");
        switch (destino.getQtdPioes()) {

            // se tiverem dois piões na casa de destino do movimento (e ela não é o final -- esse caso já foi analisado!), o pião não se move (código F-03):
            case 2:
                System.out.printf("Tabuleiro.isLivreParaMover(["+p.getCor()+p.getIndice()+"],"+qtdCasas+"): "+p.getCor()+" "+p.getIndice()+" NAO PODE MOVER - COD. 3: DESTINO LOTADO\n");
                return false;
            
            case 1:
                Piao piaoDestino = destino.getPiao();
                if (piaoDestino.getCor() == p.getCor()) {

                    // se tiver 1 pião no destino e ele for da mesma cor que o seu e ele estiver em uma casa de saída OU ABRIGO (que impede barreiras), ele não pode se mover (código F-04):
                    if (destino.getTipo() == Tipo.saida || destino.getTipo() == Tipo.abrigo) {
                        System.out.printf("Tabuleiro.isLivreParaMover(["+p.getCor()+p.getIndice()+"],"+qtdCasas+"): "+p.getCor()+" "+p.getIndice()+" NAO PODE MOVER - COD. 4: BARREIRA IMPOSSÍVEL\n");
                        return false;
                    }

                    // ...mas se ele não estiver em uma casa de saída OU ABRIGO, ele se move (e forma uma barreira):
                    else return true; // forma barreira: delegado à move()
                }
                else {

                    // se tiver 1 pião no destino e ele for de uma cor diferente e ele estiver em um abrigo OU casa padrão, pode mover:
                    if (destino.getTipo() != Tipo.saida) return true; // capturar piao é papel da move()

                    // se tiver 1 pião no destino e ele for de uma cor diferente e o destino for uma casa de saída da cor do seu pião ou da cor do oponente, pode se mover:
                    if (destino.getCor() == p.getCor() || destino.getCor() == piaoDestino.getCor()) return true;

                    // ...mas, se tiver um pião e ele for de uma cor diferente e o destino for uma casa de saída de uma cor diferente das dos dois, ele não pode mover (código F-05):
                    System.out.printf("Tabuleiro.isLivreParaMover(["+p.getCor()+p.getIndice()+"],"+qtdCasas+"): "+p.getCor()+" "+p.getIndice()+" NAO PODE MOVER - COD. 5: ABRIGO EM SAÍDA DE COR ERRADA\n");
                    return false;
                }
            
            default:
                // se não tiverem piões na casa de destino ele pode se mover:
                return true;
        }
    }

    protected boolean bloqueadoPeloDado (Piao p, int resultadoDado, boolean bonusDeCaptura) {
        System.out.println("Tabuleiro.bloqueadoPeloDado(["+p.getCor()+p.getIndice()+"],"+resultadoDado+"): fui chamada!");
        switch(resultadoDado) {
            case 6:

                // caso o jogador tenha barreiras...
                if (barreiras.get(p.getCorNum()).size() > 0) {
                    Iterator<Casa> iterator = barreiras.get(p.getCorNum()).iterator();
                    boolean existeBarreiraQuebravel = false;
                    while (iterator.hasNext()) {
                        Casa casaComBarreira = iterator.next();
                        
                        // ...e a barreira possa ser quebrada...
                        if (this.isLivreParaMover(casaComBarreira.getPiao(),resultadoDado, bonusDeCaptura)) {
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
            default:
                // se o dado rolou 1, 2, 3 ou 4, ele não influencia na possibilidade de movimentação:
                return false; 
        }
    }

    protected boolean podeMover (Piao p, int resultadoDado, boolean bonusDeCaptura) {
        System.out.println("Tabuleiro.podeMover(["+p.getCor()+p.getIndice()+"],"+resultadoDado+"): fui chamada!");
        return this.isLivreParaMover(p, resultadoDado, bonusDeCaptura) && !bloqueadoPeloDado(p, resultadoDado, bonusDeCaptura);
    }

    protected Piao getPiaoBarreiraQuebravel (Cor corVez, int resultado, boolean bonusDeCaptura) {
        System.out.println("Tabuleiro.getPiaoBarreiraQuebravel("+corVez.toString()+","+resultado+"): fui chamada!");
        int qtdBarreiras = this.barreiras.get(corVez.ordinal()).size();
        if (qtdBarreiras == 0) return null;
    
        Piao piaoQuebrado = null;
        Iterator<Casa> iterator = this.barreiras.get(corVez.ordinal()).iterator();
        if (qtdBarreiras == 2) {    
            Piao piaoBarreira1 = iterator.next().getPiao();
            Piao piaoBarreira2 = iterator.next().getPiao();
            if (!this.podeMover(piaoBarreira1, resultado, bonusDeCaptura)) {
                if (this.podeMover(piaoBarreira2, resultado, bonusDeCaptura)) piaoQuebrado = piaoBarreira2;
            }
            else {
                if (this.podeMover(piaoBarreira2, resultado, bonusDeCaptura)) {
                    if (piaoBarreira2.getPosicao() > piaoBarreira1.getPosicao()) piaoQuebrado = piaoBarreira2;
                    else piaoQuebrado = piaoBarreira1;
                }
                else piaoQuebrado = piaoBarreira1;
            }
        }

        else if (qtdBarreiras == 1) {
            Piao piaoBarreira = iterator.next().getPiao();
            if (this.isLivreParaMover(piaoBarreira, resultado, bonusDeCaptura)) piaoQuebrado = piaoBarreira;
        }

        return piaoQuebrado;
    }

    protected boolean existeJogadaPermitida(Cor c, int dado, boolean bonusDeCaptura) {
        System.out.println("Tabuleiro.existeJogadaPermitida("+c.toString()+","+dado+"): fui chamada!");
        for (int i = 0; i < 4; i++) if (this.podeMover(arrayPioes[c.ordinal()][i], dado, bonusDeCaptura)) return true;
        return false;
    }

    // termina() termina o jogo.
    protected void termina () {
        System.out.println("Tabuleiro.termina(): fui chamada!");
        int distanciasTotais[][] = new int[4][2];
        for (int i=0; i<4; i++){
            distanciasTotais[i][0] = i;
        }

        //atualiza pontuacao por cor
        for (Piao[] aux: arrayPioes) {
            for (Piao p: aux) {
                distanciasTotais[p.getCorNum()][1] += p.getPosicao();
            }
        }
        // ordena pontuacao
        Arrays.sort(distanciasTotais, Comparator.comparingDouble(o -> o[1]));
        for (int j=3; j>-1; j--){
            System.out.printf("Tabuleiro.termina(): Jogador %s: %d pontos\n",Cor.values()[distanciasTotais[j][0]].toString(),distanciasTotais[j][1]);
        }
    }

    public void tabudump() {
        for (Cor c: Cor.values()) {
            System.out.printf("%s:\t|\n",c.toString());
            int i = 0;
            for (Casa k: tabuleiro[c.ordinal()]) {
                System.out.printf("\t| %2d:%9s |\n",i++,k.getTipo().toString());
            }
            System.out.println(";");
        }
    }
}