package ru.sviridov.techsupervision.defects;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import nl.qbusict.cupboard.CupboardFactory;
import ru.sviridov.techsupervision.Helper;
import ru.sviridov.techsupervision.free.R;
import ru.sviridov.techsupervision.objects.MaterialVariant;
import ru.sviridov.techsupervision.objects.Variant;
import ru.sviridov.techsupervision.values.Values;
import ru.sviridov.techsupervision.values.ValuesProvider;
public class ElementController implements DialogInterface.OnClickListener {
   private static Variant ADD_ELEMENT = new Variant(-1, "Добавить новый");
   private static MaterialVariant ADD_MATERIAL = new MaterialVariant(-1, "Добавить новый", -1);
   private ArrayAdapter<Variant> adapter;
   /* access modifiers changed from: private */
   public final Context context;
   private AlertDialog dialog;
   /* access modifiers changed from: private */
   public Variant element = null;
   List<ElementListener> listeners = new ArrayList();

   public interface ElementListener {
      void elementChanged(Variant variant, MaterialVariant materialVariant);
   }

   public ElementController(Context context2) {
      this.context = context2;
      clear();
   }

   public void onClick(DialogInterface dialogInterface, int position) {
      if (this.element == null) {
         Variant selectedElement = this.adapter.getItem(position);
         if (ADD_ELEMENT.equals(selectedElement)) {
            if (Helper.isFree()) {
               Helper.openApp(this.context);
            } else {
               showAddNewElementDialog();
            }
            this.dialog.dismiss();
         }
         this.element = selectedElement;
         List<MaterialVariant> materials = CupboardFactory.cupboard().withContext(this.context).query(ValuesProvider.uri(Values.Materials.URI_FOR_SELECTION), MaterialVariant.class).withSelection("element_id=" + this.element.getId(), (String[]) null).list();
         materials.add(ADD_MATERIAL);
         this.adapter.clear();
         this.adapter.addAll(materials);
         this.dialog.setTitle (R.string.material);
         return;
      }
      MaterialVariant material = (MaterialVariant) this.adapter.getItem(position);
      this.dialog.dismiss();
      if (!ADD_MATERIAL.equals(material)) {
         fireElementEvent(this.element, material);
         clear();
      } else if (Helper.isFree()) {
         Helper.openApp(this.context);
      } else {
         addMaterial();
      }
   }

   private void addMaterial() {
      AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
      View rootView = LayoutInflater.from(this.context).inflate(R.layout.view_add_element, (ViewGroup) null, false);
      final EditText etTitle = (EditText) rootView.findViewById(R.id.etTitle);
      builder.setTitle( "Новый материал для " + this.element).setView(rootView).setPositiveButton("Добавить", new DialogInterface.OnClickListener() {


         public void onClick(DialogInterface dialog, int which) {
            Variant material = new Variant();
            material.setName(etTitle.getText().toString());
            int materialId = Integer.parseInt(CupboardFactory.cupboard().withContext(ElementController.this.context).put(ValuesProvider.uri("materials"), material).getLastPathSegment());
            ContentValues cv = new ContentValues();
            cv.put(Values.E2M.ELEMENT_ID, Integer.valueOf(ElementController.this.element.getId()));
            cv.put(Values.E2M.MATERIAL_ID, Integer.valueOf(materialId));
            cv.put("manually_added", true);
            cv.put("uploaded", false);
            ElementController.this.context.getContentResolver().insert(ValuesProvider.uri("elements2materials"), cv);
            Toast.makeText(ElementController.this.context, "Материал добавлен!", Toast.LENGTH_SHORT).show();
         }
      }).show();
   }

   public void clear() {
      this.dialog = null;
      this.adapter = null;
      this.element = null;
   }

   /* access modifiers changed from: protected */
   public void chooseElement() {
      clear();
      List<Variant> elements = CupboardFactory.cupboard().withContext(this.context).query(ValuesProvider.uri("elements"), Variant.class).list();
      elements.add(ADD_ELEMENT);
      AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
      builder.setTitle((int) R.string.element);
      this.adapter = new ArrayAdapter<>(this.context, R.layout.support_simple_spinner_dropdown_item, elements);
      builder.setSingleChoiceItems(this.adapter, -1, (DialogInterface.OnClickListener) this);
      this.dialog = builder.show();
   }

   private void showAddNewElementDialog() {
      AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
      View rootView = LayoutInflater.from(this.context).inflate(R.layout.view_add_element, (ViewGroup) null, false);
      final EditText etTitle = (EditText) rootView.findViewById(R.id.etTitle);
      builder.setTitle("Новая конструкция").setView(rootView).setPositiveButton((CharSequence) "Добавить", new DialogInterface.OnClickListener() {
         public void onClick(DialogInterface dialog, int which) {
            Variant newElement = new Variant();
            newElement.setName(etTitle.getText().toString());
            CupboardFactory.cupboard().withContext(ElementController.this.context).put(ValuesProvider.uri("elements"), newElement);
            Toast.makeText(ElementController.this.context, "Конструкция добавлена!", Toast.LENGTH_SHORT).show();
         }
      }).show();
   }

   public boolean addListener(ElementListener elementListener) {
      return this.listeners.add(elementListener);
   }

   /* access modifiers changed from: protected */
   public void fireElementEvent(Variant element2, MaterialVariant material) {
      for (ElementListener l : this.listeners) {
         l.elementChanged(element2, material);
      }
   }
}