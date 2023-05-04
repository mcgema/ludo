package regras;

import java.util.*;

// Tabuleiro representa o Tabuleiro de Ludo, e implementa 2 dos 3 métodos passados para Model.
public class Tabuleiro {
    // obs: com o tabuleiro correto em mãos, recontar TODO NÚMERO DE CASAS!!!

    // (Tomaz) Todas essas variáveis estão aqui só até o Ivan liberar o tabuleiro oficial. Depois disso, SUBSTITUIR POR NÚMEROS!!
    int abrigos[] = {7, 11, 22, 26, 37, 41, 52, 56, 0};
    int TOTAL_CASAS_PERCURSO = 60;
    int TOTAL_CASAS_RETA_FINAL = 6;
    int TOTAL_CASAS_ENTRE_SAIDA_E_RETA_FINAL = 2;
    
    // Tabuleiro é representado por Arrays de Casas.
    Casa percurso  []   = new Casa [TOTAL_CASAS_PERCURSO];
    Casa iniciais  []   = new Casa [4];
    Casa finais    []   = new Casa [4];
    Casa retaFinal [][] = new Casa [4][TOTAL_CASAS_RETA_FINAL];
    
    // Matriz de Piões guarda os "ponteiros" para todos os Piões, garantido que sempre serão facilmente acessíveis pela sua cor e índice.
    // (Tomaz) Guardado como arrayPioes[cor][indice]
    Piao arrayPioes[][] = new Piao[4][4];
    
    // Conjunto de Casas guarda quais são as casas que tem barreiras.
    Set barreiras[] = new HashSet[4];
    
    // bloco de inicialização
    {
        // inicializa Casas:
        int j = 0;
        for (int i = 0; i < TOTAL_CASAS_PERCURSO; i++) {
            if (i%(TOTAL_CASAS_PERCURSO/4) == 0) {}
            
            // abrigos:
            else if (abrigos[j] == i) percurso[abrigos[j++]] = new Casa(i,Tipo.abrigo);
            
            // casas padrão:
            else percurso[i] = new Casa(i,Tipo.padrao);
        }
        
        for (Cor c: Cor.values()) {
            int corNum = c.ordinal();
            
            // casas de saída:
            percurso [corNum*(TOTAL_CASAS_PERCURSO/4)] = new Casa(corNum*(TOTAL_CASAS_PERCURSO/4), Tipo.saida, c);
            
            // casas iniciais (as grandes de 4 piões):
            iniciais [corNum]    = new Casa(0, Tipo.inicial, c);
            
            // casas finais ("vitoria")
            // (Tomaz) Eu usei "vitoria" em vez de "final", porque "final" é uma palavra reservada e buga o código.
            finais   [corNum]    = new Casa(TOTAL_CASAS_RETA_FINAL+1, Tipo.vitoria, c);
            
            // casas das retas finais (exclusivas para uma cor cada:
            for (int i = 0; i < TOTAL_CASAS_RETA_FINAL; i++) {
                retaFinal[corNum][i] = new Casa(i+1,Tipo.retaFinal, c);
            }
            
            // inicializa os Piões:
            for (int i = 0; i < 4; i++) {
                arrayPioes[c.ordinal()][i] = new Piao(c,i, this);
                this.getInicial(c).inserePiao(arrayPioes[c.ordinal()][i]);
            }
            
            // inicializa o conjunto de barreiras:
            barreiras[c.ordinal()] = new HashSet<Casa>();
        }
    }
    
    // getInicial(Cor cor) retorna a Casa inicial da Cor cor
    public Casa getInicial (Cor cor) {
        return iniciais[cor.ordinal()];
    }
    
