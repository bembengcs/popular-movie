package net.mavenmobile.moviegeek.model;

import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MovieVideosResponse implements Parcelable {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("results")
    @Expose
    private List<MovieVideos> results = null;
    public final static Parcelable.Creator<MovieVideosResponse> CREATOR = new Creator<MovieVideosResponse>() {


        @SuppressWarnings({
                "unchecked"
        })
        public MovieVideosResponse createFromParcel(Parcel in) {
            MovieVideosResponse instance = new MovieVideosResponse();
            instance.id = ((int) in.readValue((int.class.getClassLoader())));
            in.readList(instance.results, (MovieVideosResponse.class.getClassLoader()));
            return instance;
        }

        public MovieVideosResponse[] newArray(int size) {
            return (new MovieVideosResponse[size]);
        }

    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<MovieVideos> getResults() {
        return results;
    }

    public void setResults(List<MovieVideos> results) {
        this.results = results;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeList(results);
    }

    public int describeContents() {
        return 0;
    }

}