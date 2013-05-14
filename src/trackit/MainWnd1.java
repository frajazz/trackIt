/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trackit;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Locale;
import java.util.Properties;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
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
class MainWnd1 extends JFrame
{
    protected final static String propProp = "propEdit.prop", numbFile = "numbLst.txt";
    protected final PropReader pPropEd;
    private final DefaultListModel listModel, rezLstM;
    private final JEditorPane rezPain;
    private final String[] rezList;
    
 //   final JTextField numb;
    
    public MainWnd1() 
    {
	super( "trackIt" );
        Locale.setDefault( new Locale( "ru", "RU" ) );
	
	addWindowListener( new WindowAdapter() 
        {
            @Override
            public void windowClosing( WindowEvent e ) 
            {
                updateWndParam(); 
            }
        } );
	
	pPropEd = new PropReader( new Properties() );
	pPropEd.initFromFile( propProp );
	
	listModel = new DefaultListModel();
	rezLstM = new DefaultListModel();
	rezPain = new JEditorPane();
	rezList = new String[ 10 ];
		
	Init();
	initNumberList();
	setVisible( true );
    }
    
    private void initNumberList()
    {

	BufferedReader fr = null;
	try 
	{
	    fr = new BufferedReader( new FileReader( numbFile ) );
	    while( true )
	    {
		String tmpStr = fr.readLine();
		if( tmpStr == null )
		    break;
		listModel.addElement( tmpStr );
	    }
	} 
	catch( Exception ex ) 
	{
	    ex.printStackTrace();
	}
	finally
	{
	    try 
	    {
		if( fr != null )
		    fr.close();
	    } 
	    catch( Exception ex ) 
	    {
		ex.printStackTrace();
	    }
	}
	
    }
    
    private void updateWndParam() 
    {
	Rectangle rec = getBounds();
	pPropEd.updateProp( "WndHi", "" + rec.height );
	pPropEd.updateProp( "WndWd", "" + rec.width );
	
	pPropEd.saveToFile( propProp, "" );
	
	FileWriter fw = null; 
	try
	{
	    fw = new FileWriter( numbFile );
	
	    for( int i = 0; i < listModel.getSize(); i++ )
		fw.write( ( String )listModel.get( i ) + "\n" );	    
	    
	    fw.flush();
	}
	catch( Exception ex )
	{
	    ex.printStackTrace();
	}
	finally
	{
	    try
	    {
		if( fw != null )
		    fw.close();
	    } 
	    catch( Exception ex ) 
	    {
		ex.printStackTrace();
	    }
	}
    }

    private void Init() 
    {
	setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	
	JPanel mainPnl = new JPanel();
	add( mainPnl );
	mainPnl.setLayout( new BorderLayout() );
	
	JPanel nPnl = new JPanel();
	mainPnl.add( nPnl, BorderLayout.NORTH );
	nPnl.setLayout( new FlowLayout() );
	
	nPnl.add( new JLabel( "Number" ) );
	final JTextField numb = new JTextField( "", 20 );
	nPnl.add( numb );
	
	JButton addBtn = new JButton( "Add to list" );
	nPnl.add( addBtn );
	addBtn.addActionListener( new ActionListener()
        {
           @Override
           public void actionPerformed( ActionEvent e )
           {
               addNumber( numb.getText() );
           }
        } );
	
	final JList numbLst = new JList( listModel );
	numbLst.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
	numbLst.setLayoutOrientation( JList.HORIZONTAL_WRAP );
	numbLst.setVisibleRowCount( -1 );
	numbLst.addListSelectionListener( new ListSelectionListener() {

	    @Override
	    public void valueChanged( ListSelectionEvent e ) 
	    {
		if( e.getValueIsAdjusting() == false ) 
		{
		    int ind = numbLst.getSelectedIndex();
		    if( ind != -1 ) 
		    {
			numb.setText( ( String )listModel.get( ind ) );
		    }
		}
	    }
	} );
	JScrollPane numbPnl = new JScrollPane( numbLst );
	numbPnl.setPreferredSize( new Dimension( 150, 200 ) );
	mainPnl.add( numbPnl, BorderLayout.WEST );
	
	JPanel sPnl = new JPanel();
	mainPnl.add( sPnl, BorderLayout.SOUTH );
	sPnl.setLayout( new FlowLayout() );	
	
	JButton actBtn = new JButton( "Get info" );
        sPnl.add( actBtn );
        actBtn.addActionListener( new ActionListener()
        {
           @Override
           public void actionPerformed( ActionEvent e )
           {
               getInfo( numb.getText() );
           }
        } );
	
	
//	JList rezLst = new JList( rezLstM );
//	rezLst.setFocusable( false );
//	rezLst.setLayoutOrientation( JList.HORIZONTAL_WRAP );
//	rezLst.setVisibleRowCount( -1 );
//	rezLst.setCellRenderer( new RespCellRenderer() );
	
	JScrollPane scrPn = new JScrollPane( rezPain );
	mainPnl.add( scrPn, BorderLayout.CENTER );
	
	int hi = pPropEd.getIntProp( "WndHi", 100 );
        int wd = pPropEd.getIntProp( "WndWd", 100 );
	
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x1 = ( screenSize.width - wd ) / 2;
        int y1 = ( screenSize.height - hi ) / 2;
        setBounds( x1, y1, wd, hi );
    }
 
