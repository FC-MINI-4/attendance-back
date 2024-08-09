
<div align="center">
  
  ![logo](https://github.com/jinyngg/attendance-back/assets/96164211/3290b91d-e1f8-4ffb-9a28-06e21048e521)

</div>

</br>

## 📚 프로젝트 (2023. 07. 24. ~ 2023. 08. 11.)

**순양그룹**의 연차 및 당직을 관리하는 프로젝트입니다.

</br>

## 👨‍👨‍👧‍👦 팀원

|![](https://avatars.githubusercontent.com/u/98010441?v=4)|![](https://avatars.githubusercontent.com/u/96164211?v=4)|![](https://avatars.githubusercontent.com/u/22290112?v=4)|
|:---:|:---:|:---:|
|[선예은](https://github.com/dpdmstjs)|[장진영](https://github.com/jinyngg)|[정준희](https://github.com/JoonheeJeong)|
|**클라이언트 페이지**</br>프로필 관리 API 개발</br>파일 업로드 API 개발|**클라이언트 페이지**</br>회원가입, 로그인 개발</br>인증/인가 API 개발</br>스프링 시큐리티 설정|**클라이언트 페이지**</br>연차/당직 API 개발</br>(조회, 등록, 수정, 취소)
|**관리자 페이지**</br>직원 관리 API 개발</br>연차, 당직 API 개발|**스케쥴러**</br>직급에 따른 연차 지급 스케쥴러 개발|**AWS 서버 설정**</br>서버 배포</br>서버 도메인 설정|


</br>

## 🛠 백엔드 기술 스택

<div align="center">

  ![기술스택](https://github.com/jinyngg/attendance-back/assets/96164211/8cd4f32e-1da4-4bea-9e5f-f5ac79c6929c)

</div>

<div align="center">
  
  <div>
    <img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
    <img src="https://img.shields.io/badge/spring Security-6DB33F?style=for-the-badge&logo=spring Security&logoColor=white">
    <img src="https://img.shields.io/badge/JPA-6DB33F?style=for-the-badge&logo=JPA&logoColor=white">
    <img src="https://img.shields.io/badge/mockito-25A162?style=for-the-badge&logo=&logoColor=white">
    <img src="https://img.shields.io/badge/Junit5-25A162?style=for-the-badge&logo=junit5&logoColor=white">
  </div>

  <div>
    <img src="https://img.shields.io/badge/SMTP-blue?style=for-the-badge&logo=&logoColor=white">
    <img src="https://img.shields.io/badge/Redis-red?style=for-the-badge&logo=Redis&logoColor=white">
    <img src="https://img.shields.io/badge/Lombok-red?style=for-the-badge&logo=&logoColor=white">
    <img src="https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white">
    <img src="https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white">
    <img src="https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=Swagger&logoColor=white">
  </div>

  <div>
    <img src="https://img.shields.io/badge/MySql-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
    <img src="https://img.shields.io/badge/H2-blue?style=for-the-badge&logo=&logoColor=white">
    <img src="https://img.shields.io/badge/QueryDsl-blue?style=for-the-badge&logo=&logoColor=white">
    <img src="https://img.shields.io/badge/AWS EC2-FF9900?style=for-the-badge&logo=amazon&logoColor=white">
    <img src="https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=Gradle&logoColor=white">
  </div>

</div>

</br>

## 💾 ERD 

<div align="center">

  ![image](https://github.com/jinyngg/attendance-back/assets/96164211/0f2d7060-5263-4ac4-a88f-33bc9fb2c020)

</div>

</br>

## 📝 테이블 설계

```sql
-- 직급(사원, 대리, 과장, 부장, 차장, 전무, 상무, 사장 등..)
CREATE TABLE position
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    type       VARCHAR(50) NOT NULL UNIQUE,
    created_at TIMESTAMP   NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP   NOT NULL DEFAULT NOW()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

```sql
-- 회원 상태(입사, 퇴사, 재입사, 휴직, 육아휴직, 복직 등..)
CREATE TABLE status
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    type       VARCHAR(20) NOT NULL UNIQUE,
    created_at TIMESTAMP   NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP   NOT NULL DEFAULT NOW()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

```sql
-- 부서(개발, 기획, 영업, 인사, 회계, 법무 등..)
CREATE TABLE department
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    type       VARCHAR(20) NOT NULL UNIQUE,
    created_at TIMESTAMP   NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP   NOT NULL DEFAULT NOW()
);
```

```sql
-- 연차(직급 별 지급하는 연차의 개수)
CREATE TABLE day_off_by_position
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    position_id BIGINT    NOT NULL UNIQUE,
    amount      TINYINT   NOT NULL,
    created_at  TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP NOT NULL DEFAULT NOW(),
    FOREIGN KEY (position_id) REFERENCES position (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

```sql
-- 연차(오전 반차, 오후 반차, 연차, 특별 휴가)
CREATE TABLE day_off
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    type        VARCHAR(20) NOT NULL UNIQUE,
    amount      FLOAT       NOT NULL,
    created_at  TIMESTAMP   NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP   NOT NULL DEFAULT NOW()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

```sql
-- 회원 테이블 생성
CREATE TABLE employee
(
    id                  BIGINT PRIMARY KEY AUTO_INCREMENT        COMMENT 'ID',
    auth_token          VARCHAR(36)   NOT NULL                   COMMENT '인증 토큰',
    position_id         BIGINT        NOT NULL                   COMMENT '직급 ID',
    department_id       BIGINT        NOT NULL                   COMMENT '부서 ID',
    status_id           BIGINT        NOT NULL                   COMMENT '회원상태 ID',
    name                VARCHAR(100)  NOT NULL                   COMMENT '이름',
    email               VARCHAR(100)  NOT NULL UNIQUE            COMMENT '이메일',
    password            CHAR(60)      NOT NULL COLLATE ascii_bin COMMENT '비밀번호',
    profile_image_path  VARCHAR(255)                             COMMENT '프로필 이미지', -- 추가된 칼럼
    hire_date           DATE          NOT NULL                   COMMENT '입사일',
    quit_date           DATE                                     COMMENT '퇴사일',
    day_off_remains     FLOAT         NOT NULL                   COMMENT '잔여 연차수',
    role                VARCHAR(20)   NOT NULL                   COMMENT '권한',
    phone               VARCHAR(60)   NOT NULL                   COMMENT '전화번호',
    birthdate           DATE                                     COMMENT '생년월일',
    zip_address         CHAR(5)                COLLATE ascii_bin COMMENT '우편번호',
    road_address        VARCHAR(255)                             COMMENT '주소1(도로명)',
    detail_address      VARCHAR(255)                             COMMENT '주소2(상세주소)',
    created_at          TIMESTAMP     NOT NULL DEFAULT NOW()     COMMENT '생성일',
    updated_at          TIMESTAMP     NOT NULL DEFAULT NOW()     COMMENT '수정일',
    last_login_at       TIMESTAMP     DEFAULT NOW()              COMMENT '최종 로그인',
    FOREIGN KEY (position_id) REFERENCES position (id),     -- position 테이블의 id 칼럼을 참조
    FOREIGN KEY (department_id) REFERENCES department (id), -- department 테이블의 id 칼럼을 참조
    FOREIGN KEY (status_id) REFERENCES status (id)          -- status 테이블의 id 칼럼을 참조;
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

```sql
CREATE TABLE position_history
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    employee_id BIGINT    NOT NULL,
    position_id BIGINT    NOT NULL,
    created_at  TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP NOT NULL DEFAULT NOW(),
    FOREIGN KEY (employee_id) REFERENCES employee (id),
    FOREIGN KEY (position_id) REFERENCES position (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

```sql
CREATE TABLE department_history
(
    id            BIGINT PRIMARY KEY AUTO_INCREMENT,
    employee_id   BIGINT    NOT NULL,
    department_id BIGINT    NOT NULL,
    created_at    TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at    TIMESTAMP NOT NULL DEFAULT NOW(),
    FOREIGN KEY (employee_id) REFERENCES employee (id),
    FOREIGN KEY (department_id) REFERENCES department(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 회원 상태 이력 테이블 생성
CREATE TABLE status_history
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    employee_id BIGINT    NOT NULL,
    status_id   BIGINT    NOT NULL,
    created_at  TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP NOT NULL DEFAULT NOW(),
    FOREIGN KEY (employee_id) REFERENCES employee (id),
    FOREIGN KEY (status_id) REFERENCES status (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

```sql
-- 연차 등록요청/응답 이력
CREATE TABLE day_off_history
(
    id               BIGINT PRIMARY KEY AUTO_INCREMENT,
    employee_id      BIGINT       NOT NULL,
    day_off_id       BIGINT       NOT NULL,
    status           VARCHAR(20)  NOT NULL,
    start_date       DATE         NOT NULL,
    end_date         DATE         NOT NULL,
    total_amount     FLOAT        NOT NULL,
    reason           VARCHAR(100) NOT NULL,  -- 100자 이내로 사유를 쓸 수 있음
    created_at       TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at       TIMESTAMP    NOT NULL DEFAULT NOW(),
    FOREIGN KEY (employee_id) REFERENCES employee (id),
    FOREIGN KEY (day_off_id) REFERENCES day_off (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

```sql
-- 당직 테이블 생성
CREATE TABLE duty_history
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    employee_id BIGINT       NOT NULL,
    status      VARCHAR(20)  NOT NULL,
    date        DATE         NOT NULL,
    created_at  TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP    NOT NULL DEFAULT NOW(),
    FOREIGN KEY (employee_id) REFERENCES employee (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

```sql
CREATE TABLE login_history (
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    employee_id BIGINT NOT NULL,
    client_ip   VARCHAR(255),
    user_agent  VARCHAR(255),
    created_at  TIMESTAMP,
    updated_at  TIMESTAMP,
    FOREIGN KEY (employee_id) REFERENCES employee (id)
);
```

</br>

## 📕 API 명세

<div align="center">

  ![API설계](https://github.com/jinyngg/attendance-back/assets/96164211/928065b4-3fa0-413a-9a80-310343463b0c)

</div>

</br>

## 👩‍💻 협업

`이슈 관리`

<div align="center">
  
  ![image](https://github.com/jinyngg/attendance-back/assets/96164211/bf0cce2f-0c85-4965-88aa-000e63882010)

</div>

</br>

`Git-flow 전략`

<div align="center">
  
  ![image](https://github.com/jinyngg/attendance-back/assets/96164211/a79baee7-4154-42b5-8c5e-216e8db6d611)

</div>

## AWS 명령어

**빌드**

```shell
./gradlew clean build bootJar -x test
```

**빌드된 앱 jar 서버에 전달**

```shell
scp -i {KEY파일} ./build/libs/*.jar {호스트명}@{호스트아이피}:{저장경로}/{jar 파일명}.jar
```

**앱 백그라운드로 무중단 실행 및 로그 저장**

```shell
nohup java -jar {jar 파일명}.jar > {로그 파일 경로} &
```

**앱 로그 확인**

```shell
tail -f -n 200 {로그 파일 경로}
```

**앱 프로세스 종료**

```shell
kill -9 $(ps -ef | grep {jar 파일명} | head -n 1 | cut -d ' ' -f 6) 
```

</br>

<!-- 
## 🗑 트러블 슈팅
-->
