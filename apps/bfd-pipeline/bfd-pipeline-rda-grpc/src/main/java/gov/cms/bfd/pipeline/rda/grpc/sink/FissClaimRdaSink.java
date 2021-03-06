package gov.cms.bfd.pipeline.rda.grpc.sink;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Strings;
import gov.cms.bfd.model.rda.PreAdjFissClaim;
import gov.cms.bfd.pipeline.rda.grpc.ProcessingException;
import gov.cms.bfd.pipeline.rda.grpc.RdaSink;
import gov.cms.bfd.pipeline.sharedutils.PipelineApplicationState;
import java.util.Collection;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** RdaSink implementation that writes PreAdjFissClaim objects to the database in batches. */
public class FissClaimRdaSink implements RdaSink<PreAdjFissClaim> {
  private static final Logger LOGGER = LoggerFactory.getLogger(FissClaimRdaSink.class);
  /** Counts the number of times that writeBatch() is called. */
  static final String CALLS_METER_NAME =
      MetricRegistry.name(FissClaimRdaSink.class.getSimpleName(), "calls");
  /** Counts the number of times that writeBatch() is called and fails. */
  static final String FAILURES_METER_NAME =
      MetricRegistry.name(FissClaimRdaSink.class.getSimpleName(), "failures");
  /** Counts the number of objects successfully written to the database. */
  static final String OBJECTS_WRITTEN_METER_NAME =
      MetricRegistry.name(FissClaimRdaSink.class.getSimpleName(), "writes", "total");
  /** Counts the number of objects written to the database using persist(). */
  static final String OBJECTS_PERSISTED_METER_NAME =
      MetricRegistry.name(FissClaimRdaSink.class.getSimpleName(), "writes", "persisted");
  /** Counts the number of objects written to the database using merge(). */
  static final String OBJECTS_MERGED_METER_NAME =
      MetricRegistry.name(FissClaimRdaSink.class.getSimpleName(), "writes", "merged");

  private final EntityManager entityManager;
  private final Meter callsMeter;
  private final Meter failuresMeter;
  private final Meter objectsWrittenMeter;
  private final Meter objectsPersistedMeter;
  private final Meter objectsMergedMeter;

  public FissClaimRdaSink(PipelineApplicationState appState) {
    entityManager = appState.getEntityManagerFactory().createEntityManager();
    callsMeter = appState.getMetrics().meter(CALLS_METER_NAME);
    failuresMeter = appState.getMetrics().meter(FAILURES_METER_NAME);
    objectsWrittenMeter = appState.getMetrics().meter(OBJECTS_WRITTEN_METER_NAME);
    objectsPersistedMeter = appState.getMetrics().meter(OBJECTS_PERSISTED_METER_NAME);
    objectsMergedMeter = appState.getMetrics().meter(OBJECTS_MERGED_METER_NAME);
  }

  @Override
  public void close() throws Exception {
    entityManager.close();
  }

  /**
   * Writes the entire batch to the database in a single transaction.
   *
   * @param claims batch of claims to be written to the database
   * @return the number of claims successfully written to the database
   * @throws ProcessingException wrapped exception if an error takes place
   */
  @Override
  public int writeBatch(Collection<PreAdjFissClaim> claims) throws ProcessingException {
    try {
      callsMeter.mark();
      try {
        persistBatch(claims);
        objectsPersistedMeter.mark(claims.size());
        LOGGER.info("wrote batch of {} claims using persist()", claims.size());
      } catch (Throwable error) {
        if (isDuplicateKeyException(error)) {
          LOGGER.info(
              "caught duplicate key exception: switching to merge for batch of {} claims",
              claims.size());
          mergeBatch(claims);
          objectsMergedMeter.mark(claims.size());
          LOGGER.info("wrote batch of {} claims using merge()", claims.size());
        } else {
          throw error;
        }
      }
    } catch (Exception error) {
      LOGGER.error("writeBatch failure: error={}", error.getMessage(), error);
      failuresMeter.mark();
      throw new ProcessingException(error, 0);
    }
    objectsWrittenMeter.mark(claims.size());
    return claims.size();
  }

  private void persistBatch(Iterable<PreAdjFissClaim> claims) {
    boolean commit = false;
    try {
      entityManager.getTransaction().begin();
      for (PreAdjFissClaim claim : claims) {
        entityManager.persist(claim);
      }
      commit = true;
    } finally {
      if (commit) {
        entityManager.getTransaction().commit();
      } else {
        entityManager.getTransaction().rollback();
      }
    }
  }

  private void mergeBatch(Iterable<PreAdjFissClaim> claims) {
    boolean commit = false;
    try {
      entityManager.getTransaction().begin();
      for (PreAdjFissClaim claim : claims) {
        entityManager.merge(claim);
      }
      commit = true;
    } finally {
      if (commit) {
        entityManager.getTransaction().commit();
      } else {
        entityManager.getTransaction().rollback();
      }
    }
  }

  @VisibleForTesting
  static boolean isDuplicateKeyException(Throwable error) {
    while (error != null) {
      if (error instanceof EntityExistsException) {
        return true;
      }
      final String errorMessage = Strings.nullToEmpty(error.getMessage()).toLowerCase();
      if (errorMessage.contains("already exists") || errorMessage.contains("duplicate key")) {
        return true;
      }
      error = error.getCause() == error ? null : error.getCause();
    }
    return false;
  }
}
