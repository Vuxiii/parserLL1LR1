package src.LR;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import src.Utils.Utils;

public class LRState {

    
    Map<NonTerminal, List<LRRule> > rules = new HashMap<>();
    Set<LRRule> containedRules = new HashSet<>();
    // List<Integer> dot = new ArrayList<>();

    Map<Term, LRState> move_to_state = new HashMap<>();



    void add( NonTerminal X, LRRule r) {
        containedRules.add( r );
        move_to_state.put( r.get_term(r.dot), null );
        if ( rules.containsKey( X ) )
            rules.get( X ).add( r );
        else
            rules.put( X, Utils.toList( r ) );
    }

    List<LRRule> get_rule( NonTerminal N ) { return rules.get( N ); }

    // int get_dot( int i ) { return dot.get( i ); }

    int size() { return rules.size(); }

    public List<Term> getMoves() {
        return Utils.toList( move_to_state.keySet() );
    }

    public String toString() {
        String s = "";

        for ( NonTerminal X : rules.keySet() ) {
            for ( LRRule r : rules.get( X ) ) {
                s += X + " -> ";
                for ( int i = 0; i < r.size(); ++i ) {
                    Term t = r.terms.get( i );
                    int dotPos = r.dot;
    
                    if ( dotPos == i ) s += ".";
                    s += t;

                } 
                s += "  \t:-> " + r.lookahead + "\n";
            }
            
        }

        return s.substring(0, s.length());
    }
}
