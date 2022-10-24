package src.Utils;
import java.util.LinkedList;
import java.util.List;

public class TablePrinter {

    private List< String[] > table = new LinkedList<>();
    private int cols = 0;
    private String sep = "|";
    private String title = "";

    public void addTitle( String title ) {
        this.title = title;
    }

    public void push( String[] row ) {
        addLast( row );
    } 

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
                if ( s == null ) s = "";
                cellLength[i] = Math.max( s.length(), cellLength[i] );
            }
        }

        int totalWidth = -sep.length();
        for ( Integer i : cellLength ) totalWidth += i + sep.length();

        String s = "";
        boolean padExtra = false;
        int extraPad = 0;
        // Add the title.
        if ( title.length() != 0 ) {
            if ( title.length() > totalWidth ) {
                padExtra = true;
                extraPad = title.length() - totalWidth;
                totalWidth = title.length();                
            } 
            s += "+" + "-".repeat( totalWidth ) + "+\n";
            s += "|" + title + " ".repeat(totalWidth - title.length()) + "|\n";
        }

        s += "+" + "-".repeat( totalWidth ) + "+\n";

        for ( int row = 0; row < table.size(); ++row ) {
            if ( padExtra ) {
                String a = getRow( row, cellLength ); 
                int lenSoFar = a.length() - sep.length();
                int remainingPadding = 1 + totalWidth - lenSoFar; // +1 from the removed above
                
                s += a.substring( 0, lenSoFar ); // Remove the seperator from the end of it
                s += " ".repeat( remainingPadding ) + sep + "\n"; // Add the padding and the seperator back in.
            } else 
                s += getRow( row, cellLength ) + "\n";
            if ( row + 1 < table.size() ) {
                s += "|";
                for ( int i = 0; i < cols; ++i ) {
                    int dashes = cellLength[i];
                    if ( padExtra && i+1 == cols ) dashes += extraPad;

                    s += "-".repeat( dashes );

                    // Add either '+' or a '|'. If it is the last column in the table, add the latter.
                    s += ( i+1 < cols ? "+" : "|" );
                }
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
            if ( text == null ) text = "";
            int padding = cellLength[i] - text.length();

            s += text + " ".repeat( padding ) + ( i+1 < cols ? sep : "" );
        }

        return s + "|";
    }


}
