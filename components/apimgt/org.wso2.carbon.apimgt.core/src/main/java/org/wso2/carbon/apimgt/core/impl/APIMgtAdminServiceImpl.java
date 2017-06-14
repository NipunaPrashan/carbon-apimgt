package org.wso2.carbon.apimgt.core.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.carbon.apimgt.core.api.APIMgtAdminService;
import org.wso2.carbon.apimgt.core.dao.APISubscriptionDAO;
import org.wso2.carbon.apimgt.core.dao.ApiDAO;
import org.wso2.carbon.apimgt.core.dao.LabelDAO;
import org.wso2.carbon.apimgt.core.dao.PolicyDAO;
import org.wso2.carbon.apimgt.core.exception.APIConfigRetrievalException;
import org.wso2.carbon.apimgt.core.exception.APIManagementException;
import org.wso2.carbon.apimgt.core.exception.APIMgtDAOException;
import org.wso2.carbon.apimgt.core.exception.ExceptionCodes;
import org.wso2.carbon.apimgt.core.models.API;
import org.wso2.carbon.apimgt.core.models.APISummary;
import org.wso2.carbon.apimgt.core.models.Label;
import org.wso2.carbon.apimgt.core.models.SubscriptionValidationData;
import org.wso2.carbon.apimgt.core.models.UriTemplate;
import org.wso2.carbon.apimgt.core.models.policy.APIPolicy;
import org.wso2.carbon.apimgt.core.models.policy.ApplicationPolicy;
import org.wso2.carbon.apimgt.core.models.policy.Policy;
import org.wso2.carbon.apimgt.core.models.policy.SubscriptionPolicy;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Implementation of APIMgtAdminService
 */
public class APIMgtAdminServiceImpl implements APIMgtAdminService {

    private static final Logger log = LoggerFactory.getLogger(APIStoreImpl.class);

    private APISubscriptionDAO apiSubscriptionDAO;
    private PolicyDAO policyDAO;
    private ApiDAO apiDAO;
    private LabelDAO labelDAO;

    public APIMgtAdminServiceImpl(APISubscriptionDAO apiSubscriptionDAO, PolicyDAO policyDAO, ApiDAO apiDAO,
                                  LabelDAO labelDAO) {
        this.apiSubscriptionDAO = apiSubscriptionDAO;
        this.policyDAO = policyDAO;
        this.apiDAO = apiDAO;
        this.labelDAO = labelDAO;
    }

    /**
     * @see APIMgtAdminService#getPoliciesByLevel(PolicyLevel)
     */
    @Override
    public List<Policy> getPoliciesByLevel(PolicyLevel policyLevel) throws APIManagementException {
        try {
            return policyDAO.getPoliciesByLevel(policyLevel);

        } catch (APIMgtDAOException e) {
            String errorMessage = "Couldn't retrieve Throttle Policies with level: " + policyLevel.name();
            log.error(errorMessage, e);
            throw new APIManagementException(errorMessage, e, e.getErrorHandler());
        }
    }

    /**
     * @see APIMgtAdminService#getPolicyByLevelAndName(PolicyLevel, String)
     */
    @Override
    public Policy getPolicyByLevelAndName(PolicyLevel policyLevel, String policyName) throws APIManagementException {
        try {
            return policyDAO.getPolicyByLevelAndName(policyLevel, policyName);

        } catch (APIMgtDAOException e) {
            String errorMessage = "Couldn't retrieve Throttle Policy with level: " + policyLevel.name() + ", name: "
                    + policyName;
            log.error(errorMessage, e);
            throw new APIManagementException(errorMessage, e.getErrorHandler());
        }
    }

    /**
     * @see APIMgtAdminService#getAPISubscriptions(int)
     */
    @Override
    public List<SubscriptionValidationData> getAPISubscriptions(int limit) throws APIManagementException {
        return apiSubscriptionDAO.getAPISubscriptionsOfAPIForValidation(limit);
    }

