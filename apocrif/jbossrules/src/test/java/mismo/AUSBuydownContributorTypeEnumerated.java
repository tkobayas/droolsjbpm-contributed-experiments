//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.1.3-b01-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.05.21 at 09:59:49 PM BST 
//


package mismo;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AUS_BuydownContributorTypeEnumerated.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="AUS_BuydownContributorTypeEnumerated">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Borrower"/>
 *     &lt;enumeration value="Other"/>
 *     &lt;enumeration value="LenderPremiumFinanced"/>
 *     &lt;enumeration value="Seller"/>
 *     &lt;enumeration value="Builder"/>
 *     &lt;enumeration value="Employer"/>
 *     &lt;enumeration value="Financed"/>
 *     &lt;enumeration value="NonParentRelative"/>
 *     &lt;enumeration value="Parent"/>
 *     &lt;enumeration value="Unassigned"/>
 *     &lt;enumeration value="UnrelatedFriend"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "AUS_BuydownContributorTypeEnumerated")
@XmlEnum
public enum AUSBuydownContributorTypeEnumerated {

    @XmlEnumValue("Borrower")
    BORROWER("Borrower"),
    @XmlEnumValue("Other")
    OTHER("Other"),
    @XmlEnumValue("LenderPremiumFinanced")
    LENDER_PREMIUM_FINANCED("LenderPremiumFinanced"),
    @XmlEnumValue("Seller")
    SELLER("Seller"),
    @XmlEnumValue("Builder")
    BUILDER("Builder"),
    @XmlEnumValue("Employer")
    EMPLOYER("Employer"),
    @XmlEnumValue("Financed")
    FINANCED("Financed"),
    @XmlEnumValue("NonParentRelative")
    NON_PARENT_RELATIVE("NonParentRelative"),
    @XmlEnumValue("Parent")
    PARENT("Parent"),
    @XmlEnumValue("Unassigned")
    UNASSIGNED("Unassigned"),
    @XmlEnumValue("UnrelatedFriend")
    UNRELATED_FRIEND("UnrelatedFriend");
    private final String value;

    AUSBuydownContributorTypeEnumerated(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AUSBuydownContributorTypeEnumerated fromValue(String v) {
        for (AUSBuydownContributorTypeEnumerated c: AUSBuydownContributorTypeEnumerated.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}