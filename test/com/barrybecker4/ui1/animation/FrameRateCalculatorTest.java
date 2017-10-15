// Copyright by Barry G. Becker, 2013. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui1.animation;

import com.barrybecker4.common.concurrency.ThreadUtil;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Barry Becker
 */
public class FrameRateCalculatorTest {

    /** instance under test. */
    private FrameRateCalculator calculator;


    @Before
    public void setUp() {
        calculator = new FrameRateCalculator();
    }


    /** The frame rate given on the first frame is 0 because no frame rendered yet. */
    @Test
    public void testDefaultFrameRate() {

        assertEquals("Unexpected initial frame count", 0, calculator.getFrameCount());
        assertEquals("Unexpected initial frame rate", 0.0, calculator.getFrameRate(), 0.001);
    }

    @Test
    public void testFrameCount() {
        calculator.incrementFrameCount();
        calculator.incrementFrameCount();
        assertEquals("Unexpected count", 2, calculator.getFrameCount());
    }

    @Test
    public void testFrameRateAfter1() {
        ThreadUtil.sleep(100);
        calculator.incrementFrameCount();
        assertEquals("Unexpected initial frame rate.", 9.0, calculator.getFrameRate(), 1.5);
    }

    @Test
    public void testInitialFrameRateWithDelay() {
        ThreadUtil.sleep(100);
        assertEquals("Unexpected initial frame rate.", 0.0, calculator.getFrameRate(), 1.0);
    }

    @Test
    public void testFrameRateAfter1WithDelay() {
        calculator.incrementFrameCount();
        ThreadUtil.sleep(100);
        assertEquals("Unexpected initial frame rate.", 9.7, calculator.getFrameRate(), 1.2);
    }

    @Test
    public void testFrameRateAfter3() {
        verifyFrameRateAfterN(3, 0, 0.0, 3000);
    }

    @Test
    public void testFrameRateAfter3WithDelayForAllButLast() {
        calculator.incrementFrameCount();
        ThreadUtil.sleep(50);
        calculator.incrementFrameCount();
        ThreadUtil.sleep(50);
        calculator.incrementFrameCount();
        assertEquals("Unexpected frame rate.", 29.0, calculator.getFrameRate(), 1.5);
    }

    @Test
    public void testFrameRateAWithPause() {
        calculator.incrementFrameCount();
        ThreadUtil.sleep(50);
        calculator.incrementFrameCount();
        ThreadUtil.sleep(50);

        calculator.setPaused(true);
        ThreadUtil.sleep(200);
        calculator.setPaused(false);

        calculator.incrementFrameCount();
        assertEquals("Unexpected frame rate.", 30.0, calculator.getFrameRate(), 1.5);
    }

    @Test
    public void testFrameRateAWithTwoPauses() {
        calculator.incrementFrameCount();
        calculator.setPaused(false);
        ThreadUtil.sleep(50);

        calculator.setPaused(true);
        calculator.setPaused(true);
        ThreadUtil.sleep(200);
        calculator.setPaused(false);

        calculator.incrementFrameCount();
        ThreadUtil.sleep(50);

        calculator.setPaused(false);
        calculator.setPaused(true);
        ThreadUtil.sleep(200);
        calculator.setPaused(false);

        calculator.incrementFrameCount();
        assertEquals("Unexpected frame rate.", 29.0, calculator.getFrameRate(), 1.2);
    }

    @Test
    public void testFrameRateAfter3WithDelayOf50() {
        verifyFrameRateAfterN(3, 50, 19.5);
    }

    @Test
    public void testFrameRateAfter3WithDelayOf100() {
        verifyFrameRateAfterN(3, 100, 9.8);
    }

    @Test
    public void testFrameRateAfter6WithDelayOf100() {
        verifyFrameRateAfterN(6, 100, 9.5);
    }

    @Test
    public void testFrameRateAfter12WithDelayOf100() {
        verifyFrameRateAfterN(12, 100, 9.0);
    }

    @Test
    public void testFrameRateAfter24WithDelayOf100() {
        verifyFrameRateAfterN(24, 100, 9.9);
    }

    /*
    @Test
    public void testFrameRateAfter48WithDelayOf100() {
        verifyFrameRateAfterN(48, 100, 10.0);
    }
    @Test
    public void testFrameRateAfter96WithDelayOf100() {
        verifyFrameRateAfterN(96, 100, 10.0);
    }
    @Test
    public void testFrameRateAfter3WithDelayOf200() {
        verifyFrameRateAfterN(3, 200, 5.0);
    }  */

    @Test
    public void testFrameRateAfterFilled() {
        verifyFrameRateAfterN(100, 0, Double.POSITIVE_INFINITY);
    }

    @Test
    public void testFrameRateWithDelayAfterFilled100() {
        verifyFrameRateAfterN(100, 10, 95.0, 11.0);
    }

    /*
    @Test
    public void testFrameRateWithDelayAfterFilled200() {
        verifyFrameRateAfterN(200, 10, 100.0);
    } */

    private void verifyFrameRateAfterN(int numFrames, int frameDelay, double expRate) {
         verifyFrameRateAfterN(numFrames, frameDelay, expRate, 1.2);
    }

    /**
     *
     * @param numFrames  number of frames to simulate
     * @param frameDelay artificial delay in milliseconds to create each frame
     * @param expRate expected frame rate.
     * @param tolerance tolerance threshold for expected result.
     */
    private void verifyFrameRateAfterN(int numFrames, int frameDelay, double expRate, double tolerance) {
        for (int i=0; i<numFrames; i++)  {
            calculator.incrementFrameCount();
            ThreadUtil.sleep(frameDelay);
        }
        assertEquals("Unexpected frameRate", expRate, calculator.getFrameRate(), tolerance);
    }

}
