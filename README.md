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
<br>

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
