public class State {
    private String id;
    private String name;
    private boolean isInitial;
    private boolean isFinal;

    public State(String id, String name, boolean isInitial, boolean isFinal) {
        this.id = id;
        this.name = name;
        this.isInitial = isInitial;
        this.isFinal = isFinal;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isInitial() {
        return isInitial;
    }

    public boolean isFinal() {
        return isFinal;
    }

    @Override
    public String toString() {
        return name; // Retorna o nome do estado
    }
}