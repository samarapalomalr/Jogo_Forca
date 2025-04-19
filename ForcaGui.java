import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

// Classe que representa a interface gráfica do Jogo da Forca
public class ForcaGui extends JFrame {
    private JogoDaForca jogo;
    private DesenhoForca desenhoForca;
    private JLabel palavraOcultaLabel;
    private JLabel statusLabel;
    private JLabel placarLabel;
    private JTextField letraInput;
    private JButton tentarBtn;
    private JButton novaPartidaBtn;
    private JButton sairBtn;
    private Placar placar;
    private BancoDePalavras bancoDePalavras;
    private Set<String> palavrasUtilizadas;
    private int tamanhoPalavra;

    // Construtor que inicializa a tela inicial do jogo
    public ForcaGui(String caminhoBanco) {
        mostrarTelaInicial(caminhoBanco);
    }

    // Exibe a tela inicial para o jogador definir o tamanho da palavra
    private void mostrarTelaInicial(String caminhoBanco) {
        JTextField tamanhoInput = new JTextField(2);
        Object[] mensagem = {"Escolha o tamanho da palavra (3-14):", tamanhoInput};
        int opcao = JOptionPane.showConfirmDialog(null, mensagem, "Configuração Inicial", JOptionPane.OK_CANCEL_OPTION);

        if (opcao == JOptionPane.OK_OPTION) {
            try {
                tamanhoPalavra = Integer.parseInt(tamanhoInput.getText());
                if (tamanhoPalavra < 3 || tamanhoPalavra > 14) {
                    throw new NumberFormatException();
                }
                iniciarJogo(caminhoBanco, tamanhoPalavra);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Inválido! Escolha um valor entre 3 e 14.", "Erro", JOptionPane.ERROR_MESSAGE);
                mostrarTelaInicial(caminhoBanco);
            }
        } else {
            System.exit(0);
        }
    }

