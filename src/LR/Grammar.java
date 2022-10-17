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

    List<LRState> state_cache;

    public Grammar() {
        LRRules = new HashMap<>();
        state_cache = new ArrayList<>();
    }

    public void cache( LRState state ) {
        if ( state != null )
            state_cache.add( state );
    }

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
        LRRules.merge( key, Utils.toList( new LRRule( rule, Utils.toSet() ) ), (o, n) -> { o.addAll( n ); return o; } );
    }

    public List<LRRule> get_rule( NonTerminal key ) {
        return LRRules.get( key );
    }
}
