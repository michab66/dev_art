/* $Id: ComputerArt.java 784 2015-01-10 20:31:10Z Michael $
 *
 * ComputerArt.
 *
 * Released under Gnu Public License
 * Copyright © 2013 Michael G. Binz
 */

package de.michab.apps.art;

import java.util.EventObject;

import javax.swing.ImageIcon;

import org.jdesktop.application.Application;
import org.jdesktop.smack.MackAppViewer;


/**
 *
 * @version $Rev: 784 $
 * @author Michael
 */
public class ComputerArt
    extends MackAppViewer<Painting, MackTiledCanvas>
{
    public ComputerArt()
    {
        // No UI elements
        super( Painting.class, false, false, false );
    }

    /**
     * No confirmation needed.
     */
    protected boolean canExit( EventObject eo )
    {
        return true;
    }

    /* (non-Javadoc)
     * @see de.michab.mack.MackApplication#createMainComponent()
     */
    @Override
    protected MackTiledCanvas createMainComponent()
    {
        ImageIcon ii =
                Application.getResourceManager().
                getApplicationResourceMap().
                getImageIcon( "Application.initialImage" );
        return new MackTiledCanvas( ii.getImage() );
    }

    public static void main( String[] argv )
    {
        launch( ComputerArt.class, argv );
    }

    /* (non-Javadoc)
     * @see de.michab.mack.MackAppViewer#load(FT[])
     */
    @Override
    public void load( Painting[] files )
    {
        getMainComponent().setTileImage( files[0].getImage() );
    }
}
