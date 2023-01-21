# Basic

Thread Safe한 상황이 아닌 상태입니다.

# Main

main에서 Client를 1000개 생성하여 호출을 하였습니다.

```java
public class Main {
    public static void main(String[] args) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        ExecutorService es = Executors.newFixedThreadPool(200);
        Board board = Board.getInstance();
        Client client = new Client();
        for (int i = 0; i < 1000; i++) {
            es.submit(client);
        }

        es.shutdown();
        es.awaitTermination(200, TimeUnit.SECONDS);

        long endTime = System.currentTimeMillis();
        System.out.println(board.getViewCount());
        System.out.println("Running Time : " + (endTime - startTime));

    }
}
```

위와 같이 호출할 경우
최종 Board의 조회수는 `1000`이 되어야 합니다.

하지만 다음과 같이 매번 다르게 나옵니다.

<img width="28" alt="스크린샷 2023-01-21 오후 4 08 26" src="https://user-images.githubusercontent.com/79268661/213848801-13d96a56-b017-4e1c-9421-ad310ee089c0.png">

<img width="28" alt="스크린샷 2023-01-21 오후 4 08 34" src="https://user-images.githubusercontent.com/79268661/213848805-a44dd682-e23b-42fa-869c-dd712f687434.png">

<img width="34" alt="스크린샷 2023-01-21 오후 4 08 54" src="https://user-images.githubusercontent.com/79268661/213848807-2081caca-4ccc-494e-acc7-e56348103d9c.png">



# 문제
> viewCount의 원자성 문제

문제는 `viewCount`가 ThreadSafe 하지 않기 때문입니다.
각 Thread가 CPU 스케쥴링에 따라 다르게 코드가 동작하여 어느 시점에 어떤 값에 각 Thread가 접근하였는지 모릅니다.

## 임계구역
> 임계 구역(critical section) 또는 공유변수 영역은 병렬컴퓨팅에서 둘 이상의 스레드가 동시에 접근해서는 안되는 공유 자원(자료 구조 또는 장치)을 접근하는 코드의 일부를 말한다. 

임계구역은 다음과 같습니다.

<img width="230" alt="스크린샷 2023-01-21 오후 4 23 17" src="https://user-images.githubusercontent.com/79268661/213848814-271c0e4c-1224-4652-9c18-eed5465dcd73.png">


> 위의 임계구역을 원자성을 유지하여 구현해보는것이 목표입니다.


# Running 시간

Running 시간은 다음과 같이 `97ms`가 걸린 것을 볼 수 있습니다.

