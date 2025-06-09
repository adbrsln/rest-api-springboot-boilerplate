package com.restapi.demo.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.web.util.UriComponentsBuilder;

@Getter
public class CustomMetadataResponse<T> {

    @JsonProperty("total")
    private final long total;

    @JsonProperty("per_page")
    private final int perPage;

    @JsonProperty("current_page")
    private final int currentPage;

    @JsonProperty("last_page")
    private final int lastPage;

    @JsonProperty("first_page_url")
    private final String firstPageUrl;

    @JsonProperty("last_page_url")
    private final String lastPageUrl;

    @JsonProperty("next_page_url")
    private final String nextPageUrl;

    @JsonProperty("prev_page_url")
    private final String prevPageUrl;

    @JsonProperty("path")
    private final String path;

    @JsonProperty("from")
    private final long from;

    @JsonProperty("to")
    private final long to;


    /**
     * Constructor to build the custom paginated response.
     * @param page The Page object returned from the repository.
     * @param uriBuilder A UriComponentsBuilder seeded with the current request's path.
     */
    public CustomMetadataResponse(Page<T> page, UriComponentsBuilder uriBuilder) {
        this.total = page.getTotalElements();
        this.perPage = page.getSize();
        this.lastPage = page.getTotalPages();
        this.currentPage = page.getNumber() + 1; // Convert 0-based page to 1-based

        // --- URL Construction ---
        // Path without query parameters
        this.path = uriBuilder.replaceQuery(null).toUriString();

        // Full URLs for navigation
        this.firstPageUrl = buildPageUrl(uriBuilder, 1); // Page 1
        this.lastPageUrl = buildPageUrl(uriBuilder, this.lastPage);
        this.nextPageUrl = page.hasNext() ? buildPageUrl(uriBuilder, this.currentPage + 1) : null;
        this.prevPageUrl = page.hasPrevious() ? buildPageUrl(uriBuilder, this.currentPage - 1) : null;

        // --- Record Count Data ---
        long start = page.getNumber() * (long) this.perPage;
        this.from = this.total > 0 ? start + 1 : 0;
        this.to = start + page.getNumberOfElements();
    }

    /**
     * Helper method to build a URL for a specific page number.
     * It correctly preserves any other existing query parameters.
     */
    private String buildPageUrl(UriComponentsBuilder builder, int pageNumber) {
        return builder.replaceQueryParam("page", pageNumber).toUriString();
    }
}