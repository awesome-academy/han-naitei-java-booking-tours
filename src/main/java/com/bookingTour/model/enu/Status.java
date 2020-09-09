package com.bookingTour.model.enu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Status {

    PENDING(1), APPROVED(2),CANCELLED(3),REJECTED(4);

    public final int value;

    private Status(int value) {
        this.value = value;
    }

    public static Status getStatus(int value){
        switch (value){
            case 1:
                return PENDING;
            case 2:
                return APPROVED;
            case 3:
                return CANCELLED;
            case 4:
                return REJECTED;
            default:
                return null;
        }
    };

    public static Status[] listForUser(){
        List<Status> statuses = new ArrayList<>();
        statuses.add(PENDING);
        statuses.add(CANCELLED);
        Status[] userStatuses = new Status[statuses.size()];
        return statuses.toArray(userStatuses);
    }

    public static Status[] listForAdmin(){
        List<Status> statuses = new ArrayList<>();
        statuses.add(PENDING);
        statuses.add(APPROVED);
        statuses.add(REJECTED);
        Status[] adminStatuses = new Status[statuses.size()];
        return statuses.toArray(adminStatuses);
    }

}
