/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trackit;

import java.util.Vector;

/**
 *
 * @author FRA
 */
public class InfoKeeper 
{
    final private Vector<String[]> info;
    final private Vector<String[]> track;
    
    public InfoKeeper()
    {
	info = new Vector<String[]>();
	track = new Vector<String[]>();
    }
    
    public void addInfo( String type, String val )
    {
	info.add( new String[]{ type, val } );
    }
    
    public void addTrack( String[] typePoint )
    {
	track.add( typePoint );
    }

    public Vector<String[]> getInfo() 
    {
	return info;
    }

    public Vector<String[]> getTrack() 
    {
	return track;
    }

    public String getHTML() 
    {
	StringBuilder sb = new StringBuilder();
	
	sb.append( "<table>" );
	for( String[] elem : info )
	{
	    if( elem[ 1 ] != null && elem[ 1 ].length() > 0 )
	    sb.append( "<tr><td>" ).append( elem[ 0 ] ).append( " - " ).append( elem[ 1 ] ).append( "</td></tr>" );
	}
	
	if( track != null && track.size() > 0 )
	{
	    sb.append( "<tr><td><table border=\"1\">" );
	    int ln = track.firstElement().length;
	    for( String[] elem : track )
	    {
		sb.append( "<tr>" );
		for( int i = 0; i < ln; i++ )
		{
		    sb.append( "<td>" ).append( elem[ i ] ).append( "</td>" );	
		}
		sb.append( "</tr>" );
	    }
	}

	sb.append( "</table></td></tr>" );
	sb.append( "</table>" );
	
	return sb.toString();
    }
}
