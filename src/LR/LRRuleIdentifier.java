package src.LR;

public class LRRuleIdentifier {
    
    final int id;
    final int dot;

    public LRRuleIdentifier( int i, int d ) {
        id = i;
        dot = d;
    }

    public String toString() {
        return "id: " + id + "\ndot: " + dot + "\n";
    }

    public int hashCode() {
        
        return id*37 + dot*37*37;
    }

    public boolean equals( Object other ) {
        if ( other == null ) return false;
        if ( !(other instanceof LRRuleIdentifier) ) return false;
        LRRuleIdentifier o = (LRRuleIdentifier) other;
        return id == o.id && dot == o.dot;
    }
}
