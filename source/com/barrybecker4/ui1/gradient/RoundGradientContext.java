package com.barrybecker4.ui1.gradient;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

/**
 * Source derived from Java 2D graphics book by J. Knudsen.
 */
class RoundGradientContext implements PaintContext {
    protected Point2D point;
    protected Point2D radius;
    protected Color color1, color2;

    /**
     * Constructor
     */
    public RoundGradientContext( Point2D p, Color c1, Point2D r, Color c2 ) {
        point = p;
        color1 = c1;
        radius = r;
        color2 = c2;
    }

    public void dispose() {}

    public ColorModel getColorModel() {
        return ColorModel.getRGBdefault();
    }

    public Raster getRaster( int x, int y, int w, int h ) {
        WritableRaster raster =
                getColorModel().createCompatibleWritableRaster( w, h );

        int[] data = new int[w * h << 2];
        double rad = radius.distance( 0, 0 );
        for ( int j = 0; j < h; j++ ) {
            for ( int i = 0; i < w; i++ ) {
                double distance = point.distance( x + i, y + j );
                double ratio = distance / rad;
                if ( ratio > 1.0 )
                    ratio = 1.0;

                int base = (j * w + i) << 2;
                data[base] = (int) (color1.getRed() + ratio *
                        (color2.getRed() - color1.getRed()));
                data[base + 1] = (int) (color1.getGreen() + ratio *
                        (color2.getGreen() - color1.getGreen()));
                data[base + 2] = (int) (color1.getBlue() + ratio *
                        (color2.getBlue() - color1.getBlue()));
                data[base + 3] = (int) (color1.getAlpha() + ratio *
                        (color2.getAlpha() - color1.getAlpha()));
            }
        }
        raster.setPixels( 0, 0, w, h, data );

        return raster;
    }
}