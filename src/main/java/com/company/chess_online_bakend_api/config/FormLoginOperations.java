/*
 * 7/26/19 7:15 PM. Created by Bartosz Kruba.
 */

/*
 * 7/26/19 7:12 PM. Created by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.config;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.collect.Multimap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.ApiListingBuilder;
import springfox.documentation.builders.OperationBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiDescription;
import springfox.documentation.service.ApiListing;
import springfox.documentation.service.Operation;
import springfox.documentation.spring.web.plugins.DocumentationPluginsManager;
import springfox.documentation.spring.web.readers.operation.CachingOperationNameGenerator;
import springfox.documentation.spring.web.scanners.ApiDescriptionReader;
import springfox.documentation.spring.web.scanners.ApiListingScanner;
import springfox.documentation.spring.web.scanners.ApiListingScanningContext;
import springfox.documentation.spring.web.scanners.ApiModelReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


// Used for Swagger documentation for spring security login and logout paths
@Component
public class FormLoginOperations extends ApiListingScanner {

    @Autowired
    private TypeResolver typeResolver;

    @Autowired
    public FormLoginOperations(ApiDescriptionReader apiDescriptionReader, ApiModelReader apiModelReader,
                               DocumentationPluginsManager pluginsManager) {


        super(apiDescriptionReader, apiModelReader, pluginsManager);
    }

    @Override
    public Multimap<String, ApiListing> scan(ApiListingScanningContext context) {
        final Multimap<String, ApiListing> def = super.scan(context);

        final List<ApiDescription> apis = new LinkedList<>();

        List<Operation> operations = new ArrayList<>();
        operations.add(new OperationBuilder(new CachingOperationNameGenerator())
                .method(HttpMethod.POST)
                .uniqueId("login")
                .parameters(Arrays.asList(new ParameterBuilder()
                                .name("username")
                                .description("The username")
                                .parameterType("query")
                                .type(typeResolver.resolve(String.class))
                                .modelRef(new ModelRef("string"))
                                .build(),
                        new ParameterBuilder()
                                .name("password")
                                .description("The password")
                                .parameterType("query")
                                .type(typeResolver.resolve(String.class))
                                .modelRef(new ModelRef("string"))
                                .build()))
                .summary("Log in") //
                .notes("Here you can log in")
                .build());

        apis.add(new ApiDescription("/auth/login",
                "Authentication documentation", operations, false));

        operations = new ArrayList<>();

        operations.add(new OperationBuilder(new CachingOperationNameGenerator())
                .method(HttpMethod.GET)
                .uniqueId("logout")
                .summary("Log out") //
                .notes("Here you can log out")
                .build());

        apis.add(new ApiDescription("/auth/logout",
                "Authentication documentation", operations, false));

        def.put("authentication", new ApiListingBuilder(context.getDocumentationContext().getApiDescriptionOrdering())
                .apis(apis)
                .description("Custom authentication")
                .build());

        return def;
    }
}
