package src.LR;

public class NonTerminal extends Term {
    public NonTerminal( String n ) {
        name = n;
    }
    

    // public boolean equals( Object other ) {
    //     if ( other == null ) return false;
    //     if ( !(other instanceof NonTerminal) ) return false;
    //     NonTerminal o = (NonTerminal) other;
    //     if ( !name.equals(o.name) ) return false;

    //     return true;
    // }
}