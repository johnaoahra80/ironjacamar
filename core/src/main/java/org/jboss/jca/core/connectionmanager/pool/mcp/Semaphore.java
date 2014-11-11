/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.jca.core.connectionmanager.pool.mcp;

import org.jboss.jca.core.CoreLogger;
import org.jboss.jca.core.connectionmanager.listener.ConnectionListener;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * A semaphore implementation that supports statistics
 *
 * @author <a href="mailto:jesper.pedersen@jboss.org">Jesper Pedersen</a>
 */
public class Semaphore extends java.util.concurrent.Semaphore
{
    /** The log */
//    private CoreLogger log;
    private Logger log = Logger.getLogger(Semaphore.class.getName());

    /** Serial version uid */
   private static final long serialVersionUID = 1L;

   /** Statistics */
   private ManagedConnectionPoolStatisticsImpl statistics;

   private Map<ConnectionListener, SemaphoreConcurrentLinkedQueueManagedConnectionPool.ConnectionListenerWrapper> cls;

    private static String poolName;

    private static int maxSize;

    private static char[] msg_1 = "Semaphore stats - (".toCharArray();
    private static char[] msg_2 = "); QueueLength: ".toCharArray();
    private static char[] msg_3 = "; AvailablePermits: ".toCharArray();
    private static char[] msg_4 = "; clsAquired: ".toCharArray();
    private static char[] msg_5 = ";".toCharArray();

    /**
    * Constructor
    * @param maxSize The maxumum size
    * @param fairness The fairness
    * @param statistics The statistics module
    */
//   public Semaphore(int maxSize, boolean fairness, ManagedConnectionPoolStatisticsImpl statistics,
//                    Map<ConnectionListener, SemaphoreConcurrentLinkedQueueManagedConnectionPool.ConnectionListenerWrapper> cls, CoreLogger log, String poolName)
   public Semaphore(int maxSize, boolean fairness, ManagedConnectionPoolStatisticsImpl statistics)
   {
      super(maxSize, fairness);
       this.maxSize = maxSize;
      this.statistics = statistics;
//       this.cls = cls;
//       this.poolName = poolName;
//       this.log = log;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean tryAcquire(long timeout, TimeUnit unit) throws InterruptedException
   {
      statistics.setMaxWaitCount(getQueueLength());
//      if (this.cls!=null && log != null) {
//          int i = 0;
//          for(Map.Entry<ConnectionListener, SemaphoreConcurrentLinkedQueueManagedConnectionPool.ConnectionListenerWrapper> clw: cls.entrySet()){
//              if(clw.getValue().hasPermit()) i++;
//          }
//          int curSize = super.availablePermits() + i;
//          if( curSize > this.maxSize) logSemphStats(i);
//      }
      return super.tryAcquire(timeout, unit);
   }

    private void logSemphStats(int permits) {
        StringBuilder sempLog = new StringBuilder();
        sempLog.append(msg_1);
        sempLog.append(this.poolName);
        sempLog.append(msg_2);
        sempLog.append(super.getQueueLength());
        sempLog.append(msg_3);
        sempLog.append(super.availablePermits());
        sempLog.append(msg_4);
        sempLog.append(permits);
        sempLog.append(msg_5);

        log.warning(sempLog.toString());
    }

}
