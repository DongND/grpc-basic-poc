package com.act.infinity.calculator.client;

import com.act.infinity.calculator.CalculatorServiceGrpc;
import com.act.infinity.calculator.FindMaximumRequest;
import com.act.infinity.calculator.FindMaximumResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class GrpcClient2 {

  public static void main(String[] args) {
    System.out.println("Starting GRPC client 2");
    ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8080 ).usePlaintext().build();

    CalculatorServiceGrpc.CalculatorServiceStub asyncClient = CalculatorServiceGrpc.newStub(channel);

    CountDownLatch latch = new CountDownLatch(1);

    StreamObserver<FindMaximumRequest> requestStreamObserver = asyncClient.findMaximum(
        new StreamObserver<FindMaximumResponse>() {
          public void onNext(FindMaximumResponse findMaximumResponse) {
              System.out.println("Got new maximum from server: " + findMaximumResponse.getMaximum());
          }

          public void onError(Throwable throwable) {
              latch.countDown();
          }

          public void onCompleted() {
              System.out.println("Server sent response done");
          }
        });

    Arrays.asList(3, 5, 17, 9, 8, 30, 12, 3, 5, 17, 9, 8, 30, 12).forEach(number ->{
      System.out.println("Sending number: " + number);
      requestStreamObserver.onNext(FindMaximumRequest.newBuilder()
          .setNumber(number).build());

      try {
        Thread.sleep(500);
      } catch (Exception ex){
        ex.printStackTrace();
      }
    });

    requestStreamObserver.onCompleted();

    try {
      latch.await(3, TimeUnit.SECONDS);
    } catch (InterruptedException e){
      e.printStackTrace();
    }


    System.out.println("Shutting down the channel.");
    channel.shutdown();
  }

}
