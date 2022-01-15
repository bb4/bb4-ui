package com.barrybecker4.ui.sliders.rangeslider

import com.barrybecker4.ui.sliders.rangeslider.RangeSlider

import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Insets
import javax.swing.{BorderFactory, JFrame, JLabel, JPanel, SwingConstants, SwingUtilities, UIManager, WindowConstants}
import javax.swing.event.ChangeEvent
import javax.swing.event.ChangeListener
import SwingConstants.*


/**
  * Demo application panel to display a range slider.
  */
object RangeSliderDemo {

  def main(args: Array[String]): Unit = {
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName)
    RangeSliderDemo(VERTICAL).display()
  }
}

class RangeSliderDemo(orientation: Int) extends JPanel {
  setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6))
  setLayout(new GridBagLayout)

  private val rangeSliderLabel1 = new JLabel
  private val rangeSliderValue1 = new JLabel
  private val rangeSliderLabel2 = new JLabel
  private val rangeSliderValue2 = new JLabel
  private val rangeSlider: RangeSlider = RangeSlider(0, 20)
  rangeSlider.setOrientation(orientation)

  rangeSliderLabel1.setText("Lower value:")
  rangeSliderLabel2.setText("Upper value:")
  rangeSliderValue1.setHorizontalAlignment(SwingConstants.LEFT)
  rangeSliderValue2.setHorizontalAlignment(SwingConstants.LEFT)
  rangeSlider.setPreferredSize(new Dimension(340, rangeSlider.getPreferredSize.height))

  // Add listener to update display.
  rangeSlider.addChangeListener(new ChangeListener() {
    override def stateChanged(e: ChangeEvent): Unit = {
      val slider = e.getSource.asInstanceOf[RangeSlider]
      rangeSliderValue1.setText(String.valueOf(slider.getValue))
      rangeSliderValue2.setText(String.valueOf(slider.getUpperValue))
    }
  })

  add(rangeSliderLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 3, 3), 0, 0))
  add(rangeSliderValue1, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 3, 0), 0, 0))
  add(rangeSliderLabel2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 3, 3), 0, 0))
  add(rangeSliderValue2, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 6, 0), 0, 0))
  add(rangeSlider, new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0))


  def display(): Unit = { // Initialize values.
    rangeSlider.setValue(3)
    rangeSlider.setUpperValue(16)
    // Initialize value display.
    rangeSliderValue1.setText(String.valueOf(rangeSlider.getValue))
    rangeSliderValue2.setText(String.valueOf(rangeSlider.getUpperValue))
    // Create window frame.
    val frame = new JFrame
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
    frame.setResizable(false)
    frame.setTitle("Range Slider Demo")
    // Set window content and validate.
    frame.getContentPane.setLayout(new BorderLayout)
    frame.getContentPane.add(this, BorderLayout.CENTER)
    frame.pack()
    // Set window location and display.
    frame.setLocationRelativeTo(null)
    frame.setVisible(true)
  }
}