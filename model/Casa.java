package model;

import java.util.*;
import cores.*;

// Casa: representa uma casa no tabuleiro.
class Casa {
    private Set<Piao> set = new HashSet<Piao>();
    private Tipo tipo;
    private Cor cor = null;
    private int indice;
    
    // construtores
    protected Casa (int i, Tipo t) {
        tipo = t;
        indice = i;
    }
    protected Casa (int i, Tipo t, Cor c) {
        tipo = t;
        cor = c;
        indice = i;
    }
    
    // metodos
    
    
    // inserePiao(Piao p) insere o Piao p na Casa.
    protected void inserePiao (Piao p) {
        set.add(p);
    }
    
    // removePiao(Piao p) remove o Piao p da Casa.
    protected void removePiao (Piao p) {
        set.remove(p);
    }
    
    // getPiao() retorna um Piao "aleatório" da Casa.
    protected Piao getPiao () {
        Iterator<Piao> iterator = set.iterator();
        Piao p = iterator.next();
        return p;
    }
    
    // getPiao(Cor c) retorna um Piao "aleatório" da cor c da Casa.
    protected Piao getPiao (Cor c) {
        Iterator<Piao> iterator = set.iterator();
        Piao p;
        while (iterator.hasNext()) {
            p = iterator.next();
            if (p.getCor() == c) return p;
        }
        return null;
    }
    
    // getTipo() retorna o Tipo da Casa.
    protected Tipo getTipo () {
        return tipo;
    }
    
    // getCor() retorna a Cor da Casa.
    protected Cor getCor () {
        return cor;
    }
    
    // getIndice() retorna o indice da Casa.
    // nao foi utilizada ainda
    protected int getIndice () {
        return indice;
    }
    
    // getQtdPioes() retorna a quantidade de Pioes da Casa.
    protected int getQtdPioes () {
        return set.size();
    }
    
    // isEmpty() retorna TRUE se a Casa não tem Piões e FALSE caso a Casa tenha Piões.
    protected boolean isEmpty () {
        return set.isEmpty();
    }
    
    // isBarreira() retorna TRUE se a Casa é uma barreira e FALSE se a casa não é uma barreira.
    protected boolean isBarreira() {
        if (this.getQtdPioes() == 2 && (this.getTipo() == Tipo.padrao || this.getTipo() == Tipo.retaFinal)) return true;
        return false;
    }
    
    // contains(Piao p) retorna TRUE se o Pião p está na Casa e FALSE caso não esteja.
    protected boolean contains (Piao p) {
        return set.contains(p);
    }
    
    // dump() é uma função de teste que deve ser deletada. Ela imprime as informações de uma Casa no terminal.
    protected void dump () {
        System.out.printf("[%2d] Tipo = %s\tCor = %s\t",indice,tipo==Tipo.padrao?"-       ":tipo.toString(),cor==null?"-  ":cor.toString());
        System.out.printf("Set (%d) = ",set.size());
        Iterator<Piao> iterator = set.iterator();
        while (iterator.hasNext()) {
            System.out.printf("%s ",iterator.next().dumpString());
        }
        System.out.printf("\t\t Barreiras = ");
    }
}
