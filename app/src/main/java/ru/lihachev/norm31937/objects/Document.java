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
   public int year;

   public Document() {
      this.responsibility = Responsibility.LOW;
      this.date = 0L;
      this.appointment = Appointment.ADMIN;
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
