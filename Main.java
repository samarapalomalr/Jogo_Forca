// Nome: Samara Paloma Lopes Augusto Ribeiro
// Matricula: 22.2.4091

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) { // // Verifica se o usuário passou o caminho do banco de palavras como argumento
            System.err.println("Uso correto: java ForcaGUI <caminho do banco de palavras>");
            System.exit(1); // Encerra o programa se não houver argumento suficiente
        }
        
        // Obtém o caminho do banco de palavras a partir dos argumentos
        String caminhoBanco = args[0];
        
        // Inicia a interface gráfica do jogo dentro da thread de eventos do Swing
        SwingUtilities.invokeLater(() -> new ForcaGui(caminhoBanco));
    }
}

