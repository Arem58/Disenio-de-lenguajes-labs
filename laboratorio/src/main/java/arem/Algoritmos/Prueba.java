package arem.Algoritmos;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Prueba {
    private int stateCount;
    private Set<Character> inputSymbols;
    private Set<Integer> acceptStates;
    private Map<Integer, Map<Character, Set<Integer>>> transitions;

    public Prueba() {
        stateCount = 0;
        inputSymbols = new HashSet<>();
        acceptStates = new HashSet<>();
        transitions = new HashMap<>();
    }

    public int createState() {
        int state = stateCount;
        stateCount++;
        transitions.put(state, new HashMap<>());
        return state;
    }

    public void addInputSymbol(char symbol) {
        inputSymbols.add(symbol);
    }

    public void setAcceptState(int state) {
        acceptStates.add(state);
    }

    public void addTransition(int fromState, char symbol, int toState) {
        Map<Character, Set<Integer>> fromStateTransitions = transitions.get(fromState);
        if (fromStateTransitions.containsKey(symbol)) {
            fromStateTransitions.get(symbol).add(toState);
        } else {
            Set<Integer> toStates = new HashSet<>();
            toStates.add(toState);
            fromStateTransitions.put(symbol, toStates);
        }
    }

    public static Prueba kleene(Prueba a) {
        Prueba kleeneA = new Prueba();

        int startState = kleeneA.createState();
        kleeneA.setAcceptState(startState);
        kleeneA.setAcceptState(a.stateCount);

        kleeneA.addTransition(startState, ' ', a.stateCount);
        kleeneA.addTransition(a.stateCount, ' ', startState);

        for (Map.Entry<Integer, Map<Character, Set<Integer>>> entry : a.transitions.entrySet()) {
            int fromState = entry.getKey();
            kleeneA.createState();

            if (a.acceptStates.contains(fromState)) {
                kleeneA.addTransition(fromState, ' ', a.stateCount);
                kleeneA.addTransition(fromState, ' ', kleeneA.stateCount - 1);
            } else {
                kleeneA.addTransition(fromState, ' ', kleeneA.stateCount - 1);
            }

            for (Map.Entry<Character, Set<Integer>> transition : entry.getValue().entrySet()) {
                char symbol = transition.getKey();
                Set<Integer> toStates = transition.getValue();

                for (int toState : toStates) {
                    kleeneA.addInputSymbol(symbol);
                    kleeneA.addTransition(kleeneA.stateCount - 1, symbol, toState);
                }
            }
        }

        return kleeneA;
    }

    public static Prueba concat(Prueba a1, Prueba a2) {
        Prueba concatA = new Prueba();

        for (int i = 0; i < a1.stateCount; i++) {
            concatA.createState();
        }

        for (int i = 0; i < a2.stateCount; i++) {
            concatA.createState();
        }

        concatA.setAcceptState(a1.stateCount + a2.stateCount - 1);

        for (Map.Entry<Integer, Map<Character, Set<Integer>>> entry : a1.transitions.entrySet()) {
            int fromState = entry.getKey();

            if (a1.acceptStates.contains(fromState)) {
                for (int toState : entry.getValue().getOrDefault(' ', Collections.emptySet())) {
                    concatA.addTransition(fromState, ' ', a2.stateCount);
                    concatA.addTransition(toState + a2.stateCount, ' ', a2.stateCount);
                }
            }

            for (Map.Entry<Character, Set<Integer>>
            transition : entry.getValue().entrySet()) {
                char symbol = transition.getKey();
                Set<Integer> toStates = transition.getValue();
    
                for (int toState : toStates) {
                    concatA.addInputSymbol(symbol);
                    concatA.addTransition(fromState, symbol, toState);
                }
            }
        }
    
        for (Map.Entry<Integer, Map<Character, Set<Integer>>> entry : a2.transitions.entrySet()) {
            int fromState = entry.getKey();
    
            if (a2.acceptStates.contains(fromState)) {
                concatA.setAcceptState(fromState + a1.stateCount);
            }
    
            for (Map.Entry<Character, Set<Integer>> transition : entry.getValue().entrySet()) {
                char symbol = transition.getKey();
                Set<Integer> toStates = transition.getValue();
    
                for (int toState : toStates) {
                    concatA.addInputSymbol(symbol);
                    concatA.addTransition(fromState + a1.stateCount, symbol, toState + a1.stateCount);
                }
            }
        }
    
        return concatA;
    }
    
    public static Prueba or(Prueba a1, Prueba a2) {
        Prueba orA = new Prueba();
    
        int startState = orA.createState();
        int endState = orA.createState();
    
        orA.setAcceptState(endState);
    
        orA.addTransition(startState, ' ',  a1.stateCount);
        orA.addTransition(startState, ' ', a2.stateCount + 1);
    
        for (int i = 0; i < a1.stateCount; i++) {
            orA.createState();
        }
    
        for (int i = 0; i < a2.stateCount; i++) {
            orA.createState();
        }
    
        for (Map.Entry<Integer, Map<Character, Set<Integer>>> entry : a1.transitions.entrySet()) {
            int fromState = entry.getKey();
    
            if (a1.acceptStates.contains(fromState)) {
                orA.addTransition(fromState, ' ', endState);
            }
    
            for (Map.Entry<Character, Set<Integer>> transition : entry.getValue().entrySet()) {
                char symbol = transition.getKey();
                Set<Integer> toStates = transition.getValue();
    
                for (int toState : toStates) {
                    orA.addInputSymbol(symbol);
                    orA.addTransition(fromState + 2, symbol, toState + 2);
                }
            }
        }
    
        for (Map.Entry<Integer, Map<Character, Set<Integer>>> entry : a2.transitions.entrySet()) {
            int fromState = entry.getKey();
    
            if (a2.acceptStates.contains(fromState)) {
                orA.addTransition(fromState + a1.stateCount + 1, ' ', endState);
            }
    
            for (Map.Entry<Character, Set<Integer>> transition : entry.getValue().entrySet()) {
                char symbol = transition.getKey();
                Set<Integer> toStates = transition.getValue();
    
                for (int toState : toStates) {
                    orA.addInputSymbol(symbol);
                    orA.addTransition(fromState + a1.stateCount + 3, symbol, toState + a1.stateCount + 3);
                }
            }
        }
    
        return orA;
    }
    
    public Set<Integer> getAcceptStates() {
        return acceptStates;
    }
    
    public boolean accepts(String input) {
        Set<Integer> currentStates = new HashSet<>();
        currentStates.add(0);
    
        for (char symbol : input.toCharArray()) {
            Set<Integer> nextStates = new HashSet<>();
            for (int state : currentStates) {
                if (transitions.containsKey(state) && transitions.get(state).containsKey(symbol)) {
                    nextStates.addAll(transitions.get(state).get(symbol));
                }
            }
            currentStates = nextStates;
        }

        for (int state : currentStates) {
            if (acceptStates.contains(state)) {
                return true;
            }
        }
    
        return false;
    }
    
    public static void main(String[] args) {
        Prueba a = new Prueba();
        a.createState();
        a.createState();
        a.addInputSymbol('0');
        a.addInputSymbol('1');
    
        a.addTransition(0, '0', 1);
        a.addTransition(0, '1', 4);
        a.addTransition(1, '0', 2);
        a.addTransition(1, '1', 4);
        a.addTransition(2, '0', 2);
        a.addTransition(2, '1', 3);
        a.addTransition(3, '0', 3);
        a.addTransition(3, '1', 4);
        a.setAcceptState(4);
    
        Prueba b = new Prueba();
        b.addInputSymbol('0');
        b.addInputSymbol('1');
    
        b.addTransition(0, '0', 0);
        b.addTransition(0, '1', 1);
        b.addTransition(1, '0', 1);
        b.addTransition(1, '1', 1);
        b.setAcceptState(1);
    
        System.out.println("Automaton A:");
        System.out.println(a);
    
        System.out.println("Automaton B:");
        System.out.println(b);
    
        Prueba concatAB = Prueba.concat(a, b);
        System.out.println("Concatenation of A and B:");
        System.out.println(concatAB);
    
        Prueba orAB = Prueba.or(a, b);
        System.out.println("Or of A and B:");
        System.out.println(orAB);
    
        Prueba kleeneA = Prueba.kleene(a);
        System.out.println("Kleene closure of A:");
        System.out.println(kleeneA);
    
        System.out.println("Input 001 accepted by A: " + a.accepts("001"));
        System.out.println("Input 001 accepted by B: " + b.accepts("001"));
        System.out.println("Input 001 accepted by A or B: " + orAB.accepts("001"));
        System.out.println("Input 001 accepted by A concat B: " + concatAB.accepts("001"));
        System.out.println("Input 001 accepted by A kleene: " + kleeneA.accepts("001"));
    
        System.out.println("Input 111 accepted by A: " + a.accepts("111"));
        System.out.println("Input 111 accepted by B: " + b.accepts("111"));
        System.out.println("Input 111 accepted by A or B: " + orAB.accepts("111"));
        System.out.println("Input 111 accepted by A concat B: " + concatAB.accepts("111"));
        System.out.println("Input 111 accepted by A kleene: " + kleeneA.accepts("111"));
    }
}
        