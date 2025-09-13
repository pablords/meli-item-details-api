# 1. Teste normal (dentro do limite)
curl -X GET "http://localhost:8080/api/v1/healthz"

# 2. Teste de sobrecarga (simule múltiplas requisições)
for i in {1..35}; do curl -X GET "http://localhost:8080/api/v1/healthz" & done

# Resultado esperado após 30 requests: HTTP 429 Too Many Requests