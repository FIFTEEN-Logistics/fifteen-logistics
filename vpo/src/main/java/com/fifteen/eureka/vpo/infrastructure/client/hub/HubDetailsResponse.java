package com.fifteen.eureka.vpo.infrastructure.client.hub;

import lombok.*;

import java.util.UUID;

@Getter
@NoArgsConstructor
@Setter
public class HubDetailsResponse {

    private UUID id;
    private String centralHub;
    private Long hubManagerId;
    private String hubName;
    private String hubAddress;
    private double latitude;
    private double longitude;

}

