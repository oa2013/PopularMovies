package agafonova.com.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

public class TrailerResult implements Parcelable {

    private String mId;
    private String mIso_639_1;
    private String mIso_3166_1;
    private String mKey;
    private String mName;
    private String mSite;
    private String mSize;
    private String mType;

    public TrailerResult() {

    }

    public TrailerResult(String iID, String iISOSix, String iISOThree, String iKey, String iName, String iSite, String iSize, String iType) {
        this.mId = iID;
        this.mIso_639_1 = iISOSix;
        this.mIso_3166_1 = iISOThree;
        this.mKey = iKey;
        this.mName = iName;
        this.mSite = iSite;
        this.mSize = iSize;
        this.mType = iType;
    }

    private TrailerResult(Parcel in) {
        mId = in.readString();
        mIso_639_1 = in.readString();
        mIso_3166_1 =  in.readString();
        mKey = in.readString();
        mName = in.readString();
        mSite = in.readString();
        mSize = in.readString();
        mType = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mIso_639_1);
        dest.writeString(mIso_3166_1);
        dest.writeString(mKey);
        dest.writeString(mName);
        dest.writeString(mSite);
        dest.writeString(mSize);
        dest.writeString(mType);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getIso_639_1() {
        return mIso_639_1;
    }

    public void setIso_639_1(String iso_639_1) {
        this.mIso_639_1 = iso_639_1;
    }

    public String getIso_3166_1() {
        return mIso_3166_1;
    }

    public void setIso_3166_1(String iso_3166_1) {
        this.mIso_3166_1 = iso_3166_1;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public String getKey() {
        return mKey;
    }

    public void setKey(String key) {
        this.mKey = key;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getSite() {
        return mSite;
    }

    public void setSite(String site) {
        this.mSite = site;
    }

    public String getSize() {
        return mSize;
    }

    public void setSize(String size) {
        this.mSize = size;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        this.mType = type;
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
