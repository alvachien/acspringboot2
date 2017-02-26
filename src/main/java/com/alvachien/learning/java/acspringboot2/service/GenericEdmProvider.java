package com.alvachien.learning.java.acspringboot2.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.alvachien.learning.java.acspringboot2.model.*;
import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlAbstractEdmProvider;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityContainer;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityContainerInfo;
import org.apache.olingo.commons.api.edm.provider.CsdlEntitySet;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityType;
import org.apache.olingo.commons.api.edm.provider.CsdlProperty;
import org.apache.olingo.commons.api.edm.provider.CsdlPropertyRef;
import org.apache.olingo.commons.api.edm.provider.CsdlSchema;
import org.apache.olingo.commons.api.edm.provider.CsdlNavigationProperty;
import org.apache.olingo.commons.api.edm.provider.CsdlNavigationPropertyBinding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

public class GenericEdmProvider extends CsdlAbstractEdmProvider {
	@Autowired
	private ApplicationContext ctx;

	@Override
	public List<CsdlSchema> getSchemas() {

		// create Schema
		CsdlSchema schema = new CsdlSchema();
		schema.setNamespace(Constants.NAMESPACE);

		// add EntityTypes
		List<CsdlEntityType> entityTypes = new ArrayList<CsdlEntityType>();
		entityTypes.add(getEntityType(Constants.ET_PRODUCT_FQN));
		entityTypes.add(getEntityType(Constants.ET_FINACNTCTGY_FQN));
		entityTypes.add(getEntityType(Constants.ET_FINACNT_FQN));
		entityTypes.add(getEntityType(Constants.ET_FINACNTEXTDP_FQN));
		schema.setEntityTypes(entityTypes);

		// add EntityContainer
		schema.setEntityContainer(getEntityContainer());

		// finally
		List<CsdlSchema> schemas = new ArrayList<CsdlSchema>();
		schemas.add(schema);

		return schemas;
	}

