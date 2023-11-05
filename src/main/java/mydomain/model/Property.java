package mydomain.model;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class Property {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.NATIVE)
    private long id;

    @Persistent
    private String name;

    @Persistent
    private String value;

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

    public String value() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }
}
