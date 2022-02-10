package com.example.lueftungsplan;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Arrays;

public class JacksonJasonPlayground {

    public static void main(String[] args) throws JsonProcessingException {
        someMeathod();
        System.out.println("A String");
    }

    public static int someMeathod() throws JsonProcessingException {
        String[] stringArr1 = {"ne", "doch"};
        String[] stringArr2 = {"mein", "Hallo", "Grüzi",};
        System.out.println("Hello, world!");
        Foo foo = new Foo(1, "first", "Second", new ArrayList<String>(Arrays.asList("a", "b", "c")), new ArrayList<String[]>(Arrays.asList(stringArr1, stringArr2)));
        ObjectMapper mapper = new ObjectMapper();

        String jasonStr = mapper.writeValueAsString(foo);
        System.out.println("JsonString: " + jasonStr);
        System.out.println("FooToString: " + foo.toString());
        Foo result = mapper.readValue(jasonStr, Foo.class);
        if(result.getId() == foo.getId()) {
            System.out.println("Yes");
        }
        return foo.getId();
    }
}
