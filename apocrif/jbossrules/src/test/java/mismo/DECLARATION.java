//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.1-b02-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.05.18 at 02:48:21 AM BST 
//


package mismo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="AlimonyChildSupportObligationIndicator" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *       &lt;attribute name="BankruptcyIndicator" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *       &lt;attribute name="BorrowedDownPaymentIndicator" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *       &lt;attribute name="CitizenshipResidencyType" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *       &lt;attribute name="CoMakerEndorserOfNoteIndicator" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *       &lt;attribute name="HomeownerPastThreeYearsType" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *       &lt;attribute name="IntentToOccupyType" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *       &lt;attribute name="LoanForeclosureOrJudgementIndicator" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *       &lt;attribute name="OutstandingJudgementsIndicator" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *       &lt;attribute name="PartyToLawsuitIndicator" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *       &lt;attribute name="PresentlyDelinquentIndicator" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *       &lt;attribute name="PriorPropertyTitleType" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *       &lt;attribute name="PriorPropertyUsageType" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *       &lt;attribute name="PropertyForeclosedPastSevenYearsIndicator" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "DECLARATION")
public class DECLARATION {

    @XmlAttribute(name = "AlimonyChildSupportObligationIndicator", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String alimonyChildSupportObligationIndicator;
    @XmlAttribute(name = "BankruptcyIndicator", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String bankruptcyIndicator;
    @XmlAttribute(name = "BorrowedDownPaymentIndicator", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String borrowedDownPaymentIndicator;
    @XmlAttribute(name = "CitizenshipResidencyType", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String citizenshipResidencyType;
    @XmlAttribute(name = "CoMakerEndorserOfNoteIndicator", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String coMakerEndorserOfNoteIndicator;
    @XmlAttribute(name = "HomeownerPastThreeYearsType", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String homeownerPastThreeYearsType;
    @XmlAttribute(name = "IntentToOccupyType", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String intentToOccupyType;
    @XmlAttribute(name = "LoanForeclosureOrJudgementIndicator", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String loanForeclosureOrJudgementIndicator;
    @XmlAttribute(name = "OutstandingJudgementsIndicator", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String outstandingJudgementsIndicator;
    @XmlAttribute(name = "PartyToLawsuitIndicator", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String partyToLawsuitIndicator;
    @XmlAttribute(name = "PresentlyDelinquentIndicator", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String presentlyDelinquentIndicator;
    @XmlAttribute(name = "PriorPropertyTitleType", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String priorPropertyTitleType;
    @XmlAttribute(name = "PriorPropertyUsageType", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String priorPropertyUsageType;
    @XmlAttribute(name = "PropertyForeclosedPastSevenYearsIndicator", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String propertyForeclosedPastSevenYearsIndicator;

    /**
     * Gets the value of the alimonyChildSupportObligationIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAlimonyChildSupportObligationIndicator() {
        return alimonyChildSupportObligationIndicator;
    }

    /**
     * Sets the value of the alimonyChildSupportObligationIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAlimonyChildSupportObligationIndicator(String value) {
        this.alimonyChildSupportObligationIndicator = value;
    }

    /**
     * Gets the value of the bankruptcyIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBankruptcyIndicator() {
        return bankruptcyIndicator;
    }

    /**
     * Sets the value of the bankruptcyIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBankruptcyIndicator(String value) {
        this.bankruptcyIndicator = value;
    }

    /**
     * Gets the value of the borrowedDownPaymentIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBorrowedDownPaymentIndicator() {
        return borrowedDownPaymentIndicator;
    }

    /**
     * Sets the value of the borrowedDownPaymentIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBorrowedDownPaymentIndicator(String value) {
        this.borrowedDownPaymentIndicator = value;
    }

    /**
     * Gets the value of the citizenshipResidencyType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCitizenshipResidencyType() {
        return citizenshipResidencyType;
    }

    /**
     * Sets the value of the citizenshipResidencyType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCitizenshipResidencyType(String value) {
        this.citizenshipResidencyType = value;
    }

    /**
     * Gets the value of the coMakerEndorserOfNoteIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCoMakerEndorserOfNoteIndicator() {
        return coMakerEndorserOfNoteIndicator;
    }

    /**
     * Sets the value of the coMakerEndorserOfNoteIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCoMakerEndorserOfNoteIndicator(String value) {
        this.coMakerEndorserOfNoteIndicator = value;
    }

    /**
     * Gets the value of the homeownerPastThreeYearsType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHomeownerPastThreeYearsType() {
        return homeownerPastThreeYearsType;
    }

    /**
     * Sets the value of the homeownerPastThreeYearsType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHomeownerPastThreeYearsType(String value) {
        this.homeownerPastThreeYearsType = value;
    }

    /**
     * Gets the value of the intentToOccupyType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIntentToOccupyType() {
        return intentToOccupyType;
    }

    /**
     * Sets the value of the intentToOccupyType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIntentToOccupyType(String value) {
        this.intentToOccupyType = value;
    }

    /**
     * Gets the value of the loanForeclosureOrJudgementIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLoanForeclosureOrJudgementIndicator() {
        return loanForeclosureOrJudgementIndicator;
    }

    /**
     * Sets the value of the loanForeclosureOrJudgementIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLoanForeclosureOrJudgementIndicator(String value) {
        this.loanForeclosureOrJudgementIndicator = value;
    }

    /**
     * Gets the value of the outstandingJudgementsIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOutstandingJudgementsIndicator() {
        return outstandingJudgementsIndicator;
    }

    /**
     * Sets the value of the outstandingJudgementsIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOutstandingJudgementsIndicator(String value) {
        this.outstandingJudgementsIndicator = value;
    }

    /**
     * Gets the value of the partyToLawsuitIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPartyToLawsuitIndicator() {
        return partyToLawsuitIndicator;
    }

    /**
     * Sets the value of the partyToLawsuitIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPartyToLawsuitIndicator(String value) {
        this.partyToLawsuitIndicator = value;
    }

    /**
     * Gets the value of the presentlyDelinquentIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPresentlyDelinquentIndicator() {
        return presentlyDelinquentIndicator;
    }

    /**
     * Sets the value of the presentlyDelinquentIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPresentlyDelinquentIndicator(String value) {
        this.presentlyDelinquentIndicator = value;
    }

    /**
     * Gets the value of the priorPropertyTitleType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPriorPropertyTitleType() {
        return priorPropertyTitleType;
    }

    /**
     * Sets the value of the priorPropertyTitleType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPriorPropertyTitleType(String value) {
        this.priorPropertyTitleType = value;
    }

    /**
     * Gets the value of the priorPropertyUsageType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPriorPropertyUsageType() {
        return priorPropertyUsageType;
    }

    /**
     * Sets the value of the priorPropertyUsageType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPriorPropertyUsageType(String value) {
        this.priorPropertyUsageType = value;
    }

    /**
     * Gets the value of the propertyForeclosedPastSevenYearsIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPropertyForeclosedPastSevenYearsIndicator() {
        return propertyForeclosedPastSevenYearsIndicator;
    }

    /**
     * Sets the value of the propertyForeclosedPastSevenYearsIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPropertyForeclosedPastSevenYearsIndicator(String value) {
        this.propertyForeclosedPastSevenYearsIndicator = value;
    }

}