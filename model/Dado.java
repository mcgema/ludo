package model;

import java.util.Random;

// Dado é um dado virtual de 6 lados.
class Dado {
    static Random rand = new Random(); 
    
    // rolar() rola o dado e retorna um inteiro pseudo-aleatório entre 1 e 6 (inclusivo).
    protected static int rolar () {
        int resultado = 1+rand.nextInt(6);
        System.out.printf("Dado rolado: %d\n",resultado);
        return resultado;
    } 
}
