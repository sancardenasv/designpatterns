package com.electroeing.designpatterns.builder;

class PersonF {
    // Address
    public String streetAddress, postcode, city;

    //employment
    public String companyName, position;
    public int annualIncome;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PersonF{");
        sb.append("streetAddress='").append(streetAddress).append('\'');
        sb.append(", postcode='").append(postcode).append('\'');
        sb.append(", city='").append(city).append('\'');
        sb.append(", companyName='").append(companyName).append('\'');
        sb.append(", position='").append(position).append('\'');
        sb.append(", annualIncome=").append(annualIncome);
        sb.append('}');
        return sb.toString();
    }
}

// Builder facade
class PersonFBuilder {
    protected PersonF person = new PersonF();

    public PersonAddressBuilder lives() {
        return new PersonAddressBuilder(person);
    }

    public PersonJobBuilder works() {
        return new PersonJobBuilder(person);
    }

    public PersonF build() {
        return person;
    }
}

class PersonAddressBuilder extends PersonFBuilder{
    public PersonAddressBuilder(PersonF person) {
        this.person = person;
    }

    public PersonAddressBuilder at(String streetAddress) {
        person.streetAddress = streetAddress;
        return this;
    }

    public PersonAddressBuilder withPostcode(String postcode) {
        person.postcode = postcode;
        return this;
    }

    public PersonAddressBuilder in(String city) {
        person.city = city;
        return this;
    }
}

class PersonJobBuilder extends PersonFBuilder {
    public PersonJobBuilder(PersonF person) {
        this.person = person;
    }

    public PersonJobBuilder at(String companyName) {
        person.companyName = companyName;
        return this;
    }

    public PersonJobBuilder as(String position) {
        person.position = position;
        return this;
    }

    public PersonJobBuilder earning(int annualIncome) {
        person.annualIncome = annualIncome;
        return this;
    }
}

public class FacetedBuilderDemo {
    public static void main(String[] args) {
        PersonF pf = new PersonFBuilder()
                .lives()
                    .at("146 main street")
                    .in("Colombia")
                    .withPostcode("9182371")
                .works()
                    .at("Globant")
                    .as("Java Developer")
                    .earning(123865)
                .build();

        System.out.println(pf);
    }
}
