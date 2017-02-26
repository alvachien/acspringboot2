package com.alvachien.learning.java.acspringboot2.data;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Iterator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.core.NewCookie;

import com.alvachien.learning.java.acspringboot2.model.*;
import com.alvachien.learning.java.acspringboot2.service.*;
import com.alvachien.learning.java.acspringboot2.utils.*;

import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.EntityCollection;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.data.ValueType;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.ex.ODataRuntimeException;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.uri.UriParameter;

public class Storage {

	private List<Entity> productList = null;
	// private List<Entity> finAccountCategoryList = null;
	// private List<Entity> finAccountList = null;
	// private List<Entity> finAccountExtDPList = null;

	private static final EntityManagerFactory entityManagerFactory;
	static {
		try {
			entityManagerFactory = Persistence.createEntityManagerFactory("acolingo3");
		} catch (Throwable ex) {
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static EntityManager getEntityManager() {
		return entityManagerFactory.createEntityManager();
	}

	public Storage() {
		this.productList = new ArrayList<Entity>();
		// this.finAccountCategoryList = new ArrayList<Entity>();
		// this.finAccountList = new ArrayList<Entity>();
		// this.finAccountExtDPList = new ArrayList<Entity>();

		// Sample data for products
		initProductSampleData();
	}

	/* PUBLIC FACADE */
	public EntityCollection readEntitySetData(EdmEntitySet edmEntitySet)throws ODataApplicationException{

		EntityCollection entityColl = null;

		// actually, this is only required if we have more than one Entity Sets
		if(edmEntitySet.getName().equals(com.alvachien.learning.java.acspringboot2.model.Constants.ES_PRODUCTS_NAME)){
			entityColl = getProducts();
		} else if (edmEntitySet.getName().equals(com.alvachien.learning.java.acspringboot2.model.Constants.ES_FINACNTCTGIES_NAME)) {
			entityColl = getFinAccountCategories();
		} else if (edmEntitySet.getName().equals(com.alvachien.learning.java.acspringboot2.model.Constants.ES_FINACNTS_NAME)) {
			entityColl = getFinAccounts();
		} else if (edmEntitySet.getName().equals(com.alvachien.learning.java.acspringboot2.model.Constants.ES_FINACNTEXTDPS_NAME)) {
			entityColl = getFinAccountExtDPs();
		}

		return entityColl;
	}

	public Entity readEntityData(EdmEntitySet edmEntitySet, List<UriParameter> keyParams) throws ODataApplicationException{
		Entity entity = null;
		EdmEntityType edmEntityType = edmEntitySet.getEntityType();

		// actually, this is only required if we have more than one Entity Type
		if(edmEntityType.getName().equals(com.alvachien.learning.java.acspringboot2.model.Constants.ET_PRODUCT_NAME)){
			entity = getProduct(edmEntityType, keyParams);
		} else if(edmEntityType.getName().equals(com.alvachien.learning.java.acspringboot2.model.Constants.ET_FINACNTCTGY_NAME)) {
			entity = getFinAccountCategory(edmEntityType, keyParams);
		} else if(edmEntityType.getName().equals(com.alvachien.learning.java.acspringboot2.model.Constants.ET_FINACNT_NAME)) {
			entity = getFinAccount(edmEntityType, keyParams);
		} else if(edmEntityType.getName().equals(com.alvachien.learning.java.acspringboot2.model.Constants.ET_FINACNTEXTDP_NAME)) {
			entity = getFinAccountExtDP(edmEntityType, keyParams);
		}

		return entity;
	}

	// Navigation
	// public Entity getRelatedEntity(Entity entity, EdmEntityType relatedEntityType) {
	// 	EntityCollection collection = getRelatedEntityCollection(entity, relatedEntityType);
	// 	if (collection.getEntities().isEmpty()) {
	// 		return null;
	// 	}

	// 	return collection.getEntities().get(0);
	// }

	// public Entity getRelatedEntity(Entity entity, EdmEntityType relatedEntityType, List<UriParameter> keyPredicates) {

	// 	EntityCollection relatedEntities = getRelatedEntityCollection(entity, relatedEntityType);
	// 	return Util.findEntity(relatedEntityType, relatedEntities, keyPredicates);
	// }

	// public EntityCollection getRelatedEntityCollection(Entity sourceEntity, EdmEntityType targetEntityType) {
	// 	EntityCollection navigationTargetEntityCollection = new EntityCollection();

	// 	FullQualifiedName relatedEntityFqn = targetEntityType.getFullQualifiedName();
	// 	String sourceEntityFqn = sourceEntity.getType();

	// 	if (sourceEntityFqn.equals(DemoEdmProvider.ET_PRODUCT_FQN.getFullQualifiedNameAsString())
	// 		&& relatedEntityFqn.equals(DemoEdmProvider.ET_CATEGORY_FQN)) {
	// 	// relation Products->Category (result all categories)
	// 	int productID = (Integer) sourceEntity.getProperty("ID").getValue();
	// 	if (productID == 1 || productID == 2) {
	// 		navigationTargetEntityCollection.getEntities().add(categoryList.get(0));
	// 	} else if (productID == 3 || productID == 4) {
	// 		navigationTargetEntityCollection.getEntities().add(categoryList.get(1));
	// 	} else if (productID == 5 || productID == 6) {
	// 		navigationTargetEntityCollection.getEntities().add(categoryList.get(2));
	// 	}
	// 	} else if (sourceEntityFqn.equals(DemoEdmProvider.ET_CATEGORY_FQN.getFullQualifiedNameAsString())
	// 		&& relatedEntityFqn.equals(DemoEdmProvider.ET_PRODUCT_FQN)) {
	// 	// relation Category->Products (result all products)
	// 	int categoryID = (Integer) sourceEntity.getProperty("ID").getValue();
	// 	if (categoryID == 1) {
	// 		// the first 2 products are notebooks
	// 		navigationTargetEntityCollection.getEntities().addAll(productList.subList(0, 2));
	// 	} else if (categoryID == 2) {
	// 		// the next 2 products are organizers
	// 		navigationTargetEntityCollection.getEntities().addAll(productList.subList(2, 4));
	// 	} else if (categoryID == 3) {
	// 		// the first 2 products are monitors
	// 		navigationTargetEntityCollection.getEntities().addAll(productList.subList(4, 6));
	// 	}
	// 	}

	// 	if (navigationTargetEntityCollection.getEntities().isEmpty()) {
	// 	return null;
	// 	}

	// 	return navigationTargetEntityCollection;
	// }

	/*  INTERNAL */
	private EntityCollection getProducts() {
		EntityCollection retEntitySet = new EntityCollection();

		for(Entity productEntity : this.productList){
		    retEntitySet.getEntities().add(productEntity);
		}

		return retEntitySet;
	}

	private Entity getProduct(EdmEntityType edmEntityType, List<UriParameter> keyParams) throws ODataApplicationException{

		// the list of entities at runtime
		EntityCollection entitySet = getProducts();
		
		/*  generic approach  to find the requested entity */
		Entity requestedEntity = Util.findEntity(edmEntityType, entitySet, keyParams);
		
		if(requestedEntity == null){
			// this variable is null if our data doesn't contain an entity for the requested key
			// Throw suitable exception
			throw new ODataApplicationException("Entity for requested key doesn't exist",
          HttpStatusCode.NOT_FOUND.getStatusCode(), Locale.ENGLISH);
		}

		return requestedEntity;
	}

	private EntityCollection getFinAccountCategories() {
		EntityCollection retEntitySet = new EntityCollection();

		EntityManager entityManager = Storage.getEntityManager();

		//entityManager.getTransaction().begin();
        @SuppressWarnings("unchecked")

        List<FinAccountCategory> ctgies = entityManager.createQuery("from FinAccountCategory fac").getResultList();
        for (Iterator<FinAccountCategory> iterator = ctgies.iterator(); iterator.hasNext();) {
          FinAccountCategory actgy = (FinAccountCategory) iterator.next();
          Entity e9 = new Entity()
            .addProperty(new Property(null, "ID", ValueType.PRIMITIVE, actgy.getID()))
            .addProperty(new Property(null, "NAME", ValueType.PRIMITIVE, actgy.getName()))
            .addProperty(new Property(null, "ASSETRFLAG", ValueType.PRIMITIVE, actgy.getAssetFlag()))
            .addProperty(new Property(null, "COMMENT", ValueType.PRIMITIVE, actgy.getComment()))
            .addProperty(new Property(null, "SYSFLAG", ValueType.PRIMITIVE, actgy.getSysFlag()))
			.addProperty(new Property(null, "CREATEDBY", ValueType.PRIMITIVE, actgy.getCreatedBy()))
			.addProperty(new Property(null, "CREATEDAT", ValueType.PRIMITIVE, actgy.getCreatedAt()))
			.addProperty(new Property(null, "UPDATEDBY", ValueType.PRIMITIVE, actgy.getUpdatedBy()))
			.addProperty(new Property(null, "UPDATEDAT", ValueType.PRIMITIVE, actgy.getUpdatedAt()))
            ;

          e9.setId(createId(Constants.ET_FINACNTCTGY_NAME, actgy.getID()));
		  retEntitySet.getEntities().add(e9);
        }
        //entityManager.getTransaction().commit();

		// for(Entity acntEntity : this.finAccountCategoryList){
		//     retEntitySet.getEntities().add(acntEntity);
		// }

		return retEntitySet;
	}

	private Entity getFinAccountCategory(EdmEntityType edmEntityType, List<UriParameter> keyParams) throws ODataApplicationException{

		// the list of entities at runtime
		EntityCollection entitySet = getFinAccountCategories();
		
		/*  generic approach  to find the requested entity */
		Entity requestedEntity = Util.findEntity(edmEntityType, entitySet, keyParams);
		
		if(requestedEntity == null){
			// this variable is null if our data doesn't contain an entity for the requested key
			// Throw suitable exception
			throw new ODataApplicationException("Entity for requested key doesn't exist",
          HttpStatusCode.NOT_FOUND.getStatusCode(), Locale.ENGLISH);
		}

		return requestedEntity;
	}

	private EntityCollection getFinAccounts() {
		EntityCollection retEntitySet = new EntityCollection();

		EntityManager entityManager = Storage.getEntityManager();

		//entityManager.getTransaction().begin();
        List<FinAccount> accounts = entityManager.createQuery("from FinAccount fa").getResultList();
        for (Iterator<FinAccount> iterator = accounts.iterator(); iterator.hasNext();) {
          FinAccount account = (FinAccount) iterator.next();
          Entity e9 = new Entity()
            .addProperty(new Property(null, "ID", ValueType.PRIMITIVE, account.getID()))
			.addProperty(new Property(null, "CTGYID", ValueType.PRIMITIVE, account.getCtgyID()))
            .addProperty(new Property(null, "NAME", ValueType.PRIMITIVE, account.getName()))
            .addProperty(new Property(null, "COMMENT", ValueType.PRIMITIVE, account.getComment()))
			.addProperty(new Property(null, "OWNER", ValueType.PRIMITIVE, account.getOwner()))
			.addProperty(new Property(null, "CREATEDBY", ValueType.PRIMITIVE, account.getCreatedBy()))
			.addProperty(new Property(null, "CREATEDAT", ValueType.PRIMITIVE, account.getCreatedAt()))
			.addProperty(new Property(null, "UPDATEDBY", ValueType.PRIMITIVE, account.getUpdatedBy()))
			.addProperty(new Property(null, "UPDATEDAT", ValueType.PRIMITIVE, account.getUpdatedAt()))
            ;

          e9.setId(createId(Constants.ES_FINACNTS_NAME, account.getID()));
		  retEntitySet.getEntities().add(e9);
        }
        //entityManager.getTransaction().commit();

		// for(Entity acntEntity : this.finAccountCategoryList){
		//     retEntitySet.getEntities().add(acntEntity);
		// }

		return retEntitySet;
	}

	private Entity getFinAccount(EdmEntityType edmEntityType, List<UriParameter> keyParams) throws ODataApplicationException{

		// the list of entities at runtime
		EntityCollection entitySet = getFinAccounts();
		
		/*  generic approach  to find the requested entity */
		Entity requestedEntity = Util.findEntity(edmEntityType, entitySet, keyParams);
		
		if(requestedEntity == null){
			// this variable is null if our data doesn't contain an entity for the requested key
			// Throw suitable exception
			throw new ODataApplicationException("Entity for requested key doesn't exist",
          HttpStatusCode.NOT_FOUND.getStatusCode(), Locale.ENGLISH);
		}

		return requestedEntity;
	}

	private EntityCollection getFinAccountExtDPs() {
		EntityCollection retEntitySet = new EntityCollection();

		EntityManager entityManager = Storage.getEntityManager();

		//entityManager.getTransaction().begin();
        List<FinAccountExtDP> accountedps = entityManager.createQuery("from FinAccountExtDP faedp").getResultList();
        for (Iterator<FinAccountExtDP> iterator = accountedps.iterator(); iterator.hasNext();) {
          FinAccountExtDP account = (FinAccountExtDP) iterator.next();
          Entity e9 = new Entity()
            .addProperty(new Property(null, "ACCOUNTID", ValueType.PRIMITIVE, account.getAccountID()))
            .addProperty(new Property(null, "DIRECT", ValueType.PRIMITIVE, account.getDirect()))
            .addProperty(new Property(null, "STARTDATE", ValueType.PRIMITIVE, account.getStartDate()))
			.addProperty(new Property(null, "ENDDATE", ValueType.PRIMITIVE, account.getEndDate()))
			.addProperty(new Property(null, "RPTTYPE", ValueType.PRIMITIVE, account.getRptType()))
			.addProperty(new Property(null, "REFDOCID", ValueType.PRIMITIVE, account.getRefDocID()))
			.addProperty(new Property(null, "DEFRRDAYS", ValueType.PRIMITIVE, account.getDefrrDays()))
			.addProperty(new Property(null, "COMMENT", ValueType.PRIMITIVE, account.getComment()))
            ;

          e9.setId(createId(Constants.ES_FINACNTEXTDPS_NAME, account.getAccountID()));
		  retEntitySet.getEntities().add(e9);
        }
        //entityManager.getTransaction().commit();

		// for(Entity acntEntity : this.finAccountCategoryList){
		//     retEntitySet.getEntities().add(acntEntity);
		// }

		return retEntitySet;
	}

	private Entity getFinAccountExtDP(EdmEntityType edmEntityType, List<UriParameter> keyParams) throws ODataApplicationException{

		// the list of entities at runtime
		EntityCollection entitySet = getFinAccounts();
		
		/*  generic approach  to find the requested entity */
		Entity requestedEntity = Util.findEntity(edmEntityType, entitySet, keyParams);
		
		if(requestedEntity == null){
			// this variable is null if our data doesn't contain an entity for the requested key
			// Throw suitable exception
			throw new ODataApplicationException("Entity for requested key doesn't exist",
          HttpStatusCode.NOT_FOUND.getStatusCode(), Locale.ENGLISH);
		}

		return requestedEntity;
	}

	/* HELPER */

	private void initProductSampleData(){

		// add some sample product entities
		final Entity e1 = new Entity()
				.addProperty(new Property(null, "ID", ValueType.PRIMITIVE, 1))
				.addProperty(new Property(null, "Name", ValueType.PRIMITIVE, "Notebook Basic 15"))
				.addProperty(new Property(null, "Description", ValueType.PRIMITIVE,
						"Notebook Basic, 1.7GHz - 15 XGA - 1024MB DDR2 SDRAM - 40GB"));
		e1.setId(createId(Constants.ES_PRODUCTS_NAME, 1));
		productList.add(e1);

		final Entity e2 = new Entity()
				.addProperty(new Property(null, "ID", ValueType.PRIMITIVE, 2))
				.addProperty(new Property(null, "Name", ValueType.PRIMITIVE, "1UMTS PDA"))
				.addProperty(new Property(null, "Description", ValueType.PRIMITIVE,
						"Ultrafast 3G UMTS/HSDPA Pocket PC, supports GSM network"));
		e2.setId(createId(Constants.ES_PRODUCTS_NAME, 2));
		productList.add(e2);

		final Entity e3 = new Entity()
				.addProperty(new Property(null, "ID", ValueType.PRIMITIVE, 3))
				.addProperty(new Property(null, "Name", ValueType.PRIMITIVE, "Ergo Screen"))
				.addProperty(new Property(null, "Description", ValueType.PRIMITIVE,
						"19 Optimum Resolution 1024 x 768 @ 85Hz, resolution 1280 x 960"));
		e3.setId(createId(Constants.ES_PRODUCTS_NAME, 3));
		productList.add(e3);
	}

	private URI createId(String entitySetName, Object id) {
		try {
			return new URI(entitySetName + "(" + String.valueOf(id) + ")");
		} catch (URISyntaxException e) {
			throw new ODataRuntimeException("Unable to create id for entity: " + entitySetName, e);
		}		
	}

	// private URI createId(Entity entity, String idPropertyName, String navigationName) {
	// 	try {
	// 	StringBuilder sb = new StringBuilder(getEntitySetName(entity)).append("(");
	// 	final Property property = entity.getProperty(idPropertyName);
	// 	sb.append(property.asPrimitive()).append(")");
	// 	if(navigationName != null) {
	// 		sb.append("/").append(navigationName);
	// 	}
	// 	return new URI(sb.toString());
	// 	} catch (URISyntaxException e) {
	// 	throw new ODataRuntimeException("Unable to create (Atom) id for entity: " + entity, e);
	// 	}
	// }

	// private String getEntitySetName(Entity entity) {
	// 	if(DemoEdmProvider.ET_CATEGORY_FQN.getFullQualifiedNameAsString().equals(entity.getType())) {
	// 	return DemoEdmProvider.ES_CATEGORIES_NAME;
	// 	} else if(DemoEdmProvider.ET_PRODUCT_FQN.getFullQualifiedNameAsString().equals(entity.getType())) {
	// 	return DemoEdmProvider.ES_PRODUCTS_NAME;
	// 	}
	// 	return entity.getType();
	// }
}
