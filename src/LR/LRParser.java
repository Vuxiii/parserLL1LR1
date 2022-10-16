package src.LR;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import src.Utils.Utils;

public class LRParser {
    

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

        for ( LRRule r : g.get_rule( start ) ) {
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
                        for ( LRRule r : g.get_rule( (NonTerminal) t ) ) {
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

    public static void LRSample() {
        {
            NonTerminal S = new NonTerminal( "S" );
            NonTerminal E = new NonTerminal( "E" );
            NonTerminal T = new NonTerminal( "T" );
    
            Terminal dollar = new Terminal( "$" );
            dollar.is_EOP = true;
            Terminal plus = new Terminal( "+" );
            Terminal x = new Terminal( "x" );
    
            Grammar g = new Grammar();
    
            g.add_rule( S, List.of( E, dollar ) );
            
            g.add_rule( E, List.of( T, plus, E ) );
            g.add_rule( E, List.of( T ) );
    
            g.add_rule( T, List.of( x ) );
    
            LRParser.parse( g, S );
        }
    }
        
}
