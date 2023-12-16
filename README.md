# 🎬 영화 리뷰 서비스  Moview
영화들을 리뷰하고, 커뮤니티를 통해 영화를 추천 받을 수 있는 서비스.

## 기능

### [ 회원 ]
- **회원가입**
  - 아이디, 패스워드, 핸드폰, 이메일 정보를 입력합니다.
  - Gmail SMTP를 사용하여 등록한 이메일로 메일을 전송 후 10분 내에 본인 인증이 되면 회원가입이 가능합니다.
  - 일반 회원으로만 회원 가입이 가능하며, 관리자 role은 존재하나 api를 제공하지 않습니다.
  
- **로그인**
  - 아이디와 비밀번호를 입력하여 로그인이 가능합니다.
  - 로그인을 하면 Access Token이 발급되며, 유효시간이 지나면 만료됩니다.
  - Access Token은 Refresh Token을 통해 재발급 받을 수 있습니다.

- **회원 정보 조회**
  - 회원은 자신의 정보를 조회할 수 있습니다.
  
- **회원 정보 수정**
  - Gmail SMTP를 사용하여 회원가입 시 등록한 이메일로 메일을 전송 후 5분 내에 본인 인증이 되면 정보 수정이 가능합니다.

### [ 게시판 ]
***회원들끼리 추천해주고 싶은 영화가 있거나 소통을 위한 커뮤니티***

- **게시판 글 등록**
  - 관리자와 회원만 게시판 글 작성이 가능합니다.
  - 글(500자 이하), 사진(10장 이내)으로 업로드가 가능합니다.
  - 지정된 카테고리 설정이 가능합니다.

- **게시판 글 수정**
  - 작성자만 글의 수정이 가능합니다.

- **게시판 글 삭제**
  - 관리자와 작성자만 글의 삭제가 가능합니다.

- **게시판 글 조회**
  - 회원이 아니라도 글을 조회합니다.

- **내가 작성한 글 조회**
  - 내가 작성한 글들을 조회합니다.

- **게시판 검색**
  - 회원이 아니라도 검색이 가능합니다.
  - 제목과 내용을 선택하여 검색어가 포함하는 글을 검색합니다.
  - 카테고리별 검색이 가능합니다.   

- **조회수 기능**
  - 사용자가 글을 조회할 때마다 조회수가 증가합니다.
  - 사용자별 조회수 증가로, 중복 증가되지 않습니다.

- **추천 기능**
  - 추천 버튼을 누르면 추천 수가 증가하며, 중복은 불가능합니다.
 

### [ 댓글 ]
- **댓글 등록**
  - 회원만 댓글 등록이 가능합니다. 

- **댓글 수정**
  - 댓글 작성자만 수정이 가능합니다.

- **댓글 삭제**
  - 관리자와 댓글 작성자만 댓글 삭제가 가능합니다.
  - 게시판 글이 삭제되면 함께 삭제됩니다. 


### [ 영화 ]
- **영화 검색**
  - 제목을 통해 영화를 검색합니다.
  - 원하는 감독/배우의 영화를 검색합니다.

- **관심 영화 등록하기**
  - 회원이 영화 상세페이지에서 관심 버튼을 클릭하면 관심 테이블에 저장됩니다.

- **조회한 영화 히스토리 등록**
  - 회원이 영화 상세 페이지 접근 시 히스토리 테이블에 저장합니다.
  - 특정 회원이 방문한 히스토리가 10개를 넘어가면 가장 오래된 히스토리를 삭제합니다.

- **히스토리 삭제**
  - 회원은 자신의 히스토리를 삭제할 수 있습니다.

- **영화 등록**
  - [TMDB API](https://developer.themoviedb.org/reference/search-movie)를 사용해서 영화를 등록하거나 관리자가 직접 등록할 수 있습니다.

- **영화 삭제**
  - 관리자만 영화를 삭제할 수 있습니다.
 
### [ 리뷰 ]
- **리뷰/별점 등록**
  - 회원은 영화 상세 페이지에서 리뷰/별점을 등록할 수 있습니다.
  - 리뷰는 글(300자), 사진(5장 이내)으로 등록할 수 있습니다.  

- **리뷰 삭제**
  - 관리자와 리뷰 작성자만 영화 리뷰를 삭제할 수 있습니다.
  - 해당 영화가 삭제되면 함께 삭제됩니다.

 <br> <br>

  
![movie_erd](https://github.com/wndgndi/Moview/assets/102509248/8ed9f69a-8bff-4089-b8b0-5a29a80022a1)

---
## Tech Stack
<div align=left> 
<img src="https://img.shields.io/badge/IntelliJ IDEA-000000?style=flat&logo=IntelliJ IDEA&logoColor=white"/> 
<img src="https://img.shields.io/badge/Git-F05032?style=flat&logo=Git&logoColor=white"/> 
<img src="https://img.shields.io/badge/MySQL-4479A1?style=flat&logo=MySQL&logoColor=white"/> 
<img src="https://img.shields.io/badge/Spring-6DB33F?style=flat&logo=Spring&logoColor=white"/>
<img src="https://img.shields.io/badge/Spring Security-6DB33F?style=flat&logo=Spring Security&logoColor=white"/>
<img src="https://img.shields.io/badge/Redis-DC382D?style=flat&logo=Redis&logoColor=white"/>

</div>
