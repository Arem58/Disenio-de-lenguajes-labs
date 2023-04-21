package arem.Algoritmos.AFD;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class Tabla {
    private LinkedHashMap<String, EstadosAFD> afdNodeData;
    
    private Map<Integer, Set<Integer>> followposTable;
    
    public Map<String, EstadosAFD> getAfdNodeData() {
        return afdNodeData;
    }

    public Tabla(nodo root) {
        afdNodeData = new LinkedHashMap<>();
        followposTable = new HashMap<>();
        traverseAndFillMap(root, afdNodeData);
        calculateFollowpos(root);
    }

    public void traverseAndFillMap(nodo root, Map<String, EstadosAFD> afdNodeData) {
        if (root == null) {
            return;
        }

        // Recorre el árbol hasta el nodo más a la izquierda
        traverseAndFillMap(root.getLeft(), afdNodeData);
        traverseAndFillMap(root.getRight(), afdNodeData);

        // Si es el nodo más a la izquierda, agrega el nodo AFDNode al HashMap
        if (!afdNodeData.containsKey(root.getIdentifier())) {
            afdNodeData.put(root.getIdentifier(), new EstadosAFD(root));
        }

        // Calcular y establecer nullable, firstpos, lastpos y followpos en EstadosAFD
        EstadosAFD estadosAFD = afdNodeData.get(root.getIdentifier());
        if (estadosAFD != null) {
            estadosAFD.setNullable(calculateNullable(root));
            estadosAFD.setFirstpos(calculateFirstpos(root));
            estadosAFD.setLastpos(calculateLastpos(root));
        }
    }

    // Métodos para calcular nullable, firstpos, lastpos y followpos
    private boolean calculateNullable(nodo node) {
        if (node == null) {
            return false;
        }
        switch (node.getOperatorType()) {
            case "ε":
                return true;
            case "|":
                return calculateNullable(node.getLeft()) || calculateNullable(node.getRight());
            case ".":
                return calculateNullable(node.getLeft()) && calculateNullable(node.getRight());
            case "*":
                return true;
            default:
                return false;
        }
    }

    private Set<Integer> calculateFirstpos(nodo node) {
        if (node == null) {
            return new HashSet<>();
        }

        EstadosAFD estadosAFD = afdNodeData.get(node.getIdentifier());
        if (estadosAFD == null) {
            return new HashSet<>();
        }

        Set<Integer> firstpos = new HashSet<>();

        switch (node.getOperatorType()) {
            case "ε":
                // Conjunto vacío para epsilon
                break;
            case "|":
                // Unión de firstpos de los hijos
                firstpos.addAll(calculateFirstpos(node.getLeft()));
                firstpos.addAll(calculateFirstpos(node.getRight()));
                break;
            case ".":
                // Si el hijo izquierdo es nullable, unir firstpos de ambos hijos
                if (afdNodeData.get(node.getLeft().getIdentifier()).isNullable()) {
                    firstpos.addAll(calculateFirstpos(node.getLeft()));
                    firstpos.addAll(calculateFirstpos(node.getRight()));
                } else {
                    firstpos.addAll(calculateFirstpos(node.getLeft()));
                }
                break;
            case "*":
                // firstpos del hijo
                firstpos.addAll(calculateFirstpos(node.getLeft()));
                break;
            default:
                // Si es un símbolo, agregar la posición del nodo
                firstpos.add(Integer.parseInt(node.getIdentifier()));
                break;
        }

        return firstpos;
    }

    private Set<Integer> calculateLastpos(nodo node) {
        if (node == null) {
            return new HashSet<>();
        }

        EstadosAFD estadosAFD = afdNodeData.get(node.getIdentifier());
        if (estadosAFD == null) {
            return new HashSet<>();
        }

        Set<Integer> lastpos = new HashSet<>();

        switch (node.getOperatorType()) {
            case "ε":
                // Conjunto vacío para epsilon
                break;
            case "|":
                // Unión de lastpos de los hijos
                lastpos.addAll(calculateLastpos(node.getLeft()));
                lastpos.addAll(calculateLastpos(node.getRight()));
                break;
            case ".":
                // Si el hijo derecho es nullable, unir lastpos de ambos hijos
                if (afdNodeData.get(node.getRight().getIdentifier()).isNullable()) {
                    lastpos.addAll(calculateLastpos(node.getLeft()));
                    lastpos.addAll(calculateLastpos(node.getRight()));
                } else {
                    lastpos.addAll(calculateLastpos(node.getRight()));
                }
                break;
            case "*":
                // lastpos del hijo
                lastpos.addAll(calculateLastpos(node.getLeft()));
                break;
            default:
                // Si es un símbolo, agregar la posición del nodo
                lastpos.add(Integer.parseInt(node.getIdentifier()));
                break;
        }

        return lastpos;
    }

    public void calculateFollowpos(nodo node) {
        if (node == null) {
            return;
        }
    
        calculateFollowpos(node.getLeft());
        calculateFollowpos(node.getRight());
    
        String operatorType = node.getOperatorType();
        if (operatorType.equals(".")) { // Concatenation
            nodo leftChild = node.getLeft();
            nodo rightChild = node.getRight();
    
            EstadosAFD leftChildData = afdNodeData.get(leftChild.getIdentifier());
            EstadosAFD rightChildData = afdNodeData.get(rightChild.getIdentifier());
    
            for (Integer position : leftChildData.getLastpos()) {
                Set<Integer> followpos = followposTable.computeIfAbsent(position, k -> new HashSet<>());
                followpos.addAll(rightChildData.getFirstpos());
                afdNodeData.get(String.valueOf(position)).setFollowpos(followpos); // Agrega esta línea
            }
        } else if (operatorType.equals("*")) { // Kleene star
            EstadosAFD nodeData = afdNodeData.get(node.getIdentifier());
    
            for (Integer position : nodeData.getLastpos()) {
                Set<Integer> followpos = followposTable.computeIfAbsent(position, k -> new HashSet<>());
                followpos.addAll(nodeData.getFirstpos());
                afdNodeData.get(String.valueOf(position)).setFollowpos(followpos); // Agrega esta línea
            }
        }
    }    

    public void printTable() {
        System.out.println("ID\tNullable\tFirstpos\t\tLastpos\t\tFollowpos\tSymbol");

        for (Map.Entry<String, EstadosAFD> entry : afdNodeData.entrySet()) {
            String id = entry.getKey();
            EstadosAFD estadosAFD = entry.getValue();

            System.out.println(String.format("%-8s%-12b%-24s%-24s%-24s%s", id, estadosAFD.isNullable(),
                    setToString(estadosAFD.getFirstpos()), setToString(estadosAFD.getLastpos()),
                    setToString(estadosAFD.getFollowpos()), estadosAFD.getSimbol()));
        }
    }

    private String setToString(Set<Integer> set) {
        if (set == null) {
            return "{}";
        }
        return set.toString();
    }
}
