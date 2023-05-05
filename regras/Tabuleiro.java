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
    
    private Casa casasNormais[] = new Casa [52];
    // bloco de inicialização
    {
        int j = 0;
        for (int i = 0; i < 52; i++) {
            // casas de saída:
            if (i%13 == 0) casasNormais[i] = new Casa(i+1, Tipo.saida, Cor.values()[i/13]);

            // abrigos:
            else if (this.abrigos[j] == i) casasNormais[abrigos[j++]] = new Casa(i,Tipo.abrigo);
            
            // casas padrão:
            else casasNormais[i] = new Casa(i,Tipo.padrao);
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
    public void move (Piao p, int qtdCasas) {
        Casa inicial = this.search(p);
        if (inicial.isBarreira()) barreiras.get(p.getCorNum()).remove(inicial); // caso isso desfaça uma barreira ela é excluída do conjunto
        inicial.removePiao(p);
        p.move(qtdCasas);
        Casa destino = this.search(p);
        destino.inserePiao(p);
        if (destino.isBarreira()) barreiras.get(p.getCorNum()).add(destino);    // caso isso crie uma barreira ela é salva no conjunto
        if (destino.getQtdPioes() == 4) this.termina();
        
        // (Tomaz) FALTA UMA CLÁUSULA PARA SE ISSO CAPTURA UM PIÃO OPONENTE!!!!!!!! (Lembrando da função captura(Piao p) )
    }   

    boolean possibleMove(Piao p, int i ) { return true;}
    // possibleMove(Piao p, int i) está INCOMPLETA. Ela DEVE retornar TRUE se o Pião p pode se mover i casas e FALSE caso não possa.
    // (Tomaz) Nota: Não sei se essa é a melhor implementação pra isso. Se quiserem fazer totalmente diferente, sintam-se à vontade. Tenho backups de tudo se precisar.
    /*
    public boolean possibleMove (Piao p, int i) {
        // se rolou 5 e tem pioes na casa inicial e eles podem sair, só eles podem mover:
        if (i == 5 && this.getInicial(p.getCor()).getQtdPioes() > 0 && percurso[p.getCorNum()*13].getPiao(p.getCor())==null) {
            if (p.getPosicao() == 0) return true;
            else return false;
        }
        
        // se tem uma barreira no caminho, o piao nao se move:
        if (p.isBarreiraNoCaminho(i)) return false;
        
        // se tem menos casas à frente que o resultado o dado, o piao nao se move:
        if (65-p.getPosicao() < i) return false;
        
        // se tem mais de um piao no destino e ele nao é o final, o piao nao se move:
        Casa destino = this.search(p.getPosicao() + i, p.getCor());
        if (destino.getQtdPioes() > 1 && destino.getTipo() != Tipo.vitoria) return false;
        
        // FALTANDO!!!
        // se rolou 6 e tem barreiras liberáveis, elas devem ser liberadas:
        
            // (Tomaz) eu não faço a menor ideia de como implementar isso.
            
            
            // essas foram algumas ideias:
            /*
            if (i == 6 && !barreiras.get(p.getCorNum()).isEmpty()) {
                if (this.isBarreiraNoCaminho(barreiras.get(p.getCorNum()).getPiao())) return true;
            }
            */
        /*
        // (Tomaz) Deve estar faltando mais coisa nessa função também?
        return true;    // esse retorno é, em parte, pra garantir que o código compile.
    }
    */
    // captura(Piao p) captura o Piao p.
    public void captura (Piao p) {
        this.search(p).removePiao(p);
        this.getInicial(p.getCor()).inserePiao(p);
        p.reset();
    }
    
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
