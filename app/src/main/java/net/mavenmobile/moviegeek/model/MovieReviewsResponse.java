package net.mavenmobile.moviegeek.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by bembengcs on 8/6/2017.
 */

public class MovieReviewsResponse implements Parcelable {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("page")
    @Expose
    private int page;
    @SerializedName("results")
    @Expose
    private List<MovieReviews> results = null;
    @SerializedName("total_pages")
    @Expose
    private int totalPages;
    @SerializedName("total_results")
    @Expose
    private int totalResults;
    public final static Parcelable.Creator<MovieReviewsResponse> CREATOR = new Parcelable.Creator<MovieReviewsResponse>() {


        @SuppressWarnings({
                "unchecked"
        })
        public MovieReviewsResponse createFromParcel(Parcel in) {
            MovieReviewsResponse instance = new MovieReviewsResponse();
            instance.id = ((int) in.readValue((int.class.getClassLoader())));
            instance.page = ((int) in.readValue((int.class.getClassLoader())));
            in.readList(instance.results, (net.mavenmobile.moviegeek.model.MovieReviews.class.getClassLoader()));
            instance.totalPages = ((int) in.readValue((int.class.getClassLoader())));
            instance.totalResults = ((int) in.readValue((int.class.getClassLoader())));
            return instance;
        }

        public MovieReviewsResponse[] newArray(int size) {
            return (new MovieReviewsResponse[size]);
        }

    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<MovieReviews> getResults() {
        return results;
    }

    public void setResults(List<MovieReviews> results) {
        this.results = results;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(page);
        dest.writeList(results);
        dest.writeValue(totalPages);
        dest.writeValue(totalResults);
    }

    public int describeContents() {
        return 0;
    }

}
