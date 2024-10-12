import javax.swing.JOptionPane;
import java.util.List;
import java.util.Set;

public class AutomatonMinimizer {

    private JFFParser parser;

    public AutomatonMinimizer(JFFParser parser) {
        this.parser = parser;
    }

    // Função para iniciar o processo de minimização
    public void minimize() {
        if (!parser.isDeterministic()) {
            JOptionPane.showMessageDialog(null, "O autômato não é determinístico. Importe um AFD.");
            return;
        }

        if (hasInaccessibleStates()) {
            JOptionPane.showMessageDialog(null, "O autômato possui estados inacessíveis.");
            return;
        }

        if (!isTotal()) {
            JOptionPane.showMessageDialog(null, "A função programa não é total.");
            return;
        }

        int response = JOptionPane.showConfirmDialog(null, "Deseja prosseguir com a minimização?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            performMinimization();
        } else {
            JOptionPane.showMessageDialog(null, "Minimização cancelada.");
        }
    }

    // Verifica se há estados inacessíveis
    private boolean hasInaccessibleStates() {
        Set<State> accessibleStates = parser.getAccessibleStates();
        List<State> allStates = parser.getStates();
        return accessibleStates.size() != allStates.size();
    }

    // Verifica se a função programa é total
    private boolean isTotal() {
    List<String> alphabet = parser.getAlphabet();
    List<State> states = parser.getStates();

    for (State state : states) {
        for (String symbol : alphabet) {
            // Verifica se a transição existe para o estado atual e o símbolo
            if (parser.getTransition(state, symbol) == null) {
                // Se não houver transição, a função programa não é total
                return false;
            }
        }
    }
    // Se todas as transições estão definidas, a função programa é total
    return true;
    }

    // Método de minimização (a ser implementado)
    private void performMinimization() {
        // Implementação da minimização
        JOptionPane.showMessageDialog(null, "Minimização em andamento...");
    }
}
