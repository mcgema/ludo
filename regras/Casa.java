package regras;

import java.util.*;


// tirar public de tudo quando for pra testar pra valer (depois de terminar as funcionalidades)

// Casa: representa uma casa no tabuleiro.
public class Casa {
    private Set<Piao> set = new HashSet<Piao>();
    private Tipo tipo;
    private Cor cor = null; // (Tomaz) algumas casas (como as iniciais) tem cor para identificar algumas propriedades.
    private int indice;     // (Tomaz) Eu tinha esquecido que isso era um parametro. Talvez de pra simplificar outras funcoes (principalmente em Tabuleiro) usando isso.
    
    // construtores
    public Casa (int i, Tipo t) {
        tipo = t;
        indice = i;
    }
    public Casa (int i, Tipo t, Cor c) {
        tipo = t;
        cor = c;
        indice = i;
    }
    
    // metodos
    
    
    // inserePiao(Piao p) insere o Piao p na Casa.
    public void inserePiao (Piao p) {
        set.add(p);
    }
    
    // removePiao(Piao p) remove o Piao p da Casa.
    public void removePiao (Piao p) {
        set.remove(p);
    }
    
    // getPiao() retorna um Piao "aleatório" da Casa.
    public Piao getPiao () {
        Iterator<Piao> iterator = set.iterator();
        Piao p = iterator.next();
        return p;
    }
    
    // getPiao(Cor c) retorna um Piao "aleatório" da cor c da Casa.
    public Piao getPiao (Cor c) {
        Iterator<Piao> iterator = set.iterator();
        Piao p;
        while (iterator.hasNext()) {
            p = iterator.next();
            if (p.getCor() == c) return p;
        }
        return null;
    }
    
    // getTipo() retorna o Tipo da Casa.
    public Tipo getTipo () {
        return tipo;
    }
    
    // getCor() retorna a Cor da Casa.
    public Cor getCor () {
        return cor;
    }
    
    // getIndice() retorna o indice da Casa.
    // (Tomaz) Nota: acho que isso não foi usado no código... Acabei esquecendo que Casas tinham índice :P
    public int getIndice () {
        return indice;
    }
    
    // getQtdPioes() retorna a quantidade de Pioes da Casa.
    // (Tomaz) Nota: o mesmo objeto Casa é usado para as casas do tabuleiro (com até 2 pioes) e para as casas iniciais e finais (com até 4 piões). Garantir que não tenha excesso de Piões é resultado da implementação.
    public int getQtdPioes () {
        return set.size();
    }
    
    // isEmpty() retorna TRUE se a Casa não tem Piões e FALSE caso a Casa tenha Piões.
    public boolean isEmpty () {
        return set.isEmpty();
    }
    
    // isBarreira() retorna TRUE se a Casa é uma barreira e FALSE se a casa não é uma barreira.
    public boolean isBarreira() {
        if (this.getQtdPioes() == 2 && (this.getTipo() == Tipo.padrao || this.getTipo() == Tipo.retaFinal)) return true;
        return false;
    }
    
    // contains(Piao p) retorna TRUE se o Pião p está na Casa e FALSE caso não esteja.
    public boolean contains (Piao p) {
        return set.contains(p);
    }
    
    // dump() é uma função de teste que deve ser deletada. Ela imprime as informações de uma Casa no terminal.
    public void dump () {
        System.out.printf("[%2d] Tipo = %s\tCor = %s\t",indice,tipo.toString(),cor==null?"-  ":cor.toString());
        System.out.printf("Set (%d) = ",set.size());
        Iterator<Piao> iterator = set.iterator();
        while (iterator.hasNext()) {
            System.out.printf("%s ",iterator.next().dumpString());
        }
        System.out.printf("\n\n");
    }
}
