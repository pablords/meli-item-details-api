package com.pablords.meli.itemdetail.component.CT001;

import io.cucumber.datatable.DataTable;
import java.util.Map;

/**
 * Utilitário para validações genéricas de DataTable em testes Cucumber
 * Centraliza a lógica de validação para melhor reutilização e manutenibilidade
 */
public class DataTableValidator {

    /**
     * Valida resposta JSON usando DataTable de forma genérica
     * @param dataTable Tabela com campos esperados
     * @param jsonResponse Resposta JSON a ser validada
     */
    public static void validateResponseWithDataTable(DataTable dataTable, String jsonResponse) {
        validateJsonResponseNotEmpty(jsonResponse);
        
        Map<String, String> expectedData = dataTable.asMap(String.class, String.class);
        
        for (Map.Entry<String, String> entry : expectedData.entrySet()) {
            String field = entry.getKey();
            String expectedValue = cleanExpectedValue(entry.getValue());
            
            validateFieldInResponse(field, expectedValue, jsonResponse);
        }
    }

    /**
     * Valida se a resposta JSON não está vazia ou nula
     */
    public static void validateJsonResponseNotEmpty(String jsonResponse) {
        assert jsonResponse != null && !jsonResponse.isEmpty() : 
            "Response content should not be empty";
    }

    /**
     * Remove aspas e espaços desnecessários do valor esperado
     */
    private static String cleanExpectedValue(String value) {
        return value.replace("\"", "").trim();
    }

    /**
     * Valida um campo específico na resposta JSON
     * @param field Nome do campo
     * @param expectedValue Valor esperado
     * @param jsonResponse Resposta JSON
     */
    private static void validateFieldInResponse(String field, String expectedValue, String jsonResponse) {
        switch (field) {
            case "id":
                validateStringField("id", expectedValue, jsonResponse);
                break;
            case "title":
                validateStringField("title", expectedValue, jsonResponse);
                break;
            case "price":
                validateNumericField("amount", expectedValue, jsonResponse);
                break;
            case "currency":
                validateStringField("currency", expectedValue, jsonResponse);
                break;
            case "available_quantity":
                validateNumericField("available_quantity", expectedValue, jsonResponse);
                break;
            case "seller_id":
                validateNestedField("seller", "id", expectedValue, jsonResponse);
                break;
            case "seller_nickname":
                validateNestedField("seller", "nickname", expectedValue, jsonResponse);
                break;
            case "message":
                validateStringField("message", expectedValue, jsonResponse);
                break;
            default:
                System.out.println("⚠️ Warning: Field '" + field + "' validation not implemented");
        }
    }

    /**
     * Valida campo string no JSON
     */
    public static void validateStringField(String fieldName, String expectedValue, String jsonResponse) {
        String expectedPattern = "\"" + fieldName + "\":\"" + expectedValue + "\"";
        assert jsonResponse.contains(expectedPattern) : 
            String.format("Expected %s: '%s' not found in response: %s", fieldName, expectedValue, jsonResponse);
    }

    /**
     * Valida campo numérico no JSON
     */
    public static void validateNumericField(String fieldName, String expectedValue, String jsonResponse) {
        String expectedPattern = "\"" + fieldName + "\":" + expectedValue;
        assert jsonResponse.contains(expectedPattern) : 
            String.format("Expected %s: %s not found in response: %s", fieldName, expectedValue, jsonResponse);
    }

    /**
     * Valida campo aninhado no JSON (ex: seller.id)
     */
    public static void validateNestedField(String parentField, String childField, String expectedValue, String jsonResponse) {
        boolean hasParent = jsonResponse.contains("\"" + parentField + "\"");
        String childPattern = "\"" + childField + "\":\"" + expectedValue + "\"";
        boolean hasChild = jsonResponse.contains(childPattern);
        
        assert hasParent && hasChild : 
            String.format("Expected %s.%s: '%s' not found in response: %s", 
                parentField, childField, expectedValue, jsonResponse);
    }

    /**
     * Valida múltiplos campos de uma vez usando DataTable
     * @param dataTable Tabela com campos e valores esperados
     * @param jsonResponse Resposta JSON
     * @param customFieldMappings Mapeamentos customizados de campo (opcional)
     */
    public static void validateMultipleFields(DataTable dataTable, String jsonResponse, 
                                            Map<String, FieldValidator> customFieldMappings) {
        validateJsonResponseNotEmpty(jsonResponse);
        
        Map<String, String> expectedData = dataTable.asMap(String.class, String.class);
        
        for (Map.Entry<String, String> entry : expectedData.entrySet()) {
            String field = entry.getKey();
            String expectedValue = cleanExpectedValue(entry.getValue());
            
            // Verifica se há validador customizado
            if (customFieldMappings != null && customFieldMappings.containsKey(field)) {
                customFieldMappings.get(field).validate(expectedValue, jsonResponse);
            } else {
                validateFieldInResponse(field, expectedValue, jsonResponse);
            }
        }
    }

    /**
     * Interface funcional para validadores customizados
     */
    @FunctionalInterface
    public interface FieldValidator {
        void validate(String expectedValue, String jsonResponse);
    }
}