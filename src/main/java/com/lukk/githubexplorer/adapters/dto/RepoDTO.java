package com.lukk.githubexplorer.adapters.dto;

import java.util.List;

public record RepoDTO(List<String> repositories) {

    public static RepoDTO of(GithubResponse githubResponse) {

        return new RepoDTO(githubResponse.searchResults()
                .stream()
                .map(GithubResponse.GithubItem::fullName)
                .toList());
    }
}
