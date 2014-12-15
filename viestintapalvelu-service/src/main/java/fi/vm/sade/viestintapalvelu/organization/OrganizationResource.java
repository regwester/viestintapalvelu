package fi.vm.sade.viestintapalvelu.organization;

import com.wordnik.swagger.annotations.ApiParam;
import fi.vm.sade.authentication.model.OrganisaatioHenkilo;
import fi.vm.sade.viestintapalvelu.AsynchronousResource;
import fi.vm.sade.viestintapalvelu.externalinterface.api.dto.OrganisaatioHierarchyDto;
import fi.vm.sade.viestintapalvelu.externalinterface.component.CurrentUserComponent;
import fi.vm.sade.viestintapalvelu.externalinterface.component.TarjontaComponent;
import fi.vm.sade.viestintapalvelu.externalinterface.organisaatio.OrganisaatioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@PreAuthorize("isAuthenticated()")
@Path("organizationhierarchy" )
public class OrganizationResource extends AsynchronousResource {

    @Autowired
    TarjontaComponent tarjontaComponent;

    @Autowired
    OrganisaatioService organisaatioService;

    @Autowired
    CurrentUserComponent currentUserComponent;

    /**
     *
     * @param applicationPeriod the application period of the haku
     * @return a tree structure of organizations that provide teaching for the selected application period. If an organization
     * provides teaching for the application period, all parent organizations of that one are included in the tree too.
     */
    @GET
    @Produces("application/json")
    @Path("/applicationPeriod/{applicationPeriod}")
    public List<OrganisaatioHierarchyDto> getOrganizationHierarchy(
            @ApiParam(name = "applicationPeriod", value = "haku (OID)", required = true)
            @PathParam("applicationPeriod") String applicationPeriod) {

        /*
        First find a list of all templates for the given application period. After that we use this list to filter
        the big list of different organizations to a group that only have templates for this application period.
         */
        //search for all schools and organizations that provide teaching for the given application period
        //List<LOPDto> providers = learningOpportunityProviderComponent.searchProviders(applicationPeriod, new Locale("fi", "FI")); //todo handle locale
        Set<String> providerOrgIds = tarjontaComponent.findByOid(applicationPeriod);
  /*
        System.out.println("hakuDetails.getNimi().toString() = " + hakuDetails.getNimi().toString());
        Set<String> providerOrgIds = new HashSet<String>();

        if(hakuDetails.getOrganisaatioOids() != null ) {
            for (String oid : hakuDetails.getOrganisaatioOids()) {
                providerOrgIds.add(oid);
            }
        }
*/
        List<OrganisaatioHierarchyDto> userRootOrganizations = new ArrayList<OrganisaatioHierarchyDto>();
        List<OrganisaatioHenkilo> currentUserOrganizations = currentUserComponent.getCurrentUserOrganizations();

        for(OrganisaatioHenkilo orgHenkilo : currentUserOrganizations) {
            String organisaatioOid = orgHenkilo.getOrganisaatioOid();
            OrganisaatioHierarchyDto root = organisaatioService.getOrganizationHierarchy(organisaatioOid);

            if(root == null)
                continue;
            /*
            Depth first search and search if the org OID is one that has templates.
            If a leaf node has templates assigned to it, we need to include all parents of the hierarchy.
             */
            filterHierarchy(root, providerOrgIds);
            userRootOrganizations.add(root);
        }

        return userRootOrganizations;
    }

    /*
    * Checks if Node has any children that are in the OIDs set. Returns true if set contains at least one child, false
    */
    private boolean filterHierarchy(OrganisaatioHierarchyDto node, Set<String> OIDs) {
        if(OIDs == null) return false;
        if(node.getChildren() == null || node.getChildren().isEmpty())
            return OIDs.contains(node.getOid());

        boolean anyChildInOids = false;
        List<OrganisaatioHierarchyDto> childrenWithoutMatches = new ArrayList<OrganisaatioHierarchyDto>();
        for(OrganisaatioHierarchyDto child : node.getChildren()) {
            boolean someChildInOids = filterHierarchy(child, OIDs);
            if(!anyChildInOids) {
                anyChildInOids = someChildInOids;
            }
            if(!someChildInOids) { //the current child didn't have any, so we remove it from this parent
                childrenWithoutMatches.add(child); //remove all these after for each loop
            }
        }
        node.getChildren().removeAll(childrenWithoutMatches);
        return anyChildInOids;
    }

}