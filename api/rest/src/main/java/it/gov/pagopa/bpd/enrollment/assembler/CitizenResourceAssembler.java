package it.gov.pagopa.bpd.enrollment.assembler;

import it.gov.pagopa.bpd.enrollment.connector.citizen.model.CitizenResource;
import it.gov.pagopa.bpd.enrollment.model.EnrollmentCitizenResource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * Mapper between <Citizen> Entity class and <CitizenResource> Resource class
 */
@Service
public class CitizenResourceAssembler {

    public EnrollmentCitizenResource toResource(CitizenResource citizenResource) {
        EnrollmentCitizenResource resource = null;

        if (citizenResource != null) {
            resource = new EnrollmentCitizenResource();
            BeanUtils.copyProperties(citizenResource, resource);
        }

        return resource;
    }
}
