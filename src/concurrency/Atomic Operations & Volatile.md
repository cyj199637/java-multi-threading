## Atomic Operations(원자적 연산)
대부분의 연산은 비원자적 연산이다.

그 중에서 원자적 연산은 다음과 같다.

### 문자열, 배열, 객체 등 reference type 할당

```java

Object a = new Object();
Object b = new Object();

a = b;  // atomic



public int[] getNumbers() {  // atomic
    return this.numbers;
}

public void setName(String name) {  // atomic
    this.name = name;
}

public void setPerson(Person person) {  // atomic
    this.person = person;
}

```

레퍼런스 타입의 변수는 단일 연산을 통해 안전하게 변경할 수 있다.
  
레퍼런스 타입을 조회하거나(getter) 레퍼런스를 객체 변수에 할당하는(setter) 등의 작업을 원자적으로 수행하게 되어서 동기화시킬 필요가 없다.

<br/>

### long, double을 제외한 primitive type 할당
primitive type은 동기화할 필요 없이 안전하게 읽고 쓸 수 있다. 

  - long과 double만 예외인 이유
    - 두 타입의 값의 길이가 64 비트라 Java가 원자성을 보장을 해주지 않기 때문
    
      (volatile 키워드로 long / double 변수를 선언하면, 해당 변수에 대한 작업도 스레드 안전성을 지닌 원자적 연산이 된다.)
    - 64 비트 컴퓨터여도 long이나 double에 쓰기 작업을 하면 실제로는 CPU가 두 개 연산을 통해 완료할 가능성이 높아진다.

<br/>

### java.util.concurrent.atomic 패키지 내 클래스
해당 클래스들은 보통 비원자적으로 수행되는 연산들을 여러 방법으로 원자적으로 수행한다.
