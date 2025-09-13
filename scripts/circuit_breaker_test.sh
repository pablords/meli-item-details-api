# 1. Produto existente
curl -X GET "http://localhost:8080/api/v1/products/MLB001"

# 2. Recomendações
curl -X GET "http://localhost:8080/api/v1/products/MLB001/recommendations?limit=6"

