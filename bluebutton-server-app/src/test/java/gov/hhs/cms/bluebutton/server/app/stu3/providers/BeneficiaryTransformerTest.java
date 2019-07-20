package gov.hhs.cms.bluebutton.server.app.stu3.providers;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.hl7.fhir.dstu3.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.dstu3.model.Patient;
import org.junit.Assert;
import org.junit.Test;

import com.codahale.metrics.MetricRegistry;

import gov.hhs.cms.bluebutton.data.codebook.data.CcwCodebookVariable;
import gov.hhs.cms.bluebutton.data.model.rif.Beneficiary;
import gov.hhs.cms.bluebutton.data.model.rif.BeneficiaryHistory;
import gov.hhs.cms.bluebutton.data.model.rif.MedicareBeneficiaryIdHistory;
import gov.hhs.cms.bluebutton.data.model.rif.samples.StaticRifResource;
import gov.hhs.cms.bluebutton.data.model.rif.samples.StaticRifResourceGroup;
import gov.hhs.cms.bluebutton.server.app.ServerTestUtils;
import gov.hhs.cms.bluebutton.server.app.stu3.providers.PatientResourceProvider.IncludeIdentifiersMode;

/**
 * Unit tests for {@link BeneficiaryTransformer}.
 */
public final class BeneficiaryTransformerTest {
	/**
	 * Verifies that {@link BeneficiaryTransformer#transform(Beneficiary)} works
	 * as expected when run against the {@link StaticRifResource#SAMPLE_A_BENES}
	 * {@link Beneficiary}.
	 */
	@Test
	public void transformSampleARecord() {
		Beneficiary beneficiary = loadSampleABeneficiary();

		Patient patient = BeneficiaryTransformer.transform(new MetricRegistry(), beneficiary,
				IncludeIdentifiersMode.OMIT_HICNS_AND_MBIS);
		assertMatches(beneficiary, patient);
		Assert.assertEquals(2, patient.getIdentifier().size());
	}

	/**
	 * Verifies that {@link BeneficiaryTransformer#transform(Beneficiary)} works as
	 * expected when run against the {@link StaticRifResource#SAMPLE_A_BENES}
	 * {@link Beneficiary}, in the
	 * {@link IncludeIdentifiersMode#INCLUDE_HICNS_AND_MBIS} mode.
	 */
	@Test
	public void transformSampleARecordWithIdentifiers() {
		Beneficiary beneficiary = loadSampleABeneficiary();

		Patient patient = BeneficiaryTransformer.transform(new MetricRegistry(), beneficiary,
				IncludeIdentifiersMode.INCLUDE_HICNS_AND_MBIS);
		assertMatches(beneficiary, patient);
		Assert.assertEquals(7, patient.getIdentifier().size());
	}

	/**
	 * @return the {@link StaticRifResourceGroup#SAMPLE_A} {@link Beneficiary}
	 *         record, with the {@link Beneficiary#getBeneficiaryHistories()} and
	 *         {@link Beneficiary#getMedicareBeneficiaryIdHistories()} fields
	 *         populated.
	 */
	private static Beneficiary loadSampleABeneficiary() {
		List<Object> parsedRecords = ServerTestUtils
				.parseData(Arrays.asList(StaticRifResourceGroup.SAMPLE_A.getResources()));

		// Pull out the base Beneficiary record and fix its HICN fields.
		Beneficiary beneficiary = parsedRecords.stream().filter(r -> r instanceof Beneficiary).map(r -> (Beneficiary) r)
				.findFirst().get();
		beneficiary.setHicnUnhashed(Optional.of(beneficiary.getHicn()));
		beneficiary.setHicn("somehash");

		// Add the HICN history records to the Beneficiary, and fix their HICN fields.
		Set<BeneficiaryHistory> beneficiaryHistories = parsedRecords.stream()
				.filter(r -> r instanceof BeneficiaryHistory).map(r -> (BeneficiaryHistory) r)
				.filter(r -> beneficiary.getBeneficiaryId().equals(r.getBeneficiaryId())).collect(Collectors.toSet());
		beneficiary.getBeneficiaryHistories().addAll(beneficiaryHistories);
		for (BeneficiaryHistory beneficiaryHistory : beneficiary.getBeneficiaryHistories()) {
			beneficiaryHistory.setHicnUnhashed(Optional.of(beneficiaryHistory.getHicn()));
			beneficiaryHistory.setHicn("somehash");
		}

		// Add the MBI history records to the Beneficiary.
		Set<MedicareBeneficiaryIdHistory> beneficiaryMbis = parsedRecords.stream()
				.filter(r -> r instanceof MedicareBeneficiaryIdHistory).map(r -> (MedicareBeneficiaryIdHistory) r)
				.filter(r -> beneficiary.getBeneficiaryId().equals(r.getBeneficiaryId().orElse(null)))
				.collect(Collectors.toSet());
		beneficiary.getMedicareBeneficiaryIdHistories().addAll(beneficiaryMbis);

		return beneficiary;
	}

