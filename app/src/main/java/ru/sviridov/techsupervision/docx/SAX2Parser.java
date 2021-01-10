package ru.sviridov.techsupervision.docx;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

/* renamed from: ru.sviridov.techsupervision.docx.SAX2Parser */
public class SAX2Parser implements Locator, XMLReader, Attributes {
   protected static final String APACHE_DYNAMIC_VALIDATION_FEATURE = "http://apache.org/xml/features/validation/dynamic";
   protected static final String APACHE_SCHEMA_VALIDATION_FEATURE = "http://apache.org/xml/features/validation/schema";
   protected static final String DECLARATION_HANDLER_PROPERTY = "http://xml.org/sax/properties/declaration-handler";
   protected static final String LEXICAL_HANDLER_PROPERTY = "http://xml.org/sax/properties/lexical-handler";
   protected static final String NAMESPACES_FEATURE = "http://xml.org/sax/features/namespaces";
   protected static final String NAMESPACE_PREFIXES_FEATURE = "http://xml.org/sax/features/namespace-prefixes";
   protected static final String VALIDATION_FEATURE = "http://xml.org/sax/features/validation";
   protected ContentHandler contentHandler = new DefaultHandler();
   protected ErrorHandler errorHandler = new DefaultHandler();

   /* renamed from: pp */
   protected XmlPullParser f77pp;
   protected String systemId;

   public SAX2Parser() throws XmlPullParserException {
      XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
      factory.setNamespaceAware(true);
      this.f77pp = factory.newPullParser();
   }

   public SAX2Parser(XmlPullParser pp) throws XmlPullParserException {
      this.f77pp = pp;
   }

   public int getLength() {
      return this.f77pp.getAttributeCount();
   }

   public String getURI(int index) {
      return this.f77pp.getAttributeNamespace(index);
   }

   public String getLocalName(int index) {
      return this.f77pp.getAttributeName(index);
   }

   public String getQName(int index) {
      String prefix = this.f77pp.getAttributePrefix(index);
      if (prefix != null) {
         return prefix + ':' + this.f77pp.getAttributeName(index);
      }
      return this.f77pp.getAttributeName(index);
   }

   public String getType(int index) {
      return this.f77pp.getAttributeType(index);
   }

   public String getValue(int index) {
      return this.f77pp.getAttributeValue(index);
   }

   public int getIndex(String uri, String localName) {
      for (int i = 0; i < this.f77pp.getAttributeCount(); i++) {
         if (this.f77pp.getAttributeNamespace(i).equals(uri) && this.f77pp.getAttributeName(i).equals(localName)) {
            return i;
         }
      }
      return -1;
   }

   public int getIndex(String qName) {
      for (int i = 0; i < this.f77pp.getAttributeCount(); i++) {
         if (this.f77pp.getAttributeName(i).equals(qName)) {
            return i;
         }
      }
      return -1;
   }

   public String getType(String uri, String localName) {
      for (int i = 0; i < this.f77pp.getAttributeCount(); i++) {
         if (this.f77pp.getAttributeNamespace(i).equals(uri) && this.f77pp.getAttributeName(i).equals(localName)) {
            return this.f77pp.getAttributeType(i);
         }
      }
      return null;
   }

   public String getType(String qName) {
      for (int i = 0; i < this.f77pp.getAttributeCount(); i++) {
         if (this.f77pp.getAttributeName(i).equals(qName)) {
            return this.f77pp.getAttributeType(i);
         }
      }
      return null;
   }

   public String getValue(String uri, String localName) {
      return this.f77pp.getAttributeValue(uri, localName);
   }

   public String getValue(String qName) {
      return this.f77pp.getAttributeValue((String) null, qName);
   }

   public String getPublicId() {
      return null;
   }

   public String getSystemId() {
      return this.systemId;
   }

   public int getLineNumber() {
      return this.f77pp.getLineNumber();
   }

   public int getColumnNumber() {
      return this.f77pp.getColumnNumber();
   }

