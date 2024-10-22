package org.ubb.domain;

public class Test extends BaseEntity<Integer> {

    private Integer id;
    private String address;
    private String phone;

    public Test(Integer id, String address, String phone) {
        this.id = id;
        this.address = address;
        this.phone = phone;
    }
    public Test() {}

    @Override
    public String toString() {
        return "Test{" +
                "id=" + id +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
