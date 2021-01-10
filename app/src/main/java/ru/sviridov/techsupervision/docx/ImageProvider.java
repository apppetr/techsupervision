package ru.sviridov.techsupervision.docx;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import fr.opensagres.xdocreport.core.document.ImageFormat;
import fr.opensagres.xdocreport.document.images.AbstractInputStreamImageProvider;

/* renamed from: ru.sviridov.techsupervision.docx.ImageProvider */
public class ImageProvider extends AbstractInputStreamImageProvider {
   private final Context context;
   private final ImageFormat imageFormat;
   private final String imagePath;

   public ImageProvider(@NonNull Context context2, @NonNull String imagePath2, boolean useImageSize) {
      super(useImageSize);
      this.context = context2;
      this.imageFormat = ImageFormat.getFormatByResourceName(imagePath2);
      this.imagePath = imagePath2;
   }

   public ImageProvider(@NonNull Context context2, @NonNull String imgUrl) {
      this(context2, imgUrl, false);
   }

   /* access modifiers changed from: protected */
   @Nullable
   public InputStream getInputStream() throws IOException {
      try {
         Uri uri = Uri.parse(this.imagePath);
         if (uri.getScheme() != null) {
            return this.context.getContentResolver().openInputStream(uri);
         }
         return new FileInputStream(uri.toString());
      } catch (IOException e) {
        // Mint.logException(e);
         return null;
      }
   }

   public ImageFormat getImageFormat() {
      return this.imageFormat;
   }
}
