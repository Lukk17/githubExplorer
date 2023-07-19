package com.lukk.githubexplorer.ports.service;

import com.lukk.githubexplorer.adapters.dto.RepoDTO;
import com.lukk.githubexplorer.ports.model.SearchData;
import reactor.core.publisher.Mono;

/**
 * This interface provides an abstract model for repository services.
 */
public interface RepositoryService {

    /**
     * Retrieves the most popular repositories based on the provided search data.
     *
     * @param searchData Data defining the search criteria.
     * @return Mono that emits a RepoDTO containing a list of popular repositories.
     */
    Mono<RepoDTO> getPopularRepos(SearchData searchData);
}
