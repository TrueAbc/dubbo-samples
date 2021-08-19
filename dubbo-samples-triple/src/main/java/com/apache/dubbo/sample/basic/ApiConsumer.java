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

import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.stream.StreamObserver;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import org.apache.dubbo.hello.HelloReply;
import org.apache.dubbo.hello.HelloRequest;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ApiConsumer {
    private static IGreeter iGreeter;

    public static void main(String[] args) throws IOException {
        ReferenceConfig<IGreeter> ref = new ReferenceConfig<>();
        ref.setInterface(IGreeter.class);
        ref.setCheck(false);
        ref.setInterface(IGreeter.class);
        ref.setCheck(false);
        ref.setProtocol(CommonConstants.TRIPLE);
        ref.setLazy(true);
        ref.setTimeout(100000);

        DubboBootstrap bootstrap = DubboBootstrap.getInstance();
        bootstrap.application(new ApplicationConfig("demo-consumer"))
                .registry(new RegistryConfig("zookeeper://127.0.0.1:2181"))
                .reference(ref)
                .start();

        iGreeter = ref.get();

        System.out.println("dubbo ref started");
//        unaryHello();
//        sayHelloException();
//        stream();
        serverStream();
        System.in.read();
    }

    public static void serverStream() {
        iGreeter.sayHelloServerStream(HelloRequest.newBuilder()
                .setName("request")
                .build(), new StreamObserver<HelloReply>() {
            @Override
            public void onNext(HelloReply data) {
                System.out.println("stream reply:" + data);
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onCompleted() {
                System.out.println("stream done");
            }
        });
    }

    public static void stream() {
        final StreamObserver<HelloRequest> request = iGreeter.sayHelloStream(new StreamObserver<HelloReply>() {
            @Override
            public void onNext(HelloReply data) {
                System.out.println("stream reply:" + data);
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onCompleted() {
                System.out.println("stream done");
            }
        });
        for (int i = 0; i < 10; i++) {
            request.onNext(HelloRequest.newBuilder()
                    .setName("request")
                    .build());
        }
        request.onCompleted();
    }

    public static void unaryHello() {
        try {
            final HelloReply reply = iGreeter.sayHello(HelloRequest.newBuilder()
                    .setName("name")
                    .build());
            TimeUnit.SECONDS.sleep(1);
            System.out.println("Reply:" + reply);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static void sayHelloException() {
        try {
            final HelloReply reply = iGreeter.sayHelloException(HelloRequest.newBuilder()
                    .setName("name")
                    .build());
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
