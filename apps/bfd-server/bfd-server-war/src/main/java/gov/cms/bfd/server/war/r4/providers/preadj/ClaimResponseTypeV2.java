package gov.cms.bfd.server.war.r4.providers.preadj;

import gov.cms.bfd.model.rda.PreAdjFissClaim;
import gov.cms.bfd.model.rda.PreAdjMcsClaim;
import gov.cms.bfd.server.war.r4.providers.preadj.common.ResourceTransformer;
import gov.cms.bfd.server.war.r4.providers.preadj.common.ResourceTypeV2;
import java.util.Optional;
import javax.persistence.Entity;
import javax.persistence.Id;
import org.hl7.fhir.r4.model.ClaimResponse;

/**
 * Enumerates the various Beneficiary FHIR Data Server (BFD) claim types that are supported by
 * {@link R4ClaimResponseResourceProvider}.
 */
public enum ClaimResponseTypeV2 implements ResourceTypeV2<ClaimResponse> {
  F(
      PreAdjFissClaim.class,
      PreAdjFissClaim.Fields.mbi,
      PreAdjFissClaim.Fields.mbiHash,
      PreAdjFissClaim.Fields.dcn,
      FissClaimResponseTransformerV2::transform),

  M(
      PreAdjMcsClaim.class,
      PreAdjMcsClaim.Fields.idrClaimMbi,
      PreAdjMcsClaim.Fields.idrClaimMbiHash,
      PreAdjMcsClaim.Fields.idrClmHdIcn,
      McsClaimResponseTransformerV2::transform);

  private final Class<?> entityClass;
  private final String entityMbiAttribute;
  private final String entityMbiHashAttribute;
  private final String entityIdAttribute;
  private final ResourceTransformer<ClaimResponse> transformer;

  /**
   * Enum constant constructor.
   *
   * @param entityClass the value to use for {@link #getEntityClass()}
   * @param entityMbiAttribute the value to use for {@link #getEntityMbiAttribute()}
   * @param entityMbiHashAttribute the value to use for {@link #getEntityMbiHashAttribute()}
   * @param entityIdAttribute the value to use for {@link #getEntityIdAttribute()}
   * @param transformer the value to use for {@link #getTransformer()}
   */
  ClaimResponseTypeV2(
      Class<?> entityClass,
      String entityMbiAttribute,
      String entityMbiHashAttribute,
      String entityIdAttribute,
      ResourceTransformer<ClaimResponse> transformer) {
    this.entityClass = entityClass;
    this.entityMbiAttribute = entityMbiAttribute;
    this.entityMbiHashAttribute = entityMbiHashAttribute;
    this.entityIdAttribute = entityIdAttribute;
    this.transformer = transformer;
  }

  /**
   * @return the JPA {@link Entity} {@link Class} used to store instances of this {@link
   *     ClaimResponseTypeV2} in the database
   */
  public Class<?> getEntityClass() {
    return entityClass;
  }

  /** @return the JPA {@link Entity} field used as the entity's {@link Id} */
  public String getEntityIdAttribute() {
    return entityIdAttribute;
  }

  /** @return The attribute name for the entity's mbi attribute. */
  public String getEntityMbiAttribute() {
    return entityMbiAttribute;
  }

  /** @return The attribute name for the entity's mbi hash attribute. */
  public String getEntityMbiHashAttribute() {
    return entityMbiHashAttribute;
  }

  /**
   * @return the {@link ResourceTransformer} to use to transform the JPA {@link Entity} instances
   *     into FHIR {@link ClaimResponse} instances
   */
  public ResourceTransformer<ClaimResponse> getTransformer() {
    return transformer;
  }

  /**
   * @param claimTypeText the lower-cased {@link ClaimResponseTypeV2#name()} value to parse back
   *     into a {@link ClaimResponseTypeV2}
   * @return the {@link ClaimResponseTypeV2} represented by the specified {@link String}
   */
  public static Optional<ResourceTypeV2<ClaimResponse>> parse(String claimTypeText) {
    for (ClaimResponseTypeV2 claimType : ClaimResponseTypeV2.values())
      if (claimType.name().toLowerCase().equals(claimTypeText)) return Optional.of(claimType);
    return Optional.empty();
  }
}
