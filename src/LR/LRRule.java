package src.LR;

import java.util.ArrayList;
import java.util.List;

public class LRRule extends Rule {
    int dot = 0;
    List<Term> lookahead = new ArrayList<>();

    public LRRule( List<Term> terms, List<Term> lookahead ) {
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

