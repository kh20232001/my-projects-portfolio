package com.api._dummy;

public class DummyData {

    private String comment;

    private boolean result;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "DummyData [comment=" + comment + ", result=" + result + "]";
    }

}
