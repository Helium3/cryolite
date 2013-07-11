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
			long now, due;
			Progress p;
			synchronized(events) {
				if(events.isEmpty()) return;
				e = events.peek();
				now = System.currentTimeMillis();
				due = e.getTime();
				p = e.getProgress();
			}
			if(due > now) {
				// wait a certain time
				p.waitDelay(due-now);
			}
			
			// re enqueue this event
			synchronized(events) {
				// double check events is still there
				// if not, we ignore output
				if(events.isEmpty() || !events.peek().equals(e)) continue;
				// output a event
				p.progress();
				e.setTime(p.getDelay()+due);
				events.add(events.poll());
			}
		}
	}
	
	public void addEvent(Progress p) {
		synchronized(events) {
			if(events.isEmpty()) {
				events.offer(new Event(p, p.getDelay()+System.currentTimeMillis()));
				Thread t = new Thread(this, "Monitor");
				t.setDaemon(true);
				t.start();
			} else {
				events.offer(new Event(p, p.getDelay()+System.currentTimeMillis()));
			}
		}
	}
	
	public void eventDone(Progress p) {
		synchronized(events) {
			for(Event e : events) {
				if(e.getProgress() == p) {
					events.remove(e);
					return;
				}
			}
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
