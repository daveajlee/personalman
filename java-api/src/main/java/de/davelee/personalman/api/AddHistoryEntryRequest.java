package de.davelee.personalman.api;

import lombok.*;

/**
 * This class is part of the PersonalMan REST API. It represents a request to add an entry to the user's history.
 * @author Dave Lee
 */
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class AddHistoryEntryRequest {

    //company that the person works for
    private String company;

    //username of the person
    private String username;

    //token of the user making the change
    private String token;

    //date that the history entry took place in format (dd-mm-yyyy)
    private String date;

    //reason for the history entry
    private String reason;

    //comment for the history entry
    private String comment;

}
