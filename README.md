# Wanted-Pre-Onboarding-Backend

## 1. 지원자의 성명
안녕하세요. 지원자 *유푸름* 입니다.
<br>
<br>

## 2. 애플리케이션의 실행 방법 (엔드포인트 호출 방법 포함)

### [도커 환경에서 git clone하는 경우]
```text
$ git clone https://github.com/ypr821/wanted-pre-onboarding-backend.git
$ ./gradlew build jar -x test
$ docker pull ypr821/wanted_backend_server:1.0
$ cd wanted-pre-onboarding-backend
$ docker-compose up --build -d
```

### [엔드포인트 호출 방법]
| HTTP Method | End point | Description |
| --- | --- | --- |
| POST | /api/users | 회원가입 |
| POST | /api/users/login | 로그인 |
| | | |
| POST | /api/posts | 게시물 등록 |
| PATCH | /api/posts/{id} | 게시물 수정 |
| DELETE | /api/posts/{id} | 게시물 삭제 |
| GET | /api/posts | 게시물 목록 조회 |
| GET | /api/posts/{Id} | 게시물 상세 조회 |

### [클라우드 환경(AWS, GCP)에 배포 환경]
API 배포 서버 URL : http://54.180.221.197/
<br>
// AWS 환경 그림으로 첨부
![스크린샷 2023-08-16 오후 6 55 38](https://github.com/ypr821/wanted-pre-onboarding-backend/assets/56250078/16b6dc5f-3bb3-4f63-8dfb-03c25996181a)



<br>
## 3. 엔드포인트 호출 방법 포함
<br>
## 4. 데이터베이스 테이블 구조
![스크린샷 2023-08-16 오후 6 03 59](https://github.com/ypr821/wanted-pre-onboarding-backend/assets/56250078/d1249211-b10e-4a77-9151-f29a216e4b66)
<br>
## 5. 구현한 API의 동작을 촬영한 데모 영상 링크
<br>
## 6. 구현 방법 및 이유에 대한 간략한 설명
<br>
## 7. API 명세(request/response 포함)
<br>
