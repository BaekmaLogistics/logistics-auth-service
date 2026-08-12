# 📦 Auth Service

> Baekma Logistics의 인증 및 계정 관리를 담당하는 Microservice입니다

---

## 📌 담당 기능

* 승인 기반 회원가입 신청
* 아이디·비밀번호 기반 로그인
* JWT Access Token 및 Refresh Token 발급
* Refresh Token을 이용한 토큰 재발급
* 로그아웃 및 Refresh Token 폐기
* 회원가입 승인·거절 결과에 따른 인증 계정 상태 변경

---

## 🛠 Tech Stack

* **Language** : Java 17
* **Framework** : Spring Boot
* **Database** : PostgreSQL
* **Cache** : Redis
* **Communication** : OpenFeign

---

## ✨ 주요 구현 내용

### 1. 승인 기반 회원가입 프로세스

* 회원가입 시 비밀번호를 BCrypt로 암호화하고 인증 계정을 PENDING 상태로 생성했습니다.
* Auth 계정의 UUID를 사용자 ID로 사용하여 Auth와 User 서비스가 동일한 사용자를 식별하도록 구성했습니다.
* OpenFeign을 통해 User Service에 승인 대기 사용자 생성을 요청했습니다.


### 2. 로그인 및 JWT 발급
* Spring Security의 AuthenticationManager와 UserDetailsService를 이용해 아이디와 비밀번호를 인증했습니다.
* 승인된 ACTIVE 계정만 로그인할 수 있도록 계정 상태를 인증 과정에 반영했습니다.
* Access Token에는 사용자 ID, 아이디, 역할과 토큰 유형을 담고 Refresh Token에는 사용자 ID와 토큰 유형을 담아 용도를 분리했습니다.

### 3. 가입 심사 결과에 따른 인증 계정 변경
* User Service가 호출하는 내부 API를 통해 인증 계정을 활성화하거나 거절 상태로 변경합니다.
* 승인 시 역할을 부여하고 PENDING 계정을 ACTIVE로 변경합니다.
* 거절 시 역할을 부여하지 않고 계정을 REJECTED로 변경합니다.

---

## 💡 기술적 고민 및 해결

### MSA 환경에서 가입 심사 상태를 Auth와 User에 반영하는 방법

**문제**

* Auth Service는 인증 계정을, User Service는 사용자와 가입 심사 정보를 소유하므로 회원가입과 승인 결과가 두 서비스에 모두 반영되어야 했습니다.

**해결**

* 회원가입 시 Auth Service가 인증 계정을 먼저 생성하고 OpenFeign으로 User Service에 동일한 UUID의 승인 대기 사용자 생성을 요청하도록 구성했습니다.
* 가입 심사는 User Service가 담당하고, 승인 또는 거절 결과를 Auth Service의 내부 API로 전달하도록 책임을 분리했습니다.

**결과**

* 인증과 사용자 심사 책임을 서비스별로 분리하면서도 동일한 사용자 ID와 계정 상태를 기준으로 두 서비스가 연동되도록 구성했습니다.
* 승인되지 않은 계정의 로그인을 차단하고, 심사 결과에 따라 인증 가능 여부가 변경됩니다.

---

## 🚀 실행 방법

```bash
./gradlew bootRun
```

필요한 환경 변수 및 외부 인프라 설정은 프로젝트 공통 README를 참고해주세요.

---

## 🔗 Project

전체 프로젝트의 아키텍처, ERD, 서비스 구성 및 팀원 역할은 Organization README에서 확인할 수 있습니다.

👉 [Baekma Logistics](Organization README URL)(제가 README 추가 후에 수정해 놓겠습니다!)
