package ru.lihachev.norm31937.objects;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.Gson;

import org.json.JSONException;

import ru.lihachev.norm31937.utils.Packet;

public class Variant extends Meta implements Parcelable {
    public static final Creator CREATOR = new Creator() {
        public Variant createFromParcel(Parcel paramAnonymousParcel)
        {
            Variant localVariant = new Variant(paramAnonymousParcel.readInt(), paramAnonymousParcel.readString());
            localVariant.setNote(paramAnonymousParcel.readString());
            localVariant.setSnip(paramAnonymousParcel.readString());
          //  localVariant.setDefectSize(paramAnonymousParcel.readString());
            //localVariant.setSnip((Snip)paramAnonymousParcel.readParcelable(Snip.class.getClassLoader()));
            //localVariant.setDefectSize((DefectSize)paramAnonymousParcel.readParcelable(DefectSize.class.getClassLoader()));
            return localVariant;
        }

        public Variant[] newArray(int var1) {
            return new Variant[var1];
        }
    };
    private Integer _id;
    private String name;
    private String snip;
    private String note;

    public String getNote() {
        if (this.note == null)
            return "";
        else
            return Packet.valueOf(this.note);
    }



    public String getSnip() {
        if (this.snip == null)
            return "";
        else
            return this.snip;
    }

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
        if (this.snip == null) {
            return null;
        } else {
            Snip snipclass = new Snip();
            Gson localGson = new Gson();

            String json = this.snip; // {"description":100,"url":"name","img_url":"name"}
            snipclass = localGson.fromJson(json, Snip.class);
            return snipclass;
        }
    }


    public Note getNoteclas() throws JSONException {
        if (this.note == null) {
            return null;
        } else {
            Note noteclass = new Note();
            Gson localGson = new Gson();

            String json = this.note; // {"description":100,"url":"name","img_url":"name"}
            noteclass = localGson.fromJson(json, Note.class);
            return noteclass;
        }
    }

    public void setNote(String note) {
         this.note = Packet.valueOf(note);
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
        return this.name;
    }

    public void writeToParcel(@NonNull Parcel paramParcel, int paramInt) {
        String note = "";
        String snip = "";
        if(this.snip != null){
            snip = this.snip;
        }

        if(this.note != null){
             note = this.note;
        }
        paramParcel.writeInt(this._id);
        paramParcel.writeString(this.name);
        paramParcel.writeString(Packet.valueOf(note));
        paramParcel.writeString(Packet.valueOf(snip));
       // paramParcel.writeString(Packet.valueOf(defectSize));
       //  paramParcel.writeString(snip);
    }
}
