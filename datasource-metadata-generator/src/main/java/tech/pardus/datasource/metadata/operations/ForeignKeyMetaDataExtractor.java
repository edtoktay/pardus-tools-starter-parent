/**
 *
 */
package tech.pardus.datasource.metadata.operations;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import tech.pardus.datasource.metadata.models.Relation;
import tech.pardus.datasource.metadata.models.RelationType;
import tech.pardus.datasource.metadata.models.Relations;

/**
 * @author edtoktay
 * @since Nov 25, 2021
 */
public class ForeignKeyMetaDataExtractor implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  public Set<Relations> getRelations(Connection connection, String catalog, String schema,
      String tableName) throws SQLException {
    var relations = new HashSet<Relations>();
    var databaseMetaData = connection.getMetaData();
    try (var resultSet = databaseMetaData.getImportedKeys(catalog, schema, tableName);) {
      while (resultSet.next()) {
        // @formatter:off
        var parentRelation = Relation
                              .builder()
                                .category(resultSet.getString("PKTABLE_CAT"))
                                .schema(resultSet.getString("PKTABLE_SCHEM"))
                                .tableName(resultSet.getString("PKTABLE_NAME"))
                                .columnName(resultSet.getString("PKCOLUMN_NAME"))
                              .build();
        var childRelation = Relation
                              .builder()
                                .category(
                                    StringUtils.isBlank(resultSet.getString("FKTABLE_CAT")) ?
                                        catalog :
                                          resultSet.getString("FKTABLE_CAT"))
                                .schema(
                                    StringUtils.isBlank(resultSet.getString("FKTABLE_SCHEM")) ?
                                        schema :
                                          resultSet.getString("FKTABLE_SCHEM"))
                                .tableName(resultSet.getString("FKTABLE_NAME"))
                                .columnName(resultSet.getString("FKCOLUMN_NAME"))
                              .build();
        relations.add(
            Relations
              .builder()
                .parentRelation(parentRelation)
                .childRelation(childRelation)
                .updateRule(resultSet.getString("UPDATE_RULE"))
                .deleteRule(resultSet.getString("DELETE_RULE"))
                .keySequence(resultSet.getInt("KEY_SEQ"))
                .fkName(resultSet.getString("FK_NAME"))
                .pkName(resultSet.getString("PK_NAME"))
                .relationType(RelationType.CHILD)
              .build()
        );
        // @formatter:on
      }
    }
    try (var resultSet = databaseMetaData.getExportedKeys(catalog, schema, tableName);) {
      while (resultSet.next()) {
        // @formatter:off
        var parentRelation = Relation
                              .builder()
                              .category(
                                  StringUtils.isBlank(resultSet.getString("PKTABLE_CAT")) ?
                                      catalog :
                                        resultSet.getString("PKTABLE_CAT"))
                              .schema(
                                  StringUtils.isBlank(resultSet.getString("PKTABLE_SCHEM")) ?
                                      schema :
                                        resultSet.getString("PKTABLE_SCHEM"))
                                .tableName(resultSet.getString("PKTABLE_NAME"))
                                .columnName(resultSet.getString("PKCOLUMN_NAME"))
                              .build();
        var childRelation = Relation
                              .builder()
                                .category(resultSet.getString("FKTABLE_CAT"))
                                .schema(resultSet.getString("FKTABLE_SCHEM"))
                                .tableName(resultSet.getString("FKTABLE_NAME"))
                                .columnName(resultSet.getString("FKCOLUMN_NAME"))
                              .build();
        relations.add(
            Relations
              .builder()
                .parentRelation(parentRelation)
                .childRelation(childRelation)
                .updateRule(resultSet.getString("UPDATE_RULE"))
                .deleteRule(resultSet.getString("DELETE_RULE"))
                .keySequence(resultSet.getInt("KEY_SEQ"))
                .fkName(resultSet.getString("FK_NAME"))
                .pkName(resultSet.getString("PK_NAME"))
                .relationType(RelationType.PARENT)
              .build()
        );
        // @formatter:on
      }
    }
    return relations;
  }
}