	@Override
	public CsdlEntityType getEntityType(FullQualifiedName entityTypeName) {

		CsdlEntityType entityType = null;

		// this method is called for one of the EntityTypes that are configured
		// in the Schema
		if (entityTypeName.equals(Constants.ET_PRODUCT_FQN)) { // Product

			// create EntityType properties
			CsdlProperty id = new CsdlProperty().setName("ID")
					.setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
			CsdlProperty name = new CsdlProperty().setName("Name")
					.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty description = new CsdlProperty().setName("Description")
					.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());

			// create CsdlPropertyRef for Key element
			CsdlPropertyRef propertyRef = new CsdlPropertyRef();
			propertyRef.setName("ID");

			// configure EntityType
			entityType = new CsdlEntityType();
			entityType.setName(Constants.ET_PRODUCT_NAME);
			entityType.setProperties(Arrays.asList(id, name, description));
			entityType.setKey(Arrays.asList(propertyRef));
		} else if (entityTypeName.equals(Constants.ET_FINACNTCTGY_FQN)) { // Account
																			// category
			// create EntityType properties
			CsdlProperty id = new CsdlProperty().setName("ID")
					.setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
			CsdlProperty name = new CsdlProperty().setName("NAME")
					.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty assetflg = new CsdlProperty().setName("ASSETFLAG")
					.setType(EdmPrimitiveTypeKind.Boolean.getFullQualifiedName());
			CsdlProperty comment = new CsdlProperty().setName("COMMENT")
					.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty sysflg = new CsdlProperty().setName("SYSFLAG")
					.setType(EdmPrimitiveTypeKind.Boolean.getFullQualifiedName());
			CsdlProperty crtedby = new CsdlProperty().setName("CREATEDBY")
					.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty crtedat = new CsdlProperty().setName("CREATEDAT")
					.setType(EdmPrimitiveTypeKind.Date.getFullQualifiedName());
			CsdlProperty updedby = new CsdlProperty().setName("UPDATEDBY")
					.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty updedat = new CsdlProperty().setName("UPDATEDAT")
					.setType(EdmPrimitiveTypeKind.Date.getFullQualifiedName());

			// create CsdlPropertyRef for Key element
			CsdlPropertyRef propertyRef = new CsdlPropertyRef();
			propertyRef.setName("ID");

			// Navigation to Account: one-to-many
			CsdlNavigationProperty navProp = new CsdlNavigationProperty().setName(Constants.NN_FINACCOUNTS)
					.setType(Constants.ET_FINACNT_FQN).setCollection(true)
			// .setPartner("Category")
			;
			List<CsdlNavigationProperty> navPropList = new ArrayList<CsdlNavigationProperty>();
			navPropList.add(navProp);

			// configure EntityType
			entityType = new CsdlEntityType();
			entityType.setName(Constants.ET_FINACNTCTGY_NAME);
			entityType.setProperties(
					Arrays.asList(id, name, assetflg, comment, sysflg, crtedby, crtedat, updedby, updedat));
			entityType.setKey(Arrays.asList(propertyRef));
			entityType.setNavigationProperties(navPropList);
		} else if (entityTypeName.equals(Constants.ET_FINACNT_FQN)) { // Account
			// create EntityType properties
			CsdlProperty id = new CsdlProperty().setName("ID")
					.setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
			CsdlProperty ctgyid = new CsdlProperty().setName("CTGYID")
					.setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
			CsdlProperty name = new CsdlProperty().setName("NAME")
					.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty comment = new CsdlProperty().setName("COMMENT")
					.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty owner = new CsdlProperty().setName("OWNER")
					.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty crtedby = new CsdlProperty().setName("CREATEDBY")
					.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty crtedat = new CsdlProperty().setName("CREATEDAT")
					.setType(EdmPrimitiveTypeKind.Date.getFullQualifiedName());
			CsdlProperty updedby = new CsdlProperty().setName("UPDATEDBY")
					.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty updedat = new CsdlProperty().setName("UPDATEDAT")
					.setType(EdmPrimitiveTypeKind.Date.getFullQualifiedName());

			// create CsdlPropertyRef for Key element
			CsdlPropertyRef propertyRef = new CsdlPropertyRef();
			propertyRef.setName("ID");

			// Navigation to category: many-to-one, null not allowed (account
			// must have a category)
			CsdlNavigationProperty navProp = new CsdlNavigationProperty().setName(Constants.NN_FINACNTCTGY)
					.setType(Constants.ET_FINACNTCTGY_FQN).setNullable(false);
			// .setPartner("Account");
			List<CsdlNavigationProperty> navPropList = new ArrayList<CsdlNavigationProperty>();
			navPropList.add(navProp);

			// Navigate to Ext. DP: 0..1
			navProp = new CsdlNavigationProperty().setName(Constants.NN_FINACNTEXTDP)
					.setType(Constants.ET_FINACNTEXTDP_FQN).setNullable(true);
			navPropList.add(navProp);

			// configure EntityType
			entityType = new CsdlEntityType();
			entityType.setName(Constants.ET_FINACNT_NAME);
			entityType
					.setProperties(Arrays.asList(id, name, ctgyid, comment, owner, crtedby, crtedat, updedby, updedat));
			entityType.setKey(Arrays.asList(propertyRef));
			entityType.setNavigationProperties(navPropList);
		} else if (entityTypeName.equals(Constants.ET_FINACNTEXTDP_FQN)) { // Account
																			// Ext.
																			// Info:
																			// DP
			// create EntityType properties
			CsdlProperty id = new CsdlProperty().setName("ACCOUNTID")
					.setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
			CsdlProperty direct = new CsdlProperty().setName("DIRECT")
					.setType(EdmPrimitiveTypeKind.Boolean.getFullQualifiedName());
			CsdlProperty st = new CsdlProperty().setName("STARTDATE")
					.setType(EdmPrimitiveTypeKind.Date.getFullQualifiedName());
			CsdlProperty ed = new CsdlProperty().setName("ENDDATE")
					.setType(EdmPrimitiveTypeKind.Date.getFullQualifiedName());
			CsdlProperty rpty = new CsdlProperty().setName("RPTTYPE")
					.setType(EdmPrimitiveTypeKind.Int16.getFullQualifiedName());
			CsdlProperty rdi = new CsdlProperty().setName("REFDOCID")
					.setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
			CsdlProperty dffd = new CsdlProperty().setName("DEFRRDAYS")
					.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty cmt = new CsdlProperty().setName("COMMENT")
					.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());

			// create CsdlPropertyRef for Key element
			CsdlPropertyRef propertyRef = new CsdlPropertyRef();
			propertyRef.setName("ACCOUNTID");

