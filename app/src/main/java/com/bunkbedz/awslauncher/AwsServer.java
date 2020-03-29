package com.bunkbedz.awslauncher;

import android.os.SystemClock;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.internal.LazyAwsCredentialsProvider;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.DescribeInstancesRequest;
import software.amazon.awssdk.services.ec2.model.DescribeInstancesResponse;
import software.amazon.awssdk.services.ec2.model.InstanceStateName;
import software.amazon.awssdk.services.ec2.model.StartInstancesRequest;
import software.amazon.awssdk.services.ec2.model.StopInstancesRequest;

public class AwsServer {
    private final String instanceId;
    private Ec2Client ec2;

    public AwsServer(String instanceId, Region region, String accessKeyId, String secretAccessKey) {
        this.instanceId = instanceId;
        ec2 = Ec2Client.builder()
                .httpClientBuilder(UrlConnectionHttpClient.builder())
                .region(region)
                .credentialsProvider(() -> AwsBasicCredentials.create(accessKeyId, secretAccessKey))
                .build();

    }

    public void startServer() {
        StartInstancesRequest startServerRequest = StartInstancesRequest.builder()
                .instanceIds(instanceId)
                .build();

        ec2.startInstances(startServerRequest);

        do {
            SystemClock.sleep(3000);
        } while (getInstanceStateName() != InstanceStateName.RUNNING);
    }

    public void stopServer() {
        StopInstancesRequest stopInstancesRequest = StopInstancesRequest.builder()
                .instanceIds(instanceId)
                .build();

        ec2.stopInstances(stopInstancesRequest);

        do {
            SystemClock.sleep(3000);
        } while (getInstanceStateName() != InstanceStateName.STOPPED);
    }


    public InstanceStateName getInstanceStateName() {
        DescribeInstancesRequest instanceStateRequest = DescribeInstancesRequest.builder()
                .instanceIds(instanceId)
                .build();

        DescribeInstancesResponse instanceStateResponse = ec2.describeInstances(instanceStateRequest);
        return instanceStateResponse.reservations().get(0).instances().get(0).state().name();
    }
}
