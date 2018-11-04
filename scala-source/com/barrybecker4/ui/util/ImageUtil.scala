/* Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.ui.util

import javax.imageio.IIOImage
import javax.imageio.ImageIO
import javax.imageio.ImageWriteParam
import javax.imageio.plugins.jpeg.JPEGImageWriteParam
import java.awt.Color
import java.awt.GraphicsEnvironment
import java.awt.Image
import java.awt.image.{BufferedImage, ImageObserver, RenderedImage}
import java.io._

import ImageUtil.ImageType.ImageType


/**
  * A utility class for generating image files and manipulating images.
  * @author Barry Becker
  */
object ImageUtil {

  object ImageType extends Enumeration {
    type ImageType = Value
    val PNG, JPG = Value
  }

  /** @return a BufferedImage from an Image*/
  def makeBufferedImage(image: Image): BufferedImage = {
    val bImg = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB)
    val g2 = bImg.createGraphics
    g2.drawImage(image, null, (img: Image, infoflags: Int, x: Int, y: Int, width: Int, height: Int) => {
      println(s"loading image w=$width h=$height ...")
      true
    })
    g2.dispose()
    bImg
  }

  /** Create an image that is compatible with your hardware */
  def createCompatibleImage(width: Int, height: Int): BufferedImage = {
    val local = GraphicsEnvironment.getLocalGraphicsEnvironment
    val screen = local.getDefaultScreenDevice
    val configuration = screen.getDefaultConfiguration
    configuration.createCompatibleImage(width, height)
  }

  /**
    * return a byte array given an image
    * @param img  the image to convert
    * @param imageTypee the type of image to create ("jpg" or "png")
    */
  def getImageAsByteArray(img: Image, imageTypee: ImageType): Array[Byte] = {
    val bos = new ByteArrayOutputStream
    val os = new BufferedOutputStream(bos)
    writeImage(img, os, imageTypee)
    bos.toByteArray
  }

  /**
    * write an image to the given output stream
    *
    * @param img  image to write.
    * @param out  output stream to write to
    * @param imageType the type of image to create ("jpg" or "png")
    */
  def writeImage(img: Image, out: OutputStream, imageType: ImageType): Unit = {
    val bi = makeBufferedImage(img)
    if (imageType eq ImageType.JPG) {
      val encoder = ImageIO.getImageWritersByFormatName("JPEG").next
      //NON-NLS
      val param = new JPEGImageWriteParam(null)
      param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT)
      encoder.setOutput(out)
      try
        encoder.write(null, new IIOImage(img.asInstanceOf[RenderedImage], null, null), param)
      catch {
        case fne: IOException =>
          throw new IllegalStateException("IOException error:" + fne.getMessage, fne)
      }
    }
    else { // PNG is the default
      //PNGEncodeParam param = PNGEncodeParam.getDefaultEncodeParam( bi );
      //ImageEncoder encoder = ImageCodec.createImageEncoder( "PNG", out, param ); //NON-NLS
      try // Writes it to a file as a .png
      ImageIO.write(bi, "png", out)
      //encoder.encode( bi );
      catch {
        case e: IOException =>
          throw new IllegalStateException("IOException error.", e)
        case npe: NullPointerException =>
          throw new IllegalStateException("Could not encode buffered image because it was null.", npe)
      }
    }
    try {
      out.flush()
      out.close()
    } catch {
      case e: IOException =>
        throw new IllegalStateException("IOException error.", e)
    }
  }

  /**
    * Saves an image to a file using the format specified by the type.
    * Note the filename should not include the extension. This will be added as appropriate.
    * @param fileName the fileName should not have an extension because it gets added based on VizContext.imageFormat
    * @param img      the image to save
    * @param imageType of image ("jpg" or "png" (default))
    */
  def saveAsImage(fileName: String, img: Image, imageType: ImageType): Unit = {
    var os: OutputStream = null
    try {
      val extension = '.' + imageType.toString.toLowerCase
      var fn = fileName
      if (!fn.endsWith(extension)) { // if it does not already have the appropriate extension add it.
        fn += extension
      }
      os = new BufferedOutputStream(new FileOutputStream(fn))
    } catch {
      case fne: FileNotFoundException =>
        System.out.println("File " + fileName + " not found: " + fne.getMessage) //NON-NLS

    }
    writeImage(img, os, imageType)
  }

  /**
    * @param pixels one dimension array of pixels where a pixel at x and y can be located with
    *               3 *(x * height + y )
    *               Note that there are 4 integers for every pixel (rgb)
    * @param width image width
    * @param height image height
    * @return image from the pixel data
    */
  def getImageFromPixelArray(pixels: Array[Int], width: Int, height: Int): Image = {
    val image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
    image.setRGB(0, 0, width, height, pixels, 0, width)
    image
  }

  /**
    * Interpolate among 4 colors (corresponding to the 4 points on a square)
    * @return The interpolated color.
    */
  def interpolate(x: Double, y: Double,
                  colorLL: Array[Float], colorLR: Array[Float], colorUL: Array[Float], colorUR: Array[Float]): Color = {
    val rgbaL = new Array[Float](4)
    val rgbaU = new Array[Float](4)
    rgbaL(0) = (colorLL(0) + x * (colorLR(0) - colorLL(0))).toFloat
    rgbaL(1) = (colorLL(1) + x * (colorLR(1) - colorLL(1))).toFloat
    rgbaL(2) = (colorLL(2) + x * (colorLR(2) - colorLL(2))).toFloat
    rgbaL(3) = (colorLL(3) + x * (colorLR(3) - colorLL(3))).toFloat
    rgbaU(0) = (colorUL(0) + x * (colorUR(0) - colorUL(0))).toFloat
    rgbaU(1) = (colorUL(1) + x * (colorUR(1) - colorUL(1))).toFloat
    rgbaU(2) = (colorUL(2) + x * (colorUR(2) - colorUL(2))).toFloat
    rgbaU(3) = (colorUL(3) + x * (colorUR(3) - colorUL(3))).toFloat
    new Color((rgbaL(0) + y * (rgbaU(0) - rgbaL(0))).toFloat,
              (rgbaL(1) + y * (rgbaU(1) - rgbaL(1))).toFloat,
              (rgbaL(2) + y * (rgbaU(2) - rgbaL(2))).toFloat,
              (rgbaL(3) + y * (rgbaU(3) - rgbaL(3))).toFloat)
  }
}