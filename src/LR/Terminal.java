package src.LR;

public class Terminal extends Term {
    boolean is_epsilon = false;
    public boolean is_EOP = false;
    public Terminal() {
        is_epsilon = true;
        name = "";
    }

    public Terminal( String n ) {
        name = n;
    }

    // public boolean equals( Object other ) {
    //     if ( other == null ) return false;
    //     if ( !(other instanceof Terminal) ) return false;
    //     Terminal o = (Terminal) other;
    //     if ( !name.equals(o.name) ) return false;
    //     if ( is_epsilon != o.is_epsilon ) return false;

    //     return true;
    // }

    public String toString() {
        return (is_epsilon ? "€" : name);
    }
}