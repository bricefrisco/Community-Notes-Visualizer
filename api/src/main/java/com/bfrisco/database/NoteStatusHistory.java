package com.bfrisco.database;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.annotations.RegisterForReflection;

import javax.persistence.*;
import java.sql.Timestamp;

@RegisterForReflection
@Entity
@Table(name = "note_status_history")
public class NoteStatusHistory extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @Column(name = "note_id")
    public String noteId;
    @JoinColumn(name = "note_id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    public Note note;
    @Column(name = "created_at")
    public Timestamp createdAt;
    @Column(name = "timestamp_first_non_nmr_status")
    public Timestamp timestampFirstNonNmrStatus;
    @Column(name = "first_non_nmr_status")
    public String firstNonNmrStatus;
    @Column(name = "timestamp_of_current_status")
    public Timestamp timestampOfCurrentStatus;
    @Column(name = "current_status")
    public String currentStatus;
    @Column(name = "timestamp_latest_non_nmr_status")
    public Timestamp timestampLatestNonNmrStatus;
    @Column(name = "latest_non_nmr_status")
    public String latestNonNmrStatus;
}
