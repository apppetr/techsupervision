package ru.lihachev.norm31937.service;

import java.util.List;

import ru.lihachev.norm31937.objects.Def2Elem;
import ru.lihachev.norm31937.objects.Defect2Reason;
import ru.lihachev.norm31937.objects.Elem2Mat;
import ru.lihachev.norm31937.objects.Reason2Compensation;
import ru.lihachev.norm31937.objects.Variant;

public class UploadVariantData {
   private String apiToken;
   private List<Variant> compensations;
   private List<Def2Elem> def2elems;
   private List<Defect2Reason> defect2reasons;
   private List<Variant> defects;
   private List<Elem2Mat> elem2mats;
   private List<Variant> elements;
   private List<Variant> materials;
   private List<Reason2Compensation> reason2compensations;
   private List<Variant> reasons;
   private User user;

   public String getApiToken() {
      return this.apiToken;
   }

   public void setApiToken(String apiToken2) {
      this.apiToken = apiToken2;
   }

   public User getUser() {
      return this.user;
   }

   public void setUser(User user2) {
      this.user = user2;
   }

   public List<Variant> getMaterials() {
      return this.materials;
   }

   public void setMaterials(List<Variant> materials2) {
      this.materials = materials2;
   }

   public List<Variant> getElements() {
      return this.elements;
   }

   public void setElements(List<Variant> elements2) {
      this.elements = elements2;
   }

   public List<Elem2Mat> getElem2mats() {
      return this.elem2mats;
   }

   public void setElem2mats(List<Elem2Mat> elem2mats2) {
      this.elem2mats = elem2mats2;
   }

   public List<Def2Elem> getDef2elems() {
      return this.def2elems;
   }

   public void setDef2elems(List<Def2Elem> def2elems2) {
      this.def2elems = def2elems2;
   }

   public List<Variant> getDefects() {
      return this.defects;
   }

   public void setDefects(List<Variant> defects2) {
      this.defects = defects2;
   }

   public List<Defect2Reason> getDefect2reasons() {
      return this.defect2reasons;
   }

   public void setDefect2reasons(List<Defect2Reason> defect2reasons2) {
      this.defect2reasons = defect2reasons2;
   }

   public List<Variant> getReasons() {
      return this.reasons;
   }

   public void setReasons(List<Variant> reasons2) {
      this.reasons = reasons2;
   }

   public List<Reason2Compensation> getReason2compensations() {
      return this.reason2compensations;
   }

   public void setReason2compensations(List<Reason2Compensation> reason2compensations2) {
      this.reason2compensations = reason2compensations2;
   }

   public List<Variant> getCompensations() {
      return this.compensations;
   }

   public void setCompensations(List<Variant> compensations2) {
      this.compensations = compensations2;
   }

   public boolean isEmpty() {
      return this.materials.isEmpty() && this.elements.isEmpty() && this.elem2mats.isEmpty() && this.def2elems.isEmpty() && this.defects.isEmpty() && this.defect2reasons.isEmpty() && this.reasons.isEmpty() && this.reason2compensations.isEmpty() && this.compensations.isEmpty();
   }

   public String toString() {
      return "UploadVariantData{materials=" + this.materials + ", elements=" + this.elements + ", elem2mats=" + this.elem2mats + ", def2elems=" + this.def2elems + ", defects=" + this.defects + ", defect2reasons=" + this.defect2reasons + ", reasons=" + this.reasons + ", reason2compensations=" + this.reason2compensations + ", compensations=" + this.compensations + '}';
   }
}
