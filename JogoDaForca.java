import java.text.Normalizer;
import java.util.HashSet;
import java.util.Set;

/**
 * Classe que  implementa a lógica de um jogo da forca em Java
 * Ela gerencia a palavra secreta, as tentativas do jogador e as regras do jogo
*/

public class JogoDaForca {
    private BancoDePalavras bancoDePalavras;
    private String palavraSecreta;
    private char[] palavraOculta;
    private Set<Character> letrasTentadas;
    private int erros;
    private int tamanhoPalavra;  
    private static final int MAX_ERROS = 7;

    // Construtor da classe 
    public JogoDaForca(BancoDePalavras banco, int tamanhoPalavra) {
        this.bancoDePalavras = banco;
        this.tamanhoPalavra = tamanhoPalavra;
        this.letrasTentadas = new HashSet<>();
        iniciarNovoJogo();
    }

    // Método para remover acentos de uma string
    private String removerAcentos(String texto) {
        if (texto == null) return null;
        return Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("[\\p{M}]", ""); 
    }

    // Inicializa um novo jogo, sorteando uma palavra e resetando variáveis
    public void iniciarNovoJogo() {
        palavraSecreta = bancoDePalavras.obterPalavraAleatoria(tamanhoPalavra);
        
        if (palavraSecreta == null) { // Se o banco não tiver palavras desse tamanho
            throw new IllegalStateException("Nenhuma palavra disponível com o tamanho especificado.");
        }

        palavraSecreta = removerAcentos(palavraSecreta).toLowerCase();
        palavraOculta = new char[palavraSecreta.length()];
        for (int i = 0; i < palavraOculta.length; i++) {
            palavraOculta[i] = '-';
        }
        
        letrasTentadas.clear();
        erros = 0;
    }

    // Método chamado a cada turno para processar a entrada do jogador
    public boolean verificarLetra(char letra) {
        letra = removerAcentos(Character.toString(letra)).toLowerCase().charAt(0);
        
        if (letrasTentadas.contains(letra)) {
            System.out.println("A letra '" + letra + "' já foi testada. Escolha outra.");
            return false; // Retorna false para indicar que a letra já foi usada
        }
    
        letrasTentadas.add(letra); // Adiciona a letra ao conjunto de tentativas
    
        boolean acertou = false;
        for (int i = 0; i < palavraSecreta.length(); i++) {
            if (palavraSecreta.charAt(i) == letra) {
                palavraOculta[i] = letra; // Revela a letra correta na palavra oculta
                acertou = true;
            }
        }
    
        if (!acertou) {
            erros++; // Incrementa o número de erros se a letra não estiver na palavra
        }
        return acertou;
    }

    // Método para obter as letras já tentadas
    public String getLetrasTentadas() {
        return letrasTentadas.toString();
    }

    // Verifica se o jogo terminou por vitória ou derrota
    public boolean jogoEncerrado() {
        return erros >= MAX_ERROS || venceu();
    }

    // Verifica se o jogador venceu (descobriu toda a palavra)
    public boolean venceu() {
        return String.valueOf(palavraOculta).equals(palavraSecreta);
    }

    // Retorna o número de erros cometidos
    public int getErros() {
        return erros;
    }

    // Retorna a palavra oculta com as letras já descobertas
    public String getPalavraOculta() {
        return String.valueOf(palavraOculta);
    }

    // Retorna a palavra secreta 
    public String getPalavraSecreta() {
        return palavraSecreta;
    }
 
    // Retorna o número de tentativas restantes
    public int getTentativasRestantes() {
        return MAX_ERROS - erros;
    }
}



