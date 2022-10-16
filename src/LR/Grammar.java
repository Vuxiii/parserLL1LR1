package src.LR;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import src.Utils.Utils;

public class Grammar {
    
    Map< NonTerminal, List<LRRule> > LRRules;

    // Map< List<LRRule>, LRState > state_cache;
    List<LRState> state_cache;

    public Grammar() {
        LRRules = new HashMap<>();
        state_cache = new ArrayList<>();
        // state_cache = new HashMap<>();
    }

    public void cache( LRState state ) {
        // Check for dot
        // Check for lookahead
        // Check for terms
        // CHeck for num of rules
        if ( state != null )
            state_cache.add( state );
        // state_cache.put( state.get_rule(null), state );

    }

    public LRState checkCache( List<LRRule> rules ) {
        // System.out.println( "Size of cache " + state_cache.size() );
        // System.out.println( "Checking for:\t" + rules);
        for ( LRState state : state_cache ) {
            List<LRRule> li = new ArrayList<>();
            // System.out.println( state );
            li.addAll( state.containedRules );

            // System.out.println( "Checking:\t" + li );

            // if ( li.size() != rules.size() ) continue;
            if ( li.containsAll( rules ) ) return state;
            
        }
        // System.out.println( "Didnt find it." );
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
        LRRules.merge( key, Utils.toList( new LRRule( rule, Utils.toList() ) ), (o, n) -> { o.addAll( n ); return o; } );
    }

    public List<LRRule> get_rule( NonTerminal key ) {
        return LRRules.get( key );
    }
}
