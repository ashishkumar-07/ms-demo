package com.ms.demo.common.util;

import java.util.ArrayList;
import java.util.List;

public class CollectionUtil {

    private CollectionUtil(){

    }

    private static class CollectionUtilHolder{
        static final CollectionUtil INSTANCE = new CollectionUtil();
    }

    public static CollectionUtil getInstance(){
        return CollectionUtilHolder.INSTANCE;
    }

    public <T> List<T> getCollectionFromIterable(Iterable<T> iterable){
        List<T> list = new ArrayList<>();
        iterable.forEach(list::add);
        return list;
    }
}
