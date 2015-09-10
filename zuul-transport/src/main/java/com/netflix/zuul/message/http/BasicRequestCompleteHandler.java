package com.netflix.zuul.message.http;

import com.google.inject.Inject;
import com.netflix.zuul.context.SessionContext;
import com.netflix.zuul.stats.RequestMetricsPublisher;
import rx.Observable;

import javax.annotation.Nullable;

/**
 * User: michaels@netflix.com
 * Date: 6/4/15
 * Time: 4:26 PM
 */
public class BasicRequestCompleteHandler implements RequestCompleteHandler
{
    @Inject @Nullable
    private RequestMetricsPublisher requestMetricsPublisher;

    @Override
    public Observable<Void> handle(HttpResponseMessage response)
    {
        SessionContext context = response.getContext();

        // Publish request-level metrics.
        if (requestMetricsPublisher != null) {
            requestMetricsPublisher.collectAndPublish(context);
        }

        return Observable.empty();
    }
}
