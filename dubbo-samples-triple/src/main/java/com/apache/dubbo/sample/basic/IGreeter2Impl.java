/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.apache.dubbo.sample.basic;

import org.apache.dubbo.rpc.RpcContext;

public class IGreeter2Impl implements IGreeter2 {
    @Override
    public String sayHello0(String request) {
        StringBuilder respBuilder = new StringBuilder(request);
        for (int i = 0; i < 20; i++) {
            respBuilder.append(respBuilder);
        }
        request = respBuilder.toString();
        return request;
    }

    @Override
    public String sayHelloException(String request) {
        throw new RuntimeException("Biz exception");
    }

    @Override
    public String sayHelloWithAttachment(String request) {
        System.out.println(RpcContext.getServerAttachment().getObjectAttachments());
        RpcContext.getServerContext().setAttachment("str", "str")
                .setAttachment("integer", 1)
                .setAttachment("raw", new byte[]{1, 2, 3, 4});
        return "hello," + request;
    }
}
