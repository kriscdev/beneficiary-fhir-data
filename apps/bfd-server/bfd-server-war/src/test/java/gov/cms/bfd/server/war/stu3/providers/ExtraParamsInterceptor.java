package gov.cms.bfd.server.war.stu3.providers;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.client.api.IClientInterceptor;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.client.api.IHttpResponse;
import java.io.IOException;

/**
 * An interceptor class to add headers to requests for supplying additional parameters to FHIR
 * "read" operations. The operation only allows for certain parameters to be sent (e.g. {@link
 * RequestDetails}) so we add headers with our own parameters to the request in order to make use of
 * them.
 */
public class ExtraParamsInterceptor implements IClientInterceptor {
  private RequestHeaders requestHeader;
  // private IHttpRequest theRequest;
  // private String includeIdentifiersValues = "";
  // private String includeAddressValues = "";

  @Override
  public void interceptRequest(IHttpRequest theRequest) {
    // String headerValue = includeIdentifiersValues;
    // String headerAddressValue = includeAddressValues;

    // inject headers values
    requestHeader
        .getNVPairs()
        .forEach(
            (n, v) -> {
              theRequest.addHeader(n, v.toString());
            });
    // theRequest.addHeader(PatientResourceProvider.HEADER_NAME_INCLUDE_IDENTIFIERS, headerValue);
    // theRequest.addHeader(
    //     PatientResourceProvider.HEADER_NAME_INCLUDE_ADDRESS_FIELDS, headerAddressValue);
  }

  @Override
  public void interceptResponse(IHttpResponse theResponse) throws IOException {
    // TODO Auto-generated method stub

  }

  public void setHeaders(RequestHeaders requestHeader) {
    this.requestHeader = requestHeader;
  }
}
