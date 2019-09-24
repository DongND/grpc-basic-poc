package com.act.infinity.calculator.server;

import com.act.infinity.calculator.CalculatorServiceGrpc;
import com.act.infinity.calculator.FindMaximumRequest;
import com.act.infinity.calculator.FindMaximumResponse;
import io.grpc.stub.StreamObserver;

public class CalculatorServiceImpl extends CalculatorServiceGrpc.CalculatorServiceImplBase {

  @Override
  public StreamObserver<FindMaximumRequest> findMaximum(
      final StreamObserver<FindMaximumResponse> responseObserver) {
      return new StreamObserver<FindMaximumRequest>() {

        int currentMax = 0;

        public void onNext(FindMaximumRequest findMaximumRequest) {
          int value = findMaximumRequest.getNumber();

          if (value > currentMax){
            currentMax = value;
            responseObserver.onNext(FindMaximumResponse.newBuilder()
                .setMaximum(currentMax)
                .build());
          } else {
            //do Nothing
          }
        }

        public void onError(Throwable throwable) {
            responseObserver.onCompleted();
        }

        public void onCompleted() {
          // send the current last maximum
          responseObserver.onNext(FindMaximumResponse.newBuilder()
              .setMaximum(currentMax)
              .build());
          // the server is done sending data
          responseObserver.onCompleted();

        }
      };
  }
}
