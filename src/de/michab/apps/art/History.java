/* $Id: History.java 685 2013-06-14 23:20:29Z Michael $
 *
 * ComputerArt.
 *
 * Released under Gnu Public License
 * Copyright © 2013 Michael G. Binz
 */

package de.michab.apps.art;

import java.util.Arrays;

/**
 * Holds a bound history of objects that are added with put.
 * If an object is put into the history, the oldest object
 * is removed.
 *
 * @version $Rev: 685 $
 * @author Michael Binz
 */
public class History<T>
{
    private final Object[] _buffer;
    int _index = 0;

    public History( int size, T initialContent )
    {
        _buffer = new Object[ size ];
        fill( initialContent );
    }
    public History( int size )
    {
        this( size, null );
    }

    public synchronized void fill( T filler )
    {
        Arrays.fill( _buffer, filler );
    }

    public synchronized void put( T element )
    {
        _buffer[_index] = element;
        _index = (_index+1) % _buffer.length;
    }

    @SuppressWarnings("unchecked")
    public synchronized T getOldest()
    {
        return (T)_buffer[ _index ];
    }
}
