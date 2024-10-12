import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class Main {
    private static JFFParser parser;

    public static void main(String[] args) {
        parser = new JFFParser();
        createAndShowGUI();
    }

    private static void createAndShowGUI() {
        // Criação do JFrame
        JFrame frame = new JFrame("Minimização de AFD");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400); // Aumenta o tamanho da janela

        // Usaremos GridBagLayout para centralizar os componentes
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10); // Adiciona espaçamento entre os componentes

        // Título
        JLabel titleLabel = new JLabel("Minimização de AFD", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Aumenta o tamanho da fonte
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        frame.add(titleLabel, gbc);

        // Descrição
        JLabel descriptionLabel = new JLabel("Importe um arquivo AFD para minimizá-lo", SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        frame.add(descriptionLabel, gbc);

        // Botão para importar arquivo
        JButton importButton = new JButton("Importar arquivo .jff");
        importButton.setPreferredSize(new Dimension(200, 50)); // Define o tamanho do botão
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        frame.add(importButton, gbc);

        // Mensagem de carregamento
        JLabel loadingLabel = new JLabel("", SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        frame.add(loadingLabel, gbc);

        // Mensagem de sucesso ou falha
        JLabel resultLabel = new JLabel("", SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        frame.add(resultLabel, gbc);

        // Ação do botão
        importButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Mostra um diálogo para escolher o arquivo
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(frame);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    loadingLabel.setText("Importando arquivo...");
                    resultLabel.setText("");

                    // Processa o arquivo
                    boolean success = parser.processJFFFile(selectedFile);

                    // Verifica se o arquivo foi processado com sucesso
                    if (success) {
                        // Verifica se é um AFN
                        if (!parser.isDeterministic()) {
                            loadingLabel.setText("");
                            resultLabel.setText("O autômato é um AFN. Por favor, importe um AFD.");
                        } else {
                            loadingLabel.setText("");
                            resultLabel.setText("Arquivo AFD importado com sucesso!");

                            // Chama a minimização
                            AutomatonMinimizer minimizer = new AutomatonMinimizer(parser);
                            minimizer.minimize();
                        }
                    } else {
                        loadingLabel.setText("");
                        resultLabel.setText("Falha na importação. Tente novamente.");
                    }
                }
            }
        });

        // Exibe a janela
        frame.setVisible(true);
    }

    private static void displayAutomatonDefinition(JFFParser parser, JLabel resultLabel) {
        // Cria um StringBuilder para armazenar a definição formal do autômato
        StringBuilder definitionBuilder = new StringBuilder("<html>Definição formal do autômato:<br>");
        
        // Estados
        definitionBuilder.append("Q: {");
        for (int i = 0; i < parser.getStates().size(); i++) {
            definitionBuilder.append(parser.getStates().get(i).toString());
            if (i < parser.getStates().size() - 1) {
                definitionBuilder.append(", ");
            }
        }
        definitionBuilder.append("}<br>");
    
        // Alfabeto
        definitionBuilder.append("Σ: {");
        for (int i = 0; i < parser.getAlphabet().size(); i++) {
            definitionBuilder.append(parser.getAlphabet().get(i));
            if (i < parser.getAlphabet().size() - 1) {
                definitionBuilder.append(", ");
            }
        }
        definitionBuilder.append("}<br>");
    
        // Transições
        definitionBuilder.append("Δ: {<br>");
        for (Transition transition : parser.getTransitions()) {
            definitionBuilder.append("(")
                             .append(transition.getFrom()).append(", ")
                             .append(transition.getRead()).append(" → ")
                             .append(transition.getTo()).append(")<br>");
        }
        definitionBuilder.append("}<br>");
    
        // Estado inicial
        State initialState = parser.getInitialState();
        definitionBuilder.append("q0: ").append(initialState != null ? initialState.toString() : "não definido").append("<br>");
    
        // Estados finais
        definitionBuilder.append("F: {");
        for (int i = 0; i < parser.getFinalStates().size(); i++) {
            definitionBuilder.append(parser.getFinalStates().get(i).toString());
            if (i < parser.getFinalStates().size() - 1) {
                definitionBuilder.append(", ");
            }
        }
        definitionBuilder.append("}<br>");
    
        definitionBuilder.append("</html>");
    
        // Mostra a definição formal em um diálogo e limpa o resultado após fechamento
        JOptionPane.showMessageDialog(null, definitionBuilder.toString());
        resultLabel.setText("");  // Limpa o texto de sucesso ao fechar o diálogo
    }    
}