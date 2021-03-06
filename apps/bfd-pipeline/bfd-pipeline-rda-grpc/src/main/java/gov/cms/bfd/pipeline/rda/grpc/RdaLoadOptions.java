package gov.cms.bfd.pipeline.rda.grpc;

import com.google.common.base.Preconditions;
import gov.cms.bfd.pipeline.rda.grpc.sink.FissClaimRdaSink;
import gov.cms.bfd.pipeline.rda.grpc.source.FissClaimStreamCaller;
import gov.cms.bfd.pipeline.rda.grpc.source.FissClaimTransformer;
import gov.cms.bfd.pipeline.rda.grpc.source.GrpcRdaSource;
import gov.cms.bfd.pipeline.sharedutils.DatabaseOptions;
import gov.cms.bfd.pipeline.sharedutils.IdHasher;
import gov.cms.bfd.pipeline.sharedutils.NullPipelineJobArguments;
import gov.cms.bfd.pipeline.sharedutils.PipelineApplicationState;
import gov.cms.bfd.pipeline.sharedutils.PipelineJob;
import java.io.Serializable;
import java.time.Clock;
import java.util.Objects;

/**
 * A single combined configuration object to hold the configuration settings for the various
 * components of the RDA load job.
 */
public class RdaLoadOptions implements Serializable {
  private static final long serialVersionUID = 7635897362336183L;

  private final RdaLoadJob.Config jobConfig;
  private final GrpcRdaSource.Config grpcConfig;
  private final IdHasher.Config idHasherConfig;

  public RdaLoadOptions(
      RdaLoadJob.Config jobConfig,
      GrpcRdaSource.Config grpcConfig,
      IdHasher.Config idHasherConfig) {
    this.jobConfig = Preconditions.checkNotNull(jobConfig, "jobConfig is a required parameter");
    this.grpcConfig = Preconditions.checkNotNull(grpcConfig, "grpcConfig is a required parameter");
    this.idHasherConfig =
        Preconditions.checkNotNull(idHasherConfig, "idHasherConfig is a required parameter");
  }

  /** @return settings for the overall job. */
  public RdaLoadJob.Config getJobConfig() {
    return jobConfig;
  }

  /** @return settings for the gRPC service caller. */
  public GrpcRdaSource.Config getGrpcConfig() {
    return grpcConfig;
  }

  /**
   * Factory method to construct a new job instance using standard parameters.
   *
   * @param databaseOptions the shared application {@link DatabaseOptions}
   * @param appState the shared {@link PipelineApplicationState}
   * @return a PipelineJob instance suitable for use by PipelineManager.
   */
  public PipelineJob<NullPipelineJobArguments> createFissClaimsLoadJob(
      PipelineApplicationState appState) {
    return new RdaLoadJob<>(
        jobConfig,
        () ->
            new GrpcRdaSource<>(
                grpcConfig,
                new FissClaimStreamCaller(
                    new FissClaimTransformer(Clock.systemUTC(), new IdHasher(idHasherConfig))),
                appState.getMetrics()),
        () -> new FissClaimRdaSink(appState),
        appState.getMetrics());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof RdaLoadOptions)) {
      return false;
    }
    RdaLoadOptions that = (RdaLoadOptions) o;
    return Objects.equals(jobConfig, that.jobConfig) && Objects.equals(grpcConfig, that.grpcConfig);
  }

  @Override
  public int hashCode() {
    return Objects.hash(jobConfig, grpcConfig);
  }
}
