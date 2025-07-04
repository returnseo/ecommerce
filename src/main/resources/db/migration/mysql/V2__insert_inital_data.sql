-- 초기 데이터 삽입 (MySQL 버전)

-- 관리자 계정 (암호화된 비밀번호: 'admin123')
INSERT INTO member (email, password, name, role, created_at, updated_at)
VALUES ('admin@example.com', '$2a$10$oPDFCeQ1W3WgL3R3rJf3u.UIE8klpz2gWlnR0rM.DbtObHpqer5Yi', '관리자', 'ADMIN', NOW(), NOW());

-- 판매자 계정 (암호화된 비밀번호: 'seller123')
INSERT INTO member (email, password, name, role, created_at, updated_at)
VALUES ('seller@example.com', '$2a$10$0oho5eUb1lS.Vpy5Fgyx2uNmBqXvdF2UmQGYIg59f9RBnRoMQJF3S', '판매자', 'SELLER', NOW(), NOW());

-- 일반 고객 계정 (암호화된 비밀번호: 'user123')
INSERT INTO member (email, password, name, address, role, created_at, updated_at)
VALUES ('user@example.com', '$2a$10$zH9HLFnvWBdPJmknQkRoN.bSLK0JbZ5M7jsRlGsZ4r1mYJdrng9/6', '고객1', '서울시 강남구', 'CUSTOMER', NOW(), NOW());

-- 카테고리
INSERT INTO category (name, depth, parent_id, created_at, updated_at)
VALUES ('전자제품', 1, NULL, NOW(), NOW());

INSERT INTO category (name, depth, parent_id, created_at, updated_at)
VALUES ('의류', 1, NULL, NOW(), NOW());

INSERT INTO category (name, depth, parent_id, created_at, updated_at)
VALUES ('도서', 1, NULL, NOW(), NOW());

-- 하위 카테고리
INSERT INTO category (name, depth, parent_id, created_at, updated_at)
SELECT '스마트폰', 2, id, NOW(), NOW() FROM category WHERE name = '전자제품';

INSERT INTO category (name, depth, parent_id, created_at, updated_at)
SELECT '노트북', 2, id, NOW(), NOW() FROM category WHERE name = '전자제품';

INSERT INTO category (name, depth, parent_id, created_at, updated_at)
SELECT '태블릿', 2, id, NOW(), NOW() FROM category WHERE name = '전자제품';

INSERT INTO category (name, depth, parent_id, created_at, updated_at)
SELECT '남성의류', 2, id, NOW(), NOW() FROM category WHERE name = '의류';

INSERT INTO category (name, depth, parent_id, created_at, updated_at)
SELECT '여성의류', 2, id, NOW(), NOW() FROM category WHERE name = '의류';

INSERT INTO category (name, depth, parent_id, created_at, updated_at)
SELECT '소설', 2, id, NOW(), NOW() FROM category WHERE name = '도서';

INSERT INTO category (name, depth, parent_id, created_at, updated_at)
SELECT '자기계발', 2, id, NOW(), NOW() FROM category WHERE name = '도서';

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
    NOW(),
    NOW();

INSERT INTO product (name, description, price, stock_quantity, status, seller_id, category_id, created_at, updated_at)
SELECT
    '고성능 노트북',
    '업무와 게임에 최적화된 고성능 노트북입니다.',
    1500000,
    50,
    'ACTIVE',
    (SELECT id FROM member WHERE email = 'seller@example.com'),
    (SELECT id FROM category WHERE name = '노트북'),
    NOW(),
    NOW();

INSERT INTO product (name, description, price, stock_quantity, status, seller_id, category_id, created_at, updated_at)
SELECT
    '베스트셀러 소설',
    '2023년 베스트셀러 소설입니다.',
    15000,
    200,
    'ACTIVE',
    (SELECT id FROM member WHERE email = 'seller@example.com'),
    (SELECT id FROM category WHERE name = '소설'),
    NOW(),
    NOW();

-- 고객 장바구니 생성
INSERT INTO cart (member_id, created_at, updated_at)
SELECT id, NOW(), NOW() FROM member WHERE email = 'user@example.com';

-- 장바구니에 상품 추가
INSERT INTO cart_item (cart_id, product_id, quantity, created_at, updated_at)
SELECT
    (SELECT id FROM cart WHERE member_id = (SELECT id FROM member WHERE email = 'user@example.com')),
    (SELECT id FROM product WHERE name = '최신 스마트폰'),
    1,
    NOW(),
    NOW();