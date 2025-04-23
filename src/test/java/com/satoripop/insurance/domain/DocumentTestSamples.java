package com.satoripop.insurance.domain;

import java.util.UUID;

public class DocumentTestSamples {

    public static Document getDocumentSample1() {
        return new Document().id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa")).name("name1").docPath("docPath1");
    }

    public static Document getDocumentSample2() {
        return new Document().id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367")).name("name2").docPath("docPath2");
    }

    public static Document getDocumentRandomSampleGenerator() {
        return new Document().id(UUID.randomUUID()).name(UUID.randomUUID().toString()).docPath(UUID.randomUUID().toString());
    }
}
