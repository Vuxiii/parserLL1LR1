package src.LR;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import src.Utils.Utils;

public class Grammar {
    
    Map< NonTerminal, List<LRRule> > LRRules;
    // Map<String, Term> terms;
    List<LRState> state_cache;



    public Grammar() {
        LRRules = new HashMap<>();
        // terms = new HashMap<>();
        state_cache = new ArrayList<>();
    }

    public void cache( LRState state ) {
        if ( state != null )
            state_cache.add( state );
    }

    // public int size() {
    //     return terms.keySet().size();
    // }

    public LRState checkCache( List<LRRule> rules ) {
        for ( LRState state : state_cache ) {
            List<LRRule> li = new ArrayList<>();
            li.addAll( state.containedRules );

            if ( li.containsAll( rules ) ) return state;    
        }
        return null;
    }

    public List<NonTerminal> nonTerms() {
        List<NonTerminal> li = new ArrayList<>();

        li.addAll( LRRules.keySet() );

        return li;
    }

    public Set<Terminal> terms() {
        Set<Terminal> li = new HashSet<>();

        for ( NonTerminal N : LRRules.keySet() ) 
            for ( Rule r : LRRules.get(N) ) 
                for ( Term t : r.terms )
                    if ( t instanceof Terminal ) 
                        li.add( (Terminal) t );

        return li;
    }

    public void add_rule( NonTerminal key, List<Term> rule ) {
        LRRules.merge( key, Utils.toList( new LRRule( Utils.toList( rule ), Utils.toSet() ) ), (o, n) -> { o.addAll( n ); return o; } );
        
    }

    public List<LRRule> get_rule( NonTerminal key ) {
        return LRRules.get( key ).stream().map( r -> r.copy() ).collect( Collectors.toList() );
    }

    public String toString() {
        String s = "";

        List<String[]> rules = new ArrayList<>();
        for ( NonTerminal X : LRRules.keySet() ) {
            for ( LRRule rule : LRRules.get( X ) ) {
                s = "";
                for ( Term t : rule.terms )
                    s += t;
                rules.add( new String[] { "" + rule.id, "] " + X + " -> " + s } );
            }
        }
        
        s = "";
        rules.sort( Comparator.comparing( ss -> Integer.parseInt( ss[0] ) ) );
        for ( String[] ss : rules )
            s += ss[0] + ss[1] + "\n";
        return s;
    }
}
