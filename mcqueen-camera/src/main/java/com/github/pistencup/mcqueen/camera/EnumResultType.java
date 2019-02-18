package com.github.pistencup.mcqueen.camera;

public enum EnumResultType {

    NONE("none"),
    EXCEPTION("exception"),
    MODEL_AND_VIEW("model_and_view"),
    STRING("string"),
    JSON("json");

    private final String value;

    EnumResultType(String value){
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
