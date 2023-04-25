package arem.Algoritmos.AFN2;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import arem.Algoritmos.GE.GE;
import arem.Algoritmos.enums.TipoGrafo;
import arem.Funciones.Lenguaje;

public class AFN2 {
    private String expresion;
    private Stack<GE<Estados2>> stack;
    private GE<Estados2> ge;

    public static int stateCount;

    public GE<Estados2> getGe() {
        return ge;
    }

    public AFN2(String expresion) {
        stateCount = 0;
        stack = new Stack<>();
        this.expresion = expresion;
        NFA();
    }

    private void NFA() {
        for (char c : expresion.toCharArray()) {
            switch (c) {
                case '.':
                    GE<Estados2> geSalida = stack.pop();
                    GE<Estados2> geEntrada = stack.pop();
                    concat(geEntrada, geSalida);
                    break;

                case '|':
                    GE<Estados2> geAbajo = stack.pop();
                    GE<Estados2> geArriba = stack.pop();
                    or(geArriba, geAbajo);
                    break;

                case '*':
                    GE<Estados2> ge1 = stack.pop();
                    kleene(ge1);
                    break;

                default:
                    if (!Lenguaje.lenguajeInicial.contains(c)) {
                        break;
                    }
                    if (Lenguaje.nuevoSimb.containsKey(c))
                        c = Lenguaje.nuevoSimb.get(c);

                    Estados2 entrada = new Estados2();
                    Estados2 salida = new Estados2();
                    Transiciones2 transicion = new Transiciones2(c, entrada, salida);
                    Set<Estados2> estados = new HashSet<>(Set.of(entrada, salida));
                    GE<Estados2> ge = new GE<>(entrada, salida, transicion.getTransicion(), estados);
                    stack.push(ge);
                    break;
            }
        }
        ge = stack.pop();
    }

    void kleene(GE<Estados2> ge1) {
        // Se obtienen los estados de entrada y salida del mini grafo
        Estados2 entrada = ge1.getEntrada();
        Estados2 salida = ge1.getSalida();

        Map<Estados2, Map<Character, Set<Estados2>>> eTransitions = new HashMap<>();
        Set<Estados2> estados = new HashSet<>();
        // Primero pasamos todas las transiciones ya existentes del minigrafo y los
        // estados
        eTransitions.putAll(ge1.getTransitions());
        estados.addAll(ge1.getListaEstados());

        // Ahora se van a crear las transiciones que son con nuevos estados
        Estados2 nueEntrada = new Estados2();
        Estados2 nueSalida = new Estados2();

        // Se agrega las transiciones del estado salida
        Set<Estados2> transiciones = new HashSet<>();
        transiciones.addAll(Set.of(entrada, nueSalida));
        // Salida --> Nuesalida && Salida --> Entrada
        Transiciones2 transicion = new Transiciones2('ε', salida, transiciones);
        eTransitions.putAll(transicion.getTransicion());

        // Se agregan las transiciones del estado nueEntrada
        transiciones = new HashSet<>();
        transiciones.addAll(Set.of(entrada, nueSalida));
        // nueEntrada --> Nuesalida %% nueEntrada --> Entrada
        transicion = new Transiciones2('ε', nueEntrada, transiciones);
        eTransitions.putAll(transicion.getTransicion());

        // Ahora se crea la nueva operacion la kleene
        GE<Estados2> eGe = new GE<>(nueEntrada, nueSalida, eTransitions, estados);
        eGe.setListaEstados(nueEntrada);
        eGe.setListaEstados(nueSalida);
        stack.push(eGe);
    }

    void or(GE<Estados2> geArriba, GE<Estados2> geAbajo) {
        // Se obtienen los estados de entrada y salida del mini grafo
        Estados2 entrada1 = geArriba.getEntrada();
        Estados2 entrada2 = geAbajo.getEntrada();
        Estados2 salida1 = geArriba.getSalida();
        Estados2 salida2 = geAbajo.getSalida();

        // Iniciamos la estructura de datos y le ingresamos las transiciones de geArriba
        // y geAbajo
        Map<Estados2, Map<Character, Set<Estados2>>> orTransitions = new HashMap<>();
        Set<Estados2> estados = new HashSet<>();
        orTransitions.putAll(geArriba.getTransitions());
        orTransitions.putAll(geAbajo.getTransitions());

        estados.addAll(geArriba.getListaEstados());
        estados.addAll(geAbajo.getListaEstados());

        // Ahora se van a crear las transiciones que son con nuevos estados
        Estados2 nueEntrada = new Estados2();
        Estados2 nueSalida = new Estados2();

        // Se agregan las transiciones del estado nueEntrada
        Set<Estados2> transiciones = new HashSet<>();
        transiciones.addAll(Set.of(entrada1, entrada2));
        // nueEntrada --> entrada1 %% nueEntrada --> entrada2
        Transiciones2 transicion = new Transiciones2('ε', nueEntrada, transiciones);
        orTransitions.putAll(transicion.getTransicion());

        // Se agrega la entrada de salida1
        transicion = new Transiciones2('ε', salida1, nueSalida);
        orTransitions.putAll(transicion.getTransicion());

        // Se agrega la entrada de salida2
        transicion = new Transiciones2('ε', salida2, nueSalida);
        orTransitions.putAll(transicion.getTransicion());

        // Ahora se crea la nueva operacion la or
        GE<Estados2> orGe = new GE<>(nueEntrada, nueSalida, orTransitions, estados);
        orGe.setListaEstados(nueEntrada);
        orGe.setListaEstados(nueSalida);
        stack.push(orGe);
    }

    void concat(GE<Estados2> geEntrada, GE<Estados2> geSalida) {
        // Se obtienen los estados de entrada y salida del mini grafo
        Estados2 entrada1 = geEntrada.getEntrada();
        Estados2 salida1 = geEntrada.getSalida();
        Estados2 entrada2 = geSalida.getEntrada();
        Estados2 salida2 = geSalida.getSalida();

        Map<Estados2, Map<Character, Set<Estados2>>> conTransitions = new HashMap<>();
        Set<Estados2> estados = new HashSet<>();
        // Primero pasamos todas las transiciones ya existentes del minigrafo y los
        // estados
        conTransitions.putAll(geEntrada.getTransitions());
        conTransitions.putAll(geSalida.getTransitions());

        estados.addAll(geEntrada.getListaEstados());
        estados.addAll(geSalida.getListaEstados());

        // Vamos a unificar salida1 con entrada2
        Map<Character, Set<Estados2>> uniTrans = conTransitions.get(entrada2);
        // Se crea una nueva transicion con el estado de entrada y eliminamos el estado
        // de salida
        conTransitions.put(salida1, uniTrans);
        conTransitions.remove(entrada2);
        estados.remove(entrada2);

        // Ahora se crea la nueva operacion la concat
        GE<Estados2> concatGe = new GE<>(entrada1, salida2, conTransitions, estados);
        stack.push(concatGe);
    }
}