	/**
	 * Verifies that {@link BeneficiaryTransformer} works correctly when passed
	 * a {@link Beneficiary} where all {@link Optional} fields are set to
	 * {@link Optional#empty()}.
	 */
	@Test
	public void transformBeneficiaryWithAllOptionalsEmpty() {
		List<Object> parsedRecords = ServerTestUtils
				.parseData(Arrays.asList(StaticRifResourceGroup.SAMPLE_A.getResources()));
		Beneficiary beneficiary = parsedRecords.stream().filter(r -> r instanceof Beneficiary).map(r -> (Beneficiary) r)
				.findFirst().get();
		TransformerTestUtils.setAllOptionalsToEmpty(beneficiary);

		Patient patient = BeneficiaryTransformer.transform(new MetricRegistry(), beneficiary,
				IncludeIdentifiersMode.OMIT_HICNS_AND_MBIS);
		assertMatches(beneficiary, patient);
	}

	/**
	 * Verifies that the {@link Patient} "looks like" it should, if it were
	 * produced from the specified {@link Beneficiary}.
	 * 
	 * @param beneficiary
	 *            the {@link Beneficiary} that the {@link Patient} was generated
	 *            from
	 * @param patient
	 *            the {@link Patient} that was generated from the specified
	 *            {@link Beneficiary}
	 */
	static void assertMatches(Beneficiary beneficiary, Patient patient) {
		TransformerTestUtils.assertNoEncodedOptionals(patient);

		Assert.assertEquals(beneficiary.getBeneficiaryId(), patient.getIdElement().getIdPart());
		Assert.assertEquals(1, patient.getAddress().size());
		Assert.assertEquals(beneficiary.getStateCode(), patient.getAddress().get(0).getState());
		Assert.assertEquals(beneficiary.getCountyCode(), patient.getAddress().get(0).getDistrict());
		Assert.assertEquals(beneficiary.getPostalCode(), patient.getAddress().get(0).getPostalCode());
		Assert.assertEquals(Date.valueOf(beneficiary.getBirthDate()), patient.getBirthDate());
		if (beneficiary.getSex() == Sex.MALE.getCode())
			Assert.assertEquals(AdministrativeGender.MALE.toString(), patient.getGender().toString().trim());
		else if (beneficiary.getSex() == Sex.FEMALE.getCode())
			Assert.assertEquals(AdministrativeGender.FEMALE.toString(), patient.getGender().toString().trim());
		TransformerTestUtils.assertExtensionCodingEquals(CcwCodebookVariable.RACE, beneficiary.getRace(), patient);
		Assert.assertEquals(beneficiary.getNameGiven(), patient.getName().get(0).getGiven().get(0).toString());
		if (beneficiary.getNameMiddleInitial().isPresent())
			Assert.assertEquals(beneficiary.getNameMiddleInitial().get().toString(),
					patient.getName().get(0).getGiven().get(1).toString());
		Assert.assertEquals(beneficiary.getNameSurname(), patient.getName().get(0).getFamily());
	}
}
