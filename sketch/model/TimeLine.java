package sketch.model;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

import sketch.common.*;
import sketch.controller.*;

public class TimeLine {

    // --- Static Members --- //

    // Frame Length //
    private static int frameLength_ = 1;

    public static int  getFrameLength ()      { return frameLength_; }
    public static void setFrameLength (int l) { frameLength_ = l; }
    public static void incFrameLength ()      { ++frameLength_; }
    public static void decFrameLength ()      { --frameLength_; }

    // Frame Index //
    private static int frameIndex_ = 0;

    public static int  getFrameIndex ()      { return frameIndex_; }
    public static void setFrameIndex (int i) { frameIndex_ = i; }
    public static void incFrameIndex ()      { ++frameIndex_; }
    public static void decFrameIndex ()      { --frameIndex_; }

    // Routine Opertaions //
    // => find out if we're at the last available frame index
    public static boolean isLatest () {
        return frameIndex_ + 1 == frameLength_;
    }

    // --- Non-static Members --- //

    // AffineTransform HashMap //
    private HashMap transforms_ = new HashMap();

    public AffineTransform getTransform (int k) {
        String key = Integer.toString(k);
        return (AffineTransform)transforms_.get(key);
    }

    public void setTransform (int k, AffineTransform tran) {
        String key = Integer.toString(k);
        transforms_.put(key, tran);
    }

    // Start and End Index //
    private int startIndex_ = 0;
    private int endIndex_   = 0;

    public int getStartIndex () { return startIndex_; }
    public int getEndIndex   () { return endIndex_;   }

    public void setStartIndex (int s) { startIndex_ = s; }
    public void setEndIndex   (int e) { endIndex_   = e; }

    // Routine Operations //
    // => find out if frame index is within this timeslice
    public boolean isVisible () {
        if (endIndex_ != 0) {
            return frameIndex_ >= startIndex_ &&
                   frameIndex_ <  endIndex_;
        }
        else {
            return frameIndex_ >= startIndex_;
        }
    }

    // => return the current transform
    // => if not exist, create a new one
    public AffineTransform getCurrentTransform () {
        String key = Integer.toString(frameIndex_);
        AffineTransform tran = (AffineTransform)transforms_.get(key);

        if (tran == null) {
            tran = new AffineTransform();
            transforms_.put(key, tran);
        }

        return tran;
    }

    // => set the current frame's transform
    public void setCurrentTransform (AffineTransform transform) {
        String key = Integer.toString(frameIndex_);
        transforms_.put(key, transform);
    }

    // => insert a copy of current frames before next frame
    // => shift the entire map by one from current frame index
    public void insertCurrentFrame () {
        for (int i = frameLength_; i > frameIndex_; --i) {
            String key = Integer.toString(i - 1);
            AffineTransform copy
                = (AffineTransform)
                ((AffineTransform)transforms_.get(key)).clone();

            transforms_.put(Integer.toString(i), copy);
        }
    }

}