    /**
     * @see APIMgtAdminService#getAPISubscriptionsOfApi(String, String)
     */
    @Override
    public List<SubscriptionValidationData> getAPISubscriptionsOfApi(String apiContext, String apiVersion)
            throws APIManagementException {
        return apiSubscriptionDAO.getAPISubscriptionsOfAPIForValidation(apiContext, apiVersion);
    }

    /**
     * @see APIMgtAdminService#getAPIInfo()
     */
    @Override
    public List<APISummary> getAPIInfo() throws APIManagementException {
        List<API> apiList = apiDAO.getAPIs();
        List<APISummary> apiSummaryList = new ArrayList<APISummary>();
        apiList.forEach(apiInfo -> {
            APISummary apiSummary = new APISummary(apiInfo.getId());
            apiSummary.setName(apiInfo.getName());
            apiSummary.setContext(apiInfo.getContext());
            apiSummary.setVersion(apiInfo.getVersion());
            apiSummary.setUriTemplates(new ArrayList<>(apiInfo.getUriTemplates().values()));
            apiSummaryList.add(apiSummary);
        });
        return apiSummaryList;
    }

    /**
     * @see APIMgtAdminService#addApiPolicy(APIPolicy)
     */
    @Override
    public String addApiPolicy(APIPolicy policy) throws APIManagementException {
        try {
            String policyUuid = policy.getUuid();
            if (policyUuid == null) {
                policyUuid = UUID.randomUUID().toString();
                policy.setUuid(policyUuid);
            }
            policyDAO.addApiPolicy(policy);
            return policyUuid;

        } catch (APIMgtDAOException e) {
            String errorMessage = "Couldn't add API policy for uuid: " + policy.getUuid();
            log.error(errorMessage, e);
            throw new APIManagementException(errorMessage, e, e.getErrorHandler());
        }
    }

    /**
     * @see APIMgtAdminService#addApplicationPolicy(ApplicationPolicy)
     */
    @Override
    public String addApplicationPolicy(ApplicationPolicy policy) throws APIManagementException {
        try {
            String policyUuid = policy.getUuid();
            if (policyUuid == null) {
                policyUuid = UUID.randomUUID().toString();
                policy.setUuid(policyUuid);
            }
            policyDAO.addApplicationPolicy(policy);
            return policyUuid;

        } catch (APIMgtDAOException e) {
            String errorMessage = "Couldn't add Application for uuid: " + policy.getUuid();
            log.error(errorMessage, e);
            throw new APIManagementException(errorMessage, e, e.getErrorHandler());
        }
    }

    /**
     * @see APIMgtAdminService#addSubscriptionPolicy(SubscriptionPolicy)
     */
    @Override
    public String addSubscriptionPolicy(SubscriptionPolicy policy) throws APIManagementException {
        try {
            String policyUuid = policy.getUuid();
            if (policyUuid == null) {
                policyUuid = UUID.randomUUID().toString();
                policy.setUuid(policyUuid);
            }
            policyDAO.addSubscriptionPolicy(policy);
            return policyUuid;

        } catch (APIMgtDAOException e) {
            String errorMessage = "Couldn't add Subscription policy for uuid: " + policy.getUuid();
            log.error(errorMessage, e);
            throw new APIManagementException(errorMessage, e, e.getErrorHandler());
        }
    }

    /**
     * @see APIMgtAdminService#updateApiPolicy(APIPolicy)
     */
    @Override
    public void updateApiPolicy(APIPolicy policy) throws APIManagementException {
        try {
            policyDAO.updateApiPolicy(policy);

        } catch (APIMgtDAOException e) {
            String errorMessage = "Couldn't update API policy for uuid: " + policy.getUuid();
            log.error(errorMessage, e);
            throw new APIManagementException(errorMessage, e, e.getErrorHandler());
        }
    }

