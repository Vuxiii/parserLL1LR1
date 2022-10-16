import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class App {
    public static void main( String[] args ) {
        {
            Grammar g = new Grammar();
            NonTerminal Z = new NonTerminal( "Z" );
            NonTerminal Y = new NonTerminal( "Y" );
            NonTerminal X = new NonTerminal( "X" );

            Terminal d = new Terminal( "d" );
            Terminal epsilon = new Terminal();
            Terminal c = new Terminal( "c" );
            Terminal a = new Terminal( "a" );

            g.add_rule( Z, List.of( d ) );
            g.add_rule( Z, List.of( X, Y, Z ) );
            
            g.add_rule( Y, List.of( epsilon ) );
            g.add_rule( Y, List.of( c ) );

            g.add_rule( X, List.of( Y ) );
            g.add_rule( X, List.of( a ) );

            LLparser.parse( g );
        }

        System.out.println( "-".repeat( 10 ) );
        {
            Grammar g = new Grammar();
            NonTerminal SS_ = new NonTerminal( "SS'" );
            NonTerminal S = new NonTerminal( "S" );
            NonTerminal B = new NonTerminal( "B" );
            NonTerminal E = new NonTerminal( "E" );
            NonTerminal X = new NonTerminal( "X" );

            Terminal bslash = new Terminal( "\\" );
            Terminal b = new Terminal( "b" );
            Terminal e = new Terminal( "e" );
            Terminal lcurl = new Terminal( "{" );
            Terminal rcurl = new Terminal( "}" );
            Terminal w = new Terminal( "w" );
            Terminal epsilon = new Terminal();
            Terminal dollar = new Terminal( "$" );


            g.add_rule( SS_, List.of(S, dollar) );

            g.add_rule( S, List.of(epsilon) );
            g.add_rule( S, List.of(X, S) );
            
            g.add_rule( B, List.of(bslash, b, lcurl, w, rcurl) );
            
            g.add_rule( E, List.of(bslash, e, lcurl, w, rcurl) );
            
            g.add_rule( X, List.of(B, S, E) );
            g.add_rule( X, List.of(lcurl, S, rcurl) );
            g.add_rule( X, List.of(w) );
            g.add_rule( X, List.of(b) );
            g.add_rule( X, List.of(e) );
            g.add_rule( X, List.of(bslash, w) );

            LLparser.parse( g );
        }

        System.out.println( LLparser.parsingTable(null, null, null) );
    }
}

class Term {
    String name = null;

    public String toString() {
        return "Term: " + name;
    }
}

class Terminal extends Term {
    boolean is_epsilon = false;

    public Terminal() {
        is_epsilon = true;
    }

    public Terminal( String n ) {
        name = n;
    }

    public String toString() {
        return "Term: " + (is_epsilon ? "epsilon" : name);
    }
}

class NonTerminal extends Term {
    public NonTerminal( String n ) {
        name = n;
    }

    // public int hashCode() {
    //     return name.hashCode();
    // }

    // public boolean equals( Object other ) {
    //     if ( other == null ) return false;
    //     if ( !(other instanceof NonTerminal) ) return false;
    //     NonTerminal o = (NonTerminal) other;
    //     return name.equals( o.name );
    // }
}


class Grammar {

    Map< NonTerminal, List<Rule> > rules;

    public Grammar() {
        rules = new HashMap<>();
    }

    public void add_rule( NonTerminal key, List<Term> rule ) {
        rules.merge( key, Utils.toList( new Rule( rule ) ), (o, n) -> { o.addAll( n ); return o; } );
    }

    public List<Rule> get_rule( NonTerminal key ) {
        return rules.get( key );
    }
}

class Rule {
    List<Term> rules;

    public Rule( List<Term> terms ) {
        rules = new ArrayList<>();
        rules.addAll( terms );
    }

    public int size() {
        return rules.size();
    }
}


class LLparser {

    public static void parse( Grammar g ) {

        Set<NonTerminal> nulls = LLparser.compute_null( g );

        System.out.println( "Nulls" );
        nulls.forEach( n -> System.out.println( n ));
        
        Map<NonTerminal, Set<Terminal> > firsts = compute_first( g, nulls );

        System.out.println( "Firsts" );
        firsts.forEach( (k, v) -> System.out.println( k + ": " + v ));

        Map< NonTerminal, Set<Terminal> > follows = compute_follow( g, nulls, firsts );

        System.out.println( "Follows" );
        follows.forEach( (k, v) -> System.out.println( k + ": " + v ));
    }

