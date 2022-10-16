package src.LR;

public class Term {
    String name = null;

    public Term() {}
    public Term( String name ) { this.name = name; }

    public boolean equals( Object other ) {
        if ( other == null ) return false;
        if ( !(other instanceof Term) ) return false;
        Term o = (Term) other;
        if ( !name.equals(o.name) ) return false;

        return true;
    }

    public String toString() {
        return name;
    }
}
