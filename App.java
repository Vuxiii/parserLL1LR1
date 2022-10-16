import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class App {
    public static void main( String[] args ) {
        {
        NonTerminal S = new NonTerminal( "S" );
        NonTerminal E = new NonTerminal( "E" );
        NonTerminal T = new NonTerminal( "T" );

        Terminal dollar = new Terminal( "$" );
        dollar.is_EOP = true;
        Terminal plus = new Terminal( "+" );
        Terminal x = new Terminal( "x" );

        Grammar g = new Grammar();

        g.add_lrRule( S, List.of( E, dollar ) );
        
        g.add_lrRule( E, List.of( T, plus, E ) );
        g.add_lrRule( E, List.of( T ) );

        g.add_lrRule( T, List.of( x ) );

        LRparser.parse( g, S );
        }

        // {
        //     NonTerminal S = new NonTerminal( "S" );
        //     NonTerminal E = new NonTerminal( "E" );
        //     NonTerminal T = new NonTerminal( "T" );
    
        //     Terminal dollar = new Terminal( "$" );
        //     Terminal plus = new Terminal( "+" );
        //     Terminal x = new Terminal( "x" );
    
        //     Grammar g = new Grammar();
    
        //     g.add_lrRule( S, List.of( E, dollar ) );
            
        //     g.add_lrRule( E, List.of( T ) );
    
        //     g.add_lrRule( T, List.of( x ) );
    
        //     LRparser.parse( g, S );
        //     }

    }

    public static void LLsample() {
        // {
        //     Grammar g = new Grammar();
        //     NonTerminal Z = new NonTerminal( "Z" );
        //     NonTerminal Y = new NonTerminal( "Y" );
        //     NonTerminal X = new NonTerminal( "X" );

        //     Terminal d = new Terminal( "d" );
        //     Terminal epsilon = new Terminal();
        //     Terminal c = new Terminal( "c" );
        //     Terminal a = new Terminal( "a" );

        //     g.add_rule( Z, List.of( d ) );
        //     g.add_rule( Z, List.of( X, Y, Z ) );
            
        //     g.add_rule( Y, List.of( epsilon ) );
        //     g.add_rule( Y, List.of( c ) );

        //     g.add_rule( X, List.of( Y ) );
        //     g.add_rule( X, List.of( a ) );

        //     LLparser.parse( g );
        // }

        {
            Grammar g = new Grammar();
            NonTerminal S = new NonTerminal( "S" );
            NonTerminal E = new NonTerminal( "E" );
            NonTerminal E_ = new NonTerminal( "E'" );
            NonTerminal T = new NonTerminal( "T" );
            NonTerminal T_ = new NonTerminal( "T'" );
            NonTerminal F = new NonTerminal( "F" );

            Terminal plus = new Terminal( "+" );
            Terminal minus = new Terminal( "-" );
            Terminal times = new Terminal( "*" );
            Terminal div = new Terminal( "/" );
            Terminal lparen = new Terminal( "(" );
            Terminal rparen = new Terminal( ")" );
            Terminal dollar = new Terminal( "$" );
            Terminal id = new Terminal( "id" );
            Terminal num = new Terminal( "num" );
            Terminal epsilon = new Terminal();

            g.add_rule( S, List.of( E, dollar ) );

            g.add_rule( E, List.of( T, E_ ) );
            
            g.add_rule( E_, List.of( plus, T, E_ ) );
            g.add_rule( E_, List.of( minus, T, E_ ) );
            g.add_rule( E_, List.of( epsilon ) );

            g.add_rule( T, List.of( F, T_ ) );
            
            g.add_rule( T_, List.of( times, F, T_ ) );
            g.add_rule( T_, List.of( div, F, T_ ) );
            g.add_rule( T_, List.of( epsilon ) );

            g.add_rule( F, List.of( id ) );
            g.add_rule( F, List.of( num ) );
            g.add_rule( F, List.of( lparen, E, rparen ) );

            LLparser.parse( g );
        }

        // System.out.println( "-".repeat( 10 ) );
        // {
        //     Grammar g = new Grammar();
        //     NonTerminal SS_ = new NonTerminal( "SS'" );
        //     NonTerminal S = new NonTerminal( "S" );
        //     NonTerminal B = new NonTerminal( "B" );
        //     NonTerminal E = new NonTerminal( "E" );
        //     NonTerminal X = new NonTerminal( "X" );

        //     Terminal bslash = new Terminal( "\\" );
        //     Terminal b = new Terminal( "b" );
        //     Terminal e = new Terminal( "e" );
        //     Terminal lcurl = new Terminal( "{" );
        //     Terminal rcurl = new Terminal( "}" );
        //     Terminal w = new Terminal( "w" );
        //     Terminal epsilon = new Terminal();
        //     Terminal dollar = new Terminal( "$" );


        //     g.add_rule( SS_, List.of(S, dollar) );

        //     g.add_rule( S, List.of(epsilon) );
        //     g.add_rule( S, List.of(X, S) );
            
        //     g.add_rule( B, List.of(bslash, b, lcurl, w, rcurl) );
            
        //     g.add_rule( E, List.of(bslash, e, lcurl, w, rcurl) );
            
        //     g.add_rule( X, List.of(B, S, E) );
        //     g.add_rule( X, List.of(lcurl, S, rcurl) );
        //     g.add_rule( X, List.of(w) );
        //     g.add_rule( X, List.of(b) );
        //     g.add_rule( X, List.of(e) );
        //     g.add_rule( X, List.of(bslash, w) );

        //     LLparser.parse( g );

            
        // }

        // System.out.println( "-".repeat( 10 ) );
        // {
        //     Grammar g = new Grammar();
        //     NonTerminal S = new NonTerminal( "S" );
        //     NonTerminal M = new NonTerminal( "M" );
        //     NonTerminal T = new NonTerminal( "T" );

        //     Terminal a = new Terminal( "a" );
        //     Terminal plus = new Terminal("+");
        //     Terminal dollar = new Terminal( "$" );
        //     Terminal epsilon = new Terminal();



        //     g.add_rule( S, List.of(T, M, dollar) );
            
        //     g.add_rule( M, List.of(plus, T, M) );
        //     g.add_rule( M, List.of(epsilon) );
            
        //     g.add_rule( T, List.of(a) );
            
        //     LLparser.parse( g );

            
        // }
    }
}

