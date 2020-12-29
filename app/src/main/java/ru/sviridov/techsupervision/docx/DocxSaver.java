package ru.sviridov.techsupervision.docx;

import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.widget.Toast;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.docx.discovery.DocxTemplateEngineConfiguration;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.ITemplateEngine;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import fr.opensagres.xdocreport.template.formatter.NullImageBehaviour;
import fr.opensagres.xdocreport.template.velocity.discovery.VelocityTemplateEngineDiscovery;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import nl.qbusict.cupboard.CupboardFactory;
import ru.sviridov.techsupervision.Metrics;
import ru.sviridov.techsupervision.db.UserDataProvider;
import ru.sviridov.techsupervision.free.R;
import ru.sviridov.techsupervision.objects.Defect;
import ru.sviridov.techsupervision.objects.Document;
import ru.sviridov.techsupervision.objects.Picture;

public class DocxSaver implements ISaver {
   private final Context context;
   private final List defects;
   private final Document document;
   private final ImageComposer imgComposer;

   public DocxSaver(@NonNull Context var1, long var2) {
      this.context = var1;
      Uri var4 = ContentUris.withAppendedId(UserDataProvider.getContentUri("Document"), var2);
      this.document = (Document)CupboardFactory.cupboard().withContext(var1).get(var4, Document.class);
      this.imgComposer = new ImageComposer(var1.getCacheDir(), var1);
      this.defects = this.initDefects(var1, var2);
   }

   private void asyncSave(@StringRes int var1, @Nullable final DocxSaver.PostListener var2) {
      final ProgressDialog var3 = new ProgressDialog(this.context, R.style.AlertDialogStyle);
      var3.setCancelable(false);
      var3.setTitle((CharSequence)null);
      var3.setMessage(this.context.getString(var1));
      var3.setIndeterminate(true);
      var3.show();
      (new AsyncTask() {
         protected Boolean doInBackground(@NonNull Object... var1) {
            DocxSaver.this.compose();
            return DocxSaver.this.save();
         }

         protected void onPostExecute(@NonNull Boolean var1) {
            System.gc();
            var3.dismiss();
            if (var2 != null) {
               var2.onPostSave(var1);
            }

         }
      }).execute(new Object[0]);
   }

   private void compose() {
      Iterator var1 = this.defects.iterator();

      while(var1.hasNext()) {
         this.composePictures(((DefectwithPicture)var1.next()).getPictures());
         System.gc();
      }

   }

   private void composePictures(List var1) {
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         Picture var3 = (Picture)var2.next();

         try {
            this.imgComposer.compose(var3);
         } catch (IOException var5) {
            HashMap var6 = new HashMap();
            var6.put("outputdir", this.imgComposer.getOutputDir());
            var6.put("picture_id", var3.getId());
            var6.put("picture_id", var3.getImgUrl());
            var6.put("Defect_Id", var3.getDefectId());
          //  Mint.logExceptionMap(var6, var5);
         }
      }

   }

   private File getSavingFile() {
      File var1 = new File(Environment.getExternalStorageDirectory() + File.separator + this.context.getString(R.string.app_name));
      if (!var1.exists()) {
         var1.mkdirs();
      }

      return new File(var1, this.document.title + ".docx");
   }

   private List initDefects(@NonNull Context var1, long var2) {
      return this.initDefectwithPictures(var1, CupboardFactory.cupboard().withContext(var1).query(UserDataProvider.getContentUri("Defect"), Defect.class).withSelection("documentId=" + var2, (String[])null).orderBy("element").list());
   }

   private List initDefectwithPictures(Context var1, List var2) {
      ArrayList var3 = new ArrayList(var2.size());
      Iterator var4 = var2.iterator();

      while(var4.hasNext()) {
         Defect var5 = (Defect)var4.next();
         List var6 = CupboardFactory.cupboard().withContext(var1).query(UserDataProvider.getContentUri("Picture"), Picture.class).withSelection("defectId=" + var5._id, (String[])null).list();
         DefectwithPicture var7 = new DefectwithPicture();
         var7.setOrder(var3.size() + 1);
         var7.setDefect(var5);
         var7.setPictures(var6);
         var3.add(var7);
      }

      return var3;
   }

   public boolean save() {
      boolean var1 = false;
      InputStream var2 = this.context.getResources().openRawResource(R.raw.defect_pattern);

      try {
         File var3 = this.getSavingFile();
         FileOutputStream var4 = new FileOutputStream(var3);
         VelocityTemplateEngineDiscovery var8 = new VelocityTemplateEngineDiscovery();
         ITemplateEngine var9 = var8.createTemplateEngine();
         var9.setConfiguration(DocxTemplateEngineConfiguration.INSTANCE);
         var9.setTemplateCacheInfoProvider(XDocReportRegistry.getRegistry());
         IXDocReport var7 = XDocReportRegistry.getRegistry().loadReport(var2, var9);
         FieldsMetadata var10 = new FieldsMetadata();
         var10.addFieldAsImage("image", "picture.Image", NullImageBehaviour.KeepImageTemplate);
         var7.setFieldsMetadata(var10);
         IContext var11 = var7.createContext();
         var11.put("defects", this.defects);
         var7.process((IContext)var11, var4);
         var4.close();
      } catch (XDocReportException var5) {
       //  Mint.logException(var5);
         return var1;
      } catch (IOException var6) {
      //   Mint.logException(var6);
         return var1;
      }

      var1 = true;
      return var1;
   }

   public void saveToDisk() {
      this.asyncSave(R.string.saving_doc, new DocxSaver.PostListener() {
         public void onPostSave(Boolean var1) {
            Context var2 = DocxSaver.this.context;
            int var3;
            if (var1) {
               var3 = R.string.saved;
            } else {
               var3 = R.string.error_save_doc;
            }

            Toast.makeText(var2, var3, Toast.LENGTH_SHORT).show();
           // Mint.logEvent("saved document", MintLogLevel.Info, Metrics.toMetrics(DocxSaver.this.document, DocxSaver.this.defects));
         }
      });
   }

   public void send() {
      this.asyncSave(R.string.prepare, new DocxSaver.PostListener() {
         public void onPostSave(Boolean var1) {
            Intent var2 = new Intent();
            var2.setAction("android.intent.action.SEND");
            var2.putExtra("android.intent.extra.SUBJECT", DocxSaver.this.document.subject());
            var2.putExtra("android.intent.extra.TEXT", DocxSaver.this.document.full());
            var2.putExtra("android.intent.extra.STREAM", Uri.fromFile(DocxSaver.this.getSavingFile()));
            var2.setType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
            DocxSaver.this.context.startActivity(var2);
         //   Mint.logEvent("sent document", MintLogLevel.Info, Metrics.toMetrics(DocxSaver.this.document, DocxSaver.this.defects));
         }
      });
   }

   private interface PostListener {
      void onPostSave(Boolean var1);
   }
}
