package ru.lihachev.norm31937.objects;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import org.json.JSONException;
import org.json.JSONObject;

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
    private String snip;

    public String getSnip() {
        if(this.snip==null)
        return "";
        else
            return this.snip;
    }



    //  private String note;

    public Variant(int var1, String var2) {
        this._id = var1;
        this.name = var2;
    }

    public Variant() {

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

    public void setSnip(String snip) {
            this.snip = snip;
    }

    public Snip getSnipclas() throws JSONException {
        if(this.snip == null){
            return null;
        }else{
            Snip snipclass = new Snip();
            Gson localGson = new Gson();

            String json = this.snip; // {"description":100,"url":"name","img_url":"name"}
            snipclass = localGson.fromJson(json, Snip.class);
            return snipclass;
        }
    }

    //public String get_Note() {
    //   return this.note;
    // }

    //public void setNote(String note) {
    //     this.note = note;
    //}
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
        return this.name;
    }

    public void writeToParcel(@NonNull Parcel paramParcel, int paramInt) {
        paramParcel.writeInt(this._id);
        paramParcel.writeString(this.name);
        //   paramParcel.writeString(this.note);
        // paramParcel.writeParcelable(snip, paramInt);
    }
}
