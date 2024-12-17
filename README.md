## fifteen-logistics
- 개발 기간 | 2024.12.04 - 2024.12.17
- 물류 관리 시스템

## 팀원 역할분담

| 역할    | 팀원 이름 | 주요 업무                     |
  |------------|-------|---------------------------|
| 팀장, 백엔드 개발 | 신영한   | AI, 슬랙 메시지 API 설계 및 구현    |
| 백엔드 개발 | 최소진   | 서브모듈, 게이트웨이(인증 및 인가), 유저  API 설계 및 구현 |
| 백엔드 개발 | 이건    | 배달, 허브, 배달경로, 허브경로  API 설계 및 구현       |
| 백엔드 개발 | 전혜리   | 상품, 업체, 주문  API 설계 및 구현               |
  </br>

## 기술 스택
![Java](https://img.shields.io/badge/Java-007396?style=flat-square&logo=java&logoColor=white) ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=flat-square&logo=springboot&logoColor=white) ![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=flat-square&logo=postgresql&logoColor=white) ![Google AI API](https://img.shields.io/badge/Google%20AI%20API-4285F4?style=flat-square&logo=google&logoColor=white) ![Git](https://img.shields.io/badge/Git-F05032?style=flat-square&logo=git&logoColor=white) ![GitHub](https://img.shields.io/badge/GitHub-181717?style=flat-square&logo=github&logoColor=white)


## ERD
![image](https://github.com/user-attachments/assets/d73aea7c-1e90-4170-91ce-f018f9b7327f)

## System Architecture

![image](https://github.com/user-attachments/assets/9f111540-11c0-4b27-8f6d-6b9003204622)

## 서비스 기능
### 업체 관리
> 업체 생성, 수정, 삭제, 조회 등 업체를 관리하기 위한 기능
- [X] 업체 생성
  - 권한에 따른 업체 생성, 속한 허브 및 업체 타입 지정
