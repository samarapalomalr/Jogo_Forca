public class Placar {
    private int score;
    private int hits;
    private int fails;

    public Placar() {
        score = 0;
        hits = 0;
        fails = 0;
    }

    // Método para quando uma palavra for acertada
    public void palavraAcertada(int tamanho) {
        hits++;
        int pontos = 10;
        score += Math.max(pontos, 0); // Evita score negativo
    }

    // Método para quando uma palavra for errada
    public void palavraErrada() {
        fails++;
    }

    // Métodos para obter os valores de score, hits e fails
    public int getScore() {
        return score;
    }

    public int getHits() {
        return hits;
    }

    public int getFails() {
        return fails;
    }

    // Método para exibir as estatísticas finais do jogador
    public String getEstatisticas() {
        return "Total Score: " + score + "\nHits: " + hits + "\nFails: " + fails;
    }
}


