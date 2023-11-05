package mydomain.model;

import javax.jdo.annotations.Element;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Join;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import java.util.List;

@PersistenceCapable
public class Person {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.NATIVE)
    private long id;

    @Persistent
    private String name;

    @Persistent(defaultFetchGroup = "true", table = "persons_properties")
    @Join(column = "person_id")
    @Element(column = "property_id")
    private List<Property> properties;

    public long id() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public String name() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public List<Property> properties() {
        return properties;
    }

    public void setProperties(final List<Property> properties) {
        this.properties = properties;
    }
}