    private void addNumber( String numb )
    {
	for( int i = 0; i < listModel.size(); i++ )
	{
	    if( numb.equals( listModel.get( i ) ) )
		return;
	}
	listModel.addElement( numb );
    }
    
    private void getInfo( String numb )
    {
	rezLstM.removeAllElements();

	getInfoFromHongKong( numb );
//	getInfoFromUPS( numb );
	getInfoFromUSPS( numb );
	getInfoFromIsraelpost( numb );
	getInfoFromRus( numb );
	
	if( rezLstM.size() == 0 )
	{
	    rezLstM.addElement( "No information has been found" );
	}
     }
    
    private String sendRequest( String urlStr, String reqStr ) throws Exception
    {
	return sendRequest( urlStr, reqStr, true );	
    }
    
    private String sendRequest( String urlStr, String reqStr, boolean isPost ) throws Exception
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
    
    private boolean getInfoFromRus( String numb )
    {
	try 
	{
	    String queryString = "BarCode=" + URLEncoder.encode( numb, "UTF-8" );
	    queryString += "&searchsign=" + URLEncoder.encode( "1", "UTF-8" );
	    
	    // Make connection
	    String url = "http://www.russianpost.ru/rp/servise/ru/home/postuslug/trackingpo";
	    
	    String answ = sendRequest( url, queryString );
	  	    
	    String resp = "";
	    int strInd = answ.indexOf( "Результат поиска" );
	    strInd = answ.indexOf( "<table", strInd );
	    if( strInd < 0 )
		return false;
//		resp = "No information has been found";
	    else
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
	    
	    rezLstM.addElement( resp );
	} 
	catch( Exception ex ) 
	{
	    ex.printStackTrace();	
	    return false;
	}
	
	return true;
    }
     
    private boolean getInfoFromUSPS( String numb )
    {
	try 
	{
	    String queryString = "tLabels=" + URLEncoder.encode( numb, "UTF-8" );
	    queryString += "&FindTrackLabelorReceiptNumber=" + URLEncoder.encode( "Find", "UTF-8" );
	    
	    // Make connection
	    String url = "https://tools.usps.com/go/TrackConfirmAction.action";
	    
	    String answ = sendRequest( url, queryString );
	  	    
	    String resp = "";
	    int strInd = answ.indexOf( "<table id=\"tc-hits\"" );
	    if( strInd < 0 )
		return false;
	    
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
	    
	    rezLstM.addElement( info.getHTML() );
	} 
	catch( Exception ex ) 
	{
	    ex.printStackTrace();	
	}
	
	return true;
    }
    
