package com.joker.ocean.ipc;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by joker on 17-12-3.
 */

public class MidData implements Parcelable {
    private int id;
    private String name;
    private String content;

    public static final Parcelable.Creator<MidData> CREATOR =
            new android.os.Parcelable.Creator<MidData>() {
                @Override
                public MidData createFromParcel(Parcel parcel) {
                    MidData midData = new MidData();
                    midData.id = parcel.readInt();
                    midData.name = parcel.readString();
                    midData.content = parcel.readString();
                    return midData;
                }

                @Override
                public MidData[] newArray(int i) {
                    return new MidData[0];
                }
            };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(content);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
