package regras;

import cores.*;

// Model é a fachada das regras, e é a única classe pública da 1a iteração.
public class Model {
    Tabuleiro tabuleiro = new Tabuleiro();
    boolean emAndamento = true;
    
    // movePiao(corPiao, idPiao, casas) move o "idPiao-ésimo" Pião de cor "corPiao" "casas" casas para a frente.
    // (Tomaz) Está INCOMPLETA: Falta implementar a CAPTURA.
    public void movePiao (Cor corPiao, int idPiao, int casas) {
        if (emAndamento) {
            tabuleiro.move(tabuleiro.getPiao(corPiao, idPiao), casas);
            if (tabuleiro.fim()) {
                emAndamento = false;
                System.out.println("Jogo acabou!");
        }
    }
    
    // podeMoverPiao(corPiao, idPiao, casas) checa se o "idPiao-ésimo" Pião de cor "corPiao" pode se mover "casas" casas para a frente.
    // (Tomaz) Está INCOMPLETA: Falta implementar a interação entre barreiras e o número 6.
    //                          Também falta checar se tem mais algo faltando. Bem possível -- regras estão bem difíceis de ler.
    public boolean podeMoverPiao (Cor corPiao, int idPiao, int casas) {
        if (emAndamento) return tabuleiro.possibleMove(tabuleiro.getPiao(corPiao, idPiao), casas);
        return false;
    }
    
    // lancaDado() lanca um dado virtual de 6 lados, retornando um inteiro dentre {1, 2, 3, 4, 5, 6} com chance pseudo-aleatória.
    // (Tomaz) Está COMPLETA!!!
    public int lancaDado () {
        return Dado.rolar();
    }
}