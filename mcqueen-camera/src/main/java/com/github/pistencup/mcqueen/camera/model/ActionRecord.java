package com.github.pistencup.mcqueen.camera.model;

import lombok.Data;
import com.github.pistencup.mcqueen.racetrack.CloudContext;

@Data
public class ActionRecord {

    private CloudContext context;

    private EndpointDescriptor endpoint;

    private RequestDescriptor request;

    private ResultDescriptor result;

    /**
     * which java method will be called
     */
    private String action;

    private Long beginTime;

    private Long endTime;

    private Long duration;

}