			// configure EntityType
			entityType = new CsdlEntityType();
			entityType.setName(Constants.ET_FINACNTEXTDP_NAME);
			entityType.setProperties(Arrays.asList(id, direct, st, ed, rpty, rdi, dffd, cmt));
			entityType.setKey(Arrays.asList(propertyRef));
		}

		return entityType;
	}

	@Override
	public CsdlEntitySet getEntitySet(FullQualifiedName entityContainer, String entitySetName) {

		CsdlEntitySet entitySet = null;

		if (entityContainer.equals(Constants.CONTAINER_FQN)) {
			if (entitySetName.equals(Constants.ES_PRODUCTS_NAME)) { // Sample
																	// product
				entitySet = new CsdlEntitySet();
				entitySet.setName(Constants.ES_PRODUCTS_NAME);
				entitySet.setType(Constants.ET_PRODUCT_FQN);
			} else if (entitySetName.equals(Constants.ES_FINACNTCTGIES_NAME)) { // Account
																				// category
				entitySet = new CsdlEntitySet();
				entitySet.setName(Constants.ES_FINACNTCTGIES_NAME);
				entitySet.setType(Constants.ET_FINACNTCTGY_FQN);

				// Navigation to accounts
				CsdlNavigationPropertyBinding navPropBinding = new CsdlNavigationPropertyBinding();
				navPropBinding.setTarget(Constants.NN_FINACCOUNTS); // the
																	// target
																	// entity
																	// set,
																	// where the
																	// navigation
																	// property
																	// points to
				navPropBinding.setPath(Constants.ES_FINACNTS_NAME); // the path
																	// from
																	// entity
																	// type to
																	// navigation
																	// property
				List<CsdlNavigationPropertyBinding> navPropBindingList = new ArrayList<CsdlNavigationPropertyBinding>();
				navPropBindingList.add(navPropBinding);
				entitySet.setNavigationPropertyBindings(navPropBindingList);
			} else if (entitySetName.equals(Constants.ES_FINACNTEXTDPS_NAME)) { // Account
																				// Ext.
																				// Info:
																				// DP
				entitySet = new CsdlEntitySet();
				entitySet.setName(Constants.ES_FINACNTEXTDPS_NAME);
				entitySet.setType(Constants.ET_FINACNTEXTDP_FQN);
			} else if (entitySetName.equals(Constants.ES_FINACNTS_NAME)) { // Account
				entitySet = new CsdlEntitySet();
				entitySet.setName(Constants.ES_FINACNTS_NAME);
				entitySet.setType(Constants.ET_FINACNT_FQN);

				// Navigation to category
				CsdlNavigationPropertyBinding navPropBinding = new CsdlNavigationPropertyBinding();
				navPropBinding.setTarget(Constants.NN_FINACNTCTGY); // the
																	// target
																	// entity
																	// set,
																	// where the
																	// navigation
																	// property
																	// points to
				navPropBinding.setPath(Constants.ES_FINACNTCTGIES_NAME); // the
																			// path
																			// from
																			// entity
																			// type
																			// to
																			// navigation
																			// property
				List<CsdlNavigationPropertyBinding> navPropBindingList = new ArrayList<CsdlNavigationPropertyBinding>();
				navPropBindingList.add(navPropBinding);

				// Navigate to Ext Info
				navPropBinding = new CsdlNavigationPropertyBinding();
				navPropBinding.setTarget(Constants.NN_FINACNTEXTDP); // the
																		// target
																		// entity
																		// set,
																		// where
																		// the
																		// navigation
																		// property
																		// points
																		// to
				navPropBinding.setPath(Constants.ES_FINACNTEXTDPS_NAME); // the
																			// path
																			// from
																			// entity
																			// type
																			// to
																			// navigation
																			// property
				navPropBindingList.add(navPropBinding);
				entitySet.setNavigationPropertyBindings(navPropBindingList);
			}
		}

		return entitySet;
	}

	@Override
	public CsdlEntityContainer getEntityContainer() {

		// create EntitySets
		List<CsdlEntitySet> entitySets = new ArrayList<CsdlEntitySet>();
		entitySets.add(getEntitySet(Constants.CONTAINER_FQN, Constants.ES_PRODUCTS_NAME));
		entitySets.add(getEntitySet(Constants.CONTAINER_FQN, Constants.ES_FINACNTCTGIES_NAME));
		entitySets.add(getEntitySet(Constants.CONTAINER_FQN, Constants.ES_FINACNTS_NAME));
		entitySets.add(getEntitySet(Constants.CONTAINER_FQN, Constants.ES_FINACNTEXTDPS_NAME));

		// create EntityContainer
		CsdlEntityContainer entityContainer = new CsdlEntityContainer();
		entityContainer.setName(Constants.CONTAINER_NAME);
		entityContainer.setEntitySets(entitySets);

		return entityContainer;
	}

	@Override
	public CsdlEntityContainerInfo getEntityContainerInfo(FullQualifiedName entityContainerName) {

		// This method is invoked when displaying the service document at e.g.
		// http://localhost:8080/DemoService/DemoService.svc
		if (entityContainerName == null || entityContainerName.equals(Constants.CONTAINER_FQN)) {
			CsdlEntityContainerInfo entityContainerInfo = new CsdlEntityContainerInfo();
			entityContainerInfo.setContainerName(Constants.CONTAINER_FQN);

			return entityContainerInfo;
		}

		return null;
	}

}
