package com.alvachien.learning.java.acspringboot2.service;

import java.util.List;
import java.util.Locale;

import org.apache.olingo.commons.api.data.ContextURL;
import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.EntityCollection;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.edm.EdmNavigationProperty;
import org.apache.olingo.commons.api.format.ContentType;
import org.apache.olingo.commons.api.http.HttpHeader;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.OData;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.ODataRequest;
import org.apache.olingo.server.api.ODataResponse;
import org.apache.olingo.server.api.ServiceMetadata;
import org.apache.olingo.server.api.processor.EntityCollectionProcessor;
import org.apache.olingo.server.api.serializer.EntityCollectionSerializerOptions;
import org.apache.olingo.server.api.serializer.ODataSerializer;
import org.apache.olingo.server.api.serializer.SerializerException;
import org.apache.olingo.server.api.serializer.SerializerResult;
import org.apache.olingo.server.api.uri.UriInfo;
import org.apache.olingo.server.api.uri.UriParameter;
import org.apache.olingo.server.api.uri.UriResource;
import org.apache.olingo.server.api.uri.UriResourceEntitySet;
import org.apache.olingo.server.api.uri.UriResourceNavigation;

import com.alvachien.learning.java.acspringboot2.model.Constants;
import com.alvachien.learning.java.acspringboot2.utils.*;
import com.alvachien.learning.java.acspringboot2.data.*;


public class GenericEntityCollectionProcessor implements EntityCollectionProcessor {

	  private OData odata;
	  private ServiceMetadata srvMetadata;
	  private Storage storage;

	  public GenericEntityCollectionProcessor(Storage storage) {
	    this.storage = storage;
	  }

	  // our processor is initialized with the OData context object
	  public void init(OData odata, ServiceMetadata serviceMetadata) {
	    this.odata = odata;
	    this.srvMetadata = serviceMetadata;
	  }

	  // the only method that is declared in the EntityCollectionProcessor interface
	  // this method is called, when the user fires a request to an EntitySet
	  // in our example, the URL would be:
	  // http://localhost:8080/ExampleService1/ExampleServlet1.svc/Products
	  public void readEntityCollection(ODataRequest request, ODataResponse response,
	      UriInfo uriInfo, ContentType responseFormat)
	      throws ODataApplicationException, SerializerException {

	    EdmEntitySet responseEdmEntitySet = null; // we'll need this to build the ContextURL
	    EntityCollection responseEntityCollection = null; // we'll need this to set the response body

	    // 1st retrieve the requested EntitySet from the uriInfo (representation of the parsed URI)
	    List<UriResource> resourceParts = uriInfo.getUriResourceParts();
	    int segmentCount = resourceParts.size();

	    UriResource uriResource = resourceParts.get(0); // in our example, the first segment is the EntitySet
	    if (!(uriResource instanceof UriResourceEntitySet)) {
	      throw new ODataApplicationException("Only EntitySet is supported",
	          HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(), Locale.ROOT);
	    }

	    UriResourceEntitySet uriResourceEntitySet = (UriResourceEntitySet) uriResource;
	    EdmEntitySet startEdmEntitySet = uriResourceEntitySet.getEntitySet();

	    if (segmentCount == 1) { // this is the case for: DemoService/DemoService.svc/Categories
	      responseEdmEntitySet = startEdmEntitySet; // the response body is built from the first (and only) entitySet

	      // 2nd: fetch the data from backend for this requested EntitySetName and deliver as EntitySet
	      responseEntityCollection = storage.readEntitySetData(startEdmEntitySet);
	    } else if (segmentCount == 2) { // in case of navigation: DemoService.svc/Categories(3)/Products

	      UriResource lastSegment = resourceParts.get(1); // in our example we don't support more complex URIs
	      if (lastSegment instanceof UriResourceNavigation) {
	        // TODO!!!        
	        // UriResourceNavigation uriResourceNavigation = (UriResourceNavigation) lastSegment;
	        // EdmNavigationProperty edmNavigationProperty = uriResourceNavigation.getProperty();
	        // EdmEntityType targetEntityType = edmNavigationProperty.getType();
	        // // from Categories(1) to Products
	        // responseEdmEntitySet = Util.getNavigationTargetEntitySet(startEdmEntitySet, edmNavigationProperty);

	        // // 2nd: fetch the data from backend
	        // // first fetch the entity where the first segment of the URI points to
	        // List<UriParameter> keyPredicates = uriResourceEntitySet.getKeyPredicates();
	        // // e.g. for Categories(3)/Products we have to find the single entity: Category with ID 3
	        // Entity sourceEntity = storage.readEntityData(startEdmEntitySet, keyPredicates);
	        // // error handling for e.g. DemoService.svc/Categories(99)/Products
	        // if (sourceEntity == null) {
	        //   throw new ODataApplicationException("Entity not found.",
	        //       HttpStatusCode.NOT_FOUND.getStatusCode(), Locale.ROOT);
	        // }
	        // // then fetch the entity collection where the entity navigates to
	        // // note: we don't need to check uriResourceNavigation.isCollection(),
	        // // because we are the EntityCollectionProcessor
	        // responseEntityCollection = storage.getRelatedEntityCollection(sourceEntity, targetEntityType);
	      }
	    } else { // this would be the case for e.g. Products(1)/Category/Products
	      throw new ODataApplicationException("Not supported",
	          HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(), Locale.ROOT);
	    }

	    // 3rd: create and configure a serializer
	    ContextURL contextUrl = ContextURL.with().entitySet(responseEdmEntitySet).build();
	    final String id = request.getRawBaseUri() + "/" + responseEdmEntitySet.getName();
	    EntityCollectionSerializerOptions opts = EntityCollectionSerializerOptions.with()
	        .contextURL(contextUrl).id(id).build();
	    EdmEntityType edmEntityType = responseEdmEntitySet.getEntityType();

	    ODataSerializer serializer = odata.createSerializer(responseFormat);
	    SerializerResult serializerResult = serializer.entityCollection(this.srvMetadata, edmEntityType,
	        responseEntityCollection, opts);

	    // 4th: configure the response object: set the body, headers and status code
	    response.setContent(serializerResult.getContent());
	    response.setStatusCode(HttpStatusCode.OK.getStatusCode());
	    response.setHeader(HttpHeader.CONTENT_TYPE, responseFormat.toContentTypeString());
	  }

