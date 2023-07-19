package com.lukk.githubexplorer.ports.service;

import com.lukk.githubexplorer.ports.model.SearchData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class GithubUriBuilderTest {

    GithubUriBuilder githubUriBuilder;

    @BeforeEach
    void init() {
        githubUriBuilder = new GithubUriBuilder();
    }

    @Test
    void whenBuildUri_returnCorrectUrl() {
        // Given
        String expected = "https://api.github.com/search/repositories?q=created:>=2003-06-16+language:Java&per_page=20&page=0&sort=stars&order=desc";
        SearchData searchData = new SearchData("Java", LocalDate.of(2003, 6, 16), 20);

        // When
        String actual = githubUriBuilder.buildUri(searchData);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void whenBuildUriWithoutLanguage_returnCorrectUrl() {
        // Given
        String expected = "https://api.github.com/search/repositories?q=created:>=2003-06-16&per_page=20&page=0&sort=stars&order=desc";
        SearchData searchData = new SearchData("", LocalDate.of(2003, 6, 16), 20);

        // When
        String actual = githubUriBuilder.buildUri(searchData);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void whenBuildUriWithoutDate_returnCorrectUrl() {
        // Given
        String expected = "https://api.github.com/search/repositories?q=created:>=1970-01-01&per_page=20&page=0&sort=stars&order=desc";
        SearchData searchData = new SearchData("", null, 20);

        // When
        String actual = githubUriBuilder.buildUri(searchData);

        // Then
        assertEquals(expected, actual);
    }
}
