package src.LL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import src.Utils.Utils;

public class Grammar {
    Map< NonTerminal, List<Rule> > rules;


    public Grammar() {
        rules = new HashMap<>();
    }


    public List<NonTerminal> nonTerms() {
        List<NonTerminal> li = new ArrayList<>();

        li.addAll( rules.keySet() );

        return li;
    }

    public Set<Terminal> terms() {
        Set<Terminal> li = new HashSet<>();

        for ( NonTerminal N : rules.keySet() ) 
            for ( Rule r : rules.get(N) ) 
                for ( Term t : r.terms )
                    if ( t instanceof Terminal ) 
                        li.add( (Terminal) t );

        return li;
    }

    public void add_rule( NonTerminal key, List<Term> rule ) {
        rules.merge( key, Utils.toList( new Rule( rule ) ), (o, n) -> { o.addAll( n ); return o; } );
    }

    public List<Rule> get_rule( NonTerminal key ) {
        return rules.get( key );
    }
}
