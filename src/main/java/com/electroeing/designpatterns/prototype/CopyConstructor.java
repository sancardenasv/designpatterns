package com.electroeing.designpatterns.prototype;

class Address2 {
    public String streetAddress, city, country;

    public Address2(String streetAddress, String city, String country) {
        this.streetAddress = streetAddress;
        this.city = city;
        this.country = country;
    }

    public Address2(Address2 other) {
        this(other.streetAddress, other.city, other.country);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Address2{");
        sb.append("streetAddress='").append(streetAddress).append('\'');
        sb.append(", city='").append(city).append('\'');
        sb.append(", country='").append(country).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

class Employee {
    public String name;
    public Address2 address;

    public Employee(String name, Address2 address) {
        this.name = name;
        this.address = address;
    }

    public Employee(Employee other) {
        name = other.name;
        address = new Address2(other.address);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Employee{");
        sb.append("name='").append(name).append('\'');
        sb.append(", address=").append(address);
        sb.append('}');
        return sb.toString();
    }
}

public class CopyConstructor {
    public static void main(String[] args) {
        Employee john = new Employee("John", new Address2("123 London Road", "London", "UK"));

        Employee chris = new Employee(john);
        chris.name = "Chris";

        System.out.println(john);
        System.out.println(chris);
    }
}