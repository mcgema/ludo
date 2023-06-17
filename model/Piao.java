package model;

import cores.*;
// Piao representa um Pião colorido.
class Piao {
    private int posicao;
    private Cor cor;
    private int indice;
    private Tabuleiro tabuleiro; 
    
    // construtor
    protected Piao(Cor cor, int indice, Tabuleiro t) {
        this.cor = cor;
        this.indice = indice;
        posicao = 0;
        this.tabuleiro = t;
    }

    // getPosicao() retorna a posicao do Pião no trajeto do SEU início (0) ao SEU final (65).
    protected int getPosicao () {
        return posicao;
    }

    // getCor() retorna a Cor do Piao.
    protected Cor getCor () {
        return cor;
    }
    
    // getCorNum() retorna o inteiro que representa a Cor do Piao.
    protected int getCorNum () {
        return cor.ordinal();
    }
    
    // getIndice() retorna o índice do Pião (0, 1, 2 ou 3 -- é o identificador do pião (além da cor)).
    protected int getIndice () {
        return indice;
    }
    
    // setPosicao() seta a posição do Piao como novaPosicao
    // não foi usado no código ainda
    protected void setPosicao (int novaPosicao) {
        posicao = novaPosicao;
    }

    // move() altera a posição do Pião no SEU TRAJETO por uma quantidade de casas.
    // isso não move o pião de Casa, apenas muda o valor que ele guarda de quanto já completou do trajeto!!
    protected void move (int casas) {
        if (posicao == 0) posicao ++;
        else posicao += casas;
    }
    
    // dumpString() é uma função de teste que retorna a referência do pião (cor e índice) como String. É uma função de TESTE que deve ser APAGADA na entrega final.
    protected String dumpString () {
        return String.format("(%s, %d, [%2d])",cor.toString(),indice,posicao);
    }

    // distFinal() retorna a distância que o Pião está do final de seu percurso pessoal.
    protected int distFinal () {
        return 57-posicao;
    }
    
    // reset() reseta o INDICE do Pião para 0. Assim como move(), ela NÃO ALTERA A CASA DO PIÃO! ISSO É FEITO EM Tabuleiro !!
    protected void reset () {
        //System.out.printf("Piao %s %d resetado!\n",this.cor.toString(), this.indice);
        this.tabuleiro.search(this).removePiao(this);
        this.setPosicao(0);
        this.tabuleiro.getInicial(cor).inserePiao(this);
    }
    
    // isBarreiraNoCaminho() retorna TRUE se tem uma barreira nas próximas 'distancia' casas depois do Pião e FALSE caso contrário.
    protected boolean isBarreiraNoCaminho (int distancia) {
        for (int i = posicao+1; i < posicao + distancia+1; i++) {
            if (i > 57) return false;
            if (tabuleiro.search(i, cor).isBarreira()) return true;
        }
        return false;
    }
}
