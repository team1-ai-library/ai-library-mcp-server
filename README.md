# ai-library-mcp-server

도서관 정보나루 Open API를 AI 모델이 직접 활용할 수 있도록 MCP(Model Context Protocol) 도구로 노출하는 Spring Boot 서버입니다.

---

## 프로젝트 개요

| 항목 | 내용 |
|------|------|
| Group ID | `com.nhnacademy` |
| Artifact ID | `data4library` |
| Java | 21 |
| Spring Boot | 3.5.15 |
| Spring AI BOM | 2.0.0 |
| MCP 프로토콜 | SSE (Server-Sent Events) |
| 서버 포트 | 8081 |

외부 API로 [도서관 정보나루](http://data4library.kr/api) (`http://data4library.kr/api`)를 사용합니다.

---

## 아키텍처

```
AI 모델
  │  MCP (SSE)
  ▼
BookTools / LibraryTools   ← MCP Tool 진입점
  │
  ▼
BookService / LibraryService  ← 비즈니스 로직, Utility 변환
  │
  ▼
NaruApiClient              ← 도서관 정보나루 REST API 호출
  │
  ▼
http://data4library.kr/api
```

### 패키지 구조

```
src/main/java/com/nhnacademy/data4library/
├── api/
│   └── NaruApiClient.java          # 정보나루 API HTTP 클라이언트
├── config/
│   ├── McpConfig.java              # MCP Tool 등록
│   └── RestClientConfig.java       # RestClient Bean 설정
├── dto/
│   ├── naru/                       # 정보나루 API 응답 DTO
│   └── response/                   # MCP Tool 반환 DTO
├── exception/
│   ├── ErrorCode.java              # 에러 코드 열거형
│   └── NaruApiException.java       # 커스텀 예외
├── properties/
│   └── NaruApiProperties.java      # API Key, Base URL 바인딩
├── service/
│   ├── BookService.java            # 도서 관련 서비스
│   └── LibraryService.java         # 도서관 관련 서비스
├── tool/
│   ├── BookTools.java              # 도서 MCP Tool
│   └── LibraryTools.java           # 도서관 MCP Tool
└── util/
    ├── DateConverter.java          # 날짜 표현 변환
    ├── RegionCodeConverter.java    # 지역명 → 지역 코드 변환
    └── RecommendTypeConverter.java # 추천 타입 변환
```

---

## MCP Tools

### BookTools — 도서 관련 Tool

| Tool 메서드 | 설명 | 파라미터 |
|------------|------|----------|
| `searchBooks` | 도서 제목 키워드로 전국 도서 검색 (최대 5건) | `title` |
| `searchBookDetail` | ISBN13으로 도서 상세 정보 조회 | `isbn13` |
| `searchHotBook` | 특정 기간·지역의 인기 대출 도서 조회 (최대 5건) | `startDate`, `endDate`, `region` |
| `searchHotTrendBooks` | 전주 대비 이번 주 대출 급상승 도서 조회 (최대 5건) | `searchDate` |
| `searchRecommendBooks` | 특정 도서 기준 연관 도서 추천 (최대 5건) | `isbn13`, `type` |

### LibraryTools — 도서관 관련 Tool

| Tool 메서드 | 설명 | 파라미터 |
|------------|------|----------|
| `searchLibraries` | 지역명으로 도서관 목록 검색 | `region` |
| `searchAvailableLibraries` | 특정 도서를 대출 가능한 도서관 검색 (병렬 처리) | `isbn13`, `region` |

#### `searchAvailableLibraries` 동작 방식

1. `searchLibrariesByBooks(isbn13, region)` — 해당 도서를 소장한 도서관 목록 조회
2. 각 도서관에 대해 `checkBookExists(libCode, isbn13)` 를 `CompletableFuture.supplyAsync` 로 병렬 실행
3. 대출 가능한 도서관만 필터링하여 반환

---

## 정보나루 API 엔드포인트 매핑

| NaruApiClient 메서드 | API 경로 | 설명 |
|---------------------|----------|------|
| `searchBooks` | `/srchBooks` | 도서 제목 검색 |
| `searchLibraries` | `/libSrch` | 지역 도서관 검색 |
| `checkBookExists` | `/bookExist` | 도서관 소장·대출 가능 여부 확인 |
| `searchLibrariesByBooks` | `/libSrchByBook` | 도서 소장 도서관 검색 |
| `searchBookDetail` | `/srchDtlList` | 도서 상세 정보 |
| `searchHotBooksByDateAndRegion` | `/loanItemSrch` | 인기 대출 도서 |
| `searchHotTrendBooks` | `/hotTrend` | 대출 급상승 도서 |
| `searchRecommendBooks` | `/recommandList` | 연관 도서 추천 |

모든 API 호출은 응답을 먼저 에러 여부로 파싱한 뒤, 실제 타입으로 역직렬화하는 2단계 방식으로 처리됩니다.

---

## Utility 클래스

### DateConverter

날짜 입력을 `yyyy-MM-dd` 형식으로 정규화합니다.

| 입력 형식 | 예시 |
|----------|------|
| 자연어 표현 | `오늘`, `내일`, `모레`, `글피`, `어제` |
| 절대 날짜 | `2026-06-30`, `20260630`, `2026/06/30` |

파싱에 실패하면 오늘 날짜를 반환합니다.

### RegionCodeConverter

한국어 지역명을 정보나루 API 지역 코드로 변환합니다.

| 지역 | 코드 | 지역 | 코드 |
|------|------|------|------|
| 서울 / 서울특별시 | 11 | 경기 / 경기도 | 31 |
| 부산 / 부산광역시 | 21 | 강원 / 강원특별자치도 | 32 |
| 대구 / 대구광역시 | 22 | 충북 / 충청북도 | 33 |
| 인천 / 인천광역시 | 23 | 충남 / 충청남도 | 34 |
| 광주 / 광주광역시 | 24 | 전북 / 전북특별자치도 | 35 |
| 대전 / 대전광역시 | 25 | 전남 / 전라남도 | 36 |
| 울산 / 울산광역시 | 26 | 경북 / 경상북도 | 37 |
| 세종 / 세종특별자치시 | 29 | 경남 / 경상남도 | 38 |
|  |  | 제주 / 제주특별자치도 | 39 |

지원하지 않는 지역명이나 빈 값 입력 시 `NaruApiException` 을 던집니다.

### RecommendTypeConverter

추천 타입 입력을 API 값으로 변환합니다.

| 입력 | 변환 결과 |
|------|----------|
| `마니아`, `mania` | `mania` |
| `다독자`, `다독`, `reader` | `reader` |
| `null`, 공백, 알 수 없는 값 | `mania` (기본값) |

---

## 예외 처리

`NaruApiException` 은 `RuntimeException` 을 상속하며, `ErrorCode` 와 메시지를 함께 가집니다.

| ErrorCode | 설명 |
|-----------|------|
| `NOT_FOUND` | 조회 결과 없음 |
| `PARSE_ERROR` | JSON 파싱 실패 |
| `INVALID_PARAM` | 잘못된 파라미터 |
| `NOT_SUPPORTED` | 지원하지 않는 값 (지역, 타입 등) |
| `API_ERROR` | API 호출 실패 |
| `NARU_API_ERROR` | 정보나루 API 자체 오류 응답 |

---

## 환경 변수 설정

프로젝트 루트에 `.env` 파일을 생성하고 아래 값을 설정합니다. (`springboot3-dotenv` 라이브러리로 자동 로드)

```
NARU_API_KEY=<도서관 정보나루 API 인증키>
```

도서관 정보나루 API 키는 [data4library.kr](http://data4library.kr) 에서 발급받을 수 있습니다.

---

## 빌드 및 실행

```bash
# 빌드 (테스트 포함)
./mvnw clean package

# 실행
./mvnw spring-boot:run
```

서버가 정상 기동되면 `http://localhost:8081` 에서 MCP SSE 엔드포인트가 활성화됩니다.

---

## 테스트

JUnit 5 + Mockito 기반 단위 테스트가 작성되어 있습니다.

| 테스트 클래스 | 대상 |
|-------------|------|
| `BookServiceTest` | `BookService` 전체 메서드 |
| `LibraryServiceTest` | `LibraryService` 전체 메서드 |
| `DateConverterTest` | `DateConverter.parseAndFormat` |
| `RegionCodeConverterTest` | `RegionCodeConverter.convert` |
| `RecommendTypeConverterTest` | `RecommendTypeConverter.convert` |

테스트 커버리지 리포트는 `mvn package` 실행 후 `target/site/jacoco/index.html` 에서 확인할 수 있습니다.

```bash
./mvnw test
```

---

## 주요 의존성

| 라이브러리 | 버전 | 용도 |
|-----------|------|------|
| spring-boot-starter-web | 3.5.15 | 웹 서버 |
| spring-ai-starter-mcp-server-webmvc | 2.0.0 | MCP 서버 |
| lombok | - | 코드 생성 |
| springboot3-dotenv | 5.1.0 | `.env` 파일 로드 |
| jacoco-maven-plugin | 0.8.12 | 코드 커버리지 |
| maven-surefire-plugin | 3.5.5 | 테스트 리포트 |