    // getFinal(Cor cor) retorna a Casa inicial da Cor cor
    public Casa getFinal (Cor cor) {
        return finais[cor.ordinal()];
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
        int corNum = corJogador.ordinal();
        
        // (Tomaz) Confia em mim: depois de trocar para números isso fica bem mais legível. Usar CTRL+H pra fazer isso quando for.
        if      (pos == 0 ) return iniciais  [corNum];
        else if (pos == TOTAL_CASAS_RETA_FINAL+TOTAL_CASAS_PERCURSO+1-TOTAL_CASAS_ENTRE_SAIDA_E_RETA_FINAL) return finais    [corNum];
        else if (pos >  TOTAL_CASAS_PERCURSO-TOTAL_CASAS_ENTRE_SAIDA_E_RETA_FINAL) return retaFinal [corNum][pos-(TOTAL_CASAS_PERCURSO+1-TOTAL_CASAS_ENTRE_SAIDA_E_RETA_FINAL)];
        else return percurso[((TOTAL_CASAS_PERCURSO/4)*corNum+pos-1)%TOTAL_CASAS_PERCURSO];
    }
    
    // search(Piao p) é polimórfica com a outra search(), e retorna a Casa em que o Pião p está.
    public Casa search (Piao p) {
        return this.search(p.getPosicao(), p.getCor());
    }
    
    // move(Piao p, int qtdCasas) move o Pião p em qtdCasas Casas.
    // (Tomaz) Essa função realmente altera a Casa em que o Pião p está!!
    public void move (Piao p, int qtdCasas) {
        Casa inicial = this.search(p);
        if (inicial.isBarreira()) barreiras[p.getCorNum()].remove(inicial); // caso isso desfaça uma barreira ela é excluída do conjunto
        inicial.removePiao(p);
        p.move(qtdCasas);
        Casa destino = this.search(p);
        destino.inserePiao(p);
        if (destino.isBarreira()) barreiras[p.getCorNum()].add(destino);    // caso isso crie uma barreira ela é salva no conjunto
        if (destino.getQtdPioes() == 4) this.termina();
        
        // (Tomaz) FALTA UMA CLÁUSULA PARA SE ISSO CAPTURA UM PIÃO OPONENTE!!!!!!!! (Lembrando da função captura(Piao p) )
    }   

    // possibleMove(Piao p, int i) está INCOMPLETA. Ela DEVE retornar TRUE se o Pião p pode se mover i casas e FALSE caso não possa.
    // (Tomaz) Nota: Não sei se essa é a melhor implementação pra isso. Se quiserem fazer totalmente diferente, sintam-se à vontade. Tenho backups de tudo se precisar.

    public boolean possibleMove (Piao p, int i) {
        // se rolou 5 e tem pioes na casa inicial e eles podem sair, só eles podem mover:
        if (i == 5 && this.getInicial(p.getCor()).getQtdPioes() > 0 && percurso[p.getCorNum()*(TOTAL_CASAS_PERCURSO/4)].getPiao(p.getCor())==null) {
            if (p.getPosicao() == 0) return true;
            else return false;
        }
        
        // se tem uma barreira no caminho, o piao nao se move:
        if (p.isBarreiraNoCaminho(i)) return false;
        
        // se tem menos casas à frente que o resultado o dado, o piao nao se move:
        if (65-p.getPosicao() < i) return false;
        
        // se tem mais de um piao no destino e ele nao é o final, o piao nao se move:
        Casa destino = this.search(p.getPosicao() + i, p.getCor());
        if (destino.getQtdPioes > 1 && destino.Tipo != Tipo.vitoria) return false;
        
        // FALTANDO!!!
        // se rolou 6 e tem barreiras liberáveis, elas devem ser liberadas:
        
            // (Tomaz) eu não faço a menor ideia de como implementar isso.
            
            
            // essas foram algumas ideias:
            /*
            if (i == 6 && !barreiras[p.getCorNum()].isEmpty()) {
                if (this.isBarreiraNoCaminho(barreiras[p.getCorNum()].getPiao())) return true;
            }
            */
        
        // (Tomaz) Deve estar faltando mais coisa nessa função também?
        return true;    // esse retorno é, em parte, pra garantir que o código compile.
    }
    
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
        
    }
}
