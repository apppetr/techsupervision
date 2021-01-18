/*
 * Decompiled with CFR <Could not determine version>.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 */
package ru.lihachev.norm31937.objects;

import android.os.Parcel;
import android.os.Parcelable;

public class Snip
implements Parcelable {
    public static final Creator<Snip> CREATOR = new Creator<Snip>(){

        public Snip createFromParcel(Parcel parcel) {
            Snip snip = new Snip();
            snip.img_url = parcel.readString();
            snip.url = parcel.readString();
            snip.description = parcel.readString();
            return snip;
        }

        public Snip[] newArray(int n10) {
            return new Snip[n10];
        }
    };
    private String description;
    private String img_url;
    private String url;

    public int describeContents() {
        return 0;
    }

    public String getDepreciation() {
        return this.img_url;
    }

    public String getDescription() {
        return this.description;
    }

    public String getParagraph() {
        return this.url;
    }

    public void setDepreciation(String string) {
        this.description = string;
    }

    public void setDescription(String string) {
        this.img_url = string;
    }

    public void setParagraph(String string) {
        this.url = string;
    }

    public void writeToParcel(Parcel parcel, int n10) {
        parcel.writeString(this.img_url);
        parcel.writeString(this.url);
        parcel.writeString(this.description);
    }

}

