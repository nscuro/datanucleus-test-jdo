package org.datanucleus.test;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports.Binding;
import mydomain.model.PersonJson;
import mydomain.model.PersonJsonb;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.postgresql.ds.PGSimpleDataSource;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import java.sql.Connection;
import java.sql.Statement;

public class SimpleTest {

    @Rule
    public PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:16-alpine")
            // Bind to a static host port, to match what we configured in persistence.xml.
            .withCreateContainerCmdModifier(cmd -> cmd.withPortBindings(new PortBinding(Binding.bindPort(5432), new ExposedPort(5432))));

    private PGSimpleDataSource dataSource;
    private PersistenceManagerFactory pmf;
    private PersistenceManager pm;

    @Before
    public void setUp() throws Exception {
        dataSource = new PGSimpleDataSource();
        dataSource.setUrl(postgresContainer.getJdbcUrl());
        dataSource.setUser(postgresContainer.getUsername());
        dataSource.setPassword(postgresContainer.getPassword());

        try (final Connection connection = dataSource.getConnection();
             final Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE \"PERSON_JSON\" (\"ID\" SERIAL PRIMARY KEY, \"NAME\" TEXT, \"DATA\" json)");
            stmt.execute("CREATE TABLE \"PERSON_JSONB\" (\"ID\" SERIAL PRIMARY KEY, \"NAME\" TEXT, \"DATA\" jsonb)");
        }

        pmf = JDOHelper.getPersistenceManagerFactory("MyTest");
        pm = pmf.getPersistenceManager();
    }

    @After
    public void tearDown() {
        if (pm != null) {
            pm.close();
        }
        if (pmf != null) {
            pmf.close();
        }
    }

    @Test
    public void testPersistJson() {
        final PersonJson person = new PersonJson();
        person.setName("foo");
        person.setData("{\"key\": \"value\"}");

        // javax.jdo.JDODataStoreException: Insert of object "mydomain.model.PersonJson@55e42449"
        // using statement "INSERT INTO "PERSON_JSON" ("DATA","NAME") VALUES (?,?)" failed :
        // ERROR: column "DATA" is of type json but expression is of type character varying
        pm.makePersistent(person);
    }

    @Test
    public void testPersistJsonb() {
        final PersonJsonb person = new PersonJsonb();
        person.setName("foo");
        person.setData("{\"key\": \"value\"}");

        // javax.jdo.JDODataStoreException: Insert of object "mydomain.model.PersonJsonb@6edcad64"
        // using statement "INSERT INTO "PERSON_JSONB" ("DATA","NAME") VALUES (?,?)" failed :
        // ERROR: column "DATA" is of type jsonb but expression is of type character varying
        pm.makePersistent(person);
    }

    @Test
    public void testFetchJson() throws Exception {
        try (final Connection connection = dataSource.getConnection();
             final Statement stmt = connection.createStatement()) {
            stmt.execute("INSERT INTO \"PERSON_JSON\" (\"NAME\", \"DATA\") VALUES ('foo', '{\"key\": \"value\"}')");
        }

        final Query<PersonJson> query = pm.newQuery(PersonJson.class, "name == 'foo'");
        PersonJson person = query.executeUnique();
        Assert.assertNotNull(person);
        Assert.assertEquals("{\"key\": \"value\"}", person.getData());
    }

    @Test
    public void testFetchJsonResult() throws Exception {
        try (final Connection connection = dataSource.getConnection();
             final Statement stmt = connection.createStatement()) {
            stmt.execute("INSERT INTO \"PERSON_JSON\" (\"NAME\", \"DATA\") VALUES ('foo', '{\"key\": \"value\"}')");
        }

        final Query<PersonJson> query = pm.newQuery(PersonJson.class, "name == 'foo'");
        query.setResult("data");
        String data = query.executeResultUnique(String.class);
        Assert.assertNotNull(data);
        Assert.assertEquals("{\"key\": \"value\"}", data);
    }

    @Test
    public void testFetchJsonb() throws Exception {
        try (final Connection connection = dataSource.getConnection();
             final Statement stmt = connection.createStatement()) {
            stmt.execute("INSERT INTO \"PERSON_JSONB\" (\"NAME\", \"DATA\") VALUES ('foo', '{\"key\": \"value\"}')");
        }

        final Query<PersonJsonb> query = pm.newQuery(PersonJsonb.class, "name == 'foo'");
        PersonJsonb person = query.executeUnique();
        Assert.assertNotNull(person);
        Assert.assertEquals("{\"key\": \"value\"}", person.getData());
    }

    @Test
    public void testFetchJsonbResult() throws Exception {
        try (final Connection connection = dataSource.getConnection();
             final Statement stmt = connection.createStatement()) {
            stmt.execute("INSERT INTO \"PERSON_JSONB\" (\"NAME\", \"DATA\") VALUES ('foo', '{\"key\": \"value\"}')");
        }

        final Query<PersonJsonb> query = pm.newQuery(PersonJsonb.class, "name == 'foo'");
        query.setResult("data");
        String data = query.executeResultUnique(String.class);
        Assert.assertNotNull(data);
        Assert.assertEquals("{\"key\": \"value\"}", data);
    }

}
