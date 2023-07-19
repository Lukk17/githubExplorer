package com.lukk.githubexplorer.adapters.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record GithubResponse(@JsonProperty("items") List<GithubItem> searchResults) {
    public record GithubItem(@JsonProperty("full_name") String fullName) {
    }
}
