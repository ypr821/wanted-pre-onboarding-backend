# Wanted-Pre-Onboarding-Backend

## 1. 지원자의 성명
안녕하세요. 지원자 *유푸름* 입니다.
꼭 참여하고 싶습니다! 간절합니다.
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

### [클라우드 환경(AWS)에 배포 환경]
API 배포 서버 URL : http://54.180.221.197/
<br>

![스크린샷 2023-08-16 오후 6 55 38](https://github.com/ypr821/wanted-pre-onboarding-backend/assets/56250078/16b6dc5f-3bb3-4f63-8dfb-03c25996181a)

<br>

## 3. 데이터베이스 테이블 구조

![스크린샷 2023-08-16 오후 6 03 59](https://github.com/ypr821/wanted-pre-onboarding-backend/assets/56250078/dc848aa4-12c6-4548-8872-86285acdcfa2)

<br>

## 4. 구현한 API의 동작을 촬영한 데모 영상 링크

- https://youtu.be/cFrbvmT3Nlk
<br><br>

## 5. 구현 방법 및 이유에 대한 간략한 설명
### 과제 1. 사용자 회원가입 엔드포인트
 - 전달받은 JSON 타입을 UserPostRequest 객체로 변환하여 회원가입을 진행합니다. spring-boot-starter-validation 종속성을 추가하여 javax Validation을 통해 객체가 email은 @Email 어노테이션으로 @ 포함 여부를 확인합니다. password 는 @size 어노테이션을 사용해서 8자 이상만 허용하도록 했습니다. 

 - 이메일과 비밀번호가 유효하고, 중복된 이메일인지 검사를 진행합니다. 검증한 객체는 Security에 설정된 PasswordEncoder 를 사용해서 전달받은 비밀번호를 단방향 암호화하여 DB에 저장합니다.

### 과제 2. 사용자 로그인 엔드포인트
 - 클라이언트로부터 입력받은 이메일(email)과 비밀번호(password)를 회원가입과 같은 방식으로 어노테이션을 사용한 유효성 검사를 진행합니다.
그리고 DB 에 저장했던 이메일과 비밀번호가 입력값과 일치하는 지 검증합니다. 비밀번호의 경우 passwordEncoder 인터페이스의 matches() 메서드를 사용하여 일치 여부를 확인합니다.

 - 유효성 조건에 부합하고 DB에 저장한 이메일과 비밀번호가 입력받은 값과 일치하면 JWT 형식의 Access Token을 발행합니다. 
Acess Token을 생성 및 검증하고 토큰으로부터 사용자 정보 추출하는 JwtTokenProvider 클래스를 생성하였습니다. 그리고 JWT 토큰을 생성할때 사용자 식별번호와 이메일을 정보에 담아 인가 과정에서 검증할때 사용하도록 했습니다. Acess Token은 기간을 12시간으로 지정해두었고 서버에서 기간을 조절 할 수 있습니다.

 - 매 요청마다 Token의 유효기간을 검증하고, 유효기간이 지났을 경우 토큰이 만료됐다는 메시지를 전달합니다. 


### 게시물 생성,수정,삭제 시 회원 검증
 - 로그인후 응답으로 받았던 accessToken을 header의 Authorization에 담아서 보냅니다. JWT 토큰으로 요청을 보내게 되면 검증을 처리하는 클래스인 JwtFilter 클래스에서 토큰 유효성을 검사하고 토큰으로부터 Authentication 객체 형태로 사용자 정보를 가져옵니다. 그리고 전역적으로 사용할 수 있도록 SecurityContext에 저장합니다.

 - SecurityContext에 저장된 Authentication 객체에서 userId 추출하여 해당 게시물의 소유자인지 검증하도록 하였습니다. 게시물의 소유자가 아닌 경우 FORBIDDEN 403 에러가 발생하도록 했습니다.

### 과제 4. 게시글 목록을 조회하는 엔드포인트
 - JPA에서 제공하는 Pageable를 이용해서 Pagination을 구현하였습니다. 전달 받은 URI를 통해서 원하는 한페이지당 출력할 게시글 사이즈, 페이지 번호 등을 입력해 커스텀할 수 있습니다.

<br>

## 6. API 명세(request/response 포함)
<br>

## 1. 회원가입
### 1.1 Request
- POST `/api/users`
  ```json
    {
      "email" : "test@gamil.com",
      "password" : "12345678"
    }
  ```
### 1.2 Response
- 201 Created
  ```json 
    {
      "userId": 2,
      "message": "회원가입 완료"
    }
  ```
- 400 Bad Request (이메일 검증)
    ```json
     {
         "dateTime": "2023-08-16 20:51:05.837",
         "status": 400,
         "message": "이미 사용중인 email 입니다."
     } 
  ```
- 400 Bad Request (비밀번호 검증)
    ```json
      {
          "dateTime": "2023-08-16 20:52:12.630",
          "status": 400,
          "message": "비밀번호를 8자이상 입력해주세요."
      }
    ```
## 2. 로그인
### 2.1 Request
- POST `/api/users/login`
    ```json
     {
         "email": "test@gamil.com",
         "password":"12345678"
     }
    ```
### 2.2 Response
- 200 OK
    ```json
     {
         "userId": 1,
         "message": "로그인 성공",
         "accessToken": "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0b2tlbiIsInVzZXJJZCI6MSwiZW1haWwiOiJ0ZXN0ODIxQGdhbWlsLmNvbSIsImlhdCI6MTY5MjE4Njc0OSwiZXhwIjoxNjkyMjI5OTQ5fQ.LKeqpO-vt6bMcHgmoFvf4x-g7X0H84eGlulbvJ5Mmnk"
     }
    ```
- 400 Bad Request
    ```json
      {
          "dateTime": "2023-08-16 20:53:40.927",
          "status": 400,
          "message": "아이디 혹은 비밀번호를 다시 입력해주세요."
      }
    ```
## 3. 게시물 등록
### 3.1 Request
- POST `/api/posts`
- Headers `Authorization: Bearer AccessToken `
    ```json
     {
         "title": "test 제목1",
         "content":"test 내용 입력1"
     }
    ```
### 3.2 Response
- 201 Created
    ```json
      {
          "dateTime": "2023-08-16 20:56:29.475",
          "status": 201,
          "message": "게시글을 저장하였습니다.",
          "postId": 4
      } 
    ```
- 401 Unauthorized (유효하지 않는 Access Token)
    ```json
      {
          "dateTime": "2023-08-16 20:57:55.472",
          "status": 401,
          "message": "유효하지 않은 토큰입니다."
      }
    ```
    
## 4. 게시물 목록 조회
### 4.1 Request
- GET `/api/posts?page=0&size=5`
- Headers `Authorization: Bearer AccessToken `
### 4.2 Response
- 200 OK
  ```json
      {
          "content": [
              {
                  "postId": 3,
                  "userId": 1,
                  "email": "test@gamil.com",
                  "title": "test 제목1",
                  "createDt": "2023-08-16 18:14:27.000"
              }
          ],
          "pageable": {
              "sort": {
                  "empty": false,
                  "sorted": true,
                  "unsorted": false
              },
              "offset": 0,
              "pageNumber": 0,
              "pageSize": 10,
              "paged": true,
              "unpaged": false
          },
          "last": true,
          "totalElements": 2,
          "totalPages": 1,
          "size": 10,
          "number": 0,
          "sort": {
              "empty": false,
              "sorted": true,
              "unsorted": false
          },
          "first": true,
          "numberOfElements": 2,
          "empty": false
      }
  ```

  ## 4. 게시물 상세 조회
### 4.1 Request
- GET `/api/posts/{id}`
- Headers `Authorization: Bearer AccessToken `
### 4.2 Response
- 200 Ok
    ```json
      {
          "postId": 1,
          "userId": 1,
          "email": "test821@gamil.com",
          "title": "test 제목1",
          "content": "test 내용 입력1",
          "createDt": "2023-08-16 00:58:50.000",
          "updateDt": "2023-08-16 00:58:50.000"
      }   
    ```
- 400 Bad Request (등록되지 않는 게시물 조회 요청)
    ```json
     { 
      "dateTime": "2023-08-16 21:06:40.232",
      "status": 400,
      "message": "유효하지 않은 게시글 고유 번호입니다."
     } 
    ```  
- 401 Unauthorized (유효하지 않는 Access Token)
    ```json
    {
        "dateTime": "2023-08-16 21:07:00.310",
        "status": 401,
        "message": "유효하지 않은 토큰입니다."
    }
    ```
## 5. 게시물 수정
### 5.1 Request
- PATCH `/api/posts/{id}`
- Headers `Authorization: Bearer AccessToken `
  ```json
     {
         "title": "test 제목 2 - 수정",
         "content":"test 내용 입력2 - 수정"
     }

### 5.2 Response
- 200 OK
  ```json
     {
         "postId": 2,
         "userId": 1,
         "email": "test821@gamil.com",
         "title": "test 제목 2 - 수정",
         "content": "test 내용 입력2 - 수정",
         "createDt": "2023-08-16 16:08:28.000",
         "updateDt": "2023-08-16 18:14:43.000",
         "message": "게시글을 수정하였습니다."
     }
  ```
- 400 Bad Request (등록되지 않는 게시물 조회 요청)
    ```json
    {
      "dateTime": "2023-08-16 21:10:23.592",
      "status": 400,
      "message": "유효하지 않은 게시글 고유 번호입니다."
    }
    ```  
- 401 Unauthorized (유효하지 않는 Access Token)
    ```json
    {
      "dateTime": "2023-08-16 21:10:47.563",
      "status": 401,
      "message": "유효하지 않은 토큰입니다."
    }
    ```
- 403 Forbidden (작성자와 로그인한 사용자가 다른 경우)
  ```json
    {
      "dateTime": "2023-08-16 21:12:04.586",
      "status": 403,
      "message": "게시글을 수정할 수 있는 권한이 없습니다."
    }
  ```
## 6. 게시물 삭제
### 6.1 Request
- DELETE `/api/posts/{id}`
- Headers `Authorization: Bearer AccessToken `
### 6.2 Response
- 200 OK
  ```json
    {
      "dateTime": "2023-08-16 21:15:27.641",
      "status": 200,
      "message": "게시글을 삭제하였습니다."
    }
  ```
- 400 Bad Request (등록되지 않는 게시물 조회 요청)
    ```json
    {
      "dateTime": "2023-08-16 21:13:48.407",
      "status": 400,
      "message": "유효하지 않은 게시글 고유 번호입니다."
    }
    ```  
- 401 Unauthorized (유효하지 않는 Access Token)
    ```json
    {
      "dateTime": "2023-08-16 21:10:47.563",
      "status": 401,
      "message": "유효하지 않은 토큰입니다."
    }
    ```
- 403 Forbidden (작성자와 로그인한 사용자가 다른 경우)
  ```json
     {
       "dateTime": "2023-08-16 21:14:38.809",
       "status": 403,
       "message": "게시글을 삭제할 수 있는 권한이 없습니다."
     }
  ```
