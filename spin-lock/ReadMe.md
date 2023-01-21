# Spin Lock

앞선 `basics` 에서 구현에서 마주친 `원자성` 문제를 스핀락을 이용하여 해결해보았습니다.

# 핵심 로직

```java
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

```

* acquire : lock을 얻는 메소드
* release : lock을 반납하는 메소드

사용하는 로직은 다음과 같습니다.

```java
public void updateViewCount() {

        spinLock.acquire();

        // -- 해당 로직이 10ms 걸린다는 조건 --
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        this.viewCount++;

        spinLock.release();
    }
```

먼저, `acquire`을 이용하여 락을 획득합니다. 주어진 로직을 수행하고, `release`를 이용하여 락을 반납합니다.

이렇게 수행하면 다음과 같이 1000이라는 올바른 조회수를 획득할 수 있습니다.

# 문제점

조회수는 해결이 되었지만, 문제가 생겼습니다.

문제는 총 2가지가 존재합니다.

* while문을 이용한 지속한 polling 작업이 이루어진다. 그리하여 불필요한 cpu 버스트 타임 및 Context Switching이 이루어진다.

* 동시에 해당로직이 수행될 수 없기 때문에, 굉장히 많은 시간이 걸린다.



