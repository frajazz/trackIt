/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trackit;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 *
 * @author FRA
 */
public class PropReader 
{
    private final Properties prop;
            
    public PropReader( Properties prop )
    {
        this.prop = prop;
    }
    
    public Properties getProp()
    {
        return prop;
    }
    
    public void updateProp( String key, String val )
    {
        prop.put( key, val );
    }
    
    public String getHTMLProp( String propNm )
    {
        String val = prop.getProperty( propNm );
        val = val.replace( '{', '<' );
        val = val.replace( '}', '>' );
        return val;
    }

    public String getProp( String propNm )
    {
        return prop.getProperty( propNm );
    }
    
    public String getProp( String propNm, String defVal )
    {
        return prop.getProperty( propNm, defVal );
    }
    
    public int getIntProp( String propNm )
    {
        return getIntProp( propNm, 0 );
    }
    
    public int getIntProp( String propNm, int defVal )
    {
        int rez = defVal;
        try
        {
            rez = Integer.parseInt( prop.getProperty( propNm ) );
        }
        catch( Exception ex )
        {
	    System.out.println( "Wrong ptoperty value for '" + propNm + "'" );
	}
        
        return rez;
    }    
    
    public void initFromFile( String propFName )
    {
        try 
        {
            getProp().loadFromXML( new FileInputStream( propFName ) );
        } 
        catch( Exception ex ) 
        {
            ex.printStackTrace();
        }
    }
    
    public void saveToFile( String propFName, String comment )
    {
        try 
        {
            getProp().storeToXML( new FileOutputStream( propFName ), comment );
        } 
        catch( Exception ex ) 
        {
            ex.printStackTrace();
        }
    }
}
