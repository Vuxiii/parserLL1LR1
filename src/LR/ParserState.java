package src.LR;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ParserState {
    public LRState current_state;
    // public Term accept;

    public Map< Term, Function<ParserState, ParserState> > accepter;

    public final boolean isError;
    public final String errorMsg;

    public ParserState( String errorMessage ) {
        this.current_state = null;
        // this.accept = null;
        isError = true;
        errorMsg = errorMessage;
        accepter = null;
    }

    // Added to easily add progress in parsing...
    public ParserState( LRState state, Term accept ) {
        current_state = state;
        // this.accept = accept;
        isError = false;
        errorMsg = "";

        accepter = new HashMap<>();
    }

    public void addMove( Term t, Function<ParserState, ParserState> fun ) {
        accepter.put( t, fun );
    }

    public void addError( Term t, String error ) {
        addMove( t, (ParserState oldState) -> new ParserStateError( error ) );
    }


    public ParserState eat( Term t ) {
        return accepter.get( t ).apply( this );
    }


}
