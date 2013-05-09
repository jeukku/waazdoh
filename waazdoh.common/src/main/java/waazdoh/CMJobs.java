/*******************************************************************************
 * Copyright (c) 2013 Juuso Vilmunen.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Juuso Vilmunen - initial API and implementation
 ******************************************************************************/
package waazdoh;

import java.util.LinkedList;
import java.util.List;

import waazdoh.cutils.MLogger;

public class CMJobs {
	private static final int MAX_UNNEEDED = 2;
	private static final int MAX_RUNNERS = 4;
	private static final int MAX_JOBS = 10;

	private List<MJob> jobs = new LinkedList<MJob>();
	private List<RunnerThread> runners = new LinkedList<RunnerThread>();
	private boolean running;
	private MLogger log = MLogger.getLogger(this);
	private ThreadGroup tgrunners = new ThreadGroup("Job runners");
	private Thread managementthread;

	public void addJob(MJob runnable) {
		synchronized (jobs) {
			log.info("addding job " + runnable + " jobs.size:" + jobs.size());
			while (jobs.size() > MAX_JOBS) {
				log.info("Job queue full. Waiting.");
				try {
					checkManagement();			
					jobs.wait(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
					log.error(e);
				}
			}
			jobs.add(runnable);
			jobs.notifyAll();
			//
			checkManagement();			
		}
	}

	public CMJobs() {
		start();
	}

	private void checkManagement() {
		if (managementthread == null) {
			start();
		} else if (!managementthread.isAlive()) {
			start();
		}
	}

	private void start() {
		if (!running || managementthread == null) {
			running = true;
			managementthread = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						try {
							runRunners();
						} catch (InterruptedException e) {
							e.printStackTrace();
							log.error(e);
						}
					} finally {
						managementthread = null;
					}
				}
			}, "Jobs management");
			managementthread.start();
		}
	}

	public void stop() {
		running = false;
	}

	private void runRunners() throws InterruptedException {
		running = true;
		//
		while (running) {
			synchronized (jobs) {
				if (jobs.size() > 0) {
					synchronized (runners) {
						if (runners.size() == 0 && runners.size() < MAX_RUNNERS
								&& tgrunners.activeCount() < MAX_RUNNERS) {
							createNewRunner();
						}
						if (jobs.size() > 0 && runners.size()>0) {
							RunnerThread t;
							t = runners.remove(0);

							MJob job;
							job = jobs.remove(0);
							t.setJob(job);
						}
					}
				} //
				if (jobs.size() == 0) {
					synchronized (runners) {
						if (runners.size() > MAX_UNNEEDED) {
							runners.remove(0).stop();
						}
					}
					jobs.wait(1000);
				}
			}
		}
	}

	private void createNewRunner() {
		synchronized (runners) {
			RunnerThread runner = new RunnerThread();
			runners.add(runner);
			log.info("created a new runner. Runner count now " + runners.size()
					+ " tg:" + tgrunners.activeCount());
		}
	}

	private class RunnerThread {
		private MJob job;
		private boolean runnerstopped;

		private void runnerRun() throws InterruptedException {
			while (running && !runnerstopped) {
				if (job != null) {
					log.info("running job " + job);
					boolean done = job.run();
					if (!done) {
						synchronized (jobs) {
							jobs.add(job);
						}
					}
					job = null;
					synchronized (runners) {
						runners.add(this);
					}
				} else {
					synchronized (jobs) {
						jobs.wait(2000);
					}
				}
			}
		}

		public void stop() {
			runnerstopped = true;
		}

		public void setJob(MJob job2) {
			if (this.job != null) {
				throw new RuntimeException("already has a job");
			}

			this.job = job2;
			synchronized (jobs) {
				jobs.notify();
			}

		}

		public RunnerThread() {
			Thread t = new Thread(tgrunners, new Runnable() {
				@Override
				public void run() {
					try {
						runnerRun();
					} catch (InterruptedException e) {
						log.error(e);
					}
				}
			}, "Jobs runner");
			t.start();
		}
	}
}
