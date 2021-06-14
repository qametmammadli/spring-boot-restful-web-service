package com.qamedev.restful.dto;

import lombok.Getter;

import java.util.Arrays;
import java.util.stream.Stream;

@Getter
public enum UserStatus {
    PENDING(0, "PENDING", "User must activate account from sent email"),
    ACTIVE(1, "ACTIVE", "User is active"),
    LOCKED(2, "LOCKED", "User locked out for a bad reason");

    private final int statusId;
    private final String statusName;
    private final String statusDesc;
    private static final Stream<UserStatus> VALUES = Arrays.stream(values());


    UserStatus(int statusId, String statusName, String statusDesc) {
        this.statusId = statusId;
        this.statusName = statusName;
        this.statusDesc = statusDesc;
    }

    public static UserStatus valueOf(int statusId) {
        return VALUES
                .filter(userStatus -> userStatus.getStatusId() == statusId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown user status id " + statusId));
    }
}
