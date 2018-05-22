package agafonova.com.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

public class TrailerResult implements Parcelable {

    String id;
    String iso_639_1;
    String iso_3166_1;
    String key;
    String name;
    String site;
    String size;
    String type;

    public TrailerResult() {

    }

    public TrailerResult(String iID, String iISOSix, String iISOThree, String iKey, String iName, String iSite, String iSize, String iType) {
        this.id = iID;
        this.iso_639_1 = iISOSix;
        this.iso_3166_1 = iISOThree;
        this.key = iKey;
        this.name = iName;
        this.site = iSite;
        this.size = iSize;
        this.type = iType;
    }

    private TrailerResult(Parcel in) {
        id = in.readString();
        iso_639_1 = in.readString();
        iso_3166_1 =  in.readString();
        key = in.readString();
        name = in.readString();
        site = in.readString();
        size = in.readString();
        type = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(iso_639_1);
        dest.writeString(iso_3166_1);
        dest.writeString(key);
        dest.writeString(name);
        dest.writeString(site);
        dest.writeString(size);
        dest.writeString(type);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getIso_639_1() {
        return iso_639_1;
    }

    public void setIso_639_1(String iso_639_1) {
        this.iso_639_1 = iso_639_1;
    }

    public String getIso_3166_1() {
        return iso_3166_1;
    }

    public void setIso_3166_1(String iso_3166_1) {
        this.iso_3166_1 = iso_3166_1;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    static final Parcelable.Creator<TrailerResult> CREATOR = new Parcelable.Creator<TrailerResult>() {
        @Override
        public TrailerResult createFromParcel(Parcel in) {
            return new TrailerResult(in);
        }

        @Override
        public TrailerResult[] newArray(int size) {
            return new TrailerResult[size];
        }
    };
}
