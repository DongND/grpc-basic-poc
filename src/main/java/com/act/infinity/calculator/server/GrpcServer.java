package com.act.infinity.calculator.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.io.IOException;



public class GrpcServer {
    public static void main(String[] args) throws IOException, InterruptedException {
      System.out.println("Starting GRPC server");
      Server server = ServerBuilder.forPort(8080)
                                    .addService(new CalculatorServiceImpl())
                                    .build()
                                    .start();
      server.awaitTermination();
    }
}
