package com.jameszhao.rsbotFlipper.server.tasks;


import com.jameszhao.rsbotFlipper.server.data.AccountIdentifier;

public class TaskInformation {
    private AccountIdentifier identifier;
    private int boxId;

    public TaskInformation(AccountIdentifier identifier,  int boxId) {
        this.identifier = identifier;
        this.boxId = boxId;
    }

    public AccountIdentifier getIdentifier() {
        return identifier;
    }

    public void setIdentifier(AccountIdentifier identifier) {
        this.identifier = identifier;
    }

    public int getBoxId() {
        return boxId;
    }

    public void setBoxId(int boxId) {
        this.boxId = boxId;
    }
}
