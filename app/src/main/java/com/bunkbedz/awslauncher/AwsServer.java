/*  AwsLauncher
    Copyright (C) 2020  Amit Zohar
    For contact: amitzohar42@gmail.com

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.*/
package com.bunkbedz.awslauncher;

import android.os.SystemClock;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
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
