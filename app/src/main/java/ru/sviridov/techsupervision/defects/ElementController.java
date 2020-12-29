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
import ru.sviridov.techsupervision.values.ValuesProvider;

public class ElementController implements OnClickListener {
   private static Variant ADD_ELEMENT = new Variant(-1, "Добавить новый");
   private static MaterialVariant ADD_MATERIAL = new MaterialVariant(-1, "Добавить новый", -1);
   private ArrayAdapter adapter;
   private final Context context;
   private AlertDialog dialog;
   private Variant element = null;
   List listeners = new ArrayList();

   public ElementController(Context var1) {
      this.context = var1;
      this.clear();
   }

   private void addMaterial() {
      AlertDialog.Builder var1 = new AlertDialog.Builder(this.context);
      View var2 = LayoutInflater.from(this.context).inflate(R.layout.view_add_element, (ViewGroup)null, false);

      OnClickListener var3 = new OnClickListener() {
         // $FF: synthetic field
         final EditText val$etTitle;

         {
            this.val$etTitle = (EditText) var2;
         }

         public void onClick(DialogInterface var1, int var2) {
            Variant var3 = new Variant();
            var3.setName(this.val$etTitle.getText().toString());
            var2 = Integer.parseInt(CupboardFactory.cupboard().withContext(ElementController.this.context).put(ValuesProvider.uri("materials"), var3).getLastPathSegment());
            ContentValues var4 = new ContentValues();
            var4.put("element_id", ElementController.this.element.getId());
            var4.put("material_id", var2);
            var4.put("manually_added", true);
            var4.put("uploaded", false);
            ElementController.this.context.getContentResolver().insert(ValuesProvider.uri("elements2materials"), var4);
            Toast.makeText(ElementController.this.context, "Материал добавлен!",  Toast.LENGTH_SHORT).show();
         }
      };
      var1.setTitle("Новый материал для " + this.element).setView(var2).setPositiveButton("Добавить", var3).show();
   }

   private void showAddNewElementDialog() {
      AlertDialog.Builder var1 = new AlertDialog.Builder(this.context);
      View var2 = LayoutInflater.from(this.context).inflate(R.layout.view_add_element, (ViewGroup)null, false);
      OnClickListener var3 = new OnClickListener() {
         // $FF: synthetic field
         final EditText val$etTitle;

         {
            this.val$etTitle = (EditText) var2;
         }

         public void onClick(DialogInterface var1, int var2) {
            Variant var3 = new Variant();
            var3.setName(this.val$etTitle.getText().toString());
            CupboardFactory.cupboard().withContext(ElementController.this.context).put(ValuesProvider.uri("elements"), var3);
            Toast.makeText(ElementController.this.context, "Конструкция добавлена!", Toast.LENGTH_SHORT).show();
         }
      };
      var1.setTitle("Новая конструкция").setView(var2).setPositiveButton("Добавить", var3).show();
   }

   public boolean addListener(ElementController.ElementListener var1) {
      return this.listeners.add(var1);
   }

   protected void chooseElement() {
      this.clear();
      List var1 = CupboardFactory.cupboard().withContext(this.context).query(ValuesProvider.uri("elements"), Variant.class).list();
      var1.add(ADD_ELEMENT);
      AlertDialog.Builder var2 = new AlertDialog.Builder(this.context);
      var2.setTitle(R.string.element);
      this.adapter = new ArrayAdapter(this.context, R.layout.support_simple_spinner_dropdown_item, var1);
      var2.setSingleChoiceItems((ListAdapter)this.adapter, -1, this);
      this.dialog = var2.show();
   }

   public void clear() {
      this.dialog = null;
      this.adapter = null;
      this.element = null;
   }

   protected void fireElementEvent(Variant var1, MaterialVariant var2) {
      Iterator var3 = this.listeners.iterator();

      while(var3.hasNext()) {
         ((ElementController.ElementListener)var3.next()).elementChanged(var1, var2);
      }

   }

   public void onClick(DialogInterface var1, int var2) {
      if (this.element == null) {
         Variant var3 = (Variant)this.adapter.getItem(var2);
         if (ADD_ELEMENT.equals(var3)) {
            if (Helper.isFree()) {
               Helper.openApp(this.context);
            } else {
               this.showAddNewElementDialog();
            }

            this.dialog.dismiss();
         }

         this.element = var3;
         List var4 = CupboardFactory.cupboard().withContext(this.context).query(ValuesProvider.uri("select_materials"), MaterialVariant.class).withSelection("element_id=" + this.element.getId(), (String[])null).list();
         var4.add(ADD_MATERIAL);
         this.adapter.clear();
         this.adapter.addAll(var4);
         this.dialog.setTitle(R.string.material);
      } else {
         MaterialVariant var5 = (MaterialVariant)this.adapter.getItem(var2);
         this.dialog.dismiss();
         if (ADD_MATERIAL.equals(var5)) {
            if (Helper.isFree()) {
               Helper.openApp(this.context);
            } else {
               this.addMaterial();
            }
         } else {
            this.fireElementEvent(this.element, var5);
            this.clear();
         }
      }

   }

   public interface ElementListener {
      void elementChanged(Variant var1, MaterialVariant var2);
   }
}
