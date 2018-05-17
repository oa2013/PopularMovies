package agafonova.com.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

public class GetTrailer implements Parcelable {

    String id;
    ArrayList<TrailerResult> results = null;

    public GetTrailer() {

    }

    public GetTrailer(String iID, ArrayList<TrailerResult> iResults) {
        this.id = iID;
        this.results = iResults;
    }

    private GetTrailer(Parcel in) {
        id = in.readString();
        in.readTypedList(results, TrailerResult.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeTypedList(results);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<TrailerResult> getResults() {
        return results;
    }

    public void setResults(ArrayList<TrailerResult> results) {
        this.results = results;
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
