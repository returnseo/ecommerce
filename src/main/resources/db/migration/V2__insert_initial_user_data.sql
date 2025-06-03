-- 관리자 계정 (암호화된 비밀번호: 'admin123')
INSERT INTO member (email, password, name, role, created_at, updated_at)
VALUES ('admin@example.com', '$2a$10$oPDFCeQ1W3WgL3R3rJf3u.UIE8klpz2gWlnR0rM.DbtObHpqer5Yi', '관리자', 'ADMIN', NOW(), NOW());

-- 판매자 계정 (암호화된 비밀번호: 'seller123')
INSERT INTO member (email, password, name, role, created_at, updated_at)
VALUES ('seller@example.com', '$2a$10$0oho5eUb1lS.Vpy5Fgyx2uNmBqXvdF2UmQGYIg59f9RBnRoMQJF3S', '판매자', 'SELLER', NOW(), NOW());

-- 일반 고객 계정 (암호화된 비밀번호: 'user123')
INSERT INTO member (email, password, name, address, role, created_at, updated_at)
VALUES ('user@example.com', '$2a$10$zH9HLFnvWBdPJmknQkRoN.bSLK0JbZ5M7jsRlGsZ4r1mYJdrng9/6', '고객1', '서울시 강남구', 'CUSTOMER', NOW(), NOW());
