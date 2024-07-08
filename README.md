# 셰어마인드

> 경험 공유 기반 연애상담 마켓플레이스 (https://sharemindapp.com)

## 프로젝트 소개
- 상담사 프로필의 한줄, 장문 소개를 통해 상담사의 경험을 살펴보고 원하는 상담사를 선택해 연애상담을 신청할 수 있는 서비스
- 편지 및 채팅 형식의 일대일 상담, 비공개 및 공개 형식의 일대다 상담을 제공
- 셰어: 상담을 받는, 자신의 고민을 나누는 사람 / 마인더: 셰어의 고민을 이해하고 상담을 해주는 사람

### 개발 배경
- 경험 기반 연애상담에 대한 수요
  - 나와 내 상황을 이해해주는 사람에게 받는 연애상담에 대한 만족도가 높은 것을 확인
  - 나와 같거나 비슷한 경험을 했던 사람에게 상담을 받기를 원하는 수요 확인

### 주요 기능
#### 셰어
- 들을 준비가 된 마인더 조회 (연애상담에 특화된 8개 카테고리, 3개의 상담 스타일로 분류)
- 원하는 방식으로 상담 신청 및 진행 (편지, 채팅, 일대다 상담)
- 상담을 진행한 마인더에 대한 리뷰 작성
- 관심 있는 마인더 프로필 저장
- 상담 결제 내역 조회

#### 마인더
- 마인더 인증을 위한 자료 제공 및 퀴즈 풀이
- 프로필 작성 (상담 카테고리/스타일/시간 설정, 경험 소개)
- 셰어와 상담 진행
- 셰어가 남긴 리뷰 확인
- 완료된 상담 정산 신청 및 수익 조회

## 개발 포인트
- CI/CD 환경 구축
- 커서 기반 페이지네이션 적용
- 복잡한 동적 쿼리 작성을 위한 QueryDSL 도입
- 비동기 처리를 활용한 응답 시간 개선 및 자원 사용 효율화
- 레디스를 활용한 캐시 처리
- 주기적으로 수행되어야하는 로직 처리 위한 스프링 스케줄러 도입

## 백엔드 팀원 소개
|  Name   |             [이유정](https://github.com/letskuku)              |             [이소현](https://github.com/aeyongdodam)       | 
|:-------:|:---------------------------------------------------------------:|:----------------------------------------------------------:|
| Profile | <img width="100px" src="https://github.com/letskuku.png" /> | <img width="100px" src="https://github.com/aeyongdodam.png" /> |

## 🛠 기술 스택

### 개발
<p>
    <img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white">
    <img src="https://img.shields.io/badge/spring%20boot-6DB33F?style=for-the-badge&logo=spring&logoColor=white">
    <img src="https://img.shields.io/badge/spring%20security-6DB33F?style=for-the-badge&logo=spring&logoColor=white">
    <img src="https://img.shields.io/badge/spring data jpa-F7DF1E?style=for-the-badge">
    <img src="https://img.shields.io/badge/querydsl-333333?style=for-the-badge">
    <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
    <img src="https://img.shields.io/badge/redis-DC382D?style=for-the-badge&logo=redis&logoColor=white">
</p>

### 인프라
<p>
    <img src="https://img.shields.io/badge/aws-232F3E?style=for-the-badge&logo=amazon-aws&logoColor=white">
    <img src="https://img.shields.io/badge/github%20actions-2088FF?style=for-the-badge&logo=github&logoColor=white">
</p>

### 테스트
<p>
    <img src="https://img.shields.io/badge/junit5-25A162?style=for-the-badge&logo=junit5&logoColor=white">
    <img src="https://img.shields.io/badge/mockito-8D6E63?style=for-the-badge&logo=mockito&logoColor=white">
</p>



### 문서/협업

<p>
<img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white">
<img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">
<img src="https://img.shields.io/badge/slack-4A154B?style=for-the-badge&logo=slack&logoColor=white">
<img src="https://img.shields.io/badge/notion-000000?style=for-the-badge&logo=notion&logoColor=white">
<img src="https://img.shields.io/badge/swagger-6DB33F?style=for-the-badge">
</p>

![image](https://github.com/sharemindteam/sharemind-server/assets/90572599/4588ca61-e72d-4501-b703-ec4aa8d2ee2a)
![image](https://github.com/sharemindteam/sharemind-server/assets/90572599/431557de-280d-4f74-b07b-a75e569e0de7)

