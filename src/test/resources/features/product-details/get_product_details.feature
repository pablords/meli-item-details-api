#language: pt 

@PRODUCTS @CT001
Funcionalidade: Obter detalhes do produto
  Como um usuário do sistema
  Eu quero ser capaz de obter detalhes do produto
  Para que eu possa ver informações detalhadas sobre um produto específico

  @success
  Cenário: Obtendo detalhes do produto com sucesso
    Dado que estou no endpoint da API de detalhes "/api/v1/products/{id}" com id "MLB001"
    Quando eu solicito os detalhes do produto com id "MLB001"
    Então o status da resposta de detalhes deve ser 200
    E a resposta deve conter os seguintes detalhes do produto:
      | id          | MLB001                     |
      | title       | "Smartphone Samsung Galaxy S24 Ultra 256GB"         |
      | price       | 4999.99                 |
      | currency    | "BRL"                     |
      | available_quantity | 25                  |
      | pictures    | ["https://http2.mlstatic.com/D_Q_NP_123456_MLB.jpg", "https://http2.mlstatic.com/D_Q_NP_123457_MLB.jpg"]          |
      | seller_id   | "SELLER001"               |

  @fail
  Cenário: Falha ao obter detalhes do produto - Product not found
    Dado que estou no endpoint da API de detalhes "/api/v1/products/{id}" com id "99999"
    Quando eu solicito os detalhes do produto com id "99999"
    Então o status da resposta de detalhes deve ser 404
    E a resposta deve conter a mensagem "Product not found"