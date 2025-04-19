import javax.swing.*;
import java.awt.*;

/**
 * Classe responsável por desenhar o jogo da forca 
 * Ela exibe a estrutura da forca e desenha progressivamente o boneco conforme os erros 
*/

public class DesenhoForca extends JPanel {
    private int erros = 0; // Contador de erros
    private final Color corBoneco = new Color(34, 139, 34); 

    // Atualiza o número de erros e redesenha a forca e o boneco
    public void setErros(int erros) {
        this.erros = erros;
        repaint(); // Atualiza o desenho
    }

    /**
     * Método responsável por desenhar a forca e o boneco 
     * @param g Objeto Graphics usado para desenhar na tela
    */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Estrutura da forca
        g.setColor(Color.BLACK);
        g.drawLine(50, 200, 150, 200);
        g.drawLine(100, 200, 100, 50);
        g.drawLine(100, 50, 200, 50);
        g.drawLine(200, 50, 200, 80);

        // Cor do boneco
        g.setColor(corBoneco);

        // Desenha partes do boneco conforme os erros
        if (erros >= 1) g.drawOval(180, 80, 40, 40); // Cabeça
        if (erros >= 2) g.drawLine(200, 120, 200, 170); // Tronco
        if (erros >= 3) g.drawLine(200, 130, 170, 160); // Braço esquerdo
        if (erros >= 4) g.drawLine(200, 130, 230, 160); // Braço direito
        if (erros >= 5) g.drawLine(200, 170, 170, 200); // Perna esquerda
        if (erros >= 6) g.drawLine(200, 170, 230, 200); // Perna direita

        // Boneco riscado no último erro
        if (erros >= 7) {
            g.drawLine(190, 80, 210, 100);
            g.drawLine(210, 80, 190, 100);
        }
    }
}

