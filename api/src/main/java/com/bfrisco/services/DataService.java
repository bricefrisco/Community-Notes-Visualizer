package com.bfrisco.services;

import com.bfrisco.database.Note;
import com.bfrisco.models.NoteDTO;
import io.quarkus.panache.common.Sort;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class DataService {
    public List<NoteDTO> fetchNotes(int page, int pageSize) {
        List<Note> notes = Note.find("classification = 'MISINFORMED_OR_POTENTIALLY_MISLEADING'", Sort.by("createdAt", Sort.Direction.Descending))
                .page(page, pageSize)
                .list();

        return DataMapper.toNoteDtos(notes);
    }
}
