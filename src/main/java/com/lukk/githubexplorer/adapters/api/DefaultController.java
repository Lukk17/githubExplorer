package com.lukk.githubexplorer.adapters.api;

import com.lukk.githubexplorer.adapters.dto.RepoDTO;
import com.lukk.githubexplorer.ports.model.SearchData;
import com.lukk.githubexplorer.ports.service.RepositoryService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.DateTimeException;
import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@Slf4j
public class DefaultController {

    private final RepositoryService repositoryService;

    @GetMapping("/repos/popular")
    public Mono<RepoDTO> getMostPopularRepos(
            @RequestParam(name = "language", defaultValue = "") String language,

            @RequestParam(name = "since", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate since,

            @RequestParam(name = "count", defaultValue = "10") @Min(1) @Max(100) int count) {

        checkIfCorrectDate(since);

        log.info("Getting {} most popular repositories names created since {}, for language: {}",
                count, since, language);

        return repositoryService.getPopularRepos(new SearchData(language, since, count));
    }

    private static void checkIfCorrectDate(LocalDate since) {

        if (since != null && since.isBefore(LocalDate.of(1970, 1, 1))) {
            throw new DateTimeException("Date must be after 01-01-1970!");
        } else if (since != null && since.isAfter(LocalDate.of(2970, 1, 1))) {
            throw new DateTimeException("Date must be before 01-01-2970!");
        }
    }
}
