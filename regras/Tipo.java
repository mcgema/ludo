package regras;

// Tipo é um tipo enumerado que lista os tipos de casa que existem.
// (Tomaz) Nota: acho que "barreira" não foi implementado, pela dificuldade em desfazer ela depois. Usei um HashSet pra isso (embora estaj incompleto). Talvez tentar tirar e ver se quebra?
public enum Tipo {
    padrao, saida, inicial, vitoria, abrigo, barreira, retaFinal
}
