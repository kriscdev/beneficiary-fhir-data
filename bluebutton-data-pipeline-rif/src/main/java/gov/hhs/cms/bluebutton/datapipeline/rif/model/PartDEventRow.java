package gov.hhs.cms.bluebutton.datapipeline.rif.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

/**
 * <p>
 * Models rows from {@link RifFileType#PDE} RIF Files.
 * </p>
 * <p>
 * The RIF file layout used here is specific to the Blue Button API's ETL
 * process. The layouts of these files is detailed in the
 * <code>bluebutton-data-pipeline-rif/dev/rif-record-layout.xlsx</code> file.
 * The columns contained in the files are largely similar to those detailed in
 * <a href="https://www.ccwdata.org/web/guest/data-dictionaries">CCW: Data
 * Dictionaries</a>.
 * </p>
 * 
 * <p>
 * Design Note: This class is too painful to maintain as a bean, so I've
 * stripped it down to just a struct. To be clear, this is <strong>not</strong>
 * immutable and thus <strong>not</strong> thread-safe (and it really shouldn't
 * need to be).
 * </p>
 */
public class PartDEventRow {
	public static final int COMPOUND_CODE_NOT_COMPOUNDED = 1;
	public static final int COMPOUND_CODE_COMPOUNDED = 2;

	public static final String SVC_PRVDR_ID_QLFYR_CD_NPI = "01";
	public static final String SVC_PRVDR_ID_QLFYR_CD_NCPDP = "07";
	public static final String SVC_PRVDR_ID_QLFYR_CD_STLICENSE = "08";
	public static final String SVC_PRVDR_ID_QLFYR_CD_FEDTAX = "11";

	public static final String PRSCRBR_ID_QLFYR_CD_NPI = "01";

	/**
	 * @see Column#VERSION
	 */
	public int version;

	/**
	 * @see Column#DML_IND
	 */
	public RecordAction recordAction;

	/**
	 * @see Column#PDE_ID
	 */
	public String partDEventId;

	/**
	 * @see Column#BENE_ID
	 */
	public String beneficiaryId;

	/**
	 * @see Column#SRVC_DT
	 */
	public LocalDate prescriptionFillDate;

	/**
	 * @see Column#PD_DT
	 */
	public Optional<LocalDate> paymentDate;

	/**
	 * @see Column#SRVC_PRVDR_ID_QLFYR_CD
	 */
	public String serviceProviderIdQualiferCode;

	/**
	 * @see Column#SRVC_PRVDR_ID
	 */
	public String serviceProviderId;

	/**
	 * @see Column#PRSCRBR_ID_QLFYR_CD
	 */
	public String prescriberIdQualifierCode;

	/**
	 * @see Column#PRSCRBR_ID
	 */
	public String prescriberId;

	/**
	 * @see Column#RX_SRVC_RFRNC_NUM
	 */
	public Long prescriptionReferenceNumber;

	/**
	 * @see Column#PROD_SRVC_ID
	 */
	public String nationalDrugCode;

	/**
	 * @see Column#PLAN_CNTRCT_REC_ID
	 */
	public String planContractId;

	/**
	 * @see Column#PLAN_PBP_REC_NUM
	 */
	public String planBenefitPackageId;

	/**
	 * @see Column#CMPND_CD
	 */
	public Integer compoundCode;

	/**
	 * @see Column#DAW_PROD_SLCTN_CD
	 */
	public String dispenseAsWrittenProductSelectionCode;

	/**
	 * @see Column#QTY_DPSNSD_NUM
	 */
	public BigDecimal quantityDispensed;

	/**
	 * @see Column#DAYS_SUPLY_NUM
	 */
	public Integer daysSupply;

	/**
	 * @see Column#FILL_NUM
	 */
	public Integer fillNumber;

	/**
	 * @see Column#DSPNSNG_STUS_CD
	 */
	public Optional<Character> dispensingStatuscode;

	// TODO FIll in rest of fields

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PartDEventRow [version=");
		builder.append(version);
		builder.append(", recordAction=");
		builder.append(recordAction);
		builder.append(", partDEventId=");
		builder.append(partDEventId);
		builder.append(", beneficiaryId=");
		builder.append(beneficiaryId);
		builder.append(", prescriptionFillDate=");
		builder.append(prescriptionFillDate);
		builder.append(", paymentDate=");
		builder.append(paymentDate);
		builder.append(", serviceProviderIdQualiferCode=");
		builder.append(serviceProviderIdQualiferCode);
		builder.append(", serviceProviderId=");
		builder.append(serviceProviderId);
		builder.append(", prescriberIdQualifierCode=");
		builder.append(prescriberIdQualifierCode);
		builder.append(", prescriberId=");
		builder.append(prescriberId);
		builder.append(", prescriptionReferenceNumber=");
		builder.append(prescriptionReferenceNumber);
		builder.append(", nationalDrugCode=");
		builder.append(nationalDrugCode);
		builder.append(", planContractId=");
		builder.append(planContractId);
		builder.append(", planBenefitPackageId=");
		builder.append(planBenefitPackageId);
		builder.append(", compoundCode=");
		builder.append(compoundCode);
		builder.append(", dispenseAsWrittenProductSelectionCode=");
		builder.append(dispenseAsWrittenProductSelectionCode);
		builder.append(", quantityDispensed=");
		builder.append(quantityDispensed);
		builder.append(", daysSupply=");
		builder.append(daysSupply);
		builder.append(", fillNumber=");
		builder.append(fillNumber);
		builder.append(", dispensingStatuscode=");
		builder.append(dispensingStatuscode);
		builder.append("]");
		return builder.toString();
		// TODO FIll in rest of fields
	}