    /**
     * @see APIMgtAdminService#updateSubscriptionPolicy(SubscriptionPolicy)
     */
    @Override
    public void updateSubscriptionPolicy(SubscriptionPolicy policy) throws APIManagementException {
        try {
            policyDAO.updateSubscriptionPolicy(policy);

        } catch (APIMgtDAOException e) {
            String errorMessage = "Couldn't update Subscription policy for uuid: " + policy.getUuid();
            log.error(errorMessage, e);
            throw new APIManagementException(errorMessage, e, e.getErrorHandler());
        }
    }

    /**
     * @see APIMgtAdminService#updateApplicationPolicy(ApplicationPolicy)
     */
    @Override
    public void updateApplicationPolicy(ApplicationPolicy policy) throws APIManagementException {
        try {
            policyDAO.updateApplicationPolicy(policy);

        } catch (APIMgtDAOException e) {
            String errorMessage = "Couldn't update Application policy for uuid: " + policy.getUuid();
            log.error(errorMessage, e);
            throw new APIManagementException(errorMessage, e, e.getErrorHandler());
        }
    }

    /**
     * @see APIMgtAdminService#deletePolicy(String, PolicyLevel)
     */
    @Override
    public void deletePolicy(String policyName, PolicyLevel policyLevel) throws APIManagementException {
        try {
            policyDAO.deletePolicy(policyLevel, policyName);

        } catch (APIMgtDAOException e) {
            String errorMessage = "Couldn't update application policy with name: " + policyName + ", level: " +
                    policyLevel;
            log.error(errorMessage, e);
            throw new APIManagementException(errorMessage, e, e.getErrorHandler());
        }
    }

    /**
     * @see APIMgtAdminService#deletePolicyByUuid(String, PolicyLevel)
     */
    @Override
    public void deletePolicyByUuid(String uuid, PolicyLevel policyLevel) throws APIManagementException {
        try {
            policyDAO.deletePolicyByUuid(policyLevel, uuid);

        } catch (APIMgtDAOException e) {
            String errorMessage = "Couldn't update application policy with id: " + uuid + ", level: " + policyLevel;
            log.error(errorMessage, e);
            throw new APIManagementException(errorMessage, e, e.getErrorHandler());
        }
    }

    /**
     * @see APIMgtAdminService#getApiPolicy(String)
     */
    @Override
    public APIPolicy getApiPolicy(String policyName) throws APIManagementException {

        try {
            return policyDAO.getApiPolicy(policyName);

        } catch (APIMgtDAOException e) {
            String errorMessage = "Couldn't retrieve API policy with name: " + policyName;
            log.error(errorMessage, e);
            throw new APIManagementException(errorMessage, e, e.getErrorHandler());
        }
    }

    /**
     * @see APIMgtAdminService#getSubscriptionPolicy(String)
     */
    @Override
    public SubscriptionPolicy getSubscriptionPolicy(String policyName) throws APIManagementException {

        try {
            return policyDAO.getSubscriptionPolicy(policyName);

        } catch (APIMgtDAOException e) {
            String errorMessage = "Couldn't retrieve Subscription policy with name: " + policyName;
            log.error(errorMessage, e);
            throw new APIManagementException(errorMessage, e, e.getErrorHandler());
        }
    }

    /**
     * @see APIMgtAdminService#getApplicationPolicy(String)
     */
    @Override
    public ApplicationPolicy getApplicationPolicy(String policyName) throws APIManagementException {

        try {
            return policyDAO.getApplicationPolicy(policyName);

        } catch (APIMgtDAOException e) {
            String errorMessage = "Couldn't retrieve Application policy with name: " + policyName;
            log.error(errorMessage, e);
            throw new APIManagementException(errorMessage, e, e.getErrorHandler());
        }
    }

