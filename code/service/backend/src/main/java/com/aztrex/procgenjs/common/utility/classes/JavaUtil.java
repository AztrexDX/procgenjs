package com.aztrex.procgenjs.common.utility.classes;

import com.aztrex.procgenjs.common.utility.constant.CommonConstant;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;

import java.time.OffsetDateTime;
import java.util.*;

@Slf4j
public class JavaUtil {

    /**
     * Builds a JPA Criteria Predicate based on search parameters, dynamically applying
     * appropriate comparisons based on the entity's field types using a switch statement.
     * Allows specifying additional parameters to ignore beyond the defaults.
     *
     * @param searchParams Map where keys are entity field names and values are search strings.
     * @param root The root object representing the entity in the Criteria query.
     * @param criteriaBuilder Used to construct predicates.
     * @param additionalIgnoreParameters A set of parameter names to ignore in addition to CommonConstant.IGNORE_PARAMETER_SET. Can be null or empty.
     * @param <T> The type of the entity.
     * @return A Predicate combining all valid search conditions with AND logic.
     */
    public static <T> Predicate buildPredicateForSearchParamsByObject(
            Map<String, String> searchParams,
            Root<T> root,
            CriteriaBuilder criteriaBuilder,
            Set<String> additionalIgnoreParameters
    ) {

        List<Predicate> predicates = new ArrayList<>();

        for (Map.Entry<String, String> entry : searchParams.entrySet()) {
            String column = entry.getKey();
            String value = entry.getValue();

            // --- Ignore Logic ---
            boolean shouldIgnore = CommonConstant.IGNORE_PARAMETER_SET.contains(column) ||
                    (additionalIgnoreParameters != null && additionalIgnoreParameters.contains(column)) ||
                    value == null || value.trim().isEmpty();

            if (shouldIgnore) {
                continue;
            }
            // --- End Ignore Logic ---

            try {
                Path<?> path = root.get(column); // Get the path (represents the attribute)
                Class<?> attributeType = path.getJavaType(); // Get the Java type of the attribute

                log.debug("Building predicate for column: {}, value: '{}', type: {}", column, value, attributeType.getSimpleName());

                // --- Handle Enums separately first ---
                if (Enum.class.isAssignableFrom(attributeType)) {
                    @SuppressWarnings({"unchecked", "rawtypes"}) // Necessary for Enum.valueOf with dynamic type
                    Class<? extends Enum> enumType = (Class<? extends Enum>) attributeType;
                    try {
                        Enum<?> enumValue = null;
                        // Case-insensitive matching
                        for (Enum<?> constant : enumType.getEnumConstants()) {
                            if (constant.name().equalsIgnoreCase(value)) {
                                enumValue = constant;
                                break;
                            }
                        }
                        if (enumValue != null) {
                            predicates.add(criteriaBuilder.equal(path, enumValue));
                        } else {
                            log.warn("No matching enum constant found for type {} with value (case-insensitive) '{}'", enumType.getSimpleName(), value);
                        }
                    } catch (IllegalArgumentException e) {
                        log.error("Error processing enum value '{}' for column '{}' of type {}", value, column, enumType.getSimpleName(), e);
                    }
                } else {
                    // --- Switch on Class Name for other types ---
                    switch (attributeType.getName()) {
                        case "java.lang.String":
                            String likeValue = CommonConstant.PERCENT + value.toLowerCase() + CommonConstant.PERCENT;
                            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(path.as(String.class)), likeValue));
                            break; // <<< Don't forget break!

                        case "java.lang.Long":
                            try {
                                predicates.add(criteriaBuilder.equal(path, Long.parseLong(value)));
                            } catch (NumberFormatException e) {
                                log.error("Cannot parse value '{}' for Long column '{}'", value, column, e);
                            }
                            break;

                        case "java.lang.Integer":
                            try {
                                predicates.add(criteriaBuilder.equal(path, Integer.parseInt(value)));
                            } catch (NumberFormatException e) {
                                log.error("Cannot parse value '{}' for Integer column '{}'", value, column, e);
                            }
                            break;

                        case "java.lang.Boolean":
                            // Boolean.parseBoolean is quite lenient (true if "true" ignoring case, false otherwise)
                            predicates.add(criteriaBuilder.equal(path, Boolean.parseBoolean(value)));
                            break;

                        // --- Add other specific types as needed ---
                         case "java.time.OffsetDateTime":
                             try {
                                 OffsetDateTime dateTimeValue = OffsetDateTime.parse(value); // Assumes ISO format
                                 predicates.add(criteriaBuilder.equal(path, dateTimeValue));
                             } catch (java.time.format.DateTimeParseException e) {
                                 log.error("Invalid date format '{}' for column '{}'", value, column, e);
                             }
                             break;

                        default:
                            // --- Fallback for unhandled non-enum types ---
                            log.warn("Unsupported search type for column '{}': {}. Attempting direct equals comparison.", column, attributeType.getName());
                            try {
                                // This might fail depending on DB and type compatibility
                                predicates.add(criteriaBuilder.equal(path, value));
                            } catch (Exception e) {
                                log.error("Fallback 'equal' comparison failed for column '{}' with value '{}'", column, value, e);
                            }
                            break;
                    }
                }

            } catch (IllegalArgumentException e) {
                // Handle cases where root.get(column) fails (invalid column name sent from client)
                log.error("Invalid search column name '{}'", column, e);
            } catch (Exception e) {
                // Catch other potential errors during predicate building
                log.error("Unexpected error processing search parameter for column '{}' with value '{}'", column, value, e);
            }
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    /**
     * Overloaded version of buildPredicateForSearchParamsByObject that uses only the default
     * ignored parameters defined in CommonConstant.
     */
    public static <T> Predicate buildPredicateForSearchParamsByObject(
            Map<String, String> searchParams,
            Root<T> root,
            CriteriaBuilder criteriaBuilder) {
        return buildPredicateForSearchParamsByObject(searchParams, root, criteriaBuilder, Collections.emptySet());
    }

    public static <T> Predicate buildPredicateForSearchParams(Map<String, String> searchParams, Root<T> root, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        for (Map.Entry<String, String> entry : searchParams.entrySet()) {
            String column = entry.getKey();
            String value = entry.getValue();

            if (CommonConstant.IGNORE_PARAMETER_SET.contains(column)) {
                continue;
            }

            if (column != null && value != null) {
                value = new StringBuilder().append(CommonConstant.PERCENT).append(value).append(CommonConstant.PERCENT).toString();
                if (CommonConstant.UUID_PARAMETER_SET.contains(column)) {
                    predicates.add(criteriaBuilder.like(criteriaBuilder.function("lower", String.class, root.get(column).as(String.class)), value));
                } else {
                    predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(column)), value));
                }
            }
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    public static <T> void populatePagedResultInResponse(Map<String, Object> response, Page<T> pagedResult) {
        response.put(CommonConstant.CONTENT, pagedResult.getContent());
        response.put(CommonConstant.CURRENT_PAGE, pagedResult.getNumber());
        response.put(CommonConstant.TOTAL_ITEMS, pagedResult.getTotalElements());
        response.put(CommonConstant.TOTAL_PAGES, pagedResult.getTotalPages());
    }
}
