package com.example.gitlabproxy.client;

import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.GroupApi;
import org.gitlab4j.api.models.Group;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest
class GitlabClientTest extends AbstractTest {

    @SpyBean
    private GitLabApi gitLabApi;

    private final GroupApi mockGroupApi = mock(GroupApi.class);

    @Autowired
    private GitlabClient gitlabClient;

    @BeforeEach
    void setUp() {
        when(gitLabApi.getGroupApi()).thenReturn(mockGroupApi);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(gitLabApi, mockGroupApi);
    }

    @Test
    @DisplayName("Успех")
    void success() throws GitLabApiException {
        List<Group> groups = List.of(
            new Group().withFullName("test/group1"),
            new Group().withFullName("test/group2")
        );
        when(mockGroupApi.getGroups()).thenReturn(groups);
        softly.assertThat(gitlabClient.getGroups()).isEqualTo(groups);
        verify(gitLabApi).getGroupApi();
        verify(mockGroupApi).getGroups();
    }

    @Test
    @DisplayName("Ошибка")
    void exception() throws GitLabApiException {
        when(mockGroupApi.getGroups()).thenThrow(new GitLabApiException("test"));
        softly.assertThatThrownBy(() -> gitlabClient.getGroups())
            .isInstanceOf(RuntimeException.class)
            .getCause()
            .isInstanceOf(GitLabApiException.class)
            .message().isEqualTo("test");
        verify(gitLabApi).getGroupApi();
        verify(mockGroupApi).getGroups();
    }

}