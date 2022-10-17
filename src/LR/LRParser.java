package src.LR;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import src.Utils.Utils;

public class LRParser {
    

    public static LRState parse( Grammar g, NonTerminal start ) {

        LRState start_state = _computeState( g, start );
        
        // _printStates( g, start_state, new HashSet<>() );

        List<ParserState> table = getParserTable( g, start_state );
        
        // ParserState ns = index.eat( Term.get( "x" ) );
        // if ( ns instanceof ParserStateError ) 
        //     System.out.println( ns.errorMsg );
        // else
        //     System.out.println( "\n\n\n\nArrived at\n" + ns.current_state );

        // In state 1

        // Reading x :-> ParserState newState = acc.get( x ).accept( oldState );



        return start_state;

    }

    public static List<ParserState> getParserTable( Grammar g, LRState start ) {
        return _getParserTable( g, new ParserState( start, null ) );
    }

    private static List<ParserState> _getParserTable( Grammar g, ParserState index ) {
        
        List<ParserState> output = new ArrayList<>();        

        List<ParserState> queue = new LinkedList<>();
        Set<Integer> visited = new HashSet<>();

        queue.add( index );

        while ( queue.size() > 0 ) {
            ParserState current = queue.remove( 0 );
            output.add( current );
            LRState state = current.current_state; 
            visited.add( state.id );

            System.out.println( "Visiting\n" + state );

            // Set up the fails. The actual eatable will overwrite these.
            g.terms().forEach(   t -> current.addError( t, "The Term '" + t + "' Connot be accepted from the given state\n" + state ) );
            g.nonTerms().forEach(t -> current.addError( t, "The Term '" + t + "' Connot be accepted from the given state\n" + state ) );



            for ( Term t : state.move_to_state.keySet() ) {
                if ( t == Rule.EOR || t == Rule.EOP ) continue; // This should remove (1)

                LRState to_state = state.move_to_state.get( t );
                
                if ( to_state == null ) continue; // (1)
    
                ParserState move = new ParserState( to_state, t );
    
                System.out.println( "To state id: " + to_state.id );
                
                
                // Here we can add whatever code we want. This will be executed when we move from one state to another.
                current.addMove( t, ( ParserState oldState ) -> { 
                    // We have access to 
                    //  * oldState  = fromState
                    //  * move      = toState 
                    //  * t         = usedTerm
                    
                    // System.out.println( "Made move " + t + " from" );
                    // System.out.println( oldState.current_state );
                    // System.out.println( "And arrived at" );
                    // System.out.println( move.current_state );
                    return move; 
                } );

                if ( !visited.contains( to_state.id ) )
                    queue.add( move );
            }

        }


        return output;
    }

