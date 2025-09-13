#language: pt 

@user @CT001
Funcionalidade: Obter detalhes do produto
  Como um usuário do sistema
  Eu quero ser capaz de obter detalhes do produto
  Para que eu possa ver informações detalhadas sobre um produto específico

  @success
  Cenário: Obtendo detalhes do produto com sucesso
    Dado que estou no endpoint da API "/products/{id}" com id "12345"
    Quando eu solicito os detalhes do produto com id "12345"
    Então o status da resposta deve ser 200
    E a resposta deve conter os seguintes detalhes do produto:
      | id          | 12345                     |
      | title       | "Smartphone XYZ"         |
      | price       | 99.99                     |
      | currency    | "USD"                     |
      | available_quantity | 10                  |
      | pictures    | ["url1", "url2"]          |
      | seller_id   | "seller123"               |
      | seller_nickname | "vendedor_exemplo"      |
      | seller_email | "vendedor@example.com"    |

  @fail
  Cenário: Falha ao obter detalhes do produto - Product not found
    Dado que estou no endpoint da API "/products/{id}" com id "99999"
    Quando eu solicito os detalhes do produto com id "99999"
    Então o status da resposta deve ser 404
    E a resposta deve conter a mensagem "Product not found"