	/**
	 * Enumerates the raw RIF columns represented in {@link PartDEventRow},
	 * where {@link Column#ordinal()} values represent each column's position in
	 * the actual data.
	 */
	public static enum Column {
		/**
		 * Type: (unknown), max chars: (unknown).
		 */
		VERSION,

		/**
		 * Type: (unknown), max chars: (unknown).
		 */
		DML_IND,

		/**
		 * Type: <code>CHAR</code>, max chars: 15. See <a href=
		 * "https://www.ccwdata.org/cs/groups/public/documents/datadictionary/pde_id.txt">
		 * CCW Data Dictionary: PDE_ID</a>, though note that this instance of +
		 * * the field is unencrypted.
		 */
		PDE_ID,

		/**
		 * Type: <code>CHAR</code>, max chars: 15. See <a href=
		 * "https://www.ccwdata.org/cs/groups/public/documents/datadictionary/bene_id.txt">
		 * CCW Data Dictionary: BENE_ID</a>, though note that this instance of +
		 * * the field is unencrypted.
		 */
		BENE_ID,

		/**
		 * Type: <code>DATE</code>, max chars: 8. See <a href=
		 * "https://www.ccwdata.org/cs/groups/public/documents/datadictionary/srvc_dt.txt">
		 * CCW Data Dictionary: SRVC_DT</a>.
		 */
		SRVC_DT,

		/**
		 * Type: <code>DATE</code>, max chars: 8, <code>Optional</code>. See
		 * <a href=
		 * "https://www.ccwdata.org/cs/groups/public/documents/datadictionary/pd_dt.txt">
		 * CCW Data Dictionary: PD_DT</a>.
		 */
		PD_DT,

		/**
		 * Type: <code>CHAR</code>, max chars: 2. See <a href=
		 * "https://www.ccwdata.org/cs/groups/public/documents/datadictionary/srvc_prvdr_id_qlfyr_cd.txt">
		 * CCW Data Dictionary: SRVC_PRVDR_ID_QLFYR_CD</a>.
		 */
		SRVC_PRVDR_ID_QLFYR_CD,

		/**
		 * Type: <code>CHAR</code>, max chars: 15. See <a href=
		 * "https://www.ccwdata.org/cs/groups/public/documents/datadictionary/srvc_prvdr_id.txt">
		 * CCW Data Dictionary: SRVC_PRVDR_ID</a>.
		 */
		SRVC_PRVDR_ID,

		/**
		 * Type: <code>CHAR</code>, max chars: 2. See <a href=
		 * "https://www.ccwdata.org/cs/groups/public/documents/datadictionary/prscrbr_id_qlfyr_cd.txt">
		 * CCW Data Dictionary: PRSCRBR_ID_QLFYR_CD</a>.
		 */
		PRSCRBR_ID_QLFYR_CD,

		/**
		 * Type: <code>CHAR</code>, max chars: 15. See <a href=
		 * "https://www.ccwdata.org/cs/groups/public/documents/datadictionary/prscrbr_id.txt">
		 * CCW Data Dictionary: PRSCRBR_ID</a>.
		 */
		PRSCRBR_ID,

		/**
		 * Type: <code>NUM</code>, max chars: 12. See <a href=
		 * "https://www.ccwdata.org/cs/groups/public/documents/datadictionary/rx_srvc_rfrnc_num.txt">
		 * CCW Data Dictionary: RX_SRVC_RFRNC_NUM</a>.
		 */
		RX_SRVC_RFRNC_NUM,

		/**
		 * Type: <code>CHAR</code>, max chars: 19. See <a href=
		 * "https://www.ccwdata.org/cs/groups/public/documents/datadictionary/prod_srvc_id.txt">
		 * CCW Data Dictionary: PROD_SRVC_ID</a>.
		 */
		PROD_SRVC_ID,