class Term {
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

class Terminal extends Term {
    boolean is_epsilon = false;
    boolean is_EOP = false;
    public Terminal() {
        is_epsilon = true;
        name = "";
    }

    public Terminal( String n ) {
        name = n;
    }

    public boolean equals( Object other ) {
        if ( other == null ) return false;
        if ( !(other instanceof Terminal) ) return false;
        Terminal o = (Terminal) other;
        if ( !name.equals(o.name) ) return false;
        if ( is_epsilon != o.is_epsilon ) return false;

        return true;
    }

    public String toString() {
        return (is_epsilon ? "€" : name);
    }
}

class NonTerminal extends Term {
    public NonTerminal( String n ) {
        name = n;
    }
    

    public boolean equals( Object other ) {
        if ( other == null ) return false;
        if ( !(other instanceof NonTerminal) ) return false;
        NonTerminal o = (NonTerminal) other;
        if ( !name.equals(o.name) ) return false;

        return true;
    }
}


class Grammar {

    Map< NonTerminal, List<Rule> > rules;
    Map< NonTerminal, List<LRRule> > lrRules;

    // Map< List<LRRule>, LRState > state_cache;
    List<LRState> state_cache;

    public Grammar() {
        rules = new HashMap<>();
        lrRules = new HashMap<>();
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

    public void add_lrRule( NonTerminal key, List<Term> rule ) {
        lrRules.merge( key, Utils.toList( new LRRule( rule, Utils.toList() ) ), (o, n) -> { o.addAll( n ); return o; } );
    }
    public List<Rule> get_rule( NonTerminal key ) {
        return rules.get( key );
    }
    public List<LRRule> get_lrRule( NonTerminal key ) {
        return lrRules.get( key );
    }
}

class Rule {
    public static Term EOR = new Term( "EndOfRule" );
    public static Term EOP = new Term( "EndOfParse" );
    List<Term> terms = new ArrayList<>();

    public Rule( List<Term> terms ) {
        this.terms.addAll( terms );
    }

    public Term get_term( int i ) { return i < size() ? terms.get( i ) : Rule.EOR; }

