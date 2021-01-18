package ru.lihachev.norm31937.objects;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;


public class Variant extends Meta implements Parcelable {
    public static final Creator CREATOR = new Creator() {
        public Variant createFromParcel(Parcel var1) {
            return new Variant(var1.readInt(), var1.readString());
        }

        public Variant[] newArray(int var1) {
            return new Variant[var1];
        }
    };
    private Integer _id;
    private String name;
    private String s_description;



    private String note;

    public Variant() {
    }

    public Variant(int var1, String var2) {
        this._id = var1;
        this.name = var2;
    }


    public int describeContents() {
        return 0;
    }

    public int getId() {
        return this._id;
    }

    public String getName() {
        return this.name;
    }

    public void setS_description(String var1) {
        this.s_description = var1;
    }

    public String getS_description() {
        return this.s_description;
    }

    public String get_Note() {
        return this.note;
    }

    public void setNote(String note) {
        this.note = note;
    }
  // public Snip getSnip() {
  //      return snip;
   // }

   // public void setSnip(Snip paramSnip) {
   //     snip = paramSnip;
   // }

    public void setName(String var1) {
        this.name = var1;
    }

    public String toString() {
        return this.name + this.s_description;
    }

    public void writeToParcel(@NonNull Parcel paramParcel, int paramInt) {
       paramParcel.writeInt(this._id);
       paramParcel.writeString(this.name);
        paramParcel.writeString(this.note);
      // paramParcel.writeParcelable(snip, paramInt);
    }
}
