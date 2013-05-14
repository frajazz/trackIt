/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trackit;

import java.io.ByteArrayInputStream;
import java.net.URLEncoder;
import javax.swing.text.AttributeSet;
import javax.swing.text.EditorKit;
import javax.swing.text.Element;
import javax.swing.text.ElementIterator;
import javax.swing.text.StyleConstants;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

/**
 *
 * @author FRA
 */
public class GetInfoUSPS extends GetInfoBase
{
    public GetInfoUSPS( String num )
    {
	super( num );
    }
    
    protected String getInfo( String numb )
    {
	try 
	{
	    String queryString = "tLabels=" + URLEncoder.encode( numb, "UTF-8" );
	    queryString += "&FindTrackLabelorReceiptNumber=" + URLEncoder.encode( "Find", "UTF-8" );
	    
	    // Make connection
	    String url = "https://tools.usps.com/go/TrackConfirmAction.action";
	    
	    String answ = sendRequest( url, queryString );
	 //   System.out.println( answ );
	  	    
	    String resp = "";
	    int strInd = answ.indexOf( "<table id=\"tc-hits\"" );
	    if( strInd < 0 )
		return "";
	    
	    int endInd = strInd;
	    int tmpInd = answ.indexOf( "</table", endInd );	
	    if( tmpInd > -1 )
		endInd = answ.indexOf( ">", tmpInd );	

	    resp = answ.substring( strInd, endInd );
	    
	    EditorKit kit = new HTMLEditorKit();
	    HTMLDocument doc = ( HTMLDocument )kit.createDefaultDocument();
	    kit.read( new ByteArrayInputStream( resp.getBytes( "UTF-8" ) ), doc, 0 );
	    
	    InfoKeeper info = new InfoKeeper();
	    
	    ElementIterator iterator = new ElementIterator( doc );
	    Element element;
	    String[] track = new String[]{ "", "", "" };
	    while( ( element = iterator.next() ) != null )
	    {
		AttributeSet as = element.getAttributes();
		if( as.getAttribute( StyleConstants.NameAttribute ) == HTML.Tag.DIV ) 
		{
		    String elemClass = ( String )as.getAttribute( HTML.Attribute.CLASS );
		    if( "td-service".equals( elemClass ) )
			info.addInfo( "Service", getElemText( element, doc ) );
		    else if( "feature".equals( elemClass ) )
			info.addInfo( "Feature", getElemText( element, doc ) );
		    else if( "td-status".equals( elemClass ) )
			track[ 0 ] = getElemText( element, doc );
		    else if( "td-date-time".equals( elemClass ) )
			track[ 1 ] = getElemText( element, doc );
		    else if( "td-location".equals( elemClass ) )
		    {
			track[ 2 ] = getElemText( element, doc );
			info.addTrack( track );
			track = new String[]{ "", "", "" };
		    }
		}
	    }
	    
	    return info.getHTML();
	} 
	catch( Exception ex ) 
	{
	    ex.printStackTrace();	
	    return "";
	}
    }
}

