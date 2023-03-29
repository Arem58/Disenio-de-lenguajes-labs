// package arem.Funciones;

// import java.util.ArrayList;
// import java.util.HashMap;
// import java.util.HashSet;
// import java.util.LinkedList;
// import java.util.List;
// import java.util.Map;
// import java.util.Queue;
// import java.util.Set;
// import java.util.Stack;

// import arem.Algoritmos.AFN2.AFN2;
// import arem.Algoritmos.AFN2.Estados2;
// import arem.Algoritmos.AFN2.GE;

// public class OrdenarAFN {
//     private Map<Estados2, Map<Character, Set<Estados2>>> transicion;
//     private Map<Estados2, Map<Character, Set<Estados2>>> transicionOrdenada;
//     private GE ge; 

//     public OrdenarAFN(GE ge){
//         this.ge = ge;
//         this.transicion = ge.getTransitions();
//         transicionOrdenada = new HashMap<>();
//         AFN2.stateCount = 0;
//         Set<Estados2> visitados = new HashSet<>();
//         // Ordenamiento(ge.getTransitions(), ge.getEntrada(), visitados);
//         Ordenamiento2();
//         imprimir();
//     }

//     private void Ordenamiento2(){
//         List<Estados2> estados = new ArrayList<>(ge.getListaEstados());
//         Queue<Estados2> colaEs = new LinkedList<>();
//         Estados2 estado = ge.getEntrada();
//         while (!estados.isEmpty()){
//             estados.remove(estado);
//             Map<Character, Set<Estados2>> nodoTransicion = transicion.get(estado);
//             if (nodoTransicion == null)
//                 return;
//             for (Map.Entry<Character, Set<Estados2>> entry: nodoTransicion.entrySet()){
//                 Character simbolo = entry.getKey();
//                 Estados2 nuevoE = new Estados2();
//                 Set<Estados2> targets = entry.getValue();
//                 for(Estados2 target: targets){
//                     transicionOrdenada.computeIfAbsent(nuevoE, k -> new HashMap<>())
//                                       .computeIfAbsent(simbolo, k -> new HashSet<>())
//                                       .add(new Estados2());
//                     colaEs.add(target);
//                 }
//             }
//             estado = colaEs.poll();
//         }
//     }

//     void imprimir(){
//         // Recorrer el mapa completo de transiciones
//         for (Map.Entry<Estados2, Map<Character, Set<Estados2>>> entry : transicionOrdenada.entrySet()) {
//             Estados2 estado = entry.getKey();
//             int sourceState = estado.getId(); // Estado de origen
//             Map<Character, Set<Estados2>> transitionMap = entry.getValue(); // Mapa interno de transiciones

//             // Recorrer el mapa interno de transiciones
//             for (Map.Entry<Character, Set<Estados2>> transitionEntry : transitionMap.entrySet()) {
//                 char symbol = transitionEntry.getKey(); // Símbolo de entrada
//                 Set<Estados2> destinationStates = transitionEntry.getValue(); // Conjunto de estados de destino

//                 // Recorrer el conjunto de estados de destino
//                 for (Estados2 destState : destinationStates) {
//                     // Imprimir la información completa de la transición
//                     System.out.println("Transición: " + sourceState + " --" + symbol + "--> " + destState.getId());
//                 }
//             }
//         }
//     }

//     private void Ordenamiento(Map<Estados2, Map<Character, Set<Estados2>>> transicion, Estados2 inicial, Set<Estados2> visitados) {
//         visitados.add(inicial);
//         System.out.println("Estado visitado:" + inicial.toString());
//         Map<Character, Set<Estados2>> nodoTransicion = transicion.get(inicial);
//         if (nodoTransicion == null)
//             return;
//         for (Map.Entry<Character, Set<Estados2>> entry: nodoTransicion.entrySet()){
//             Character simbolo = entry.getKey();
//             Set<Estados2> targets = entry.getValue();
//             for(Estados2 target: targets){
//                 if (!visitados.contains(target)){
//                     Ordenamiento(transicion, target, visitados);
//                 }
//             }
//         }
//     }
// }