    public static Set<NonTerminal> compute_null( Grammar g ) {

        Set<NonTerminal> nulls = new HashSet<>();
        int size;
        do {
            size = nulls.size();

            for ( NonTerminal N : g.rules.keySet() ) {
                // if ( nulls.size() == size ) break; // Nothing changed 
                
                List<Rule> rules_for_N = g.get_rule( N );
                for ( Rule rule : rules_for_N ) {
                    // System.out.println( "\nChecking NonTerm " + N + " -> " + rule.rules );
                    int i = 0;

                    // Removes all the first NULLS.
                    while ( i < rule.size() && ((rule.rules.get( i ) instanceof Terminal && ((Terminal) rule.rules.get( i )).is_epsilon)
                            || nulls.contains( rule.rules.get( i ) )) ) {
                        // System.out.println( rule.rules.get( i ) + " is nullable.");
                        ++i;
                    }  
                    
                    if ( i == rule.size() ) {
                        nulls.add( N );
                        // System.out.println( "Added NonTerminal to NULL " + N );
                    }


                    // // Check if current is a NonTerminal
                    // if ( i < rule.size() && rule.rules.get( i ) instanceof NonTerminal ) {
                    //     if ( !rule.rules.get(i).name.equals( N.name ) ) {
                    //         nulls.add( (NonTerminal) rule.rules.get( i ) );
                    //         System.out.println( "Added NonTerminal to NULL " + rule.rules.get( i ) );
                    //     }
                    // }
    
                }
            }

        } while ( size != nulls.size() );
        

        return nulls;
    }


    public static Map< NonTerminal, Set<Terminal> > compute_first( Grammar g, Set<NonTerminal> nulls ) {

        Map< NonTerminal, Set<Terminal> > firsts = new HashMap<>();

        for ( NonTerminal N : g.rules.keySet() ) 
            firsts.put( N, new HashSet<>() );

        int size;
        do {
            size = firsts.values().parallelStream().map( li -> li.size() ).reduce(0, Integer::sum);
            

            // System.out.println( "-".repeat(20) );
            
            // firsts.forEach( (k, v) -> System.out.println( "First(" + k + ") = {" + v + "}" ) );

            // System.out.println( "-".repeat(20) );
            
            for ( NonTerminal N : g.rules.keySet() ) {
                for ( Rule r : g.rules.get( N ) ) {
                    // System.out.println( "\nChecking NonTerm " + N + " -> " + r.rules );
                    int i = 0;
                    Term current = r.rules.get(i);
                    if ( current instanceof Terminal ) {
                        // Ignore epsilons.
                        if ( ((Terminal) current).is_epsilon ) continue;

                        // System.out.println( "First(" + N + ") += {" + current + "}\t\tBy rule 1 (is terminal)");
                        firsts.get( N ).add( (Terminal) current );
                        // firsts.merge( N, Utils.toSet( (Terminal) current ), (n, o) -> { 
                        //     System.out.println( "{" + o + "} -> {" + n + "}" );
                            
                        //     o.addAll( n ); return o; } );
                    } else {
                        boolean addNext = true; //current instanceof NonTerminal && 
                        while( addNext ) { // We know it is a NonTerminal from above.
                            
                            if ( current instanceof NonTerminal ){
                                // System.out.println( "First(" + N + ") += First(" + current + ") = " + firsts.getOrDefault( (NonTerminal) current, Utils.toSet()) + "\tBy rule 2 (Prev were nullable. Current = " + current+ ")");
                                
                                firsts.get( N ).addAll( firsts.get( (NonTerminal) current ) );

                                // firsts.merge( N, firsts.getOrDefault( (NonTerminal) current, Utils.toSet()), (n, o) -> { 
                                    
                                //     System.out.println( "{" + o + "} -> {" + n + "}" );
                                //     o.addAll( n ); return o; } );
                                

                                addNext = nulls.contains( (NonTerminal) current );
                            } else {
                                // System.out.println( "First(" + N + ") += {" + current + "}\t\tBy rule 1 (is terminal)");
                                firsts.get( N ).add( (Terminal) current );
                                // firsts.merge( N, Utils.toSet( (Terminal) current ), (n, o) -> { 
                                //     System.out.println( "{" + o + "} -> {" + n + "}" );
                                //     o.addAll( n ); return o; } );

                                addNext = false;
                            }
                            
                            
                            i++;
                            current = i < r.size() ? r.rules.get( i ) : null;
                            addNext = addNext && current != null;
                        }   
                    }
                }
            }
        } while( size != firsts.values().parallelStream().map( li -> li.size() ).reduce(0, Integer::sum) );


        return firsts;

    }

