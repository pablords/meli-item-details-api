#language: pt 

@PRODUCTS @CT002
Funcionalidade: Obter recomendações de produtos
  Como um usuário do sistema
  Eu quero ser capaz de obter recomendações de produtos
  Para que eu possa descobrir novos produtos que possam me interessar

  @success
  Cenário: Obtendo recomendações de produtos com sucesso
    Dado que estou no endpoint da API de recomendações "/products/{id}/recommendations" com id "MLB001"
    Quando eu solicito as recomendações de produtos para o id "MLB001"
    Então o status da resposta de recomendações deve ser 200
    E a resposta deve conter uma lista de recomendações de produtos com 3 itens


  @fail
  Cenário: Falha ao obter recomendações de produtos - []
    Dado que estou no endpoint da API de recomendações "/products/{id}/recommendations" com id "99999"
    Quando eu solicito as recomendações de produtos para o id "99999"
    Então o status da resposta de recomendações deve ser 200
    E a resposta deve conter uma lista vazia de recomendações de produtos