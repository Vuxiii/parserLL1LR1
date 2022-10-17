package src.LR;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LRRule extends Rule {
    int dot = 0;
    Set<Term> lookahead = new HashSet<>();

    public LRRule( List<Term> terms, Set<Term> lookahead ) {
        super( terms );
        this.lookahead = lookahead;
    }

    

    public Term get_dot_item() {
        return dot < size() ? terms.get( dot ) : Rule.EOR;
    }

    public boolean equals( Object other ) {
        if ( other == null )                            return false;
        if ( !(other instanceof LRRule) )               return false;
        LRRule o = (LRRule) other;
        if ( dot != o.dot )                             return false;
        if ( lookahead.size() != o.lookahead.size() )   return false;
        if ( !lookahead.containsAll( o.lookahead ) )    return false;
        if ( terms.size() != o.terms.size() )           return false;
        if ( !terms.containsAll( o.terms ) )            return false;


        return true;
    }

    public String toString() {
        String s = "";

        s += terms + " :-> " + lookahead;

        return s;
    }
}

