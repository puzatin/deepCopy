package net.puzatin.copy;

import java.util.Collections;
import java.util.List;

class SuperMan {

    private String name;
    private int age;
    private List<Object> objects;
    private SuperMan superMan;
    private int[] arr;


    public SuperMan(){

    }


    public int[] getArr() {
        return arr;
    }

    public void setArr(int[] arr) {
        this.arr = arr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }



    public List<Object> getObjects() {
        return objects;
    }

    public void setObjects(List<Object> objects) {
        this.objects = objects;
    }

    public SuperMan getSuperMan() {
        return superMan;
    }

    public void setSuperMan(SuperMan superMan) {
        this.superMan = superMan;
    }
}