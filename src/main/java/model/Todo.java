package model;

import java.util.*;

public class Todo /* extends PanacheEntity */ {
    public String id = UUID.randomUUID().toString();
    public String task;
    public boolean completed;
    
    // Mocking of existing data, this would normally be in your DB and go via Hibernate/Panache
    private static final List<Todo> all = new ArrayList<>();
    
    public static List<Todo> listAll(){
        return all;
    }

    public static Optional<Todo> findById(String id){
        return all.stream().filter(item -> id.equals(item.id)).findFirst();
    }

    public void persist() {
        all.add(this);
    }
}