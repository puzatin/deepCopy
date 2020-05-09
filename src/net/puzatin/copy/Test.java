package net.puzatin.copy;


import java.util.ArrayList;
import java.util.List;

public class Test {

    public static void main(String[] args) {


        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");


        Man man = new Man("Vasya", 99, list);

        Man copyMan = CopyUtils.deepCopy(man);



    }
}
