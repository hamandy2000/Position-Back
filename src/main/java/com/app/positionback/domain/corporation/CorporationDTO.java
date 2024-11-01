package com.app.positionback.domain.corporation;

import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Getter @Setter @ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class CorporationDTO {
    @EqualsAndHashCode.Include
    private Long id;
    private String corporationName;
    private String corporationAddress;
    private String corporationAddressDetail;
    private String corporationBusiness;
    private String corporationType;
    private String corporationOwner;
    private String corporationEmail;
    private String corporationPassword;
    private String corporationHomepage;
    private String corporationGen;
    private String corporationSales;

    public  CorporationVO toVO() {
        return new CorporationVO(id,corporationName,corporationAddress,corporationAddressDetail,corporationBusiness,
                corporationType,corporationOwner,corporationEmail,corporationPassword,corporationHomepage,corporationGen,corporationSales);
    }
}
