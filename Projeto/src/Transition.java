public class Transition {
    private String fromState; // Estado de origem
    private String toState;   // Estado de destino
    private String symbol;    // Símbolo de transição

    // Construtor
    public Transition(String fromState, String toState, String symbol) {
        this.fromState = fromState;
        this.toState = toState;
        this.symbol = symbol;
    }

    // Métodos para obter os valores dos atributos
    public String getFrom() {
        return fromState; // Retorna o estado de origem
    }

    public String getTo() {
        return toState;   // Retorna o estado de destino
    }

    public String getRead() {
        return symbol;    // Retorna o símbolo de transição
    }
}