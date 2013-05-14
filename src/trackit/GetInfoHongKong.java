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
public class GetInfoHongKong extends GetInfoBase
{
    public GetInfoHongKong( String num )
    {
	super( num );
    }
    
    protected String getInfo( String numb )
    {
	try 
	{
	    String queryString = "tracknbr=" + URLEncoder.encode( numb, "UTF-8" );
	    queryString += "&submit=" + URLEncoder.encode( "Enter", "UTF-8" );
	    
	    // Make connection
	    String url = "http://app3.hongkongpost.com/CGI/mt/genresult.jsp";
	    
	    String answ = sendRequest( url, queryString );
	//    System.out.println( answ );
	  	    
	    String resp = "";
	    int strInd = answ.indexOf( "<body class=\"bg\">" );
	    if( strInd < 0 )
		return "";
	    
	    int endInd = strInd;
	    int tmpInd = answ.indexOf( "</body", endInd );	
	    if( tmpInd > -1 )
		endInd = answ.indexOf( ">", tmpInd );	

	    resp = answ.substring( strInd, endInd );
	    
	    EditorKit kit = new HTMLEditorKit();
	    HTMLDocument doc = ( HTMLDocument )kit.createDefaultDocument();
	    kit.read( new ByteArrayInputStream( resp.getBytes( "UTF-8" ) ), doc, 0 );
	    
	    InfoKeeper info = new InfoKeeper();
	    
	    Element elem = doc.getElement( "clfContent" );
	    String tmpStr = getElemText( elem.getElement( 2 ), doc );
	    if( "OTHERS".equals( tmpStr ) )
		return "";
	    
	    info.addInfo( "Service", getElemText( elem.getElement( 2 ), doc ) );
	    tmpStr = getElemText( elem.getElement( 5 ), doc );
	    if( tmpStr.startsWith( "Destination - " ) )
		tmpStr = tmpStr.substring( "Destination - ".length(), tmpStr.length() - 1 );
	    info.addInfo( "Destination", tmpStr );
	    tmpStr = getElemText( elem.getElement( 6 ), doc );
	    int ind = tmpStr.indexOf( "To make further enquiry" );
	    if( ind > -1 )
		tmpStr = tmpStr.substring( 0, ind );
	    info.addInfo( "Information", tmpStr );
	    
	    return info.getHTML();
	} 
	catch( Exception ex ) 
	{
	    ex.printStackTrace();	
	    return "";
	}
    }
}

