package net.puzatin.copy;


import java.util.ArrayList;
import java.util.List;

public class Test {

    public static void main(String[] args) {

        List<String> books = new ArrayList<>();
        books.add("one");
        books.add("two");
        books.add("three");
        List<Object> list = new ArrayList<>();

        Child child = new Child();
        SuperMan superMan = new SuperMan();
        Man man = new Man("Vasya", 33, books);

        Object[] o = new Object[1];
        int[] arr = new int[] {3, 4};
        o[0] = superMan;
        list.add(o);
        list.add(1);
        list.add("3");
        list.add(superMan);
        list.add(arr);
        list.add(list);
        list.add(man);
        superMan.setAge(99);
        superMan.setName("SuperMan");
        superMan.setObjects(list);
        superMan.setArr(arr);
        superMan.setSuperMan(superMan);


        Man copyMan = CopyUtils.deepCopy(man);
        SuperMan copySuperMan = CopyUtils.deepCopy(superMan);
        Child copyChild = CopyUtils.deepCopy(child);

        // результаты смотрел в отладчике

    }
}
