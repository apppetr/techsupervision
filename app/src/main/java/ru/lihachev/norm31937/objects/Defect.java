package ru.lihachev.norm31937.objects;

import ru.lihachev.norm31937.defects.place.object.Place;
import ru.lihachev.norm31937.utils.Formats;

public class Defect {
   public Long _id;
   private String category = "";
   public Variant[] compensations;
   public int documentId;
   private Variant element;
   private MaterialVariant material;
   public Place place;
   public Variant[] problems;
   public String problemsForSaving;
   public Variant[] reasons;
   public long updated;
   private String volume;

   public String getCategory() {
      return this.category;
   }

   public Variant getElement() {
      return this.element;
   }

   public MaterialVariant getMaterial() {
      return this.material;
   }

   public String getNiceCompensations() {
      return Formats.formatArray(this.compensations);
   }

   public String getNiceProblems() {
      return Formats.formatArray(this.problems);
   }

   public String getHuinyar() {
      String Huinyar = this.getNiceReasons();
      return Huinyar;
   }

   public String getHuinyac() {
      String Huinyac = this.getNiceCompensations();
      return Huinyac;
   }

   public String getNiceReasons() {
      return Formats.formatArray(this.reasons);
   }

   public String getVolume() {
      String Volume = this.volume;
      return  Volume;
   }

   public void setCategory(String var1) {
      this.category = var1;
   }

   public void setElement(Variant var1) {
      this.element = var1;
   }

   public void setMaterial(MaterialVariant var1) {
      this.material = var1;
   }

   public void setVolume(String var1) {
      this.volume = var1;
   }
}
