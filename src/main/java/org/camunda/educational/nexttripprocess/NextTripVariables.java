package org.camunda.educational.nexttripprocess;

public class NextTripVariables {
    /**
     * List of Map
     */
    public static final String LIST_PARTICIPANTS_VALIDATION = "listParticipantsValidation";

    public static final String LIST_PARTICIPANT_VALIDATION_ACCEPTED = "accepted";
    public static final String LIST_PARTICIPANT_VALIDATION_COMMENT = "comment";
    public static final String LIST_PARTICIPANT_VALIDATION_VOTER = "voter";

    /**
     * Local variable used in the Multi instance value
     */
    public static final String LOCAL_ONE_PARTICIPANT = "oneParticipant";

    /**
     * Participants given by the users, separate by a ; : "Pierre;Paul;Jacques"
     */
    public static final String PARTICIPANTS = "participants";
    /**
     * List of String
     */
    public static final String LIST_PARTICIPANTS = "listParticipants";

    /**
     * All voters accept the destination
     */
    public static final String ALL_ACCEPTED = "allAccepted";


    public static final String REQUESTER = "requester";
    public static final String NEED_PLANE = "needPlane";
    public static final String NEED_AIRBNB = "needAirbnb";
    public static final String DATE_TRIP = "dateTrip";
    public static final String COUNTRY = "country";

    /**
     * class of constant
     */
    private NextTripVariables(){}
}
