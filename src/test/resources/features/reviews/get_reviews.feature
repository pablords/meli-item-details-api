#language: pt 

@REVIEWS @CT003
Funcionalidade: Obter avaliações de produtos
  Como um usuário do sistema
  Eu quero ser capaz de obter avaliações de produtos
  Para que eu possa tomar decisões informadas sobre minhas compras

  @success
  Cenário: Obtendo avaliações de produtos com sucesso
    Dado que estou no endpoint da API de avaliações "/products/{id}/reviews" com id "MLB001"
    Quando eu solicito as avaliações de produtos para o id "MLB001"
    Então o status da resposta de avaliações deve ser 200
    E a resposta deve conter uma lista de avaliações de produtos com 2 itens

  @fail
  Cenário: Falha ao obter avaliações de produtos - []
    Dado que estou no endpoint da API de avaliações "/products/{id}/reviews" com id "99999"
    Quando eu solicito as avaliações de produtos para o id "99999"
    Então o status da resposta de avaliações deve ser 200
    E a resposta deve conter uma lista vazia de avaliações de produtos