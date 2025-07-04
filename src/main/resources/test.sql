SELECT
    p.id,
    p.name,
    p.price,
    p.stock_quantity,
    'Category' as categoryName,
    p.status
FROM products p
WHERE p.status = 'ACTIVE'
AND (p.name LIKE %keyword% OR p.description LIKE %keyword%)
AND p.categoryId = categoryId
AND p.min_price >= minPrice
