#language: pt 

@user @CT001
Funcionalidade: Criar usuário
  Como um usuário do sistema
  Eu quero ser capaz de criar um novo usuário
  Para que eu possa acessar o sistema com minhas credenciais

  @success
  Cenário: Criar um usuário com detalhes válidos
    Dado que estou no endpoint da API "/users"
    Quando eu crio um usuário com os seguintes detalhes: "src/test/resources/features/requests/create-user-201.json"
    Então o status da resposta do usuário deve ser 201

  @fail
  Cenário: Falha ao criar um usuário com detalhes inválidos
    Quando eu crio um usuário com os seguintes detalhes: "src/test/resources/features/requests/create-user-422.json"
    Então o status da resposta do usuário deve ser 422