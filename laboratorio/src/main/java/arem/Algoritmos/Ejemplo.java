package arem.Algoritmos;

import java.util.*;

public class Ejemplo {

    public static Map<Integer, Set<Integer>> postfixToNFA(String postfix) {
        Deque<Map<Integer, Set<Integer>>> stack = new ArrayDeque<>();
        int stateCount = 0;

        for (char c : postfix.toCharArray()) {
            switch (c) {
                case '.':
                    Map<Integer, Set<Integer>> bTransitions = stack.pop();
                    Map<Integer, Set<Integer>> aTransitions = stack.pop();

                    for (int aState : aTransitions.keySet()) {
                        Set<Integer> epsilonTransitions = bTransitions.getOrDefault(aState, new HashSet<Integer>());
                        epsilonTransitions.addAll(aTransitions.get(aState));
                        bTransitions.put(aState, epsilonTransitions);
                    }

                    stack.push(bTransitions);
                    break;

                case '|':
                    Map<Integer, Set<Integer>> dTransitions = stack.pop();
                    Map<Integer, Set<Integer>> cTransitions = stack.pop();

                    Map<Integer, Set<Integer>> unionTransitions = new HashMap<>();
                    unionTransitions.putAll(cTransitions);
                    unionTransitions.putAll(dTransitions);
                    unionTransitions.put(stateCount, Collections.emptySet());

                    Set<Integer> cEpsilonTransitions = unionTransitions.getOrDefault(stateCount - 1, new HashSet<Integer>());
                    cEpsilonTransitions.add(stateCount);
                    unionTransitions.put(stateCount - 1, cEpsilonTransitions);

                    Set<Integer> dEpsilonTransitions = unionTransitions.getOrDefault(stateCount - 2, new HashSet<Integer>());
                    dEpsilonTransitions.add(stateCount);
                    unionTransitions.put(stateCount - 2, dEpsilonTransitions);

                    stack.push(unionTransitions);
                    stateCount++;
                    break;

                case '*':
                    Map<Integer, Set<Integer>> eTransitions = stack.pop();

                    Set<Integer> epsilonTransitions = eTransitions.getOrDefault(stateCount - 1, new HashSet<Integer>());
                    epsilonTransitions.add(stateCount);
                    eTransitions.put(stateCount - 1, epsilonTransitions);

                    epsilonTransitions = eTransitions.getOrDefault(stateCount - 1, new HashSet<Integer>());
                    epsilonTransitions.add(stateCount);
                    eTransitions.put(stateCount, Collections.emptySet());

                    stack.push(eTransitions);
                    stateCount++;
                    break;

                default:
                    Map<Integer, Set<Integer>> transitions = new HashMap<>();
                    transitions.put(stateCount, Collections.singleton(stateCount + 1));
                    stack.push(transitions);
                    stateCount++;
                    transitions = new HashMap<>();
                    transitions.put(stateCount - 1, Collections.singleton(stateCount));
                    stack.push(transitions);
                    stateCount++;
                    Map<Integer, Set<Integer>> transition = new HashMap<>();
                    transition.put(stateCount - 2, Collections.singleton(stateCount - 1));
                    stack.push(transition);
                    stateCount++;
                    break;
            }
        }

        Map<Integer, Set<Integer>> startTransitions = stack.pop();
        Map<Integer, Set<Integer>> endTransitions = Collections.singletonMap(stateCount, Collections.emptySet());
        Map<Integer, Set<Integer>> transitions = new HashMap<>();
        transitions.putAll(startTransitions);
        transitions.putAll(endTransitions);

        for (int fromState : startTransitions.keySet()) {
            Set<Integer> epsilonTransitions = transitions.getOrDefault(fromState, new HashSet<Integer>());
            epsilonTransitions.add(stateCount);
            transitions.put(fromState, epsilonTransitions);
        }

        return transitions;
    }

    public static void main(String[] args) {
        String postfix = "ab.cd.|*";
        Map<Integer, Set<Integer>> transitions = postfixToNFA(postfix);
    
        System.out.println("Transitions:");
        for (int fromState : transitions.keySet()) {
            Set<Integer> toStates = transitions.get(fromState);
            for (int toState : toStates) {
                System.out.printf("%d -> %d\n", fromState, toState);
            }
        }
    }
}

