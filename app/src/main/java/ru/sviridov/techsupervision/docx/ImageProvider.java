package ru.sviridov.techsupervision.docx;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import fr.opensagres.xdocreport.core.document.ImageFormat;
import fr.opensagres.xdocreport.document.images.AbstractInputStreamImageProvider;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageProvider extends AbstractInputStreamImageProvider {
   private final Context context;
   private final ImageFormat imageFormat;
   private final String imagePath;

   public ImageProvider(@NonNull Context var1, @NonNull String var2) {
      this(var1, var2, false);
   }

   public ImageProvider(@NonNull Context var1, @NonNull String var2, boolean var3) {
      super(var3);
      this.context = var1;
      this.imageFormat = ImageFormat.getFormatByResourceName(var2);
      this.imagePath = var2;
   }

   public ImageFormat getImageFormat() {
      return this.imageFormat;
   }

   @Nullable
   protected InputStream getInputStream() throws IOException {
      Object var1 = null;

      InputStream var5;
      label35: {
         FileInputStream var4;
         try {
            Uri var2 = Uri.parse(this.imagePath);
            if (var2.getScheme() != null) {
               var5 = this.context.getContentResolver().openInputStream(var2);
               break label35;
            }

            var4 = new FileInputStream(var2.toString());
         } catch (IOException var3) {
         //   Mint.logException(var3);
            return (InputStream)var1;
         }

         var1 = var4;
         return (InputStream)var1;
      }

      var1 = var5;
      return (InputStream)var1;
   }
}