		/**
		 * Type: <code>CHAR</code>, max chars: 5. See <a href=
		 * "https://www.ccwdata.org/cs/groups/public/documents/datadictionary/plan_cntrct_rec_id.txt">
		 * CCW Data Dictionary: PLAN_CNTRCT_REC_ID</a>.
		 */
		PLAN_CNTRCT_REC_ID,

		/**
		 * Type: <code>CHAR</code>, max chars: 3. See <a href=
		 * "https://www.ccwdata.org/cs/groups/public/documents/datadictionary/plan_pbp_rec_num.txt">
		 * CCW Data Dictionary: PLAN_PBP_REC_NUM</a>.
		 */
		PLAN_PBP_REC_NUM,

		/**
		 * Type: <code>NUM</code>, max chars: 2. See <a href=
		 * "https://www.ccwdata.org/cs/groups/public/documents/datadictionary/cmpnd_cd.txt">
		 * CCW Data Dictionary: CMPND_CD</a>.
		 */
		CMPND_CD,

		/**
		 * Type: <code>CHAR</code>, max chars: 1. See <a href=
		 * "https://www.ccwdata.org/cs/groups/public/documents/datadictionary/daw_prod_slctn_cd.txt">
		 * CCW Data Dictionary: DAW_PROD_SLCTN_CD</a>.
		 */
		DAW_PROD_SLCTN_CD,

		/**
		 * Type: <code>NUM</code>, max chars: 12. See <a href=
		 * "https://www.ccwdata.org/cs/groups/public/documents/datadictionary/qty_dspnsd_num.txt">
		 * CCW Data Dictionary: QTY_DPSNSD_NUM</a>.
		 */
		QTY_DPSNSD_NUM,

		/**
		 * Type: <code>NUM</code>, max chars: 3. See <a href=
		 * "https://www.ccwdata.org/cs/groups/public/documents/datadictionary/days_suply_num.txt">
		 * CCW Data Dictionary: DAYS_SUPLY_NUM</a>.
		 */
		DAYS_SUPLY_NUM,

		/**
		 * Type: <code>NUM</code>, max chars: 3. See <a href=
		 * "https://www.ccwdata.org/cs/groups/public/documents/datadictionary/fill_num.txt">
		 * CCW Data Dictionary: FILL_NUM</a>.
		 */
		FILL_NUM,

		/**
		 * Type: <code>CHAR</code>, max chars: 1, <code>Optional</code>. See
		 * <a href=
		 * "https://www.ccwdata.org/cs/groups/public/documents/datadictionary/dspnsng_stus_cd.txt">
		 * CCW Data Dictionary: DSPNSNG_STUS_CD</a>.
		 */
		DSPNSNG_STUS_CD,

		/**
		 * Type: <code>CHAR</code>, max chars: 1. See <a href=
		 * "https://www.ccwdata.org/cs/groups/public/documents/datadictionary/drug_cvrg_stus_cd.txt">
		 * CCW Data Dictionary: DRUG_CVRG_STUS_CD</a>.
		 */
		DRUG_CVRG_STUS_CD,

		/**
		 * Type: <code>CHAR</code>, max chars: 1, <code>Optional</code>. See
		 * <a href=
		 * "https://www.ccwdata.org/cs/groups/public/documents/datadictionary/adjstmt_dltn_cd.txt">
		 * CCW Data Dictionary: ADJSTMT_DLTN_CD</a>.
		 */
		ADJSTMT_DLTN_CD,

		/**
		 * Type: <code>CHAR</code>, max chars: 1, <code>Optional</code>. See
		 * <a href=
		 * "https://www.ccwdata.org/cs/groups/public/documents/datadictionary/nstd_frmt_cd.txt">
		 * CCW Data Dictionary: NSTD_FRMT_CD</a>.
		 */
		NSTD_FRMT_CD,

		/**
		 * Type: <code>CHAR</code>, max chars: 1, <code>Optional</code>. See
		 * <a href=
		 * "https://www.ccwdata.org/cs/groups/public/documents/datadictionary/prcng_excptn_cd.txt">
		 * CCW Data Dictionary: PRCNG_EXCPTN_CD</a>.
		 */
		PRCNG_EXCPTN_CD,

		/**
		 * Type: <code>CHAR</code>, max chars: 1, <code>Optional</code>. See
		 * <a href=
		 * "https://www.ccwdata.org/cs/groups/public/documents/datadictionary/ctstrphc_cvrg_cd.txt">
		 * CCW Data Dictionary: CTSTRPHC_CVRG_CD</a>.
		 */
		CTSTRPHC_CVRG_CD,

		/**
		 * Type: <code>NUM</code>, max chars: 10. See <a href=
		 * "https://www.ccwdata.org/cs/groups/public/documents/datadictionary/gdc_blw_oopt_amt.txt">
		 * CCW Data Dictionary: GDC_BLW_OOPT_AMT</a>.
		 */
		GDC_BLW_OOPT_AMT,