    // Inicializa o jogo com base no tamanho da palavra selecionado
    private void iniciarJogo(String caminhoBanco, int tamanhoPalavra) {
        try {
            bancoDePalavras = new BancoDePalavras(caminhoBanco);
            jogo = new JogoDaForca(bancoDePalavras, tamanhoPalavra);
            placar = new Placar();
            palavrasUtilizadas = new HashSet<>();

            setTitle("Jogo da Forca");
            setSize(600, 400);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLayout(new BorderLayout());
            getContentPane().setBackground(new Color(34, 139, 34));

            JPanel painelSuperior = new JPanel();
            placarLabel = new JLabel("Total score: 0 | Hits: 0 | Fails: 0");
            statusLabel = new JLabel("| Guesses: 7");
            placarLabel.setForeground(Color.BLACK);
            statusLabel.setForeground(Color.BLACK);

            painelSuperior.add(placarLabel);
            painelSuperior.add(statusLabel);
            add(painelSuperior, BorderLayout.NORTH);

            JPanel painelCentral = new JPanel(new BorderLayout());
            desenhoForca = new DesenhoForca();
            palavraOcultaLabel = new JLabel(jogo.getPalavraOculta(), SwingConstants.CENTER);
            palavraOcultaLabel.setFont(new Font("Arial", Font.BOLD, 24));
            palavraOcultaLabel.setForeground(Color.BLACK);
            painelCentral.add(desenhoForca, BorderLayout.CENTER);
            painelCentral.add(palavraOcultaLabel, BorderLayout.SOUTH);
            add(painelCentral, BorderLayout.CENTER);

            JPanel painelInferior = new JPanel();
            letraInput = new JTextField(2);
            tentarBtn = new JButton("Tentar");
            novaPartidaBtn = new JButton("Nova Partida");
            sairBtn = new JButton("Sair");

            Color verdeEscuro = new Color(34, 139, 34);
            tentarBtn.setBackground(verdeEscuro);
            novaPartidaBtn.setBackground(verdeEscuro);
            sairBtn.setBackground(verdeEscuro);

            painelInferior.add(new JLabel("Digite uma letra: "));
            painelInferior.add(letraInput);
            painelInferior.add(tentarBtn);
            painelInferior.add(novaPartidaBtn);
            painelInferior.add(sairBtn);
            add(painelInferior, BorderLayout.SOUTH);

            tentarBtn.addActionListener(e -> tentarLetra());
            novaPartidaBtn.addActionListener(e -> iniciarNovaPartida());
            sairBtn.addActionListener(e -> sairJogo());

            setVisible(true);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar o banco de palavras: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    // Garante que cada turno seja processado ao pressionar o botão "Tentar"
    private void tentarLetra() {
        // Impede que o jogador continue tentando após o jogo encerrar
        if (jogo.jogoEncerrado()) {
            JOptionPane.showMessageDialog(this, "O jogo já terminou! Inicie uma nova partida.", "Jogo Encerrado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String letra = letraInput.getText().toLowerCase();
    
        if (letra.length() == 1 && Character.isLetter(letra.charAt(0))) {
            char charLetra = letra.charAt(0);
    
            // Converte para String antes de verificar se já foi tentada
            if (jogo.getLetrasTentadas().contains(String.valueOf(charLetra))) {
                JOptionPane.showMessageDialog(this, "A letra '" + charLetra + "' já foi testada. Escolha outra.", 
                                              "Letra Repetida", JOptionPane.WARNING_MESSAGE);
                letraInput.setText("");
                return;
            }
    
            // Se a letra não foi usada, tenta no jogo
            jogo.verificarLetra(charLetra);
            letraInput.setText("");
            atualizarInterface();
    
            if (jogo.jogoEncerrado()) {
                if (jogo.venceu()) {
                    placar.palavraAcertada(jogo.getPalavraSecreta().length());
                    JOptionPane.showMessageDialog(this, "Parabéns, você ganhou!");
                } else {
                    placar.palavraErrada();
                    JOptionPane.showMessageDialog(this, "Você perdeu! A palavra era: " + jogo.getPalavraSecreta());
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Digite apenas uma letra válida!", "Entrada Inválida", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Inicializa uma nova partida
    private void iniciarNovaPartida() {
        JTextField tamanhoInput = new JTextField(2);
        Object[] mensagem = {"Escolha o tamanho da palavra (3-14):", tamanhoInput};
        int opcao = JOptionPane.showConfirmDialog(null, mensagem, "Nova Partida", JOptionPane.OK_CANCEL_OPTION);

        if (opcao == JOptionPane.OK_OPTION) {
            try {
                int novoTamanho = Integer.parseInt(tamanhoInput.getText());
                if (novoTamanho < 3 || novoTamanho > 14) {
                    throw new NumberFormatException();
                }

                tamanhoPalavra = novoTamanho;
                jogo = new JogoDaForca(bancoDePalavras, tamanhoPalavra);
                palavrasUtilizadas.clear();
                atualizarInterface();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Tamanho inválido! Escolha um valor entre 3 e 14.", "Erro", JOptionPane.ERROR_MESSAGE);
                iniciarNovaPartida();
            }
        }
    }

    private void sairJogo() {
        JOptionPane.showMessageDialog(this, "Jogo encerrado!\n" + placar.getEstatisticas());
        System.exit(0);
    }

    private void atualizarInterface() {
        palavraOcultaLabel.setText(jogo.getPalavraOculta());
        statusLabel.setText("Guesses: " + jogo.getTentativasRestantes());
        placarLabel.setText("Total Score: " + placar.getScore() + " | Hits: " + placar.getHits() + " | Fails: " + placar.getFails());
        desenhoForca.setErros(jogo.getErros());
        desenhoForca.repaint();
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Uso correto: java ForcaGUI <caminho do banco de palavras>");
            System.exit(1);
        }
        
        String caminhoBanco = args[0];
        
        SwingUtilities.invokeLater(() -> new ForcaGui(caminhoBanco));
    } 
}









