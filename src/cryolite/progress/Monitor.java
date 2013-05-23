package cryolite.progress;

import java.util.PriorityQueue;
import cryolite.tuple.Tuple2;

public class Monitor implements Runnable {

	PriorityQueue<Event> events = new PriorityQueue<Event>();
	
	private Monitor() {}
	static Monitor _ = new Monitor();
	
	@Override
	public void run() {
		while(true) {
			// get a event
			Event e;
			synchronized(events) {
				if(events.isEmpty()) return;
				e = events.peek();
			}
			long now = System.currentTimeMillis();
			long due = e.getTime();
			Progress p = e.getProgress();
			if(due > now) {
				// wait a certain time
				synchronized(p) {
					try {
						p.wait(due-now);
					} catch (InterruptedException ie) { }
				}
			}
			if(System.currentTimeMillis() < due) {
				// we are notify by eventDone
				// no output this time
				continue;
			}
			
			// re enqueue this event
			synchronized(events) {
				// double check events is still there
				// if not, we ignore output
				if(!events.peek().equals(e)) continue;
				// output a event
				p.output();
				e.setTime(p.getDelay()+due);
				events.add(events.poll());
			}
		}
	}
	
	public void addEvent(Progress p) {
		synchronized(events) {
			if(events.isEmpty()) new Thread(this).start();
			
			events.offer(new Event(p, p.getDelay()+System.currentTimeMillis()));
		}
	}
	
	public void eventDone(Progress p) {
		synchronized(events) {
			events.remove(p);
		}

		synchronized(p) {
			p.notify();			
		}
	}

	class Event extends Tuple2<Progress, Long> implements Comparable<Event> {

		Event(Progress p, Long time) {
			super(p, time);
		}
		
		long getTime() {
			return super._2();
		}
		
		Progress getProgress() {
			return super._1();
		}
		
		void setTime(long t) {
			super._2(t);
		}

		@Override
		public int compareTo(Event e) {
			return (int) (this._2() - e._2());
		}
	}
}