		/**
		 * Type: <code>NUM</code>, max chars: 10. See <a href=
		 * "https://www.ccwdata.org/cs/groups/public/documents/datadictionary/gdc_abv_oopt_amt.txt">
		 * CCW Data Dictionary: GDC_ABV_OOPT_AMT</a>.
		 */
		GDC_ABV_OOPT_AMT,

		/**
		 * Type: <code>NUM</code>, max chars: 10. See <a href=
		 * "https://www.ccwdata.org/cs/groups/public/documents/datadictionary/ptnt_pay_amt.txt">
		 * CCW Data Dictionary: PTNT_PAY_AMT</a>.
		 */
		PTNT_PAY_AMT,

		/**
		 * Type: <code>NUM</code>, max chars: 10. See <a href=
		 * "https://www.ccwdata.org/cs/groups/public/documents/datadictionary/othr_troop_amt.txt">
		 * CCW Data Dictionary: OTHR_TROOP_AMT</a>.
		 */
		OTHR_TROOP_AMT,

		/**
		 * Type: <code>NUM</code>, max chars: 10. See <a href=
		 * "https://www.ccwdata.org/cs/groups/public/documents/datadictionary/lics_amt.txt">
		 * CCW Data Dictionary: LICS_AMT</a>.
		 */
		LICS_AMT,

		/**
		 * Type: <code>NUM</code>, max chars: 10. See <a href=
		 * "https://www.ccwdata.org/cs/groups/public/documents/datadictionary/plro_amt.txt">
		 * CCW Data Dictionary: PLRO_AMT</a>.
		 */
		PLRO_AMT,

		/**
		 * Type: <code>NUM</code>, max chars: 10. See <a href=
		 * "https://www.ccwdata.org/cs/groups/public/documents/datadictionary/cvrd_d_plan_pd_amt.txt">
		 * CCW Data Dictionary: CVRD_D_PLAN_PD_AMT</a>.
		 */
		CVRD_D_PLAN_PD_AMT,

		/**
		 * Type: <code>NUM</code>, max chars: 10. See <a href=
		 * "https://www.ccwdata.org/cs/groups/public/documents/datadictionary/ncvrd_plan_pd_amt.txt">
		 * CCW Data Dictionary: NCVRD_PLAN_PD_AMT</a>.
		 */
		NCVRD_PLAN_PD_AMT,

		/**
		 * Type: <code>NUM</code>, max chars: 10. See <a href=
		 * "https://www.ccwdata.org/cs/groups/public/documents/datadictionary/tot_rx_cst_amt.txt">
		 * CCW Data Dictionary: TOT_RX_CST_AMT</a>.
		 */
		TOT_RX_CST_AMT,

		/**
		 * Type: <code>CHAR</code>, max chars: 1, <code>Optional</code>. See
		 * <a href=
		 * "https://www.ccwdata.org/cs/groups/public/documents/datadictionary/rx_orgn_cd.txt">
		 * CCW Data Dictionary: RX_ORGN_CD</a>.
		 */
		RX_ORGN_CD,

		/**
		 * Type: <code>NUM</code>, max chars: 10. See <a href=
		 * "https://www.ccwdata.org/cs/groups/public/documents/datadictionary/rptd_gap_dscnt_num.txt">
		 * CCW Data Dictionary: RPTD_GAP_DSCNT_NUM</a>.
		 */
		RPTD_GAP_DSCNT_NUM,

		/**
		 * Type: <code>CHAR</code>, max chars: 1. See <a href=
		 * "https://www.ccwdata.org/cs/groups/public/documents/datadictionary/brnd_gnrc_cd.txt">
		 * CCW Data Dictionary: BRND_GNRC_CD</a>.
		 */
		BRND_GNRC_CD,

		/**
		 * Type: <code>CHAR</code>, max chars: 2. See <a href=
		 * "https://www.ccwdata.org/cs/groups/public/documents/datadictionary/phrmcy_srvc_type_cd.txt">
		 * CCW Data Dictionary: PHRMCY_SRVC_TYPE_CD</a>.
		 */
		PHRMCY_SRVC_TYPE_CD,

		/**
		 * Type: <code>CHAR</code>, max chars: 2. See <a href=
		 * "https://www.ccwdata.org/cs/groups/public/documents/datadictionary/ptnt_rsdnc_cd.txt">
		 * CCW Data Dictionary: PTNT_RSDNC_CD</a>.
		 */
		PTNT_RSDNC_CD,

		/**
		 * Type: <code>CHAR</code>, max chars: 2, <code>Optional</code>. See
		 * <a href=
		 * "https://www.ccwdata.org/cs/groups/public/documents/datadictionary/submsn_clr_cd.txt">
		 * CCW Data Dictionary: SUBMSN_CLR_CD</a>.
		 */
		SUBMSN_CLR_CD
	}
}