    private boolean getInfoFromUPS( String numb )
    {
	try 
	{
	    String queryString = "tracknum=" + URLEncoder.encode( numb, "UTF-8" );
	    queryString += "&track.x=" + URLEncoder.encode( "Track", "UTF-8" );
	    
	    // Make connection
	    String url = "http://wwwapps.ups.com/WebTracking/track?loc=en_US";
	    
	    String answ = sendRequest( url, queryString );
	  	    
/*	    String resp = "";
	    int strInd = answ.indexOf( "<table id=\"tc-hits\"" );
	    if( strInd < 0 )
		return false;
	    
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
	    
	    rezLstM.addElement( info.getHTML() ); */
	    rezLstM.addElement( answ );
	    
	} 
	catch( Exception ex ) 
	{
	    ex.printStackTrace();	
	}
	
	return true;
    }
    
    private boolean getInfoFromHongKong( String numb )
    {
	try 
	{
	    String queryString = "tracknbr=" + URLEncoder.encode( numb, "UTF-8" );
	    queryString += "&submit=" + URLEncoder.encode( "Enter", "UTF-8" );
	    
	    // Make connection
	    String url = "http://app3.hongkongpost.com/CGI/mt/genresult.jsp";
	    
	    String answ = sendRequest( url, queryString );
	  	    
	    String resp = "";
	    int strInd = answ.indexOf( "<body class=\"bg\">" );
	    if( strInd < 0 )
		return false;
	    
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
		return false;
	    
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
	    
	    rezLstM.addElement( info.getHTML() );
	} 
	catch( Exception ex ) 
	{
	    ex.printStackTrace();	
	}
	
	return true;
    }
    
    private boolean getInfoFromIsraelpost( String numb )
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
		return false;
	    
	    strInd += 17;
	    int endInd = strInd;
	    endInd = answ.indexOf( "\"", endInd );	

	    resp = answ.substring( strInd, endInd );
	    if( resp.startsWith( "No info" ) )
		return false;
	    
	    rezLstM.addElement( resp );
	} 
	catch( Exception ex ) 
	{
	    ex.printStackTrace();	
	}
	
	return true;
    }    
        
    private String getElemText( Element elem, HTMLDocument doc ) throws BadLocationException
    {
	int startOffset = elem.getStartOffset();
	int endOffset = elem.getEndOffset();
	int length = endOffset - startOffset;
	return doc.getText( startOffset, length ).trim();
    }
    
    private void writeResp( String resp )
    {
	try
	{
	    BufferedOutputStream fw = new BufferedOutputStream( new FileOutputStream( new File( "resp.html" ) ) );
	    fw.write( resp.getBytes( "UTF-8" ) );
	    fw.flush();
	    fw.close();
	}
	catch( Exception ex )
	{
	    ex.printStackTrace();
	}
    }
    
    void addHTML( String fName )
    {
        try
        {
	    JEditorPane textPnl;
	    textPnl = new JTextPane();
	    textPnl.setContentType( "text/html; charset=UTF-8" );
	    
            File fl = new File( fName );
            URL url = fl.toURI().toURL();
            textPnl.setPage( url );
	    
	    rezLstM.addElement( textPnl );
	    
//	    scrPn.getViewport().removeAll();
//	    scrPn.getViewport().add( textPnl );
//	    scrPn.invalidate();
        }
        catch( Exception ex )
        {
            ex.printStackTrace();
        }
    }    
    
    class RespCellRenderer implements ListCellRenderer
    {
	@Override
	public Component getListCellRendererComponent( JList list, Object value, int index, boolean isSelected, boolean cellHasFocus ) 
	{
	    try 
	    {
		writeResp( ( String )value );
		
		JEditorPane textPnl;
		textPnl = new JTextPane();
		textPnl.setContentType( "text/html; charset=UTF-8" );
		
		textPnl.setText( ( String )value );
		
		File fl = new File( "resp.html" );
		URL url = fl.toURI().toURL();
		
		return textPnl;
	    } 
	    catch( Exception ex ) 
	    {
		ex.printStackTrace();
	    }
	    return null;
	}
    }    
}