    private static void _printStates( Grammar g, LRState state, Set<LRState> visited ) {
        visited.add( state );
        System.out.println( state.toString() + "\n" );

        for ( Term move : state.move_to_state.keySet() ) {
            LRState ste = state.move_to_state.get( move );
            if ( ste != null && !visited.contains( ste ) ) {
                
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
            r.lookahead.add( Term.QUESTION );
            ste.add( start, r );
        }

        _computeClosure( ste, g );

        // Cache the resulting state, so it can be used for loops.
        g.cache( ste );

        for ( Term move : ste.getMoves() ) {
            if ( move.equals( Rule.EOR ) ) continue;
        
            LRState ns = _computeState( ste, move, g );
            g.cache( ns );
            ste.move_to_state.put( move, ns );
            
        }
        return ste;
    }

    /**
     * This method computes the closure for the given state.
     * @param state The state in which to compute the closure.
     * @param g The grammar for the language.
     */
    private static void _computeClosure( LRState state, Grammar g ) {
        List< NonTerminal > addQueue = new LinkedList<>();
        
        Map<Term, Set<Term> > getLookahead = new HashMap<>();

        g.terms().forEach( t -> getLookahead.put( t, Utils.toSet() ) ); // Fill it with empty lookahead to avoid null.
        g.nonTerms().forEach( t -> getLookahead.put( t, Utils.toSet() ) ); // Fill it with empty lookahead to avoid null.
        addQueue.addAll( state.rules.keySet() );
        
        while ( !addQueue.isEmpty() ) {
            NonTerminal X = addQueue.remove(0);
            List<LRRule> rules = state.get_rule( X );

            for ( int i = 0; i < rules.size(); ++i ) {
                LRRule rule = rules.get( i );
                
                if ( rule.dot >= rule.size() ) continue;
                
                Term t = rule.get_dot_item();
                if ( !(t instanceof NonTerminal) ) continue;
                
                for ( LRRule r : g.get_rule( (NonTerminal) t ) ) {
                    if ( state.containedRules.contains( r ) ) continue;
                    state.add( (NonTerminal) t, r );
                    addQueue.add( (NonTerminal) t );
                }
            }
        }

        for ( NonTerminal X : state.rules.keySet() ) {
            List<LRRule> rules = state.get_rule( X );
            for ( LRRule rule : rules ) {
               
                // Add the lookahead from looking at the current rule
                if ( rule.dot+1 < rule.size() ) {
                    Term lahead = rule.terms.get( rule.dot + 1 );
                    if ( rule.get_dot_item() instanceof NonTerminal )
                        getLookahead.get( rule.get_dot_item() ).add( lahead );
                    
                } else { // Add the lookahead from the Productions ruleset.
                    if ( getLookahead.containsKey( X ) ) {
                        Set<Term> lahead = getLookahead.get( X );
                        if ( rule.get_dot_item() instanceof NonTerminal )
                            getLookahead.get( rule.get_dot_item() ).addAll( lahead );
                        
                    }
                }
               
                // Add the found lookaheads.
                Term t = rule.get_dot_item();
                if ( !(t instanceof NonTerminal) ) continue;
                for ( LRRule r : g.get_rule( (NonTerminal) t ) ) 
                    r.lookahead.addAll( getLookahead.get( t ) );
                 
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
        // (1) Collect all the rules where you can make the move.
        // (2) Generate a new state with these new rules
        // (3) and compute the closure. 
        // (4) * Repeat.
        Map<NonTerminal, List<LRRule> > mapper = new HashMap<>();
        
        // (1) Collect the rules where "move" can be made from the previous state.
        for ( NonTerminal X : from.rules.keySet() ) {
            for ( LRRule rule : from.rules.get( X ) ) {
                if ( rule.get_dot_item().name.equals( move.name ) )
                    mapper.merge( X, Utils.toList( rule ), (o, n) -> { o.addAll( n ); return o; } );
            }
        }

        // (2) Generate the new state, and insert all the rules from the original state 
        //      and progress them by one.
        LRState ste = new LRState();
        
        for ( NonTerminal X : mapper.keySet() ) {
            for ( LRRule r : mapper.get( X ) ) {
                LRRule rule = new LRRule( r.terms, r.lookahead );
                rule.dot = r.dot+1;
                ste.add( X, rule );
            }
        }

        // (3) Compute the closure for this state.
        _computeClosure( ste, g );

        // Check if this state already has been made. If it has, just return it.
        // Prevents infinite recursion on loops.
        LRState ns = g.checkCache( Utils.toList( ste.containedRules ) );
        
        if ( ns != null ) return ns; // We have already seen this state before. Just return it.
        
        // This state has not been seen before, therefore cache it, and compute the new states from this state.
        g.cache( ste );

        // (4) Generate the new states by making each move available from this newly created state.
        for ( Term new_move : ste.getMoves() ) {
            if ( new_move.equals( Rule.EOR ) ) { /*System.out.println( "Found EOR" );*/ continue; } // End of Rule
            if ( new_move instanceof Terminal && ((Terminal) new_move).is_EOP ) { /*System.out.println( "Found EOP" );*/ continue; }// End of Parse $
            
            ste.move_to_state.put( new_move, _computeState( ste, new_move, g  ) );
        }

        return ste;
    }

    /**
     * Provides a sample CFG to be parsed.
     */
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
