package com.ok.integration;

import org.springframework.integration.core.MessageSelector;
import org.springframework.messaging.Message;

import java.util.List;

enum CompositeType {
    AND, OR
}

public class CompositeFilter implements MessageSelector {
    private final List<MessageSelector> filters;
    private final CompositeType type;

    public CompositeFilter(List<MessageSelector> filters, CompositeType type) {
        this.filters = filters;
        this.type = type;
    }

    @Override
    public boolean accept(Message<?> message) {
        return type == CompositeType.AND ?
                filters.stream().allMatch(filter -> filter.accept(message)) :
                filters.stream().anyMatch(filter -> filter.accept(message));
    }
}

class ContainsKeywordFilter implements MessageSelector {
    private final String keyword;

    ContainsKeywordFilter(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public boolean accept(Message<?> message) {
        return message.getPayload().toString().contains(keyword);
    }
}

class MinimumLengthFilter implements MessageSelector {
    private final int minLen;

    MinimumLengthFilter(int minLen) {
        this.minLen = minLen;
    }

    @Override
    public boolean accept(Message<?> message) {
        return message.getPayload().toString().length() > minLen;
    }
}
