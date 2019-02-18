package com.github.pistencup.mcqueen.camera.saver;

import com.alibaba.fastjson.JSON;
import com.github.pistencup.mcqueen.camera.model.ActionRecord;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DefaultDataWriter implements DataWriter {
    private final Log logger = LogFactory.getLog("pistencup-camera");

    @Override
    public void write(ActionRecord record) {
        logger.info(JSON.toJSONString(record));
    }

    @Override
    public void flush() {
        //flush operation is automatic
    }
}
