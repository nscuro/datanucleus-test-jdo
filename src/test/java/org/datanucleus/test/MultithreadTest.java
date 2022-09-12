package org.datanucleus.test;

import org.junit.Test;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MultithreadTest {

    @Test
    public void testMulti() throws InterruptedException, ExecutionException {
        final PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory("MyTest");

        final ExecutorService es = Executors.newFixedThreadPool(40);

        final List<TestCallable> tasks = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            tasks.add(new TestCallable(pmf));
        }

        final List<Future<Void>> futures = es.invokeAll(tasks);
        for (Future<Void> future : futures) {
            future.get();
        }
    }

    private static class TestCallable implements Callable<Void> {

        private final PersistenceManagerFactory pmf;

        TestCallable(final PersistenceManagerFactory pmf) {
            this.pmf = pmf;
        }

        @Override
        public Void call() {
            final PersistenceManager pm = pmf.getPersistenceManager();
            pm.close();
            return null;
        }

    }

}
