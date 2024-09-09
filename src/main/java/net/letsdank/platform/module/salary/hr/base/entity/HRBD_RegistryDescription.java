package net.letsdank.platform.module.salary.hr.base.entity;

import lombok.Getter;
import lombok.Setter;
import net.letsdank.platform.utils.platform.metadata.registry.InfoRegistryPeriodicity;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class HRBD_RegistryDescription {
    /**
     * Registry name.
     */
    private String registryName;

    /**
     * Registry type (<code>"InfoRegister"</code> or <code>"AccumulationRegister"</code>.
     */
    private String registryType;

    /**
     * List of names of dimensions.
     */
    private List<String> dimensions;

    /**
     * List of names of resources that doesn't have a pair of ...onEnd, exc. validTo.
     * TODO: What does that mean?
     */
    private List<String> resources;

    /**
     * List of names of requests.
     */
    private List<String> requests;

    /**
     * List of names of standard requests.
     */
    private List<String> standardRequests;

    /**
     * Map that contains a pair of dimension name in upper case, and a boolean value - <code>true</code>
     */
    private Map<String, Boolean> dimensionsForSearch;

    /**
     * (for info register) List of names of resources that have a pair of ...onEnd
     */
    private List<String> returnedResources;

    /**
     * InfoRegistryPeriodicity.
     */
    private InfoRegistryPeriodicity periodicity;

    /**
     * List of names of dimensions that will be performed filtering.
     */
    private List<String> filterDimensions;

    private boolean hasReturnEvents;

    public HRBD_RegistryDescription(String registryName) {
        this(registryName, true);
    }

    public HRBD_RegistryDescription(String registryName, boolean excludeUnused) {
        // TODO: Need a register metadata, that we can't have it actually right now...
        this.registryName = registryName;

    }
}
