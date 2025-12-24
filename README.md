# Tech Blog Spring Boot API
현대적인 기술 블로그를 위한 빠르고 안전한 백엔드입니다. Spring Boot 3, JWT 인증,
그리고 게시글, 태그, 좋아요, 이미지 업로드에 최적화되어 있습니다.

Fast, secure backend for a modern tech blog. Built with Spring Boot 3, JWT auth,
and first-class support for posts, tags, likes, and image uploads.

## Highlights / 주요 특징
- Post CRUD with slug generation, SEO fields, view/like counters
  - 슬러그 생성, SEO 필드, 조회/좋아요 카운터 포함한 게시글 CRUD
- Tags + categories with join table support
  - 태그 + 카테고리, 조인 테이블 지원
- JWT auth with refresh tokens stored in Redis (HTTP-only cookies)
  - Redis에 리프레시 토큰 저장, HTTP-only 쿠키 기반 JWT 인증
- Image upload with local file storage + metadata
  - 로컬 파일 저장 + 메타데이터 관리 이미지 업로드
- OpenAPI/Swagger UI for API discovery
  - OpenAPI/Swagger UI 제공

## Tech Stack / 기술 스택
- Java 21 + Spring Boot 3.4
- Spring Security + JWT
- Spring Data JPA (MySQL)
- Spring Data Redis
- springdoc-openapi

## API At A Glance / API 요약
Base path / 기본 경로: `/api/v1`

| Area | Method | Path | Notes |
| --- | --- | --- | --- |
| Auth | POST | `/auth/login` | Sets access/refresh cookies |
| Auth | POST | `/auth/logout` | Clears cookies |
| Users | POST | `/user/author-signup` | Create author |
| Users | POST | `/user/admin-signup` | Create admin |
| Users | POST | `/user/normal-signup` | Create user |
| Users | PATCH | `/user/{id}` | Update profile |
| Posts | POST | `/post` | Create post (auth) |
| Posts | PATCH | `/post` | Update post (auth) |
| Posts | GET | `/post` | List posts (categoryName, paging) |
| Posts | GET | `/post/slug/{slug}` | Get by slug |
| Posts | GET | `/post/id/{id}` | Get by id |
| Posts | DELETE | `/post/{id}` | Delete post (auth) |
| Posts | POST | `/post/view-count/{id}` | Increment view count |
| Posts | GET | `/post/search/recent` | Recent search by keyword |
| Categories | POST | `/category` | Create category (auth) |
| Categories | GET | `/category/list` | List categories |
| Likes | POST | `/like/{postId}` | Like a post (auth) |
| Likes | DELETE | `/like/{postId}` | Unlike a post (auth) |
| Likes | GET | `/like/{postId}` | Users who liked a post |
| Images | POST | `/image` | Upload image (multipart) |
| Images | GET | `/image/{uuid}` | Fetch image by public id |

Swagger UI / 스웨거 UI: `http://localhost:8080/swagger-ui/index.html`

## Local Development / 로컬 개발
Prereqs / 필수 환경: Java 21, MySQL 8, Redis 7+

1) Configure local DB/Redis in `src/main/resources/application.properties`
   - 로컬 DB/Redis 설정: `src/main/resources/application.properties`
2) Run the app / 애플리케이션 실행

```bash
./gradlew bootRun
```

## Production Configuration / 운영 환경 설정
`src/main/resources/application-prod.yml` expects these environment variables:
`src/main/resources/application-prod.yml`에서 아래 환경 변수를 사용합니다:

```bash
MYSQL_HOST=localhost
MYSQL_PORT=3306
MYSQL_DATABASE=tech_blog
MYSQL_USER=root
MYSQL_PASSWORD=change-me

REDIS_HOST=localhost
REDIS_PORT=6379

JWT_SECRET=change-me
IMAGE_UPLOAD_DIR=/app/images
```

## Build And Run With Docker / Docker로 빌드 및 실행
```bash
./gradlew bootJar
docker build -t tech-blog-api .
docker run -p 8080:8080 --env-file .env -v ./images:/app/images tech-blog-api
```

## Project Structure / 프로젝트 구조
```
src/main/java/com/naru/tech
  common/       # shared utilities and exceptions / 공통 유틸, 예외
  config/       # security, JWT, Redis, password encoder / 보안, JWT, Redis
  controller/   # REST endpoints / REST 엔드포인트
  data/         # entities, DTOs, repositories / 엔티티, DTO, 리포지토리
  service/      # business logic / 비즈니스 로직
```

## License / 라이선스
See `LICENSE`. / `LICENSE`를 참고하세요.
