# Mutex

뮤텍스는 스핀락의 원리와 비슷합니다. lock을 얻은 스레드만이 임계구역에 접근을 할 수 있습니다. 스핀락과의 다른점으로는 다음과 같습니다.

> lock을 얻지 못한 스레드는 대기상태로 빠지게 됩니다.

# 어떠한 이점이 있는가?

쓰레드를 대기상태로 빠지게하면 다음과 같은 효과가 있습니다.

* 스핀락과는 반대로 While문이 계속해서 실행 중이지 않아서 불필요한 cpu 자원을 얻지않는다.
* 쓰레드가 대기상태로 빠지기 때문에, Context Switching 비용이 발생하지 않는다.

그리하여 결과를 보면 다음과 같습니다.

1. 최초에는 쓰레드가 계속해서 잠들게 됩니다.

<img width="29" alt="스크린샷 2023-01-24 오후 2 26 37" src="https://user-images.githubusercontent.com/79268661/214218559-983dc031-ab31-4456-9b80-d473bdcc3bd5.png">

2. 최초의 current thread가 작업을 끝내게되면 잠든 쓰레드가 일어나고, 다시 lock을 얻지못한 쓰레드들은 잠들게됩니다.
<img width="38" alt="스크린샷 2023-01-24 오후 2 26 46" src="https://user-images.githubusercontent.com/79268661/214218630-4e3e964a-a4d7-4ce2-a19e-d9c785e0ada9.png">

3. 최종적으로 모든 쓰레드가 완료되고, 메인 쓰레드가 종료됩니다.
<img width="167" alt="스크린샷 2023-01-24 오후 2 26 54" src="https://user-images.githubusercontent.com/79268661/214218671-b96657fb-ac16-4242-80ea-cd383d85a257.png">



스핀락과는 반대로 큰 영향을 받을 정도의 성능차이는 일어나지 않지만 실제 배포 상황에서는 상당히 큰 효과를 얻을 수 있는 경우가 많습니다.

# 핵심 로직

```java
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
```

위의 로직은 thread가 lock을 얻었을 경우 current thread를 자기 자신으로 등록해둡니다.

lock을 얻지못한 다른 thread는 current thread를 기다리게 됩니다.

> 실제로 join은 다음과 같은 로직으로 구성되어있습니다.

```java
public final synchronized void join(final long millis)
    throws InterruptedException {
        if (millis > 0) {
            if (isAlive()) {
                final long startTime = System.nanoTime();
                long delay = millis;
                do {
                    wait(delay);
                } while (isAlive() && (delay = millis -
                        TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime)) > 0);
            }
        } else if (millis == 0) {
            while (isAlive()) {
                wait(0);
            }
        } else {
            throw new IllegalArgumentException("timeout value is negative");
        }
    }
```

위의 로직은 일정 시간 대기를하며 지속해서 lock을 얻은 current thread가 살아있는지 확인을 하는 로직이 포함되어 있습니다.

실제로는 스핀락과 매우 흡사한 형태의 코드를 가지고 있습니다. 이럴 경우 스핀락과 마찬가지로 cpu 자원을 사용하게 됩니다. 또한 context switching이 발생하기 때문에 성능상 좋지못합니다.

그리하여 추 후에, 실제 thread를 직접 notify해주는 로직으로 수정해주어야 합니다.

