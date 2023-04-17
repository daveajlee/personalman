package de.davelee.personalman.api;

import lombok.*;

/**
 * This class is part of the PersonalMan REST API. It represents a request to add a new training course or qualification
 * to the user's profile.
 * @author Dave Lee
 */
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class AddTrainingRequest {

    //company that the person works for
    private String company;

    //username of the person
    private String username;

    //token of the user making the change
    private String token;

    //name of the training course or qualification to be added to the user's profile
    private String trainingCourse;

}
