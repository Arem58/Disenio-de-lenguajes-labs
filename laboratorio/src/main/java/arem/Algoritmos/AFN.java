// package arem.Algoritmos;

// import java.util.ArrayList;
// import java.util.LinkedList;
// import java.util.List;
// import java.util.Stack;

// import arem.Funciones.Lenguaje;

// public class AFN {
//     public static int stateCount;
//     private String expresion;
//     private List<Transiciones> transiciones;
//     private List<String> EstadosT;
    
//     private Stack<Estados> stack;
//     private Estados estadoFinal;

//     public AFN(String expresion) {
//         this.expresion = expresion;
//         transiciones = new LinkedList<Transiciones>();
//         EstadosT = new ArrayList<>();
//         stack = new Stack<Estados>();
//         NFA();
//     }

//     public int createState() {
//         int state = stateCount;
//         stateCount ++;
//         return state;
//     }

//     public List<Transiciones> getTransitions() {
//         return this.transiciones;
//     }

//     public Estados getEstadoFinal(){
//         return stack.pop();
//     }

//     public Estados getEstadoInicial(){
//         return stack.pop();
//     }
    
//     public List<String> getEstadosT() {
//         return EstadosT;
//     }

//     private void NFA(){
//         for (int i = 0; i < expresion.length(); i++){
//             char c = expresion.charAt(i);
//             if (Lenguaje.lenguajeInicial.contains(c)){
//                 if (Lenguaje.nuevoSimb.containsKey(c))
//                     c = Lenguaje.nuevoSimb.get(c);

//                 TransicionParaUnCaracter(c);
//             }else if(c == '|'){
//                 Estados inferior1 = stack.pop();
//                 Estados inferior2 = stack.pop();
//                 Estados superior1 = stack.pop();
//                 Estados superior2 = stack.pop();

//                 ForOr(inferior1, inferior2, superior1, superior2);
//             }else if(c == '.'){
//                 estadoFinal = stack.pop();

//                 Estados izquierda = stack.pop();
//                 Estados derecha = stack.pop();

//                 conector(izquierda, derecha);
//             }else if(c == '*'){
//                 Estados izquierda = stack.pop();
//                 Estados derecha = stack.pop();

//                 kleen(izquierda, derecha);
//             }
//         }
//     }

//     private void TransicionParaUnCaracter(char c){
//         int primero = createState();
//         Estados nodo = new Estados(primero);
//         int segundo = createState();
//         Estados nodo2 = new Estados(segundo);
        
//         Transiciones trans = new Transiciones(c, nodo, nodo2);
//         transiciones.add(trans);

//         stack.push(nodo);
//         stack.push(nodo2);

//         //Se agregan los estados generados
//         EstadosT.add("" + primero);
//         EstadosT.add("" + segundo);
//     }

//     private void ForOr(Estados inferior1, Estados inferior2, Estados superior1, Estados superior2){
//         //Se crean los estados que hacen que se unan los cuatro que se concatenan
//         int primero = createState();
//         Estados nuevo1 = new Estados(primero);
//         int segundo = createState();
//         Estados nuevo2 = new Estados(segundo);

//         //Les creamos una transicion al inicial y al final
//         Transiciones trans1 = new Transiciones('ε', nuevo1, inferior1);
//         Transiciones trans2 = new Transiciones('ε', nuevo1, superior1);
//         Transiciones trans3 = new Transiciones('ε', inferior2, nuevo2);
//         Transiciones trans4 = new Transiciones('ε', superior2, nuevo2);

//         transiciones.add(trans1);
//         transiciones.add(trans2);
//         transiciones.add(trans3);
//         transiciones.add(trans4);

//         stack.push(nuevo1);
//         stack.push(nuevo2);

//         //Se agregan los estados generados
//         EstadosT.add("" + primero);
//         EstadosT.add("" + segundo);
//     }

//     private void conector(Estados izquierdo, Estados derecho){
//         Transiciones trans = new Transiciones('ε', izquierdo, derecho);

//         transiciones.add(trans);
//         stack.push(estadoFinal);
//     }

//     private void kleen(Estados izquierdo, Estados derecho){
//         //Primero creamos los nodos que se cren en el kleen a los lados 
//         int primero = createState();
//         Estados nuevo1 = new Estados(primero);
//         int segundo = createState();
//         Estados nuevo2 = new Estados(segundo);

//         //Ahora hacemos las conexiones con los nuevos nodos
//         Transiciones trans1 = new Transiciones('ε', nuevo1, izquierdo);
//         Transiciones trans2 = new Transiciones('ε', derecho, izquierdo);
//         Transiciones trans3 = new Transiciones('ε', derecho, nuevo2);
//         Transiciones trans4 = new Transiciones('ε', nuevo1, nuevo2);

//         transiciones.add(trans1);
//         transiciones.add(trans2);
//         transiciones.add(trans3);
//         transiciones.add(trans4);

//         stack.add(nuevo1);
//         stack.add(nuevo2);

//         //Se agregan los estados generados
//         EstadosT.add("" + primero);
//         EstadosT.add("" + segundo);
//     }
// }
