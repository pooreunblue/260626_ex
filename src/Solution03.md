# 자바의 자원 반납과 Try-with-resources

이 자료는 [Solution03.java](file:///Users/baegseungho/IdeaProjects/260626_ex/src/Solution03.java)에 구현된 자원 반납 방식의 진화 과정을 바탕으로 작성된 초심자용 가이드 및 면접 대비용 요약 자료입니다.

---

## 1. 초심자용 가이드 (For Beginners)

### 🔌 '자원(Resource)'이란 무엇이고, 왜 반납해야 하나요?
자바에서 `Scanner`, `InputStream`, `OutputStream`, `Connection`(데이터베이스 연결) 등은 시스템 내부의 파일이나 네트워크, 메모리 같은 **외부 자원(Resource)**을 다루는 도구들입니다.
자바 가비지 컬렉터(GC)는 자바 메모리는 자동으로 정리해 주지만, 이러한 **외부 시스템 자원**은 자동으로 닫아주지 못합니다. 
따라서 사용이 끝나면 반드시 `.close()` 메서드를 호출하여 자원을 수동으로 운영체제에 돌려주어야(반납해야) 합니다. 반납하지 않으면 시스템 메모리가 낭비되거나 파일이 계속 잠겨 있는 **자원 누수(Resource Leak)** 현상이 발생합니다.

---

### 📈 자원 반납 방식의 3단계 진화 과정

#### 1단계: try 블록 안에서 직접 닫기 (실패 예시)
```java
try {
    Scanner sc = new Scanner(System.in);
    int a = sc.nextInt(); // 여기서 에러가 발생한다면?
    sc.close(); // ❌ 에러 발생 시 이 라인에 도달하지 못해 자원이 누수됩니다!
} catch (Exception e) { ... }
```

#### 2단계: finally 블록에서 닫기 (과거의 해결책)
예외가 발생하든 안 하든 **무조건 실행되는 `finally` 블록**을 이용합니다.
```java
Scanner sc = null; // 스코프 문제 때문에 try 밖에 미리 null로 선언
try {
    sc = new Scanner(System.in);
    // 비즈니스 로직 실행
} catch (Exception e) { ... } 
finally {
    if (sc != null) { // ❌ NullPointerException 방지를 위해 null 체크 필수
        sc.close();
    }
}
```
* **문제점**: 코드가 지저분해지고 변수 스코프가 넓어지며, `finally` 안에서 `close()`를 호출할 때 추가 예외가 발생할 수 있어 이중 예외 처리 코드가 필요하게 됩니다.

#### 3단계: try-with-resources 문법 사용 (현대의 정석)
자바 7부터 도입된 가장 안전하고 깔끔한 방식입니다. `try` 괄호 `()` 안에 자원을 선언합니다.
```java
try (Scanner sc = new Scanner(System.in)) {
    int a = sc.nextInt();
    // 비즈니스 로직 실행
} catch (Exception e) { ... }
// 💡 catch 블록이나 try를 벗어나는 순간 자동으로 sc.close()가 안전하게 호출됨!
```

---

### 📊 자원 반납 흐름 비교 (Mermaid)

```mermaid
graph TD
    subgraph try-catch-finally (과거)
        A1[try 진입 및 자원 생성] --> A2[비즈니스 로직 실행]
        A2 -- 예외 발생 --> A3[catch 블록 실행]
        A2 -- 정상 종료 --> A4[finally 블록 실행]
        A3 --> A4
        A4 --> A5[close 명시적 호출 및 null 체크]
    end

    subgraph try-with-resources (현대)
        B1[try 괄호 안 자원 생성] --> B2[비즈니스 로직 실행]
        B2 -- 예외 발생 및 종료 --> B3[JVM이 암묵적으로 close 자동 호출]
        B3 --> B4[catch 블록 실행]
    end
```

---

## 2. 면접 대비용 가이드 (For Interview)

### 📌 Q1. `try-with-resources` 문법의 동작 원리와 대상 자원의 조건은 무엇인가요?
* **답변**: `try-with-resources`는 자바 컴파일러가 바이트코드를 빌드할 때, 프로그래머 대신 자동으로 `finally` 블록을 생성하고 `close()` 호출 및 예외 누락 방지 코드를 주입하는 구문 설탕(Syntactic Sugar)입니다.
* **조건**: 이 문법을 사용할 수 있는 자원 객체는 반드시 **`java.lang.AutoCloseable`** 인터페이스(혹은 이의 하위 인터페이스인 `java.io.Closeable`)를 구현하고 있어야 합니다. 이 인터페이스는 매개변수가 없고 `Exception`을 던질 수 있는 `close()` 메서드 단 하나만을 가지고 있습니다.
  ```java
  public interface AutoCloseable {
      void close() throws Exception;
  }
  ```

---

### 📌 Q2. 기존의 `try-catch-finally`와 비교했을 때 `try-with-resources`가 예외 처리 관점에서 갖는 치명적인 장점은 무엇인가요? (Suppressed Exception 관련)
* **답변**: 기존 `try-catch-finally`에서는 `try` 블록 안에서 예외(A)가 발생하고, 이후 `finally` 블록의 `close()` 과정에서도 또 다른 예외(B)가 발생하면, **최초의 실제 원인 예외(A)가 지워지고 마지막에 발생한 예외(B)만 호출자에게 전달되는 현상**이 발생합니다. 이를 예외 유실이라고 합니다.
* 반면, `try-with-resources`를 사용하면 실제 로직 중 발생한 예외(A)가 정상적으로 전파되며, 자원을 자동으로 닫는 동안 발생한 예외(B)는 버려지지 않고 최초 예외(A) 내부에 **`Suppressed(억제된) 예외`**로 기록되어 디버깅 시 모든 에러 이력을 온전히 확인할 수 있습니다.

---

### 📊 자원 반납 방식 특징 비교

| 비교 항목 | try-catch-finally | try-with-resources |
| :--- | :--- | :--- |
| **코드 가독성** | 자원 선언 및 null 체크, 이중 try-catch 등으로 지저분함 | `try()` 안에 선언하므로 한눈에 파악되고 깔끔함 |
| **자원 반납 보장** | 프로그래머가 실수로 `finally`를 누락하거나 `close()`를 안 쓰면 누수 발생 | 블록을 나가는 즉시 컴파일러 레벨에서 자동 반납 보장 |
| **예외 유실 문제** | `finally`에서 예외 발생 시 `try` 내부 원본 예외가 묻혀 유실됨 | 원본 예외가 보존되며 닫기 예외는 `Suppressed Exception`으로 누적됨 |
| **지원 버전** | JDK 1.0~ | JDK 7~ (Java 9부터는 기존 참조 변수도 사용 가능) |
| **필요 조건** | 없음 | 대상 클래스가 `AutoCloseable`을 구현해야 함 |
