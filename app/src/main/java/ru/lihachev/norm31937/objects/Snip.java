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
            snip.description = parcel.readString();
            snip.paragraph = parcel.readString();
            snip.depreciation = parcel.readString();
            return snip;
        }

        public Snip[] newArray(int n10) {
            return new Snip[n10];
        }
    };
    private String depreciation;
    private String description;
    private String paragraph;

    public int describeContents() {
        return 0;
    }

    public String getDepreciation() {
        return this.depreciation;
    }

    public String getDescription() {
        return this.description;
    }

    public String getParagraph() {
        return this.paragraph;
    }

    public void setDepreciation(String string) {
        this.depreciation = string;
    }

    public void setDescription(String string) {
        this.description = string;
    }

    public void setParagraph(String string) {
        this.paragraph = string;
    }

    public void writeToParcel(Parcel parcel, int n10) {
        parcel.writeString(this.description);
        parcel.writeString(this.paragraph);
        parcel.writeString(this.depreciation);
    }

}

