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
public class GetInfoRus extends GetInfoBase
{
    public GetInfoRus( String num )
    {
	super( num );
    }
    
    protected String getInfo( String numb )
    {
	try 
	{
	    String queryString = "BarCode=" + URLEncoder.encode( numb, "UTF-8" );
	    queryString += "&searchsign=" + URLEncoder.encode( "1", "UTF-8" );

	    // Make connection
	    String url = "http://www.russianpost.ru/rp/servise/ru/home/postuslug/trackingpo";

	    String answ = sendRequest( url, queryString );

	    if( answ.indexOf( "<body onload=\"document.myform.submit();\">" ) > -1 )
	    {
		String keyS = "input id=\"key\" name=\"key\" value=\"";
		int ind = answ.indexOf( keyS );
		if( ind > -1 )
		{
		    ind += keyS.length();
		    String key = answ.substring( ind, answ.indexOf( '"', ind ) );
		    String queryString2 = "key=" + URLEncoder.encode( key, "UTF-8" );
		    answ = sendRequest( url, queryString2 );
    		    answ = sendRequest( url, queryString );
	    	}
	    }

	    String resp = "";
	    int strInd = answ.indexOf( "Результат поиска" );

	    if( strInd > 0 )
	    {
		int endInd = strInd;
		for( int i = 0; i < 2; i++ )
		{
		    int tmpInd = answ.indexOf( "</table", endInd );	
		    if( tmpInd > -1 )
			endInd = answ.indexOf( ">", tmpInd );	
		}

		resp = answ.substring( strInd, endInd );
	    }

	    return resp;
	} 
	catch( Exception ex ) 
	{
	    ex.printStackTrace();	
	    return "";
	}
    }
}

