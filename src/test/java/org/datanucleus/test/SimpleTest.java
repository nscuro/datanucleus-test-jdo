package org.datanucleus.test;

import org.datanucleus.util.NucleusLogger;
import org.junit.Test;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.jdo.Transaction;
import java.util.List;
import java.util.StringJoiner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class SimpleTest
{

    public static class QueryResult {
        public String name;
        public String definition;
        public String tableName;
        public String columnName;

        @Override
        public String toString() {
            return new StringJoiner(", ", QueryResult.class.getSimpleName() + "[", "]")
                    .add("name='" + name + "'")
                    .add("definition='" + definition + "'")
                    .add("tableName='" + tableName + "'")
                    .add("columnName='" + columnName + "'")
                    .toString();
        }
    }

    @Test
    public void testSimple()
    {
        NucleusLogger.GENERAL.info(">> test START");
        PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory("MyTest");

        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try
        {
            String sqlQuery = "SELECT CC.CONSTRAINT_NAME AS \"name\" " +
                              "     , CC.CHECK_CLAUSE AS \"definition\" " +
                              "     , CCU.TABLE_NAME AS \"tableName\" " +
                              "     , CCU.COLUMN_NAME AS \"columnName\" " +
                              "  FROM INFORMATION_SCHEMA.CONSTRAINT_COLUMN_USAGE AS CCU " +
                              " INNER JOIN INFORMATION_SCHEMA.CHECK_CONSTRAINTS AS CC " +
                              "    ON CC.CONSTRAINT_SCHEMA = CCU.CONSTRAINT_SCHEMA " +
                              "   AND CC.CONSTRAINT_NAME = CCU.CONSTRAINT_NAME " +
                              " WHERE CCU.TABLE_NAME = 'PERSON'";
            Query<?> query = pm.newQuery(Query.SQL, sqlQuery);
            List<QueryResult> results = query.executeResultList(QueryResult.class);

            assertEquals("Expected a single check constraint, but got: " + results, 1, results.size());
        }
        catch (Throwable thr)
        {
            NucleusLogger.GENERAL.error(">> Exception in test", thr);
            fail("Failed test : " + thr.getMessage());
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }

        pmf.close();
        NucleusLogger.GENERAL.info(">> test END");
    }
}
