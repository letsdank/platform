package net.letsdank.platform.utils.platform.sql.schema.join;

import lombok.Getter;

@Getter
public enum QuerySchemaJoinType {
    INNER("INNER"),
    LEFT_OUTER("LEFT"),
    FULL_OUTER("FULL"),
    RIGHT_OUTER("RIGHT");

    private final String schemaType;

    QuerySchemaJoinType(String schemaType) {
        this.schemaType = schemaType;
    }
}