	  /**
	   * Helper method for providing some sample data
	   * @param edmEntitySet for which the data is requested
	   * @return data of requested entity set
	   */
	  // private EntityCollection getData(EdmEntitySet edmEntitySet){

	  //   EntityCollection rstCollection = new EntityCollection();
	  //   //check for which EdmEntitySet the data is requested
	  //   if(DemoEdmProvider.ES_PRODUCTS_NAME.equals(edmEntitySet.getName())) {
	  //     List<Entity> productList = rstCollection.getEntities();

	  //     // add some sample product entities
	  //     final Entity e1 = new Entity()
	  //         .addProperty(new Property(null, "ID", ValueType.PRIMITIVE, 1))
	  //         .addProperty(new Property(null, "Name", ValueType.PRIMITIVE, "Notebook Basic 15"))
	  //         .addProperty(new Property(null, "Description", ValueType.PRIMITIVE,
	  //             "Notebook Basic, 1.7GHz - 15 XGA - 1024MB DDR2 SDRAM - 40GB"));
	  //     e1.setId(createId("Products", 1));
	  //     productList.add(e1);

	  //     final Entity e2 = new Entity()
	  //         .addProperty(new Property(null, "ID", ValueType.PRIMITIVE, 2))
	  //         .addProperty(new Property(null, "Name", ValueType.PRIMITIVE, "1UMTS PDA"))
	  //         .addProperty(new Property(null, "Description", ValueType.PRIMITIVE,
	  //             "Ultrafast 3G UMTS/HSDPA Pocket PC, supports GSM network"));
	  //     e2.setId(createId("Products", 1));
	  //     productList.add(e2);

	  //     final Entity e3 = new Entity()
	  //         .addProperty(new Property(null, "ID", ValueType.PRIMITIVE, 3))
	  //         .addProperty(new Property(null, "Name", ValueType.PRIMITIVE, "Ergo Screen"))
	  //         .addProperty(new Property(null, "Description", ValueType.PRIMITIVE,
	  //             "19 Optimum Resolution 1024 x 768 @ 85Hz, resolution 1280 x 960"));
	  //     e3.setId(createId("Products", 1));
	  //     productList.add(e3);
	  //   } 
	  //   else if (DemoEdmProvider.ES_FINACNTCTGIES_NAME.equals(edmEntitySet.getName())) {
	  //     List<Entity> rstList = rstCollection.getEntities();
	  //     try {
	  //       entityManager.getTransaction().begin();
	  //       @SuppressWarnings("unchecked")
	  //       List<FinAccountCategory> ctgies = entityManager.createQuery("select * from t_fin_account_ctgy").getResultList();
	  //       for (Iterator<FinAccountCategory> iterator = ctgies.iterator(); iterator.hasNext();) {
	  //         FinAccountCategory actgy = (FinAccountCategory) iterator.next();
	  //         Entity e9 = new Entity()
	  //           .addProperty(new Property(null, "ID", ValueType.PRIMITIVE, actgy.getID()))
	  //           .addProperty(new Property(null, "NAME", ValueType.PRIMITIVE, actgy.getName()))
	  //           .addProperty(new Property(null, "ASSETRFLAG", ValueType.PRIMITIVE, actgy.getAssetFlag()))
	  //           .addProperty(new Property(null, "COMMENT", ValueType.PRIMITIVE, actgy.getComment()))
	  //           .addProperty(new Property(null, "SYSFLAG", ValueType.PRIMITIVE, actgy.getSysFlag()))
	  //           ;

	  //         e9.setId(createId("AcntCategory", actgy.getID()));
	  //         rstList.add(e9);
	  //         //System.out.println(FinAccountCategory());
	  //       }
	  //       entityManager.getTransaction().commit();
	  //     } catch (Exception e) {
	  //       entityManager.getTransaction().rollback();
	  //     }
	  //   }

	  //   return rstCollection;
	  // }
	  
	  // private URI createId(String entitySetName, Object id) {
	  //   try {
	  //     return new URI(entitySetName + "(" + String.valueOf(id) + ")");
	  //   } catch (URISyntaxException e) {
	  //     throw new ODataRuntimeException("Unable to create id for entity: " + entitySetName, e);
	  //   }
	  // }
	}
