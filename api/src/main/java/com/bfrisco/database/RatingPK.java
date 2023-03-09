package com.bfrisco.database;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class RatingPK implements Serializable {
    @Column(name = "note_id")
    public String noteId;
    @Column(name = "participant_id")
    public String participantId;

    public RatingPK() {
    }

    public RatingPK(String noteId, String participantId) {
        this.noteId = noteId;
        this.participantId = participantId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RatingPK that = (RatingPK) o;
        return Objects.equals(noteId, that.noteId) && Objects.equals(participantId, that.participantId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(noteId, participantId);
    }
}