    /**
     * @see APIMgtAdminService#getApiPolicyByUuid(String)
     */
    @Override
    public APIPolicy getApiPolicyByUuid(String uuid) throws APIManagementException {
        try {
            return policyDAO.getApiPolicyByUuid(uuid);

        } catch (APIMgtDAOException e) {
            String errorMessage = "Couldn't retrieve API policy with id: " + uuid;
            log.error(errorMessage, e);
            throw new APIManagementException(errorMessage, e, e.getErrorHandler());
        }
    }

    /**
     * @see APIMgtAdminService#getApplicationPolicyByUuid(String)
     */
    @Override
    public ApplicationPolicy getApplicationPolicyByUuid(String uuid) throws APIManagementException {
        try {
            return policyDAO.getApplicationPolicyByUuid(uuid);

        } catch (APIMgtDAOException e) {
            String errorMessage = "Couldn't retrieve Application policy with id: " + uuid;
            log.error(errorMessage, e);
            throw new APIManagementException(errorMessage, e, e.getErrorHandler());
        }

    }

    /**
     * @see APIMgtAdminService#getSubscriptionPolicyByUuid(String)
     */
    @Override
    public SubscriptionPolicy getSubscriptionPolicyByUuid(String uuid) throws APIManagementException {
        try {
            return policyDAO.getSubscriptionPolicyByUuid(uuid);

        } catch (APIMgtDAOException e) {
            String errorMessage = "Couldn't retrieve Subscription policy with id: " + uuid;
            log.error(errorMessage, e);
            throw new APIManagementException(errorMessage, e, e.getErrorHandler());
        }
    }

    /**
     * @see APIMgtAdminService#getApiPolicies()
     */
    @Override
    public List<APIPolicy> getApiPolicies() throws APIManagementException {
        try {
            return policyDAO.getApiPolicies();
        } catch (APIMgtDAOException e) {
            String errorMessage = "Couldn't retrieve API policies";
            log.error(errorMessage, e);
            throw new APIManagementException(errorMessage, e, e.getErrorHandler());
        }
    }

    /**
     * @see APIMgtAdminService#getApplicationPolicies()
     */
    @Override
    public List<ApplicationPolicy> getApplicationPolicies() throws APIManagementException {
        try {
            return policyDAO.getApplicationPolicies();
        } catch (APIMgtDAOException e) {
            String errorMessage = "Couldn't retrieve Application policies";
            log.error(errorMessage, e);
            throw new APIManagementException(errorMessage, e, e.getErrorHandler());
        }
    }

    /**
     * @see APIMgtAdminService#getSubscriptionPolicies()
     */
    @Override
    public List<SubscriptionPolicy> getSubscriptionPolicies() throws APIManagementException {
        try {
            return policyDAO.getSubscriptionPolicies();
        } catch (APIMgtDAOException e) {
            String errorMessage = "Couldn't retrieve Subscription policies";
            log.error(errorMessage, e);
            throw new APIManagementException(errorMessage, e, e.getErrorHandler());
        }
    }

    /**
     * @see org.wso2.carbon.apimgt.core.api.APIMgtAdminService#deleteLabel(String)
     */
    @Override
    public void deleteLabel(String labelId) throws APIManagementException {

        try {
            labelDAO.deleteLabel(labelId);
        } catch (APIMgtDAOException e) {
            String msg = "Error occurred while deleting label [labelId] " + labelId;
            log.error(msg, e);
            throw new APIManagementException(msg, ExceptionCodes.APIMGT_DAO_EXCEPTION);
        }
    }

