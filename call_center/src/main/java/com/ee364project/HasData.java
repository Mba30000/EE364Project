package com.ee364project;

public interface HasData extends Cloneable {
    String getDataTypeName();  // get class
    String[] getHeaders();  // csv file first line
    String[] getData();
    HasData parseData(String[] dataFields);
    HasData shuffle();
}
