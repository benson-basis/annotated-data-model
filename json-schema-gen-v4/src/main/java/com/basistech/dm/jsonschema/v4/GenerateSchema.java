/*
* Copyright 2016 Basis Technology Corp.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.basistech.dm.jsonschema.v4;

import com.basistech.rosette.dm.AnnotatedText;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kjetland.jackson.jsonSchema.JsonSchemaGenerator;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Generate a V4 Json Schema from the ADM.
 */
public final class GenerateSchema {
    private GenerateSchema() {
        //
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.format("Usage: GenerateSchema OUTPUTFILE%n");
            System.exit(1);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        JsonSchemaGenerator jsonSchemaGenerator = new JsonSchemaGenerator(objectMapper);

        // If using JsonSchema to generate HTML5 GUI:
        // JsonSchemaGenerator html5 = new JsonSchemaGenerator(objectMapper, JsonSchemaConfig.html5EnabledSchema() );

        JsonNode jsonSchema = jsonSchemaGenerator.generateJsonSchema(AnnotatedText.class,
                scala.Option.apply("Annotated Data Model"),
                scala.Option.apply("Annotated Data Model"));

        try (OutputStream output = Files.newOutputStream(Paths.get(args[0]))) {
            objectMapper.writeValue(output, jsonSchema);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
