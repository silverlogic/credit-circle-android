package com.tsl.baseapp.model.objects.api;

import android.net.UrlQuerySanitizer;

import com.google.gson.annotations.SerializedName;
import com.tsl.baseapp.utils.Writer;

import java.util.List;

import io.realm.RealmModel;

/**
 * Created by Manu on 21/12/16.
 */

/**
 * Class to handle the paginated responses given by the backend
 * @param <T> The type of the objects of the results list
 */
public class PaginatedResponse<T extends RealmModel> {

    @SerializedName("count")
    private Integer count;

    @SerializedName("next")
    private String nextString;

    @SerializedName("previous")
    private String prevString;

    @SerializedName("results")
    private List<T> results;

    public Integer getCount() {
        return count;
    }

    public Boolean isFirstPage() {
        return (prevString == null);
    }

    public Boolean isLastPage() {
        return (nextString == null);
    }

    public Integer getNext() {
        return getPageParameter(nextString);
    }

    public Integer getPrevious() {
        return getPageParameter(prevString);
    }

    private Integer getPageParameter(String url) {
        if (url == null || url.length() == 0) {
            return null;
        }

        UrlQuerySanitizer sanitizer = new UrlQuerySanitizer(url);
        String value = sanitizer.getValue("page");
        return Integer.parseInt(value);
    }

    public List<T> getResults() {
        return results;
    }

    public void persist() {
        results = Writer.persist(results);
    }
}
