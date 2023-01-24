package lock;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;

public class Mutex {

    private volatile AtomicBoolean lock = new AtomicBoolean(true);
    private volatile Thread currentThread;

    public void acquire() throws InterruptedException {

        while(true){
            if(lock.compareAndSet(true, false)){
                currentThread = Thread.currentThread();
                return;
            }else {
                System.out.println("잠듬");
                currentThread.join();
                System.out.println("일어남");
            }
        }
    }

    public void release(){
        this.lock.compareAndSet(false, true);
    }
}
