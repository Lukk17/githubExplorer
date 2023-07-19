package com.lukk.githubexplorer.config;

import java.time.LocalDate;

public class Constants {
    public static final LocalDate MIN_GITHUB_DATE = LocalDate.of(1970, 1, 1);
    public static final String CREATED_SEARCH_PARAM_NAME = "created:>=";
    public static final String LANGUAGE_SEARCH_PARAM_NAME = "language:";
    public static final String BASE_URL = "https://api.github.com";
    public static final String URL_PATH = "/search/repositories";
}
