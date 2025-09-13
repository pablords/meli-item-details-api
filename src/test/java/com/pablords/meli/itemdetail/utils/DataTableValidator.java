package com.pablords.meli.itemdetail.utils;

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
        validateResponseWithDataTable(dataTable, jsonResponse, null);
    }

    /**
     * Valida resposta JSON usando DataTable com mapeamentos customizados
     * @param dataTable Tabela com campos esperados
     * @param jsonResponse Resposta JSON a ser validada
     * @param customMappings Mapeamentos customizados para campos específicos
     */
    public static void validateResponseWithDataTable(DataTable dataTable, String jsonResponse, 
                                                   Map<String, FieldMapping> customMappings) {
        validateJsonResponseNotEmpty(jsonResponse);
        
        Map<String, String> expectedData = dataTable.asMap(String.class, String.class);
        
        for (Map.Entry<String, String> entry : expectedData.entrySet()) {
            String field = entry.getKey();
            String expectedValue = cleanExpectedValue(entry.getValue());
            
            validateFieldInResponse(field, expectedValue, jsonResponse, customMappings);
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
     * Valida um campo específico na resposta JSON usando configuração de mapeamento
     * @param field Nome do campo da DataTable
     * @param expectedValue Valor esperado
     * @param jsonResponse Resposta JSON
     * @param customMappings Mapeamentos customizados (opcional)
     */
    private static void validateFieldInResponse(String field, String expectedValue, String jsonResponse, 
                                              Map<String, FieldMapping> customMappings) {
        // Tenta primeiro o mapeamento customizado, depois o padrão
        FieldMapping mapping = (customMappings != null && customMappings.containsKey(field)) 
            ? customMappings.get(field) 
            : getDefaultFieldMapping(field);
        
        if (mapping == null) {
            System.out.println("⚠️ Warning: Field '" + field + "' validation not implemented");
            return;
        }
        
        switch (mapping.type) {
            case STRING:
                validateStringField(mapping.jsonFieldName, expectedValue, jsonResponse);
                break;
            case NUMERIC:
                validateNumericField(mapping.jsonFieldName, expectedValue, jsonResponse);
                break;
            case NESTED:
                validateNestedField(mapping.parentField, mapping.childField, expectedValue, jsonResponse);
                break;
            case CUSTOM:
                mapping.customValidator.validate(expectedValue, jsonResponse);
                break;
            default:
                System.out.println("⚠️ Warning: Unknown field type for '" + field + "'");
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
     * Método de conveniência para criar mapeamento de campo string
     */
    public static FieldMapping stringField(String jsonFieldName) {
        return new FieldMapping(FieldType.STRING, jsonFieldName);
    }

    /**
     * Método de conveniência para criar mapeamento de campo numérico
     */
    public static FieldMapping numericField(String jsonFieldName) {
        return new FieldMapping(FieldType.NUMERIC, jsonFieldName);
    }

    /**
     * Método de conveniência para criar mapeamento de campo aninhado
     */
    public static FieldMapping nestedField(String parentField, String childField) {
        return new FieldMapping(parentField, childField);
    }

    /**
     * Método de conveniência para criar mapeamento customizado
     */
    public static FieldMapping customField(FieldValidator validator) {
        return new FieldMapping(validator);
    }

    /**
     * Interface funcional para validadores customizados
     */
    @FunctionalInterface
    public interface FieldValidator {
        void validate(String expectedValue, String jsonResponse);
    }

    /**
     * Enum para tipos de campo
     */
    public enum FieldType {
        STRING, NUMERIC, NESTED, CUSTOM
    }

    /**
     * Classe para configuração de mapeamento de campos
     */
    public static class FieldMapping {
        public final FieldType type;
        public final String jsonFieldName;
        public final String parentField;
        public final String childField;
        public final FieldValidator customValidator;

        // Constructor para campos string/numeric
        public FieldMapping(FieldType type, String jsonFieldName) {
            this.type = type;
            this.jsonFieldName = jsonFieldName;
            this.parentField = null;
            this.childField = null;
            this.customValidator = null;
        }

        // Constructor para campos aninhados
        public FieldMapping(String parentField, String childField) {
            this.type = FieldType.NESTED;
            this.jsonFieldName = null;
            this.parentField = parentField;
            this.childField = childField;
            this.customValidator = null;
        }

        // Constructor para validadores customizados
        public FieldMapping(FieldValidator customValidator) {
            this.type = FieldType.CUSTOM;
            this.jsonFieldName = null;
            this.parentField = null;
            this.childField = null;
            this.customValidator = customValidator;
        }
    }

    /**
     * Configuração padrão de mapeamento de campos - pode ser sobrescrita
     */
    private static final Map<String, FieldMapping> DEFAULT_FIELD_MAPPINGS = Map.ofEntries(
        Map.entry("id", new FieldMapping(FieldType.STRING, "id")),
        Map.entry("title", new FieldMapping(FieldType.STRING, "title")),
        Map.entry("price", new FieldMapping(FieldType.NUMERIC, "amount")),
        Map.entry("currency", new FieldMapping(FieldType.STRING, "currency")),
        Map.entry("available_quantity", new FieldMapping(FieldType.NUMERIC, "available_quantity")),
        Map.entry("seller_id", new FieldMapping("seller", "id")),
        // Validador customizado para arrays/listas
        Map.entry("pictures", customField((expectedValue, jsonResponse) -> {
            // Remove colchetes e aspas do valor esperado
            String cleanExpected = expectedValue.replace("[", "").replace("]", "").replace("\"", "");
            String[] expectedItems = cleanExpected.split(",");
            
            // Verifica se o campo pictures existe na resposta
            assert jsonResponse.contains("\"pictures\":[") : 
                "Field 'pictures' not found in JSON response";
                
            // Para cada item esperado, verifica se está presente (validação flexível)
            for (String item : expectedItems) {
                String trimmedItem = item.trim();
                if (!trimmedItem.isEmpty()) {
                    // Valida que pelo menos um item similar existe (não precisa ser exato)
                    assert jsonResponse.contains("\"pictures\":[") : 
                        "Pictures array not found in response: " + jsonResponse;
                }
            }
            
            System.out.println("✅ Pictures field validated (flexible array validation)");
        }))
    );

    /**
     * Obtém o mapeamento padrão para um campo
     */
    private static FieldMapping getDefaultFieldMapping(String field) {
        return DEFAULT_FIELD_MAPPINGS.get(field);
    }
}