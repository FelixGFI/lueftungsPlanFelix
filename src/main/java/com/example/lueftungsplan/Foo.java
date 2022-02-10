package com.example.lueftungsplan;

import java.util.ArrayList;

public class Foo {

    private int id;
    private String s;
    private String s2;
    ArrayList<String> list;
    ArrayList<String[]> listOfArrays;

    /**
     * WICHTIG! Jason benötigt immer einen sogennanten DEFAULT CONSTRUCTOR. Das bedeutet
     * einen Konstruktor der ein Lehres OBjelkt erstellt und KEINE Atribute benötigt oder Festlegt
     * WICHTIG! Jason benötigt für alle Attribute einen Getter und einen Setter (Glaube ich)
     */
    public Foo() {}

    public Foo(int id, String s, String s2, ArrayList<String> list, ArrayList<String[]> listOfArrays) {
        this.id = id;
        this.s = s;
        this.s2 = s2;
        this.list = list;
        this.listOfArrays = listOfArrays;
    }

    public int getId() {
        return id;
    }

    public String getS() {
        return s;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setS(String s) {
        this.s = s;
    }

    public String getS2() {
        return s2;
    }

    public void setS2(String s2) {
        this.s2 = s2;
    }

    public ArrayList<String> getList() {
        return list;
    }

    public ArrayList<String[]> getListOfArrays() {
        return listOfArrays;
    }

    public void setList(ArrayList<String> list) {
        this.list = list;
    }

    public void setListOfArrays(ArrayList<String[]> listOfArrays) {
        this.listOfArrays = listOfArrays;
    }

    @Override
    public String toString() {
        return "Foo{" +
                "id=" + id +
                ", s='" + s + '\'' +
                ", s2='" + s2 + '\'' +
                ", list=" + list +
                ", listOfArrays=" + listOfArrays +
                '}';
    }
}
