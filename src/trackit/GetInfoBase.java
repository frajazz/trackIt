/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trackit;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.html.HTMLDocument;

/**
 *
 * @author FRA
 */
public class GetInfoBase extends Thread
{
    private final String num;
    protected boolean readyFl = false;
    protected String rezInfo = null;
    
    public GetInfoBase( String num )
    {
	this.num = num;
    }
    
    public boolean isReady()
    {
	return readyFl;
    }
    
    public String getInfo()
    {
	return rezInfo;
    }	    
    
    @Override
    public void run()
    {
	rezInfo = getInfo( num );
	readyFl = true;
    }
    
    protected String getInfo( String numb )
    {
	return null;
    }
    
    protected String sendRequest( String urlStr, String reqStr ) throws Exception
    {
	return sendRequest( urlStr, reqStr, true );	
    }
    
    protected String sendRequest( String urlStr, String reqStr, boolean isPost ) throws Exception
    {
	URLConnection urlConnection = null;
	
	if( isPost )
	{
	    URL url = new URL( urlStr );
	    urlConnection = url.openConnection();
	    urlConnection.setDoOutput( true );
	    OutputStreamWriter out = new OutputStreamWriter( urlConnection.getOutputStream() );
	    // Write query string to request body
	    out.write( reqStr );
	    out.flush();
	    out.close();
	}
	else
	{
	    URL url = new URL( urlStr + "?" + reqStr );
	    urlConnection = url.openConnection();
	}

	// Read the response
	BufferedReader in = new BufferedReader(
	   new InputStreamReader( urlConnection.getInputStream(), "UTF-8" ) );

	StringBuilder sb = new StringBuilder();
	String line = null;
	while ((line = in.readLine()) != null)
	    sb.append( line );
	
	in.close();
	
	return sb.toString();
    }    
    
    protected String getElemText( Element elem, HTMLDocument doc ) throws BadLocationException
    {
	int startOffset = elem.getStartOffset();
	int endOffset = elem.getEndOffset();
	int length = endOffset - startOffset;
	return doc.getText( startOffset, length ).trim();
    }
}
