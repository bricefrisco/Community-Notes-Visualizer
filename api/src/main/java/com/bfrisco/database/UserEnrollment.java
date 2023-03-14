package com.bfrisco.database;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.annotations.RegisterForReflection;

import javax.persistence.*;
import java.sql.Timestamp;

@RegisterForReflection
@Entity
@Table(name = "user_enrollment")
public class UserEnrollment extends PanacheEntityBase {
    @Id
    @Column(name = "participant_id")
    public String participantId;
    @Column(name = "enrollment_state")
    public String enrollmentState;
    @Column(name = "successful_rating_needed_to_earn_in")
    public Integer successfulRatingNeededToEarnIn;
    @Column(name = "timestamp_of_last_state_change")
    public Timestamp timestampOfLastStateChange;
}
