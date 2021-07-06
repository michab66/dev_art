/* $Id: MackTiledCanvas.java 685 2013-06-14 23:20:29Z Michael $
 *
 * ComputerArt.
 *
 * Released under Gnu Public License
 * Copyright © 2013 Michael G. Binz
 */
package de.michab.apps.art;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

/**
 *
 * @version $Rev: 685 $
 * @author Michael Binz
 */
@SuppressWarnings("serial")
public class MackTiledCanvas extends JPanel
{
    private Image tileimage;
    private int tilewidth;
    private int tileheight;
    private Rectangle bounds;
    private Insets _insets;

    private float _offsetX = 0;
    private float _offsetY = 0;

    private int TIMER_PERIOD = 1000 / 25;
    private Timer _timer;

    /**
     * Create a JTiledPanel with the given image. The tile argument may be null,
     * you can set it later with setTileImage(). Note that a JTiledPanel is
     * always opaque.
     */
    public MackTiledCanvas()
    {
        this( null );
    }
    public MackTiledCanvas( Image tile )
    {
        setTileImage( tile );
        setOpaque( true );
        bounds = new Rectangle();
        _insets = new Insets( 0, 0, 0, 0 );

        addMouseListener( _ml );
        addMouseMotionListener( _ml );

        setCursor( Cursor.getPredefinedCursor( Cursor.HAND_CURSOR ) );
    }

    private MouseAdapter _ml = new MouseAdapter()
    {
        private int _startX;
        private int _startY;

        private final History<MouseEvent> _meHistory =
                new History<MouseEvent>( 2 );

        private float _maOffsetX = 0;
        private float _maOffsetY = 0;

        @Override
        public void mousePressed( MouseEvent me )
        {
            if ( _timer != null )
                _timer.cancel();

            _startX = me.getX();
            _startY = me.getY();

            _maOffsetX = _offsetX;
            _maOffsetY = _offsetY;

            _meHistory.fill( me );
        }

        @Override
        public void mouseReleased( MouseEvent me )
        {
            int dX = me.getX() - _meHistory.getOldest().getX();
            int dY = me.getY() - _meHistory.getOldest().getY();

            if ( dX == 0 && dY == 0 )
                return;

            long time = me.getWhen() - _meHistory.getOldest().getWhen();
            if ( time <= 0 )
                return;

            float dT = time;

            animationX = dX / dT;
            animationY = dY / dT;

            _timer = new Timer( getClass().getSimpleName(), true );
            _timer.scheduleAtFixedRate( getTask(), 0, TIMER_PERIOD );
        }

        @Override
        public void mouseDragged( MouseEvent me )
        {
            int deltaX = me.getX() - _startX;
            int deltaY = me.getY() - _startY;

            _offsetX = Math.round( _maOffsetX + deltaX ) % tilewidth;
            _offsetY = Math.round( _maOffsetY + deltaY ) % tileheight;

            _meHistory.put( me );

            repaint();
        }
    };

    private float animationX;
    private float animationY;

    private TimerTask getTask()
    {
        return new TimerTask()
        {
            public void run()
            {
                _offsetX += (TIMER_PERIOD * animationX);
                _offsetX %= tilewidth;
                _offsetY += (TIMER_PERIOD * animationY);
                _offsetY %= tileheight;

                repaint();
            }
        };
    }

    public void setOffsetX( float offset )
    {
        _offsetX = offset % tilewidth;
    }
    public void setOffsetY( float offset )
    {
        _offsetY = offset % tileheight;
    }

    /**
     * Get the current tiling image, or null if there isn't any right now.
     */
    public Image getTileImage()
    {
        return tileimage;
    }

    public void setTileImage( Image tile )
    {
        tileimage = tile;

        if ( tile == null )
        {
            setPreferredSize( null );
            return;
        }

        tilewidth = tile.getWidth( this );
        tileheight = tile.getHeight( this );

        setPreferredSize( new Dimension( tilewidth, tileheight ) );

        repaint();
    }

    /**
     * Paint this component
     */
    public void paintComponent( Graphics g )
    {
        if ( tileimage == null )
            return;

        getBounds( bounds );

        Insets insets = getInsets( _insets );
        bounds.translate( insets.left, insets.top );
        bounds.width -= (insets.left + insets.right);
        bounds.height -= (insets.top + insets.bottom);

        Shape cache = g.getClip();
        g.clipRect( bounds.x, bounds.y, bounds.width, bounds.height );

        float startx = _offsetX > 0 ?
                _offsetX - tilewidth : _offsetX;
        float starty = _offsetY > 0 ?
                _offsetY - tileheight : _offsetY;

        for ( int yp = bounds.y + Math.round( starty ) ; yp < bounds.y + bounds.height; yp += tileheight )
        {
            for ( int xp = bounds.x + Math.round( startx ) ; xp < bounds.x + bounds.width; xp += tilewidth )
            {
                g.drawImage( tileimage, xp, yp, getBackground(), this );
            }
        }
        g.setClip( cache );
    }
}
