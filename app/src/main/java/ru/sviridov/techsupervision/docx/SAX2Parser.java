package ru.sviridov.techsupervision.docx;


import java.io.IOException;
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
   protected XmlPullParser pp;
   protected String systemId;

   public SAX2Parser() throws XmlPullParserException {
      XmlPullParserFactory var1 = XmlPullParserFactory.newInstance();
      var1.setNamespaceAware(true);
      this.pp = var1.newPullParser();
   }

   public SAX2Parser(XmlPullParser var1) throws XmlPullParserException {
      this.pp = var1;
   }

   public int getColumnNumber() {
      return this.pp.getColumnNumber();
   }

   public ContentHandler getContentHandler() {
      return this.contentHandler;
   }

   public DTDHandler getDTDHandler() {
      return null;
   }

   public EntityResolver getEntityResolver() {
      return null;
   }

   public ErrorHandler getErrorHandler() {
      return this.errorHandler;
   }

   public boolean getFeature(String var1) throws SAXNotRecognizedException, SAXNotSupportedException {
      boolean var2;
      if ("http://xml.org/sax/features/namespaces".equals(var1)) {
         var2 = this.pp.getFeature("http://xmlpull.org/v1/doc/features.html#process-namespaces");
      } else if ("http://xml.org/sax/features/namespace-prefixes".equals(var1)) {
         var2 = this.pp.getFeature("http://xmlpull.org/v1/doc/features.html#report-namespace-prefixes");
      } else if ("http://xml.org/sax/features/validation".equals(var1)) {
         var2 = this.pp.getFeature("http://xmlpull.org/v1/doc/features.html#validation");
      } else {
         var2 = this.pp.getFeature(var1);
      }

      return var2;
   }

   public int getIndex(String var1) {
      int var2 = 0;

      while(true) {
         if (var2 >= this.pp.getAttributeCount()) {
            var2 = -1;
            break;
         }

         if (this.pp.getAttributeName(var2).equals(var1)) {
            break;
         }

         ++var2;
      }

      return var2;
   }

   public int getIndex(String var1, String var2) {
      int var3 = 0;

      while(true) {
         if (var3 >= this.pp.getAttributeCount()) {
            var3 = -1;
            break;
         }

         if (this.pp.getAttributeNamespace(var3).equals(var1) && this.pp.getAttributeName(var3).equals(var2)) {
            break;
         }

         ++var3;
      }

      return var3;
   }

   public int getLength() {
      return this.pp.getAttributeCount();
   }

   public int getLineNumber() {
      return this.pp.getLineNumber();
   }

   public String getLocalName(int var1) {
      return this.pp.getAttributeName(var1);
   }

   public Object getProperty(String var1) throws SAXNotRecognizedException, SAXNotSupportedException {
      Object var2 = null;
      if (!"http://xml.org/sax/properties/declaration-handler".equals(var1) && !"http://xml.org/sax/properties/lexical-handler".equals(var1)) {
         var2 = this.pp.getProperty(var1);
      }

      return var2;
   }

   public String getPublicId() {
      return null;
   }

   public String getQName(int var1) {
      String var2 = this.pp.getAttributePrefix(var1);
      if (var2 != null) {
         var2 = var2 + ':' + this.pp.getAttributeName(var1);
      } else {
         var2 = this.pp.getAttributeName(var1);
      }

      return var2;
   }

   public String getSystemId() {
      return this.systemId;
   }

   public String getType(int var1) {
      return this.pp.getAttributeType(var1);
   }

   public String getType(String var1) {
      int var2 = 0;

      while(true) {
         if (var2 >= this.pp.getAttributeCount()) {
            var1 = null;
            break;
         }

         if (this.pp.getAttributeName(var2).equals(var1)) {
            var1 = this.pp.getAttributeType(var2);
            break;
         }

         ++var2;
      }

      return var1;
   }

   public String getType(String var1, String var2) {
      int var3 = 0;

      while(true) {
         if (var3 >= this.pp.getAttributeCount()) {
            var1 = null;
            break;
         }

         if (this.pp.getAttributeNamespace(var3).equals(var1) && this.pp.getAttributeName(var3).equals(var2)) {
            var1 = this.pp.getAttributeType(var3);
            break;
         }

         ++var3;
      }

      return var1;
   }

   public String getURI(int var1) {
      return this.pp.getAttributeNamespace(var1);
   }

   public String getValue(int var1) {
      return this.pp.getAttributeValue(var1);
   }

   public String getValue(String var1) {
      return this.pp.getAttributeValue((String)null, var1);
   }

   public String getValue(String var1, String var2) {
      return this.pp.getAttributeValue(var1, var2);
   }

   public void parse(String var1) throws SAXException, IOException {
      this.parse(new InputSource(var1));
   }

   public void parse(InputSource param1) throws SAXException, IOException {
      // $FF: Couldn't be decompiled
   }

   public void parseSubTree(XmlPullParser var1) throws SAXException, IOException {
      this.pp = var1;
      boolean var2 = var1.getFeature("http://xmlpull.org/v1/doc/features.html#process-namespaces");

      XmlPullParserException var10000;
      label179: {
         boolean var10001;
         try {
            if (var1.getEventType() != 2) {
               StringBuilder var36 = new StringBuilder();
               SAXException var37 = new SAXException(var36.append("start tag must be read before skiping subtree").append(var1.getPositionDescription()).toString());
               throw var37;
            }
         } catch (XmlPullParserException var32) {
            var10000 = var32;
            var10001 = false;
            break label179;
         }

         int[] var5;
         StringBuilder var6;
         int var7;
         var5 = new int[2];
         var6 = new StringBuilder(16);
         var7 = var1.getDepth() - 1;

         int var8 = 2;

         label167:
         while(true) {
            String var4;
            int var9;
            String var10;
            String var35;
            label161:
            switch(var8) {
            case 1:
               return;
            case 2:
               if (var2) {
                  try {
                     var9 = var1.getDepth() - 1;
                     var8 = var1.getNamespaceCount(var9);
                     var9 = var1.getNamespaceCount(var9 + 1);
                  } catch (XmlPullParserException var29) {
                     var10000 = var29;
                     var10001 = false;
                     break label167;
                  }

                  for(; var8 < var9; ++var8) {
                     try {
                        this.contentHandler.startPrefixMapping(var1.getNamespacePrefix(var8), var1.getNamespaceUri(var8));
                     } catch (XmlPullParserException var28) {
                        var10000 = var28;
                        var10001 = false;
                        break label167;
                     }
                  }

                  var4 = var1.getName();
                  var35 = var1.getPrefix();

                  if (var35 != null) {
                     var6.setLength(0);
                     var6.append(var35);
                     var6.append(':');
                     var6.append(var4);
                  }

                  var10 = var1.getNamespace();

                  if (var35 == null) {
                     var35 = var4;
                  } else {
                     var35 = var6.toString();
                  }

                  this.startElement(var10, var4, var35);
               } else {
                  this.startElement(var1.getNamespace(), var1.getName(), var1.getName());
               }
               break;
            case 3:
               if (var2) {
                  var4 = var1.getName();
                  var35 = var1.getPrefix();

                  if (var35 != null) {
                     var6.setLength(0);
                     var6.append(var35);
                     var6.append(':');
                     var6.append(var4);
                  }

                  ContentHandler var11;
                  var11 = this.contentHandler;
                  var10 = var1.getNamespace();

                  if (var35 == null) {
                     var35 = var4;
                  } else {
                     var35 = var6.toString();
                  }

                  label154: {
                     try {
                        var11.endElement(var10, var4, var35);
                        if (var7 > var1.getDepth()) {
                           var8 = var1.getNamespaceCount(var1.getDepth());
                           break label154;
                        }
                     } catch (XmlPullParserException var30) {
                        var10000 = var30;
                        var10001 = false;
                        break label167;
                     }

                     var8 = 0;
                  }

                  try {
                     var9 = var1.getNamespaceCount(var1.getDepth() - 1) - 1;
                  } catch (XmlPullParserException var16) {
                     var10000 = var16;
                     var10001 = false;
                     break label167;
                  }

                  while(true) {
                     if (var9 < var8) {
                        break label161;
                     }

                     try {
                        this.contentHandler.endPrefixMapping(var1.getNamespacePrefix(var9));
                     } catch (XmlPullParserException var15) {
                        var10000 = var15;
                        var10001 = false;
                        break label167;
                     }

                     --var9;
                  }
               } else {
                  this.contentHandler.endElement(var1.getNamespace(), var1.getName(), var1.getName());
                  break;
               }
            case 4:
               char[] var3 = var1.getTextCharacters(var5);
               this.contentHandler.characters(var3, var5[0], var5[1]);
            }

            try {
               var8 = var1.next();
            } catch (XmlPullParserException var13) {
               var10000 = var13;
               var10001 = false;
               break;
            }

            if (var1.getDepth() <= var7) {
               return;
            }
         }
      }

      XmlPullParserException var33 = var10000;
      //Mint.logException(var33);
      SAXParseException var34 = new SAXParseException("parsing error: " + var33, this, var33);
      this.errorHandler.fatalError(var34);
   }

   public void setContentHandler(ContentHandler var1) {
      this.contentHandler = var1;
   }

   public void setDTDHandler(DTDHandler var1) {
   }

   public void setEntityResolver(EntityResolver var1) {
   }

   public void setErrorHandler(ErrorHandler var1) {
      this.errorHandler = var1;
   }

   public void setFeature(String var1, boolean var2) throws SAXNotRecognizedException, SAXNotSupportedException {
      try {
         if ("http://xml.org/sax/features/namespaces".equals(var1)) {
            this.pp.setFeature("http://xmlpull.org/v1/doc/features.html#process-namespaces", var2);
         } else if ("http://xml.org/sax/features/namespace-prefixes".equals(var1)) {
            if (this.pp.getFeature("http://xmlpull.org/v1/doc/features.html#report-namespace-prefixes") != var2) {
               this.pp.setFeature("http://xmlpull.org/v1/doc/features.html#report-namespace-prefixes", var2);
            }
         } else if ("http://xml.org/sax/features/validation".equals(var1)) {
            this.pp.setFeature("http://xmlpull.org/v1/doc/features.html#validation", var2);
         } else {
            this.pp.setFeature(var1, var2);
         }
      } catch (XmlPullParserException var3) {
       //  Mint.logException(var3);
      }

   }

   public void setProperty(String var1, Object var2) throws SAXNotRecognizedException, SAXNotSupportedException {
      if ("http://xml.org/sax/properties/declaration-handler".equals(var1)) {
         throw new SAXNotSupportedException("not supported setting property " + var1);
      } else if ("http://xml.org/sax/properties/lexical-handler".equals(var1)) {
         throw new SAXNotSupportedException("not supported setting property " + var1);
      } else {
         try {
            this.pp.setProperty(var1, var2);
         } catch (XmlPullParserException var3) {
            //Mint.logException(var3);
            throw new SAXNotSupportedException("not supported set property " + var1 + ": " + var3);
         }
      }
   }

   protected void startElement(String var1, String var2, String var3) throws SAXException {
      this.contentHandler.startElement(var1, var2, var3, this);
   }
}
