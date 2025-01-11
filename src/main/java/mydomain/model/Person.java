package mydomain.model;

import javax.jdo.annotations.*;

@PersistenceCapable(detachable="true")
public class Person
{
    @PrimaryKey
    Long id;

    String name;

    @Extension(vendorName = "datanucleus", key = "enum-check-constraint", value = "true")
    Gender gender;

    public Person(long id, String name, Gender gender)
    {
        this.id = id;
        this.name = name;
        this.gender = gender;
    }

    public Long getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public Gender getGender() {
        return gender;
    }

}
