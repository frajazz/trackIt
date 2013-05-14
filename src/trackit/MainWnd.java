/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trackit;

import java.awt.BorderLayout;
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
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URL;
import java.util.Locale;
import java.util.Properties;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author FRA
 */
class MainWnd extends JFrame
{
    protected final static String propProp = "propEdit.prop", numbFile = "numbLst.txt";
    protected final PropReader pPropEd;
    private final DefaultListModel listModel;
    private final JScrollPane scrPn;
    private JButton actBtn;
    
    private boolean busyFl = false;
    
 //   final JTextField numb;
    
    public MainWnd() 
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
	scrPn = new JScrollPane();
		
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
	
	actBtn = new JButton( "Get info" );
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
	if( busyFl )
	    return;

	busyFl = true;
	
	int getCnt = 4, exitCnt = 4;
	GetInfoBase[] getLst = new GetInfoBase[ getCnt ];
	String[] rezLst = new String[ getCnt ];
	
	updateInfo( null );
	
	getLst[ 0 ] = new GetInfoRus( numb );
	getLst[ 1 ] = new GetInfoUSPS( numb );
	getLst[ 2 ] = new GetInfoHongKong( numb );
	getLst[ 3 ] = new GetInfoIsrael( numb );
	
	for( int i = 0; i < getCnt; i++ )
	{
	    rezLst[ i ] = null;
	    getLst[ i ].start();
	}
	
	while( exitCnt > 0 )
	{
	    int prevCnt = exitCnt;
	    
	    for( int i = 0; i < getCnt; i++ )
	    {
		if( getLst[ i ].isReady() && rezLst[ i ] == null )
		{
		    rezLst[ i ] = getLst[ i ].getInfo();
		    exitCnt--;
		}
	    }
	    
	    if( prevCnt != exitCnt )
		updateInfo( rezLst );
	}
	
	//( new GetInfoUSPS( 0, numb, this ) ).start();
	busyFl = false;
    }
    
    public void updateInfo( String[] rez )
    {
	StringBuilder sb = new StringBuilder();
	sb.append( "<table>" );
	for( int i = 0; rez != null && i < rez.length; i++ )
	{
	    if( rez[ i ] != null && rez[ i ].length() > 0 )
		sb.append( "<tr><td>" ).append( rez[ i ] ).append( "</td></tr>" );
	}
	sb.append( "</table>" );
	
	writeResp( sb.toString() );
	
	try
        {
	    JTextPane rezPain = new JTextPane();
	    rezPain.setContentType( "text/html; charset=UTF-8" );
            File fl = new File( "resp.html" );
            URL url = fl.toURI().toURL();
            rezPain.setPage( url );
	    
		scrPn.getViewport().removeAll();
		scrPn.getViewport().add( rezPain );
        }
        catch( Exception ex )
        {
            ex.printStackTrace();
        }
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
}
