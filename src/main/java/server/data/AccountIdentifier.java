package server.data;

import java.util.LinkedList;
import java.util.List;

public class AccountIdentifier {
    public static List<Integer> idList = new LinkedList<Integer>();
    public static int highestId = 0;
    private int id ;
    private String name;
//    private ReentrantLock lock;

    public static int generateId() {
        int id = ++highestId;
        idList.add(id);
        return id;
    }

    public AccountIdentifier(int id, String name) {
        this.id = id;
        this.name = name;
//        lock = new ReentrantLock(true);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
//
//    public ReentrantLock getLock() {
//        return lock;
//    }

    @Override
    public String toString() {
        return "[Name: " + name + ", Id: " + id + "]";
    }

    @Override
    public boolean equals(Object obj) {
        AccountIdentifier a = (AccountIdentifier)obj;
        return a.id == this.id && a.name.equals(this.name);
    }
}
