package org.ubb.domain;

public class Test extends BaseEntity<Integer> {

    private String id;
    private String address;

    public Test(String id, String address) {
        this.id = id;
        this.address = address;
    }

    @Override
    public String toString() {
        return "Test{" +
                "id='" + id + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
