package com.ats.samarthajuice.model;

public class CancelMessageModel {

    private int msgId;
    private String msgTitle;
    private int delStatus;

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public String getMsgTitle() {
        return msgTitle;
    }

    public void setMsgTitle(String msgTitle) {
        this.msgTitle = msgTitle;
    }

    public int getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(int delStatus) {
        this.delStatus = delStatus;
    }

    @Override
    public String toString() {
        return "CancelMessageModel{" +
                "msgId=" + msgId +
                ", msgTitle='" + msgTitle + '\'' +
                ", delStatus=" + delStatus +
                '}';
    }
}
