package lock;

import java.util.concurrent.atomic.AtomicInteger;

public class SpinLock {

    private volatile int lock = 0;

    public void acquire(){
        while(true){
            synchronized (this){
                if(lock == 0){
                    lock = 1;
                    break;
                }
            }

        }
    }

    public void release(){
        this.lock = 0;
    }
}
