package com.realestate.project.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class LegacySchemaCleanup implements ApplicationRunner {

    private static final String DEFAULT_DISTRICT = "Colombo";
    private static final String[] DISTRICTS = {
            "Ampara", "Anuradhapura", "Badulla", "Batticaloa", "Colombo",
            "Galle", "Gampaha", "Hambantota", "Jaffna", "Kalutara", "Kandy",
            "Kegalle", "Kilinochchi", "Kurunegala", "Mannar", "Matale",
            "Matara", "Monaragala", "Mullaitivu", "Nuwara Eliya",
            "Polonnaruwa", "Puttalam", "Ratnapura", "Trincomalee", "Vavuniya"
    };

    private final JdbcTemplate jdbcTemplate;

    public LegacySchemaCleanup(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(ApplicationArguments args) {
        migrateLegacyLocation("transactions");
        migrateLegacyLocation("analytics_reports");
        migrateLegacyLocation("property");
        migrateLegacyLocation("properties");
    }

    private void relaxLegacyNullableColumn(String tableName, String columnName) {
        try {
            if (columnExists(tableName, columnName)) {
                jdbcTemplate.execute("ALTER TABLE " + quoteIdentifier(tableName)
                        + " MODIFY COLUMN " + quoteIdentifier(columnName) + " varchar(255) NULL");
            }
        } catch (DataAccessException ex) {
            System.err.println("Could not update legacy schema column " + tableName + "." + columnName + ": " + ex.getMessage());
        }
    }

    private void migrateLegacyLocation(String tableName) {
        try {
            if (!columnExists(tableName, "location")) {
                return;
            }

            if (columnExists(tableName, "address") && columnExists(tableName, "district")) {
                List<Map<String, Object>> rows = jdbcTemplate.queryForList(
                        "SELECT id, location, address, district FROM " + quoteIdentifier(tableName)
                                + " WHERE ((address IS NULL OR address = '') OR (district IS NULL OR district = ''))"
                                + " AND location IS NOT NULL AND location <> ''"
                );

                for (Map<String, Object> row : rows) {
                    String legacyLocation = text(row.get("location"));
                    String address = hasText(row.get("address")) ? text(row.get("address")) : legacyLocation;
                    String district = hasText(row.get("district")) ? text(row.get("district")) : inferDistrict(legacyLocation);
                    jdbcTemplate.update(
                            "UPDATE " + quoteIdentifier(tableName)
                                    + " SET address = ?, district = ? WHERE id = ?",
                            address,
                            district,
                            row.get("id")
                    );
                }
            }

            relaxLegacyNullableColumn(tableName, "location");
        } catch (DataAccessException ex) {
            System.err.println("Could not migrate legacy location column on " + tableName + ": " + ex.getMessage());
        }
    }

    private boolean columnExists(String tableName, String columnName) {
        Integer count = jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM information_schema.columns
                WHERE table_schema = DATABASE()
                  AND table_name = ?
                  AND column_name = ?
                """,
                Integer.class,
                tableName,
                columnName
        );
        return count != null && count > 0;
    }

    private String inferDistrict(String value) {
        String normalized = text(value).toLowerCase();
        for (String district : DISTRICTS) {
            if (normalized.contains(district.toLowerCase())) {
                return district;
            }
        }
        return DEFAULT_DISTRICT;
    }

    private boolean hasText(Object value) {
        return value != null && !text(value).isBlank();
    }

    private String text(Object value) {
        return value == null ? "" : String.valueOf(value).trim();
    }

    private String quoteIdentifier(String identifier) {
        return "`" + identifier.replace("`", "``") + "`";
    }
}
