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