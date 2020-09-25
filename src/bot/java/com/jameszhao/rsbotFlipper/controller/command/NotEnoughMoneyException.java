package com.jameszhao.rsbotFlipper.controller.command;

public class NotEnoughMoneyException extends Exception {
    private int id;
    private int quantity;
    private int price;
    private long inventoryMoney;

    public NotEnoughMoneyException(String s, int id, int price, int quantity, long inventoryMoney) {
        super(s);
        this.id = id;
        this.quantity = quantity;
        this.price = price;
        this.inventoryMoney = inventoryMoney;
    }

    int getId() {
        return id;
    }

    int getQuantity() {
        return quantity;
    }

    int getPrice() {
        return price;
    }

    long getInventoryMoney() {
        return inventoryMoney;
    }
}