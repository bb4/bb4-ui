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
    RangeSliderDemo().display()
  }
}

class RangeSliderDemo() extends JPanel {
  setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6))
  setLayout(new GridBagLayout)

  private val rangeSliderLabel = new JLabel
  private val rangeSlider1: RangeSlider = RangeSlider(0, 20)
  rangeSlider1.setOrientation(HORIZONTAL)

  rangeSliderLabel.setText("Lower value:  Upper value: ")
  rangeSliderLabel.setHorizontalAlignment(SwingConstants.LEFT)
  rangeSlider1.setPreferredSize(new Dimension(340, rangeSlider1.getPreferredSize.height))

  // Add listener to update display.
  rangeSlider1.addChangeListener(new ChangeListener() {
    override def stateChanged(e: ChangeEvent): Unit = {
      val slider = e.getSource.asInstanceOf[RangeSlider]
      updatelabel(slider)
    }
  })

  add(rangeSliderLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
        GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 3, 3), 0, 0))
  add(rangeSlider1, new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0,
        GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0))


  def display(): Unit = { // Initialize values.
    rangeSlider1.setValue(3)
    rangeSlider1.setUpperValue(15)
    // Initialize value display.
    updatelabel(rangeSlider1)
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

  private def updatelabel(slider: RangeSlider): Unit = {
    rangeSliderLabel.setText(s"Lower value: ${slider.getValue} Upper value: ${slider.getUpperValue}")
  }
}