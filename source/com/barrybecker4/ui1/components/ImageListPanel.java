// Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui1.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Displays an array of images.
 * The images are displayed in a way that uses the available space
 * effectively without using a scrollbar.
 * The images may get smaller than their actual size, but they
 * will maintain their aspect ratio, and they will not get bigger.
 *
 * @author Barry Becker
 */
public final class ImageListPanel extends JPanel
                                  implements MouseMotionListener, MouseListener {
    private List<BufferedImage> images;
    private BufferedImage highlightedImage = null;
    private List<BufferedImage> selectedImages;

    private double imageRatio;
    private int baseImageWidth;
    private int numColumns;
    private int imageDisplayHeight;
    private int imageDisplayWidth;
    private int maxNumSelections = Integer.MAX_VALUE;
    private boolean enlargeHighlightedImage_ = false;
    private static final int TIME_TO_ENLARGE = 900;

    private static final Color BACKGROUND_COLOR = Color.WHITE;
    private static final Color HIGHLIGHT_COLOR = Color.ORANGE;
    private static final Color SELECTION_COLOR = Color.BLUE;
    private static final Stroke BORDER_STROKE = new BasicStroke( 2.0f );

    private static final int IMAGE_MARGIN = 2;
    private static final int TOTAL_MARGIN = 2 * IMAGE_MARGIN;

    private List<ImageSelectionListener> imageSelectionListeners;

    /**
     * Use this Constructor if you just want the empty panel initially.
     * Maybe add constructor for unequal sized images where we specify the desired max size.
     */
    public ImageListPanel() {
        imageSelectionListeners = new LinkedList<ImageSelectionListener>();

        this.setMinimumSize(new Dimension(100, 100));
        this.addComponentListener( new ComponentAdapter()  {
            @Override
            public void componentResized( ComponentEvent ce ) {
                repaint();
            }
        } );
        this.addMouseMotionListener(this);
        this.addMouseListener(this);
    }

    /**
     * Constructor.
     * @param images array of identically sized images to show in an array.
     */
    public ImageListPanel(List<BufferedImage> images) {
        this();
        setImageList(images);
    }

    public void setImageList(List<BufferedImage> images) {
        assert images!=null;
        this.images = images;
        imageRatio = calculateImageRatio(images);
        baseImageWidth = images.get(0).getWidth() + TOTAL_MARGIN;
        selectedImages = new ArrayList<BufferedImage>();
        highlightedImage = null;
        this.repaint();
    }

    public List<BufferedImage> getImageList() {
        return images;
    }

    public void setMaxNumSelections(int max) {
        maxNumSelections = max;
    }

    /**
     * Sometimes we just want to show a single image and have it fit the
     * available area.
     */
    public void setSingleImage(BufferedImage image) {
        if (image == null) {
            System.out.println("warning: setting null image"); //NON-NLS
            return;
        }
        List<BufferedImage> imageList = new ArrayList<BufferedImage>(1);
        imageList.add(image);
        setImageList(imageList);
    }

    /**
     * This is how the client can register itself to receive these events.
     * @param isl the listener to add
     */
    public void addImageSelectionListener( ImageSelectionListener isl )
    {
        imageSelectionListeners.add(isl);
    }

    /**
     * This is how the client can unregister itself to receive these events.
     * @param isl the listener  to remove
     */
    public void removeImageSelectionListener( ImageSelectionListener isl )
    {
        imageSelectionListeners.remove(isl);
    }


    public List<Integer> getSelectedImageIndices() {

        if (images == null)
            return null;
        List<Integer> selectedIndices = new LinkedList<Integer>();

        for (int i = 0; i < images.size(); i++ ) {

            BufferedImage img =  images.get(i);
            if (selectedImages.contains(img)) {
                selectedIndices.add(i);
            }
        }
        return selectedIndices;
    }

    public void setSelectedImageIndices(List<Integer> selectedIndices) {
        assert selectedIndices != null;
        // replace what we have with the new selections
        selectedImages.clear();
        for (int i = 0; i < images.size(); i++ ) {
            if (selectedIndices.contains(i)) {
                BufferedImage img =  images.get(i);
                selectedImages.add(img);
                updateImageSelectionListeners(img);
            }
        }
    }

    private static double calculateImageRatio(List<BufferedImage> images) {
        if (images == null || images.isEmpty()) return 1.0;
        // first assert that all the images are the same size
        assert images.size() > 0;
        BufferedImage firstImage = images.get(0);

        int w = firstImage.getWidth();
        int h = firstImage.getHeight();
        for (BufferedImage img : images) {
            assert (img.getWidth() == w) :
                    "Image dimensions " + img.getWidth() + ", " + img.getHeight() +" do not match first: "+ w+ ", "+ h;
        }
        w += TOTAL_MARGIN;
        h += TOTAL_MARGIN;
        return (double)w/(double)h;
    }

    /**
     * This renders the array of images to the screen.
     */
    @Override
    protected void paintComponent( Graphics g ) {

        super.paintComponents( g );
        if (images == null || images.size() == 0)
            return;

        // erase what's there and redraw.
        g.clearRect( 0, 0, getWidth(), getHeight() );
        g.setColor( BACKGROUND_COLOR );
        g.fillRect( 0, 0, getWidth(), getHeight() );

        double panelRatio = (double)getWidth() / (double)getHeight();

        // find the number of rows that will give the closest match on the aspect ratios
        int numImages = images.size();
        int numRows = 1;
        numColumns = numImages;
        double ratio = imageRatio * numImages;
        double lastRatio = 100000000;
        while (ratio > panelRatio) {
            lastRatio = ratio;
            numRows++;
            numColumns = (int)Math.ceil((double)numImages / (double)numRows);
            ratio = imageRatio * (double) numColumns /(double)numRows;
            //System.out.println("ratio="+ ratio +" panelRatio=" + panelRatio + " numRows="+ numRows);
        }
        if (panelRatio - ratio < lastRatio - panelRatio) {
            // then we may have space on right side, but vertical space completely used
            imageDisplayHeight = Math.min(getHeight() / numRows, (int)((baseImageWidth)/ imageRatio) )- TOTAL_MARGIN;
            imageDisplayWidth = (int) (imageDisplayHeight * imageRatio);
        } else {
            // horizontal space completely used
            numRows--;
            numColumns = (int)Math.ceil((double)numImages / (double)numRows);
            imageDisplayWidth = Math.min( getWidth() / numColumns, baseImageWidth) - TOTAL_MARGIN;
            imageDisplayHeight = (int) (imageDisplayWidth / imageRatio);
        }

        Graphics2D g2 = (Graphics2D)g;
        int enlargedImageIndex = -1;

        g2.setStroke( BORDER_STROKE );

        for (int i = 0; i < images.size(); i++ ) {

            int colPos = getColumnPosition(i) + IMAGE_MARGIN;
            int rowPos = getRowPosition(i)  + IMAGE_MARGIN;
            BufferedImage img =  images.get(i);
            if (img.equals(highlightedImage) && enlargeHighlightedImage_) {
                enlargedImageIndex = i;
            }
            g2.drawImage(img, colPos , rowPos,
                    imageDisplayWidth, imageDisplayHeight, null);

            // put a border around images that are selected or highlighted
            if (highlightedImage == img || selectedImages.contains(img)) {
                g2.setColor((highlightedImage == img) ? HIGHLIGHT_COLOR :SELECTION_COLOR);
                g2.drawRect(colPos- IMAGE_MARGIN, rowPos - IMAGE_MARGIN,
                                     imageDisplayWidth + TOTAL_MARGIN, imageDisplayHeight + TOTAL_MARGIN);
            }
        }

        if (enlargedImageIndex >= 0) {
            int w = highlightedImage.getWidth();
            int h = highlightedImage.getHeight();

            if (w > getWidth()) {
                w = getWidth();
                h = (int)(w/ imageRatio);
            }
            if (h > getHeight()) {
                h = getHeight();
                w = (int)(h * imageRatio);
            }
            g2.drawImage(highlightedImage, 0 , 0, w, h, null);
        }
    }

    private int getRowPosition(int i) {
        int row = i / numColumns;
        return row * ( imageDisplayHeight + TOTAL_MARGIN);
    }

    private int getColumnPosition(int i) {
        int col = i % numColumns;
        return col * ( imageDisplayWidth + TOTAL_MARGIN);
    }


    /**
     * @return  the image that the mouse is currently over (at x, y coordinates)
     */
    private BufferedImage findImageOver(int x, int y) {

        if (images == null) return null;
        int selectedIndex = -1;
        for (int i = 0; i < images.size(); i++ ) {
            int row = i / numColumns;
            int col = i % numColumns;
            int colPos = col * (imageDisplayWidth + TOTAL_MARGIN);
            int rowPos = row * (imageDisplayHeight + TOTAL_MARGIN);
            if (  x > colPos && x <= colPos + imageDisplayWidth
                && y > rowPos && y <= rowPos + imageDisplayHeight) {
                selectedIndex = i;
                break;
            }
        }
        if (selectedIndex == -1) {
            return null;
        }
        return images.get(selectedIndex);
    }

    public void mouseMoved(MouseEvent e) {
        BufferedImage image = findImageOver(e.getX(), e.getY());

        boolean changed = image != highlightedImage;

        if (changed) {
            if (image != null) {
                highlightedImage = image;
            } else {
                highlightedImage = null;
            }
            this.repaint();

            // start a time that is canceled if the mouse moves
            final Timer enlargementTimer = new Timer();
            enlargeHighlightedImage_ = false;
            enlargementTimer.schedule(
                    new TimerTask () {
                       @Override
                       public void run ()   {
                            enlargeHighlightedImage_ = true;
                            enlargementTimer.cancel ();
                            repaint();
                       }
                   }, TIME_TO_ENLARGE);
        }
    }


    public void mouseDragged(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {}

    /**
     * An image was selected on release of the mouse click.
     */
    public void mouseReleased(MouseEvent e) {
        BufferedImage img = findImageOver(e.getX(), e.getY());
        if (img != null) {
            if (selectedImages.contains(img)) {
                selectedImages.remove(img);
            }
            else {
                // if we are at our limit we first need to remove the first selected
                if (selectedImages.size() == maxNumSelections) {
                    selectedImages.remove(0);
                }
                selectedImages.add(img);
            }
            this.repaint();
        }

         updateImageSelectionListeners(img);
    }

    private void updateImageSelectionListeners(BufferedImage selectedImage) {
        // make sure listeners get notification that an image was selected.
        for (ImageSelectionListener isl : imageSelectionListeners) {
                isl.imageSelected(selectedImage);
         }
    }

    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
}

