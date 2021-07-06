/* $Id: Painting.java 685 2013-06-14 23:20:29Z Michael $
 *
 * ComputerArt.
 *
 * Released under Gnu Public License
 * Copyright © 2013 Michael G. Binz
 */

package de.michab.apps.art;

import java.awt.MediaTracker;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.ImageIcon;

/**
 * An extended ImageIcon that adds a File constructor. Used as a
 * Mack file type.
 *
 * @version $Rev: 685 $
 * @author Michael
 */
@SuppressWarnings("serial")
public class Painting extends ImageIcon
{
    /**
     * Creates an instance from a file.
     *
     * @param f The file to load.
     * @throws IllegalArgumentException If the passed file is not
     * an image file.
     * @throws FileNotFoundException If the file doesn't exist.
     */
    public Painting( File f )
        throws Exception
    {
        super( f.getPath() );

        if ( getImageLoadStatus() == MediaTracker.ERRORED )
        {
            if ( ! f.exists() )
                throw new FileNotFoundException( f.getPath() );
            throw new IllegalArgumentException(
                    "Unknown file format: " + f.getPath() );
        }
    }
}
