import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Classe que representa um banco de palavras, permitindo carregar palavras de um arquivo,
 * armazená-las e organizadas por tamanho e fornecer palavras aleatórias não repetidas.
 */

public class BancoDePalavras {
    private Map<Integer, List<String>> palavrasPorTamanho; // Armazena as palavras organizadas por tamanho
    private Set<String> palavrasSelecionadas; // Conjunto para rastrear palavras já utilizadas

    /**
     * Construtor que inicializa o banco de palavras a partir de um arquivo
     * @param caminhoArquivo Caminho do arquivo contendo a lista de palavras
     * @throws IOException Se ocorrer um erro na leitura do arquivo
    */
    public BancoDePalavras(String caminhoArquivo) throws IOException {
        palavrasPorTamanho = new HashMap<>(); 
        palavrasSelecionadas = new HashSet<>(); 
        carregarPalavras(caminhoArquivo);
    }

    
    /**
     * Carrega palavras de um arquivo de texto e as armazena em um mapa organizado por tamanho
     * Apenas palavras com tamanho entre 3 e 14 caracteres são armazenadas
    */
    private void carregarPalavras(String caminhoArquivo) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(caminhoArquivo), StandardCharsets.UTF_8))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                linha = linha.trim().toLowerCase();
                int tamanho = linha.length();

                if (tamanho >= 3 && tamanho <= 14) {
                    palavrasPorTamanho.putIfAbsent(tamanho, new ArrayList<>());
                    palavrasPorTamanho.get(tamanho).add(linha);
                }
            }
        }
    }

    // Retorna uma palavra aleatória de um tamanho específico, garantindo que não seja repetida
    public String obterPalavraAleatoria(int tamanho) {
        // Verifica se existem palavras disponíveis para o tamanho solicitado
        if (!palavrasPorTamanho.containsKey(tamanho) || palavrasPorTamanho.get(tamanho).isEmpty()) {
            return null; // Nenhuma palavra disponível para esse tamanho
        }

        List<String> palavras = palavrasPorTamanho.get(tamanho);
        Collections.shuffle(palavras); // Embaralha a lista para garantir seleção aleatória

        for (String palavra : palavras) {
            // Verifica se a palavra já foi utilizada em alguma rodada anterior
            if (!palavrasSelecionadas.contains(palavra)) {
                palavrasSelecionadas.add(palavra); // Marca a palavra como utilizada
                return palavra; // Retorna a palavra aleatória que ainda não foi selecionada
            }
        }

        return null; // Todas as palavras desse tamanho já foram usadas
    }

    // Verifica se tem palavras disponíveis
    public boolean temPalavrasDisponiveis(int tamanho) {
        return palavrasPorTamanho.containsKey(tamanho) && palavrasPorTamanho.get(tamanho).stream()
                .anyMatch(palavra -> !palavrasSelecionadas.contains(palavra));
    }
}

