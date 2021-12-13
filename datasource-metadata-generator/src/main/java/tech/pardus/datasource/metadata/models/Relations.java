/**
 *
 */
package tech.pardus.datasource.metadata.models;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

/**
 * @author edtoktay
 * @since Nov 25, 2021
 */
@Data
@Builder
public class Relations implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  private Relation parentRelation;
  private Relation childRelation;
  private String updateRule;
  private String deleteRule;
  private int keySequence;
  private String fkName;
  private String pkName;
  private RelationType relationType;
}
