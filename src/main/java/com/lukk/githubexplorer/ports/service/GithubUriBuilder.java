package com.lukk.githubexplorer.ports.service;

import com.lukk.githubexplorer.ports.model.SearchData;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;

import static com.lukk.githubexplorer.config.Constants.*;

/**
 * A service that implements UriBuilder interface to build URIs for Github API requests.
 */
@Service
@Primary
public class GithubUriBuilder implements UriBuilder {

    /**
     * {@inheritDoc}
     */
    @Override
    public String buildUri(SearchData searchData) {
        return buildUri(searchData, "0");
    }

    /**
     * {@inheritDoc}
     * <p>
     * For Github API, this method constructs a URI string to search for repositories
     * using the given search data and page number. The URI includes parameters for
     * repository creation date, language, page number, and sorting option.
     */
    @Override
    public String buildUri(SearchData searchData, String page) {

        LocalDate since = findSearchDate(searchData);
        String searchParam = findSearchParam(searchData, since);

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromUriString(BASE_URL)
                .path(URL_PATH)
                .queryParam("q", searchParam)
                .queryParam("per_page", searchData.count())
                .queryParam("page", page)
                .queryParam("sort", "stars")
                .queryParam("order", "desc");

        return builder.build().toUriString();
    }

    private static LocalDate findSearchDate(SearchData searchData) {
        LocalDate since;
        if (null == searchData.since()) {
            since = MIN_GITHUB_DATE;
        } else {
            since = searchData.since();
        }
        return since;
    }

    private static String findSearchParam(SearchData searchData, LocalDate since) {
        String searchParam;
        if (!searchData.language().isBlank()) {
            searchParam = String.format(CREATED_SEARCH_PARAM_NAME + "%s+" + LANGUAGE_SEARCH_PARAM_NAME + "%s", since, searchData.language());
        } else {
            searchParam = String.format(CREATED_SEARCH_PARAM_NAME + "%s", since);
        }
        return searchParam;
    }
}
