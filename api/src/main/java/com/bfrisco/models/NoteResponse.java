package com.bfrisco.models;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;

import java.util.List;

@Data
@RegisterForReflection
public class NoteResponse {
    private Integer page;
    private Integer pageSize;
    private Integer totalPages;
    private Long totalResults;
    private List<NoteDTO> results;
}
