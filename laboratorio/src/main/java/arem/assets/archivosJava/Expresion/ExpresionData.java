package arem.assets.archivosJava.Expresion;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ExpresionData implements Serializable {
    private List<Character> unsustitutedExpressions;
    private Map<String, String> expandedActions;
    private Set<String> returnedTokens;
    private Set<String> tokens;

    public ExpresionData(Map<String, String> expandedActions, Set<String> returnedTokens, Set<String> tokens, List<Character> unsustitutedExpressions) {
        this.expandedActions = expandedActions;
        this.returnedTokens = returnedTokens;
        this.tokens = new HashSet<>(tokens);
        this.unsustitutedExpressions = unsustitutedExpressions;
    }

    public Map<String, String> getExpandedActions() {
        return expandedActions;
    }

    public Set<String> getReturnedTokens() {
        return returnedTokens;
    }

    public Set<String> getTokens() {
        return tokens;
    }

    public List<Character> getValidExpressions() {
        return unsustitutedExpressions;
    }
}
