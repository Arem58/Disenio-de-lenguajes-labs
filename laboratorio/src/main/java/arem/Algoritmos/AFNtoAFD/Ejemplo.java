private void setTabla() {
    Map<Character, Set<Estados2>> TransicionesNodo = tablaT.get(inicial);
    Set<Estados2> Conjunto = TransicionesNodo.get('ε');
    Deque<Set<Estados2>> pila = new ArrayDeque<>();
    EstadosAFN nodoInicial = new EstadosAFN();
    boolean nuevosConjuntos = true;
    Map<EstadosAFN, Set<Estados2>> subConjuntos = new HashMap<>();

    while (nuevosConjuntos) {
        Map<EstadosAFN, Set<Estados2>> conjuntos = new HashMap<>();
        Set<Estados2> NuevoConjunto = new HashSet<>();
        for (Character caracter : Lenguaje.lenguajeInicial) {
            for (Estados2 target : Conjunto) {
                Map<Character, Set<Estados2>> conjunto = tablaT.get(target);
                if (conjunto.containsKey(caracter)) {
                    Set<Estados2> nodo = conjunto.get(caracter);
                    Estados2 nodoActual = nodo.iterator().next();
                    NuevoConjunto.addAll(tablaT.get(nodoActual).get('ε'));
                }
            }
            pila.push(NuevoConjunto);
            EstadosAFN newEstado = new EstadosAFN();
            if (!subConjuntos.isEmpty()) {
                for (Map.Entry<EstadosAFN, Set<Estados2>> entry : subConjuntos.entrySet()) {
                    Set<Estados2> viejosConjuntos = entry.getValue();
                    if (!NuevoConjunto.equals(viejosConjuntos)) {
                        subConjuntos.put(newEstado, NuevoConjunto);
                        nuevosConjuntos = true;
                    } else {
                        nuevosConjuntos = false;
                    }
                }
            } else {
                subConjuntos.put(newEstado, NuevoConjunto);
            }
            conjuntos.put(newEstado, NuevoConjunto);
            TransicionesAFD transicionesAFD = new TransicionesAFD(caracter, conjuntos);
            tablaS.computeIfAbsent(nodoInicial, k -> new HashMap<>()).putAll(transicionesAFD.getTransicion());
        }
        Conjunto = pila.pop();
    }
}