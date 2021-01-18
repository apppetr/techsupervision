/*
 * Decompiled with CFR <Could not determine version>.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  android.text.TextUtils
 */
package ru.lihachev.norm31937.objects;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

public class DefectSize
implements Parcelable {
    public static final Creator<DefectSize> CREATOR = new Creator<DefectSize>(){

        public DefectSize createFromParcel(Parcel parcel) {
           DefectSize defectSize = new DefectSize();
            defectSize.length = parcel.readString();
            defectSize.depth = parcel.readString();
            defectSize.area = parcel.readString();
            defectSize.width = parcel.readString();
            defectSize.count = parcel.readString();
            defectSize.lengthQuality = parcel.readString();
            defectSize.depthQuality = parcel.readString();
            defectSize.areaQuality = parcel.readString();
            defectSize.widthQuality = parcel.readString();
            defectSize.countQuality = parcel.readString();
            return defectSize;
        }

        public DefectSize[] newArray(int n10) {
            return new DefectSize[n10];
        }
    };
    private String area;
    private String areaQuality;
    private String count;
    private String countQuality;
    private String depth;
    private String depthQuality;
    private String length;
    private String lengthQuality;
    private String width;
    private String widthQuality;

    public int describeContents() {
        return 0;
    }

    public String getArea() {
        return this.area;
    }

    public String getAreaQuality() {
        return this.areaQuality;
    }

    public String getCount() {
        return this.count;
    }

    public String getCountQuality() {
        return this.countQuality;
    }

    public String getDepth() {
        return this.depth;
    }

    public String getDepthQuality() {
        return this.depthQuality;
    }

    public String getLength() {
        return this.length;
    }

    public String getLengthQuality() {
        return this.lengthQuality;
    }

    public String getWidth() {
        return this.width;
    }

    public String getWidthQuality() {
        return this.widthQuality;
    }

    public boolean isEmpty() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.length);
        stringBuilder.append(this.depth);
        stringBuilder.append(this.area);
        stringBuilder.append(this.width);
        stringBuilder.append(this.count);
        return TextUtils.isEmpty((CharSequence)stringBuilder.toString());
    }

    public void setArea(String string) {
        this.area = string;
    }

    public void setAreaQuality(String string) {
        this.areaQuality = string;
    }

    public void setCount(String string) {
        this.count = string;
    }

    public void setCountQuality(String string) {
        this.countQuality = string;
    }

    public void setDepth(String string) {
        this.depth = string;
    }

    public void setDepthQuality(String string) {
        this.depthQuality = string;
    }

    public void setLength(String string) {
        this.length = string;
    }

    public void setLengthQuality(String string) {
        this.lengthQuality = string;
    }

    public void setWidth(String string) {
        this.width = string;
    }

    public void setWidthQuality(String string) {
        this.widthQuality = string;
    }

    public String toShortString() {
        boolean bl;
        StringBuilder stringBuilder = new StringBuilder();
        boolean bl2 = TextUtils.isEmpty((CharSequence)this.length);
        boolean bl3 = false;
        if (!bl2) {
            stringBuilder.append("l=");
            stringBuilder.append(this.length);
            stringBuilder.append(this.lengthQuality.toLowerCase());
            bl = false;
        } else {
            bl = true;
        }
        boolean bl4 = bl;
        if (!TextUtils.isEmpty((CharSequence)this.width)) {
            if (!bl) {
                stringBuilder.append(", ");
            }
            stringBuilder.append("w=");
            stringBuilder.append(this.width);
            stringBuilder.append(this.widthQuality.toLowerCase());
            bl4 = false;
        }
        if (!TextUtils.isEmpty((CharSequence)this.depth)) {
            if (!bl4) {
                stringBuilder.append(", ");
            }
            stringBuilder.append("d=");
            stringBuilder.append(this.depth);
            stringBuilder.append(this.depthQuality.toLowerCase());
            bl4 = bl3;
        }
        if (!TextUtils.isEmpty((CharSequence)this.area)) {
            if (!bl4) {
                stringBuilder.append(", ");
            }
            stringBuilder.append("s=");
            stringBuilder.append(this.area);
            stringBuilder.append(this.areaQuality.toLowerCase());
        }
        if (TextUtils.isEmpty((CharSequence)this.count)) return stringBuilder.toString();
        if (!bl4) {
            stringBuilder.append(", ");
        }
        stringBuilder.append("c=");
        stringBuilder.append(this.count);
        stringBuilder.append(this.countQuality.toLowerCase());
        return stringBuilder.toString();
    }

    public String toString() {
        boolean bl;
        StringBuilder stringBuilder = new StringBuilder();
        boolean bl2 = TextUtils.isEmpty((CharSequence)this.length);
        boolean bl3 = false;
        if (!bl2) {
            stringBuilder.append(" \u0434\u043b\u0438\u043d\u043e\u0439 ");
            stringBuilder.append(this.length);
            stringBuilder.append(this.lengthQuality.toLowerCase());
            bl = false;
        } else {
            bl = true;
        }
        boolean bl4 = bl;
        if (!TextUtils.isEmpty((CharSequence)this.width)) {
            if (!bl) {
                stringBuilder.append(", ");
            }
            stringBuilder.append(" \u0448\u0438\u0440\u0438\u043d\u043e\u0439 ");
            stringBuilder.append(this.width);
            stringBuilder.append(this.widthQuality.toLowerCase());
            bl4 = false;
        }
        if (!TextUtils.isEmpty((CharSequence)this.depth)) {
            if (!bl4) {
                stringBuilder.append(", ");
            }
            stringBuilder.append(" \u0433\u043b\u0443\u0431\u0438\u043d\u043e\u0439 ");
            stringBuilder.append(this.depth);
            stringBuilder.append(this.depthQuality.toLowerCase());
            bl4 = bl3;
        }
        if (!TextUtils.isEmpty((CharSequence)this.area)) {
            if (!bl4) {
                stringBuilder.append(", ");
            }
            stringBuilder.append(" \u043f\u043b\u043e\u0449\u0430\u0434\u044c\u044e ");
            stringBuilder.append(this.area);
            stringBuilder.append(this.areaQuality.toLowerCase());
        }
        if (TextUtils.isEmpty((CharSequence)this.count)) return stringBuilder.toString();
        if (!bl4) {
            stringBuilder.append(", ");
        }
        stringBuilder.append(" \u043a\u043e\u043b\u0438\u0447\u0435\u0441\u0442\u0432\u043e\u043c ");
        stringBuilder.append(this.count);
        stringBuilder.append(this.countQuality.toLowerCase());
        return stringBuilder.toString();
    }

    public void writeToParcel(Parcel parcel, int n10) {
        parcel.writeString(this.length);
        parcel.writeString(this.depth);
        parcel.writeString(this.area);
        parcel.writeString(this.width);
        parcel.writeString(this.count);
        parcel.writeString(this.lengthQuality);
        parcel.writeString(this.depthQuality);
        parcel.writeString(this.areaQuality);
        parcel.writeString(this.widthQuality);
        parcel.writeString(this.countQuality);
    }

}

