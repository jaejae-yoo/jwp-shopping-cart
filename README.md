# 장바구니
장바구니 미션 저장소

# 1단계 요구사항 정리

## 형식
- Username - 공백 빼고 32이하
- Email - 한글, 공백 빼고 64자 이하
- Password - 한글, 공백 빼고 6자 이상


- [x] 회원가입
  - [x] 요청으로 이름, 이메일, 비밀번호를 받는다.
  - [x] 응답으로 이름, 이메일을 반환한다. 201
- [x] 로그인
  - [x] 요청으로 이메일, 비밀번호를 받는다.
  - [x] 응답으로 이름, 이메일, 토큰을 반환한다. 200
- [x] 회원 정보 조회
  - [x] 요청으로 토큰을 받는다.
  - [x] 응답으로 이름, 이메일을 반환한다. 200
- [x] 회원 정보 수정 (비밀번호 수정)
  - [x] 요청으로 기존 비밀번호와 바꿀 비밀번호, 토큰을 받는다.
  - [x] 응답으로 이름을 반환하다. 200
- [x] 회원 탈퇴
  - [x] 요청으로 비밀번호와 토큰을 받는다.
  - [x] 204

# 2단계 요구사항 정리
- [] 상품
  - [] 상품 등록
  - [] 상품 수정
  - [] 상품 전제 조회
  - [] 상품 조회
  - [] 상품 삭제 

- [] 장바구니
  - [] 상품 담기
  - [] 상품 조회 
  - [] 상품 하나 제거
  - [] 상품 여러 개 제거
  - [] 상품 전체 제거
  - [] 장바구니 상품 정보 수정 (수량, 구매체크) 단일
  - [] 장바구니 상품 정보 수정 (수량, 구매체크) 복수

[API 문서](https://www.notion.so/brorae/1-API-c10e17f6fdc940bbb2379ec7e07b1cb4)
## ✏️ Code Review Process
[텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)
