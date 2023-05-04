package regras;

import cores.*;
// Piao representa um Pião colorido.
public class Piao {
    private int posicao;
    private Cor cor;
    private int indice;
    private Tabuleiro tabuleiro;    // (Tomaz) incluí uma referência ao tabuleiro para poder implementar o isBarreiraNoCaminho() como método de Pião. Achei que fazia mais sentido esse método aqui do que em Tabuleiro.
    
    // construtor
    public Piao(Cor cor, int indice, Tabuleiro t) {
        this.cor = cor;
        this.indice = indice;
        posicao = 0;
        this.tabuleiro = t;
    }

    // getPosicao() retorna a posicao do Pião no trajeto do SEU início (0) ao SEU final (65).
    public int getPosicao () {
        return posicao;
    }

    // getCor() retorna a Cor do Piao.
    public Cor getCor () {
        return cor;
    }
    
    // getCorNum() retorna o inteiro que representa a Cor do Piao.
    // (Tomaz) Serve basicamente para evitar a quantidade de cor.ordinal() que apareceria para mexer nos arrays.
    public int getCorNum () {
        return cor.ordinal();
    }
    
    // getIndice() retorna o índice do Pião (0, 1, 2 ou 3 -- é o identificador do pião (além da cor)).
    public int getIndice () {
        return indice;
    }
    
    // setPosicao() seta a posição do Piao como novaPosicao
    // (Tomaz) Acho que também não foi usado no código.
    public void setPosicao (int novaPosicao) {
        posicao = novaPosicao;
    }

    // move() altera a posição do Pião no SEU TRAJETO por uma quantidade de casas.
    // (Tomaz) Nota: isso não move o pião de Casa, apenas muda o valor que ele guarda de quanto já completou do trajeto!!
    public void move (int casas) {
        if (posicao == 0) posicao ++;
        else posicao += casas;
    }
    
    // dumpString() é uma função de teste que retorna a referência do pião (cor e índice) como String. É uma função de TESTE que deve ser APAGADA na entrega final.
    public String dumpString () {
        return String.format("(%s, %d)",cor.toString(),indice);
    }

    // distFinal() retorna a distância que o Pião está do final de seu percurso pessoal.
    public int distFinal () {
        return 65-posicao;
    }
    
    // reset() reseta o INDICE do Pião para 0. Assim como move(), ela NÃO ALTERA A CASA DO PIÃO! ISSO É FEITO EM Tabuleiro !!
    public void reset () {
        this.setPosicao(0);
    }
    
    // isBarreiraNoCaminho() retorna TRUE se tem uma barreira nas próximas 'distancia' casas depois do Pião e FALSE caso contrário.
    // (Tomaz) É a última função que implementei hoje (03/05). Serve como auxiliar para a funcao de avaliar se um movimento é possível em Tabuleiro.
    public boolean isBarreiraNoCaminho (int distancia) {
        for (int i = posicao; i < posicao + distancia; i++) {
            if (i > 64) return false;
            if (tabuleiro.search(i, cor).isBarreira()) return true;
        }
        return false;
    }
}
