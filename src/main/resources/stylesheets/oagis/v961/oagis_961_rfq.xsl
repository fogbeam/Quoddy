<xsl:stylesheet	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
				xmlns:oagis="http://www.openapplications.org/oagis/9" 
				xmlns:java="http://xml.apache.org/xalan/java"
				exclude-result-prefixes="xsl oagis java"
				version="1.0">
	
	<xsl:output method="html" />
	
	<xsl:template match="/oagis:RFQ">
		<div class="oagisRFQ">
			<div class="oagisRFQHeader">
				<xsl:apply-templates select="oagis:RFQHeader"/>
			</div>
			<div class="oagisRFQLines">
				<xsl:apply-templates select="oagis:RFQLine"/>
			</div>
		</div>
	</xsl:template>
	<xsl:template match="oagis:RFQHeader">

		<ul>
			<li>Customer: <xsl:value-of select="oagis:CustomerParty/oagis:CustomerAccountID"/></li>
			<li>Document Date: <xsl:value-of select="oagis:DocumentDateTime"/></li>
			<li>Catalog Reference: <xsl:value-of select="oagis:CatalogReference/oagis:DocumentID/oagis:ID" /></li>
			<li>Buyer Category: <xsl:value-of select="oagis:BuyerParty/oagis:AccountID" /></li>
		</ul>
		<ul> 
			<li>Total Amount: <xsl:value-of select="java:org.apache.commons.lang.StringUtils.trim( oagis:TotalAmount)" /></li>
			<li>Qualified Amount: <xsl:value-of select="java:org.apache.commons.lang.StringUtils.trim( oagis:QualifiedAmount)" /></li>
		</ul>
		<ul>
			<li>Partial Shipment Allowed: <xsl:value-of select="java:org.apache.commons.lang.StringUtils.trim(oagis:PartialShipmentAllowedIndicator)" /></li>
			<li>Drop Shipment Allowed: <xsl:value-of select="java:org.apache.commons.lang.StringUtils.trim(oagis:DropShipmentAllowedIndicator)" /></li>
			<li>Early Shipment Allowed: <xsl:value-of select="java:org.apache.commons.lang.StringUtils.trim(oagis:EarlyShipmentAllowedIndicator)" /></li>
		</ul>
		
	</xsl:template>
	
	<xsl:template match="oagis:RFQLine">
		<ul>
			<li>Manufacturer ID: <xsl:value-of select="oagis:Item/oagis:ManufacturerItemID/oagis:ID" /> </li>
			<li>Quantity: <xsl:value-of select="oagis:Quantity" /> </li>
			<li>Extended Amount: <xsl:value-of select="oagis:ExtendedAmount" /></li>
		</ul>
	</xsl:template>
</xsl:stylesheet>