    public int size() {
        return terms.size();
    }
}

class LRRule extends Rule {
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
        if ( other == null ) return false;
        if ( !(other instanceof LRRule) ) return false;
        LRRule o = (LRRule) other;
        if ( dot != o.dot ) return false;
        if ( lookahead.size() != o.lookahead.size() ) return false;
        if ( !lookahead.containsAll( o.lookahead ) ) return false;
        if ( terms.size() != o.terms.size() ) return false;
        if ( !terms.containsAll( o.terms ) ) return false;


        return true;
    }

    public String toString() {
        String s = "";

        s += terms + " :-> " + lookahead;

        return s;
    }
}

class LRState {

    
    Map<NonTerminal, List<LRRule> > rules = new HashMap<>();
    Set<LRRule> containedRules = new HashSet<>();
    // List<Integer> dot = new ArrayList<>();

    Map<Term, LRState> move_to_state = new HashMap<>();



    void add( NonTerminal X, LRRule r) {
        containedRules.add( r );
        move_to_state.put( r.get_term(r.dot), null );
        if ( rules.containsKey( X ) )
            rules.get( X ).add( r );
        else
            rules.put( X, Utils.toList( r ) );
    }

    List<LRRule> get_rule( NonTerminal N ) { return rules.get( N ); }

    // int get_dot( int i ) { return dot.get( i ); }

    int size() { return rules.size(); }

    public List<Term> getMoves() {
        return Utils.toList( move_to_state.keySet() );
    }

    public String toString() {
        String s = "";

        for ( NonTerminal X : rules.keySet() ) {
            for ( LRRule r : rules.get( X ) ) {
                s += X + " -> ";
                for ( int i = 0; i < r.size(); ++i ) {
                    Term t = r.terms.get( i );
                    int dotPos = r.dot;
    
                    if ( dotPos == i ) s += ".";
                    s += t;

                } 
                s += "  \t:-> " + r.lookahead + "\n";
            }
            
        }

        return s.substring(0, s.length());
    }
}

class LRparser {
    

    public static void parse( Grammar g, NonTerminal start ) {
        LRState state1 = _computeState( g, start );
        
        _printStates( g, state1, new HashSet<>() );

    }

    private static void _printStates( Grammar g, LRState state, Set<LRState> visited ) {
        visited.add( state );
        System.out.println( state );

        for ( Term move : state.move_to_state.keySet() ) {
            LRState ste = state.move_to_state.get( move );
            // System.out.println( "Move " + move );
            // System.out.println( "State\n" + ste );
            if ( ste != null && !visited.contains( ste ) ) {
                System.out.println( "Making move " + move + " from");
                System.out.println( state );
                System.out.println( "to");
                _printStates( g, ste, visited );
            }
            
        }
            

    } 

    /**
     * Computes the initial start state
     * @param g The grammar
     * @param start The of the CFG
     * @return The start state
     */
    private static LRState _computeState( Grammar g, NonTerminal start ) {
        
        LRState ste = new LRState();

        for ( LRRule r : g.get_lrRule( start ) ) {
            System.out.println( "adding rule " + r + " to start");
            ste.add( start, r );

        }

        _computeClosure( ste, g );

        // Cache the resulting state, so it can be used for loops.

        g.cache( ste );

        for ( Term move : ste.getMoves() ) {
            if ( move.equals( Rule.EOR ) ) continue;
            // System.out.println( "-".repeat( 20 ) );
            LRState ns = _computeState( ste, move, g );
            g.cache( ns );
            ste.move_to_state.put( move, ns );
            // System.out.println( "-".repeat( 20 ) );
            
        }

        return ste;
    }

