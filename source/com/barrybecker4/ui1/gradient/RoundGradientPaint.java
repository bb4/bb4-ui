package com.barrybecker4.ui1.gradient;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;

/**
 * Source derived from Java 2D graphics book by J. Knudsen.
 */
public class RoundGradientPaint
        implements Paint {
    protected Point2D point;
    protected Point2D radius;
    protected Color pointColor, bgColor;

    public RoundGradientPaint( double x, double y, Color pointColor,
                               Point2D radius, Color backgroundColor ) {
        if ( radius.distance( 0, 0 ) <= 0 ) {
            throw new IllegalArgumentException( "Radius must be greater than 0." );
        }
        point = new Point2D.Double( x, y );
        this.pointColor = pointColor;
        this.radius = radius;
        bgColor = backgroundColor;
    }

    public PaintContext createContext(ColorModel cm,
                                      Rectangle deviceBounds, Rectangle2D userBounds,
                                      AffineTransform xform, RenderingHints hints ) {
        Point2D transformedPoint = xform.transform(point, null );
        Point2D transformedRadius = xform.deltaTransform(radius, null );
        return new RoundGradientContext( transformedPoint, pointColor,
                transformedRadius, bgColor);
    }

    public int getTransparency() {
        int a1 = pointColor.getAlpha();
        int a2 = bgColor.getAlpha();
        return (((a1 & a2) == 0xff) ? OPAQUE : TRANSLUCENT);
    }
}