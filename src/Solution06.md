# 자바 문자 스트림 (Character Stream)

이 자료는 [Solution06.java](file:///Users/baegseungho/IdeaProjects/260626_ex/src/Solution06.java)의 코드를 바탕으로 자바의 문자 입출력(I/O) 스트림 개념을 정리한 문서입니다. 초심자용 가이드와 면접 대비용 내용으로 구성되어 있습니다.

---

## 1. 초심자용 가이드 (For Beginners)

### 🔠 문자 스트림(Character Stream)이란 무엇인가요?
바이트 스트림([Solution05.java](file:///Users/baegseungho/IdeaProjects/260626_ex/src/Solution05.java)에서 다룬 `FileInputStream` 등)이 데이터를 1바이트(8bits) 단위로 쪼개어 단순한 숫자로 주고받았다면, **문자 스트림**은 데이터를 **문자 단위(2바이트/유니코드)**로 묶어서 다룹니다.

컴퓨터에 저장되는 텍스트 파일은 기본적으로 바이트들의 연속이지만, 문자 스트림을 사용하면 자바가 중간에서 이를 문자(Character) 단위로 알아서 번역(인코딩/디코딩)하여 자바 프로그램에 안전하게 전달해 줍니다.

---

### 🎨 한글 처리 흐름 비교 (Mermaid)

바이트 스트림과 문자 스트림이 UTF-8로 작성된 한글 파일("안녕하세요")을 읽을 때 일어나는 차이를 나타낸 다이어그램입니다.

```mermaid
graph TD
    subgraph 바이트 스트림 (Solution05)
        F1[output-byte.txt <br/> 한글 1글자=3바이트] -->|1Byte씩 읽음| B1[FileInputStream]
        B1 -->|0xED만 캐스팅| C1[char '밭' 깨짐 발생]
    end

    subgraph 문자 스트림 (Solution06)
        F2[output-char.txt <br/> 한글 1글자=3바이트] -->|인코딩 기반 변환| B2[FileReader]
        B2 -->|3바이트를 조합하여 1문자로 복원| C2[char '안' 정상 출력]
    end
```

---

### 🔍 Solution06.java 코드 상세 분석

#### 1. 문자 스트림으로 파일 쓰기 (`FileWriter`)
```java
private static void saveByCharStream(String text, String charFileName) {
    try (FileWriter writer = new FileWriter(charFileName)) {
        writer.write(text); // 💡 String을 바이트 배열로 쪼개지 않고 통째로 전달 가능!
        System.out.println("문자 스트림 작성 완료");
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
}
```
* **동작**: `FileOutputStream`을 사용할 때는 `text.getBytes(...)`처럼 직접 바이트 배열로 변환하는 작업을 거쳐야 했으나, `FileWriter`는 자바 `String`을 그대로 인자로 받아 JVM의 기본 캐릭터셋(UTF-8)으로 변환해 파일에 작성해 줍니다.

#### 2. 문자 스트림으로 파일 읽기 (`FileReader`)
```java
private static void loadByCharStream(String charFileName) {
    try (FileReader reader = new FileReader(charFileName)) {
        int c;
        while ((c = reader.read()) != -1) { // 💡 바이트가 아닌 문자 코드 포인트를 반환
            System.out.print((char) c);   // 한글이 깨지지 않고 온전히 조합되어 출력됨
        }
        System.out.println();
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
}
```
* **동작**: `reader.read()`는 파일에서 1바이트가 아닌 **한 개의 유니코드 문자**를 읽어 반환합니다. 따라서 한글("안녕하세요")처럼 3바이트 크기를 가진 문자도 정상적으로 온전히 해독되어 `(char) c`로 캐스팅되었을 때 깨짐 없이 정상 출력됩니다.

---

### 📊 스트림별 특징 및 용도 표

| 구분 | 바이트 스트림 (Byte Stream) | 문자 스트림 (Character Stream) |
| :--- | :--- | :--- |
| **최상위 추상 클래스** | `InputStream`, `OutputStream` | `Reader`, `Writer` |
| **최하위 구현 클래스** | `FileInputStream`, `FileOutputStream` | `FileReader`, `FileWriter` |
| **적합한 파일 종류** | 이미지(`.jpg`), 동영상(`.mp4`), 압축파일(`.zip`), 실행파일(`.exe`) 등 | 텍스트 파일(`.txt`), 소스코드(`.java`, `.html`), 마크다운(`.md`) 등 |
| **데이터 처리 단위** | `byte` (8-bit) | `char` (16-bit 유니코드) |
| **I/O 단위의 텍스트 표현** | 문자의 경계(Encoding)를 직접 관리해야 함 | 인코딩 규칙에 맞추어 유효한 문자로 자동 결합/해석 |

---

## 2. 면접 대비용 가이드 (For Interview)

### 📌 Q1. `FileReader`와 `FileWriter`가 텍스트 파일(다국어 포함)을 깨지지 않고 입출력할 수 있는 내부 원리는 무엇인가요?
* **답변**: 자바의 `FileReader`와 `FileWriter`는 내부적으로 문자셋 디코더(Charset Decoder)와 인코더(Encoder)를 내장하고 있습니다. 
  * 파일로부터 데이터를 읽을 때는 스트림에 지정된 문자셋(예: UTF-8)의 바이트 표현 방식을 분석하여, 바이트들이 몇 바이트 결합체(예: UTF-8에서 한글은 3바이트, 영어는 1바이트)인지를 식별한 뒤 이에 대응하는 2바이트 크기의 Java 유니코드 문자(`char`)로 병합하여 반환합니다.
  * 반대로 쓸 때는 프로그램 상의 유니코드 문자들을 해당 파일의 문자셋 인코딩 바이트 스트림으로 자동 변환하여 기록합니다. 이처럼 인코딩 변환 작업이 추상화되어 있어 개발자가 입출력 시 인코딩 문제로부터 자유롭게 텍스트를 다룰 수 있게 됩니다.

---

### 📌 Q2. Java에서 파일 입출력 시 캐릭터셋(Charset)을 명시하지 않았을 때 발생할 수 있는 문제점과 해결 방안은 무엇인가요? (Platform Default Encoding 이슈)
* **답변**: 
  * **문제점**: Java 17 이하 버전에서는 `FileReader`나 `FileWriter`를 사용할 때 캐릭터셋을 명시하지 않으면 **실행 환경 OS의 기본 인코딩(Platform Default Encoding)**을 따르게 됩니다. 예를 들어 Mac/Linux 환경은 기본적으로 `UTF-8`이지만, 한글 Windows 환경은 `MS949(EUC-KR)`를 사용합니다. 이로 인해 개발 서버(Linux)에서는 한글 파일이 정상적으로 읽히지만 로컬 Windows PC에서는 한글이 깨지는 **이식성 문제**가 발생합니다.
  * **해결 방안 1 (Java 18 이후)**: Java 18 버전부터는 **JEP 400** 표준에 의해 OS 환경에 상관없이 JVM의 기본 캐릭터셋이 **`UTF-8`**로 통일되어 안정성이 향상되었습니다.
  * **해결 방안 2 (명시적 캐릭터셋 선언)**: 플랫폼 종속성을 완전히 차단하기 위해, 생성자 호출 시 아래와 같이 사용할 캐릭터셋을 명시해 주는 것이 업계 표준이자 베스트 프랙티스입니다.
    ```java
    // Java 11 이상 지원 문법
    try (FileReader reader = new FileReader(charFileName, StandardCharsets.UTF_8)) { ... }
    ```

---

### 📌 Q3. `BufferedReader`와 `BufferedWriter` 보조 스트림을 사용하면 텍스트 입출력 시 성능과 개발 편의성 측면에서 어떤 이점이 있나요?
* **답변**:
  * **성능 측면 (버퍼링)**: 1차 스트림인 `FileReader`는 `read()` 호출 시마다 운영체제의 시스템 콜을 호출하여 디스크에 물리적으로 직접 접근(1글자씩)하므로 오버헤드가 매우 큽니다. `BufferedReader`는 내부 메모리에 버퍼(보통 8192글자)를 두어, 디스크의 데이터를 버퍼에 미리 가득 채워둔 뒤 메모리 상에서 반환하므로 물리적인 I/O 횟수를 최소화하여 비약적인 성능 향상을 만들어냅니다.
  * **편의 기능**: 문자 스트림을 다룰 때 아주 유용한 라인 단위 입력 메서드인 **`readLine()`**을 지원하여 개행 문자(`\n`, `\r\n`)를 기준으로 파일의 내용을 한 줄 단위로 쉽게 파싱할 수 있습니다. `BufferedWriter` 또한 플랫폼 독립적인 줄바꿈을 지원하는 `newLine()` 메서드를 제공합니다.

---

### 📌 Q4. 바이트 기반 스트림에서 문자 기반 스트림으로 데이터를 전환해야 할 때 사용되는 자바의 다리(Bridge) 클래스는 무엇인가요?
* **답변**: 
  * 바이트 스트림을 문자 스트림으로 변환해주는 클래스는 **`java.io.InputStreamReader`**이며, 반대 방향(문자 -> 바이트)으로 변환해주는 클래스는 **`java.io.OutputStreamWriter`**입니다.
  * 이 클래스들은 바이트 기반의 스트림(`InputStream`, `OutputStream`)을 생성자 인자로 받아 문자 기반의 `Reader`/`Writer`처럼 사용할 수 있도록 장식해 주며, 이때 특정 인코딩 지정을 통해 원본 바이트 데이터를 타겟 캐릭터셋에 맞는 문자 데이터로 해독해 줍니다. 대표적으로 네트워크 소켓 통신(바이트 스트림)을 통해 수신한 문자열 데이터를 한 줄씩 가공할 때 유용하게 조합하여 쓰입니다.
