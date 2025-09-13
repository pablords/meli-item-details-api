package com.pablords.meli.itemdetail.utils;

public class StringCountList {
  /**
   * Método SUPER SIMPLES para contar itens
   * Funciona para: {"items":[]} ou {"items":[{...},{...}]}
   */
  public static int countItemsSimple(String jsonResponse) {
    // Caso 1: Lista vazia
    if (jsonResponse.contains("\"items\":[]")) {
      return 0;
    }

    // Caso 2: Não tem campo items
    if (!jsonResponse.contains("\"items\":[")) {
      return 0;
    }

    // Caso 3: Conta vírgulas + 1 para objetos simples
    // Pega só a parte do array "items"
    int startItems = jsonResponse.indexOf("\"items\":[") + 9;
    int endItems = jsonResponse.indexOf("]", startItems);

    if (startItems >= endItems) {
      return 0;
    }

    String itemsContent = jsonResponse.substring(startItems, endItems);

    // Se está vazio (só espaços), retorna 0
    if (itemsContent.trim().isEmpty()) {
      return 0;
    }

    // Conta objetos no nível raiz do array
    // Procura por padrões [{, ,{ que indicam início de objetos no nível raiz
    int count = 0;
    for (int i = 0; i < itemsContent.length(); i++) {
      if (itemsContent.charAt(i) == '{') {
        // É um objeto no nível raiz se vem logo após [ ou após },
        if (i == 0 ||
            (i > 0 && (itemsContent.charAt(i - 1) == '[' ||
                (i > 1 && itemsContent.charAt(i - 1) == ',' && itemsContent.charAt(i - 2) == '}')))) {
          count++;
        }
      }
    }
    return count;
  }
}
