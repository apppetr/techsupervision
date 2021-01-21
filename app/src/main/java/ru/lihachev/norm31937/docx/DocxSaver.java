package ru.lihachev.norm31937.docx;

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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import  fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.docx.discovery.DocxTemplateEngineConfiguration;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.ITemplateEngine;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import fr.opensagres.xdocreport.template.formatter.NullImageBehaviour;
import fr.opensagres.xdocreport.template.velocity.discovery.VelocityTemplateEngineDiscovery;
import nl.qbusict.cupboard.CupboardFactory;
import ru.lihachev.norm31937.free.R;
import ru.lihachev.norm31937.objects.Defect;
import ru.lihachev.norm31937.objects.Document;
import ru.lihachev.norm31937.objects.Picture;
import ru.lihachev.norm31937.db.UserDataHelper;
import ru.lihachev.norm31937.db.UserDataProvider;

public class DocxSaver implements ISaver {
   public final Context context;
   public final List<DefectwithPicture> defects;
   public final Document document;
   private final ImageComposer imgComposer;

   private interface PostListener {
      void onPostSave(Boolean bool);
   }

   public DocxSaver(@NonNull Context context2, long docId) {
      this.context = context2;
      this.document = (Document) CupboardFactory.cupboard().withContext(context2).get(ContentUris.withAppendedId(UserDataProvider.getContentUri(UserDataHelper.DOCUMENT_URL), docId), Document.class);
      this.imgComposer = new ImageComposer(context2.getCacheDir(), context2);
      this.defects = initDefects(context2, docId);
   }

   private List<DefectwithPicture> initDefectwithPictures(Context context2, List<Defect> defects2) {
      List<DefectwithPicture> defectswithPictures = new ArrayList<>(defects2.size());
      for (Defect defect : defects2) {
         List<Picture> pics = CupboardFactory.cupboard().withContext(context2).query(UserDataProvider.getContentUri(UserDataHelper.PICTURE_URL), Picture.class).withSelection("defectId=" + defect._id, (String[]) null).list();
         DefectwithPicture defectwithPictures = new DefectwithPicture();
         defectwithPictures.setOrder(defectswithPictures.size() + 1);
         defectwithPictures.setDefect(defect);
         defectwithPictures.setPictures(pics);
         defectswithPictures.add(defectwithPictures);
      }
      return defectswithPictures;
   }

   private List<DefectwithPicture> initDefects(@NonNull Context context2, long docId) {
      return initDefectwithPictures(context2, CupboardFactory.cupboard().withContext(context2).query(UserDataProvider.getContentUri(UserDataHelper.DEFECT_URL), Defect.class).withSelection("documentId=" + docId, (String[]) null).orderBy("element").list());
   }

   /* access modifiers changed from: private */
   public void compose() {
      for (DefectwithPicture defect : this.defects) {
        // defect.NiceReasons = defect.getNiceReasons();
        // defect.NiceCompensations = defect.getNiceCompensations();
        // defect.NiceProblems = defect.getNiceProblems();
         composePictures(defect.getPictures());
         System.gc();
      }
   }

   private void composePictures(List<Picture> pictures) {
      for (Picture picture : pictures) {
         try {
            this.imgComposer.compose(picture);
         } catch (IOException e) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("outputdir", this.imgComposer.getOutputDir());
            map.put("picture_id", picture.getId());
            map.put("picture_id", picture.getImgUrl());
            map.put("Defect_Id", picture.getDefectId());
           //Mint.logExceptionMap(map, e);
         }
      }
   }

   public boolean save() {
      InputStream inputStream = this.context.getResources().openRawResource(R.raw.defect_pattern);
      try {
         FileOutputStream outStream = new FileOutputStream(getSavingFile());
         ITemplateEngine engine = new VelocityTemplateEngineDiscovery().createTemplateEngine();
         engine.setConfiguration(DocxTemplateEngineConfiguration.INSTANCE);
         engine.setTemplateCacheInfoProvider(XDocReportRegistry.getRegistry());
         IXDocReport report = XDocReportRegistry.getRegistry().loadReport(inputStream, engine);
         FieldsMetadata metadata = new FieldsMetadata();
         metadata.addFieldAsImage("image", "picture.Image", NullImageBehaviour.KeepImageTemplate);
         report.setFieldsMetadata(metadata);
         IContext iContext = report.createContext();
         iContext.put("defects", this.defects);
         iContext.put("document", this.document);
         report.process(iContext, (OutputStream) outStream);
         outStream.close();
         return true;
      } catch (XDocReportException | IOException e) {
       ///  Mint.logException(e);
         return false;
      } // Mint.logException(e2);

   }

   /* access modifiers changed from: private */
   public File getSavingFile() {
      File dir = new File(Environment.getExternalStorageDirectory() + File.separator + this.context.getString(R.string.app_name));
      if (!dir.exists()) {
         dir.mkdirs();
      }
      return new File(dir, this.document.title + ".docx");
   }

   public void saveToDisk() {
      asyncSave(R.string.saving_doc, new PostListener() {
         public void onPostSave(Boolean isSaved) {
            Toast.makeText(DocxSaver.this.context, isSaved.booleanValue() ? R.string.saved : R.string.error_save_doc, Toast.LENGTH_SHORT).show();
         //   Mint.logEvent(Metrics.SAVED_DOC, MintLogLevel.Info, Metrics.toMetrics(DocxSaver.this.document, (List<DefectwithPicture>) DocxSaver.this.defects));
         }
      });
   }

   public void send() {
      asyncSave(R.string.prepare, new PostListener() {
         public void onPostSave(Boolean isSaved) {
            Intent sendIntent = new Intent();
            sendIntent.setAction("android.intent.action.SEND");
            sendIntent.putExtra("android.intent.extra.SUBJECT", DocxSaver.this.document.subject());
            sendIntent.putExtra("android.intent.extra.TEXT", DocxSaver.this.document.full());
            sendIntent.putExtra("android.intent.extra.STREAM", Uri.fromFile(DocxSaver.this.getSavingFile()));
            sendIntent.setType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
            DocxSaver.this.context.startActivity(sendIntent);
           // Mint.logEvent(Metrics.SENT_DOC, MintLogLevel.Info, Metrics.toMetrics(DocxSaver.this.document, (List<DefectwithPicture>) DocxSaver.this.defects));
         }
      });
   }

   private void asyncSave(@StringRes int saveTitleId, @Nullable final PostListener listener) {
      final ProgressDialog dialog = new ProgressDialog(this.context, R.style.AlertDialogStyle);
      dialog.setCancelable(false);
      dialog.setTitle((CharSequence) null);
      dialog.setMessage(this.context.getString(saveTitleId));
      dialog.setIndeterminate(true);
      dialog.show();
      new AsyncTask<Object, Object, Boolean>() {
         /* access modifiers changed from: protected */
         public Boolean doInBackground(@NonNull Object... params) {
            DocxSaver.this.compose();
            return Boolean.valueOf(DocxSaver.this.save());
         }

         /* access modifiers changed from: protected */
         public void onPostExecute(@NonNull Boolean result) {
            System.gc();
            dialog.dismiss();
            if (listener != null) {
               listener.onPostSave(result);
            }
         }
      }.execute();
   }
}
