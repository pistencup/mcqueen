package com.github.pistencup.mcqueen.sampleservera;

import com.alibaba.fastjson.JSON;
import com.github.pistencup.mcqueen.camera.model.EndpointDescriptor;
import com.github.pistencup.mcqueen.racetrack.CloudContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class SampleController {

    @Autowired
    private ServerBClient client;

    @Autowired
    private EndpointDescriptor endpoint;

    @GetMapping("/sa")
    public String sampleAction(){
        ArrayList<String> list = new ArrayList<>();
        list.add(JSON.toJSONString(CloudContext.getCurrent()));
        list.add(client.sa());
        list.add(client.sa());
        list.add(JSON.toJSONString("abc"));
        list.add(JSON.toJSONString(endpoint));



        return JSON.toJSONString(list);
    }

    @GetMapping("/get")
    public Integer get(){
        return 1;
    }
}
