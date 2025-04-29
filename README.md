![header](https://capsule-render.vercel.app/api?type=Waving&color=auto&height=300&section=header&text=delivery-api&fontSize=70&desc=Spring%20Boot를%20활용한%20배달%20앱%20프로젝트입니다.&descAlignY=30)

<br/>

# 🔗 목차

1. [📆 프로젝트 소개](#-프로젝트-소개)
2. [🕰️ 개발 기간](#%EF%B8%8F-개발-기간)
3. [📚 기술 스택](#-기술-스택)
4. [🌳 디렉토리 구조](#-디렉토리-구조)
5. [⚠️ 빌드 전 확인사항](#%EF%B8%8F-프로젝트-빌드-전-확인사항)
6. [📝 프로젝트 명세](#-프로젝트-명세)

   6-1. [ERD](#erd)

   6-2. [API 명세서](#api-명세서)

<br/>

# 🧮 프로젝트 소개

1️⃣ 프로필 관리

- 프로필 조회 기능
- 프로필 수정 기능

2️⃣ 뉴스피드 게시물 관리

- 게시물 작성, 조회, 수정, 삭제 기능
- 뉴스피드 조회 기능

3️⃣ 사용자 인증

- 회원가입 기능
- 회원탈퇴 기능

4️⃣ 친구 관리

- 특정 사용자 친구 추가/삭제 기능
- 상대방의 수락 기능

🆙 댓글 기능

- 댓글 작성, 조회, 수정, 삭제 기능

🆙 좋아요 기능

- 댓글/게시글 좋아요 기능

<br/>

Spring Boot와 JPA를 활용한 간단한 배달 앱의 백엔드 API를 구현한 프로젝트입니다.

Front-end는 구현하지 않고 데이터 통신과 DB와의 연동 위주로 작성되었습니다.

<br/>

# 🕰️ 개발 기간

- 2025.04.22 ~ 2025.04.29 (총 6일)

<br/>

# 📚 기술 스택

### Language

[![My Skills](https://skillicons.dev/icons?i=java)](https://skillicons.dev)

### Backend

[![My Skills](https://skillicons.dev/icons?i=spring)](https://skillicons.dev)

### Database

[![My Skills](https://skillicons.dev/icons?i=mysql)](https://skillicons.dev)

### Development Tools

[![My Skills](https://skillicons.dev/icons?i=idea,postman)](https://skillicons.dev)

### SCM

[![My Skills](https://skillicons.dev/icons?i=git,github)](https://skillicons.dev)

### Communication

[![My Skills](https://skillicons.dev/icons?i=notion)](https://skillicons.dev)

<br/>

# 🌳 디렉토리 구조

```
src/main/java/com/example/springrider
├── SpringRiderApplication.java
├── aop/
├── config/
│   ├── filter/
│   └── interceptor/
├── domain/
│   ├── cart/
│   │   ├── controller/
│   │   ├── dto/
│   │   │   ├── request/
│   │   │   └── response/
│   │   ├── entity/
│   │   ├── enums/
│   │   ├── repository/
│   │   └── service/
│   ├── common/
│   │   └── entity/
│   ├── menu/
│   │   ├── controller/
│   │   ├── dto
│   │   │   ├── request/
│   │   │   └── response/
│   │   ├── entity/
│   │   ├── repository/
│   │   └── service/
│   ├── order/
│   │   ├── controller/
│   │   ├── dto/
│   │   │   ├── request/
│   │   │   └── response/
│   │   ├── entity/
│   │   ├── enums/
│   │   ├── repository/
│   │   └── service/
│   ├── review
│   │   ├── controller/
│   │   ├── dto/
│   │   │   ├── request/
│   │   │   └── response/
│   │   ├── entity/
│   │   ├── enums/
│   │   ├── repository/
│   │   └── service/
│   ├── store
│   │   ├── controller/
│   │   ├── dto/
│   │   │   ├── request/
│   │   │   └── response/
│   │   ├── entity/
│   │   ├── enums/
│   │   ├── repository/
│   │   └── service/
│   └── user/
│       ├── controller/
│       ├── dto
│       │   ├── request/
│       │   └── response/
│       ├── entity/
│       ├── enums/
│       ├── repository/
│       └── service/
└── global/
    ├── exception/
    ├── handler/
    ├── response/
    ├── security/
    └── validation/
```

<br/>

# ⚠️ 프로젝트 빌드 전 확인사항

해당 프로젝트는 `dotenv` 로 환경변수 설정을 해야합니다.
```java
DB_URL=jdbc:mysql://localhost:3306/SpringRider
DB_USERNAME=your_username
DB_PASSWORD=your_password
```
`./src` 경로에 있는 `.env.example` 파일을 복사하고 `.env` 로 파일명을 수정한 뒤,<br/>
`DB_URL`, `DB_USERNAME`, `DB_PASSWORD` 값을 각자 환경에 맞도록 수정해야 합니다.

<br/>

# 📝 프로젝트 명세

### [ERD](https://github.com/xaehub/Spring-Rider/wiki/ERD)

### [API 명세서](https://github.com/xaehub/Spring-Rider/wiki)

<br/>
