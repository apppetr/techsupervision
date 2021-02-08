package ru.lihachev.norm31937.objects;

public class Document {
   public Long _id;
   public String address;
   public Appointment appointment;
   public long date;
   public int floors;
   public String photo;
   public Responsibility responsibility;
   public String sizes;
   public String title;
   public String organization;
   public String creator;
   public String inspector;
   public String coordinator;
   public String orgAddres;
   public int year;

   public Document() {
      this.responsibility = Responsibility.LOW;
      this.date = 0L;
      this.appointment = Appointment.ADMIN;
   }
   public String getTitle() {
      return this.title;
   }

   public String getFull() {
      return this.full();
   }

   public String full() {
      return String.format("Адрес объекта: %s,\nГод постройки: %s,\nУровень ответственности: %s,\nГабариты в плане: %s,\nЭтажность: %s,\nНазначение: %s", this.address, this.year, this.responsibility.getName(), this.sizes, this.floors, this.appointment.getName());
   }

   public String subject() {
      return "Документ " + this.title;
   }

   public String toString() {
      return this.title;
   }
}
