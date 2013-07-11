package cryolite.mem;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MemoryPool {
	
	/**
	 * min size of one assignment
	 */
	private final int pageSize;
	
	/**
	 * number of pages can be assigned
	 */
	private final int capacity;
	
	/**
	 * number of pages already assigned
	 */
	private int assigned = 0;

    /** page index for malloc or free */
	private int index = 0;

    /** Main lock guarding all access */
    private final ReentrantLock lock;
    /** Condition for waiting takes */
    private final Condition notEmpty;
	
	/**
	 * a pool contain fix length pages
	 */
	private final byte[][] pool;
	
	public MemoryPool(long poolSize, int pageSize) {
		this.pageSize = pageSize;
		this.capacity = (int) (poolSize/pageSize);
		this.pool = new byte[capacity][];
		
        lock = new ReentrantLock();
        notEmpty = lock.newCondition();
	}
	
	public byte[] malloc() throws InterruptedException {
        final ReentrantLock lock = this.lock;
        lock.lockInterruptibly();
		try {
			if (assigned == capacity) {
				notEmpty.await();
			}
			
			// no page is in the pool
			if (index == 0) {
				++assigned;
				return new byte[pageSize];
			}
			// index != 0
			// some page in the pool
			assert index != 0;
			++assigned;
			return pool[--index];
		} finally {
			lock.unlock();
		}
	}
	
	public void free(byte[] page) {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
        	assert index <= capacity;
        	pool[index++] = page;
        	--assigned;
        	notEmpty.signal();
        } finally {
        	lock.unlock();
        }
	}
	
	public void recycle() {
        final ReentrantLock lock = this.lock;
        lock.lock();		
		try {
			final int poolLen = pool.length;
			for (int i = 0; i < poolLen; ++i) {
				pool[i] = null;
			}
			index = 0;
			assigned = 0;
		} finally {
			lock.unlock();
		}
	}

  public float usedPercantage() {
    final ReentrantLock lock = this.lock;
    lock.lock();
    try {
      return 1.0f * assigned / capacity;
    } finally {
      lock.unlock();
    }
  }
}
