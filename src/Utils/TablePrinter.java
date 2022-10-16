package src.Utils;
import java.util.LinkedList;
import java.util.List;

public class TablePrinter {

    private List< String[] > table = new LinkedList<>();
    private int cols = -1;
    private String sep = "|";

    public void addLast( String[] row ) {
        table.add( row );
        if ( row.length > cols ) cols = row.length;
    }

    public void addFirst( String[] row ) {
        table.add( 0, row );
        if ( row.length > cols ) cols = row.length;
    }

    public String[] popFirst() {
        return table.remove( 0 );
    }

    public String[] popLast() {
        return table.remove( table.size() - 1 );
    }

    public int cols() { return cols; }

    public String compute() {

        int[] cellLength = new int[ cols ];
        for ( int i = 0; i < cols; ++i ) cellLength[i] = 0;


        for ( String[] ss : table ) {
            for ( int i = 0; i < ss.length; ++i ) {
                String s = ss[i];
                cellLength[i] = Math.max( s.length(), cellLength[i] );
            }
        }

        int totalWidth = -sep.length();
        for ( Integer i : cellLength ) totalWidth += i + sep.length();

        String s = "+" + "-".repeat( totalWidth ) + "+\n";

        for ( int row = 0; row < table.size(); ++row ) {

            s += getRow( row, cellLength ) + "\n";
            if ( row + 1 < table.size() ) {
                s += "|";
                for ( int i = 0; i < cols; ++i )
                    s += "-".repeat( cellLength[i]) + ( i+1 < cols ? "+" : "|" );
                s += "\n";
            }
            
        }

        s += "+" + "-".repeat( totalWidth ) + "+";
        return s;
    }

    private String getRow( int row, int[] cellLength ) {
        String s = "|";

        for ( int i = 0; i < cols; ++i ) {
            String text = table.get(row)[i];
            int padding = cellLength[i] - text.length();

            s += text + " ".repeat( padding ) + ( i+1 < cols ? sep : "" );
        }

        return s + "|";
    }


}