    private static void _computeClosure( LRState state, Grammar g ) {
        

        List< NonTerminal > addQueue = new LinkedList<>();
        // Set<Term> visited = new HashSet<>();

        addQueue.addAll( state.rules.keySet() );

        while ( !addQueue.isEmpty() ) {
            // System.out.println( "Size of queue is " + addQueue.size() );
            NonTerminal X = addQueue.remove(0);
            // if ( visited.contains(X) ) continue;
            // visited.add( X );
            
            List<LRRule> rules = state.get_rule( X );
            // System.out.println( rules );
            // System.out.println( X + " has " + rules.size() + " rules -> " + rules );
            for ( int i = 0; i < rules.size(); ++i ) {
                LRRule rule = rules.get( i );
                
                int dotPos = rule.dot;

                // System.out.println( "Looking at rule[" + i + "]:\n" + X + " -> " + rule.terms );
                
                if ( dotPos < rule.size() ) {
                    Term t = rule.get_term( dotPos );
                    if ( t instanceof NonTerminal ) {
                        for ( LRRule r : g.get_lrRule( (NonTerminal) t ) ) {
                            if ( !state.containedRules.contains( r ) ) {
                                // System.out.println( "Adding rule: " + r + " to " + t);

                                state.add( (NonTerminal) t, r );
                                addQueue.add( (NonTerminal) t );
                                
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Computes the rest of the states
     * @param from Which state we move from
     * @param move With what Symbol
     * @param g The grammar we are using
     * @return The state arrived at by traversing with the given "move" from the given "state"
     */
    private static LRState _computeState( LRState from, Term move, Grammar g ) {
        
        // System.out.println( "\twith move " + move );
        // System.out.println( from );

        // (1) Collect all the rules where you can make the move.
        // (2) Generate a new state with these new rules
        // (3) and compute the closure. 
        // (4) * Repeat.
        Map<NonTerminal, List<LRRule> > mapper = new HashMap<>();
        
        // (1)
        for ( NonTerminal X : from.rules.keySet() ) {
            for ( LRRule rule : from.rules.get( X ) ) {
                if ( rule.get_dot_item().name.equals( move.name ) )
                    mapper.merge( X, Utils.toList( rule ), (o, n) -> { o.addAll( n ); return o; } );
            }
        }

        // System.out.println( mapper );


        // (2)
        // System.out.println( "(2)" );
        LRState ste = new LRState();

        for ( NonTerminal X : mapper.keySet() ) {
            // System.out.println( "Looking at rule " + X );
            for ( LRRule r : mapper.get( X ) ) {
                LRRule rule = new LRRule( r.terms, r.lookahead );
                rule.dot = r.dot+1;
                ste.add( X, rule );
                
            }
        }
        // System.out.println( "New state:" );
        // System.out.println( ste );

        // (3)
        // System.out.println( "(3)" );
        _computeClosure( ste, g );
        // System.out.println( "After closure:" );
        // System.out.println( ste );
        // for ( LRRule r : ste.containedRules ) 
        //     System.out.println( "ASD " +  r );
        LRState ns = g.checkCache(Utils.toList( ste.containedRules ) );
        if ( ns == null )
            g.cache( ste );
        else {
            // System.out.println( "FOUND A DUPLICATE STATE\n" + ns);
            return ns;
        }
        
        // (4)

        // System.out.print( "(4) " );
        for ( Term new_move : ste.getMoves() ) {
            if ( new_move.equals( Rule.EOR ) ) continue;
            if ( new_move instanceof Terminal && ((Terminal) new_move).is_EOP ) continue;
            // System.out.println( new_move );
            // System.out.println( "-".repeat( 20 ) );

            // ns = g.checkCache( Utils.toList( ste.containedRules ) );
            if ( ns == null ) ns = _computeState( ste, new_move, g );
            // System.out.println( "adding edge " );
            // System.out.println( ste );
            // System.out.println( "With move " + new_move );
            // System.out.println( "TO" );
            // System.out.println( ns );
            ste.move_to_state.put( new_move, ns );
            ns = null;
            // System.out.println( "-".repeat( 20 ) );
            
        }

        return ste;
    }

}

class LLparser {

    public static void parse( Grammar g ) {

        Set<NonTerminal> nulls = LLparser.compute_null( g );

        System.out.println( "Nulls" );
        nulls.forEach( n -> System.out.println( "\t" + n ));
        
        Map<NonTerminal, Set<Terminal> > firsts = compute_first( g, nulls );

        System.out.println( "Firsts" );
        firsts.forEach( (k, v) -> System.out.println( "\t" + k + ": " + v ));

        Map< NonTerminal, Set<Terminal> > follows = compute_follow( g, nulls, firsts );

        System.out.println( "Follows" );
        follows.forEach( (k, v) -> System.out.println( "\t" + k + ": " + v ));

        System.out.println( LLparser.parsingTable(g, nulls, firsts, follows) );
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
                    while ( i < rule.size() && ((rule.terms.get( i ) instanceof Terminal && ((Terminal) rule.terms.get( i )).is_epsilon)
                            || nulls.contains( rule.terms.get( i ) )) ) {
                        // System.out.println( rule.terms.get( i ) + " is nullable.");
                        ++i;
                    }  
                    
                    if ( i == rule.size() ) {
                        nulls.add( N );
                        // System.out.println( "Added NonTerminal to NULL " + N );
                    }


                    // // Check if current is a NonTerminal
                    // if ( i < rule.size() && rule.terms.get( i ) instanceof NonTerminal ) {
                    //     if ( !rule.terms.get(i).name.equals( N.name ) ) {
                    //         nulls.add( (NonTerminal) rule.terms.get( i ) );
                    //         System.out.println( "Added NonTerminal to NULL " + rule.terms.get( i ) );
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
                    // System.out.println( "\nChecking NonTerm " + N + " -> " + r.terms );
                    int i = 0;
                    Term current = r.terms.get(i);
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
                            current = i < r.size() ? r.terms.get( i ) : null;
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

                        if ( r.terms.get( i ) instanceof NonTerminal ) {
                            // System.out.println( "At rule: " + N + " -> " + r.rules );
                            NonTerminal current = (NonTerminal) r.terms.get( i );
                            
                            int offset = 1;
                            Term next = i+offset < r.size() ? r.terms.get( i+offset ) : null;
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
                                            next = i+offset+j < r.size() ? r.terms.get( i+offset+j ) : null;
                                            if ( next == null || next instanceof Terminal ) break;
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
                                    next = i+offset < r.size() ? r.terms.get( i+offset ) : null;
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

    public static String parsingTable( Grammar g, Set<NonTerminal> nulls, Map<NonTerminal, Set<Terminal> > firsts, Map< NonTerminal, Set<Terminal> > follows ) {

        TablePrinter p = new TablePrinter();

        // p.addLast( new String[] {"", "Nullable", "Firsts", "Follow"} );
        
        List<Terminal> terms = new ArrayList<>();
        List<NonTerminal> nonTerms = g.nonTerms();
        terms.addAll( g.terms().parallelStream().filter( term -> { return !term.is_epsilon; } ).collect( Collectors.toList() ) );
        
        // Sort the arrays.
        terms.sort( Comparator.comparing( Term::toString ) );
        nonTerms.sort( Comparator.comparing( Term::toString ) );

        {
            String[] s = new String[terms.size() + 1];
            s[0] = "NT \\ T";
            for ( int i = 0; i < terms.size(); ++i )
                s[i+1] = "'" + terms.get(i).name + "'" ;
            p.addFirst( s );
        }   

        for ( NonTerminal X : nonTerms ) {
            String[] s = new String[terms.size() + 1];
            s[0] = X.name;
            for ( int i = 0; i < terms.size(); ++i ) {
                Terminal T = terms.get( i );
                s[i+1] = "";
                if ( firsts.get( X ).contains( T ) ) {
                    for ( Rule r : g.rules.get( X )) {
                        if ( r.terms.get(0) instanceof Terminal ) {
                            if ( (!((Terminal)r.terms.get(0)).is_epsilon) && r.terms.get(0).name.equals(T.name) )
                                s[i+1] += X.name + "->" + r.terms + " ";
                        } else {
                            // remove the nulls
                            if ( firsts.get((NonTerminal) r.terms.get(0)).contains( T ) )
                                s[i+1] += X.name + "->" + r.terms + " ";
                        }

                    }
                } else {
                    boolean allNull = true;
                    for ( Rule r : g.rules.get( X ) ) {
                        allNull = r.terms.stream().allMatch( t -> { return nulls.contains( t ) || (t instanceof Terminal && ((Terminal) t).is_epsilon ); } );
                        if ( allNull && follows.get(X).contains( T ) ) {
                            s[i+1] += X.name + "->" + r.terms + " ";
                            System.out.println( T + " is in " + X);
                            System.out.println( T + " is in " + follows.get(X) );
                            // break;
                        }
                    }

                }

            }
            p.addLast( s );
        }


        return p.compute();
    }

}