    public static Map< NonTerminal, Set<Terminal> > compute_follow( Grammar g, Set<NonTerminal> nulls, Map<NonTerminal, Set<Terminal> > firsts) {

        Map< NonTerminal, Set<Terminal> > follow = new HashMap<>();

        for ( NonTerminal N : g.rules.keySet() ) 
            follow.put( N, new HashSet<>() );

        int size;
        do {
            size = follow.values().parallelStream().map( li -> li.size() ).reduce(0, Integer::sum);

            for ( NonTerminal N : g.rules.keySet() ) {
                for ( Rule r : g.rules.get( N ) ) {
                    for ( int i = 0; i < r.size(); ++i ) {

                        if ( r.rules.get( i ) instanceof NonTerminal ) {
                            // System.out.println( "At rule: " + N + " -> " + r.rules );
                            NonTerminal current = (NonTerminal) r.rules.get( i );
                            
                            int offset = 1;
                            Term next = i+offset < r.size() ? r.rules.get( i+offset ) : null;
                            if ( i == r.size() - 1 ){ 
                                // Because nothing comes after this current NonTerminal, 
                                // we add First( left hand side ) to Follow( current )
                                // System.out.println( "Follow(" + current + ") += Follow(" + N + ") = " + follow.get( N ) + "\tRule 1" );
                                follow.get( current ).addAll( follow.get( N ) );

                            }else {
                                do {
                                    // Add first of current+1
                                    if ( next instanceof NonTerminal ){
                                        // System.out.println( "Follow(" + current + ") += First(" + next + ") = " + firsts.get( (NonTerminal) next ) + "\tRule 2" );
                                        follow.get( current ).addAll( firsts.get( (NonTerminal) next ) );
                                        
                                        int j = 1;
                                        while ( nulls.contains( (NonTerminal) next ) ) {
                                            next = i+offset+j < r.size() ? r.rules.get( i+offset+j ) : null;
                                            if ( next == null ) break;
                                            // System.out.println( "Follow(" + current + ") += First(" + next + ") = " + firsts.get( (NonTerminal) next ) + "\tRule 2.5" );
                                            follow.get( current ).addAll( firsts.get( (NonTerminal) next ) );
                                            
                                            j++;                                            
                                        }
                                        if ( next == null ) {
                                            // System.out.println( "Follow(" + current + ") += Follow(" + N + ") = " + follow.get( N ) + "\tRule 2.75" );
                                            follow.get( current ).addAll( follow.get( N ) );
                                        }
                                        // follow.get( current ).addAll( follow.get( (NonTerminal) next ) );
                                    }else{
                                        // System.out.println( "Follow(" + current + ") += {" + next + "}" + "\tRule 3" );
                                        follow.get( current ).add( (Terminal) next );
                                    }
                                    offset++;
                                    next = i+offset < r.size() ? r.rules.get( i+offset ) : null;
                                    if ( next instanceof Terminal || (next instanceof NonTerminal && !nulls.contains( (NonTerminal) next) ) )
                                        next = null;
                                } while ( next != null );
                            }
                            // System.out.println();
                        }
                    }
                }
            }

        } while( size != follow.values().parallelStream().map( li -> li.size() ).reduce(0, Integer::sum) );
        return follow;
    }

    public static String parsingTable( Set<NonTerminal> nulls, Map<NonTerminal, Set<Terminal> > firsts, Map< NonTerminal, Set<Terminal> > follows ) {

        TablePrinter p = new TablePrinter();

        p.addLast( new String[] {"Nullable", "Firsts", "Follow"} );
        p.addLast( new String[] {"x->y", "Firsts", "ajkshd d"} );
        p.addLast( new String[] {"asdasdasdasdasd", " 3123 2", "Follow"} );
        p.addLast( new String[] {"kajshd iausdh ", "2", "alkfja oij9j3qlk a.sd"} );

        return p.compute();
    }

}

class LRparser {

}