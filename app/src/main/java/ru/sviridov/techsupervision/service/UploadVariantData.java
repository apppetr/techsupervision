package ru.sviridov.techsupervision.service;

import java.util.List;

public class UploadVariantData {
   private String apiToken;
   private List compensations;
   private List def2elems;
   private List defect2reasons;
   private List defects;
   private List elem2mats;
   private List elements;
   private List materials;
   private List reason2compensations;
   private List reasons;
   private User user;

   public String getApiToken() {
      return this.apiToken;
   }

   public List getCompensations() {
      return this.compensations;
   }

   public List getDef2elems() {
      return this.def2elems;
   }

   public List getDefect2reasons() {
      return this.defect2reasons;
   }

   public List getDefects() {
      return this.defects;
   }

   public List getElem2mats() {
      return this.elem2mats;
   }

   public List getElements() {
      return this.elements;
   }

   public List getMaterials() {
      return this.materials;
   }

   public List getReason2compensations() {
      return this.reason2compensations;
   }

   public List getReasons() {
      return this.reasons;
   }

   public User getUser() {
      return this.user;
   }

   public boolean isEmpty() {
      boolean var1;
      if (this.materials.isEmpty() && this.elements.isEmpty() && this.elem2mats.isEmpty() && this.def2elems.isEmpty() && this.defects.isEmpty() && this.defect2reasons.isEmpty() && this.reasons.isEmpty() && this.reason2compensations.isEmpty() && this.compensations.isEmpty()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public void setApiToken(String var1) {
      this.apiToken = var1;
   }

   public void setCompensations(List var1) {
      this.compensations = var1;
   }

   public void setDef2elems(List var1) {
      this.def2elems = var1;
   }

   public void setDefect2reasons(List var1) {
      this.defect2reasons = var1;
   }

   public void setDefects(List var1) {
      this.defects = var1;
   }

   public void setElem2mats(List var1) {
      this.elem2mats = var1;
   }

   public void setElements(List var1) {
      this.elements = var1;
   }

   public void setMaterials(List var1) {
      this.materials = var1;
   }

   public void setReason2compensations(List var1) {
      this.reason2compensations = var1;
   }

   public void setReasons(List var1) {
      this.reasons = var1;
   }

   public void setUser(User var1) {
      this.user = var1;
   }

   public String toString() {
      return "UploadVariantData{materials=" + this.materials + ", elements=" + this.elements + ", elem2mats=" + this.elem2mats + ", def2elems=" + this.def2elems + ", defects=" + this.defects + ", defect2reasons=" + this.defect2reasons + ", reasons=" + this.reasons + ", reason2compensations=" + this.reason2compensations + ", compensations=" + this.compensations + '}';
   }
}
