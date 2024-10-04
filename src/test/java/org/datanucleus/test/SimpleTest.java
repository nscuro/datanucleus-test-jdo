package org.datanucleus.test;

import org.datanucleus.PropertyNames;
import org.datanucleus.api.jdo.JDOPersistenceManager;
import org.datanucleus.flush.FlushMode;
import org.datanucleus.util.NucleusLogger;
import org.junit.Assert;
import org.junit.Test;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

public class SimpleTest
{
    @Test
    public void testSimple()
    {
        PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory("MyTest");

        try (JDOPersistenceManager pm = (JDOPersistenceManager) pmf.getPersistenceManager()) {
            Assert.assertNull(pm.getExecutionContext().getFlushMode());

            pm.setProperty(PropertyNames.PROPERTY_FLUSH_MODE, FlushMode.MANUAL.name());
        }

        try (JDOPersistenceManager pm = (JDOPersistenceManager) pmf.getPersistenceManager()) {
            Assert.assertNull(pm.getExecutionContext().getFlushMode()); // Fails
        }

        pmf.close();
        NucleusLogger.GENERAL.info(">> test END");
    }
}
