package ru.lihachev.norm31937.objects;

public enum Responsibility {
   HIGH("Повышенный"),
   LOW("Пониженный"),
   MEDIUM("Нормальный");

   private final String name;

   private Responsibility(String var3) {
      this.name = var3;
   }

   public String getName() {
      return this.name;
   }
}
