package agafonova.com.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

public class GetTrailer implements Parcelable {

    private String mId;
    private ArrayList<TrailerResult> mResults = null;

    public GetTrailer() {

    }

    public GetTrailer(String iID, ArrayList<TrailerResult> iResults) {
        this.mId = iID;
        this.mResults = iResults;
    }

    private GetTrailer(Parcel in) {
        mId = in.readString();
        in.readTypedList(mResults, TrailerResult.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeTypedList(mResults);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public ArrayList<TrailerResult> getResults() {
        return mResults;
    }

    public void setResults(ArrayList<TrailerResult> results) {
        this.mResults = results;
    }

    static final Parcelable.Creator<GetTrailer> CREATOR = new Parcelable.Creator<GetTrailer>() {
        @Override
        public GetTrailer createFromParcel(Parcel in) {
            return new GetTrailer(in);
        }

        @Override
        public GetTrailer[] newArray(int size) {
            return new GetTrailer[size];
        }
    };
}
