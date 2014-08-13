/**
 * Copyright 2014 Netflix, Inc.
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
package com.netflix.zuul;

import io.netty.buffer.ByteBuf;
import io.reactivex.netty.protocol.http.server.HttpServerResponse;

import java.util.Map;

public class EgressResponse {
    private final HttpServerResponse<ByteBuf> nettyResponse;

    private EgressResponse(HttpServerResponse<ByteBuf> nettyResponse) {
        this.nettyResponse = nettyResponse;
    }

    public static EgressResponse from(HttpServerResponse<ByteBuf> nettyResp) {
        return new EgressResponse(nettyResp);
    }

    public void addHeader(String name, String value) {
        nettyResponse.getHeaders().addHeader(name, value);
    }

    public EgressResponse copyFrom(IngressResponse ingressResp) {
        nettyResponse.setStatus(ingressResp.getStatus());

        for (Map.Entry<String, String> entry: ingressResp.getHeaders().entries()) {
            addHeader(entry.getKey(), entry.getValue());
        }

        if (ingressResp.containsContent()) {
            nettyResponse.write(ingressResp.getByteBuf());
        }
        return this;
    }
}
