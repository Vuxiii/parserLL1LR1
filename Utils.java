
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Utils {
    public static boolean debug = true;
    public static<T> void log( T s ) {
        if ( debug )
            System.out.println( s );
    }
    public static void log() {
        log( "" );
    }

    public static void logno( String s ) {
        if ( debug )
            System.out.print( s );
    }

    public static<T> List<T> toList( T el ) {
        List<T> list = new ArrayList<T>();
        list.add( el );
        return list;
    }


    public static<T> List<T> toList() {
        List<T> list = new ArrayList<T>();
        return list;
    }

    public static<T> Set<T> toSet( T el ) {
        Set<T> set = new HashSet<T>();
        set.add( el );
        return set;
    }


    public static<T> Set<T> toSet( Collection<T> el ) {
        Set<T> set = new HashSet<T>();
        set.addAll( el );
        return set;
    }



    public static<T> Set<T> toSet() {
        Set<T> set = new HashSet<T>();
        return set;
    }

    public static void lines( int n ) {
        log( "\n".repeat( n ) );
    }
}


