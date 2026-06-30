package com.nhnacademy.data4library.config;

import com.nhnacademy.data4library.tool.BookTools;
import com.nhnacademy.data4library.tool.LibraryTools;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class McpConfig {

    @Bean
    public ToolCallbackProvider customToolCallbackProvider(BookTools bookTools,
                                                           LibraryTools libraryTools) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(bookTools, libraryTools)
                .build();
    }
}