    /**
     * @see org.wso2.carbon.apimgt.core.api.APIMgtAdminService#registerGatewayLabels(List, String)
     */
    @Override
    public void registerGatewayLabels(List<Label> labels, String overwriteLabels) throws APIManagementException {

        if (!labels.isEmpty()) {
            List<String> labelNames = new ArrayList<>();
            boolean overwriteValues = Boolean.parseBoolean(overwriteLabels);

            for (Label label : labels) {
                labelNames.add(label.getName());
            }

            try {
                List<Label> existingLabels = labelDAO.getLabelsByName(labelNames);

                if (!existingLabels.isEmpty()) {
                    List<Label> labelsToRemove = new ArrayList<>();

                    for (Label existingLabel : existingLabels) {
                        for (Label label : labels) {
                            if (existingLabel.getName().equals(label.getName())) {
                                if (overwriteValues) {
                                    labelDAO.updateLabel(label);
                                }
                                labelsToRemove.add(label);
                            }
                        }
                    }
                    labels.removeAll(labelsToRemove);    // Remove already existing labels from the list
                }
                labelDAO.addLabels(labels);
            } catch (APIMgtDAOException e) {
                String msg = "Error occurred while registering gateway labels";
                log.error(msg, e);
                throw new APIManagementException(msg, ExceptionCodes.APIMGT_DAO_EXCEPTION);
            }
        }

    }

    /**
     * @see org.wso2.carbon.apimgt.core.api.APIMgtAdminService#getAPIGatewayServiceConfig(String) (String)
     */
    @Override
    public String getAPIGatewayServiceConfig(String apiId) throws APIConfigRetrievalException {
        try {
            return apiDAO.getGatewayConfigOfAPI(apiId);
        } catch (APIMgtDAOException e) {
            String errorMessage = "Couldn't retrieve gateway configuration for apiId " + apiId;
            log.error(errorMessage, e);
            throw new APIConfigRetrievalException(errorMessage, ExceptionCodes.APIMGT_DAO_EXCEPTION);
        }
    }

    /**
     * @see org.wso2.carbon.apimgt.core.api.APIMgtAdminService#getAllResourcesForApi(String, String)
     */
    @Override
    public List<UriTemplate> getAllResourcesForApi(String apiContext, String apiVersion) throws APIManagementException {
        try {
            return apiDAO.getResourcesOfApi(apiContext, apiVersion);
        } catch (APIManagementException e) {
            String msg = "Couldn't retrieve resources for Api Name: " + apiContext;
            log.error(msg, e);
            throw new APIManagementException(msg, e, ExceptionCodes.APIMGT_DAO_EXCEPTION);
        }
    }

    @Override public List<API> getAPIsByStatus(List<String> gatewayLabels, String status)
            throws APIManagementException {
        List<API> apiList;
        try {
            if (gatewayLabels != null && status != null) {
                apiList = apiDAO.getAPIsByStatus(gatewayLabels, status);
            } else {
                if (gatewayLabels == null) {
                    String msg = "Gateway labels cannot be null";
                    log.error(msg);
                    throw new APIManagementException(msg, ExceptionCodes.GATEWAY_LABELS_CANNOT_BE_NULL);
                } else {
                    String msg = "Status cannot be null";
                    log.error(msg);
                    throw new APIManagementException(msg, ExceptionCodes.STATUS_CANNOT_BE_NULL);
                }
            }
        } catch (APIMgtDAOException e) {
            String msg = "Error occurred while getting the API list in given states";
            log.error(msg, e);
            throw new APIManagementException(msg, ExceptionCodes.APIM_DAO_EXCEPTION);
        }
        return apiList;
    }

    @Override public List<API> getAPIsByGatewayLabel(List<String> gatewayLabels) throws APIManagementException {
        List<API> apiList;
        try {
            if (gatewayLabels != null) {
                apiList = apiDAO.getAPIsByGatewayLabel(gatewayLabels);
            } else {
                String msg = "Gateway labels cannot be null";
                log.error(msg);
                throw new APIManagementException(msg, ExceptionCodes.GATEWAY_LABELS_CANNOT_BE_NULL);
            }
        } catch (APIMgtDAOException e) {
            String msg = "Error occurred while getting the API list in given gateway labels";
            log.error(msg, e);
            throw new APIManagementException(msg, ExceptionCodes.APIM_DAO_EXCEPTION);
        }
        return apiList;
    }

}
