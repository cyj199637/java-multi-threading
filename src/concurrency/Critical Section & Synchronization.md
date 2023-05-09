## Critical Section(임계 영역)
> 동시 실행되지 않게 보호해야 하는 코드가 있는 영역

<br/>

```java
void aggregateFunction() {
    entry section
    operation1();
    operation2();
    operation3();
    exit section
}
```
스레드 A가 임계 영역에 진입해 operation1()을 실행하고 있다면, 다른 스레드들은 스레드 A가 모든 작업을 마치고 임계 영역을 빠져나올 때까지 임계 영역에 접근하지 못한다.

-> 임계 영역 안에 작성된 코드는 한 번에 하나의 스레드만 진입하여 실행된다.

-> 임계 영역 안에 포함된 연산의 수와 상관없이 임계 영역으로 내의 연산들은 원자성이 확보된다.

<br/><br/>

## 동기화 방법
### synchronized
> 여러 개의 스레드가 코드 블록이나 전체 메서드에 액세스할 수 없도록 설계된 락킹 메커니즘

<br/>

<b> 사용 방법 1) 메소드에 선언 </b>

-> 메소드 전체를 동기화

```java
public class ClassWithCriticalSections {
    public synchronized void method1() {
        ...
    }

    public synchronized void method2() {
        ...
    }
}
```
스레드 A가 `ClassWithCriticalSections` 객체의 메소드 중 하나를 실행한다면 다른 스레드들은 동일한 객체에 대해서 `synchronized` 가 선언된 모든 메소드를 실행할 수 없다.

-> method1 과 method2 는 동일한 모니터락 객체를 공유하여 사용하고 있기 때문

<br/>

만약 아래와 같이 스레드 A와 스레드 B가 각각 다른 `ClassWithCriticalSections` 객체를 사용한다면 스레드 A와 스레드 B는 각자 자유롭게 `synchronized` 가 선언된 모든 메소드를 실행할 수 있다.
```java
public class Main {
    public static void main(String [] args) {
        ClassWithCriticalSections sharedObject1 = new ClassWithCriticalSections();
        ClassWithCriticalSections sharedObject2 = new ClassWithCriticalSections();
 
        Thread threadA = new Thread(() -> {
            while (true) {
                sharedObject1.method1();
            }
        });
 
        Thread threadB = new Thread(() -> {
            while (true) {
                sharedObject2.method2();
            }
        });
 
        thread1.start();
        thread2.start();
    }

    static class ClassWithCriticalSections {
        public synchronized void method1() {
            ...
        }

        public synchronized void method2() {
            ...
        }
    }
}
```

<br/>

<b> 사용 방법 2) 코드 블록으로 선언 </b>

-> 선언된 영역만 동기화

```java
public class ClassWithCriticalSections {
    // 락으로 사용할 객체는 어떤 객체든 가능
    Object lock = new Object();
    
    public void method1() {
        synchronized(lock) {
            ...
        }
    }

    public void method2() {
        synchronized(lock) {
            ...
        }
    }
}
```
위의 코드도 method1 과 method2 는 동일한 모니터락 객체를 공유하여 사용하고 있기 때문에 스레드 A가 `ClassWithCriticalSections` 객체의 메소드 중 하나를 실행한다면 다른 스레드들은 동일한 객체에 대해서 `synchronized` 가 선언된 코드 블록을 실행할 수 없다.

<br/>

```java
public class ClassWithCriticalSections {
    Object lock1 = new Object();
    Object lock2 = new Object();
    
    public void method1() {
        synchronized(lock1) {
            ...
        }
    }

    public void method2() {
        synchronized(lock2) {
            ...
        }
    }
}
```
위와 같이 `synchronized` 블록에서 사용하는 락 객체가 다르다면 스레드 A가 method1의 코드를 실행하고 있어도 다른 스레드들이 method2를 실행할 수 있다. 

<br/>

사실 메소드를 `synchronized` 로 선언하는 것은 아래 코드처럼 메소드 내의 전체 코드를 synchronized 블록으로 선언하는 것과 같다.
```java
public class ClassWithCriticalSections {
    Object lock = new Object();
    
    public void method1() {
        synchronized(this) {
            ...
        }
    }

    public void method2() {
        synchronized(this) {
            ...
        }
    }
}
```

메소드 일부에만 임계 영역이 포함된 경우 전체 메서드를 동기화할 필요가 없다.

동기화가 필요한 최소한의 코드만 synchronized 블록으로 정의하고 나머지는 동기화하지 않는게 좋다. 

그래야 더 많은 코드가 여러 스레드로 동시에 실행될 수 있기 때문이다.

<br/>

<b>synchronized의 재진입성</b>

`synchronized` 의 락은 재진입이 가능한 락이다.

즉, 스레드는 자신이 이미 소유하고 있는 락에 대해서 다시 획득할 수 있다는 것이다.

재진입이 가능한 락을 사용하면 아래와 같이 동일한 락 객체를 사용하여 동기화를 하는 코드를 같은 스레드가 호출해도 문제가 없다.

```java
public class ReentrantExample{

  public synchronized outer(){
      inner();
  }

  public synchronized inner(){
      ...
  }
}
```
만약 재진입이 불가능했다면 동일한 락에 대해 데드락이 발생할 것이다.