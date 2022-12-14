package com.example.gitlabproxy.client;

import lombok.RequiredArgsConstructor;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Group;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GitlabClient {

    private final GitLabApi gitLabApi;

    public List<Group> getGroups() {
        try {
            return gitLabApi.getGroupApi().getGroups();
        } catch (GitLabApiException e) {
            throw new RuntimeException(e);
        }
    }

}
