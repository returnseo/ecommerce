-- 초기 데이터 삽입 (PostgreSQL 버전)

-- 관리자 계정 (암호화된 비밀번호: 'admin123')
INSERT INTO member (email, password, name, role, created_at, updated_at)
VALUES ('admin@example.com', '$2a$10$oPDFCeQ1W3WgL3R3rJf3u.UIE8klpz2gWlnR0rM.DbtObHpqer5Yi', '관리자', 'ADMIN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 판매자 계정 (암호화된 비밀번호: 'seller123')
INSERT INTO member (email, password, name, role, created_at, updated_at)
VALUES ('seller@example.com', '$2a$10$0oho5eUb1lS.Vpy5Fgyx2uNmBqXvdF2UmQGYIg59f9RBnRoMQJF3S', '판매자', 'SELLER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 일반 고객 계정 (암호화된 비밀번호: 'user123')
INSERT INTO member (email, password, name, address, role, created_at, updated_at)
VALUES ('user@example.com', '$2a$10$zH9HLFnvWBdPJmknQkRoN.bSLK0JbZ5M7jsRlGsZ4r1mYJdrng9/6', '고객1', '서울시 강남구', 'CUSTOMER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 카테고리
INSERT INTO category (name, depth, parent_id, created_at, updated_at)
VALUES ('전자제품', 1, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO category (name, depth, parent_id, created_at, updated_at)
VALUES ('의류', 1, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO category (name, depth, parent_id, created_at, updated_at)
VALUES ('도서', 1, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 하위 카테고리
INSERT INTO category (name, depth, parent_id, created_at, updated_at)
SELECT '스마트폰', 2, id, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM category WHERE name = '전자제품';

INSERT INTO category (name, depth, parent_id, created_at, updated_at)
SELECT '노트북', 2, id, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM category WHERE name = '전자제품';

INSERT INTO category (name, depth, parent_id, created_at, updated_at)
SELECT '태블릿', 2, id, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM category WHERE name = '전자제품';

INSERT INTO category (name, depth, parent_id, created_at, updated_at)
SELECT '남성의류', 2, id, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM category WHERE name = '의류';

INSERT INTO category (name, depth, parent_id, created_at, updated_at)
SELECT '여성의류', 2, id, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM category WHERE name = '의류';

INSERT INTO category (name, depth, parent_id, created_at, updated_at)
SELECT '소설', 2, id, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM category WHERE name = '도서';

INSERT INTO category (name, depth, parent_id, created_at, updated_at)
SELECT '자기계발', 2, id, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM category WHERE name = '도서';

-- 상품 등록 (판매자 ID를 찾아서 사용)
INSERT INTO product (name, description, price, stock_quantity, status, seller_id, category_id, created_at, updated_at)
SELECT
    '최신 스마트폰',
    '최신 기술을 탑재한 스마트폰입니다.',
    1000000,
    100,
    'ACTIVE',
    (SELECT id FROM member WHERE email = 'seller@example.com'),
    (SELECT id FROM category WHERE name = '스마트폰'),
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP;

INSERT INTO product (name, description, price, stock_quantity, status, seller_id, category_id, created_at, updated_at)
SELECT
    '고성능 노트북',
    '업무와 게임에 최적화된 고성능 노트북입니다.',
    1500000,
    50,
    'ACTIVE',
    (SELECT id FROM member WHERE email = 'seller@example.com'),
    (SELECT id FROM category WHERE name = '노트북'),
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP;

INSERT INTO product (name, description, price, stock_quantity, status, seller_id, category_id, created_at, updated_at)
SELECT
    '베스트셀러 소설',
    '2023년 베스트셀러 소설입니다.',
    15000,
    200,
    'ACTIVE',
    (SELECT id FROM member WHERE email = 'seller@example.com'),
    (SELECT id FROM category WHERE name = '소설'),
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP;