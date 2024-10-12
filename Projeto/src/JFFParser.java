import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class JFFParser {

    // Listas para armazenar estados e transições
    private ArrayList<State> states;
    private ArrayList<Transition> transitions;
    private ArrayList<String> alphabet; // Lista para armazenar o alfabeto
    private State initialState; // Estado inicial
    private ArrayList<State> finalStates; // Lista de estados finais

    public JFFParser() {
        // Inicializa as listas
        states = new ArrayList<>();
        transitions = new ArrayList<>();
        alphabet = new ArrayList<>();
        finalStates = new ArrayList<>();
    }

    // Método para processar o arquivo .jff
    public boolean processJFFFile(File file) {
        try {
            // Cria o parser do XML
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);

            // Normaliza o documento (opcional, mas recomendado)
            document.getDocumentElement().normalize();

            // Carrega os estados e as transições
            loadStates(document);
            loadTransitions(document);

            // Arquivo carregado com sucesso
            return true; 

        } catch (Exception e) {
            e.printStackTrace();
            return false; // Retorna false se ocorrer uma exceção
        }
    }

    // Método para carregar os estados do autômato
    private void loadStates(Document document) {
        NodeList stateList = document.getElementsByTagName("state");

        for (int i = 0; i < stateList.getLength(); i++) {
            Node stateNode = stateList.item(i);

            if (stateNode.getNodeType() == Node.ELEMENT_NODE) {
                Element stateElement = (Element) stateNode;
                String id = stateElement.getAttribute("id");
                String name = stateElement.getAttribute("name");

                boolean isInitial = stateElement.getElementsByTagName("initial").getLength() > 0;
                boolean isFinal = stateElement.getElementsByTagName("final").getLength() > 0;

                // Adiciona o estado à lista de estados
                State state = new State(id, name, isInitial, isFinal);
                states.add(state);

                // Se for estado inicial, armazena na variável
                if (isInitial) {
                    initialState = state; // Armazena a referência do estado
                }

                // Se for estado final, adiciona à lista de estados finais
                if (isFinal) {
                    finalStates.add(state); // Armazena a mesma referência do estado
                }
            }
        }
    }

    // Método para carregar as transições do autômato
    private void loadTransitions(Document document) {
        NodeList transitionList = document.getElementsByTagName("transition");

        for (int i = 0; i < transitionList.getLength(); i++) {
            Node transitionNode = transitionList.item(i);

            if (transitionNode.getNodeType() == Node.ELEMENT_NODE) {
                Element transitionElement = (Element) transitionNode;

                String from = transitionElement.getElementsByTagName("from").item(0).getTextContent();
                String to = transitionElement.getElementsByTagName("to").item(0).getTextContent();
                String read = transitionElement.getElementsByTagName("read").item(0).getTextContent();

                // Adiciona a transição à lista de transições
                transitions.add(new Transition(from, to, read));

                // Adiciona o símbolo ao alfabeto, se ainda não estiver presente
                if (!alphabet.contains(read)) {
                    alphabet.add(read);
                }
            }
        }
    }

    public boolean isDeterministic() {
        // Usaremos um mapa para armazenar as transições
        HashMap<String, HashMap<String, String>> transitionMap = new HashMap<>();
    
        for (Transition transition : transitions) {
            String from = transition.getFrom();
            String to = transition.getTo();
            String read = transition.getRead();
    
            // Se houver uma transição vazia (ε), o autômato é AFN
            if (read.isEmpty()) {
                return false;
            }
    
            // Verificar se já existem transições para o estado 'from'
            transitionMap.putIfAbsent(from, new HashMap<>());
    
            // Verifica se já existe uma transição para o símbolo 'read'
            HashMap<String, String> symbolTransitions = transitionMap.get(from);
            
            // Se já houver uma transição para o mesmo símbolo
            if (symbolTransitions.containsKey(read)) {
                // O autômato é AFN se a nova transição leva a um estado diferente
                if (!symbolTransitions.get(read).equals(to)) {
                    return false; // Transições diferentes para o mesmo símbolo a partir do mesmo estado
                }
            }
    
            // Adiciona ou atualiza a transição
            symbolTransitions.put(read, to);
        }
    
        // Se não violou nenhuma condição, o autômato é AFD
        return true;
    }    

    // Método para obter a transição para um determinado estado e símbolo
    public State getTransition(State currentState, String symbol) {
        // Percorre todas as transições
        for (Transition transition : transitions) {
            // Verifica se a transição parte do estado atual e lê o símbolo correto
            if (transition.getFrom().equals(currentState.getId()) && transition.getRead().equals(symbol)) {
                // Retorna o estado de destino como um State
                return states.stream()
                        .filter(s -> s.getId().equals(transition.getTo()))
                        .findFirst()
                        .orElse(null);
            }
        }
        return null; // Retorna null se não houver uma transição correspondente
    }

    public Set<State> getAccessibleStates() {
        Set<State> accessibleStates = new HashSet<>();
        State initialState = getInitialState();

        if (initialState != null) {
            exploreAccessibleStates(initialState, accessibleStates);
        }

        return accessibleStates;
    }

    // Método recursivo para explorar estados acessíveis
    private void exploreAccessibleStates(State currentState, Set<State> accessibleStates) {
        // Adiciona o estado atual ao conjunto de estados acessíveis
        accessibleStates.add(currentState);

        // Percorre todas as transições saindo do estado atual
        for (String symbol : getAlphabet()) {
            State nextState = getTransition(currentState, symbol);
            if (nextState != null && !accessibleStates.contains(nextState)) {
                exploreAccessibleStates(nextState, accessibleStates);
            }
        }
    }

    // Método para obter a lista de estados
    public ArrayList<State> getStates() {
        return states;
    }

    // Método para obter a lista de transições
    public ArrayList<Transition> getTransitions() {
        return transitions;
    }

    // Método para obter o alfabeto
    public ArrayList<String> getAlphabet() {
        return alphabet;
    }

    // Método para obter o estado inicial
    public State getInitialState() {
        return initialState;
    }

    // Método para obter a lista de estados finais
    public ArrayList<State> getFinalStates() {
        return finalStates;
    }
}