package ru.sviridov.techsupervision.objects;

public enum Appointment {
   ADMIN("Административное"),
   LIVING("Жилое"),
   PRODUCTION("Производственное");

   private final String name;

   private Appointment(String var3) {
      this.name = var3;
   }

   public String getName() {
      return this.name;
   }
}
