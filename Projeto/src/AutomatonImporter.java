import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class AutomatonImporter extends JFrame {

    private JButton importButton;

    public AutomatonImporter() {
        super("Automaton Importer");

        // Configurações básicas da janela
        setLayout(new FlowLayout());

        // Botão para importar o arquivo .jff
        importButton = new JButton("Import .jff File");
        add(importButton);

        // Ação do botão ao ser clicado
        importButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);

                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    // Chama o método para processar o arquivo .jff
                    JFFParser parser = new JFFParser();
                    parser.processJFFFile(selectedFile);
                }
            }
        });

        // Configurações da janela
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}