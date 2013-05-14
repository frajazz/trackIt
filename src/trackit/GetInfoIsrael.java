/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trackit;

import java.net.URLEncoder;

/**
 *
 * @author FRA
 */
public class GetInfoIsrael extends GetInfoBase
{
    public GetInfoIsrael( String num )
    {
	super( num );
    }
    
    protected String getInfo( String numb )
    {
	try 
	{
	    String queryString = "OpenAgent";
	    queryString += "&lang=" + URLEncoder.encode( "EN", "UTF-8" );
	    queryString += "&itemcode=" + URLEncoder.encode( numb, "UTF-8" );

	    // Make connection
	    String url = "http://www.israelpost.co.il/itemtrace.nsf/trackandtraceJSON";
	    
	    String answ = sendRequest( url, queryString, false );
	    
  	    
	    String resp = "";
	    int strInd = answ.indexOf( "itemcodeinfo\" : \"" );
	    if( strInd < 0 )
		return "";
	    
	    strInd += 17;
	    int endInd = strInd;
	    endInd = answ.indexOf( "\"", endInd );	

	    resp = answ.substring( strInd, endInd );
	    if( resp.startsWith( "No info" ) )
		return "";
	    
	    return resp;
	} 
	catch( Exception ex ) 
	{
	    ex.printStackTrace();
	    return "";
	}
    }
}

