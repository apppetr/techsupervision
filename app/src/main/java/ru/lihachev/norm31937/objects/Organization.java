package ru.lihachev.norm31937.objects;

public class Organization {
    public Long _id;
    public String address;
    public long date;
    public String name;
    public int year;
    public String creator;
    public String inspector;
    public String coordinator;

    public Organization() {
        this.date = 0L;
    }
    public String getName() {
        return this.name;
    }

    public String getFull() {
        return this.full();
    }

    public String full() {
        return String.format("Адрес организации: %s,\nГод образования: %s", this.address, this.year);
    }

    public String subject() {
        return "Документ " + this.name;
    }

    public String toString() {
        return this.name;
    }
}

