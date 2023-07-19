package com.lukk.githubexplorer.ports.service;

import com.lukk.githubexplorer.ports.model.SearchData;

/**
 * Interface for building URIs used to make API requests.
 */
public interface UriBuilder {

    /**
     * Builds a URI for making an API request with the given search data.
     *
     * @param searchData The search data used to build the URI.
     * @return The URI string.
     */
    String buildUri(SearchData searchData);

    /**
     * Builds a URI for making an API request with the given search data and page number.
     *
     * @param searchData The search data used to build the URI.
     * @param page       The page number for the API request.
     * @return The URI string.
     */
    String buildUri(SearchData searchData, String page);
}
