package src.LR;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import src.Utils.TablePrinter;
import src.Utils.Utils;

public class ParseTable {
    List<ParserState> states;
    Grammar g; 

    public ParseTable( Grammar g ) {
        states = new ArrayList<>();
        this.g = g;
    }

    public void add( ParserState state ) {
        states.add( state );
    }


    public String toString() {

        List<Terminal> terms = Utils.toList( g.terms() );
        
        terms = terms.stream().filter( t -> !t.is_epsilon ).collect( Collectors.toList() );

        List<NonTerminal> nterms =  g.nonTerms();

        nterms = nterms.stream().filter( nt -> !nt.is_start ).collect( Collectors.toList() );

        Map<Term, Integer> term_to_index = new HashMap<>();


        TablePrinter tp = new TablePrinter();

        tp.addTitle( "Parse-table" );


        int size = terms.size() + nterms.size() + 1;
        String[] header = new String[ size ];
        header[0] = "State";
        
        for ( int i = 0; i < terms.size(); ++i ) {
            if ( terms.get(i) == Rule.EOP || terms.get(i) == Rule.EOR ) continue;
            int offset = i+1;
            Term t = terms.get( i );
            // System.out.println( t );
            header[offset] = t.name;
            term_to_index.put(t, offset);
        }
        for ( int i = 0; i < nterms.size(); ++i ) {
            if ( nterms.get(i) == Rule.EOP || nterms.get(i) == Rule.EOR ) continue;
            int offset = i+terms.size()+1;
            Term t = nterms.get( i );

            header[offset] = t.name;
            term_to_index.put(t, offset);
        }
        tp.push( header );
        

        // Maybe sort states by id before.
        List<ParserState> states_ = Utils.toList( states );
        states_.sort( Comparator.comparing( state -> state.current_state.id ));

        for ( ParserState state : states_ ) {
            String[] row = new String[size];
            for ( int i = 0; i < row.length; ++i ) row[i] = "";
            row[0] = "" + state.current_state.id;
            
            Map<Term, LRState> mapper = state.current_state.move_to_state;
            
            // Find shift and goto actions.
            for ( Term term : mapper.keySet() ) {
                // System.out.println( "At " + term );
                if ( term == Rule.EOR || term == Rule.EOP ) { }
                
                if ( mapper.get( term ) == null ) continue;

                
                String mode = "";
                
                if ( term instanceof NonTerminal )
                    mode = "g";
                else
                    mode = "s";
                row[ term_to_index.get(term) ] += mode + mapper.get( term ).id;
            }

            // Find reduce actions (9)
            for ( LRRule rule : state.current_state.containedRules ) {
                if ( rule.dot == rule.size() || rule.get_dot_item() instanceof Terminal && ((Terminal)rule.get_dot_item()).is_epsilon ) {
                    
                    for ( Term term : rule.lookahead ) {
                        row[ term_to_index.get( term ) ] += "r" + rule.id;
                    }

                } else if ( rule.get_dot_item().name.equals( "$" ) ) {
                    row[ term_to_index.get( rule.get_dot_item() ) ] += "a";
                } 
            }


            tp.push( row );
        }

        return tp.compute();
    }

}
