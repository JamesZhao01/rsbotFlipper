package com.jameszhao.rsbotFlipper.server.data;

import com.google.protobuf.InvalidProtocolBufferException;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class DataHandler {
    private ThreadPoolExecutor threadPool;
    private ConcurrentHashMap<AccountIdentifier, AccountInformation> data;
    public DataHandler() {
        threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(6);
        data = new ConcurrentHashMap<AccountIdentifier, AccountInformation>();
    }

    public AccountInformation get(AccountIdentifier identifier) {
        return data.get(identifier);
    }

    public AccountIdentifier getIdentifierById(int id) {
        for(AccountIdentifier a : data.keySet()) {
            if(a.getId() == id)
                return a;
        }
        return null;
    }
//    public BotResponse createNew(String name, StatusUpdate statusUpdate) {
//        AccountIdentifier identifier = new AccountIdentifier(AccountIdentifier.generateId(), name);
//
//        if(data.keySet().contains(identifier)) {
//            return BotResponse.newBuilder().setStatus(BotResponse.BotResponseStatus.ALREADY_CONNECTED).build();
//        } else {
//            AccountInformation accountInformation = new AccountInformation(statusUpdate, -1, false);
//            data.put(identifier, accountInformation);
//            return BotResponse.newBuilder().setId(identifier.getId()).setStatus(BotResponse.BotResponseStatus.OK).build();
//        }
//    }
//
//    public Set<AccountIdentifier> getKeys() {
//        return data.keySet();
//    }
//
//    public StatusUpdate getById(int id) throws IdNotFoundInMapException {
//        for(AccountIdentifier a : data.keySet()) {
//            if(a.getId() == id)
//                return data.get(a).getStatusUpdate();
//        }
//        throw new IdNotFoundInMapException(String.format("Id %d does not exist", id));
//    }
//    public void write(AccountIdentifier i,  StatusUpdate update) {
//        System.out.println(String.format("[DataHandler] write | identifier: %s", i.toString()));
//        data.get(i).setStatusUpdate(update);
//    }
//
//    public void write(Update update) {
//        AccountIdentifier identifier = scanForId(update.getId());
//        write(identifier, update.getStatusUpdate());
//    }
//
//    public void populateDummy() {
//        try {
//            createNew("dookie", Update.parseFrom(converter("[8, 2, 26, 102, 10, 30, 8, 2, 18, 18, 8, -75, 10, 18, 13, 82, 117, 110, 101, 32, 115, 99, 105, 109, 105, 116, 97, 114, 24, 1, 32, 10, 40, 10, 64, 1, 10, 21, 8, 1, 18, 9, 8, -27, 1, 18, 4, 86, 105, 97, 108, 24, 11, 32, 1, 40, 1, 64, 2, 10, 25, 8, 2, 18, 13, 8, -59, 6, 18, 8, 67, 114, 111, 115, 115, 98, 111, 119, 24, 76, 32, 1, 40, 1, 64, 3, 18, 18, 10, 16, 10, 10, 8, -29, 7, 18, 5, 67, 111, 105, 110, 115, 16, -102, -124, 5]")).getStatusUpdate());
//        } catch (InvalidProtocolBufferException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public Set fetchAll() {
//        return data.entrySet();
//    }
//
//    public AccountIdentifier scanForId(int id) {
//        for(AccountIdentifier identifier : data.keySet()) {
//            if(identifier.getId() == id) {
//                return identifier;
//            }
//        }
//        return null;
//    }
//
//    public StatusUpdate read(AccountIdentifier i) {
//        return data.get(i).getStatusUpdate();
//    }
//
//    public Set<Map.Entry<AccountIdentifier, AccountInformation>> readAll() {
//        return data.entrySet();
//    }
//
//    public void printData() {
//        for(AccountIdentifier identifier : data.keySet()) {
//            System.out.println("Identifier: " + identifier.toString());
//            System.out.println("Data: " + data.get(identifier).toString());
//        }
//    }
//
//    private byte[] converter(String s) {
//         int byteCount = 0;
//        for(char c : s.toCharArray()) {
//            if(c == ',') {
//                byteCount++;
//            }
//        }
//        byteCount++;
//
//        byte[] data = new byte[byteCount];
//        String buffer = "";
//        int dataindex = 0;
//        for(char c : s.toCharArray()) {
//            if(c >= '0' && c <= '9' || c == '-') {
//                buffer+= c + "";
//            } else if(c == ',' || c == ']') {
//                int piece = Integer.parseInt(buffer);
//                data[dataindex] = (byte)piece;
//                dataindex++;
//                buffer = "";
//            }
//        }
//        return data;
//    }
//
//    public class IdNotFoundInMapException extends Exception {
//        public IdNotFoundInMapException(String s) {
//            super(s);
//        }
//    }

}
