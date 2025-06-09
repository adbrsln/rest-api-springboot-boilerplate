package com.restapi.demo.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Getter
public class PaginatedResponse<T> {

    @JsonProperty("content")
    private final List<T> content;

    @JsonProperty("metadata")
    private final CustomMetadataResponse metadata;

    public PaginatedResponse(Page<T> page, UriComponentsBuilder uriBuilder) {
        this.content = page.getContent();
        this.metadata = new CustomMetadataResponse(page, uriBuilder);
    }
}