package com.example.voldemars.translation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

/* http://developer.android.com/training/basics/network-ops/xml.html */
public class WordListXmlReader extends WordListReader {
	private static final String ns = null;

	private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
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
	
	private String readType(XmlPullParser parser) throws XmlPullParserException, IOException {
		String type = "";
		int count = parser.getAttributeCount();
		int i;
		
		for (i = 0; i < count; i++) {
			String attr = parser.getAttributeName(i);
			if (attr.equals("type")) {
				type = parser.getAttributeValue(i);
				break;
			}
		}
			
		return type;
	}
	
	private String readLang(XmlPullParser parser, String lang) throws IOException, XmlPullParserException {
		String txt = "";
	    parser.require(XmlPullParser.START_TAG, ns, lang);
	    if (parser.next() == XmlPullParser.TEXT) {
	        txt = parser.getText();
	        parser.nextTag();
	    }
	    parser.require(XmlPullParser.END_TAG, ns, lang);
	    return txt;
	}

	private Word readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
	    parser.require(XmlPullParser.START_TAG, ns, "translation");
	    String src = null;
	    String dst = null;
	    String hint = "";
	    String type = readType(parser);

	    while (parser.next() != XmlPullParser.END_TAG) {
	        if (parser.getEventType() != XmlPullParser.START_TAG) {
	            continue;
	        }
	        String name = parser.getName();
	        if (name.equals("src")) {
	            src = readLang(parser, "src");
	        } else if (name.equals("dst")) {
	        	dst = readLang(parser, "dst");
	        } else if (name.equals("hint")) {
	        	hint = readLang(parser, "hint");
	        } else {
	            skip(parser);
	        }
	    }
	    Debug.out(hint);
	    return new Word(src, null, null, dst, type, hint);
	}

	private void readFile(WordList list, XmlPullParser parser) throws XmlPullParserException, IOException {
	    parser.require(XmlPullParser.START_TAG, ns, "feed");
	    while (parser.next() != XmlPullParser.END_TAG) {
	        if (parser.getEventType() != XmlPullParser.START_TAG) {
	            continue;
	        }
	        String name = parser.getName();
	        // Starts by looking for the entry tag
	        if (name.equals("translation")) {
	            list.add(readEntry(parser));
	        } else {
	            skip(parser);
	        }
	    }
	}
	
	@Override
	public void getWordList(WordList list, File file) {
		try {
			FileInputStream in = new FileInputStream(file);
			XmlPullParser parser = Xml.newPullParser();
			try {
				parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
		        parser.setInput(in, null);
		        parser.nextTag();
				readFile(list, parser);
			} catch (XmlPullParserException e) {
				// TODO Bloc catch généré automatiquement
				e.printStackTrace();
			}
			in.close();
		} catch (FileNotFoundException e) {
			// TODO Bloc catch généré automatiquement
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Bloc catch généré automatiquement
			e.printStackTrace();
		}
	}
}
