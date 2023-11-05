package org.datanucleus.test;

import mydomain.model.Person;
import org.datanucleus.util.NucleusLogger;
import org.junit.Assert;
import org.junit.Test;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Transaction;
import java.util.LinkedList;
import java.util.List;

public class SimpleTest
{
    @Test
    public void testSimple()
    {
        NucleusLogger.GENERAL.info(">> test START");
        PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory("MyTest");

        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();

            final Person person = new Person();
            person.setName("John Doe");
            pm.makePersistent(person);

            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }

        try {
            tx.begin();

            // Fetch list of persons to force bulk-fetch of properties collection.
            // Because no properties were assigned, DataNucleus will assign an empty
            // ArrayList instance to the properties field.
            // https://github.com/datanucleus/datanucleus-rdbms/commit/7fc3a65c96eb8913c3e2fe5745c668c57328e0e1
            final List<Person> persons = pm.newQuery(Person.class).executeList();

            Assert.assertEquals(1, persons.size());

            // Assigning a list implementation other than ArrayList will cause a ClassCastException, e.g.:
            //   java.lang.ClassCastException: class java.util.LinkedList cannot be cast to class java.util.ArrayList

            // This works:
            //   persons.get(0).setProperties(new ArrayList<>());
            // But this doesn't:
            persons.get(0).setProperties(new LinkedList<>());

            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }

        pm.close();
        pmf.close();
        NucleusLogger.GENERAL.info(">> test END");
    }
}
