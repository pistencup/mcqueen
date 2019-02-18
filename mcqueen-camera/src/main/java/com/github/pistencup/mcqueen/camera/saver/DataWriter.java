package com.github.pistencup.mcqueen.camera.saver;

import com.github.pistencup.mcqueen.camera.model.ActionRecord;

public interface DataWriter {

    void write(ActionRecord record);

    void flush();

}