   public boolean getFeature(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
      if (NAMESPACES_FEATURE.equals(name)) {
         return this.f77pp.getFeature("http://xmlpull.org/v1/doc/features.html#process-namespaces");
      }
      if (NAMESPACE_PREFIXES_FEATURE.equals(name)) {
         return this.f77pp.getFeature("http://xmlpull.org/v1/doc/features.html#report-namespace-prefixes");
      }
      if (VALIDATION_FEATURE.equals(name)) {
         return this.f77pp.getFeature("http://xmlpull.org/v1/doc/features.html#validation");
      }
      return this.f77pp.getFeature(name);
   }

   public void setFeature(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException {
      try {
         if (NAMESPACES_FEATURE.equals(name)) {
            this.f77pp.setFeature("http://xmlpull.org/v1/doc/features.html#process-namespaces", value);
         } else if (NAMESPACE_PREFIXES_FEATURE.equals(name)) {
            if (this.f77pp.getFeature("http://xmlpull.org/v1/doc/features.html#report-namespace-prefixes") != value) {
               this.f77pp.setFeature("http://xmlpull.org/v1/doc/features.html#report-namespace-prefixes", value);
            }
         } else if (VALIDATION_FEATURE.equals(name)) {
            this.f77pp.setFeature("http://xmlpull.org/v1/doc/features.html#validation", value);
         } else {
            this.f77pp.setFeature(name, value);
         }
      } catch (XmlPullParserException ex) {
      //   Mint.logException(ex);
      }
   }

   public Object getProperty(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
      if (!DECLARATION_HANDLER_PROPERTY.equals(name) && !LEXICAL_HANDLER_PROPERTY.equals(name)) {
         return this.f77pp.getProperty(name);
      }
      return null;
   }

   public void setProperty(String name, Object value) throws SAXNotRecognizedException, SAXNotSupportedException {
      if (DECLARATION_HANDLER_PROPERTY.equals(name)) {
         throw new SAXNotSupportedException("not supported setting property " + name);
      } else if (LEXICAL_HANDLER_PROPERTY.equals(name)) {
         throw new SAXNotSupportedException("not supported setting property " + name);
      } else {
         try {
            this.f77pp.setProperty(name, value);
         } catch (XmlPullParserException ex) {
         //   Mint.logException(ex);
            throw new SAXNotSupportedException("not supported set property " + name + ": " + ex);
         }
      }
   }

   public EntityResolver getEntityResolver() {
      return null;
   }

   public void setEntityResolver(EntityResolver resolver) {
   }

   public DTDHandler getDTDHandler() {
      return null;
   }

   public void setDTDHandler(DTDHandler handler) {
   }

   public ContentHandler getContentHandler() {
      return this.contentHandler;
   }

   public void setContentHandler(ContentHandler handler) {
      this.contentHandler = handler;
   }

   public ErrorHandler getErrorHandler() {
      return this.errorHandler;
   }

   public void setErrorHandler(ErrorHandler handler) {
      this.errorHandler = handler;
   }

   public void parse(InputSource source) throws SAXException, IOException {
      this.systemId = source.getSystemId();
      this.contentHandler.setDocumentLocator(this);
      Reader reader = source.getCharacterStream();
      if (reader == null) {
         try {
            InputStream stream = source.getByteStream();
            String encoding = source.getEncoding();
            if (stream == null) {
               this.systemId = source.getSystemId();
               if (this.systemId == null) {
                  this.errorHandler.fatalError(new SAXParseException("null source systemId", this));
                  return;
               }
               try {
                  stream = new URL(this.systemId).openStream();
               } catch (MalformedURLException nue) {
               //   Mint.logException(nue);
                  try {
                     stream = new FileInputStream(this.systemId);
                  } catch (FileNotFoundException fnfe) {
                     //Mint.logException(fnfe);
                     this.errorHandler.fatalError(new SAXParseException("could not open file with systemId " + this.systemId, this, fnfe));
                     return;
                  }
               }
            }
            this.f77pp.setInput(stream, encoding);
         } catch (XmlPullParserException ex) {
          //  Mint.logException(ex);
            this.errorHandler.fatalError(new SAXParseException("parsing initialization error: " + ex, this, ex));
            return;
         }
      } else {
         try {
            this.f77pp.setInput(reader);
         } catch (XmlPullParserException e) {
            e.printStackTrace();
         }
      }
      try {
         this.contentHandler.startDocument();
         this.f77pp.next();
         if (this.f77pp.getEventType() != 2) {
            this.errorHandler.fatalError(new SAXParseException("expected start tag not" + this.f77pp.getPositionDescription(), this));
            return;
         }
         parseSubTree(this.f77pp);
         this.contentHandler.endDocument();
      } catch (XmlPullParserException ex2) {
         //Mint.logException(ex2);
         this.errorHandler.fatalError(new SAXParseException("parsing initialization error: " + ex2, this, ex2));
      }
   }

   public void parse(String systemId2) throws SAXException, IOException {
      parse(new InputSource(systemId2));
   }

   public void parseSubTree(XmlPullParser pp) throws SAXException, IOException {
      this.f77pp = pp;
      boolean namespaceAware = pp.getFeature("http://xmlpull.org/v1/doc/features.html#process-namespaces");
      try {
         if (pp.getEventType() != 2) {
            throw new SAXException("start tag must be read before skiping subtree" + pp.getPositionDescription());
         }
         int[] holderForStartAndLength = new int[2];
         StringBuilder rawName = new StringBuilder(16);
         int level = pp.getDepth() - 1;
         int type = 2;
         do {
            switch (type) {
               case 1:
                  return;
               case 2:
                  if (!namespaceAware) {
                     startElement(pp.getNamespace(), pp.getName(), pp.getName());
                     break;
                  } else {
                     int depth = pp.getDepth() - 1;
                     int countPrev = pp.getNamespaceCount(depth);
                     int count = pp.getNamespaceCount(depth + 1);
                     for (int i = countPrev; i < count; i++) {
                        this.contentHandler.startPrefixMapping(pp.getNamespacePrefix(i), pp.getNamespaceUri(i));
                     }
                     String name = pp.getName();
                     String prefix = pp.getPrefix();
                     if (prefix != null) {
                        rawName.setLength(0);
                        rawName.append(prefix);
                        rawName.append(':');
                        rawName.append(name);
                     }
                     startElement(pp.getNamespace(), name, prefix == null ? name : rawName.toString());
                     break;
                  }
               case 3:
                  if (!namespaceAware) {
                     this.contentHandler.endElement(pp.getNamespace(), pp.getName(), pp.getName());
                     break;
                  } else {
                     String name2 = pp.getName();
                     String prefix2 = pp.getPrefix();
                     if (prefix2 != null) {
                        rawName.setLength(0);
                        rawName.append(prefix2);
                        rawName.append(':');
                        rawName.append(name2);
                     }
                     this.contentHandler.endElement(pp.getNamespace(), name2, prefix2 == null ? name2 : rawName.toString());
                     int countPrev2 = level > pp.getDepth() ? pp.getNamespaceCount(pp.getDepth()) : 0;
                     for (int i2 = pp.getNamespaceCount(pp.getDepth() - 1) - 1; i2 >= countPrev2; i2--) {
                        this.contentHandler.endPrefixMapping(pp.getNamespacePrefix(i2));
                     }
                     break;
                  }
               case 4:
                  this.contentHandler.characters(pp.getTextCharacters(holderForStartAndLength), holderForStartAndLength[0], holderForStartAndLength[1]);
                  break;
            }
            type = pp.next();
         } while (pp.getDepth() > level);
      } catch (XmlPullParserException ex) {
      //   Mint.logException(ex);
         this.errorHandler.fatalError(new SAXParseException("parsing error: " + ex, this, ex));
      }
   }

   /* access modifiers changed from: protected */
   public void startElement(String namespace, String localName, String qName) throws SAXException {
      this.contentHandler.startElement(namespace, localName, qName, this);
   }
}
