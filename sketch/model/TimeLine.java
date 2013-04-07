package sketch.model;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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

    // --- Serialization --- //
    
    // -> serliaze all data
    public void serialize (Element root, Document doc) {
        Element tag = doc.createElement("timeline");
        root.appendChild(tag);

        Element startTag = doc.createElement("start");
        startTag.appendChild(doc.createTextNode(Integer.toString(startIndex_)));
        tag.appendChild(startTag);

        Element endTag = doc.createElement("end");
        endTag.appendChild(doc.createTextNode(Integer.toString(endIndex_)));
        tag.appendChild(endTag);

        Set entries = transforms_.entrySet();
        for (Object obj : entries) {
            Map.Entry entry = (Map.Entry)obj;
            
            Element entryTag = doc.createElement("entry");
            serializeEntry(entry, entryTag, doc);

            tag.appendChild(entryTag);
        }
    }

    // -> serialize <String, AffineTransform>
    public void serializeEntry (Map.Entry entry, Element tag, Document doc) {
        // serialize key
        String key = (String)entry.getKey();

        Element keyTag = doc.createElement("key");
        keyTag.appendChild(doc.createTextNode(key));

        tag.appendChild(keyTag);

        // serialize value
        AffineTransform tran = (AffineTransform)entry.getValue();

        Element valTag = doc.createElement("value");
        serializeTransform(tran, valTag, doc);

        tag.appendChild(valTag);
    }

    // -> serialize AffineTransform
    public void serializeTransform (AffineTransform tran,
                                    Element         tag,
                                    Document        doc) {
        double[] flatmatrix = new double[6];
        tran.getMatrix(flatmatrix);

        // m00
        Element m00Tag = doc.createElement("m00");
        m00Tag.appendChild(doc.createTextNode(Double.toString(flatmatrix[0])));
        tag.appendChild(m00Tag);

        // m10
        Element m10Tag = doc.createElement("m10");
        m10Tag.appendChild(doc.createTextNode(Double.toString(flatmatrix[1])));
        tag.appendChild(m10Tag);

        // m01
        Element m01Tag = doc.createElement("m01");
        m01Tag.appendChild(doc.createTextNode(Double.toString(flatmatrix[2])));
        tag.appendChild(m01Tag);

        // m11
        Element m11Tag = doc.createElement("m11");
        m11Tag.appendChild(doc.createTextNode(Double.toString(flatmatrix[3])));
        tag.appendChild(m11Tag);

        // m02
        Element m02Tag = doc.createElement("m02");
        m02Tag.appendChild(doc.createTextNode(Double.toString(flatmatrix[4])));
        tag.appendChild(m02Tag);

        // m12
        Element m12Tag = doc.createElement("m12");
        m12Tag.appendChild(doc.createTextNode(Double.toString(flatmatrix[5])));
        tag.appendChild(m12Tag);
    }

    // -> deserialize all data
    public void deserialize (Element element) {
        // extrat start index
        Element startTag =
            (Element)element.getElementsByTagName("start").item(0);
        startIndex_ = extractInt(startTag);

        // extrat end index
        Element endTag =
            (Element)element.getElementsByTagName("end").item(0);
        endIndex_ = extractInt(endTag);

        NodeList list = element.getElementsByTagName("entry");
        for (int i = 0; i < list.getLength(); ++i) {
            deserializeEntry((Element)list.item(i));
        }
    }

    // -> deserialize <String, AffineTransform>
    public void deserializeEntry (Element element) {
        Element keyTag =
            (Element)element.getElementsByTagName("key").item(0);

        String key = keyTag.getChildNodes().item(0).getNodeValue();

        AffineTransform val =
            deserializeTransform((Element)
                    element.getElementsByTagName("value").item(0));

        transforms_.put(key, val);
    }

    // -> deserialize AffineTransform
    public AffineTransform deserializeTransform (Element element) {
        Element m00Tag = (Element)element.getElementsByTagName("m00").item(0);
        Element m10Tag = (Element)element.getElementsByTagName("m10").item(0);
        Element m01Tag = (Element)element.getElementsByTagName("m01").item(0);
        Element m11Tag = (Element)element.getElementsByTagName("m11").item(0);
        Element m02Tag = (Element)element.getElementsByTagName("m02").item(0);
        Element m12Tag = (Element)element.getElementsByTagName("m12").item(0);

        AffineTransform tran = new AffineTransform(
                extractDouble(m00Tag),
                extractDouble(m10Tag),
                extractDouble(m01Tag),
                extractDouble(m11Tag),
                extractDouble(m02Tag),
                extractDouble(m12Tag)
                );

        return tran;
    }

    // -> extract double out of an element
    public double extractDouble (Element element) {
        return Double.parseDouble(element.getChildNodes().item(0).getNodeValue());
    }

    // -> extract int out of an element
    public int extractInt (Element element) {
        return Integer.parseInt(element.getChildNodes().item(0).getNodeValue());
    }

}
