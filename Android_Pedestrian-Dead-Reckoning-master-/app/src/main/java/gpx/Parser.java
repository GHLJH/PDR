package gpx;

import android.util.Xml;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;


public class Parser {

        // We don't use namespaces
        private static final String ns = null;

        public static GPX parse(InputStream in) throws XmlPullParserException, IOException {
            try {
                XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(in, null);
                parser.nextTag();
                return readGPX(parser);
            } finally {
                in.close();
            }
        }



        // Processes GPX tags.
        private static GPX readGPX(XmlPullParser parser) throws XmlPullParserException, IOException {
            GPX entries = new GPX();

            parser.require(XmlPullParser.START_TAG, ns, "gpx");
            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name = parser.getName();
                // Starts by looking for the trk tag
                if (name.equals("trk")) {
                    entries.addTrack(readTrack(parser));
                } else {
                    skip(parser);
                }
            }
            return entries;
        }



        // Processes Track tags.
        private static Track readTrack(XmlPullParser parser) throws XmlPullParserException, IOException {
            Track entries = new Track();

            parser.require(XmlPullParser.START_TAG, ns, "trk");
            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name = parser.getName();
                // Starts by looking for the trk tag
                if (name.equals("trkseg")) {
                    entries.addTrackSeg(readTrackSeg(parser));

                } else {
                    skip(parser);
                }
            }
            return entries;
        }


        // Processes TrackSeg tags.
        private static TrackSeg readTrackSeg(XmlPullParser parser) throws XmlPullParserException, IOException {
            TrackSeg entries = new TrackSeg();

            parser.require(XmlPullParser.START_TAG, ns, "trkseg");
            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name = parser.getName();
                // Starts by looking for the trkpt tag
                if (name.equals("trkpt")) {
                    entries.addTrackPoint(readTrackPoint(parser));
                    skip(parser);
                } else {
                    skip(parser);
                }
            }
            return entries;
        }




        // Processes TrackPoint tags.
        private static TrackPoint readTrackPoint(XmlPullParser parser) throws XmlPullParserException, IOException {
            TrackPoint entries;
            double lat = 0;
            double lon = 0;

            parser.require(XmlPullParser.START_TAG, ns, "trkpt");

            String name = parser.getName();
            // Starts by looking for the trkseg tag
            if (name.equals("trkpt")) {
                lat =  Double.parseDouble(parser.getAttributeValue(null, "lat"));
                lon =  Double.parseDouble(parser.getAttributeValue(null, "lon"));
                //System.out.println("Lat : " + lat +  " Lon : " + lon );
            }
            else {
                skip(parser);
            }
            entries = new TrackPoint(lat, lon);
            return entries;
        }

        //Skip
        private static void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                throw new IllegalStateException();
            }
            int depth = 1;
            while (depth != 0) {
                switch (parser.next()) {
                    case XmlPullParser.END_TAG:
                        depth--;
                        break;
                    case XmlPullParser.START_TAG:
                        depth++;
                        break;
                }
            }

        }

    }