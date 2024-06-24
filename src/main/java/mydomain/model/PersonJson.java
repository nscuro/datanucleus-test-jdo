package mydomain.model;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(table = "PERSON_JSON")
public class PersonJson {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.NATIVE)
    private Long id;

    @Persistent
    @Column(name = "NAME")
    private String name;

    @Persistent
    @Column(name = "DATA", jdbcType = "CLOB", sqlType = "JSON")
    private String data;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getData() {
        return data;
    }

    public void setData(final String data) {
        this.data = data;
    }

}
