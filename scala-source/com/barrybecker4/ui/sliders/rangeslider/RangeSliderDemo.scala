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

  private val rangeSliderLabel1 = new JLabel
  rangeSliderLabel1.setHorizontalAlignment(SwingConstants.LEFT)
  private val rangeSlider1: RangeSlider = RangeSlider(0, 20)
  rangeSlider1.setOrientation(HORIZONTAL)
  rangeSlider1.setPreferredSize(new Dimension(340, rangeSlider1.getPreferredSize.height))

  rangeSlider1.addChangeListener(new ChangeListener() {
    override def stateChanged(e: ChangeEvent): Unit = {
      val slider = e.getSource.asInstanceOf[RangeSlider]
      updatelabel1(slider)
    }
  })

  private val rangeSliderLabel2 = new JLabel
  rangeSliderLabel2.setHorizontalAlignment(SwingConstants.LEFT)
  private val rangeSlider2: RangeSlider = RangeSlider(5, 30)
  rangeSlider2.setOrientation(VERTICAL)
  rangeSlider2.setPreferredSize(new Dimension(rangeSlider2.getPreferredSize.width, 340))

  rangeSlider2.addChangeListener(new ChangeListener() {
    override def stateChanged(e: ChangeEvent): Unit = {
      val slider = e.getSource.asInstanceOf[RangeSlider]
      updatelabel2(slider)
    }
  })

  add(rangeSliderLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
        GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 3, 3), 0, 0))
  add(rangeSlider1, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0,
        GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0))

  add(rangeSliderLabel2, new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0,
    GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 3, 3), 0, 0))
  add(rangeSlider2, new GridBagConstraints(0, 3, 2, 1, 0.0, 0.0,
    GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0))


  def display(): Unit = { // Initialize values.
    rangeSlider1.setValue(3)
    rangeSlider1.setUpperValue(15)

    rangeSlider2.setValue(8)
    rangeSlider2.setUpperValue(20)

    updatelabel1(rangeSlider1)
    updatelabel2(rangeSlider2)

    val frame = new JFrame
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
    frame.setResizable(false)
    frame.setTitle("Range Slider Demo")

    frame.getContentPane.setLayout(new BorderLayout)
    frame.getContentPane.add(this, BorderLayout.CENTER)
    frame.pack()

    frame.setLocationRelativeTo(null)
    frame.setVisible(true)
  }

  private def updatelabel1(slider: RangeSlider): Unit = {
    rangeSliderLabel1.setText(s"HORIZONTAL - Lower value: ${slider.getValue} Upper value: ${slider.getUpperValue}")
  }

  private def updatelabel2(slider: RangeSlider): Unit = {
    rangeSliderLabel2.setText(s"VERTICAL - Lower value: ${slider.getValue} Upper value: ${slider.getUpperValue}")
  }
}