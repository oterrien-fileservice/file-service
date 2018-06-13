package com.ote.file.service.mock;

import com.ote.file.service.JsonUtils;
import com.ote.file.api.model.File;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@Service
@Profile("test")
@Slf4j
public class FileRestControllerMock {

    private static final String URI = "/v1/storage";

    @Autowired
    private WebConfigurationMock webConfiguration;


    public Set<String> getFolders(String user, String application, String perimeter) throws Exception {

        String uri = UriComponentsBuilder.fromPath(URI).
                path("/folders").
                queryParam("user", user).
                queryParam("application", application).
                queryParam("perimeter", perimeter).
                build(true).
                toUriString();

        MvcResult result = webConfiguration.getMockMvc().perform(get(uri)).andReturn();

        if (result.getResponse().getStatus() == HttpStatus.OK.value()) {
            return JsonUtils.parseFromJsonSet(result.getResponse().getContentAsString(), String.class);
        } else {
            throw result.getResolvedException();
        }
    }

    public Set<String> getFiles(String user, String application, String perimeter, String folder) throws Exception {

        String uri = UriComponentsBuilder.fromPath(URI).
                path("/files").
                queryParam("user", user).
                queryParam("application", application).
                queryParam("perimeter", perimeter).
                queryParam("folder", folder).
                build(true).
                toUriString();

        MvcResult result = webConfiguration.getMockMvc().perform(get(uri)).andReturn();

        if (result.getResponse().getStatus() == HttpStatus.OK.value()) {
            return JsonUtils.parseFromJsonSet(result.getResponse().getContentAsString(), String.class);
        } else {
            throw result.getResolvedException();
        }
    }

    public Set<String> getFiles(String user, String application, String perimeter) throws Exception {

        String uri = UriComponentsBuilder.fromPath(URI).
                path("/files").
                queryParam("user", user).
                queryParam("application", application).
                queryParam("perimeter", perimeter).
                build(true).
                toUriString();

        MvcResult result = webConfiguration.getMockMvc().perform(get(uri)).andReturn();

        if (result.getResponse().getStatus() == HttpStatus.OK.value()) {
            return JsonUtils.parseFromJsonSet(result.getResponse().getContentAsString(), String.class);
        } else {
            throw result.getResolvedException();
        }
    }

    public byte[] readFile(String user, String application, String perimeter, File file) throws Exception {

        String uri = UriComponentsBuilder.fromPath(URI).
                path("/files/content").
                queryParam("user", user).
                queryParam("application", application).
                queryParam("perimeter", perimeter).
                queryParam("folder", file.getFolder().getPath()).
                queryParam("file", file.getName()).
                build(true).
                toUriString();

        MvcResult result = webConfiguration.getMockMvc().perform(get(uri)).andReturn();

        if (result.getResponse().getStatus() == HttpStatus.OK.value()) {
            return result.getResponse().getContentAsByteArray();
        } else {
            throw result.getResolvedException();
        }
    }

    public void createOrReplaceFile(String user, String application, String perimeter, File file, byte[] content, boolean replaceIfFound) throws Exception {

        MockMultipartFile multipartFile = new MockMultipartFile("file", file.getName(), MediaType.TEXT_PLAIN_VALUE, content);

        String uri = UriComponentsBuilder.fromPath(URI).
                path("/files/content").
                queryParam("user", user).
                queryParam("application", application).
                queryParam("perimeter", perimeter).
                queryParam("folder", file.getFolder().getPath()).
                queryParam("replaceIfFound", replaceIfFound).
                build(true).
                toUriString();

        MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart(uri);
        builder.with(request -> {
            request.setMethod("PUT");
            return request;
        });

        MvcResult result = webConfiguration.getMockMvc().perform(builder.file(multipartFile)).andReturn();

        if (result.getResponse().getStatus() != HttpStatus.OK.value()) {
            throw result.getResolvedException();
        }
    }

    public void appendFile(String user, String application, String perimeter, File file, byte[] content, boolean createIfNotFound) throws Exception {

        MockMultipartFile multipartFile = new MockMultipartFile("file", file.getName(),
                MediaType.TEXT_PLAIN_VALUE, content);

        String uri = UriComponentsBuilder.fromPath(URI).
                path("/files/content").
                queryParam("user", user).
                queryParam("application", application).
                queryParam("perimeter", perimeter).
                queryParam("folder", file.getFolder().getPath()).
                queryParam("createIfNotFound", createIfNotFound).
                build(true).
                toUriString();

        MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart(uri);
        builder.with(request -> {
            request.setMethod("PATCH");
            return request;
        });

        MvcResult result = webConfiguration.getMockMvc().perform(builder.file(multipartFile)).andReturn();

        if (result.getResponse().getStatus() != HttpStatus.OK.value()) {
            throw result.getResolvedException();
        }
    }
}
